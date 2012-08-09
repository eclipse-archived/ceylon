package com.redhat.ceylon.tools;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.tools.importjar.ImportJarToolTest;
import com.redhat.ceylon.tools.help.HelpToolTest;
import com.redhat.ceylon.tools.help.PlaintextTest;

@RunWith(Suite.class)
@SuiteClasses({
    CompilerToolTest.class,
    DocToolTest.class,
    ImportJarToolTest.class,
    PlaintextTest.class,
    HelpToolTest.class,
})
public class CompilerToolsTests {

}
