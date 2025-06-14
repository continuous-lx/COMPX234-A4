import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

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


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
