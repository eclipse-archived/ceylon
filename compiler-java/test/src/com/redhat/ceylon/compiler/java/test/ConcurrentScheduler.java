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

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.runners.model.RunnerScheduler;

public class ConcurrentScheduler implements RunnerScheduler {
    ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
            new ThreadFactory() {
                int ii = 0;
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "testrunner-thread-"+(ii++));
                }
            });
    
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
