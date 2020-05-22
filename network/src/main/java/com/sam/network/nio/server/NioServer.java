package com.sam.network.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * nio server network program
 * @author sam.liang
 */
public class NioServer {
    public static void main(String[] args) {
        new Thread(new ServerHandler(12345)).start();
    }
}

/**
 * server socket handler
 * @author sam.liang
 */
class ServerHandler implements Runnable{
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean started;

    public ServerHandler(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("server start port:"+ port);
            started = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (started) {
            try {
                selector.select();
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
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //回收资源
        if (selector!=null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * selector 事件處理方法
     * @param key 事件
     */
    private void handleKeyEvent(SelectionKey key) throws IOException {
        if(key.isValid()) {
            //连接事件
            if (key.isAcceptable()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                //连接成功之后注册读事件接受客户端数据
                System.out.println("server: receive client connect");
                socketChannel.configureBlocking(false);
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            //读事件
            else if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = socketChannel.read(buffer);
                if(read>0) {
                    buffer.flip();
                    byte[] b = new byte[buffer.remaining()];
                    buffer.get(b);
                    String result = new String(b, StandardCharsets.UTF_8);
                    System.out.println("receive client message :" + result);
                    //再次将socketChannel注册到selector上且监控读事件，继续处理后续读事件
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    //响应数据到客户端
                    doWrite(socketChannel,"server response : "+result);
                }else if (read < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel,String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        socketChannel.write(buffer);
    }
}