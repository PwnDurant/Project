package thread_demo;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
           while(true){
               if(Thread.currentThread().isInterrupted()){
                   System.out.println("线程被中断，退出");
                    break;
               }
               System.out.println("运行中...");
               try {
                   Thread.sleep(1000);
                   System.out.println(Thread.interrupted());
               } catch (InterruptedException e) {
                   System.out.println("发生中断，跳出sleep");
                   break;
               }
           }
        });
        t.start();
        Thread.sleep(3000);
        t.interrupt(); //通知线程可以停下来了
    }

}
