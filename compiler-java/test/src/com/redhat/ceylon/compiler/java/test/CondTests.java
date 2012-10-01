package com.redhat.ceylon.compiler.java.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.java.test.expression.ExpressionTest3;
import com.redhat.ceylon.compiler.java.test.statement.StatementTest;

@RunWith(Suite.class)
@SuiteClasses({
    StatementTest.class, 
    ExpressionTest3.class
})
public class CondTests {

}
