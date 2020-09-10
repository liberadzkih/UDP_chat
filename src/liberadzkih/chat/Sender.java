package liberadzkih.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Sender {

    private MulticastSocket socket;
    private InetAddress address;
    private int port;

    public Sender(MulticastSocket socket, InetAddress address, int port) {
        this.socket = socket;
        this.address = address;
        this.port = port;
    }

    public void send(String msg) {
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet, (byte) 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
