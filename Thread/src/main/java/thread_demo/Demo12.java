package thread_demo;

public class Demo12 {
    public static void main(String[] args) throws InterruptedException {
        Thread mainThread=Thread.currentThread();
        Thread t=new Thread(()->{
//            需要在t中调用主线程join
            System.out.println("t 线程开始等待");
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("t 线程等待结束");
        });
        t.start();

        Thread.sleep(2000);
        System.out.println("main 线程结束");
    }
}
