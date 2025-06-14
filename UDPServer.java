import java.io.*;
import java.net.*;
import java.util.concurrent.*;

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
        
        class ServerWorker implements Runnable {
            private final String filename;
            private final int fileSize;
            private final InetAddress clientAddress;
            private final int dataPort;
        
            public ServerWorker(String filename, int fileSize, InetAddress clientAddress, int dataPort) {
                this.filename = filename;
                this.fileSize = fileSize;
                this.clientAddress = clientAddress;
                this.dataPort = dataPort;
            }
        
            @Override
            public void run() {
                try (DatagramSocket dataSocket = new DatagramSocket()) {
                    File file = new File("ServerFiles", filename);
                    byte[] buffer = new byte[1024];
                    try (FileInputStream fis = new FileInputStream(file)) {
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            DatagramPacket packet = new DatagramPacket(buffer, bytesRead, clientAddress, dataPort);
                            dataSocket.send(packet);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        int listenPort = Integer.parseInt(args[0]);
        DatagramSocket serverSocket = new DatagramSocket(listenPort);
        System.out.println("Server listening on port " + listenPort);

        while(true) {
            byte[] buffer = new byte[2048];
            DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(requestPacket);

            String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
            InetAddress clientAddress = requestPacket.getAddress();
            int clientPort = requestPacket.getPort();

            if (request.startsWith("DOWNLOAD")) {
                String filename = request.split(" ")[1];
                File file = new File("ServerFiles", filename);
                if (!file.exists()) {
                    String errMsg = "ERR " + filename + " NOT_FOUND";
                    serverSocket.send(new DatagramPacket(errMsg.getBytes(), errMsg.length(), clientAddress, clientPort));
                } else {
                    int fileSize = (int) file.length();
                    int assignedPort = getNextDataPort();
                    String okMsg = "OK " + filename + " SIZE " + fileSize + " PORT " + assignedPort;
                    serverSocket.send(new DatagramPacket(okMsg.getBytes(), okMsg.length(), clientAddress, clientPort));

                    executor.submit(new ServerWorker(filename, fileSize, clientAddress, assignedPort));
                }
            }
        }
    }
    private static synchronized int getNextDataPort() {
        int port = nextDataPort;
        nextDataPort++;
        if (nextDataPort > DATA_PORT_END) nextDataPort = DATA_PORT_START;
        return port;
    }
}