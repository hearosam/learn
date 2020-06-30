package com.sam.network.netty.framework.server;

import com.sam.network.netty.framework.vo.MessagePacket;
import com.sam.network.netty.framework.vo.MessageType;
import com.sam.network.netty.framework.vo.PacketHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 握手请求处理handler
 *  1.检查重复登录
 *  2.登录ip是否在白名单
 * @author sam.liang
 */
public class LoginAuthRespHandler extends SimpleChannelInboundHandler<MessagePacket> {

    /**
     * 登录ip白名单限制(在真实环境这个白名单应该存放数据库然后加载到内存由redis在内存维护)
     */
    private static final String[] whiteList = new String[]{"127.0.0.1"};
    /**
     * 登录用户,通过ip限制
     */
    private Map<String,Byte> loginUserList = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePacket msg) throws Exception {
        if (msg.getPacketHeader()!=null && MessageType.LOGIN_REQ.value() == msg.getPacketHeader().getType()) {
            String address = ctx.channel().remoteAddress().toString();
            //白名单校验是否通过
            boolean allowAccess = false;
            MessagePacket messagePacket = null;
                    //先进行重复登录校验
            Byte byteFlag = loginUserList.get(address);
            if (byteFlag != null)  {
                //白名单检验
                for (String allowIp : whiteList) {
                    if (allowIp.equals(address)) {
                        allowAccess = true;
                        break;
                    }
                }
                messagePacket = allowAccess ? buildLoginResp((byte) 0) :  buildLoginResp((byte) -1);
                if (allowAccess) {
                    loginUserList.put(address,(byte)0);
                }
            }else{
                messagePacket = buildLoginResp((byte) -1);
            }
            //构建登录处理数据包响应客户端
            ctx.writeAndFlush(messagePacket);
            //释放msg : SimpleChannelHandler ->会自动调用release方法
            //如果不继承 SimpleChannelInboundHandler->需要手动释放->ReferenceCountUtil.release(msg)
//            ReferenceCountUtil.release(msg);
        }else {
            //把消息传递到下一个handler
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 构造一个登录成功消息响应给客户端
     * @return
     */
    private MessagePacket buildLoginResp(byte respCode) {
        MessagePacket messagePacket = new MessagePacket();
        PacketHeader header = new PacketHeader();
        header.setType(MessageType.LOGIN_RESP.value());
        messagePacket.setPacketHeader(header);
        messagePacket.setBody(respCode);
        return messagePacket;
    }
}
