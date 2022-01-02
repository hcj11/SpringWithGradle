package org.apache.http.client.nio;

import cn.hutool.core.lang.Assert;
import com.google.j2objc.annotations.LoopTranslation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.CoyoteInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.impl.nio.codecs.IdentityDecoder;
import org.apache.http.impl.nio.codecs.IdentityEncoder;
import org.apache.http.impl.nio.codecs.LengthDelimitedDecoder;
import org.apache.http.impl.nio.reactor.SessionInputBufferImpl;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.nio.client.methods.ZeroCopyConsumer;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;

import java.io.*;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.apache.http.client.nio.HttpClientNioSourceTest.CustomHeapByteBufferAllocator.customHeapByteBufferAllocator;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
public class HttpClientNioSourceTest {
    // TestRule
    // ParamterRule 提供入参进行invoke();
    SessionInputBufferImpl sessionInputBuffer =null;
            IdentityDecoder identityDecoder =null;
    LengthDelimitedDecoder lengthDelimitedDecoder =null;
    IdentityEncoder identityEncoder =null;
    FileChannel channel =null;
    Object lock = new Object();
    volatile  RandomAccessFile rw = null;
   volatile RandomAccessFile rwdSource = null;
    volatile RandomAccessFile rwdDestination = null;
    /**
     * 生成test对象。
     */
    @BeforeEach
    public void setUp(){

        // 如何加入 探针。
        // todo chunkEncoder and ChunkDecoder;
        // todo read from channel or write to channel
        // step by step and   one by one


        //  channel  ( such as a hardware device, a file, a network socket )
        //  read buffer  or readableByteChannel;
        ReadableByteChannel readableByteChannel = mock(ReadableByteChannel.class);
         sessionInputBuffer = new SessionInputBufferImpl(256,256,null,customHeapByteBufferAllocator);
        HttpTransportMetricsImpl httpTransportMetrics = mock(HttpTransportMetricsImpl.class);
        httpTransportMetrics.getBytesTransferred();


        identityDecoder = new IdentityDecoder(readableByteChannel, sessionInputBuffer, httpTransportMetrics);
        lengthDelimitedDecoder = new LengthDelimitedDecoder(readableByteChannel, sessionInputBuffer, httpTransportMetrics,5);

        WritableByteChannel writableByteChannel = mock(WritableByteChannel.class);

//    nio    org.apache.http.impl.io.SessionOutputBufferImpl sessionOutputBuffer1 = new SessionOutputBufferImpl(httpTransportMetrics, 256, 0, null);
        org.apache.http.nio.reactor.SessionOutputBuffer  sessionOutputBuffer = new org.apache.http.impl.nio.reactor.SessionOutputBufferImpl(256,256,null,customHeapByteBufferAllocator);
        identityEncoder = new IdentityEncoder(writableByteChannel, sessionOutputBuffer, httpTransportMetrics);

        try {
             rw = new RandomAccessFile("G:\\11.txt", "rws");
             rwdSource = new RandomAccessFile("G:\\fj.pdf", "rwd");
             rwdDestination = new RandomAccessFile("G:\\tmp\\fj1.pdf", "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         channel = rw.getChannel();




    }
    @AfterEach
    public void after(){
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * FutureTask => use tirek stask , 1 vs multi
     * new Thread(){ c.call()};
     *
     */
    @Test
    public void futureTest() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> submit = executorService.submit(() -> {
            return 1;
        });
        Integer integer = submit.get();


    }
    @Test
    public void sliceTest(){
        String str ="hello world";
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));
        ByteBuffer slice = wrap.slice();
        log.info("slice: {},{}",slice,new String(slice.array()));
        Assert.isTrue(slice.limit()==11&&slice.position()==0&&slice.capacity()==11);
    }
    @Test
    public void test2_2(){
        String str ="hello world";
        ByteBuffer allocate1 = ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8));
        log.info("ByteBuffer2: {}",allocate1);
        Assert.isTrue(allocate1.limit()==11&&allocate1.position()==0&&allocate1.capacity()==11);
        ByteBuffer allocate = ByteBuffer.allocate(12);
        log.info("ByteBuffer2-2: {}",allocate);
        Assert.isTrue(allocate1.limit()==12&&allocate1.position()==0&&allocate1.capacity()==12);
    }
    @Test
    public void arrayCopy(){
        String str ="hello world";
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));
        System.arraycopy(wrap.array(), 0, wrap.array(),0, 11);
        log.info("ByteBuffer1: {},{}",wrap,new String(wrap.array()));
    }

    @Test
    public void compactTestWithArrayCopy(){
        String str ="hello world";
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        ByteBuffer compact = allocate1.compact();
        log.info("compact: {},{}",compact,new String(compact.array()));

//        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));
//        System.arraycopy(wrap.array(), 0, wrap.array(),0, 11);
//
//        log.info("wrap: {},{}",wrap,new String(wrap.array()));
//        ByteBuffer compact = wrap.compact();
//        log.info("compact: {},{}",compact,new String(compact.array()));
    }

    @Test
    public void compactTest(){
        String str ="hello world";
        // p:0,L=12,c:12
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        // p:0,L=11,c:11
        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));
        //
        ByteBuffer compact = wrap.compact();
        log.info("ByteBuffer1: {},{}",compact,new String(compact.array()));
        Assert.isTrue(wrap.limit()==11&&wrap.position()==11&&wrap.capacity()==11);
    }
    @Test
    public void combineWithByteBuffer(){
        String str ="hello world";
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));
        log.info("ByteBuffer1: {}",wrap);
        Assert.isTrue(wrap.limit()==11&&wrap.position()==0&&wrap.capacity()==11);
        String strNew ="hello world1";
        Executable executable = ()->{ wrap.put(strNew.getBytes(StandardCharsets.UTF_8));};
        assertThrows(BufferOverflowException.class,executable);


        ByteBuffer allocate = ByteBuffer.allocate(11 + 12);
        System.arraycopy(wrap.array(), 0, allocate.array(),0, 11);
        ByteBuffer put = allocate.put(strNew.getBytes(StandardCharsets.UTF_8));
        log.info("ByteBuffer1 + ByteBuffer2: {}",put);
        Assert.isTrue(put.limit()==23&&put.position()==12&&put.capacity()==11 + 12);


    }

    @Test
    public void readWithInputStreamAndWriteWithFileChannel() throws IOException, InterruptedException {

        // todo mock inputstream method have problem.
        FileChannel out = rwdDestination.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(8192);
        buf = buf.wrap("helloworld!!!".getBytes(StandardCharsets.UTF_8));


        CoyoteInputStream mock = mock(CoyoteInputStream.class);
        when(mock.read(buf)).thenReturn(13,6,0,-1);// 0:代表空转，-1代表结束，
        int read=0;
        while ( ( read = mock.read(buf))>= 0 || buf.position() != 0) {
            if(read!=0){
                buf.position(0);
                buf.limit(read-1);
            }else{
                buf.position(13 - read);
            }

            log.info("after read buf:{}",buf.toString());
            // write(buf) -- write remaing buffer;
            out.write(buf);
            log.info("after write buf:{}",buf.toString());
            out.force(true);
            buf.compact();    // In case of partial write
            log.info("after compact buf:{}",buf.toString());

        }
    }
    @Test
    public void readAndWriteTwoTimes() throws IOException, InterruptedException {
        log.info("rwdSource:{},rwdDestination:{}",rwdSource,rwdDestination);
        readAndWrite();;
        try {
            rwdSource = new RandomAccessFile("G:\\fj.pdf", "rwd");
            rwdDestination = new RandomAccessFile("G:\\tmp\\fj1.pdf", "rwd");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //not append,becase of the position  stay the 0;
        log.info("rwdSource:{},rwdDestination:{}",rwdSource,rwdDestination);
        readAndWrite();;
    };
    @Test
    public void readAndWrite() throws IOException, InterruptedException {
        int size =0 ;
        FileChannel in = rwdSource.getChannel();
        FileChannel out = rwdDestination.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(8192);

        buf.clear();          // Prepare buffer for use
        while (in.read(buf) >= 0 || buf.position() != 0) {
            buf.flip();
            size += out.write(buf);
            out.force(true);
            buf.compact();    // In case of partial write

        }
        log.info("read and write:{}",size);
        in.close();;
        out.close();;

    }
    /**
     * copy  , inputstream transfer to outputstream; try.
     *
     *   test for synchronously write ‘s order.
     */
    private void write(int value) throws FileNotFoundException{
        String str ="hello world"+value;
        ByteBuffer allocate1 = ByteBuffer.allocate(12);
        ByteBuffer wrap = allocate1.wrap(str.getBytes(StandardCharsets.UTF_8));


        try {
            int write = channel.write(wrap);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
        }
    }
    @Test
    public void channelTest()  {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Stream.generate(()->{return atomicInteger.getAndIncrement();}).parallel().forEach((ss)->{
            if(ss==2000){
                return;
            }
            try {
                Thread.yield();
                write(ss.intValue());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });


    }

    public  static class CustomHeapByteBufferAllocator implements ByteBufferAllocator {
      final  static  CustomHeapByteBufferAllocator customHeapByteBufferAllocator = new CustomHeapByteBufferAllocator();

        @Override
        public ByteBuffer allocate(final int size) {
            ByteBuffer allocate = ByteBuffer.allocate(size);
            // 256  first =0   11 ;size =12 ; 留一位。byte
             String str = "hello world";
            ByteBuffer put = allocate.put(str.getBytes(StandardCharsets.UTF_8));

            return  allocate;
        }
    }
    /**
     *    yes:   transferred = ((FileContentDecoder)decoder).transfer(
     *                     this.fileChannel, this.idx, Integer.MAX_VALUE);
     *
     *     transferred = this.fileChannel.transferFrom(
     *                     new ContentDecoderChannel(decoder), this.idx, Integer.MAX_VALUE);
     */
    @Test
    public void readFromFileChannelWithIdentity() throws FileNotFoundException {

        String path2 = "G:\\tmp\\notepad.txt";
        File download = new File(path2);
        RandomAccessFile accessFile= new RandomAccessFile(download, "rw");
        FileChannel channel = accessFile.getChannel();
        try {
            long transfer = identityDecoder.transfer(channel, 0, 100);
            log.info("identityDecoder transfer bytes:{}",transfer);
            Assert.isTrue(transfer==11);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void readFromFileChannelWithLengthDelimited() throws FileNotFoundException {

        String path2 = "G:\\tmp\\notepad.txt";
        File download = new File(path2);
        RandomAccessFile accessFile= new RandomAccessFile(download, "rw");
        FileChannel channel = accessFile.getChannel();
        try {
            long transfer = lengthDelimitedDecoder.transfer(channel, 0, 100);
            log.info("lengthDelimitedDecoder transfer bytes:{}",transfer);
            Assert.isTrue(transfer==5);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * the use postion of  identityDecoder  and  lengthDelimitedDecoder?
     */
    @Test
    public void test3() throws IOException {
        // 一次接受 5个长度
        log.info("this object:{}",this);
        ByteBuffer allocate = ByteBuffer.allocate(11);
        int read = lengthDelimitedDecoder.read(allocate);
        log.info("lengthDelimitedDecoder.read count :{}",read);
        Assert.isTrue(read==5);
    }
    @Test
    public void test1_2() throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(5);
        int read = identityDecoder.read(allocate);
        log.info("identityDecoder.read count :{},{}",read,allocate);
        Assert.isTrue(read==5);
        Assert.isTrue(allocate.limit()==5&&allocate.position()==5&&allocate.capacity()==5);
        Assert.isTrue(sessionInputBuffer.hasData());
        log.info("sessionInputBuffer: {}",sessionInputBuffer.toString());
//        [mode=out pos=5 lim=11 cap=256]

    }
    @Test
    public void test1() throws IOException {
// the.buffer.remaining = 11; return 11
        ByteBuffer allocate = ByteBuffer.allocate(11);
        int read = identityDecoder.read(allocate);
        log.info("identityDecoder.read count :{}",read);
        Assert.isTrue(read==11);
        java.lang.String string = new java.lang.String(allocate.array());
        Assert.isTrue(string.equals("hello world"));
    }
    @Test
    public void writeByteBufferWithIdentityEncoder(){

//        identityEncoder.write()
    }
    @Test
    public void writeByteBufferWithLengthDelimitedEncoder(){

    }

    @Test
    public void writeToFileChannelTest(){

    }
    /**
     * how to control the data flow of network  for the provide content ?
     */
    @Test
    public void provideTest(){

    }
}
