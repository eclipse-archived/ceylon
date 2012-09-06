package com.redhat.ceylon.compiler.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

public class TestModuleManager {

    private static TypeChecker tc;
    private static RepositoryManager repoman;
    private static Options options;

    @BeforeClass
    public static void setup() throws IOException {
        new java.io.File("build/test/test_modules").mkdirs();
        ArrayList<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("src/test/resources/loader/pass1/m1/test.ceylon",
                "-rep", "build/test/test_modules", "-out", "build/test/test_modules",
                "-src", "src/test/resources/loader/pass1"));
        options = Options.parse(args);
        repoman = CeylonUtils.makeRepositoryManager(
                options.getRepos(), options.getOutDir(), new JULLogger());
        //Create a typechecker to compile the test module
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.moduleManagerFactory(new JsModuleManagerFactory());
        tcb.addSrcDirectory(new java.io.File("src/test/resources/loader/pass1"));
        tcb.setRepositoryManager(repoman);
        tc = tcb.getTypeChecker();
        tc.process();
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
    }

    @Test
    public void test() {
        final RepositoryManager repoman = CeylonUtils.makeRepositoryManager(
                options.getRepos(), options.getOutDir(), new JULLogger());
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.moduleManagerFactory(new JsModuleManagerFactory());
        tcb.addSrcDirectory(new java.io.File("src/test/resources/loader/pass2"));
        tcb.setRepositoryManager(repoman);
        tc = tcb.getTypeChecker();
        tc.process();
    }

}
