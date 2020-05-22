package com.sam.network.bio.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * bio socket 客户端
 * @author sam.liang
 */
public class BioSocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        System.out.println("开始连接服务器");
        socket.connect(new InetSocketAddress("127.0.0.1",8090));

        //使用一个线程接受服务端返回消息
        new ReceiveMessageThread(socket).start();
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        while (true) {
            System.out.println("请输入消息：");
            pw.println(new Scanner(System.in).nextLine());
            pw.flush();
        }
    }
}

/**
 * 服务器消息接受线程
 * @author sam.liang
 */
class ReceiveMessageThread extends Thread {
    private Socket socket;

    public ReceiveMessageThread(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String receiveMessage = null;
            while ((receiveMessage = br.readLine()) != null) {
                System.out.println(receiveMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clear();
        }
    }

    public void clear() {
        if (socket!=null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}