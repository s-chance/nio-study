package org.entropy.network;

import lombok.extern.slf4j.Slf4j;
import org.entropy.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

@Slf4j
public class NioServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建 selector，管理多个 channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        // 2. 建立 selector 和 channel 的联系（注册）
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // 关注 accept 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("sscKey: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 3. select 方法，没有事件发生时线程阻塞，有事件发生时线程继续运行
            // select 在事件未处理完时不会阻塞线程
            selector.select();
            // 4. 处理事件，selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 处理 key 时，需用手动从 selectorKeys 集合中删除，否则下次处理会遇到 NullPointerException
                iterator.remove();
                log.debug("key: {}", key);
                // 5. 区分事件类型
                if (key.isAcceptable()) {
                    // accept 事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                    log.debug("scKey: {}", scKey);
                } else if (key.isReadable()) {
                    // read 事件
                    SocketChannel channel = (SocketChannel) key.channel(); // 获取触发事件的 channel
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    int read = channel.read(buffer);
                    if (read == -1) {
                        // 客户端正常或异常关闭
                        key.cancel(); // 彻底从 selector 的 keys 集合中移除 key
                        channel.close();
                    } else {
                        buffer.flip();
                        ByteBufferUtil.debugAll(buffer);
                        buffer.clear();
                    }
                }
            }
        }
    }
}
