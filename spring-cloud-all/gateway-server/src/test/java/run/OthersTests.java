package run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OthersTests {
    @Test
    public void exgex(){
//        /foo/(?<id>\d.*)  -> ${id}
        String regex= "/foo/(?<id>\\d.*)";
        String input ="/foo/123";
        Pattern compile = Pattern.compile("/foo/(?<id>\\d.*)");
        Matcher matcher = compile.matcher("/foo/123");
        Assertions.assertTrue(matcher.find());

        String s = input.replaceAll(regex, "${id}");
        Assertions.assertEquals(s,"123");


    }

    @Test
    public void antMatcher() {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        Assertions.assertFalse(antPathMatcher.match("/gateway/**", "/actuator/gateway/routes"));
        Assertions.assertFalse(antPathMatcher.match("/gateway/**", "actuator/gateway/routes"));
        Assertions.assertTrue(antPathMatcher.match("/actuator/gateway/**", "/actuator/gateway/routes"));
        Assertions.assertFalse(antPathMatcher.match("/actuator/gateway/**", "actuator/gateway/routes"));
        System.out.println();

    }

    @Test
    public void uri() throws MalformedURLException, URISyntaxException {
        URL url = new URL("file://F:/integration/spring-all/spring-framework-5.2.6.RELEASE/spring-webflux");

        URI uri = url.toURI();
        URL url1 = uri.toURL();
        //mail:list:252@qq.com


        new URI("");
    }
}
