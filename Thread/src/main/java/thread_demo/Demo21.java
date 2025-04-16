package thread_demo;

import java.util.Scanner;

public class Demo21 {
    private static Object locker=new Object();

    public static void main(String[] args) {


        Thread t1=new Thread(()->{
            synchronized (locker){
                System.out.println("t1 wait之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t1 wait 之后");
            }
        });

        Thread t2=new Thread(()->{
            System.out.println("t2 notify 之前");
            System.out.println("请输入：");
            Scanner sc=new Scanner(System.in);
            sc.nextInt();  //此处主要是通过next，构造阻塞

            synchronized (locker){
                locker.notify();
            }

            System.out.println("t2 notify之后");
        });

        t1.start();
        t2.start();
    }
}
