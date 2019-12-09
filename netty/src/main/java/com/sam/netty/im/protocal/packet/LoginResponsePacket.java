package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 登录响应数据包对象
 * @author sam.liang
 */
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;
    private String reason;
    private String userId;
    private String userName;

    @Override
    public byte getCommand() {
        return Command.LONGI_RESPONSE;
    }
}
