package thread_demo;


class Counter{
    public int count=0;

//    public void add(){
//        synchronized (this){
//            count++;
//        }
//    }

    synchronized public void add(){
        count++;
    }
}


public class Demo16 {
    public static void main(String[] args) throws InterruptedException {
        Counter counter=new Counter();

        Thread t1=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (counter){
                    counter.add();
                }
            }
        });


        Thread t2=new Thread(()->{
            for (int i = 0; i < 5000; i++) {
                synchronized (counter){
                    counter.add();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("counter="+counter.count);
    }
}
