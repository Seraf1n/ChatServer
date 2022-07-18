import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements Runnable{

    public List<Client> clients;
    ServerSocket serverSocket;
    Socket socket = null;

    public ChatServer() throws IOException {
        System.out.println("Waiting...");
        this.clients = new ArrayList<>();
        this.serverSocket = new ServerSocket(1234);
    }

    public void run() {
       activateChatServer();
    }

    private void activateChatServer(){

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // создаем клиента на своей стороне
            Client client = new Client(socket, this);
            clients.add(client);
            // запускаем поток
            Thread thread = new Thread(client);
            thread.start();
            announceAll(client);
        }

    }
    private void announceAll(Client client) {
        for(Client c : clients) {
            if (c != client) {
                c.printMsg(c.getName() + " connected!");
            }else {
                c.printMsg("добро пожаловать," + c.getName());
            }
        }
    }

    public void msgAll(String msg, Client client){
        for(Client c : clients) {
            if (c != client) {
             c.printMsg(client.getName() + ": " + msg);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        new Thread(new ChatServer()).start();

    }
}
