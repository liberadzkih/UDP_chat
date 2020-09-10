package liberadzkih.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class NickHelper {

    public static String getNick(MulticastSocket socket, Sender sender) {
        Scanner scanner = new Scanner(System.in);
        String inputNick;
        do {
            System.out.print("Enter your nickname: ");
            inputNick = scanner.nextLine();
        }
        while (!isNickCorrect(socket, sender, inputNick));
        return inputNick;
    }

    public static boolean isNickCorrect(MulticastSocket socket, Sender sender, String nick) {
        sender.send("NICK " + nick); //NICK nickname
        byte[] buffer = new byte[("NICK " + nick + " BUSY").getBytes().length];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        long endTime = System.currentTimeMillis() + 5000;

        try {
            while (endTime > System.currentTimeMillis()) {
                socket.setSoTimeout(5000);
                socket.receive(packet);
                String response = new String(packet.getData(), 0, packet.getLength());
                if (Receiver.isNickBusy(response) && Receiver.getNickFromMsg(response).equals(nick)) {
                    System.out.println("Nick is busy");
                    return false;
                }
            }
            return true;
        } catch (SocketTimeoutException e) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
