package com.sam.netty.im.serializer;


import com.sam.netty.im.serializer.impl.JsonSerializer;

/**
 * 定义序列化规则
 * @author sam.liang
 */
public interface Serializer {
    /**
     * json序列化
     */
    byte JSON_SERIALIZER = 1;
    /**
     * 默认序列化算法
     */
    Serializer DEFAULT = new JsonSerializer();

    /**
     *  序列化算法
     * @return
     */
    Byte getSerializerAlgorithm();

    /**
     * 序列化方法
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化方法
     * @param clazz 反序列化的class类型
     * @param bytes 反序列化的字节数组
     * @param <T> 返回对象
     * @return
     */
    <T> T deSerialize(Class<T> clazz, byte [] bytes);
}
