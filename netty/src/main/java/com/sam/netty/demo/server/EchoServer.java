package com.sam.netty.demo.server;

import com.sam.netty.demo.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * netty echo服务端 服务引导类
 * @author sam.liang
 */
public class EchoServer {

    private final int PORT;

    public EchoServer(int port) {
        this.PORT = port;
    }

    public static void main(String[] args) throws InterruptedException {
        /**
         * 整个交互流程
         * 服务端启动并绑定端口，之后启动一条 NIO 线程监听客户端连接；
         * 客户端进行阻塞连接，连接完成时，创建 ByteBuf，发送数据给服务端；
         * 服务端接收到消息，将数据打印到 console 之后，原封数据返回给客户端；
         * 客户端读取数据并打印到 console，完成一次 ping-pong
         */
        //服务器监听端口
        int port = 8080;
        //启动服务
        new EchoServer(port).start();
    }

    /**
     * netty echo 服务器启动
     */
    public void start() throws InterruptedException {
        EchoServerHandler handler = new EchoServerHandler();
        /**
         * 如果 bossGroup 只用于绑定一个端口，那么 groupSize 设为1，因为只会使用到 bossGroup 中的一个 NioEventLoop，如果 groupSize>1，则其他多余的 NioEventLoop 只会白占内存
         */
        //创建EventLoopGroup
        // 1. 配置 bossGroup 和 workerGroup
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //创建serverBootstrap
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                //指定使用的NIO传输渠道
                .channel(NioServerSocketChannel.class)
                //服务端监听指定的服务端口用于接受数据
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.INFO))
                //添加一个EchoServerHandler到子Channel的channelPipeline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sch) throws Exception {
                        sch.pipeline().addLast(handler);
                    }
                });

        //阻塞绑定端口
        ChannelFuture future = b.bind(this.PORT).sync();
        /**
         * 建议使用异步的方式进行优雅关闭，不建议 f.channel().closeFuture().sync(); 同步关闭，因为在实际使用中，主线程还需要继续往后执行，不能阻塞
         */
        //为服务端关闭的channelFuture添加监听器用于实现优雅关闭
        future.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }

}
