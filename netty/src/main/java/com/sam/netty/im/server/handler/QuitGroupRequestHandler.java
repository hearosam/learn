package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.QuitGroupRequestPacket;
import com.sam.netty.im.protocal.packet.QuitGroupResponsePacket;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 退出群聊数据包逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();
    protected QuitGroupRequestHandler() {}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket packet) throws Exception {
        String groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        //构造退出群聊响应数据包
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setUserId(SessionUtil.getSession(ctx.channel()).getUserId());

        if(channelGroup!=null) {
            channelGroup.remove(ctx.channel());
            responsePacket.setSuccess(true);
        }else{
            responsePacket.setSuccess(false);
            responsePacket.setReason("当前群聊不存在");
        }
        channelGroup.writeAndFlush(responsePacket);
    }
}
