package com.sam.netty.im.util.console;

import com.sam.netty.im.protocal.command.ConsoleCommand;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 控制台指令管理类
 * @author sam.liang
 */
public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String,ConsoleCommand> commandMap;

    public ConsoleCommandManager(){
        commandMap = new HashMap<>();
        commandMap.put("sendToUser",new SendToUserConsoleCommand());
        commandMap.put("logout",new LogoutConsoleCommand());
        commandMap.put("createGroup",new CreateGroupConsoleCommand());
        commandMap.put("joinGroup",new JoinGroupConsoleCommand());
        commandMap.put("quitGroup",new QuitGroupConsoleCommand());
        commandMap.put("listGroupUser",new ListGroupUserConsoleCommand());
        commandMap.put("sendToGroup",new SendToGroupConsoleCommand());
    }

    @Override
    public void exec(Scanner sc, Channel channel) {
        System.out.println("请输入操作指令");
        //获取第一个指令
        String command = sc.next();
        ConsoleCommand consoleCommand = commandMap.get(command);
        if(consoleCommand != null ) {
            consoleCommand.exec(sc,channel);
        }else{
            System.out.println("未识别指令");
        }
    }
}
