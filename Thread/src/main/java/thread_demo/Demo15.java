package thread_demo;

public class Demo15 {

    private static int sum1=0;
    private static int sum2=0;

    public static void main(String[] args) throws InterruptedException {
        long beg=System.currentTimeMillis();

        int[] arr=new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=i+1;
        }

        Thread t1=new Thread(()->{
            for (int i = 0; i < arr.length; i+=2) {
                sum1+=arr[i];
            }
        });

        Thread t2=new Thread(()->{
            for(int i=1;i<arr.length;i+=2){
                sum2+=arr[i];
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("sum1="+sum1);
        System.out.println("sum2="+sum2);
        System.out.println("sum="+(sum1+sum2));

        long end=System.currentTimeMillis();
        System.out.println("程序运行时间:"+(end-beg)+"ms");
    }
}
