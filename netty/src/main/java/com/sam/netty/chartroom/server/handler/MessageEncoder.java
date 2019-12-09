package com.sam.netty.chartroom.server.handler;

import com.sam.netty.chartroom.utils.Message;
import com.sam.netty.chartroom.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 将服务端Message转成byte消息的handler
 * @author sam.liang
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        String content = MessageUtil.encode(msg);
        buffer.getShort(3);
        buffer.writeBytes(content.getBytes());
        ctx.writeAndFlush(buffer);
    }
}
