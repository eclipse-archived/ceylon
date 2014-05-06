package com.redhat.ceylon.compiler.js;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class AttributeGenerator {

    static void getter(final Tree.AttributeGetterDefinition that, GenerateJsVisitor gen) {
        gen.beginBlock();
        gen.initSelf(that.getDeclarationModel(), false);
        gen.visitStatements(that.getBlock().getStatements());
        gen.endBlock();
    }

    static void setter(final Tree.AttributeSetterDefinition that, GenerateJsVisitor gen) {
        if (that.getSpecifierExpression() == null) {
            gen.beginBlock();
            gen.initSelf(that.getDeclarationModel(), false);
            gen.visitStatements(that.getBlock().getStatements());
            gen.endBlock();
        } else {
            gen.out("{");
            gen.initSelf(that.getDeclarationModel(), true);
            gen.out("return ");
            that.getSpecifierExpression().visit(gen);
            gen.out(";}");
        }
    }

}
