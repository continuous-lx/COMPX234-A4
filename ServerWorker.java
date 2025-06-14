import java.net.InetAddress;

public class ServerWorker implements Runnable {
     private String filename;
    private int fileSize;
    private InetAddress clientAddress;
    private int port;
}
