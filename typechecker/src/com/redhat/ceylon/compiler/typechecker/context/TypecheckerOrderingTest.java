package com.redhat.ceylon.compiler.typechecker.context;

import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

public class TypecheckerOrderingTest {

    // Change this to the location of "ceylon.language/src" in your distribution
    private static final File SRC_DIR = new File("../ceylon.language/src");

    private static void testCompileLanguageModule() throws IOException {
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.addSrcDirectory(SRC_DIR);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
    }

    public static void main(String[] args) throws IOException {
        for (int i=1; i <= 20; i++) {
            System.out.println("Try " + i);
            testCompileLanguageModule();
        }
    }
}
