package com.sam.network.netty.splicing.delimiter.server;

import com.sam.network.netty.splicing.delimiter.server.handler.MyDelimiterHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * netty server
 * @author sam.liang
 */
public class NettyEchoServer {

    /**
     * 端口
     */
    private static final int PORT = 12345;
    private static EventLoopGroup main = new NioEventLoopGroup();
    private static EventLoopGroup worker = new NioEventLoopGroup();
    private static MyDelimiterHandler myDelimiterHandler = new MyDelimiterHandler();

    /**
     * netty 服务端启动方法
     * @throws InterruptedException 当程序发生错误将会抛出InterruptedException
     */
    private void start() throws InterruptedException {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(main,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //自定义分隔符
                        ByteBuf byteBuf = Unpooled.wrappedBuffer("\r\n".getBytes());
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                        ch.pipeline().addLast(myDelimiterHandler);
                    }
                });
        ChannelFuture future = sb.bind(new InetSocketAddress(PORT)).sync();
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                main.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyEchoServer().start();
    }
}
