package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.QuitGroupRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 退出群聊指令
 * @author sam.liang
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.print("请输入去聊ID：");
        String groupId = sc.next();

        QuitGroupRequestPacket packet = new QuitGroupRequestPacket();
        packet.setGroupId(groupId);
        channel.writeAndFlush(packet);
    }
}
