package com.sam.netty.im.protocal.packet;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import lombok.Data;

/**
 *
 * @author sam.liang
 */
@Data
public class GroupMessageResponsePacket extends Packet {
    private String userName;
    private String message;

    @Override
    public byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
