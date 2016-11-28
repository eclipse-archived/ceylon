package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.io.IOException;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import org.junit.Test;

import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

public class TestJavaDeps {

    @Test
    public void testJavaDependencies() throws IOException {
        final RepositoryManager repoman = CeylonUtils.repoManager()
                .systemRepo("../dist/dist/repo")
                .outRepo("test-modules")
                .buildManager();
        final TypeCheckerBuilder builder = new TypeCheckerBuilder()
            .setRepositoryManager(repoman)
            .addSrcDirectory(new File("src/test/resources/javadeps"));
        final TypeChecker tc = builder.getTypeChecker();
        tc.process();
        final Options opts = new Options()
                .addSrcDir("src/test/resources/javadeps")
                .outRepo("./build")
                .comment(false)
                .generateSourceArchive(false)
                .encoding("UTF-8");
        final JsCompiler comp = new JsCompiler(tc, opts);
        comp.generate();
    }

}
