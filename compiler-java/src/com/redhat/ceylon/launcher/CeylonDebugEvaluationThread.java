package com.redhat.ceylon.launcher;

import java.lang.ref.WeakReference;

public class CeylonDebugEvaluationThread extends Thread {
    public final static String name = CeylonDebugEvaluationThread.class.getName();
    public final static String methodForBreakpoint = "noop";
    public final static String mainThreadRefFieldName = "mainThreadRef";
    public final static String START_EVALUATION_THREAD_PROPERTY = "ceylon.debug.startEvaluationThread";
    private static boolean running = false;
    public static WeakReference<Thread> mainThreadRef = new WeakReference<Thread>(null);
    
    synchronized static void startDebugEvaluationThread() {
        if (! running) {
            mainThreadRef = new WeakReference<Thread>(currentThread());
            new CeylonDebugEvaluationThread().start();
            running = true;
        }
    }
    
    private CeylonDebugEvaluationThread() {
        super(new ThreadGroup(name), name);
        setDaemon(true);
        setPriority(Thread.MAX_PRIORITY);
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println("Uncaught exception in CeylonDebugEvaluationThread :");
                e.printStackTrace();
            }
        });
    }
    
    /*
     *  Method that can host a method entry breakpoint 
     *  so that the thread is always suspended on it
     *  while waiting for evaluation requests.
     */
    private void noop() {
        try {
            Thread.sleep(1000);
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (true) { noop(); }
    }
}