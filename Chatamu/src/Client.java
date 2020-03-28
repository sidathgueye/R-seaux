import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket sk = new Socket(InetAddress.getLocalHost(), 2019);
        loginProcedure(sk);
        new Thread(new KeyboardHandler(sk)).start();
        new ServerReader(sk).run();
    }

    public static void loginProcedure(Socket socket) {
        try {
            String msg;
            PrintStream sout = new PrintStream(socket.getOutputStream());
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Donnez votre pseudo: ");
                msg = "LOGIN "+stdin.readLine();
                sout.println(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static class KeyboardHandler implements Runnable {
        Socket socket;

        public KeyboardHandler(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            try {
                String msg;
                PrintStream sout = new PrintStream(socket.getOutputStream());
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    msg = stdin.readLine();
                    sout.println("MESSAGE "+ msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static class ServerReader implements Runnable {
        Socket socket;

        public ServerReader(Socket s) {
            this.socket = s;
        }
        @Override
        public void run() {
            try {
                String msg;
                BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    msg = sin.readLine();
                    if (msg.startsWith("MESSAGE ")) {
                        System.out.print(msg.substring("MESSAGE ".length()) + "\n");
                    }else if (msg.startsWith("ERROR "))
                        System.out.println("ERREUR : "+ msg.substring("ERROR ".length()) + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

