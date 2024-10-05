package cn.ether.im.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolUtils {

    private static final List<ThreadPoolExecutor> executors = new LinkedList();

    private static ThreadPoolExecutor DEFAULT_EXECUTOR = createExecutor(32, 1024);


    static {
        executors.add(DEFAULT_EXECUTOR);
    }

    public static ThreadPoolExecutor createExecutor(int poolSize, int queueSize) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize,
                poolSize,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executors.add(executor);
        return executor;
    }


    /**
     * execute task in thread pool
     */
    public static void execute(Runnable command) {
        DEFAULT_EXECUTOR.execute(command);
    }

    public static <T> Future<T> shumit(Callable<T> task) {
        return DEFAULT_EXECUTOR.submit(task);
    }

    public static void shutdown() {
        log.info("正在关闭线程池...");
        for (ThreadPoolExecutor executor : executors) {
            executor.shutdown();
        }
        log.info("已成功关闭线程池");
    }
}