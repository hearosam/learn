package com.sam.netty.chartroom.client.handler;

import com.sam.netty.chartroom.utils.Message;
import com.sam.netty.chartroom.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * client 消息转换器 将byte 转换成Message
 * @author sam.liang
 */
public class ClientTransferMsgHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] buff = new byte[2024];
        int length = in.readableBytes();
        in.readBytes(buff, 0, length);

        String totalMsg = new String(buff, 0, length, StandardCharsets.UTF_8);
        String[] content = totalMsg.split("~");
        out.add(new Message(content[0], MessageUtil.parseDateTime(content[1]), content[2]));
    }
}
