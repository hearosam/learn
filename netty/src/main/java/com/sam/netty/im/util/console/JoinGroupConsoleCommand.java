package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.JoinGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 进入群聊操作指令
 * @author sam.liang
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        JoinGroupRequestPacket packet = new JoinGroupRequestPacket();
        System.out.print("请输入群聊ID");
        String groupId = sc.next();
        packet.setGroupId(groupId);

        channel.writeAndFlush(packet);
    }
}
