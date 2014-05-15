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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

public class CeylonTestGroup extends ParentRunner<Runner> {

    private LinkedHashMap<Runner, Description> children = new LinkedHashMap<Runner, Description>();
    private String name;
    private Runnable moduleInitialiser;


    public CeylonTestGroup(List<Runner> list, String moduleName, Runnable moduleInitialiser) throws InitializationError {
        super(CeylonTestGroup.class);
        for (Runner group : list) {
            children.put(group, group.getDescription());
        }
        this.name = "Module "+moduleName;
        this.moduleInitialiser = moduleInitialiser;
    }
    
    @Override
    protected String getName() {
        return name;
    }
    
    @Override
    protected Description describeChild(Runner child) {
        return children.get(child);
    }

    @Override
    protected List<Runner> getChildren() {
        return new ArrayList<Runner>(this.children.keySet());
    }

    @Override
    protected void runChild(Runner method, RunNotifier notifier) {
        Description description = describeChild(method);
        notifier.fireTestStarted(description);
        Failure failure = null;
        try {
            method.run(notifier);
        } finally {
            if (failure != null) {
                notifier.fireTestFailure(failure);
            }
        }
        notifier.fireTestFinished(description);
    }

    @Override
    public int testCount() {
        int ret = 0;
        for(Runner child : children.keySet()){
            ret += child.testCount();
        }
        return ret;
    }

    @Override
    public void run(RunNotifier notifier) {
        synchronized(CompilerTest.RUN_LOCK){
            // the module initialiser code needs to run in a protected section because the language module Util is not loaded by
            // the test classloader but by our own classloader, which may be shared with other tests running in parallel, so if
            // we set up the module system while another thread is setting it up for other modules we're toast
            moduleInitialiser.run();
            super.run(notifier);
        }
    }
}
