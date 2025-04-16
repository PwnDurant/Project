package thread_demo;

class MyThread extends Thread{
    @Override
    public void run() {
//        这是即将要创建的线程所要执行的逻辑
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


public class Demo1 {

    public static void main(String[] args) throws InterruptedException {
        MyThread t=new MyThread();
        t.start();
//        run不会创建线程，只是在主线程里面执行了t线程的逻辑
//        t.run();
        while(true){
            System.out.println("hello main");
            Thread.sleep(1000);
        }


    }

}
