package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 发送消息给指定用户
 * @author sam.liang
 */
public class SendToUserConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
        //输入格式：消息目标用户id+空格+具体消息
        System.out.print("请输入发送消息：");
        String toUserId = sc.next();
        String message = sc.nextLine();

        MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
        messageRequestPacket.setToUserId(toUserId);
        messageRequestPacket.setMessage(message);

        channel.writeAndFlush(messageRequestPacket);
    }
}
