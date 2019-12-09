package com.sam.netty.chartroom.server;

import com.sam.netty.chartroom.server.handler.MessageEncoder;
import com.sam.netty.chartroom.server.handler.ServerMsgHandler;
import com.sam.netty.chartroom.server.handler.ServerTransferHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 聊天室server端启动类
 * @author sam.liang
 */
public class ChartRoomServerApp {

    private final int PORT;

    private final NioEventLoopGroup bossgroup = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workersgroup = new NioEventLoopGroup();

    public ChartRoomServerApp(int port) {
        this.PORT = port;
    }

    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossgroup,workersgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024 * 10)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new MessageEncoder(),new ServerTransferHandler(),new ServerMsgHandler());
                    }
                });
        ChannelFuture future = b.bind(this.PORT).sync();
        future.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                bossgroup.shutdownGracefully();
                workersgroup.shutdownGracefully();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        new ChartRoomServerApp(8888).start();
    }

}
