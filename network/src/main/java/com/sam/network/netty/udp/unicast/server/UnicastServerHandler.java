package com.sam.network.netty.udp.unicast.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * 单播应答端handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class UnicastServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    /*应答的具体内容从常量字符串数组中取得，由nextQuote方法随机获取*/
    private static final String[] DICTIONARY = {
            "只要功夫深，铁棒磨成针。",
            "旧时王谢堂前燕,飞入寻常百姓家。",
            "洛阳亲友如相问，一片冰心在玉壶。",
            "一寸光阴一寸金，寸金难买寸光阴。",
            "老骥伏枥，志在千里，烈士暮年，壮心不已" };
    private static Random r = new Random();

    /**
     * 网卡缓存区有数据可读就调用该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf content = msg.content();
        String request = content.toString(StandardCharsets.UTF_8);
        if (request.equals("88")) {
            ctx.close();
            return;
        }
        //响应古诗给请求端
        String response = DICTIONARY[r.nextInt(4)];
        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(response.getBytes()),msg.sender()));
    }
}
