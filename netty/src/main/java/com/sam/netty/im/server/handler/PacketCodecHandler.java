package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 数据包编解码器 可以把PacketDecoder跟PacketEncoder集成到一个类里面
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf,Packet> {

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();
    protected PacketCodecHandler(){}
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        PacketCodeC.INSTANCE.encode(byteBuf,packet);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(PacketCodeC.INSTANCE.decode(byteBuf));
    }
}
