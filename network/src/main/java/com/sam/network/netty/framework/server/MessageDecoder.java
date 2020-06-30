package com.sam.network.netty.framework.server;

import com.sam.network.netty.framework.serialization.KryoSerializer;
import com.sam.network.netty.framework.vo.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 解码器
 * @author sam.liang
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        MessagePacket messagePacket = (MessagePacket) KryoSerializer.deserialize(msg);
        out.add(messagePacket);
        ctx.writeAndFlush(out);
    }
}
