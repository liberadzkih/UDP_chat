package liberadzkih.chat;

import java.util.Scanner;

public class MsgSender implements Runnable {

    private Sender sender;
    private String nick;
    private String room;

    public MsgSender(Sender sender) {
        this.sender = sender;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            String msg = in.nextLine();
            if (msg.equals("!leave")) {
                System.out.println("You've left the room");
                sender.send("LEAVE " + room + " " + nick);
                setRoom();
            } else if (msg.length() > 0) {
                sender.send("MSG " + nick + " " + room + " " + msg);
            }
        }
    }

    private void setRoom(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select room: room1 (1), room2 (2), room3 (3)");
        System.out.print("Your choice(1, 2 or 3): ");
        String input = scanner.nextLine();
        System.out.println("@"+input+"@");
        switch (Integer.parseInt(input)) {
            case 1:
                sender.send("SETROOM room1 " + nick);
                return;
            case 2:
                sender.send("SETROOM room2 " + nick);
                return;
            case 3:
                sender.send("SETROOM room3 " + nick);
                return;
        }
        System.out.println("wrong input, selected room1");
        sender.send("SETROOM room1 " + nick);
    }
}
