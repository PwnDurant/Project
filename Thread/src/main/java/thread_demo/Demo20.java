package thread_demo;

public class Demo20 {
    public static void main(String[] args) throws InterruptedException {
        Object obj=new Object();
        System.out.println("wait 之前");
        synchronized (obj){
            obj.wait();
        }
        System.out.println("wait 之后");
    }
}
