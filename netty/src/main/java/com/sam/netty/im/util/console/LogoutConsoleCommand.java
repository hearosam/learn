package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 登出指令
 * @author sam.liang
 */
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner sc, Channel channel) {
     }
}
