/**
 * CS 380.01 - Computer Networks
 * Professor: NDavarpanah
 *
 * Project 1
 * ChatClient
 *
 * Justin Galloway
 *
 * ~ChatClient Class~
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

// Main ChatClient class. Handles input and printstreaming
public class ChatClient {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("18.221.102.182", 38001)){
            Vector<String> users = new Vector<String>();

            // Initialize input stream and reader
            // Server and client communicate using UTF-8
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");

            // Initialize buffered reader and input field
            BufferedReader br = new BufferedReader(isr);
            Scanner input = new Scanner(System.in);
            OutputStream os = socket.getOutputStream();
            PrintStream ps = new PrintStream(os);

            // Username entry
            System.out.print("Enter your UserName: ");
            String user = input.nextLine();
            if(users.contains(user) == false) {
                users.add(user);
                System.out.println("Enjoy your chat, " + user + ".");
                ps.println(user);
            } else if (users.contains(user) == true) {
                System.out.println("Name in use.");
                socket.close();
                System.exit(0);
            }

            // See "PollThread(br)" in PollThread Class
            PollThread pt = new PollThread(br);
            pt.start();

            // Always
            while(true) {
                if (".exit".equals(input)) {
                    System.out.println("Closing connection...");
                    socket.close();
                    System.exit(0);
                }
                ps.println(input.nextLine());
            }
        }
    }
}

// Polls for possibility of text, catches exception
class PollThread extends Thread {
    private String msg;
    private BufferedReader br;

    public void run() {
        try {
            // While there is a message...
            while((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            System.out.println("There was an exception, but don't worry, dude.");
        }
    }

    public PollThread (BufferedReader br) {
        this.br = br;
    }
}
