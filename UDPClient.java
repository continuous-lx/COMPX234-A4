import java.io.IOException;
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
}
