package thread_demo;

public class Demo11 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
            for(int i=0;i<4;i++){
                System.out.println("hello t1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("t1 结束");
        });

        Thread t2=new Thread(()->{
            for(int i=0;i<4;i++){
                System.out.println("hello t2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                t1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t2 结束");
        });

        t1.start();
        t2.start();
        System.out.println("主线程等待开始");
        t2.join();
        t1.join();
        System.out.println("主线程等待结束");


    }
}
