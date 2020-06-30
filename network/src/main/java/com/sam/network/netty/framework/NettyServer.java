package com.sam.network.netty.framework;


import com.sam.network.netty.framework.vo.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * netty 服务端启动类
 */
public class NettyServer {

    private static final Log LOG = LogFactory.getLog(NettyServer.class);

    private int port;

    private NettyServer(int port) {
        this.port = port;
    }

    private void bind() throws InterruptedException {
        EventLoopGroup main = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(main,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(null)
                .localAddress(this.port);

        sb.bind().sync().channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                main.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });


    }
    public static void main(String[] args) throws InterruptedException {
        //异常处理
        new NettyServer(NettyConstant.PORT).bind();
    }

}
