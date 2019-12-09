package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.protocal.packet.LoginRequestPacket;
import com.sam.netty.im.protocal.packet.LoginResponsePacket;
import com.sam.netty.im.protocal.packet.MessageResponsePacket;
import com.sam.netty.im.util.LoginUtil;
import com.sam.netty.im.util.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 客户端业务逻辑处理器
 * 这个类废弃掉了，使用PacketDecoder,PacketEncoder,LoginResponseHandler,MessageResponseHandler来处理相关逻辑
 * @author sam.liang
 */
@Deprecated
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当客户端连接上连接上服务服务端的时候会回调该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");
        //创建登录请求数据包对象
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUsername("sam");
        packet.setUserId("001");
        packet.setPassword("pwd");

        //进行编码
        ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(),packet);
        //写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    /**
     * 当接收到数据的时候会回调该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        //解码
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        //判断是否是登录响应数据包
        if(packet instanceof LoginResponsePacket) {
            LoginResponsePacket responsePacket = (LoginResponsePacket) packet;
            if(responsePacket.isSuccess()) {
                System.out.println("登录成功！！！！");
                //标志当前渠道登录成功
                LoginUtil.markAsLogin(ctx.channel());
            }else{
                System.out.println("登录失败！！！！，失败原因是："+responsePacket.getReason());
                ctx.close();
            }
        }else if(packet instanceof MessageResponsePacket){
            MessageResponsePacket responsePacket = (MessageResponsePacket) packet;
            System.out.println("收到服务端消息："+responsePacket.getMessage());
        }else{
            System.out.println("不是登录响应数据包");
        }
    }
}
