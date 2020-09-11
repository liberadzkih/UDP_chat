package liberadzkih.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Receiver implements Runnable {

    private MulticastSocket socket;
    private Sender sender;
    private String nick;
    private String room;

    public Receiver(MulticastSocket socket, Sender sender) {
        this.sender = sender;
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                //NICK nickname
                //NICK nickname BUSY
                //JOIN ROOM NICK
                //MSG ROOM NICK msg
                //SETROOM ROOM NICK
                //LEAVE ROOM NICK
                //WHOIS ROOM NICK
                String header = message.split(" ")[0];
                switch (header) {
                    case "NICK":
                        if (message.split(" ")[1].equals(nick) && !message.substring(message.length() - 4).equals("BUSY")) {
                            sender.send("NICK " + nick + " BUSY");
                        }
                        break;
                    case "JOIN":
                        if (getRoomFromPacket(message).equals(room) && !getNickFromPacket(message).equals(nick)) {
                            System.out.println(getNickFromPacket(message) + " joined to this room");
                        }
                        break;
                    case "MSG":
                        if (getRoomFromPacket(message).equals(room) && !getNickFromPacket(message).equals(nick)) {
                            System.out.println(getNickFromPacket(message) + ": " + message(message));
                        }
                        break;
                    case "LEAVE":
                        if (getRoomFromPacket(message).equals(room)) {
                            if (getNickFromPacket(message).equals(nick)) {
                                room = null;
                            } else {
                                System.out.println(getNickFromPacket(message) + " left the room");
                            }
                        }
                        break;
                    case "SETROOM":
                        if (getNickFromPacket(message).equals(nick)) {
                            room = getRoomFromPacket(message);
                            sender.send("JOIN " + room + " " + nick);
                            System.out.println("Welcome in " + room + ". Let's talk!");
                        }
                        break;
                    case "WHOIS":
                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRoomFromPacket(String message) {
        return message.split(" ")[1];
    }

    private String getNickFromPacket(String message) {
        return message.split(" ")[2];
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public static boolean isNickBusy(String message) {
        return message.substring(message.length() - 4).equals("BUSY");
    }

    private String message(String message) {
        String[] data = message.split(" ");
        String msg = new String();
        for (int i = 3; i < data.length; i++) {
            msg += data[i] + " ";
        }
        return msg;
    }

    public static String getNickFromMsg(String message) {
        return message.split(" ")[1];
    }

}
