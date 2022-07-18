import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client extends Thread {
    Socket socket;
    InputStream is;
    OutputStream os;
    PrintStream out;
    Scanner in;
    String input;
    ChatServer context;

    public Client(Socket socket, ChatServer context) {
        this.context = context;
        this.socket = socket;
        try {
            this.is = socket.getInputStream();
            this.os = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        in = new Scanner(is);
        out = new PrintStream(os);
    }

    public void printMsg(String msg) {
        out.println(msg);
    }


    public void run() {
        input = in.nextLine();
        while (!isInterrupted() && !input.equals("bye")) {
            context.msgAll(input, this);
            input = in.nextLine();
        }
        context.msgAll(input, this);

        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.clients.remove(this);
    }
}