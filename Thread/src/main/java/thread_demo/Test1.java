package thread_demo;

public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                // 任务执行
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 如果是被中断唤醒的，要再次设置中断状态
                    Thread.currentThread().interrupt();
                    System.out.println("被中断了，退出");
//                    break;
                }
            }
        });
        worker.start();
        Thread.sleep(3000);
        worker.interrupt(); // 发送中断请求
    }

}
