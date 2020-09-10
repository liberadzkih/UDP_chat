package liberadzkih.chat;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) throws UnknownHostException, SocketException {
        Chat chat = new Chat(2115, InetAddress.getByName("239.0.0.222"));
        chat.runChat();
    }
}
