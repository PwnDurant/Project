package com.wzc.mq.mqserver.datacenter;


import com.wzc.mq.common.BinaryTool;
import com.wzc.mq.common.MQException;
import com.wzc.mq.mqserver.core.MSGQueue;
import com.wzc.mq.mqserver.core.Message;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 这个类实现对硬盘消息的管理
 */
public class MessageFileManager {

    @NoArgsConstructor
    @AllArgsConstructor
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
    public void createQueueFiles(String queueName) throws IOException {
//        先创建对应的消息目录
        File baseDir = new File(getQueueDir(queueName));
        if(!baseDir.exists()) {
            boolean ok = baseDir.mkdirs();
            if (!ok) throw new IOException("创建目录失败! baseDir="+baseDir.getAbsolutePath());
        }

//        创建队列数据文件
        File queueDataFile = new File(getQueueDataPath(queueName));
        if(!queueDataFile.exists()){
            boolean ok = queueDataFile.mkdirs();
            if (!ok) throw new IOException("创建队列数据文件失败! queueDataFile="+queueDataFile.getAbsolutePath());
        }

//        创建队列消息统计文件
        Stat stat = new Stat(0,0);
        writeStat(queueName,stat);
    }

    /**
     * 删除队列的目录和文件
     * 队列也是可以被删除的，当队列删除之后，对应的消息文件和统计文件也应该被删除
     */
    public void destroyQueueFiles(String queueName) throws IOException {
        File dataFile = new File(getQueueDataPath(queueName));
        boolean data = dataFile.delete();
        File statFile = new File(getQueueStatPath(queueName));
        boolean stat = statFile.delete();
        File dirFile = new File(getQueueDir(queueName));
        boolean dir = dirFile.delete();
        if(!data||!stat||!dir) throw new IOException("删除队列目录和文件失败！baseDir="+dirFile);
    }

    /**
     * 检查对应的队列目录和文件是否存在 -> 队列的数据文件和统计文件都需要检查！！！
     * 假设后续有生产者给 broker server 生产消息，这个消息就可能需要被记录到文件上（取决于消息是否要持久化）
     */
    public boolean checkFilesExits(String queueName){
        File dataFile = new File(getQueueDataPath(queueName));
        File statFile = new File(getQueueStatPath(queueName));
        return dataFile.exists() && statFile.exists();
    }


