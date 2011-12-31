package com.redhat.ceylon.compiler.js;

import static java.lang.Character.toUpperCase;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnnotationList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AssignOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Body;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CharLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExecutableStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Outer;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Return;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StringLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SumOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Super;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.This;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class GenerateJsVisitor extends Visitor 
        implements NaturalVisitor {
    
    private final Writer out;
    
    public GenerateJsVisitor(Writer out) {
        this.out = out;
    }

    private void out(String code) {
        try {
            out.write(code);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    int indentLevel = 0;
    
    private void indent() {
        for (int i=0;i<indentLevel;i++) {
            out("    ");
        }
    }
    
    private void endLine() {
        out("\n");
        indent();
    }

    private void beginBlock() {
        indentLevel++;
        out("{");
        endLine();
    }
    
    private void endBlock() {
        indentLevel--;
        endLine();
        out("}");
        endLine();
    }
    
    private void location(Node node) {
        out(" at ");
        out(node.getUnit().getFilename());
        out(" (");
        out(node.getLocation());
        out(")");
    }
    
    @Override
    public void visit(CompilationUnit that) {
        super.visit(that);
        try {
            out.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    @Override
    public void visit(Parameter that) {
        out(that.getDeclarationModel().getName());
    }
    
    @Override
    public void visit(ParameterList that) {
        out("(");
        boolean first=true;
        for (Parameter param: that.getParameters()) {
            if (!first) out(",");
            out(param.getDeclarationModel().getName());
            first = false;
        }
        out(")");
    }
    
    @Override
    public void visit(Body that) {
        List<Statement> stmnts = that.getStatements();
        for (int i=0; i<stmnts.size(); i++) {
            Statement s = stmnts.get(i);
            s.visit(this);
            if (s instanceof ExecutableStatement) {
                endLine();
            }
        }
    }
    
    @Override
    public void visit(Block that) {
        List<Statement> stmnts = that.getStatements();
        if (stmnts.isEmpty()) {
            out("{}");
            endLine();
        }
        else {
            beginBlock();
            for (int i=0; i<stmnts.size(); i++) {
                Statement s = stmnts.get(i);
                s.visit(this);
                if (i<stmnts.size()-1 && 
                        s instanceof ExecutableStatement) {
                    endLine();
                }
            }
            endBlock();
        }
    }
    
    @Override
    public void visit(InterfaceDefinition that) {
        endLine();
        out("//interface ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("function ");
        out(that.getDeclarationModel().getName());
        out("()");
        beginBlock();
        out("var ");
        out(self(that.getDeclarationModel()));
        out("={};");
        endLine();
        if (that.getSatisfiedTypes()!=null)
        for (Tree.SimpleType st: that.getSatisfiedTypes().getTypes()) {
            out("var $super");
            out(st.getDeclarationModel().getName());
            out("=");
            out(st.getDeclarationModel().getName());
            out("();");
            endLine();
            out("for(var $m in $super");
            out(st.getDeclarationModel().getName());
            out("){");
            out(self(that.getDeclarationModel()));
            out("[$m]=$super");
            out(st.getDeclarationModel().getName());
            out("[$m]}");
            endLine();
        }
        that.getInterfaceBody().visit(this);
        out("return ");
        out(self(that.getDeclarationModel()));
        out(";");
        endBlock();
    }
    
    @Override
    public void visit(ClassDefinition that) {
        endLine();
        out("//class ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("function ");
        out(that.getDeclarationModel().getName());
        that.getParameterList().visit(this);
        beginBlock();
        out("var "); 
        out(self(that.getDeclarationModel()));
        out("={};");
        endLine();
        if (that.getExtendedType()!=null) {
            out("var $super=");
            out(that.getExtendedType().getType()
                    .getDeclarationModel().getName());
            that.getExtendedType().getInvocationExpression().visit(this);
            out(";");
            endLine();
            out("for(var $m in $super){");
            out(self(that.getDeclarationModel()));
            out("[$m]=$super[$m]}");
            endLine();
        }
        if (that.getSatisfiedTypes()!=null)
        for (Tree.SimpleType st: that.getSatisfiedTypes().getTypes()) {
            out("var $super");
            out(st.getDeclarationModel().getName());
            out("=");
            out(st.getDeclarationModel().getName());
            out("();");
            endLine();
            out("for(var $m in $super");
            out(st.getDeclarationModel().getName());
            out("){");
            out(self(that.getDeclarationModel()));
            out("[$m]=$super");
            out(st.getDeclarationModel().getName());
            out("[$m]}");
            endLine();
        }
        that.getClassBody().visit(this);
        out("return ");
        out(self(that.getDeclarationModel()));
        out(";");
        endBlock();
        if (that.getDeclarationModel().isClassOrInterfaceMember()) {
            out(outerSelf(that.getDeclarationModel()));
            out(".");
            out(that.getDeclarationModel().getName());
            out("=");
            out(that.getDeclarationModel().getName());
            out(";");
            endLine();
        }
    }
        
    @Override
    public void visit(ObjectDefinition that) {
        //TODO: fix copy/paste from ClassDefinition
        endLine();
        out("//object ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("var $");
        out(that.getDeclarationModel().getName());
        out("=");
        out("new function ");
        out(that.getDeclarationModel().getName());
        out("()");
        beginBlock();
        out("var ");
        out(self(that.getDeclarationModel()));
        out("={};");
        endLine();
        if (that.getExtendedType()!=null) {
            out("var $super=");
            out(that.getExtendedType().getType()
                    .getDeclarationModel().getName());
            that.getExtendedType().getInvocationExpression().visit(this);
            out(";");
            endLine();
            out("for(var $m in $super){");
            out(self(that.getDeclarationModel()));
            out("[$m]=$super[$m]}");
            endLine();
        }
        if (that.getSatisfiedTypes()!=null)
        for (Tree.SimpleType st: that.getSatisfiedTypes().getTypes()) {
            out(st.getDeclarationModel().getName());
            out("(");
            out(self(that.getDeclarationModel()));
            out(");");
            endLine();
        }
        that.getClassBody().visit(this);
        out("return ");
        out(self(that.getDeclarationModel()));
        out(";");         
        indentLevel--;
        endLine();
        out("}();");
        endLine();
        if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                that.getDeclarationModel().isShared()) {
            out(outerSelf(that.getDeclarationModel()));
            out(".");
            out(getter(that.getDeclarationModel()));
            out("=");
        }
        out("function ");
        out(getter(that.getDeclarationModel()));
        out("()");
        beginBlock();
        out("return $");
        out(that.getDeclarationModel().getName());
        out(";");
        endBlock();
    }
        
    @Override
    public void visit(MethodDeclaration that) {}
        
    @Override
    public void visit(MethodDefinition that) {
        endLine();
        out("//function ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("function ");
        out(that.getDeclarationModel().getName());
        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        super.visit(that);
        if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                that.getDeclarationModel().isShared()) {
            out(outerSelf(that.getDeclarationModel()));
            out(".");
            out(that.getDeclarationModel().getName());
            out("=");
            out(that.getDeclarationModel().getName());
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(AttributeGetterDefinition that) {
        endLine();
        out("//value ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("function ");
        out(getter(that.getDeclarationModel()));
        out("()");
        super.visit(that);
        if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                that.getDeclarationModel().isShared()) {
            out(outerSelf(that.getDeclarationModel()));
            out(".");
            out(getter(that.getDeclarationModel()));
            out("=");
            out(getter(that.getDeclarationModel()));
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(AttributeSetterDefinition that) {
        endLine();
        out("//assign ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        out("function ");
        out(setter(that.getDeclarationModel()));
        out("(");
        out(that.getDeclarationModel().getName());
        out(")");
        super.visit(that);
        if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                that.getDeclarationModel().isShared()) {
            out(outerSelf(that.getDeclarationModel()));
            out(".");
            out(setter(that.getDeclarationModel()));
            out("=");
            out(setter(that.getDeclarationModel()));
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(AttributeDeclaration that) {
        endLine();
        out("//value ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
        if (!that.getDeclarationModel().isFormal()) {
            out("var $");
            out(that.getDeclarationModel().getName());
            if (that.getSpecifierOrInitializerExpression()!=null) {
                out("=");
            }
            super.visit(that);
            out(";");
            endLine();
            out("function ");
            out(getter(that.getDeclarationModel()));
            out("()");
            beginBlock();
            out("return $");
            out(that.getDeclarationModel().getName());
            out(";");
            endBlock();
            if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                    that.getDeclarationModel().isShared()) {
                out(outerSelf(that.getDeclarationModel()));
                out(".");
                out(getter(that.getDeclarationModel()));
                out("=");
                out(getter(that.getDeclarationModel()));
                out(";");
                endLine();
            }
            if (that.getDeclarationModel().isVariable()) {
                out("function ");
                out(setter(that.getDeclarationModel()));
                out("(");
                out(that.getDeclarationModel().getName());
                out(")");
                beginBlock();
                out("$");
                out(that.getDeclarationModel().getName());
                out("=");
                out(that.getDeclarationModel().getName());
                out(";");
                endBlock();
                if (that.getDeclarationModel().isClassOrInterfaceMember() &&
                        that.getDeclarationModel().isShared()) {
                    out(outerSelf(that.getDeclarationModel()));
                    out(".");
                    out(setter(that.getDeclarationModel()));
                    out("=");
                    out(setter(that.getDeclarationModel()));
                    out(";");
                    endLine();
                }
            }
        }
    }
    
    @Override
    public void visit(CharLiteral that) {
        out("new Character(");
        out(that.getText().replace('`', '"'));
        out(")");
    }
    
    @Override
    public void visit(StringLiteral that) {
        out("new String(");
        out(that.getText());
        out(")");
    }
    
    @Override
    public void visit(FloatLiteral that) {
        out("new Float(");
        out(that.getText());
        out("'");
    }
    
    @Override
    public void visit(NaturalLiteral that) {
        out("new Integer(");
        out(that.getText());
        out(")");
    }
    
    @Override
    public void visit(This that) {
        out(self(that.getTypeModel().getDeclaration()));
    }
    
    @Override
    public void visit(Super that) {
        out("$super");
    }
    
    @Override
    public void visit(Outer that) {
        out(self(that.getTypeModel().getDeclaration()));
    }
    
    @Override
    public void visit(BaseMemberExpression that) {
        if (that.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter ||
                that.getDeclaration() instanceof Method) {
            if (qualifyBaseMember(that, that.getDeclaration())) {
                out(self(that.getScope(), that.getDeclaration()));
                out(".");
            }
            out(that.getDeclaration().getName());
        }
        else {
            if (qualifyBaseMember(that, that.getDeclaration())) {
                out(self(that.getScope(), that.getDeclaration()));
                out(".");
            }
            out(getter(that.getDeclaration()));
            out("()");
        }
    }
    
    @Override
    public void visit(QualifiedMemberExpression that) {
        super.visit(that);
        out(".");
        if (that.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter ||
                that.getDeclaration() instanceof Method) {
            out(that.getDeclaration().getName());
        }
        else {
            out(getter(that.getDeclaration()));
            out("()");
        }
    }
    
    @Override
    public void visit(BaseTypeExpression that) {
        if (qualifyBaseMember(that, that.getDeclaration())) {
            out(self(that.getScope(), that.getDeclaration()));
            out(".");
        }
        out(that.getDeclaration().getName());
    }
    
    @Override
    public void visit(QualifiedTypeExpression that) {
        super.visit(that);
        out(".");
        out(that.getDeclaration().getName());
    }
    
    @Override
    public void visit(InvocationExpression that) {
        if (that.getNamedArgumentList()!=null) {
            out("(function (){");
            that.getNamedArgumentList().visit(this);
            out("return ");
            that.getPrimary().visit(this);
            out("(");
            if (that.getPrimary().getDeclaration() instanceof Functional) {
                Functional f = (Functional) that.getPrimary().getDeclaration();
                if (!f.getParameterLists().isEmpty()) {
                    boolean first=true;
                    for (com.redhat.ceylon.compiler.typechecker.model.Parameter p: 
                        f.getParameterLists().get(0).getParameters()) {
                        if (!first) out(",");
                        out("$");
                        out(p.getName());
                        first = false;
                    }
                }
            }
            out(")}())");
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(PositionalArgumentList that) {
        out("(");
        boolean first=true;
        boolean sequenced=false;
        for (PositionalArgument arg: that.getPositionalArguments()) {
            if (!first) out(",");
            if (!sequenced && arg.getParameter().isSequenced()) {
                sequenced=true;
                out("new ArraySequence([");
            }
            arg.visit(this);
            first = false;
        }
        if (sequenced) {
            out("])");
        }
        out(")");
    }
    
    @Override
    public void visit(NamedArgumentList that) {
        for (NamedArgument arg: that.getNamedArguments()) {
            out("var $");
            out(arg.getParameter().getName());
            out("=");
            arg.visit(this);
            out(";");
        }
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg!=null) {
            out("var $");
            out(sarg.getParameter().getName());
            out("=");
            sarg.visit(this);
            out(";");
        }
    }
    
    @Override
    public void visit(SequencedArgument that) {
        out("new ArraySequence([");
        boolean first=true;
        for (Expression arg: that.getExpressionList().getExpressions()) {
            if (!first) out(",");
            arg.visit(this);
            first = false;
        }
        out("])");
    }
    
    @Override
    public void visit(SpecifierStatement that) {
        BaseMemberExpression bme = (Tree.BaseMemberExpression) that.getBaseMemberExpression();
        if (qualifyBaseMember(that, bme.getDeclaration())) { 
            out(self(that.getScope(), bme.getDeclaration()));
            out(".");
        }
        out(bme.getDeclaration().getName());
        out("=");
        that.getSpecifierExpression().visit(this);
    }
    
    @Override
    public void visit(AssignOp that) {
        BaseMemberExpression bme = (Tree.BaseMemberExpression) that.getLeftTerm();
        if (qualifyBaseMember(that, bme.getDeclaration())) {
            out(self(that.getScope(), bme.getDeclaration()));
            out(".");
        }
        out(setter(bme.getDeclaration()));
        out("(");
        that.getRightTerm().visit(this);
        if (bme.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter) {}
        else {
            out(")");
        }
    }

    boolean qualifyBaseMember(Node that, Declaration d) {
        return d.isClassOrInterfaceMember() &&
                d.isShared() &&
                that.getScope().isInherited(d);
    }
    
    @Override
    public void visit(ExecutableStatement that) {
        super.visit(that);
        out(";");
    }
    
    @Override
    public void visit(Return that) {
        out("return ");
        super.visit(that);
    }
    
    @Override
    public void visit(AnnotationList that) {}
    
    private String self(Scope scope, Declaration d) {
        return "$this" + scope.getInheritingDeclaration(d).getName();
    }
    
    private String self(Declaration d) {
        return "$this" + d.getName();
    }
    
    private String outerSelf(Declaration d) {
        //TODO: this is broken, since the container
        //      might not be the class
        return "$this" + ((Declaration) d.getContainer()).getName();
    }
    
    private String setter(Declaration d) {
        return "set" + toUpperCase(d.getName().charAt(0)) + 
                d.getName().substring(1);
    }
    
    private String getter(Declaration d) {
        return "get" + toUpperCase(d.getName().charAt(0)) + 
                d.getName().substring(1);
    }
    
    @Override
    public void visit(SumOp that) {
        that.getLeftTerm().visit(this);
        out(".plus(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
}
