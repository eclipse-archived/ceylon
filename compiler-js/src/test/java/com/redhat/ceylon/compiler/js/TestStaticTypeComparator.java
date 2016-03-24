package com.redhat.ceylon.compiler.js;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;

public class TestStaticTypeComparator {

    private static TypeChecker tc;

    @BeforeClass
    public static void setup() {
        final TypeCheckerBuilder tcb = new TypeCheckerBuilder().statistics(true).encoding("UTF-8");
        tcb.addSrcDirectory(new File("../language/src"));
        tc = tcb.getTypeChecker();
        tc.process();
    }

    private static TypeDeclaration getTypeDeclaration(String name) {
        return (TypeDeclaration)tc.getPhasedUnits().getPhasedUnits().get(0).getUnit().getPackage().getMember(name, null, false);
    }

    private static Type getType(String name, Type... targs) {
        TypeDeclaration td = getTypeDeclaration(name);
        return td.appliedType(null, Arrays.asList(targs));
    }

    private static Tree.StaticType staticType(Type pt) {
        Tree.StaticType node = new Tree.BaseType(null);
        node.setTypeModel(pt);
        return node;
    }

    @Test
    public void testCollection_vs_Range() {
        final Type d_coll = getType("Collection", getType("Integer"));
        final Type d_ranged = getType("Ranged", getType("Integer"), getType("Integer"), getType("List", getType("Integer")));
        List<Tree.StaticType> types = Arrays.asList(staticType(d_coll), staticType(d_ranged));
        Collections.sort(types, new TypeGenerator.StaticTypeComparator());
        Assert.assertTrue(d_ranged == types.get(0).getTypeModel());
        types = Arrays.asList(staticType(d_ranged), staticType(d_coll));
        Collections.sort(types, new TypeGenerator.StaticTypeComparator());
        Assert.assertTrue(d_ranged == types.get(0).getTypeModel());
        final Type d_iterable = getType("Iterable", getType("Integer"), getType("Null"));
        types = Arrays.asList(staticType(d_coll), staticType(d_ranged), staticType(d_iterable));
        Collections.sort(types, new TypeGenerator.StaticTypeComparator());
        Assert.assertTrue(d_iterable == types.get(0).getTypeModel());
        Assert.assertTrue(d_ranged == types.get(1).getTypeModel());
        Assert.assertTrue(d_coll == types.get(2).getTypeModel());
    }

}
