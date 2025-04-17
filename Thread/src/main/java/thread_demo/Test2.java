package thread_demo;

public class Test2 {

    private static boolean isQuit=false;

    public static void main(String[] args) throws InterruptedException {
//        这里如果把isQuit改为main方法的局部变量就不可以正常运行了，涉及到了变量捕获
//        它是lambda表达式/内部类的一个语法规则,isQuit和lambda定义在一个作用域中，此时lambda内部，是可以访问到
//        lambda外部（和lambda同一个作用域）中的变量，但是java中变量捕获有特殊要求，要求捕获的变量是final/事实final
//        要么用final修饰，要么就不能在代码中对这个变量进行修改，否则编译不通过
//        写成成员变量就可以了，是因为此时走的是“内部类访问外部类的成员，本身是ok”和变量捕获无关！！！
//        boolean isQuit=false;
        Thread t=new Thread(()->{
           while(!isQuit){
               System.out.println("hello thread");
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        t.start();

        Thread.sleep(2000);
        System.out.println("main终止t线程");

//        isQuit=true;
    }
}
