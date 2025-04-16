package thread_demo;


class MyRunnable implements Runnable{
    @Override
    public void run() {
//        描述线程要执行的逻辑
        while(true){
            System.out.println("hello thread");
//            休息1秒
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


public class Demo2 {

    public static void main(String[] args) throws InterruptedException {
        MyRunnable myRunnable=new MyRunnable();
        Thread t=new Thread(myRunnable);
        t.start();
        while(true){
            System.out.println("hello main");
            Thread.sleep(1000);
        }
    }

}
