package com.sam.network.netty.udp.broadcast.client;

import com.sam.network.netty.udp.broadcast.common.LogConst;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 广播接收端代码实例
 * @author sam.liang
 */
public class BroadcastClient {

    static class LogBroadcast{
        private final EventLoopGroup worker;
        private final Bootstrap bootstrap;
        public LogBroadcast(InetSocketAddress localAddress) {
            worker = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .option(ChannelOption.SO_REUSEADDR,true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new BroadcastClientDecoder());
                            ch.pipeline().addLast(new BroadcastClientHandler());
                        }
                    })
                    .localAddress(localAddress);
        }
        Channel bind() {
            return bootstrap.bind().syncUninterruptibly().channel();
        }
        void stop() {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        LogBroadcast logBroadcast = new LogBroadcast(new InetSocketAddress(LogConst.MONITOR_SIDE_PORT));
        Channel channel = logBroadcast.bind();

        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logBroadcast.stop();
        }
    }
}
