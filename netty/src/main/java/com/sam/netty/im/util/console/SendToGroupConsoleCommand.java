package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.GroupMessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 发送信息到指定群指令
 * @author sam.liang
 */
public class SendToGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.println("请输入消息格式为：群id+空格+发送消息");
        String groupId = sc.next();
        String message = sc.nextLine();

        GroupMessageRequestPacket packet = new GroupMessageRequestPacket();
        packet.setGroupId(groupId);
        packet.setMessage(message);

        channel.writeAndFlush(packet);
    }
}
