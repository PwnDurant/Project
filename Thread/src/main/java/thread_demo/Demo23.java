package thread_demo;

public class Demo23 {

    private static Object locker1=new Object();
    private static Object locker2=new Object();
    private static Object locker3=new Object();

    public static void main(String[] args) throws InterruptedException {


        Thread t1=new Thread(()->{
            for (int i = 0; i < 10; i++) {
                System.out.print("A");
                synchronized (locker1){
                    locker1.notify();
                }

                synchronized (locker3){
                    try {
                        locker3.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Thread t2=new Thread(()->{
            for (int i = 0; i < 10; i++) {
                synchronized (locker1){
                    try {
                        locker1.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.print("B");
                synchronized (locker2){
                    locker2.notify();
                }
            }
        });

        Thread t3=new Thread(()->{
            for (int i = 0; i < 10; i++) {
                synchronized (locker2){
                    try {
                        locker2.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("C");
                synchronized (locker3){
                    locker3.notify();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();
//        主线程加入sleep确保所有线程都执行到wait
        Thread.sleep(100);
//        主线程去唤醒t1
        synchronized (locker3){
            locker3.notify();
        }

    }

}
