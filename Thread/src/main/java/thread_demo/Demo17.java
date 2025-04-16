package thread_demo;

public class Demo17 {

    private static Object locker1=new Object();

    private static Object locker2=new Object();

    public static void main(String[] args) {
        Thread t1=new Thread(()->{
            synchronized (locker1){
                System.out.println("t1加锁locker1完成");
//                这里的sleep是确保，t1和t2都分别拿到locker1和locker2然后再分别再拿对方的锁
//                如果没有sleep执行顺序就不可控，可能出现某一个线程一口气拿到两把锁，另一个线程还没执行呢，无法构造出死锁
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (locker2){
                    System.out.println("t1 加锁 locker2 完成");
                }
            }
        });

        Thread t2=new Thread(()->{
            synchronized (locker2){
                System.out.println("t2加锁locker2完成");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (locker1){
                    System.out.println("t2 加锁 locker1完成");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
