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
package com.redhat.ceylon.compiler.java.util;

import java.util.HashMap;
import java.util.Map;

import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Options;

public class Timer {
    private static long programStart;
    private static String currentTask;
    private static long currentTaskStart;
    private static boolean verbose;
    private static final Map<String,IgnoredCategory> ignoredCategories = new HashMap<String,IgnoredCategory>();

    public static void setup(Options options) {
        verbose = options.get(OptionName.VERBOSE) != null 
                || options.get(OptionName.VERBOSE + ":benchmark" ) != null;
        // we delay printing the program start because we don't know if the verbose option is set yet at
        // that time, so we fake it later on with the correct time
        if(verbose)
            log("Program start", programStart);
    }

    public static void init(){
        programStart = System.currentTimeMillis();
    }

    public static void end() {
        if(!verbose)
            return;
        log("Program end");
    }

    public static void startTask(String name){
        if(!verbose)
            return;
        if(currentTask != null)
            endTask();
        currentTask = name;
        currentTaskStart = System.currentTimeMillis();
        log("Task "+currentTask+" start");
    }

    public static void log(String string) {
        if(!verbose)
            return;
        log(string, System.currentTimeMillis());
    }

    private static void log(String string, long now) {
        long delta = now - programStart;
        System.err.println("["+delta+"ms] "+string);
    }

    public static void endTask() {
        if(!verbose)
            return;
        long time = System.currentTimeMillis();
        long delta = time - currentTaskStart;
        log("Task "+currentTask+" end: "+delta+"ms");
        printIgnoredCategories();
        currentTask = null;
    }

    public static void startIgnore(String category) {
        if(!verbose)
            return;
        IgnoredCategory ignoredCategory = ignoredCategories.get(category);
        if(ignoredCategory == null){
            ignoredCategory = new IgnoredCategory(category);
            ignoredCategories.put(category, ignoredCategory);
        }
        ignoredCategory.start();
    }

    public static void stopIgnore(String category) {
        if(!verbose)
            return;
        IgnoredCategory ignoredCategory = ignoredCategories.get(category);
        ignoredCategory.stop();
    }
    
    private static void printIgnoredCategories(){
        for(IgnoredCategory category : ignoredCategories.values()){
            if(category.total != 0){
                System.err.println(" Including "+category.name+" for "+category.total+"ms");
            }
            category.reset();
        }
    }

    private final static class IgnoredCategory {
        String name;
        long start;
        long total;
        int count;
        public IgnoredCategory(String category) {
            this.name = category;
        }
        public void start() {
            if(count++ == 0){
                start = System.currentTimeMillis();
            }
        }
        public void stop() {
            if(--count == 0){
                long end = System.currentTimeMillis();
                long delta = end - start;
                total += delta;
            }
        }
        public void reset() {
            if(count != 0)
                System.err.println("Ignored category "+name+" count is "+count+" during reset: timings will be wrong");
            // try to fix it for next time?
            count = 0;
            total = 0;
        }
    }
}
