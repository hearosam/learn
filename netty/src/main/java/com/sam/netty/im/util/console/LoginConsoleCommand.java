package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import com.sam.netty.im.protocal.packet.LoginRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台登录指令
 * @author sam.liang
 */
public class LoginConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.println("请输入用户名：");
        String userName = sc.nextLine();
        //构造一个登录数据包
        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUsername(userName);
        packet.setPassword("pwd");

        channel.writeAndFlush(packet);
        waitForLoginResponse();
    }
    private static void waitForLoginResponse() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
