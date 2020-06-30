package com.sam.network.netty.framework.server;

import com.sam.network.netty.framework.vo.MessagePacket;
import com.sam.network.netty.framework.vo.MessageType;
import com.sam.network.netty.framework.vo.PacketHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 心跳响应处理handler
 */
public class HeartBeatRespHandler extends SimpleChannelInboundHandler<MessagePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePacket msg) throws Exception {
        if (msg.getPacketHeader()!=null && msg.getPacketHeader().getType() == MessageType.HEART_REQ.value()) {
            MessagePacket messagePacket = buildHeartBeatResp();
            ctx.writeAndFlush(messagePacket);
            //释放msg : SimpleChannelHandler ->会自动调用release方法
            //如果不继承 SimpleChannelInboundHandler->需要手动释放->ReferenceCountUtil.release(msg)
//            ReferenceCountUtil.release(msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 构建心跳响应报文
     * @return 心跳报文数据包
     */
    private MessagePacket buildHeartBeatResp() {
        MessagePacket packet = new MessagePacket();
        PacketHeader header = new PacketHeader();
        header.setType(MessageType.HEART_RESP.value());
        packet.setPacketHeader(header);
        packet.setBody((byte)0);
        return packet;
    }
}
