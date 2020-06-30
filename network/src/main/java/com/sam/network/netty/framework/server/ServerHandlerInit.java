package com.sam.network.netty.framework.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerHandlerInit extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //使用固定长度的消息编解码器来解决粘包半包问题
        ch.pipeline().addLast();
    }
}
