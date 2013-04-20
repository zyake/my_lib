package my.lib;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.regex.Pattern;

public class ContainsMatcher extends BaseMatcher<String> {

    private String text;

    private boolean result;

    private String target;

    public ContainsMatcher(String pattern) {
        this.text = pattern.replaceAll("\n", "");
    }

    @Override
    public boolean matches(Object o) {
        this.target = o.toString().replaceAll("\n", "");
        result = target.contains(text);

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
                "pattern unmatched: result=" + result +", pattern=" + text + ", target=" + target);
    }
}
