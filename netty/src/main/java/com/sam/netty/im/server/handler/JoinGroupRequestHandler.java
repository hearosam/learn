package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.JoinGroupRequestPacket;
import com.sam.netty.im.protocal.packet.JoinGroupResponsePacket;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 加入群聊数据包逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();
    protected JoinGroupRequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket packet) throws Exception {
        String groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        String userId = SessionUtil.getSession(ctx.channel()).getUserId();
        //将当前channel加入到群聊中
        channelGroup.add(ctx.channel());
        //构造加群响应发送给客户端
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSuccess(true);
        responsePacket.setUserId(userId);

        channelGroup.writeAndFlush(responsePacket);
    }
}
