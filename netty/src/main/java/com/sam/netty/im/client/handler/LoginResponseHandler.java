package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.LoginRequestPacket;
import com.sam.netty.im.protocal.packet.LoginResponsePacket;
import com.sam.netty.im.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 登录响应数据包逻辑处理器
 * @author sam.liang
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    /**
     * 当客户端连接上连接上服务服务端的时候会回调该方法
     * @param ctx
     * @throws Exception
     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(new Date() + ": 客户端开始登录...");
//        //创建登录请求数据包对象
//        LoginRequestPacket packet = new LoginRequestPacket();
//        packet.setUsername("sam2010");
//        packet.setUserId("001");
//        packet.setPassword("pwd");
//        //写数据
//        ctx.channel().writeAndFlush(packet);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket) throws Exception {
        if(responsePacket.isSuccess()) {
            System.out.println("["+responsePacket.getUserName()+"]登录成功,userId为："+responsePacket.getUserId());
            //标志当前渠道登录成功
            LoginUtil.markAsLogin(ctx.channel());
        }else{
            System.out.println("登录失败！！！！，失败原因是："+responsePacket.getReason());
            ctx.close();
        }
    }

    /**
     * 连接断开的时候就会被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭");
    }
}
