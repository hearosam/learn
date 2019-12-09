package com.sam.netty.chartroom.client;

import com.sam.netty.chartroom.client.handler.ClientMsgHandler;
import com.sam.netty.chartroom.client.handler.ClientTransferMsgHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 聊天室启动类
 * @author sam.liang
 */
public class ChatroomClientApp {
    private final int PORT;
    private final String HOST;

    public ChatroomClientApp(String host,int port) {
        this.PORT = port;
        this.HOST = host;
    }

    /**
     * 聊天室客户端启动方法
     */
    public void start() throws InterruptedException {
        NioEventLoopGroup workersgroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workersgroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientTransferMsgHandler(),new ClientMsgHandler());
                    }
                });
        ChannelFuture future = b.connect(this.HOST, this.PORT).sync();
        future.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                workersgroup.shutdownGracefully();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        new ChatroomClientApp("localhost",8888).start();
    }

}
