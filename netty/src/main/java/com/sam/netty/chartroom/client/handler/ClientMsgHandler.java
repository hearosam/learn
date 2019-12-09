package com.sam.netty.chartroom.client.handler;

import com.sam.netty.chartroom.utils.Message;
import com.sam.netty.chartroom.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;
import java.util.Scanner;

/**
 * 客户端消息handler
 * @author sam.liang
 */
public class ClientMsgHandler  extends SimpleChannelInboundHandler<Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        try {
            MessageUtil.pringMsg(msg);
            Scanner scanner = new Scanner(System.in);
            System.out.println("client:please input msg:");
            String reply = scanner.nextLine();

            Message client = new Message("client", new Date(), reply);
            String content = MessageUtil.encode(client);
            ByteBuf buffer = ctx.alloc().buffer();
            buffer.writeBytes(content.getBytes());

            ctx.writeAndFlush(buffer);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
