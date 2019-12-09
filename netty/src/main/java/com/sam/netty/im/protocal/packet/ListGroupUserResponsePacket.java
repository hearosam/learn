package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

import java.util.List;

/**
 * 列出群聊所有用户响应数据包
 * @author sam.liang
 */
@Data
public class ListGroupUserResponsePacket extends Packet {
    private List<String> userNameList;
    private String groupId;
    private boolean success;
    private String reason;

    @Override
    public byte getCommand() {
        return Command.LIST_GROUP_USER_RESPONSE;
    }
}
