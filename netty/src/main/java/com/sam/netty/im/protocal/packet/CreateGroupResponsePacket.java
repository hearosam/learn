package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

import java.util.List;

/**
 * 群聊创建响应数据包
 * @author sam.liang
 */
@Data
public class CreateGroupResponsePacket extends Packet {
    private String groupId;
    private List<String> userNameList;
    private boolean success;


    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
