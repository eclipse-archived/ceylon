package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

public class TestJavaDeps {

    @Test
    public void testJavaDependencies() throws IOException {
        final TypeCheckerBuilder builder = new TypeCheckerBuilder();
        builder.addSrcDirectory(new File("src/test/resources/javadeps"));
        final TypeChecker tc = builder.getTypeChecker();
        tc.process();
        final Options opts = new Options(Collections.<String>emptyList(), Collections.singletonList("src/test/resources/javadeps"),
                null, null, "./build", null, null, false, true, false, true, null, false, false, false, "UTF-8", false);
        final JsCompiler comp = new JsCompiler(tc, opts);
        comp.generate();
    }

}
