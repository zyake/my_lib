package my.lib.data.jdbc.it;

import my.lib.TestResourceUtil;
import my.lib.data.DataAccessException;
import my.lib.data.DataMapper;
import my.lib.data.jdbc.CategoryConfig;
import my.lib.data.jdbc.JdbcDataMapperFactory;
import my.lib.data.jdbc.JdbcDataMapperFactoryTest;
import my.lib.data.jdbc.TestDataSourceUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JdbcDataMapperIT {

    private static final String PACKAGE_PATH = TestResourceUtil.getPackagePath(JdbcDataMapperIT.class);

    private  DataSource dataSource = TestDataSourceUtil.createDataSource("test/DataMapper");

    @Before
    public void before() throws SQLException {
        // 古いH2databaseのファイルを削除する。
        File file = new File("test/DataMapper.h2.db");
        if ( file.exists() ) {
            file.delete();
        }

        TestDataSourceUtil.executeQuery(dataSource.getConnection(),
                "CREATE TABLE CategoryConfig(Id INT, Config_Name VARCHAR(20))");
    }

    @After
    public void after() throws SQLException {
        TestDataSourceUtil.executeQuery(dataSource.getConnection(),
                "DROP TABLE CategoryConfig");
    }

    @Test
    public void testQueryObjects_success() throws Exception {
        TestDataSourceUtil.executeQuery(dataSource.getConnection(),
                "INSERT INTO CategoryConfig(Id, Config_Name) VALUES(1, 'TEST1'), (2, 'TEST2'), (3, 'TEST3')");

        DataMapper dataMapper = JdbcDataMapperFactory.createDataMapperFromFile(
                "test/" + PACKAGE_PATH + "jdbc-datamapper_success.xml");

        Map<String,Object> configMap = new HashMap<String, Object>();
        configMap.put("Id", 1);
        List<JdbcDataMapperFactoryTest.CategoryConfig> categoryConfigs = dataMapper.queryObjects("select-config", configMap);

        assertThat(categoryConfigs.toString(),
                is("[{ id=2, config name=TEST2 }, { id=3, config name=TEST3 }]"));
    }

    @Test(expected = DataAccessException.class)
    public void testQueryObjects_failure() throws Exception {
        DataSource dataSource = TestDataSourceUtil.createDataSource("test/DataMapper");
        TestDataSourceUtil.executeQuery(dataSource.getConnection(),
                "INSERT INTO CategoryConfig(Id, Config_Name) VALUES(1, 'TEST1'), (2, 'TEST2'), (3, 'TEST3')");

        DataMapper dataMapper = JdbcDataMapperFactory.createDataMapperFromFile(
                "test/" + PACKAGE_PATH + "jdbc-datamapper_failure.xml");

        Map<String,Object> configMap = new HashMap<String, Object>();
        configMap.put("Id", 1);
        dataMapper.queryObjects("select-config", configMap);
    }

    @Test
    public void testInsertObjects_success() throws Exception {
        DataMapper dataMapper = JdbcDataMapperFactory.createDataMapperFromFile(
                "test/" + PACKAGE_PATH + "jdbc-datamapper_success.xml");

        List<CategoryConfig> categoryConfigs = new ArrayList<CategoryConfig>();
        Collections.addAll(categoryConfigs,
                new CategoryConfig("TEST1", 1),
                new CategoryConfig("TEST2", 2));

        dataMapper.insertObjects("insert-config", categoryConfigs);

        ResultSet resultSet = TestDataSourceUtil.select(dataSource.getConnection(),
                "SELECT ID, CONFIG_NAME FROM CategoryConfig");
        StringBuilder stringBuilder = new StringBuilder();
        while ( resultSet.next() ) {
            stringBuilder
                .append("{id=" + resultSet.getInt("ID"))
                .append(",")
                .append("CONFIG_NAME=" + resultSet.getString("CONFIG_NAME"))
                .append("}");
        }

        assertThat(stringBuilder.toString(),
                is("{id=1,CONFIG_NAME=TEST1}{id=2,CONFIG_NAME=TEST2}"));
    }

    @Test(expected = DataAccessException.class)
    public void testInsertObjects_failure() throws Exception {
        DataMapper dataMapper = JdbcDataMapperFactory.createDataMapperFromFile(
                "test/" + PACKAGE_PATH + "jdbc-datamapper_failure.xml");

        List<CategoryConfig> categoryConfigs = new ArrayList<CategoryConfig>();
        Collections.addAll(categoryConfigs,
                new CategoryConfig("TEST1", 1),
                new CategoryConfig("TEST2aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 2));

        dataMapper.insertObjects("insert-config", categoryConfigs);
    }
}
