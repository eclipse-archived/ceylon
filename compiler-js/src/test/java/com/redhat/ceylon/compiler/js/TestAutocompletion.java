package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

@RunWith(Parameterized.class)
public class TestAutocompletion {

    private final String file;
    private final AutocompleteVisitor assist;

    @Parameters
    public static Collection<Object[]> sources() {
        return Arrays.asList(new Object[][]{
            {"src/test/resources/complete/t1.ceylon", 3, 4 },
            {"src/test/resources/complete/t2.ceylon", 4, 12 },
            {"src/test/resources/complete/t3.ceylon", 3, 16 },
            {"src/test/resources/complete/t4.ceylon", 3, 13 },
        });
    }

    public TestAutocompletion(String path, int r, int c) {
        file = path.substring(path.lastIndexOf('/')+1);
        TypeChecker tc = new TypeCheckerBuilder().verbose(false).addSrcDirectory(new File(path)).getTypeChecker();
        tc.process();
        assist = new AutocompleteVisitor(r, c, tc);
        assist.findNode();
    }

    @Test
    public void testCompletions() {
        Node node = assist.getNodeAtLocation();
        Assert.assertNotNull("No node found at " + assist.getRow() + ":" + assist.getColumn() + " for file " + file, node);
        System.out.println("FOUND IT! " + node.getText() + " @ " + node.getLocation());
        System.out.println("Completions for " + file + ": " + assist.getCompletions());
    }

}
