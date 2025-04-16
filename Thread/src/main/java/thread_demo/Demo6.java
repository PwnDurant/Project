package thread_demo;

public class Demo6 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
           while(true){
               System.out.println("hello t1");
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });
        t1.start();
        Thread t2=new Thread(()->{
            while(true){
                System.out.println("hello t2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t2.start();
        Thread t3=new Thread(()->{
            while(true){
                System.out.println("hello t3");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t3.start();
        for(int i=0;i<3;i++){
            System.out.println("hello main");
            Thread.sleep(1000);
        }
    }
}
