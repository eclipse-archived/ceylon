package com.redhat.ceylon.compiler.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.redhat.ceylon.compiler.test.expression.ExpressionTest;
import com.redhat.ceylon.compiler.test.issues.IssuesTest;
import com.redhat.ceylon.compiler.test.misc.MiscTest;
import com.redhat.ceylon.compiler.test.model.AnnotationsTest;
import com.redhat.ceylon.compiler.test.model.ModelLoaderTest;
import com.redhat.ceylon.compiler.test.model.TypeParserTest;
import com.redhat.ceylon.compiler.test.statement.StatementTest;
import com.redhat.ceylon.compiler.test.structure.StructureTest;

@RunWith(Suite.class) 
@SuiteClasses({
    AnnotationsTest.class,
    ExpressionTest.class,
    IssuesTest.class,
	MiscTest.class,
    ModelLoaderTest.class,
    StatementTest.class,
	StructureTest.class,
	TypeParserTest.class
})
public class AllTests {
}
