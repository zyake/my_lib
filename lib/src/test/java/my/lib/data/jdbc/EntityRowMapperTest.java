package my.lib.data.jdbc;

import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EntityRowMapperTest {

    @Test
    public void testMapRow_success() throws Exception {
        DataSource dataSource = TestDataSourceUtil.createOnMemoryDataSource();
        Connection connection = dataSource.getConnection();

        TestDataSourceUtil.executeQuery(connection,
                "CREATE TABLE employees(name VARCHAR(10), birthday TIMESTAMP)");
        TestDataSourceUtil.executeQuery(connection,
                "INSERT INTO employees(name, birthday) VALUES('TEST1', '2013-03-23')");

        EntityRowMapper entityRowMapper = new EntityRowMapper();
        Map<String,Object> configMap = new HashMap<String, Object>();
        configMap.put(EntityRowMapper.CONFIG_SETTERRESOLVER_FQCN, UnderScoreSetterResolver.class.getName());
        entityRowMapper.initialize(configMap);

        ResultSet resultSet = TestDataSourceUtil.select(connection, "SELECT name, birthday FROM employees");
        resultSet.next();
        Employee employee = entityRowMapper.mapRow(resultSet, Employee.class);

        assertThat(employee.toString(), is("{ name=TEST1, birthday=2013-03-23 00:00:00.0 }"));
    }

    public static class Employee {

        private String name;

        private Date birthday;

        public void setName(String name) {
            this.name = name;
        }

        public void setBirthday(Date date) {
            this.birthday = date;
        }

        @Override
        public String toString() {
            return "{ name=" + name + ", birthday=" + birthday + " }";
        }
    }
}
