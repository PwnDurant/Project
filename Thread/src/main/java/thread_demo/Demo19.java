package thread_demo;

public class Demo19 {
//    volatile只是解决了变量的可见性，一个线程修改了变量的值，另一个线程能立刻看到。
    private static volatile int count=0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                count++;
            }
        });
        Thread t2=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                count++;
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("count= "+count);
    }
}
