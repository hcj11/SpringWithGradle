package org.apache.hc.client5.http.examples.async;

import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.io.CloseMode;

/**
 * A simple example that uses HttpClient to execute an HTTP request against
 * a target site that requires user authentication.
 */
public class AsyncClientAuthentication {

    public static void main(final String[] args) throws Exception {
        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope("httpbin.org", 80),
                new UsernamePasswordCredentials("user", "passwd".toCharArray()));
        final CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        httpclient.start();

        final SimpleHttpRequest request = SimpleRequestBuilder.get("http://httpbin.org/basic-auth/user/passwd")
                .build();

        System.out.println("Executing request " + request);
        final Future<SimpleHttpResponse> future = httpclient.execute(
                SimpleRequestProducer.create(request),
                SimpleResponseConsumer.create(),
                new FutureCallback<SimpleHttpResponse>() {

                    @Override
                    public void completed(final SimpleHttpResponse response) {
                        System.out.println(request + "->" + new StatusLine(response));
                        System.out.println(response.getBody());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        System.out.println(request + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        System.out.println(request + " cancelled");
                    }

                });
        future.get();

        System.out.println("Shutting down");
        httpclient.close(CloseMode.GRACEFUL);

    }
}