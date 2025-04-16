package thread_demo;

import java.util.Scanner;

public class Demo22 {

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
               System.out.println("t1 wait之后");
           }
        });


        Thread t2=new Thread(()->{
            synchronized (locker){
                System.out.println("t2 wait之前");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("t2 wait之后");
            }
        });

        Thread t3=new Thread(()->{
           synchronized (locker){
               System.out.println("t3 wait之前");
               try {
                   locker.wait();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               System.out.println("t3 wait之后");
           }
        });

        Thread t4=new Thread(()->{
            Scanner sc=new Scanner(System.in);
            sc.next();
            System.out.println("t4 notify之前");
            synchronized (locker){
//                locker.notify();
//                locker.notify();
////                locker.notify();
////                locker.notify();
                locker.notifyAll();
            }
            System.out.println("t4 notify之后");
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
