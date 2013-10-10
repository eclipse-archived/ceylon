package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;

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
        final Options opts = new Options()
                .addSrc("src/test/resources/javadeps")
                .outDir("./build")
                .indent(false)
                .comment(false)
                .generateSourceArchive(false)
                .encoding("UTF-8");
        final JsCompiler comp = new JsCompiler(tc, opts);
        comp.generate();
    }

}
