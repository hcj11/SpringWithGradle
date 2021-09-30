package org.apache.http.client;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.nacos.common.utils.MD5Utils;
import com.alibaba.nacos.common.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.async.methods.AbstractCharResponseConsumer;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.nio.AsyncRequestProducer;
import org.apache.hc.core5.http.nio.support.AsyncRequestBuilder;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class HttpClientTest {
    @Test
    public void hexTest2() {
        String s0 = Integer.toString(-1, 16);
        int i1 = Integer.parseInt(s0, 16);

        // ff  100  100000000
        String s1 = Integer.toString(255, 16);
        // ff , 16
        int i = Integer.parseInt(s1, 16);
        Assert.assertEquals(255, i);
        String s2 = Integer.toString(255, 2);
        Assert.assertEquals(Integer.parseInt(s2, 2), 255);
        String s3 = Integer.toString(256, 16);
        Assert.assertEquals(Integer.parseInt(s3, 16), 256);
        int a = -1;
        String s = Integer.toBinaryString(a);
        //  0x07fffffff;
        // 11111111111111111111111111111111
        int i2 = Integer.MAX_VALUE;
        String s4 = Integer.toString(i2, 16);
        // 16 = F 100  1 11111111111111111111111111
        // unsignedInteger value range from  -2^31 ~ 2^31 -1
        log.info("{},{},{}", s1, s, i2);
        // -2 ^ 31
        long l = Long.parseLong(String.valueOf(Integer.MIN_VALUE));

        String pow = Long.toBinaryString((long) Math.pow(2, 32));

    }

    @Test
    public void hexTest1() {
        long e = -256;
        int i = Long.bitCount(256);
        long l = Long.parseLong(String.valueOf(e), 10);
        // 11111111111111111 >>>4
        String s1 = Long.toBinaryString(-1); // -1
        String s = Long.toBinaryString(-1 >>> 4); // -1
        log.info("{},{},{},{},{}", s1, s, i, l, e >>> 4);

        // 16 * 16 = 256  2^8
        // 0001 0000 0000
    }

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    public void hexTest3() {
        String json = "低位字的两个高位";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        int l = bytes.length;
        char[] out = new char[l << 1];

        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & bytes[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & bytes[i]];
        }

    }

    /**
     *
     */
    @Test
    public void hexTest() {
      // 32 位 转 16 位

        String json = "低位字的两个高位";
        // 474e9de94327d238497071a2df24fea5
        // 474e9de94327d238497071a2df24fea5
        String s = null;
        try {
            s = MD5Utils.md5Hex(json.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("{}", s);

        // 0 - 6 - 8 -12
        long a = 8;
        a = a << 1;
        log.info("{},{}", a, (a >> 1) == 8);

        long b = 14; // 12 / 1.5 = 8
        b = b >> 1;
        log.info("{},{}", b, (b << 1) == 14);

        long c = 1; //
        c = c >> 16; // 0
        log.info("{},{}", c, c == 0);

    }

    @Test
    public void timestamp() {
        UuidUtils.generateUuid();

        long aLong = LocalDateTime.now().getLong(ChronoField.MILLI_OF_SECOND);
        long aLong1 = LocalDateTime.now().getLong(ChronoField.MILLI_OF_DAY);
        log.info("{},{}", aLong, aLong1);
        // ZoneOffset
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        log.info("{}", l); // 1631441280 1631441280000
    }

    @Test
    public void uuidTest2() {
        // ThreadLocalRandom  thread safe
        ThreadLocalRandom.current().nextInt();
        UUID uuid1 = UUID.randomUUID(true);
        UUID uuid2 = UUID.randomUUID(false);
//        log.info("{},{},{},{}",uuid1.clockSequence(),uuid1.node(),uuid1.timestamp(),uuid1.variant());
        UUID uuid = UUID.nameUUIDFromBytes("".getBytes(StandardCharsets.UTF_8));
        log.info("{}", uuid1);
        UUID uuid3 = UUID.fastUUID();
    }

    @Test
    public void uuidTest1() {
        // ff  * 3 = 8  * 3 =24 + 4 + 4 = 24  +8 =32
//        Integer i = Integer.MAX_VALUE & Integer.MAX_VALUE;
        //  2 ^ 6  =64 +1   01000001  2>>32
        long val = (long) Math.pow(2, 32);
        int digits = 8;
        long hi = 1L << (digits * 4);// 2 ^ 32 * 2
        /**
         * 0000
         * 0000
         */
        long l1 = 0 | (val & (hi - 1));
        long l2 = (val & (hi - 1));
        log.info("==(int)Math.pow(2,32):{},{},{}==", val, l1, l2);
        String substring = Long.toHexString(hi | (val & (hi - 1)));
        log.info("=={}==", substring);
        int MAX_VALUE = 0x7fffffff;
        java.util.UUID uuid = new java.util.UUID(0, 0);
        log.info("{}", uuid.toString());
    }

    @Test
    public void uuidTest() {
        long aLong = LocalDateTime.now().getLong(ChronoField.INSTANT_SECONDS);
        long time = new Date().getTime();
//        int a = 0 << 8;
//        log.info("{}",a); //
//        assert a == (int) Math.pow(2, 8);
        long hi = 1L << (8 * 4);
// 1 ^32   4294967296
        int maxValue = Integer.MAX_VALUE;
        // 2147483647,
        // 32 + 32
        log.info("{},{},{},{}", maxValue, hi, UUID.randomUUID(), UuidUtils.generateUuid());
    }

    @Test
    public void quickStartWithAsyncHttpClient() throws IOException, InterruptedException, ExecutionException {
        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            // Start the client
            httpclient.start();

            // Execute request
            SimpleHttpRequest request1 = SimpleHttpRequests.get("http://httpbin.org/get");
            Future<SimpleHttpResponse> future = httpclient.execute(request1, null);
            // and wait until response is received
            SimpleHttpResponse response1 = future.get();
            System.out.println(request1.getRequestUri() + "->" + response1.getCode());

            // One most likely would want to use a callback for operation result
            CountDownLatch latch1 = new CountDownLatch(1);
            SimpleHttpRequest request2 = SimpleHttpRequests.get("http://httpbin.org/get");
            httpclient.execute(request2, new FutureCallback<SimpleHttpResponse>() {

                @Override
                public void completed(SimpleHttpResponse response2) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response2.getCode());
                }

                @Override
                public void failed(Exception ex) {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }

                @Override
                public void cancelled() {
                    latch1.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }

            });
            latch1.await();

            // In real world one most likely would want also want to stream
            // request and response body content
            CountDownLatch latch2 = new CountDownLatch(1);
            AsyncRequestProducer producer3 = AsyncRequestBuilder.get("http://httpbin.org/get").build();
            AbstractCharResponseConsumer<HttpResponse> consumer3 = new AbstractCharResponseConsumer<HttpResponse>() {

                HttpResponse response;

                @Override
                protected void start(HttpResponse response, ContentType contentType) throws HttpException, IOException {
                    this.response = response;
                }

                @Override
                protected int capacityIncrement() {
                    return Integer.MAX_VALUE;
                }

                @Override
                protected void data(CharBuffer data, boolean endOfStream) throws IOException {
                    data.compact();
                    data.append('1');
                    data.duplicate();
                    data.asReadOnlyBuffer();
                    data.slice();

                    // Do something useful
                }

                @Override
                protected HttpResponse buildResult() throws IOException {
                    return response;
                }

                @Override
                public void releaseResources() {
                }

            };
            httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(HttpResponse response3) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + response3.getCode());
                }

                @Override
                public void failed(Exception ex) {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + "->" + ex);
                }

                @Override
                public void cancelled() {
                    latch2.countDown();
                    System.out.println(request2.getRequestUri() + " cancelled");
                }

            });
            latch2.await();

        }
    }

    @Test
    public void quickStartWithAlbeitLessSimple() {
//        Request.Get("http://targethost/homepage")
//                .execute().returnContent();
//        Request.Post("http://targethost/login")
//                .bodyForm(Form.form().add("username",  "vip").add("password",  "secret").build())
//                .execute().returnContent();
    }

    @Test
    public void quickStart() throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("http://httpbin.org/get");
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            }

            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "vip"));
            nvps.add(new BasicNameValuePair("password", "secret"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            }
        }
    }

}
