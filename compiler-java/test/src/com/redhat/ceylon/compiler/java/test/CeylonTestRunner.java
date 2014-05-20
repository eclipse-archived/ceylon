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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.ComparisonFailure;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

public class CeylonTestRunner extends ParentRunner<Method> {

    private Class<?> testClass;
    
    private LinkedHashMap<Method, Description> children = new LinkedHashMap<Method, Description>();

    private Method failureCountGetter;

    public CeylonTestRunner(Class<?> testClass, Method failureCountGetter, List<String> list) throws InitializationError {
        super(testClass);
        this.testClass = testClass;
        this.failureCountGetter = failureCountGetter;
        for (String method : list) {
            Method m;
            try {
                m = testClass.getMethod(method);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Description description = Description.createTestDescription(testClass, method);
            children.put(m, description);
        }
    }
    
    @Override
    protected Description describeChild(Method child) {
        return children.get(child);
    }

    @Override
    protected List<Method> getChildren() {
        return new ArrayList<Method>(this.children.keySet());
    }

    @Override
    protected void runChild(Method method, RunNotifier notifier) {
        Description description = describeChild(method);
        notifier.fireTestStarted(description);
        Failure failure = null;
        try {
            failure = executeTest(method, description);
        } finally {
            if (failure != null) {
                notifier.fireTestFailure(failure);
            }
        }
        notifier.fireTestFinished(description);
    }

    private Failure executeTest(Method child, Description description) {
        Failure failure = null;
        try {
            
            Object instance;
            if (Modifier.isStatic(child.getModifiers())) {
                instance = null;
            } else {
                instance = testClass.newInstance();
            }
            Long previousCount = null;
            if(failureCountGetter != null)
                previousCount = (Long) failureCountGetter.invoke(null);
            
            child.invoke(instance);
            
            if(failureCountGetter != null){
                Long newCount = (Long) failureCountGetter.invoke(null);
                if(!newCount.equals(previousCount))
                    failure = new Failure(description, new AssertionError("check() failed: "+(newCount - previousCount)+" errors"));
            }
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            StackTraceElement[] st = cause.getStackTrace();
            if ("ceylon.language.AssertionError".equals(cause.getClass().getName())) {
                AssertionError error = new AssertionError(cause.getMessage());
                error.setStackTrace(cause.getStackTrace());
                failure = new Failure(description, error);
            } else if ("com.redhat.ceylon.compiler.java.test.runtime.AssertionFailed".equals(cause.getClass().getName())) {
                AssertionError error = new AssertionError(cause.getMessage());
                error.setStackTrace(cause.getStackTrace());
                failure = new Failure(description, error);
            } else if ("com.redhat.ceylon.compiler.java.test.runtime.ComparisonFailed".equals(cause.getClass().getName())) {
                Object expected = get(cause, "getExpected");
                Object got = get(cause, "getGot");
                ComparisonFailure error = new ComparisonFailure(cause.getMessage(), String.valueOf(expected), String.valueOf(got));
                error.setStackTrace(cause.getStackTrace());
                failure = new Failure(description, error);
            } else {
                failure = new Failure(description, e);
            }
        } catch (Exception e) {
            failure = new Failure(description, e);
        } catch (AssertionError e) {
            failure = new Failure(description, e);
        }
        return failure;
    }

    private Object get(Throwable cause, String getter) {
        try {
            Method method = cause.getClass().getMethod(getter);
            Object expected = method.invoke(cause);
            return expected;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    
    public int testCount() {
        return this.children.size();
    }
    
}
