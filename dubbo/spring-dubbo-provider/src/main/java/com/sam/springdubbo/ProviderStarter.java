package com.sam.springdubbo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProviderStarter {

    public static void main(String[] args) throws IOException {
        Map<String,Byte> map = new HashMap<String,Byte>();
        map.put("q",(byte)0);
        System.out.println(map.get("q") == null);
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/dubbo.xml");
//        context.start();
//        System.in.read(); // 按任意键退出
    }
}
