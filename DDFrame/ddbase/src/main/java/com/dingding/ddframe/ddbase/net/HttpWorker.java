package com.dingding.ddframe.ddbase.net;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by zzz on 15/8/5.
 */
public class HttpWorker {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private ExecutorService executor;

    private void HttpWorker() {

        executor = Executors.newFixedThreadPool(MAXIMUM_POOL_SIZE);
    }

    Future<?> doA(Callable callable) throws ExecutionException, InterruptedException {
        Future<?> submit = executor.submit(callable);
        Object o = submit.get();
        return null;
    }

    Callable call = new Callable() {
        @Override
        public Object call() throws Exception {

            return null;
        }
    };

    public class OneTask implements Callable{

        @Override
        public Object call() throws Exception {

            return null;
        }
    }
}
