package test;

public class test1 {
    public static void main(String[] args) {
        for(Thread.State state:Thread.State.values()){
            System.out.println(state);
        }
    }
}
