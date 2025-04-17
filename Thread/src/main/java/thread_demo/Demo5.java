package thread_demo;

public class Demo5 {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(()->{
            while(true){
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
        while(true){
            System.out.println("hello main");
            Thread.sleep(1000);
        }
    }
}
