package com.redhat.ceylon.compiler.java.test;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.Suite;

public class ConcurrentSuite extends Suite {

    public ConcurrentSuite(Class<?> clase, RunnerBuilder builder) throws InitializationError {
        super(clase, builder);
        setScheduler(new ConcurrentScheduler());
    }
}
