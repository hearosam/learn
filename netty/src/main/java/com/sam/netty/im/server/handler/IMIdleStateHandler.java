package com.sam.netty.im.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 空闲检查handler
 * @author sam.liang
 */
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READ_IDLE_TIME = 15;
    public IMIdleStateHandler() {
        super(READ_IDLE_TIME,0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READ_IDLE_TIME+"秒没有读取到数据，连接关闭");
        ctx.channel().close();
    }
}
