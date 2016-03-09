/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.RunnerScheduler;

public class ConcurrentScheduler implements RunnerScheduler {
    private final ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
            new ThreadFactory() {
                int ii = 0;
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "testrunner-thread-"+(ii++));
                }
            });
    private final ArrayList<Runnable> sequential = new ArrayList<Runnable>();
    
    @Override
    public void schedule(Runnable r) {
        /* This is disgusting, but JUnit doesn't expose a way to know how
         * the Runnable is related to the thing it's running, and some of 
         * our tests should not run concurrently, so use reflection to 
         * make the association that JUnit doesn't provide and execute 
         * those tests after the executor has finished running the concurrent 
         * ones.
         */
        for (Field f : r.getClass().getDeclaredFields()) {
            if ("val$each".equals(f.getName())) {
                f.setAccessible(true);
                Object runner;
                try {
                    runner = f.get(r);
                } catch (IllegalArgumentException e) {
                    break;
                } catch (IllegalAccessException e) {
                    break;
                }
                if (runner instanceof BlockJUnit4ClassRunner) {
                    Class<?> testClass = ((BlockJUnit4ClassRunner)runner).getTestClass().getJavaClass();
                    if (testClass.isAnnotationPresent(RunSingleThreaded.class)) {
                        System.out.printf("Submitting task into to run single threaded: %s%n", r);
                        sequential.add(r);
                        return;
                    }
                }
            }
        }
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
        if (!sequential.isEmpty()) {
            System.out.printf("Now running %d test cases single threaded", sequential.size());
            for (Runnable r : sequential) {
                r.run();
            }
        }
    }
}
