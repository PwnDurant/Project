package org.mon.library_management_system.adapter;

public class Client {
    public static void main(String[] args) {
        Slf4jApi api=new Slf4jLog4jAdapter(new Log4jApi());
        api.log("打印日志");
    }
}
