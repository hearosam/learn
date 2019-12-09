package com.sam.netty.im.commom;

import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义数据包拆包器（用于防止或者解决拆包/粘包问题）
 * @author sam.liang
 */
public class Spliter extends LengthFieldBasedFrameDecoder {
    /**
     * 长度域相对于整个数据包的偏移量（也就是自定义协议里面【数据长度】这个值的偏移量）
     */
    private static final int LENGTH_FIELD_OFFSET = 7;
    /**
     * 长度域的长度（存放长度域的这个数据占了几个字节）
     */
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //拒绝所有非本协议的客户端
        if(in.getInt(in.readerIndex())!= PacketCodeC.MAGIC_NUMBER) {
            //关闭当前通道
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
