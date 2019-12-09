package com.sam.netty.chartroom.server.handler;

import com.sam.netty.chartroom.utils.Message;
import com.sam.netty.chartroom.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 用于处理输入消息的handler
 * @author sam.liang
 */
public class ServerTransferHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String totalMsg = in.readCharSequence(in.readableBytes(), Charset.forName("utf-8")).toString();
        String[] content = totalMsg.split("~");
        out.add(new Message(content[0], MessageUtil.parseDateTime(content[1]),content[2]));
    }
}
