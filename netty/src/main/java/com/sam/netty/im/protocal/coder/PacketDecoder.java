package com.sam.netty.im.protocal.coder;

import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 数据包解码处理器（字节数据转java对象）
 * 该类已经废弃可以用{@link PacketEncoder}的decode方法代替了
 * @author sam.liang
 */
@Deprecated
public class PacketDecoder  extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(byteBuf));
    }
}
