package org.entropy.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestByteBufferString {
    public static void main(String[] args) {
        // 1. 字符串转为 ByteBuffer，默认为写模式
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());
        debugAll(buffer);

        // 2. Charset，默认为读模式
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);

        // 3. wrap，默认为读模式
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        // 4. ByteBuffer 转字符串
        String str1 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(str1);

        // 写模式下转换为字符串会出现问题
        // 需要先转换为读模式
        buffer.flip();
        String str2 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str2);

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
