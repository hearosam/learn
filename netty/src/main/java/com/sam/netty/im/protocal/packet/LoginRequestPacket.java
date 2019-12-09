package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 登录请求数据包对象
 * @author sam.liang
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;
    private String username;
    private String password;

    @Override
    public byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
