package cn.zhengjianglong.java.thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhengjianglong
 * @create: 2018-05-13 11:02
 */
public class ReOrderExample {
    public int a = 0;
    public boolean flag = false;

    public void witer() {
        a = 1;
        flag = true;
        System.out.println("writer finished");
    }

    public void reader() {
        if (flag) {
            int i = a * a;
            System.out.println(i);
        }
        System.out.println("reader finished");
    }

    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final ReOrderExample example = new ReOrderExample();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier.await();
                    example.witer();
                } catch (Exception e) {

                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    barrier.await();
                    example.reader();
                } catch (Exception e) {

                }
            }
        });

        service.shutdown();

    }
}
