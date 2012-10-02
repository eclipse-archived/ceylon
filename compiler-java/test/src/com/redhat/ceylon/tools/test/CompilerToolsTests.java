package com.redhat.ceylon.tools.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.tools.test.ImportJarToolTest;
import com.redhat.ceylon.tools.bashcompletion.BashCompletionToolTest;
import com.redhat.ceylon.tools.help.DocToolToolTest;
import com.redhat.ceylon.tools.help.HelpToolTest;
import com.redhat.ceylon.tools.help.MarkdownTest;
import com.redhat.ceylon.tools.help.PlaintextTest;

@RunWith(Suite.class)
@SuiteClasses({
    BashCompletionToolTest.class,
    CompilerToolTest.class,
    DocToolTest.class,
    ImportJarToolTest.class,
    PlaintextTest.class,
    MarkdownTest.class,
    HelpToolTest.class,
    DocToolToolTest.class
})
public class CompilerToolsTests {

}
