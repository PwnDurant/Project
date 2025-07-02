package com.wzc.mq;

import com.wzc.mq.mqserver.core.Binding;
import com.wzc.mq.mqserver.core.Exchange;
import com.wzc.mq.mqserver.core.ExchangeType;
import com.wzc.mq.mqserver.core.MSGQueue;
import com.wzc.mq.mqserver.datacenter.DataBaseManager;
import org.junit.jupiter.api.*;
import org.mockito.internal.stubbing.BaseStubbing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DataBaseManagerTest {

    private final DataBaseManager dataBaseManager = new DataBaseManager();

    /**
     * 在执行每个测试方法前的初始化操作
     */
    @BeforeEach
    public void setUp(){
        MqApplication.context = SpringApplication.run(MqApplication.class);
        dataBaseManager.init();
    }

    /**
     * 在执行每个测试方法前的收尾工作
     * 注意：这个需要的操作就是把数据库文件删除就行了
     * 但是这里不能直接删除，而是需要先关闭 context 对象
     * 此处的 context 对象，持有了 MetaMapper 实例又打开了 meta.db 数据库文件
     * 如果 meta.db 被别人打开了，此时的删除文件操作是不会成功的（Windows 系统限制，Linux 就没有问题）
     * 另一方面，获取 context 操作，会占用 8080 端口，此处的 close 也是释放 8080
     */
    @AfterEach
    public void tearDown(){
        MqApplication.context.close();
        dataBaseManager.deleteDB();
    }

    /**
     * setUp 函数中以及创建过数据库了，这里只需要检查数据库状态就行了
     * 查交换机表，里面应该有一个数据（匿名 exchange）；查队列列表，没数据；查绑定列表，没数据
     */
    @Test
    public void testInitTable(){
        List<Exchange> exchanges = dataBaseManager.selectAllExchanges();
        List<MSGQueue> msgQueues = dataBaseManager.selectAllQueue();
        List<Binding> bindings = dataBaseManager.selectAllBindings();

//        这里使用断言来判断结果是否相等
//        assertEquals 的形参，第一个形参数叫做 expected（预期的），第二个形式参数叫做 actual（实际的）
        Assertions.assertEquals(1,exchanges.size());
        Assertions.assertEquals("",exchanges.get(0).getName());
        Assertions.assertEquals(ExchangeType.DIRECT,exchanges.get(0).getType());
        Assertions.assertEquals(0,msgQueues.size());
        Assertions.assertEquals(0,bindings.size());
    }

//    用于创建一个交换机
    private Exchange createTestExchange(String exchangeName){
        Exchange exchange = new Exchange();
        exchange.setName(exchangeName);
        exchange.setType(ExchangeType.FANOUT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        exchange.setArguments("aaa",1);
        exchange.setArguments("bbb",2);
        return exchange;
    }

    /**
     * 测试插入交换机的功能是否成功
     */
    @Test
    public void testInsertExchange(){
//        构造一个数据库对象并插入到数据库中看看结果是否符合预期
        Exchange testExchange = createTestExchange("testExchange");
        dataBaseManager.insertExchange(testExchange);
//        插入完毕之后，查询结果
        List<Exchange> exchanges = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(2,exchanges.size());
        Exchange exchange = exchanges.get(1);
        Assertions.assertEquals("testExchange",exchange.getName());
        Assertions.assertEquals(ExchangeType.FANOUT,exchange.getType());
        Assertions.assertFalse(exchange.isAutoDelete());
        Assertions.assertTrue(exchange.isDurable());
        Assertions.assertEquals(1,exchange.getArguments("aaa"));
        Assertions.assertEquals(2,exchange.getArguments("bbb"));
    }

    /**
     * 测试删除交换机的功能是否成功
     */
    @Test
    public void testDeleteExchange(){
//        先构造一个交换机插入数据库，然后按照名称删除
        Exchange testExchange = createTestExchange("testExchange");
        dataBaseManager.insertExchange(testExchange);
        List<Exchange> exchanges = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(2,exchanges.size());
        Assertions.assertEquals("testExchange",exchanges.get(1).getName());
//        进行删除操作
        dataBaseManager.deleteExchange("testExchange");
//        再次查询
        List<Exchange> exchanges1 = dataBaseManager.selectAllExchanges();
        Assertions.assertEquals(1,exchanges.size());
        Assertions.assertEquals("",exchanges.get(0).getName());
    }

//    创建一个消息队列
    private MSGQueue createTestQueue(String queueName){
        MSGQueue queue = new MSGQueue();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        queue.setArguments("aaa",1);
        queue.setArguments("bbb",2);
        return queue;
    }

    @Test
    public void testInsertQueue(){
        MSGQueue testQueue = createTestQueue("testQueue");
        dataBaseManager.insertQueue(testQueue);
        List<MSGQueue> queueList = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(1,queueList.size());
        MSGQueue newQueue = queueList.get(0);
        Assertions.assertEquals("testQueue", newQueue.getName());
        Assertions.assertTrue(newQueue.isDurable());
        Assertions.assertFalse(newQueue.isAutoDelete());
        Assertions.assertFalse(newQueue.isExclusive());
        Assertions.assertEquals(1, newQueue.getArguments("aaa"));
        Assertions.assertEquals(2, newQueue.getArguments("bbb"));
    }

    /**
     * 测试删除队列
     */
    @Test
    public void testDeleteQueue(){
        MSGQueue queue = createTestQueue("testQueue");
        dataBaseManager.insertQueue(queue);
        List<MSGQueue> queueList = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(1,queueList.size());
//        进行删除
        dataBaseManager.deleteQueue("testQueue");
        List<MSGQueue> msgQueues = dataBaseManager.selectAllQueue();
        Assertions.assertEquals(0,msgQueues.size());
    }

//    创建绑定关系
    private Binding createTestBinding(String exchangeName,String queueName){
        return new Binding(exchangeName, queueName,"testBindingKey");
    }

    /**
     * 测试新建绑定关系
     */
    @Test
    public void testInsertBinding(){
        Binding testBinding = createTestBinding("testExchange", "testQueue");
        dataBaseManager.insertBinding(testBinding);
        List<Binding> bindings = dataBaseManager.selectAllBindings();
        Assertions.assertEquals(1,bindings.size());
        Assertions.assertEquals("testExchange",bindings.get(0).getExchangeName());
        Assertions.assertEquals("testQueue",bindings.get(0).getQueueName());
        Assertions.assertEquals("testBindingKey",bindings.get(0).getBindingKey());
    }

    /**
     * 测试删除绑定关系
     */
    @Test
    public void testDeleteBinding(){
        Binding testBinding = createTestBinding("testExchange", "testQueue");
        dataBaseManager.insertBinding(testBinding);
        List<Binding> bindings = dataBaseManager.selectAllBindings();
        Assertions.assertEquals(1,bindings.size());
//        删除绑定关系
        dataBaseManager.deleteBinding(testBinding);
        List<Binding> bindings1 = dataBaseManager.selectAllBindings();
        Assertions.assertEquals(0,bindings1.size());
    }

}
