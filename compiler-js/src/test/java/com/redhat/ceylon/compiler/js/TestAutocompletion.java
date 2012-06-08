package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

@RunWith(Parameterized.class)
public class TestAutocompletion {

    private final String file;
    private final AutocompleteVisitor assist;
    private final String nodeText;
    private final String checkCompletion;

    @Parameters
    public static Collection<Object[]> sources() {
        return Arrays.asList(new Object[][]{
            {"src/test/resources/complete/t1.ceylon", 3,  4, "pri", "print" },
            {"src/test/resources/complete/t2.ceylon", 4, 12, "ini", "initial" },
            {"src/test/resources/complete/t3.ceylon", 3, 16, "st",  "strings" },
            {"src/test/resources/complete/t4.ceylon", 3, 13, "m",   "maybe" },
            {"src/test/resources/complete/t5.ceylon", 2, 11, "",    "reversed" },
        });
    }

    public TestAutocompletion(String path, int r, int c, String text, String found) {
        file = path.substring(path.lastIndexOf('/')+1);
        TypeChecker tc = new TypeCheckerBuilder().verbose(false).addSrcDirectory(new File(path)).getTypeChecker();
        tc.process();
        nodeText = text;
        checkCompletion = found;
        assist = new AutocompleteVisitor(r, c, tc);
    }

    @Test
    public void testCompletions() {
        assist.findNode();
        Node node = assist.getNodeAtLocation();
        //Check that we did find a node
        Assert.assertNotNull("No node found at " + assist.getRow() + ":" + assist.getColumn() + " for file " + file, node);
        //Check that the node we found is the one we were actually looking for
        Assert.assertEquals(nodeText, assist.getTextAtLocation());
        List<String> comps = assist.getCompletions();
        System.out.println("Completions for " + file + ": " + comps);
        //Check that the completions contain the expected one
        Assert.assertTrue("Completion '" + checkCompletion + "' not found in " + comps, comps.contains(checkCompletion));
    }

}
