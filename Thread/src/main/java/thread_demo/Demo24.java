package thread_demo;


//创建一个单例的类
//饿汉方式实现
//饿 的意思是 “迫切”
//在类被加载的时候，就会创建出这个类

class Singleton{
    private static Singleton instance=new Singleton();

    public static Singleton getInstance(){
        return instance;
    }

//    单例模式的最关健部分,对象不能被程序员new出来
    private Singleton(){}
}


public class Demo24 {

    public static void main(String[] args) {
        Singleton s1=Singleton.getInstance();
        Singleton s2=Singleton.getInstance();
        System.out.println(s1==s2);
    }

}
