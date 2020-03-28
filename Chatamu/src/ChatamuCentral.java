import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static java.lang.module.ModuleDescriptor.read;

public class ChatamuCentral {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.register(selector,SelectionKey.OP_ACCEPT);
        ServerSocket serverSocket = ssc.socket();
        serverSocket.bind(new InetSocketAddress(2019));
        System.out.println("J'attends la connection sur le port: "+ serverSocket.getLocalPort());

        while (true) {
            System.out.println("je suis dans le while");
            int keyCount = selector.select();
            System.out.println("keyCount: " + keyCount);

           System.out.println("je vais definir mon iterator");
           Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                System.out.println("je vais faire mes iterrations");
                SelectionKey key = keys.next();

                if (key.isAcceptable()) {
                    System.out.println("je vais accepeter la connection");
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel  socketchannel ;
                    try {
                        socketchannel = serverSocketChannel.accept();
                        socketchannel.configureBlocking(false);
                        socketchannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2020));
                        System.out.println("Une connexion est acceptée au port numero:"+ socketchannel.getRemoteAddress());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                     String msg;
                        BufferedReader sin = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
                        //BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                        while (true) {
                            msg = sin.readLine();
                            if (msg.startsWith("MESSAGE ")) {
                                System.out.print(msg.substring("MESSAGE ".length()) + "\n");
                            } else if (msg.startsWith("ERROR "))
                                System.out.println("ERREUR : " + msg.substring("ERROR ".length()) + "\n");
                        }
                   /* try {
                        .read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    key.interestOps(SelectionKey.OP_WRITE);*/
                }
                keys.remove();
            }
        }
    }
}
