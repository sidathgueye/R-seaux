import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SalonCentral {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(2019);
        System.out.println(" Demarrage du Salon Central");
        while (true) {
            Socket s = ss.accept();
            new Thread(new ClientReader(s)).start();
        }
    }
    public  static class ClientReader implements Runnable {
        Socket socket;
        public ClientReader(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try{
                String msg;
                BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    msg = sin.readLine();
                    if (msg.startsWith("MESSAGE ")) {
                        System.out.print(msg.substring("MESSAGE ".length()) + "\n");
                    } else if (msg.startsWith("ERROR "))
                        System.out.println("ERREUR : " + msg.substring("ERROR ".length()) + "\n");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
