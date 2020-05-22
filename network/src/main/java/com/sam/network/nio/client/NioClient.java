package com.sam.network.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * nio network program
 * @author sam.liang
 */
public class NioClient {

    private static NioClientHandler handler;
    public static void main(String[] args) throws IOException {
        start();
        for (;;) {
            System.out.println("请输入消息");
            handler.sendMsg(new Scanner(System.in).nextLine());
        }
    }
    public static void start() {
        if (handler != null) {
            handler.stop();
        }
        handler = new NioClientHandler("127.0.0.1",12345);
        new Thread(handler).start();
    }
}

class NioClientHandler implements Runnable {

    private String ip;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean started;

    public NioClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            //创建selector
            this.selector = Selector.open();
            //创建socketChannel
            this.socketChannel = SocketChannel.open();
            this.socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void run() {
        //处理连接
        try {
            doConnect();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        //阻塞方法，监听当前selector上是否存在注册的事件
        while (started) {
            try {
                selector.select();
                //获取selector上的事件
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    try {
                        handleKeyEvent(key);
                    } catch (IOException e) {
                        if (key!=null) {
                            key.cancel();
                            if (key.channel()!=null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //释放资源
        if(selector != null ) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理selector事件
     * @param key selector事件
     */
    private void handleKeyEvent(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            //连接事件
            if(key.isConnectable()) {
                if (channel.finishConnect()) {
                }else{
                    System.exit(1);
                }
            }
            //读事件
            if (key.isReadable()) {
                //向堆申请1k内存
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = channel.read(buffer);
                if (read > 0 ) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    System.out.println("receive server message :"+new String(bytes,StandardCharsets.UTF_8));
                }else if (read < 0) {
                    key.cancel();
                    channel.close();
                }
            }
        }
    }

    /**
     * 处理连接方法
     * @throws IOException
     */
    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(this.ip,this.port))) {}
        else {
            //向selector注册op_connect前客户端channel关心就绪状态,表示当连接事件
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
    }
    public void sendMsg(String msg) throws IOException {
        //注册读事件
        socketChannel.register(selector,SelectionKey.OP_READ);
        doWrite(socketChannel,msg);
    }
    public void stop() {
        this.started = false;
    }
}
