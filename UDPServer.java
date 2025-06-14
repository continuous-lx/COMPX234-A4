import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer {
    private static final int DATA_PORT_START = 50000;
    private static final int DATA_PORT_END = 51000;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static int nextDataPort = DATA_PORT_START;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java UDPServer <port>");
            return;
        }

        int listenPort = Integer.parseInt(args[0]);
        DatagramSocket serverSocket = new DatagramSocket(listenPort);
        System.out.println("Server listening on port " + listenPort);

        

    }
}