import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class ReliableSender {
    public static String sendAndReceive(DatagramSocket socket, String message, InetAddress address, int port, int retries) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.setSoTimeout(1000);


        throw new IOException("No response after retries.");
    }

    public static void sendAndWait(DatagramSocket socket, String message, InetAddress address, int port) throws IOException {
        sendAndReceive(socket, message, address, port, 5);
    }
}
