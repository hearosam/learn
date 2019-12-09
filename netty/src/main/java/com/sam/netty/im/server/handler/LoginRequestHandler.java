package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.protocal.command.Session;
import com.sam.netty.im.protocal.packet.LoginRequestPacket;
import com.sam.netty.im.protocal.packet.LoginResponsePacket;
import com.sam.netty.im.util.LoginUtil;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 处理登录请求数据包的逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    /**
     * 单例模式所有客户端共享一个handler（单例模式必须要加@Sharable注解，不然会报错）
      */
    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(loginRequestPacket.getVersion());
        if(valid(loginRequestPacket)) {
            //校验成功
            responsePacket.setSuccess(true);
            Channel channel = ctx.channel();
            responsePacket.setUserId(channel.id()+"");
            responsePacket.setUserName(loginRequestPacket.getUsername());

            System.out.println(new Date() + "---"+loginRequestPacket.getUsername()+": 登录成功!");
            //标识用户登录状态
            LoginUtil.markAsLogin(ctx.channel());
            //建立用户跟渠道的映射关系
            SessionUtil.bindSession(new Session(channel.id()+"^_^",channel.id()+""),channel);
        }else{
            //校验失败
            responsePacket.setSuccess(false);
            responsePacket.setReason("帐号密码校验失败");
            System.out.println(new Date() + ": 登录失败!");
        }
        //写数据
//        ctx.channel().writeAndFlush(responsePacket);
        ctx.writeAndFlush(responsePacket);
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
