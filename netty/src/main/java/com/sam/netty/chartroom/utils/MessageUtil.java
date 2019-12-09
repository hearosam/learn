package com.sam.netty.chartroom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息发送工具类
 * @author sam.liang
 */
public class MessageUtil {

    public static String encode(Message msg) {
        return msg.getUsername()+ "~" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(msg.getSentTime())+"~"+msg.getMsg();
    }

    public static String formatDateTime(Date time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    public static Date parseDateTime(String time) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void pringMsg(Message msg) {
        System.out.println("=================================================================================================");
        System.out.println("                      " + formatDateTime(msg.getSentTime()) + "                     ");
        System.out.println(msg.getUsername() + ": " + msg.getMsg());
        System.out.println("=================================================================================================");
    }
}
