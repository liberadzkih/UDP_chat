package liberadzkih.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Chat {

    private MulticastSocket socket;
    private Sender sender;
    private Receiver receiver;
    private MsgSender msgSender;

    public Chat(int port, InetAddress address) {
        try {
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(address);
            this.sender = new Sender(this.socket, address, port);
            this.receiver = new Receiver(this.socket, this.sender);
            this.msgSender = new MsgSender(this.sender);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioexcepption");
            System.exit(1);
        }
    }

    public void runChat() throws SocketException {
        System.out.println("~~~~~~~~~~~ CHAT STARTED ~~~~~~~~~~~");
        String nick = NickHelper.getNick(socket, sender);
        String room = setRoom();
        
        sender.send("JOIN " + room + " " + nick);
        
        receiver.setNick(nick);
        receiver.setRoom(room);
        
        msgSender.setRoom(room);
        msgSender.setNick(nick);
        socket.setSoTimeout(0);

        System.out.println("Welcome in room " + room + ". Let's talk!.");

        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(msgSender);

        receiverThread.start();
        senderThread.start();
    }
    
    private String setRoom(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select room: room1 (1), room2 (2), room3 (3)");
        System.out.print("Your choice(1, 2 or 3): ");
        String input = scanner.nextLine();

        switch (Integer.parseInt(input)) {
            case 1:
                return "room1";
            case 2:
                return "room2";
            case 3:
                return "room3";
        }
        System.out.println("wrong input, selected room1");
        return "room1";
    }
}
