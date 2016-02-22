package com.redhat.ceylon.model.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.model.test.loader.impl.reflect.CachedTOCJarsTest;

@RunWith(Suite.class) 
@SuiteClasses({
    CachedTOCJarsTest.class
})
public class AllTests {
}
