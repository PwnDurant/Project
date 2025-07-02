package com.wzc.mq.common;


import com.fasterxml.jackson.core.type.TypeReference;
import com.wzc.mq.mqserver.core.Message;

import java.io.*;

/**
 * 二进制序列化工具
 * 如果想实现序列化和反序列化，其对象必须实现 Serializable 接口
 */
public class BinaryTool {

    /**
     * 把一个对象序列化成一个字节数组
     * ByteArrayOutputStream : 流对象 -> 这里作为变长数组使用
     * ObjectOutputStream : Java 标准库中用来实现对象的二进制序列化和反序列化的类
     * @param object 需要被序列化的对象
     * @return 返回对像的二进制数据的字节数组
     */
    public static byte[] toBytes(Object object) throws IOException {
//        由于不知道序列化后的对象的长度是多少，所以需要一个变长数组，这里引用 ByteArrayOutputStream 这个流对象
//        把 object 序列化的数据逐渐写入到 byteArrayOutputStream 中，再统一转为 byte[]
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)){
//                这里 writeObject 方法就会将 object 序列化的数据写入到 objectOutputStream 中
//                objectOutputStream 又关联了 byteArrayOutputStream 所以最后结果就写入到 byteArrayOutputStream 中了
                objectOutputStream.writeObject(object);
            }
//            把 byteArrayOutputStream 中的二进制数据取出来然后转换为 byte[]
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * 将字节数组中的数据反序列化成对象
     * @param data 需要被反序列化的字节数组
     * @return 返回指定的对象
     */
    public static Message fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        Message message = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)){
            try(ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)){
//                readObject 将 data 中的字节数组读取并进行反序列化
                message = (Message) objectInputStream.readObject();
            }
        }
        return message;
    }

}
