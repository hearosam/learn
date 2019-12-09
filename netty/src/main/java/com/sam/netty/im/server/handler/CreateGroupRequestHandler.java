package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.CreateGroupRequestPacket;
import com.sam.netty.im.protocal.packet.CreateGroupResponsePacket;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建群聊逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();
    protected CreateGroupRequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket groupRequestPacket) throws Exception {
        List<String> userIdList = groupRequestPacket.getUserIdList();
        //创建一个用户名列表
        List<String> userNameList = new ArrayList<>();

        //创建一个channel分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        //筛选出待加入群聊的用户的channel和username
        Channel channel = null;
        for(String userId : userIdList) {
            channel = SessionUtil.getChannel(userId);
            if(channel!=null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }
        //缓存群信息
        SessionUtil.addChannelGroup(channelGroup.name(),channelGroup);
        //创建群聊创建结果响应
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setUserNameList(userNameList);
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(channelGroup.name());

        //给每一个用户群创建通知
        channelGroup.writeAndFlush(responsePacket);

        System.out.println("群创建成功，id为："+responsePacket.getGroupId());
        System.out.println("群里面有："+responsePacket.getUserNameList());

    }
}
