package com.redhat.ceylon.compiler.test.codegen;

import com.redhat.ceylon.compiler.codegen.Gen2;
import com.redhat.ceylon.compiler.codegen.GlobalGen;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class GlobalGenTest {

    private Context context;
    private TreeMaker make;
    private Name.Table names;
    private GlobalGen globalGen;

    @Before
    public void setUp() throws Exception {
        context = new Context();
        CeyloncFileManager.preRegister(context);
        make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        globalGen = Gen2.getInstance(context).globalGen();
    }

    @Test
    public void testDefineSimple() {
        JCTree.JCExpression variableType = toType("VariableType");
        Name variableName = toName("variableName");

        JCTree result = globalGen
                .defineGlobal(variableType, variableName)
                .build();

        assertThat(toCanonicalString(result), is(lines(
                "final class variableName {",
                "    private static VariableType value;",
                "    ",
                "    static VariableType getValue() {",
                "        return value;",
                "    }",
                "    ",
                "    static void setValue(VariableType newValue) {",
                "        value = newValue;",
                "    }",
                "}")));
    }

    @Test
    public void testDefineVisibilities() {
        JCTree.JCExpression variableType = toType("VariableType");
        Name variableName = toName("variableName");

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .classVisibility(Flags.PUBLIC)
                .getterVisibility(Flags.PROTECTED)
                .setterVisibility(Flags.PRIVATE)
                .build();

        String result = toCanonicalString(tree);

        assertThat(result, containsString("public final class variableName"));
        assertThat(result, containsString("protected static VariableType getValue("));
        assertThat(result, containsString("private static void setValue("));
    }

    @Test
    public void testDefineImmutableAndInitialValue() {
        JCTree.JCExpression variableType = toType("VariableType");
        Name variableName = toName("variableName");

        JCTree.JCExpression initialValue = make.Ident(names.fromString("initialValue"));

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .immutable()
                .initialValue(initialValue)
                .build();

        String result = toCanonicalString(tree);

        assertThat(result, containsString("value = initialValue;"));
        assertThat(result, not(containsString("setValue")));
    }

    @Test
    public void testGetValue() {
        JCTree.JCExpression packageName = make.Select(
                make.Ident(toName("some")),
                toName("pkg"));
        Name variableName = toName("variableName");

        JCTree.JCExpression expr = globalGen.getGlobalValue(packageName, variableName);
        String result = toCanonicalString(expr);

        assertThat(result, is("some.pkg.variableName.getValue()"));
    }

    @Test
    public void testSetValue() {
        JCTree.JCExpression packageName = make.Select(
                make.Ident(toName("some")),
                toName("pkg"));
        Name variableName = toName("variableName");

        JCTree.JCExpression newValue = make.Ident(toName("newVariableValue"));

        JCTree.JCExpression expr = globalGen.setGlobalValue(packageName, variableName, newValue);
        String result = toCanonicalString(expr);

        assertThat(result, is("some.pkg.variableName.setValue(newVariableValue)"));
    }

    private JCTree.JCIdent toType(String type) {
        return make.Ident(names.fromString(type));
    }

    private Name toName(String name) {
        return names.fromString(name);
    }

    private String toCanonicalString(JCTree result) {
        return result.toString().trim();
    }

    private static String lines(String... lines) {
        if (lines.length == 0) {
            return "";
        }

        if (lines.length == 1) {
            return lines[0];
        }

        StringBuilder builder = new StringBuilder(lines[0]);
        String eol = System.getProperty("line.separator");

        for (int i = 1; i < lines.length; i++) {
            builder.append(eol).append(lines[i]);
        }

        return builder.toString();
    }

}
