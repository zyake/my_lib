package my.lib.data.jdbc;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class UnderScoreSetterResolverTest {

    @Test
    public void testResolveSetter_success() throws Exception {
        Method method = new UnderScoreSetterResolver().resolveSetter("next_year", Stub.class);

        assertThat(method.toString(),
                is("public void my.lib.data.jdbc.UnderScoreSetterResolverTest$Stub.setNextYear(java.lang.String)"));
    }

    public class Stub {

        private String name;

        public void setNextYear(String name) {
            this.name = name;
        }
    }
}
