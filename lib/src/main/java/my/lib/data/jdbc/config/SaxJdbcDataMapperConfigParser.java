package my.lib.data.jdbc.config;

import my.lib.data.DataAccessException;
import my.lib.util.StringUtil;
import my.lib.util.io.PropertiesUtil;
import my.lib.util.xml.SaxUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SaxJdbcDataMapperConfigParser implements JdbcDataMapperConfigParser {

    @Override
    public JdbcDataMapperConfig parseConfig(InputStream inputStream) {
        InternalHandler internalHandler = new InternalHandler();
        SaxUtil.parseWithSAX(inputStream, internalHandler);
        JdbcDataMapperConfig mapperConfig = createMapperConfig(internalHandler);

        return mapperConfig;
    }

    private JdbcDataMapperConfig createMapperConfig(InternalHandler internalHandler) {
        Map<String, SqlConfig> sqlConfigs = new HashMap<String, SqlConfig>();
        for ( String key : internalHandler.sqlQueries.keySet() ) {
            StringBuilder sqlQuery = internalHandler.sqlQueries.get(key);
            Map<String, String> attrMap = internalHandler.sqlAttrs.get(key);
            String normalizedSqlQuery = StringUtil.normalize(sqlQuery.toString());
            SqlConfig sqlConfig = new SqlConfig(attrMap.get("id"), attrMap, normalizedSqlQuery);
            sqlConfigs.put(sqlConfig.getId(), sqlConfig);
        }

        Properties properties = PropertiesUtil.loadFromString(internalHandler.globalConfig.toString());
        Map<String, Object> globalConfig = new HashMap<String, Object>();
        for ( Object key : properties.keySet() ) {
            Object value = properties.get(key);
            globalConfig.put(key.toString(), value);
        }

        return new JdbcDataMapperConfig(globalConfig, sqlConfigs);
    }

    /**
     * 設定ファイル「jdbc-datamapper.xml」をパースする。
     *
     * 要素「sql」については、パーサの実装によって、使用可能な属性が異なるため、
     * 属性「id」による一意性の確認のみを行う。
     */
    private class InternalHandler extends DefaultHandler {

        private StringBuilder globalConfig = new StringBuilder();

        private Map<String, Map<String, String>> sqlAttrs = new HashMap<String, Map<String, String>>();

        private Map<String, StringBuilder> sqlQueries = new HashMap<String, StringBuilder>();

        private String currentQueryId = "";

        private Positions currentPos = Positions.None;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ( "global".equals(qName) ) {
                currentPos = Positions.OnGlobal;
            } else if ( "sql".equals(qName) ) {
                currentPos = Positions.OnSql;

                String id = attributes.getValue("id");
                boolean idRequired = id == null;
                if ( idRequired ) {
                    throw new DataAccessException("要素「sql」の必須属性「id」が指定されていません。");
                }

                boolean idDuplicated = sqlQueries.containsKey(id);
                if ( idDuplicated ) {
                    throw new DataAccessException("要素「sql」の必須属性「id」は一意でなければなりません: id=" + id);
                }

                Map<String, String> attrMap = new HashMap<String, String>();
                for ( int i = 0 ; i < attributes.getLength() ; i ++ ) {
                    String attrKey = attributes.getQName(i);
                    String attrValue = attributes.getValue(i);
                    attrMap.put(attrKey, attrValue);
                }
                sqlAttrs.put(id, attrMap);

                sqlQueries.put(id, new StringBuilder());
                currentQueryId = id;
            } else {
                currentPos = Positions.None;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            char[] partialChars = Arrays.copyOfRange(ch, start, start + length);
            String partialText = new String(partialChars);
            switch ( currentPos ) {
                case OnGlobal:
                    globalConfig.append(partialText);
                break;
                case OnSql:
                    StringBuilder stringBuilder = sqlQueries.get(currentQueryId);
                    stringBuilder.append(partialText);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch ( currentPos ) {
                case OnGlobal:
                case OnSql:
                    currentPos = Positions.None;
            }
        }
    }

    private enum Positions {
        OnGlobal, OnSql, None
    }
}