    /**
     * 用来把一个新的消息放到队列的对应文件中
     * DataOutputStream : 是 Java 标准库中用来写指定字节的 IO 类
     */
    public void sendMessage(MSGQueue queue, Message message) throws IOException {
//        先检查一下当前队列是否存在
        if(!checkFilesExits(queue.getName()))
            throw new MQException("【MessageFileManager】队列对应的文件不存在！queueName = "+queue.getName());
//        把 Message 对象序列化为 二进制数据
        byte[] messageBinary = BinaryTool.toBytes(message);
//        避免发生线程安全问题
        synchronized (queue){
//            先获取当前队列数据文件的长度，用来计算该 Message 对象的 offsetBeg 和 offsetEnd
//            把新的 Message 数据，写入到数据文件的末尾，此时 Message 对象的 offsetBeg，就是当前文件长度 +4
//            offsetEnd 长度就是当前文件长度 +4 + message 自身长度
            File queueDataFile = new File(getQueueDataNewPath(queue.getName()));
            message.setOffsetBeg(queueDataFile.length() + 4);
            message.setOffsetEnd(queueDataFile.length() + 4 + messageBinary.length);
//            追加写入消息到文件
            try(FileOutputStream fileOutputStream = new FileOutputStream(queueDataFile,true)){
                try(DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)){
//                    先保存消息长度（4个字节）然后再保存消息内容
                    dataOutputStream.writeInt(messageBinary.length);
                    dataOutputStream.write(messageBinary);
                }
            }
//            更新消息统计文件数据
            Stat stat = readStat(queue.getName());
            assert stat != null;
            stat.totalCount++;
            stat.validCount++;
            writeStat(queue.getName(),stat);
        }

    }

    /**
     * 删除消息
     * 逻辑删除：把消息体中 isValid 属性设置为 0x0 就行
     * 先将文件中对应数据反序列化为 Message 对象修改属性之后再将数据重新写回文件
     * RandomAccessFile : Java 标准库中支持对文件的光标移动读取类
     * @param queue 被删除消息所在的队列
     * @param message 此处被删除的消息必须包含有效的 offsetBeg 和 offsetEnd 数值
     */
    public void deleteMessage(MSGQueue queue,Message message) throws IOException, ClassNotFoundException {
        synchronized (queue){
            try(RandomAccessFile randomAccessFile = new RandomAccessFile(getQueueDataPath(queue.getName()),"rw")){
//            先从文件中读取对应的 Message 数据，并将其转换为 Message 对象
                byte[] bufMessage = new byte[(int) (message.getOffsetBeg() - message.getOffsetEnd())];
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.read(bufMessage);
                Message diskMessage = BinaryTool.fromBytes(bufMessage);
//            设置无效属性之后再重新写入文件,此处不需要再给参数中的 message 设置无效属性，这里的对象代表的是内存中的对象，之后会被销毁
                diskMessage.setIsValid((byte) 0x0);
                byte[] DestBuf = BinaryTool.toBytes(diskMessage);
//            由于上面的读取操作将光标向后移了，需要重新调整光标才能将刚刚文件重新正确写入
                randomAccessFile.seek(message.getOffsetBeg());
                randomAccessFile.write(DestBuf);
            }
//        更新统计文件
            Stat stat = readStat(queue.getName());
            assert stat != null;
            if(stat.validCount > 0) stat.validCount --;
            writeStat(queue.getName(),stat);
        }
    }

    /**
     * 使用这个方法从文件中读取中所有的消息内容，加载到内存中（具体是一个链表中）
     * 在启动的过程中进行调用
     * 使用 LinkedList，主要目的就是为了后续进行头删操作
     * @param queueName 需要加载的队列名,并且由于在程序启动时调用，此时服务器还不能处理请求，
     *                  不涉及多线程操作，也不需要加锁
     * @return 已加载到队列中的数据
     */
    public LinkedList<Message> loadAllMessageFromQueue(String queueName) throws IOException, ClassNotFoundException {
        LinkedList<Message> messages = new LinkedList<>();
        try(FileInputStream fileInputStream = new FileInputStream(getQueueDataPath(queueName))){
            try(DataInputStream dataInputStream = new DataInputStream(fileInputStream)){
//                这个变量记录当前文件光标，并循环读取一个队列中的消息
                long currentOffset = 0;
                while(true){
//                    readInt 方法在读取到文件末尾的时候会抛出 EOFException 异常，这一点和很多流对象不一样
                    int messageSize = dataInputStream.readInt();
//                    按照 messageSize 长度向后读取内容
                    byte[] buffer = new byte[messageSize];
                    int actualSize = dataInputStream.read(buffer);
//                    防御式编程
                    if(messageSize != actualSize)
                        throw new MQException("【MessageFileManager】文件格式错误！queueName = "+queueName+" 可能原因：文件不完整、网络中断、Socket 半关闭等等");
//                    反序列化并判断是否有效
                    Message diskMessage = BinaryTool.fromBytes(buffer);
                    if(diskMessage.getIsValid() != 0x1){
//                        如果是无效数据，直接跳过，并要更新 offset
                        currentOffset += (4 + messageSize);
                        continue;
                    }
//                    有效数据，直接把这个 Message 对象加入到链表中，加入之前还需要填写 offsetBeg 和 offsetEnd
//                    这里 OffsetBeg/OffsetEnd 的作用就体现出来了，虽然没有持久化保存到磁盘上，但是可以用来给内存使用，方便后续操作
                    diskMessage.setOffsetBeg(currentOffset + 4);
                    diskMessage.setOffsetEnd(currentOffset + 4 +messageSize);
//                    读取完一个消息之后就要手动的记录一下光标，方便对 Offset 计算
                    currentOffset += (4 + messageSize);
                    messages.add(diskMessage);
                }
            } catch (EOFException e){
                System.out.println("【MessageFileManager】恢复 Message 数据完成！");
            }
        }
        return messages;
    }

    /**
     * 检查当前是否要针对该队列的消息数据文件进行 GC
     */
    public boolean checkGC(String queueName){
        Stat stat = readStat(queueName);
        assert stat != null;
        return stat.totalCount > 2000 && (double) stat.validCount / (double) stat.totalCount < 0.5;
    }

    /**
     * 暂存的新文件名字
     */
    public String getQueueDataNewPath(String queueName){
        return getQueueDir(queueName) + "/queue_data_new.txt";
    }

    /**
     * 真正执行消息文件的垃圾回收操作 -> 使用复制算法来完成
     * 1，创建一个名为 queue_data_new.txt 的新文件
     * 2，把之前消息数据文件中的有效数据都读出来，写到新的文件中
     * 3，删除旧文件，再把新文件改为 queue_data.txt，并更新新消息统计文件
     * 注意：此操作是针对消息数据文件的大洗牌，在这个过程中其他线程不能针对该队列的消息文件做任何修改，所以必须要保证线程安全
     */
    public void gc(MSGQueue queue) throws IOException, ClassNotFoundException {
        synchronized (queue){
            long gcBeg = System.currentTimeMillis();
//            创建新文件
            File queueDataNewFile = new File(getQueueDataNewPath(queue.getName()));
            if(queueDataNewFile.exists())
                throw new MQException("【MessageFileManager】gc 时发现该队列的 queue_data_new 已经存在！queueName = "+queue.getName());
            boolean ok = queueDataNewFile.createNewFile();
            if(!ok) throw new MQException("【MessageFileManager】创建文件失败！queueDataNewFile = "+queueDataNewFile.getAbsolutePath());
//            从旧文件中读取所有消息对象
            LinkedList<Message> messages = loadAllMessageFromQueue(queue.getName());
//            把有效消息写入到新文件中
            try(FileOutputStream fileOutputStream = new FileOutputStream(queueDataNewFile)){
                try(DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)){
                    for (Message message : messages) {
                        byte[] validMessage = BinaryTool.toBytes(message);
                        dataOutputStream.writeInt(validMessage.length);
                        dataOutputStream.write(validMessage);
                    }
                }
            }
//            删除旧的数据文件，并把新数据文件重新命名
            File oldDataFile = new File(getQueueDataPath(queue.getName()));
            ok = oldDataFile.delete();
            if(!ok) throw new MQException("【MessageFileManager】删除旧数据文件失败！queueDataOldFile = "+oldDataFile.getAbsolutePath());
            ok = queueDataNewFile.renameTo(oldDataFile);
            if(!ok) throw new MQException("【MessageFileManager】文件重命名失败！queueDataNewFile = "+queueDataNewFile.getAbsolutePath() +
                    ",queueDataOldFile = " + oldDataFile.getAbsolutePath());
//            更新统计文件
            Stat stat = readStat(queue.getName());
            assert stat != null;
            stat.totalCount = messages.size();
            stat.validCount = messages.size();
            writeStat(queue.getName(),stat);
            long gcEnd = System.currentTimeMillis();
            System.out.println("【MessageFileManager】gc 执行完毕！queueName = " + queue.getName() + ",time = "
            + (gcEnd - gcBeg) + "ms");
        }
    }



}
