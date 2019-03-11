package com.sljl.core.utils;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 对一些并行操作进行封装的工具类
 *
 * @author L.Y.F
 */
public class ConcurrentUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrentUtil.class);

    /**
     * 与 {@link #mergeFutureResult(Collection, long, TimeUnit)} 唯一的区别是，本方法会使用默认的 3s 作为超时时间，
     * 当需要自定义超时时间时，请调用另一个方法
     *
     * @param futures 原始的futures，不能为null，此方法不会对原始list进行修改
     *
     * @return
     */
    public static MergedResult mergeFutureResult(Collection<? extends Future<?>> futures) {
        return mergeFutureResult(futures, 3L, TimeUnit.SECONDS);
    }


    /**
     * 合并futures的结果，如果时间超过指定的timeout，则get多少，返回多少，所以MergedResult的结果集可能会小于futures.
     * size()， 对于一定需要全部数据获取到的情况，可以先使用 {@link MergedResult#isAllFinished()} 来确定是否全部结果在指定时间内获取到
     *
     * @param futures 原始的futures，不能为null，此方法不会对原始list进行修改
     * @param timeout 超时时间
     * @param unit 超时时间单位
     *
     * @return MergedResult，合并后的结果对象
     */
    public static MergedResult mergeFutureResult(Collection<? extends Future<?>> futures, long timeout, TimeUnit unit) {
        // 复制一个list出来用于循环使用，该list会被修改，原始futures不被修改，建议传入futures为不可变list
        List<Future<?>> futuresForLoop = Lists.newArrayList(futures.iterator());
        MergedResult mergedResult = new MergedResult(futures.size());
        LinkedHashMap<Future<?>, Object> resMap = new LinkedHashMap<Future<?>, Object>(futuresForLoop.size() + 4);
        mergedResult.map = resMap;
        if (futuresForLoop.isEmpty()) {
            return mergedResult;
        }
        long timeoutMillis = System.currentTimeMillis() + unit.toMillis(timeout);
        boolean noError = true;
        while (true && noError) {
            // 超过预期的总等待时间，则超时不在使用
            if (timeoutMillis < System.currentTimeMillis() || futuresForLoop.isEmpty()) {
                break;
            }
            Future<?> futureForTryGet = futuresForLoop.iterator().next();
            try {
                futureForTryGet.get(10, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                // 等待超时抛弃
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                // TODO 当一个任务失败就直接返回，看这个是否使用参数控制
                logger.error("任务执行失败", e);
                noError = false;
                break;
            }
            for (Iterator<Future<?>> iterator = futuresForLoop.iterator(); iterator.hasNext(); ) {
                Future<?> future = iterator.next();
                if (future.isDone()) {
                    // 已经done的，直接可以获取，不会阻塞
                    try {
                        resMap.put(future, future.get());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException e) {
                        // TODO 当一个任务失败就直接返回，看这个是否使用参数控制
                        logger.error("任务执行失败", e);
                        noError = false;
                        break;
                    }
                    iterator.remove();
                }
            }
        }
        // 对于尚未完成的任务，直接全部取消掉（试图通过interrupt的方式）
        for (Future<?> future : futuresForLoop) {
            future.cancel(true);
        }
        return mergedResult;
    }

    /**
     * 经过合并后的结果类，方便获取结果并且不会报unchecked警告，其行为特征类似Map<?, ?>
     *
     * @author L.Y.F
     */
    public static class MergedResult {

        LinkedHashMap<Future<?>, ?> map;
        final int taskCount;

        // 外部不可以实例化
        private MergedResult(int taskCount) {
            this.taskCount = taskCount;
        }

        /**
         * 根据对应future返回相应的结果对象，如果 {@link #isAllFinished()} 为false，则返回结果可能为null，
         * 否则立即返回同future.get()一样的结果，不会阻塞
         *
         * @param future 提交合并任务时，使用的future
         *
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T> T getResult(Future<T> future) {
            return (T) map.get(future);
        }

        /**
         * 结果集中结果的个数
         *
         * @return
         */
        public int size() {
            return map.size();
        }

        /**
         * 是否所有任务都已经完成，判断依据是结果的size是否等于任务size
         *
         * @return
         */
        public boolean isAllFinished() {
            return taskCount == map.size();
        }

        @Override
        public String toString() {
            return this.map.toString();
        }

    }

}
