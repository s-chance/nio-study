package io.github.schance.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.TimeUnit;

public class NettyClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                        System.out.println(s);
                                    }
                                });
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect("localhost", 8080);
        connectFuture.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println("Connected to server");
                EventLoop eventloop = connectFuture.channel().eventLoop();
                eventloop.scheduleAtFixedRate(() -> {
                    connectFuture.channel().writeAndFlush("hello " + System.currentTimeMillis() + "\n");
                }, 0, 1, TimeUnit.SECONDS);
            }
        });
    }
}
