package com.sam.netty.demo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * netty echo 服务器端
 *
 * Sharable注解当前的EchoServerHandler可以被多个channel安全的共享
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server Recive Data : " + in.toString(CharsetUtil.UTF_8));
        //创建响应 ButeBuf,回写接受到的信息
        ByteBuf resp = Unpooled.wrappedBuffer(in);
        //将响应消息写入发送缓冲区
        ctx.write(resp);

    }

    /**
     * 通知ChannelInBoundHandler最后一次对channelRead的调用是当前批量读取中最后一条消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将发送缓冲数组中的消息全部冲刷到SocketChannel中
        ctx.flush();
    }

    /**
     * 在读取操作期间，有异常抛出时会调用该方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常栈跟踪
        cause.printStackTrace();
        //关闭channel
        ctx.close();
    }
}
