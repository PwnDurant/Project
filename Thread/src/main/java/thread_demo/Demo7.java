package thread_demo;

public class Demo7 {

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

//        这样的设置得在start之前进行
//        将t1设置为守护线程：守护线程是服务于用户线程（非守护线程）的后台线程，当所有用户线程结束后，守护线程也会自动退出，JVM 随之终止运行。
//        默认是前台线程（非守护线程）
        t1.setDaemon(true);

        t1.start();
        for(int i=0;i<3;i++){
            System.out.println("hello main");
            Thread.sleep(1000);
        }
        System.out.println("main 结束");
    }
}
