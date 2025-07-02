package com.wzc.mq.mqserver.datacenter;


import java.io.*;
import java.util.Scanner;

/**
 * 这个类实现对硬盘消息的管理
 */
public class MessageFileManager {

//    定义一个内部类来表示该队列的统计信息
    static public class Stat{
        public int totalCount;
        public int validCount;
}

    /**
     * 需要准备的初始化工作
     */
    public void init(){

    }

    /**
     * 获取指定队列对应的消息文件所在的路径
     */
    private String getQueueDir(String queueName){
        return "./data/"+queueName;
    }

    /**
     * 这个方法获取该队列的消息数据文件路径
     * 二进制文件一般不使用 txt 作为后缀，一般使用 .bin / .dat
     */
    private String getQueueDataPath(String queueName){
        return getQueueDir(queueName)+"/queue_data.txt";
    }

    /**
     * 这个方法用来获取该消息的统计文件路径
     */
    private String getQueueStatPath(String queueName){
        return getQueueDir(queueName)+"/queue_stat.txt";
    }

    /**
     * 读取指定队列的消息统计文件信息
     * @param queueName 指定的队列
     * @return 统计信息 -> Stat
     */
    private Stat readStat(String queueName){
//        由于是文本文件所以可以直接使用 Scanner 来读取
        Stat stat = new Stat();
        try (FileInputStream fileInputStream = new FileInputStream(getQueueStatPath(queueName))){
            Scanner scanner = new Scanner(fileInputStream);
            stat.totalCount = scanner.nextInt();
            stat.validCount = scanner.nextInt();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定消息统计文件写入信息
     * 使用 PrintWrite 来写文件
     * OutputStream 打开文件，默认情况下，会把原文件直接清空，此时相当于新的数据覆盖了旧的数据
     */
    private void writeStat(String queueName,Stat stat){
        try (FileOutputStream fileOutputStream = new FileOutputStream(getQueueStatPath(queueName))){
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.write(stat.totalCount+"\t"+stat.validCount);
            printWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 创建对应队列对目录和文件
     */

    /**
     * 删除队列的目录和文件
     */

    /**
     * 检查对应的队列目录和文件是否存在
     */

    /**
     * 用来把一个新的消息放到队列的对应文件中
     */

    /**
     * 删除消息
     */



}
