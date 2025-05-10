package io.github.schance.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServer {
    public static void main(String[] args) {
        Map<Channel, List<String>> db = new ConcurrentHashMap<>();
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ResponseHandler())
                                .addLast(new DbHandler(db));
                    }
                });
        ChannelFuture bindFuture = serverBootstrap.bind(8080);
        bindFuture.addListener(f -> {
            if (f.isSuccess()) {
                System.out.println("Server listened on port 8080");
            } else {
                System.out.println("Failed to listen on port 8080");
            }
        });
    }

    static class ResponseHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.println(s);
            String msg = s + " world\n";
            channelHandlerContext.channel().writeAndFlush(msg);
            channelHandlerContext.fireChannelRead(s);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel() + " is registered");
            ctx.fireChannelRegistered();
        }
    }

    static class DbHandler extends SimpleChannelInboundHandler<String> {
        private Map<Channel, List<String>> db;

        public DbHandler(Map<Channel, List<String>> db) {
            this.db = db;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            List<String> messageList = db.computeIfAbsent(channelHandlerContext.channel(), k -> new ArrayList<>());
            messageList.add(s);
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel() + " registered");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel() + " unregistered");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel() + " active");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            List<String> strings = db.get(ctx.channel());
            System.out.println(strings);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }
    }
}
