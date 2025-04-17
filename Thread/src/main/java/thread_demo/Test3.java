package thread_demo;

public class Test3 {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
           Thread currentThread=Thread.currentThread();
           while(!currentThread.isInterrupted()){
               System.out.println("hello thread");
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
//                   throw new RuntimeException(e);
                   System.out.println("执行到catch");
               }
           }
        });
        t.start();
        Thread.sleep(1000);
        t.interrupt();
    }
}
