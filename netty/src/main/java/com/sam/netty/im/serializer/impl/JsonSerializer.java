package com.sam.netty.im.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.sam.netty.im.serializer.Serializer;
import com.sam.netty.im.serializer.SerializerAlgorithm;

/**
 * JSON序列化
 * @author sam.liang
 */
public class JsonSerializer implements Serializer {
    /**
     * 返回序列化算法
     * @return
     */
    @Override
    public Byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }
    /**
     * 对象序列化为字节数组
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }
    /**
     * 字节数组序列化为java对象
     * @param clazz 反序列化的class类型
     * @param bytes 反序列化的字节数组
     * @param <T>
     * @return
     */
    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
