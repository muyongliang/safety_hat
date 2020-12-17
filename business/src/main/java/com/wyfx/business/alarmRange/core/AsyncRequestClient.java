package com.wyfx.business.alarmRange.core;

import com.google.common.cache.LoadingCache;
import com.wyfx.business.alarmRange.util.RequestUtils;
import com.wyfx.business.alarmRange.util.TimeUtils;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author johnson liu
 * @date 2019/11/29
 * @description 异步请求客户端
 * 1、将待执行的任务存储在阻塞队列中，等到调度；
 * 2、获取任务线程不断循环阻塞队列获取任务；
 */
public class AsyncRequestClient {
    /**
     * 等待队列：所有待处理的任务均存入该队列中，等到线程池调度
     */
    private static final BlockingQueue<TaskCallable<String>> waitingQueue = new LinkedBlockingQueue<TaskCallable<String>>();
    /**
     * 每分钟并发数
     */
    private static final long minutesConcurrency = 5000;
    /**
     * 每分钟并发控制器
     */
    private static final LoadingCache<Long, AtomicInteger> minutesCounter = null;
    /**
     * 每秒并发控制器
     */
    private static final LoadingCache<Long, AtomicInteger> secondsCounter = null;
    private static AsyncRequestClient instance = null;
    /**
     * 线程池service
     */
    private static ExecutorService executorService = null;

    /**
     * 任务调度线程
     */
    private static TaskScheduleThread taskScheduleThread = null;

    /**
     * 是否执行任务
     */
    private static boolean isRunning = false;

    public static AsyncRequestClient getInstance() {
        if (null == instance) {
            synchronized (AsyncRequestClient.class) {
                if (null == instance) {
                    instance = new AsyncRequestClient();
                    instance.init();
                }
            }
        }
        return instance;
    }

    /**
     * 执行任务
     */
    private static void doTask() {
        TaskCallable<String> taskCallable = null;
        while (isRunning) {
            try {
                taskCallable = waitingQueue.take();
            } catch (InterruptedException e) {
                // 该异常无需处理，继续轮询即可
                continue;
            }
            // 并发控制
            executorService.submit(taskCallable);
            /*if (concurrencyControl()) {
                // 若未超并发，执行任务
                executorService.submit(taskCallable);
            } else {
                // 若超并发，休眠1s再执行
                System.out.println("超并发，休眠1s, waitingQueue size : " + waitingQueue.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 该异常无需处理
                }
                executorService.submit(taskCallable);
            }*/

        }
    }

    /**
     * 并发控制
     *
     * @return
     */
    private static boolean concurrencyControl() {
        long currentMinutes = TimeUtils.getCurrentTimeOfMinutes();
        long currentSeconds = System.currentTimeMillis() / 1000;
        int currentMinutesCounts = 0;
        int currentSecondsCounts = 0;
        try {
            currentMinutesCounts = minutesCounter.get(currentMinutes).incrementAndGet();
            currentSecondsCounts = secondsCounter.get(currentSeconds).incrementAndGet();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
            return true;
        }

        return currentMinutesCounts * 200 <= minutesConcurrency
                && currentSecondsCounts * 200 <= minutesConcurrency / 60;
    }

    /**
     * 初始化
     */
    public void init() {
        executorService = new ThreadPoolExecutor(NetConstants.CORE_POOL_SIZE, NetConstants.MAX_POOL_SIZE,
                NetConstants.KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        /*minutesCounter = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(Long arg0) throws Exception {
                        return new AtomicInteger(0);
                    }
                });

        secondsCounter = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(Long arg0) throws Exception {
                        return new AtomicInteger(0);
                    }
                });*/
    }

    /**
     * 开启任务调度
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            taskScheduleThread = new TaskScheduleThread();
            taskScheduleThread.start();
        }
    }

    /**
     * 停止任务调度
     */
    public void stop() {
        isRunning = false;
        if (null != taskScheduleThread) {
            taskScheduleThread.interrupt();
        }
    }

    /**
     * 销毁client
     */
    public void destroy() {
        isRunning = false;
        if (null != taskScheduleThread) {
            taskScheduleThread.interrupt();
        }
    }

    /**
     * 提交任务
     *
     * @param requestId
     * @param action
     * @param parameters
     * @param method
     */
    public void submitTask(long requestId, String action, Map<String, String> parameters, String method) {
        // 构造待执行任务
        TaskRunable taskRunnable = new TaskRunable(requestId, action, parameters, method);
        TaskCallable<String> taskCallable = new TaskCallable<String>(taskRunnable, "SUCCESS");
        // 任务存放到待执行队列
        try {
            waitingQueue.put(taskCallable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务封装类，作为参数传给TaskCallable
     *
     * @author baidu
     */
    public static final class TaskRunable implements Runnable {

        private final long requestId;

        private final String action;

        private final Map<String, String> parameters;

        private final String method;

        public TaskRunable(long requestId, String action, Map<String, String> parameters, String method) {
            super();
            this.requestId = requestId;
            this.action = action;
            this.parameters = parameters;
            this.method = method;
        }

        @Override
        public void run() {
            // 发送请求

            String result = RequestUtils.sendRequest(action, parameters, method);
            // 解析响应
            /*TrackHandler.parseResponse(requestId, action, result);*/
        }
    }

    /**
     * 任务封装类，作为参数传给线程池
     *
     * @param <T>
     * @author baidu
     */
    public static final class TaskCallable<T> implements Callable<T> {

        private final Runnable task;

        private final T result;

        public TaskCallable(Runnable task, T result) {
            super();
            this.task = task;
            this.result = result;
        }

        @Override
        public T call() throws Exception {
            task.run();
            return result;
        }

    }

    /**
     * 任务调度线程，不断循环阻塞队列获取任务
     *
     * @author baidu
     */
    public static final class TaskScheduleThread extends Thread {
        @Override
        public void run() {
            doTask();
        }
    }
}
