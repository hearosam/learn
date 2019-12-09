package com.sam.netty.im.util;

import com.sam.netty.im.protocal.command.Command;
import com.sam.netty.im.protocal.command.Packet;
import com.sam.netty.im.protocal.packet.*;
import com.sam.netty.im.serializer.Serializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装协议/解封协议工具类
 * 数据协议 ：
 *      魔数    版本号  序列化算法    指令   数据长度   数据
 *      4字节 | 1字节 |  1字节    |  1字节 | 4字节  |  n字节
 * @author sam.liang
 */
public class PacketCodeC {
    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;
    /**
     * 单例
     */
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    //TODO 这里先用map暂时模拟一下指令跟实现类、序列号算法标识跟序列化算法对象的关联关系
    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LONGI_RESPONSE,LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(Command.LIST_GROUP_USER_REQUEST, ListGroupUserRequestPacket.class);
        packetTypeMap.put(Command.LIST_GROUP_USER_RESPONSE, ListGroupUserResponsePacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(Command.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(Command.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

        serializerMap = new HashMap<>();
        serializerMap.put(Serializer.DEFAULT.getSerializerAlgorithm(), Serializer.DEFAULT);
    }

    /**
     * 封装数据协议
     * @param packet 数据包
     * @return
     */
    public ByteBuf encode(ByteBuf byteBuf,Packet packet) {
        //序列化 java对象
        byte[] serialize = Serializer.DEFAULT.serialize(packet);
        //封装数据
        byteBuf.writeInt(MAGIC_NUMBER);
        //数据包版本号
        byteBuf.writeByte(packet.getVersion());
        //序列化算法类型
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //当前数据包指令
        byteBuf.writeByte(packet.getCommand());
        //负载数据长度
        byteBuf.writeInt(serialize.length);
        //负载数据
        byteBuf.writeBytes(serialize);

        return byteBuf;
    }

    /**
     * 解封数据协议
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        //读指针  跳过4个字节（跳过魔数）
        byteBuf.skipBytes(4);
        //读指针 跳过版本号
        byteBuf.skipBytes(1);
        //读取一个字节 获取序列化算法标识
        byte serializerAlgorithm = byteBuf.readByte();
        //读取一个字节 获取指令
        byte command = byteBuf.readByte();
        //读取一个整型数（也就是4个字节） 获取数据长度
        int size = byteBuf.readInt();
        byte[] bytes = new byte[size];
        //从当前指针开始读取所有数据到一个字节数组里面
        byteBuf.readBytes(bytes);

        //通过指令获取数据包的类型
        Class<? extends Packet> requestType = packetTypeMap.get(command);
        //通过序列化算法标识获取序列化算法对象
        Serializer serializer = serializerMap.get(serializerAlgorithm);
        //反序列化
        if(requestType != null && serializer != null) {
            return serializer.deSerialize(requestType,bytes);
        }
        return null;
    }
}
