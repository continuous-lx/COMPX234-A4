import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer {
    private static final int DATA_PORT_START = 50000;
    private static final int DATA_PORT_END = 51000;
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static int nextDataPort = DATA_PORT_START;

    
}