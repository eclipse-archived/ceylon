package com.redhat.ceylon.compiler.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.test.misc.MiscTest;
import com.redhat.ceylon.compiler.test.statement.StatementTest;
import com.redhat.ceylon.compiler.test.structure.StructureTest;

@RunWith(Suite.class) 
@SuiteClasses({
	MiscTest.class,
	StructureTest.class,
	StatementTest.class
})
public class AllTests {
}
