import java.io.*;
import java.net.*;
import java.util.*;

public class BeatBoxServer {

    ArrayList<ObjectOutputStream> clientOutputStream;

    public static void main(String[] args) {
        new BeatBoxServer().go();
    }

    public void go(){
        clientOutputStream = new ArrayList<>();
         try{
             ServerSocket serverSock = new ServerSocket(4242);

             while (true) {
                 Socket clientSocket = serverSock.accept();
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                 clientOutputStream.add(out);

                 Thread t = new Thread(new ClientHandler(clientSocket));
                 t.start();

                 System.out.println("got a connection");
             }
         } catch (Exception e) {
             System.out.println(e.getMessage());
         }
    }

    public void tellEveryone(Object one, Object two) {
        Iterator it = clientOutputStream.iterator();
        while (it.hasNext()) {
            try {
                ObjectOutputStream out = (ObjectOutputStream) it.next();
                out.writeObject(one);
                out.writeObject(two);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public class ClientHandler implements Runnable {

        ObjectInputStream in;
        Socket clientSocket;

        public ClientHandler(Socket socket) {
            try {
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        public void run() {
            Object o2 = null;
            Object o1 = null;

            try{
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();

                    System.out.println("read two objects");
                    tellEveryone(o1, o2);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }
}
