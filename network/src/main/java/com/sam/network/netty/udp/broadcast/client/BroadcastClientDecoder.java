package com.sam.network.netty.udp.broadcast.client;

import com.sam.network.netty.udp.broadcast.common.LogMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息解码器
 * @author sam.liang
 */
public class BroadcastClientDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out) throws Exception {
        //8 8 负载消息长度 分隔符
        ByteBuf byteBuf = packet.content();
        long msgId = byteBuf.readLong();
        long time = byteBuf.readLong();
        //读取分隔符
        byteBuf.readByte();
        ByteBuf slice = byteBuf.slice();
        String content = slice.toString(StandardCharsets.UTF_8);
        LogMsg msg = new LogMsg(packet.sender(),content,msgId,time);
        out.add(msg);
    }
}
