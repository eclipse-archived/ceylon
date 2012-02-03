package com.redhat.ceylon.compiler.java.test;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runners.model.InitializationError;
import org.junit.runners.Suite;

public class ConcurrentSuite extends Suite {

    public ConcurrentSuite(Class<?> clase) throws InitializationError {
        super(clase, new AllDefaultPossibilitiesBuilder(true));
        setScheduler(new ConcurrentScheduler());
    }
}
