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
            if (msg.length() > 0) {
                sender.send("MSG " + nick + " " + room + " " + msg);
            }
        }
    }
}
