package org.eclipse.ceylon.test.smoke.test;

import org.eclipse.ceylon.test.maven.test.AetherTestCase;
import org.eclipse.ceylon.test.maven.test.ResolverTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        SmokeTestCase.class,
        HerdTestCase.class,
        BuilderTestCase.class,
        CallbackTestCase.class,
        AetherTestCase.class,
        ResolverTestCase.class
})
public class AllCmrTests {
}
