package com.redhat.ceylon.compiler.test.codegen;

import com.redhat.ceylon.compiler.codegen.Gen2;
import com.redhat.ceylon.compiler.codegen.GlobalGen;
import com.redhat.ceylon.compiler.tools.CeyloncFileManager;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class GlobalGenTest {

    private TreeMaker make;
    private Name.Table names;
    private GlobalGen globalGen;

    @Before
    public void setUp() throws Exception {
        Context context = new Context();
        CeyloncFileManager.preRegister(context);
        make = TreeMaker.instance(context);
        names = Name.Table.instance(context);
        globalGen = Gen2.getInstance(context).globalGen();
    }

    @Test
    public void testDefineSimple() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        JCTree result = globalGen
                .defineGlobal(variableType, variableName)
                .build();

        assertThat(toCanonicalString(result), is(lines(
                "@.com.redhat.ceylon.compiler.metadata.java.Ceylon",
                "final class variableName {",
                "    private static VariableType value;",
                "    ",
                "    static VariableType getVariableName() {",
                "        return value;",
                "    }",
                "    ",
                "    static void setVariableName(VariableType newValue) {",
                "        value = newValue;",
                "    }",
                "}")));
    }

    @Test
    public void testDefineAddToDefs() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        ListBuffer<JCTree> defsBuf = ListBuffer.lb();

        globalGen
                .defineGlobal(variableType, variableName)
                .appendDefinitionsTo(defsBuf);

        List<JCTree> defs = defsBuf.toList();

        assertThat(defs.size(), is(3));
        assertThat(toCanonicalString(defs.get(0)), is(lines("private static VariableType value")));
        assertThat(toCanonicalString(defs.get(1)), is(lines(
                "static VariableType getVariableName() {",
                "    return value;",
                "}")));

        assertThat(toCanonicalString(defs.get(2)), is(lines(
                "static void setVariableName(VariableType newValue) {",
                "    value = newValue;",
                "}")));
    }

    @Test
    public void testDefineReservedWordShouldBeQuotedInClassNameOnly() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "true";

        JCTree result = globalGen
                .defineGlobal(variableType, variableName)
                .build();

        assertThat(toCanonicalString(result), containsString("class $true"));
        assertThat(toCanonicalString(result), containsString("getTrue("));
        assertThat(toCanonicalString(result), containsString("setTrue("));
    }

    @Test
    public void testDefineVisibilities() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .classVisibility(Flags.PUBLIC)
                .getterVisibility(Flags.PROTECTED)
                .setterVisibility(Flags.PRIVATE)
                .build();

        String result = toCanonicalString(tree);

        assertThat(result, containsString("public final class variableName"));
        assertThat(result, containsString("protected static VariableType getVariableName("));
        assertThat(result, containsString("private static void setVariableName("));
    }

    @Test
    public void testDefineImmutableAndInitialValue() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        JCTree.JCExpression initialValue = make.Ident(names.fromString("initialValue"));

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .immutable()
                .initialValue(initialValue)
                .build();

        String result = toCanonicalString(tree);

        assertThat(result, containsString("value = initialValue;"));
        assertThat(result, not(containsString("setVariableName")));
    }

    @Test
    public void testDefineClassAnnotations() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .classAnnotations(List.of(createAnnotationWithType("AnnotationType")))
                .build();

        String result = toCanonicalString(tree);

        assertThat("annotation applied to class", result,
                containsString(lines(
                        "@AnnotationType",
                        "final class variableName")));
    }

    @Test
    public void testDefineValueAnnotations() {
        JCTree.JCExpression variableType = toType("VariableType");
        String variableName = "variableName";

        JCTree tree = globalGen
                .defineGlobal(variableType, variableName)
                .valueAnnotations(List.of(createAnnotationWithType("AnnotationType")))
                .build();

        String result = toCanonicalString(tree);

        assertThat("annotation applied to getter", result,
                containsString(lines(
                        "@AnnotationType",
                        "    static VariableType getVariableName")));
        assertThat("annotation applied to setter parameter", result,
                containsString(lines(
                        "setVariableName(@AnnotationType",
                        "    VariableType newValue")));
    }

    private JCTree.JCAnnotation createAnnotationWithType(String annotationType) {
        return make.Annotation(make.Ident(toName(annotationType)),
                List.<JCTree.JCExpression>nil());
    }

    @Test
    public void testGetter() {
        JCTree.JCExpression packageName = make.Select(
                make.Ident(toName("some")),
                toName("pkg"));
        String variableName = "variableName";

        JCTree.JCExpression expr = globalGen.getGlobalValue(packageName, variableName);
        String result = toCanonicalString(expr);

        assertThat(result, is("some.pkg.variableName.getVariableName()"));
    }

    @Test
    public void testSetter() {
        JCTree.JCExpression packageName = make.Select(
                make.Ident(toName("some")),
                toName("pkg"));
        String variableName = "variableName";

        JCTree.JCExpression newValue = make.Ident(toName("newVariableValue"));

        JCTree.JCExpression expr = globalGen.setGlobalValue(packageName, variableName, newValue);
        String result = toCanonicalString(expr);

        assertThat(result, is("some.pkg.variableName.setVariableName(newVariableValue)"));
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
