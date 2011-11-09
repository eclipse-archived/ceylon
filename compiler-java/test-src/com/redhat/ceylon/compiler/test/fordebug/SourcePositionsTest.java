package com.redhat.ceylon.compiler.test.fordebug;

import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class SourcePositionsTest extends CompilerTest {
	
    @Test
    public void testSimplePositions(){
        compareWithJavaSourceWithPositions("positions/simple");
    }

    @Test
    public void testSimpleLines(){
        compareWithJavaSourceWithLines("lines/simple");
    }
}
