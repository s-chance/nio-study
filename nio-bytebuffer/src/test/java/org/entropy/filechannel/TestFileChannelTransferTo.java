package org.entropy.filechannel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel()
        ) {
            // 效率高，底层会利用操作系统的零拷贝技术进行优化
            long size = from.size(); // 获取文件总大小
            long position = 0; // 当前已传输的字节位置
            long chunkSize = 8 * 1024 * 1024; // 设置分块大小，这里设置为8MB

            while (position < size) {
                // 计算剩余大小，防止超过文件末尾
                long remaining = size - position;
                long bytesToTransfer = Math.min(chunkSize, remaining);

                // 执行分块传输
                long transferred = from.transferTo(position, bytesToTransfer, to);
                position += transferred; // 更新已传输的字节位置
            }
        } catch (IOException e) {
        }
    }
}