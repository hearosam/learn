package com.sam.network.netty.udp.broadcast.server;

import com.sam.network.netty.udp.broadcast.common.LogConst;
import com.sam.network.netty.udp.broadcast.common.LogMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 广播发送端演示
 * @author sam.liang
 */
public class BroadcastServer {

    static class LogEventBroadcast {

        private final Bootstrap bootstrap;
        private final EventLoopGroup worker;

        LogEventBroadcast(InetSocketAddress remoteAddress){
            worker = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new BroadcastServerEncoder(remoteAddress));
        }

        void start() throws InterruptedException {
            Channel channel = bootstrap.bind(0).sync().channel();
            int count = 0 ;
            for (;;){
                channel.writeAndFlush(new LogMsg(null,count++, LogConst.getLogInfo()));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /**
         * stop the server
         */
        void stop(){
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        LogEventBroadcast broadcast = new LogEventBroadcast(new InetSocketAddress("255.255.255.255",LogConst.MONITOR_SIDE_PORT));
        try {
            broadcast.start();
        } catch (InterruptedException e) {
            broadcast.stop();
        }
    }
}
