package com.redhat.ceylon.compiler.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;

class ClassVisitor extends StatementVisitor {
    final ClassDefinitionBuilder classBuilder;

    ClassVisitor(CeylonTransformer gen, ClassDefinitionBuilder classBuilder) {
        super(gen);
        this.classBuilder = classBuilder;
    }

    // Class Initializer parameter
    public void visit(Tree.Parameter param) {
        classBuilder.parameter(param);
    }

    public void visit(Tree.Block b) {
        b.visitChildren(this);
    }

    public void visit(Tree.MethodDefinition meth) {
        classBuilder.defs(classGen.transform(meth));
    }

    public void visit(Tree.MethodDeclaration meth) {
        classBuilder.defs(classGen.transform(meth));
    }

    public void visit(Tree.Annotation ann) {
        // Handled in processAnnotations
    }

    // FIXME: Here we've simplified CeylonTree.MemberDeclaration to
    // Tree.AttributeDeclaration
    public void visit(Tree.AttributeDeclaration decl) {
        classGen.transform(decl, classBuilder);
    }

    public void visit(final Tree.AttributeGetterDefinition getter) {
        classBuilder.defs(classGen.transform(getter));
    }

    public void visit(final Tree.AttributeSetterDefinition setter) {
        classBuilder.defs(classGen.transform(setter));
    }

    public void visit(final Tree.ClassDefinition cdecl) {
        classBuilder.defs(classGen.transform(cdecl));
    }

    public void visit(final Tree.InterfaceDefinition cdecl) {
        classBuilder.defs(classGen.transform(cdecl));
    }

    // FIXME: also support Tree.SequencedTypeParameter
    public void visit(Tree.TypeParameterDeclaration param) {
        classBuilder.typeParameter(param);
    }

    public void visit(Tree.ExtendedType extendedType) {
        classBuilder.extending(extendedType);
    }

    // FIXME: implement
    public void visit(Tree.TypeConstraint l) {
    }
}
