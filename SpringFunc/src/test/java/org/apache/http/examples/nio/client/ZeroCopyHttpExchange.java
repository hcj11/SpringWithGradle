/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.http.examples.nio.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Future;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.*;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;
import org.apache.http.nio.client.methods.ZeroCopyPost;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@Slf4j
/**
 * This example demonstrates how HttpAsyncClient can be used to upload or download files
 * without creating an intermediate content buffer in memory (zero copy file transfer).
 */
public class ZeroCopyHttpExchange {
    String path1 = "G:\\fj.pdf";
    String path2 = "G:\\tmp\\tmp\\fj.pdf";
    Object lock = new Object();
    @Test
    public void consumeInput(){
        // client 尝试重构，直接找实现类
        File upload = new File(path1);
        long length = upload.length();
        log.info("{}",length);
        // 代码不是靠猜的，

    }
    @Test
    public void HttpClientTest() throws FileNotFoundException {
        File upload = new File(path1);
        File download = new File(path2);
        ZeroCopyPost httpost = new ZeroCopyPost("http://localhost:63190/", upload,
                ContentType.create("text/plain"));

        ZeroCopyConsumer<File> consumer = new ZeroCopyConsumer<File>(download) {
            @Override
            protected File process( // download = file
                                    final HttpResponse response,
                                    final File file,
                                    final ContentType contentType) throws Exception {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    throw new ClientProtocolException("Upload failed: " + response.getStatusLine());
                }
                return file;
            }
        };


        HttpClientContext httpClientContext = HttpClientContext.create();

        CloseableHttpAsyncClient httpClient = mock(CloseableHttpAsyncClient.class);
//        httpClient.execute()
//        when(httpClient.<HttpResponse>execute(httpost, consumer,httpClientContext, null))
//                .then((InvocationOnMock invocation)->{
//
//                        return invocation.callRealMethod();
//                });

        HttpConnectionMetrics metrics = httpClientContext.getConnection().getMetrics();
        log.info("getReceivedBytesCount:{}",metrics.getReceivedBytesCount());


    }
    @Test
    public void uploadAndDownload() throws Exception {

        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();

        //  provider, 分段逻辑。它采用定长发送数据的方式。
        // 如何发送这些数据的呢？ channel.write(bytebuffer)
        // 采用zerocopy,
        // write: fileChannel -> session.Channel,
        // read: sessionChannel -> fileChannel

        try {
            httpclient.start();

            File upload = new File(path1);

            File download = new File(path2);
            ZeroCopyPost httpost = new ZeroCopyPost("http://localhost:61273/", upload,
                    ContentType.create("text/plain"));
            ZeroCopyConsumer<File> consumer = new ZeroCopyConsumer<File>(download) {

                @Override
                protected File process( // download = file
                        final HttpResponse response,
                        final File file,
                        final ContentType contentType) throws Exception {
                    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        throw new ClientProtocolException("Upload failed: " + response.getStatusLine());
                    }
                    return file;
                }

            };

            // how to send data to consumer;  complete flag?
            HttpClientContext httpClientContext = HttpClientContext.create();

            FutureCallback futureCallback = new FutureCallback() {
                @Override
                public void completed(Object result) {
                    log.info("getReceivedBytesCount:{}",httpClientContext.getConnection().getMetrics().getReceivedBytesCount());
                }
                @Override
                public void failed(Exception ex) {
                }

                @Override
                public void cancelled() {
                }
            };
            // AbstractContentDecoder
// mock , assert , when, given(),


            Future<File> future = httpclient.execute(httpost, consumer,httpClientContext,futureCallback);

            File result = future.get();
            System.out.println("Response file length: " + result.length());
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }
    @Test
    public void test1(){
        //
//        FastByteBuffer.class;
    }



}