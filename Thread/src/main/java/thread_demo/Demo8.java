package thread_demo;

public class Demo8 {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
           for (int i=0;i<3;i++){
               System.out.println("hello thread");
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

//        这个结果为false，此时还没有创建线程
        System.out.println(t.isAlive());
        t.start();
        while(true){
            System.out.println(t.isAlive());
            Thread.sleep(1000);
        }
    }
}
