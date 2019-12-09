package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.CreateGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建群聊指令操作
 * @author sam.liang
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        System.out.print("【拉人群聊】输入用户id列表，用户id要以因为逗号隔开：");
        String userIds = sc.next();
        packet.setUserIdList(Arrays.asList(userIds.split(",")));
        channel.writeAndFlush(packet);
    }
}
