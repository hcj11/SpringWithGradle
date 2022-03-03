package ServerClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Server {
    public static void demo1() throws IOException {
        InetAddress localhost = InetAddress.getByName("localhost");
        MulticastSocket multicastSocket = new MulticastSocket(8080);
        multicastSocket.joinGroup(localhost);

        String hel="hello world";
        DatagramPacket datagramPacket = new DatagramPacket(hel.getBytes(), hel.length(), localhost, 8080);
        multicastSocket.send(datagramPacket);


    }
    public static void demo2() throws IOException{
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket accept = serverSocket.accept();
        DataOutputStream dataOutputStream = new DataOutputStream(accept.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());

        dataInputStream.readUTF();
        dataInputStream.readDouble();
        dataInputStream.readBoolean();

        dataOutputStream.writeUTF("");
    }
    public static void main(String[] args) throws IOException {
        demo1();
    }
}
