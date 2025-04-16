package thread_demo;


//懒汉模式实现单例模式
class SingletonLazy{
//    加上关键字防止指令重排序：1->2->3   1->3->2
    private static volatile SingletonLazy instance=null;

    private static Object locker=new Object();

    public static SingletonLazy getInstance(){
//        在这个条件中判断当前是否要加锁
        if(instance==null){
            synchronized (locker){
                if(instance==null){
                    instance=new SingletonLazy();
                }
            }
        }
        return instance;
    }
    private SingletonLazy(){}
}


public class Demo25 {

    public static void main(String[] args) {
        SingletonLazy t1=SingletonLazy.getInstance();
        SingletonLazy t2=SingletonLazy.getInstance();
        System.out.println(t1==t2);
    }

}
