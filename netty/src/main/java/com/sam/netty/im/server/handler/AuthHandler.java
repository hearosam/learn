package com.sam.netty.im.server.handler;

import com.sam.netty.im.util.LoginUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理用户登录认证的handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();
    protected AuthHandler(){}

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!LoginUtil.hasLogin(ctx.channel())) {
            //当前通道还没有登录
            //关闭当前通道
            ctx.channel().close();
        }else{
            //当前通道已经绑定了用户信息，放行给后面handler执行
            super.channelRead(ctx,msg);
            ctx.pipeline().remove(this);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证，移除AuthHandler！");
        }else{
            System.out.println("无法登录验证，强制关闭连接！");
        }
    }
}
