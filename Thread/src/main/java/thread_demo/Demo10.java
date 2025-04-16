package thread_demo;

public class Demo10 {

    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
            for(int i=0;i<3;i++){
                System.out.println("这是线程 t");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("线程t结束");
        });

        t.start();

//        Thread.sleep(4000);

//        让主线程等待t线程
        System.out.println("main线程开始等待");

        t.join();

        System.out.println("main线程结束等待");
    }



}
