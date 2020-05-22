package com.sam.network.netty.splicing.delimiter.client;

import com.sam.network.netty.first.handler.NettyClientHandler;
import com.sam.network.netty.splicing.delimiter.client.handler.MyClientDelimiterHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * netty client
 * @author sam.liang
 */
public class NettyEchoClient {

    private static EventLoopGroup worker = new NioEventLoopGroup();
    /**
     * ip地址
     */
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 12345;
    /**
     * 客户端启动方法
     */
    private void start() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //自定义分隔符
                        ByteBuf byteBuf = Unpooled.wrappedBuffer("\r\n".getBytes());
                        //自定义分隔符编码器
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,byteBuf));
                        //客户端handler
                        ch.pipeline().addLast(new MyClientDelimiterHandler());
                    }
                });
        ChannelFuture future = b.connect(new InetSocketAddress(HOST, PORT)).sync();
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                worker.shutdownGracefully();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyEchoClient().start();
    }
}
