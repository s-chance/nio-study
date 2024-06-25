package org.entropy.bytebuffer.problem;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TCPProblem {
    @Test
    public void NioStickyPacketServer() {
        int port = 12345;
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            System.out.println("Server started on port " + port);

            while (true) {
                SocketChannel socketChannel = serverChannel.accept();
                if (socketChannel != null) {
                    handleClient(socketChannel);
                }
            }
        } catch (IOException e) {
        }
    }

    private static void handleClient(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(27); // 使用较小的缓冲区
        try {
            while (socketChannel.read(buffer) != -1) {
                buffer.flip();
                String message = StandardCharsets.UTF_8.decode(buffer).toString();
                buffer.compact(); // 重置缓冲区，准备接收下一部分数据
                System.out.println("Received message: " + message);
            }
        } catch (IOException e) {
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
            }
        }
    }

    @Test
    public void NioStickyPacketClient() {
        int port = 12345;
        String host = "localhost";
        String[] payload = new String[]{"Hello,world\n", "I'm zhangsan\n", "How are you?\n"};

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress(host, port));
            System.out.println("Connected to server");

            for (int i = 0; i < 3; i++) { // 发送多个数据包
                ByteBuffer buffer = ByteBuffer.wrap((payload[i]).getBytes(StandardCharsets.UTF_8));
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        } catch (IOException e) {
        }
    }
}
