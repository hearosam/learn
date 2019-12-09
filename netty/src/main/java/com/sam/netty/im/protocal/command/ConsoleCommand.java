package com.sam.netty.im.protocal.command;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 控制台指令
 * @author sam.liang
 */
public interface ConsoleCommand {
    /**
     * 指令执行类
     * @param sc
     * @param channel
     */
    void exec(Scanner sc, Channel channel);
}
