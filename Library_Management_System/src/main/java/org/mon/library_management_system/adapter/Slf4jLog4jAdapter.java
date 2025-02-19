package org.mon.library_management_system.adapter;

public class Slf4jLog4jAdapter implements Slf4jApi{
    private Log4jApi log4jApi;

    public Slf4jLog4jAdapter(Log4jApi log4jApi){
        this.log4jApi=log4jApi;
    }

    @Override
    public void log(String log) {
        log4jApi.print(log);
    }
}
