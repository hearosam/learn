package com.sam.netty.chartroom.server.handler;

import com.sam.netty.chartroom.utils.Message;
import com.sam.netty.chartroom.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import java.util.Date;
import java.util.Scanner;

/**
 * 处理接受消息的handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class ServerMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client键入聊天室");

        Message msg = new Message("chartroom server",new Date(),"hello i am chartroom site");
        ByteBuf buffer = ctx.alloc().buffer();
        String content = MessageUtil.encode(msg);
        buffer.writeBytes(content.getBytes());

        ctx.writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            Message myMsg = (Message) msg;
            MessageUtil.pringMsg(myMsg);
            System.out.println("server: pleas input msg:");
            Scanner scanner = new Scanner(System.in);
            String reply = scanner.nextLine();

            Message replyMsg = new Message("chartroom server", new Date(), reply);
            String content = MessageUtil.encode(replyMsg);

            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(content.getBytes());

            ctx.writeAndFlush(buffer);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
