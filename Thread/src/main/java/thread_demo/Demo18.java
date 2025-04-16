package thread_demo;

import java.util.Scanner;

public class Demo18 {
    private static volatile int n=0;

    public static void main(String[] args) {
        Thread t1=new Thread(()->{
           while(n==0){
               try {
                   Thread.sleep(10);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
            System.out.println("t1线程结束循环");
        });

        Thread t2=new Thread(()->{
            Scanner sc=new Scanner(System.in);
            System.out.println("请输入一个整数：");
            n=sc.nextInt();
        });

        t1.start();
        t2.start();
    }
}
