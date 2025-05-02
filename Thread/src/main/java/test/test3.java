package test;

public class test3 {

    private static int count=0;
    private static Object locker=new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (locker){
                    count++;
                }
            }
        });
        Thread t2=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (locker){
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
