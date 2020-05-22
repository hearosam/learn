package com.sam.network.netty.udp.unicast.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * 单播演示示例
 * @author sam.liang
 */
public class UnicastClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioDatagramChannel.class)
                .handler(new UnicastClientHandler());

        //0表示不绑定端口
        Channel channel = bootstrap.bind(0).sync().channel();
        //发送数据
        channel.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer("给我一句古诗".getBytes()),new InetSocketAddress("127.0.0.1",12345)));

        channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                worker.shutdownGracefully();
            }
        });

    }
}
