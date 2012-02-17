package com.redhat.ceylon.compiler.java.test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class ConcurrentRunner extends BlockJUnit4ClassRunner {

    public ConcurrentRunner(Class<?> clase) throws InitializationError {
        super(clase);
        setScheduler(new ConcurrentScheduler());
    }
}
