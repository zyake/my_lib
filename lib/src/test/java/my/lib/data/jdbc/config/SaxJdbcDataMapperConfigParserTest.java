package my.lib.data.jdbc.config;

import my.lib.TestResourceUtil;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class SaxJdbcDataMapperConfigParserTest {

    private static final String PACKAGE_PATH =
            TestResourceUtil.getPackagePath(SaxJdbcDataMapperConfigParser.class);

    @Test
    public void testParseConfig_success() throws Exception {
        InputStream inputStream = TestResourceUtil.loadStream(PACKAGE_PATH + "jdbc-datamapper_success.xml");
        JdbcDataMapperConfig mapperConfig = new SaxJdbcDataMapperConfigParser().parseConfig(inputStream);

        assertThat(mapperConfig.toString(),
                is("{ global={datamapper.jdbc.sqlexpressionparser.fqcn=my.apps.phrasesearcher.dataaccess.jdbc.NamedParameterSqlExpressionParser, " +
                   "datamapper.jdbc.mapping.strategies.entity.fqcn=my.apps.phrasesearcher.dataaccess.jdbc.EntityRowMapper, " +
                   "datamapper.jdbc.mapping.strategies.entity.setterresolver.fqcn=my.apps.phrasesearcher.dataaccess.jdbc.UnderScoreSetterResolver," +
                   " datamapper.jdbc.mapperfqcn=my.apps.phrasesearcher.dataaccess.jdbc.JdbcDataMapper}, sqls={" +
                   "select-config={ id=select-config, sql=SELECT DataAccess, Logging, Writer             FROM CategoryConfig            WHERE ConfigName = {ConfigName}, " +
                   "attrs={id=select-config, target=CategoryConfig, strategy=entity} }} }"));
    }


}
