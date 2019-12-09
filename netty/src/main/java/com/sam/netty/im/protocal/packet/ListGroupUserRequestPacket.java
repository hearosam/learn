package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 * 列出群聊所有用户数据包
 * @author sam.liang
 */
@Data
public class ListGroupUserRequestPacket extends Packet {
    private String groupId;

    @Override
    public byte getCommand() {
        return Command.LIST_GROUP_USER_REQUEST;
    }
}
