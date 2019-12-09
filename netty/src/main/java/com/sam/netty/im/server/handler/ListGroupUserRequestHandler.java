package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.ListGroupUserRequestPacket;
import com.sam.netty.im.protocal.packet.ListGroupUserResponsePacket;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 列出群聊所有用户请求数据包逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class ListGroupUserRequestHandler extends SimpleChannelInboundHandler<ListGroupUserRequestPacket> {

    public static final ListGroupUserRequestHandler INSTANCE = new ListGroupUserRequestHandler();
    protected ListGroupUserRequestHandler() {}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupUserRequestPacket packet) throws Exception {
        String groupId = packet.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        List<String> userNameList = new ArrayList<>();

        ListGroupUserResponsePacket responsePacket = new ListGroupUserResponsePacket();
        responsePacket.setGroupId(groupId);

        //构造列出群聊所有用户响应结果数据包
        if (channelGroup != null) {
            for(Channel chanel : channelGroup) {
              userNameList.add(SessionUtil.getSession(chanel).getUserName());
            }
            responsePacket.setSuccess(true);
            responsePacket.setUserNameList(userNameList);
        }else{
            responsePacket.setSuccess(false);
            responsePacket.setReason("当前群聊不存在");
        }

//        ctx.channel().writeAndFlush(responsePacket);
        ctx.writeAndFlush(responsePacket);
    }
}
