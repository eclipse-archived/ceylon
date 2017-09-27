package org.eclipse.ceylon.model.test;

import org.eclipse.ceylon.model.test.loader.impl.reflect.CachedTOCJarsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class) 
@SuiteClasses({
    CachedTOCJarsTest.class,
    ClassFileUtilTest.class,
    OsgiVersionTests.class
})
public class AllModelTests {
}
