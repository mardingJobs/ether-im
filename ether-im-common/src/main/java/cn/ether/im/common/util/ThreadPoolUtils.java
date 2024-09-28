package cn.ether.im.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtils {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16,
            16,
            30,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4096),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * execute task in thread pool
     */
    public static void execute(Runnable command) {
        executor.execute(command);
    }

    public static <T> Future<T> shumit(Callable<T> task) {
        return executor.submit(task);
    }

    public static void shutdown() {
        log.info("正在关闭线程池...");
        if (executor != null) {
            executor.shutdown();
        }
        log.info("已成功关闭线程池");
    }
}