package io.github.schance.bio;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws Exception {
        Thread tom = new Thread(() -> {
            try {
                sendHello();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "tom");

        Thread jerry = new Thread(() -> {
            try {
                sendHello();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "jerry");

        tom.start();
        jerry.start();
        tom.join();
        jerry.join();
    }

    private static void sendHello() throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 8080));
        OutputStream outputStream = socket.getOutputStream();
        for (int i = 0; i < 10; i++) {
            outputStream.write((Thread.currentThread().getName() + " hello " + i).getBytes());
            outputStream.flush();
        }
        Thread.sleep(10000);
    }

    public static class User {
        private String name;
        private int age;
    }
}
