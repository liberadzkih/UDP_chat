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

                if (isNickFree(message) && getNickFromMsg(message).equals(nick)) {
                    sender.send("NICK " + nick + " BUSY");
                } else if (isMsg(message) && messageRoom(message).equals(room) && !getNickFromMsg(message).equals(nick)) {
                    System.out.println(messageAuthor(message) + ": " + message(message));
                } else if (isJoinInformation(message) && getRoom(message).equals(room) && !getJoined(message).equals(nick)) {
                    System.out.println(getJoined(message) + " joined to this room");
                } else if (isLeave(message) && getRoom(message).equals(room)) {
                    if (message.split(" ")[2].equals(nick)) {
                        room = null;
                    } else {
                        System.out.println(message.split(" ")[2] + " left the room");
                    }
                } else if (isUserChangingRoom(message) && message.split(" ")[2].equals(nick)) {
                    room = getRoom(message);
                    sender.send("JOIN " + room + " " + nick);
                    System.out.println("Welcome in " + room + ". Let's talk!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    private boolean isNickFree(String message) {
        return message.substring(0, 4).equals("NICK") && !message.substring(message.length() - 4).equals("BUSY");
    }

    public static boolean isNickBusy(String message) {
        return message.substring(message.length() - 4).equals("BUSY");
    }

    private boolean isMsg(String message) {
        return message.substring(0, 3).equals("MSG");
    }

    private boolean isJoinInformation(String message) {
        return message.substring(0, 4).equals("JOIN");
    }

    private String getJoined(String message) {
        return message.split(" ")[2];
    }

    private String getRoom(String message) {
        return message.split(" ")[1];
    }

    private String messageAuthor(String message) {
        return message.split(" ")[1];
    }

    private String message(String message) {
        String[] data = message.split(" ");
        String msg = new String();
        for (int i = 3; i < data.length; i++) {
            msg += data[i] + " ";
        }
        return msg;
    }

    private String messageRoom(String message) {
        return message.split(" ")[2];
    }

    public static String getNickFromMsg(String message) {
        return message.split(" ")[1];
    }

    private boolean isLeave(String message) {
        return message.substring(0, 5).equals("LEAVE");
    }

    private boolean isUserChangingRoom(String message) {
        return message.substring(0, 7).equals("SETROOM");
    }

}
