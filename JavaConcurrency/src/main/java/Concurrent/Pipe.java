package Concurrent;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Pipe extends Thread{
    PipedInputStream pipedInputStream;
    public Pipe(PipedInputStream pipedInputStream){
       this.pipedInputStream=pipedInputStream;
    }
    @Override
    public void run() {
        super.run();
        byte[] readbytes = new byte[1024];
        int length=0;
        try{  int read = pipedInputStream.read(readbytes);
        }catch (Exception e){
        }
        System.out.println(new String(readbytes,0,1024));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream();
        pipedOutputStream.connect(pipedInputStream);
        new Thread(new Pipe(pipedInputStream)).start();
        pipedOutputStream.write(new String("hello,world").getBytes());
        pipedOutputStream.flush();


    }
}
