package org.entropy.bytebuffer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestScatteringReads {
    public static void main(String[] args) {
        try (FileChannel channel = new RandomAccessFile("3parts.txt", "r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3); // one
            ByteBuffer b2 = ByteBuffer.allocate(3); // two
            ByteBuffer b3 = ByteBuffer.allocate(5); // three
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();
            debugAll(b1);
            debugAll(b2);
            debugAll(b3);
        } catch (IOException e) {
        }
    }

    static void debugAll(ByteBuffer buffer) {
        System.out.println("position: " + buffer.position() + ", " + "limit: " + buffer.limit());
        for (int i = 0; i < buffer.array().length; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        for (byte b : buffer.array()) {
            System.out.print(Integer.toHexString(b) + "\t");
        }
        for (byte b : buffer.array()) {
            System.out.print((char) (b));
        }
        System.out.println();
    }
}
