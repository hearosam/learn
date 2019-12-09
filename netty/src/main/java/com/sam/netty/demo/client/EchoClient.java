package com.sam.netty.demo.client;

import com.sam.netty.demo.client.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * netty echo 客户端引导
 * @author sam.liang
 */
public class EchoClient {
    private final int PORT;

    /**
     * 最佳实践：通常一个服务可能会调用多个其他服务，针对每一个被调服务，会创建一个 NettyClient
     * 与被调服务的 NettyServer 进行通信，为了防止 NioEventLoop 太多，
     * 多个 NettyClient 可以共享一个 NioEventLoopGroup。
     */
    final static EventLoopGroup group = new NioEventLoopGroup();

    private final String HOST;

    public EchoClient(String host,int port) {
        this.PORT = port;
        this.HOST = host;
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient("localhost",8080).start();
    }

    /**
     * echo 客户端连接
     */
    public void start() throws InterruptedException {
        //创建并配置客户端启动辅助类 Bootstrap
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sch) throws Exception {
                        sch.pipeline().addLast(new EchoClientHandler());
                    }
                });
        //阻塞连接服务端
        ChannelFuture future = b.connect(this.HOST,this.PORT).sync();
        //为channelFuture添加监听器实现优雅关闭
        future.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                group.shutdownGracefully();
            }
        });
    }

}
