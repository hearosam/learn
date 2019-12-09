package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;
import java.util.List;

/**
 * 创建群聊数据包
 * @author sam.liang
 */
@Data
public class CreateGroupRequestPacket extends Packet {
    /**
     * 用户id列表
     */
    private List<String> userIdList;

    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
