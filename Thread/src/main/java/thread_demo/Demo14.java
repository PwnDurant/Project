package thread_demo;

import java.util.ArrayList;
import java.util.List;

public class Demo14 {
    private static int count=0;

    private static Object locker=new Object();
    private static Object locker1=new Object();

    private static String s="hello";
    private static List<String> list=new ArrayList<>();

    private static int a=10;

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (Demo14.class){
                    count++;
                }
            }
        });

        Thread t2=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (Demo14.class){
                    count++;
                }
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("count="+count);
    }
}
