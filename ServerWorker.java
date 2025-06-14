import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Base64;

public class ServerWorker implements Runnable {
    private String filename;
    private int fileSize;
    private InetAddress clientAddress;
    private int port;

    public ServerWorker(String filename, int fileSize, InetAddress clientAddress, int port) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.clientAddress = clientAddress;
        this.port = port;
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port);
             FileInputStream fis = new FileInputStream(new File("ServerFiles", filename))) {
            byte[] buffer = new byte[1000];
            int start = 0;
            int readBytes;

            while ((readBytes = fis.read(buffer)) != -1) {
                byte[] dataToEncode = new byte[readBytes];
                System.arraycopy(buffer, 0, dataToEncode, 0, readBytes);
                String encoded = Base64.getEncoder().encodeToString(dataToEncode);
                int end = start + readBytes - 1;
                String response = String.format("FILE %s OK START %d END %d DATA %s", filename, start, end, encoded);

                ReliableSender.sendAndWait(socket, response, clientAddress, port);
                start = end + 1;
            }

            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (msg.startsWith("FILE") && msg.contains("CLOSE")) {
                    String ack = "FILE " + filename + " CLOSE_OK";
                    socket.send(new DatagramPacket(ack.getBytes(), ack.length(), clientAddress, packet.getPort()));
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
