package com.redhat.ceylon.compiler.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;

//Tests for issue 195
public class TestMultipackages {

    private static RepositoryManager repoman;

    @BeforeClass
    public static void sharedSetup() {
        ArrayList<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("multi/1.0.0",
                "-rep", "build/test/test_modules", "-out", "build/test/test_modules",
                "-src", "src/test/resources/loader"));
        Options options = Options.parse(args);
        repoman = CeylonUtils.repoManager()
                .systemRepo(options.getSystemRepo())
                .userRepos(options.getRepos())
                .outRepo(options.getOutDir())
                .buildManager();
    }

    @Before
    public void setup() throws IOException {
        //Compile module with 2 packages
        System.out.println("Compiling multi");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.addSrcDirectory(new java.io.File("src/test/resources/multi/pass1"));
        tcb.setRepositoryManager(repoman);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
        ArrayList<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(
                "-rep", "build/test/test_modules", "-out", "build/test/test_modules",
                "-src", "src/test/resources/multi/pass1"));
        Options options = Options.parse(args);
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
    }

    @Test
    public void test() throws IOException {
        //Compile module with 2 packages
        System.out.println("Compiling usemulti");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.moduleManagerFactory(new JsModuleManagerFactory("UTF-8"));
        tcb.addSrcDirectory(new java.io.File("src/test/resources/multi/pass2"));
        tcb.setRepositoryManager(repoman);
        TypeChecker tc = tcb.getTypeChecker();
        tc.process();
        ArrayList<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList(
                "-rep", "build/test/test_modules", "-out", "build/test/test_modules",
                "-src", "src/test/resources/multi/pass2", "-verbose"));
        Options options = Options.parse(args);
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
    }

}
