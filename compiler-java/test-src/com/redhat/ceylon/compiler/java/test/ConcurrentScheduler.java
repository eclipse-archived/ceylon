package com.redhat.ceylon.compiler.java.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.runners.model.RunnerScheduler;

public class ConcurrentScheduler implements RunnerScheduler {
    ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
    
    @Override
    public void schedule(Runnable r) {
        System.out.printf("Submitting task into pool: %s%n", r);
        tpool.execute(r);
    }
    
    @Override
    public void finished() {
        System.out.printf("Waiting for pool to finish with %d active threads and %d tasks%n", tpool.getActiveCount(), tpool.getTaskCount());
        try {
            tpool.shutdown();
            while (tpool.getActiveCount() > 0) {
                tpool.awaitTermination(5, TimeUnit.SECONDS);
                System.out.printf("Waiting for pool to finish with %d active threads and %d tasks%n", tpool.getActiveCount(), tpool.getTaskCount());
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            tpool.shutdownNow();
        }
    }
}
