package com.sam.network.netty.framework;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty 客户端
 * @author sam.liang
 */
public class NettyClient implements Runnable{

    //创建一个线程池用户执行重连操作
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    /**
     * 客户端是否连接成功
     */
    private volatile boolean connected = false;
    /**
     * 是否是用户手动关闭
     */
    private volatile boolean isUserClosed = false;

    /**
     * 登录重试次数
     */
    private AtomicInteger loginRetryCount = new AtomicInteger(0);

    public boolean isConnected(){
        return this.connected;
    }

    private Channel channel;
    private EventLoopGroup worker = new NioEventLoopGroup();
    public void connect(String ip,int port) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .handler(null)
                .localAddress(new InetSocketAddress(ip,port));
        channel = b.connect().sync().channel();
        channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                worker.shutdownGracefully();
            }
        });
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
       String ss = "SELECT %s FROM '%s' for update ";
        System.out.println(String.format(ss, "ssdff", "fff"));

    }
}
