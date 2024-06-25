package org.entropy.bytebuffer;

import java.nio.ByteBuffer;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读取
        /*buffer.get(new byte[4]);
        debugAll(buffer);
        buffer.rewind();
        System.out.println((char) buffer.get());*/

        // mark & reset
        // mark 做一个标记，记录 position 位置，reset 会将 position 重置到 mark 的位置
        /*System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark(); // 标记索引位置
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset(); // position 重置到索引位置
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());*/

        // get(i)
        System.out.println((char) buffer.get(3));
        debugAll(buffer);
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
