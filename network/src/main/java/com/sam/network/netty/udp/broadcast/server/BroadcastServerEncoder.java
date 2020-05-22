package com.sam.network.netty.udp.broadcast.server;

import com.sam.network.netty.udp.broadcast.common.LogMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 广播发送端handler
 * 将日志实体类消息编码成DatagramPacket
 * @author sam.liang
 */
public class BroadcastServerEncoder extends MessageToMessageEncoder<LogMsg> {

    private InetSocketAddress remoteAddress;

    public BroadcastServerEncoder(InetSocketAddress remoteAddress){
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogMsg msg, List<Object> out) throws Exception {
        //计算字节缓冲区大小8 + 8 + 数据内容长度+分隔符(1个字节)
        String content = msg.getMsg();
        byte[] contentBytes = content.getBytes();
        ByteBuf byteBuf = Unpooled.buffer(8*2+contentBytes.length+1);
        byteBuf.writeLong(msg.getMsgId());
        byteBuf.writeLong(msg.getTime());
        byteBuf.writeByte(LogMsg.SEPARATOR);
        byteBuf.writeBytes(contentBytes);

        ctx.writeAndFlush(new DatagramPacket(byteBuf,remoteAddress));
    }
}
