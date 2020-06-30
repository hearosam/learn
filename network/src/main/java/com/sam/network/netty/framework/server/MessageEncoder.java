package com.sam.network.netty.framework.server;

import com.sam.network.netty.framework.serialization.KryoSerializer;
import com.sam.network.netty.framework.vo.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 编码器
 * @author sam.liang
 */
public class MessageEncoder extends MessageToMessageEncoder<MessagePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessagePacket msg, List<Object> out) throws Exception {
        //根据消息的长度创建buffer缓冲区
        ByteBuf buffer = Unpooled.buffer(msg.getPacketHeader().getLength());
        KryoSerializer.serialize(msg,buffer);
        out.add(buffer);
        ctx.writeAndFlush(out);
    }
}
