package com.sam.netty.im.protocal.coder;

import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据包编码器(java对象编码成二进制对象)
 * 该类已经废弃可以用{@link PacketEncoder}的encode方法代替了
 * @author sam.liang
 */
@Deprecated
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodeC.INSTANCE.encode(byteBuf,packet);
    }
}
