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
package com.redhat.ceylon.model.loader;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Timer {
    private long programStart;
    private String currentTask;
    private long currentTaskStart;
    protected boolean verbose;
    private final Map<String,IgnoredCategory> ignoredCategories;
    protected PrintWriter out;

    protected Timer(){
        ignoredCategories = new HashMap<String,IgnoredCategory>();
    }
    
    private Timer(PrintWriter out, long programStart, boolean verbose, Map<String,IgnoredCategory> ignoredCategories) {
        this.programStart = programStart;
        this.verbose = verbose;
        this.ignoredCategories = ignoredCategories;
        this.out = out;
    }
    
    public Timer(boolean verbose) {
        ignoredCategories = new HashMap<String,IgnoredCategory>();
        setup(verbose);
    }
    
    private void setup(boolean verbose) {
        // we delay printing the program start because we don't know if the verbose option is set yet at
        // that time, so we fake it later on with the correct time
    }

    /**
     * Initializes this timer and {@linkplain #log(String) logs} a 
     * "program start" message.
     */
    public void init(){
        programStart = System.nanoTime();
        if(verbose)
            log("Program start", programStart);
    }

    public void end() {
        if(!verbose)
            return;
        log("Program end");
    }

    /**
     * {@linkplain #endTask() Ends} the current task (if any) and starts a 
     * timed task with the given name, 
     * {@linkplain #log(String) logging} the task name. 
     * 
     * @param name The name of the task to start.
     * 
     * @see #nestedTimer()
     */
    public void startTask(String name){
        if(!verbose)
            return;
        if(currentTask != null)
            endTask();
        currentTask = name;
        currentTaskStart = System.nanoTime();
        log("Task "+currentTask+" start");
    }
    
    /**
     * Logs a message, including the elapsed time since this Timer was 
     * {@linkplain #init() initialized}.
     * @param string The message
     */
    public void log(String string) {
        if(!verbose)
            return;
        log(string, System.nanoTime());
    }

    private void log(String string, long now) {
        long delta = (now - programStart)/1_000_000;
        String msg = "["+delta+"ms] "+string;
        if(out != null)
            out.println(msg);
        else
            System.err.println(msg);
    }

    /**
     * Prints a message
     * @param string The message
     */
    public void print(String string) {
        if(!verbose)
            return;
        if(out != null)
            out.println(string);
        else
            System.err.println(string);
    }

    /**
     * Ends the current task, {@linkplain #log(String) logging} the name of 
     * the task and its elapsed time
     * @see #startTask(String)
     */
    public void endTask() {
        if(!verbose)
            return;
        long time = System.nanoTime();
        long delta = (time - currentTaskStart)/1_000_000L;
        log("Task "+currentTask+" end: "+delta+"ms");
        printIgnoredCategories();
        currentTask = null;
    }

    public void startIgnore(String category) {
        if(!verbose)
            return;
        IgnoredCategory ignoredCategory = ignoredCategories.get(category);
        if(ignoredCategory == null){
            ignoredCategory = new IgnoredCategory(category);
            ignoredCategories.put(category, ignoredCategory);
        }
        ignoredCategory.start();
    }

    public void stopIgnore(String category) {
        if(!verbose)
            return;
        IgnoredCategory ignoredCategory = ignoredCategories.get(category);
        if (ignoredCategory != null) {
            ignoredCategory.stop();
        }
    }
    
    private void printIgnoredCategories(){
        for(IgnoredCategory category : ignoredCategories.values()){
            if(category.total != 0){
                print(" Including "+category.name+" for "+category.total+"ms");
            }
            category.reset();
        }
    }

    private final class IgnoredCategory {
        String name;
        long start;
        long total;
        int count;
        public IgnoredCategory(String category) {
            this.name = category;
        }
        public void start() {
            if(count++ == 0){
                start = System.nanoTime();
            }
        }
        public void stop() {
            if(--count == 0){
                long end = System.nanoTime();
                long delta = (end - start)/1_000_000;
                total += delta;
            }
        }
        public void reset() {
            if(count != 0)
                print("Ignored category "+name+" count is "+count+" during reset: timings will be wrong");
            // try to fix it for next time?
            count = 0;
            total = 0;
        }
    }
    
    /**
     * Creates and returns a new timer suitable for timing sub-tasks 
     * without stopping the 'outer' timer. Necessary because 
     * {@link #startTask(String)} stops the current timer. 
     * @return The new timer
     */
    public Timer nestedTimer() {
        return new Timer(out, programStart, verbose, ignoredCategories);
    }
}
