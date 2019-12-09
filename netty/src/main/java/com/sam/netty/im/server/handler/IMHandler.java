package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 将其他handler合并到当前这个handler里面做处理，缩短事件的传播路径
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class IMHandler extends SimpleChannelInboundHandler<Packet> {
    private Map<Byte,SimpleChannelInboundHandler<? extends Packet>> handlerMap;
    public static final IMHandler INSTANCE = new IMHandler();

    protected IMHandler() {
        handlerMap = new HashMap<>();
        handlerMap.put(Command.GROUP_MESSAGE_REQUEST,GroupMessageRequestHandler.INSTANCE);
        handlerMap.put(Command.LIST_GROUP_USER_REQUEST,ListGroupUserRequestHandler.INSTANCE);
        handlerMap.put(Command.QUIT_GROUP_REQUEST,QuitGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.CREATE_GROUP_REQUEST,CreateGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.JOIN_GROUP_REQUEST,JoinGroupRequestHandler.INSTANCE);
        handlerMap.put(Command.MESSAGE_REQUEST,MessageRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        handlerMap.get(packet.getCommand()).channelRead(ctx,packet);
    }


}
