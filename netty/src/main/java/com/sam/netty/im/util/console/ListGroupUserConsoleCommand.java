package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.ListGroupUserRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 列出群聊所有用户指令
 * @author sam.liang
 */
public class ListGroupUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.print("请输入群聊ID：");
        String groupId = sc.next();

        ListGroupUserRequestPacket packet = new ListGroupUserRequestPacket();
        packet.setGroupId(groupId);

        channel.writeAndFlush(packet);
    }
}
