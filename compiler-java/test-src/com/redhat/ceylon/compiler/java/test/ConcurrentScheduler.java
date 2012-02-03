package com.redhat.ceylon.compiler.java.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.runners.model.RunnerScheduler;

public class ConcurrentScheduler implements RunnerScheduler {
    ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
    
    @Override
    public void schedule(Runnable r) {
        System.out.println("Aventando una tarea al pool");
        tpool.submit(r, null);
    }
    
    @Override
    public void finished() {
        System.out.println("esperando a que termine el pool con " + tpool.getActiveCount() + " hilos activos y " + tpool.getTaskCount() + " tareas");
        try {
            while (tpool.getActiveCount() > 0) {
                tpool.awaitTermination(10, TimeUnit.SECONDS);
                System.out.println("esperando a que termine el pool con " + tpool.getActiveCount() + " hilos activos y " + tpool.getTaskCount() + " tareas");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } finally {
            tpool.shutdownNow();
        }
    }
}
