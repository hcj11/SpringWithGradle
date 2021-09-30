package org.apache.hc.client5.http.examples;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * This example demonstrates the use of a local HTTP context populated with
 * custom attributes.
 */
public class ClientCustomContext {

    public static void main(final String[] args) throws Exception {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // Create a local instance of cookie store
            final CookieStore cookieStore = new BasicCookieStore();

            BasicClientCookie basicClientCookie = new BasicClientCookie("id", String.valueOf(UUID.randomUUID()));
            basicClientCookie.setDomain("baidu.com");
            Date from = Date.from(Instant.now().plus(60 * 60 * 24, ChronoUnit.SECONDS));
            basicClientCookie.setExpiryDate(from);
            basicClientCookie.setPath("*");
            cookieStore.addCookie(basicClientCookie);

            // Create local HTTP context
            final HttpClientContext localContext = HttpClientContext.create();
            // Bind custom cookie store to the local context
            localContext.setCookieStore(cookieStore);

            final HttpGet httpget = new HttpGet("http://httpbin.org/cookies");
            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

            // Pass local context as a parameter
            try (final CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("Local cookie: " + cookies.get(i));
                }
                EntityUtils.consume(response.getEntity());
            }
        }
    }

}