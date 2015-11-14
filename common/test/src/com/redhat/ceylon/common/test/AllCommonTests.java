package com.redhat.ceylon.common.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.common.tool.ToolsSuite;

@RunWith(Suite.class) 
@SuiteClasses({
    ConfigSuite.class,
    ToolsSuite.class
})
public class AllCommonTests {
}
