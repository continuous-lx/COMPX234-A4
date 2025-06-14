import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java UDPClient <host> <port> <filelist.txt>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        List<String> files = readFileList(args[2]);

        for (String filename : files) {
            downloadFile(host, port, filename);
        }
    }
    private static List<String> readFileList(String path) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.trim());
            }
        }
        return list;
    }
    private static void downloadFile(String host, int port, String filename) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(host);

        String downloadRequest = "DOWNLOAD " + filename;
        String response = ReliableSender.sendAndReceive(socket, downloadRequest, serverAddress, port, 5);
        if (response.startsWith("ERR")) {
            System.out.println("File not found: " + filename);
            return;
        }

        String[] parts = response.split(" ");
        int fileSize = Integer.parseInt(parts[4]);
        int dataPort = Integer.parseInt(parts[6]);
        DatagramSocket fileSocket = new DatagramSocket();

        try (FileOutputStream fos = new FileOutputStream("ClientFiles/" + filename)) {
            int start = 0;
            while (start < fileSize) {
                int end = Math.min(start + 999, fileSize - 1);
                String request = String.format("FILE %s GET START %d END %d", filename, start, end);
                String reply = ReliableSender.sendAndReceive(fileSocket, request, serverAddress, dataPort, 5);

                String base64 = reply.substring(reply.indexOf("DATA") + 5);
                byte[] data = Base64.getDecoder().decode(base64);
                fos.write(data);
                start = end + 1;
            }

            // Send CLOSE
            String close = "FILE " + filename + " CLOSE";
            String closeOk = ReliableSender.sendAndReceive(fileSocket, close, serverAddress, dataPort, 3);
            System.out.println("Downloaded " + filename);
        }
    }
}
