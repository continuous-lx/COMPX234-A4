import java.io.IOException;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java UDPClient <host> <port> <filelist.txt>");
            return;
        }
    }
}
