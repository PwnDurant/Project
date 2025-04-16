package thread_demo;

public class Demo13 {
    public static void main(String[] args) throws InterruptedException {
        Thread mainThread=Thread.currentThread();
        Thread t=new Thread(()->{
           while(true){
               System.out.println("main state="+mainThread.getState());
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        System.out.println(t.getState());

        t.start();

//        由于t线程是持续while循环，因此join不会返回
//        观察主线程的状态，就能看到waiting
        t.join();

        System.out.println(t.getState());
    }
}
