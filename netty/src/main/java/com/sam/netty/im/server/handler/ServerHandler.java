package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.protocal.packet.LoginRequestPacket;
import com.sam.netty.im.protocal.packet.LoginResponsePacket;
import com.sam.netty.im.protocal.packet.MessageRequestPacket;
import com.sam.netty.im.protocal.packet.MessageResponsePacket;
import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端逻辑处理器
 * 这个类废弃掉了，使用PacketDecoder,PacketEncoder,LoginRequestHandler,MessageRequestHandler来处理相关逻辑
 * @author sam.liang
 */
@Deprecated
public class ServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当接受到数据的时候会调用这个方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        //进行解码
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        //判断是否是登录请求的数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket responsePacket = new LoginResponsePacket();
            responsePacket.setVersion(loginRequestPacket.getVersion());
            if(valid(loginRequestPacket)) {
                //校验成功
                responsePacket.setSuccess(true);
            }else{
                //校验失败
                responsePacket.setSuccess(false);
                responsePacket.setReason("帐号密码校验失败");
            }
            //进行响应处理
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), responsePacket);
            //写数据
            ctx.channel().writeAndFlush(responseByteBuf);
        }else if(packet instanceof MessageRequestPacket){
            MessageRequestPacket messagePacket = (MessageRequestPacket) packet;
            System.out.println("收到客户端消息:"+messagePacket.getMessage());

            //创建服务端响应消息
            MessageResponsePacket responsePacket = new MessageResponsePacket();
            responsePacket.setMessage("服务端返回消息【"+messagePacket.getMessage()+"】");

            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), responsePacket);
            //写数据
            ctx.channel().writeAndFlush(responseByteBuf);
        }else{
            System.out.println("不是登录请求的数据包");
        }
    }

    /**
     * 登陆校验
     * @param packet 数据包对象
     * @return
     */
    private boolean valid(Packet packet) {
        return true;
    }
}
