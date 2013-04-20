package my.lib.data.jdbc.config;

import my.lib.data.DataAccessException;
import my.lib.data.jdbc.NamedParameterSqlExpression;
import my.lib.data.jdbc.SqlExpression;
import my.lib.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;

public class NamedParameterSqlExpressionParser implements SqlExpressionParser {

    @Override
    public SqlExpression parseExpression(SqlConfig config) {
        String target = config.getAttributes().get("target");
        boolean targetRequired = target == null;
        if ( targetRequired ) {
            throw new DataAccessException("必須属性「target」が指定されていません。");
        }

        Class targetClass = ClassUtil.classForName(target, Object.class);

        String strategy = config.getAttributes().get("strategy");
        boolean strategyRequired = strategy == null;
        if ( strategyRequired ) {
            throw new DataAccessException("必須属性「strategy」が指定されていません。");
        }

        ParsedExpression parsedExpression = doParseExpression(config.getSqlQuery());

        SqlExpression sqlExpression = createNamedExpression(config.getId(), targetClass, strategy, parsedExpression);

        return sqlExpression;
    }

    protected SqlExpression createNamedExpression(String id, Class target, String strategy, ParsedExpression parsedExpression) {
        return new NamedParameterSqlExpression(
                id, parsedExpression.parameters, parsedExpression.parsedQuery.toString(), strategy, target);
    }

    private ParsedExpression doParseExpression(String sqlQuery) {
        List<String> parameters = new ArrayList<String>();
        StringBuilder parsedQuery = new StringBuilder();
        Positions currentPos = Positions.OnOther;
        for ( int i = 0 ; i < sqlQuery.length() ; i ++ ) {
            char currentChar = sqlQuery.charAt(i);
            switch ( currentPos ) {
                case OnOther:
                boolean  startDoubleQuote = '"' == currentChar;
                if ( startDoubleQuote ) {
                    currentPos = Positions.OnDoubleQuoted;
                    parsedQuery.append(currentChar);
                    continue;
                }

                boolean startOnSingleQuote = '\'' == currentChar;
                if ( startOnSingleQuote ) {
                    currentPos = Positions.OnSingleQuoted;
                    parsedQuery.append(currentChar);
                    continue;
                }

                boolean startComment = '/' == currentChar;
                if ( startComment ) {
                    boolean isValidComment =
                            i + 1 < sqlQuery.length() && '*' == sqlQuery.charAt(i + 1);
                    if ( ! isValidComment ) {
                        throw new DataAccessException("SQLのコメント形式が不正です。");
                    }
                    currentPos = Positions.OnComment;
                    parsedQuery.append(currentChar);
                    continue;
                }

                boolean startParameter = '{' == currentChar;
                if ( startParameter ) {
                    // read until end parameter clause '}'
                    int paramEndPos = i;
                    while ( sqlQuery.charAt(paramEndPos) != '}' && paramEndPos < sqlQuery.length() ) {
                        paramEndPos ++;
                    }
                    boolean endParamRequired = i == sqlQuery.length() - 1;
                    if ( endParamRequired ) {
                        throw new DataAccessException("パラメータの終了文字「}」が見つかりませんでした。");
                    }

                    String parameter = sqlQuery.substring(i + 1, paramEndPos);
                    parameters.add(parameter);
                    parsedQuery.append("?"); // replace with JDBC place holder
                    i  = paramEndPos;

                    continue;
                }

                parsedQuery.append(currentChar);
                break;

                case OnSingleQuoted:
                    boolean isInQuotedText = '\'' != currentChar;
                    if (  isInQuotedText ) {
                        parsedQuery.append(currentChar);
                        continue;
                    }

                    boolean escapeFound = i + 1 < sqlQuery.length() && '\'' == sqlQuery.charAt(i + 1);
                    if ( escapeFound ) {
                        parsedQuery.append("''");
                        i ++;
                        continue;
                    }
                    parsedQuery.append(currentChar);
                    currentPos = Positions.OnOther;

                break;

                case OnDoubleQuoted:
                    boolean isInDoubleQuotedText = '"' != currentChar;
                    if ( isInDoubleQuotedText ) {
                        parsedQuery.append(currentChar);
                        continue;
                    }

                    boolean doubleQuotedEscapeFound = i + 1 < sqlQuery.length() && '"' == sqlQuery.charAt(i + 1);
                    if ( doubleQuotedEscapeFound ) {
                        parsedQuery.append("\"\"");
                        i ++;
                        continue;
                    }
                    parsedQuery.append(currentChar);
                    currentPos = Positions.OnOther;

                break;

                case OnComment:
                    boolean isEndComment =
                            '*' == currentChar && i + 1 < sqlQuery.length() && '/' == sqlQuery.charAt(i + 1);
                    if ( isEndComment ) {
                        currentPos = Positions.OnOther;
                        parsedQuery.append("*/");
                        i ++;

                        continue;
                    }

                    parsedQuery.append(currentChar);

                break;
            }
        }

        ParsedExpression parsedExpression = new ParsedExpression(parsedQuery, parameters);

        return parsedExpression;
    }

    private class ParsedExpression {

        private StringBuilder parsedQuery;

        private List<String> parameters;

        private ParsedExpression(StringBuilder parsedQuery, List<String> parameters) {
            this.parsedQuery = parsedQuery;
            this.parameters = parameters;
        }
    }

    enum Positions {
        OnOther, OnDoubleQuoted, OnSingleQuoted, OnComment
    }
}
