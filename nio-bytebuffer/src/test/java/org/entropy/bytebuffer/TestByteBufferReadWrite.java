package org.entropy.bytebuffer;

import java.nio.ByteBuffer;

public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put((byte) 0x61); // 'a'
        debugAll(buffer);
        buffer.put(new byte[]{0x62, 0x63, 0x64});
        debugAll(buffer);
        // 如果不切换到读模式，这里实际获取的是下一个写入位置的数据
        // System.out.println(buffer.get());
        buffer.flip();
        System.out.println(buffer.get());
        debugAll(buffer);
        // 这些模式切换的操作，不会清除任何原有的数据，原有的数据只会在写入时被覆盖
        buffer.compact();
        debugAll(buffer);
        buffer.put(new byte[]{0x65, 0x66, 0x67});
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


