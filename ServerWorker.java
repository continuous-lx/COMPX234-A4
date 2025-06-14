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

    }
}
