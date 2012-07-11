package com.redhat.ceylon.tools;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class) 
@SuiteClasses({
    ModelBuilderTest.class,
    ToolBuilderTest.class
})
public class ToolsSuite {

    public ToolsSuite() {
        // TODO Auto-generated constructor stub
    }

}
