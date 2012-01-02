package com.redhat.ceylon.compiler.js;

import static java.lang.Character.toUpperCase;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.DifferenceOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.EqualOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExecutableStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExtendedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FloatLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IdenticalOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Import;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InterfaceDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NaturalLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NegativeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotEqualOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NotOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ObjectDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Outer;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Parameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositiveOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ProductOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotientOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Return;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiedTypes;
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
    
    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }
    
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
        Module clm = that.getUnit().getPackage().getModule()
                .getLanguageModule();
        require(clm.getPackage(clm.getNameAsString()));
        super.visit(that);
        /*try {
            out.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }*/
    }
    
    @Override
    public void visit(Import that) {
        require(that.getImportList().getImportedPackage());
    }

    void require(Package pkg) {
        out("var ");
        packageAlias(pkg);
        out("=require('");
        scriptPath(pkg);
        out("');");
        endLine();
    }
    
    void packageAlias(Package pkg) {
        out("$$");
        //out(pkg.getNameAsString().replace('.', '$'));
        for (String s: pkg.getName()) {
            out(s.substring(0,1));
        }
        out(Integer.toString(pkg.getQualifiedNameString().length()));
    }

    public void scriptPath(Package pkg) {
        out(pkg.getModule().getNameAsString().replace('.', '/'));
        out("/");
        if (!pkg.getModule().isDefault()) {
            out(pkg.getModule().getVersion());
            out("/");
        }
        out(pkg.getNameAsString());
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
    
    private void comment(com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration that) {
        endLine();
        out("//");
        out(that.getNodeType());
        out(" ");
        out(that.getDeclarationModel().getName());
        location(that);
        endLine();
    }
    
    private void var(Declaration d) {
        out("var ");
        out(d.getName());
        out("=");
    }

    private void selfVar(Declaration d) {
        out("var ");
        self(d);
        out("=");
    }

    private void share(Declaration d) {
        if (d.isShared()) {
            outerSelf(d);
            out(".");
            out(d.getName());
            out("=");
            out(d.getName());
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(ClassDeclaration that) {
        Class d = that.getDeclarationModel();
        comment(that);
        var(d);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel()
                .getDeclaration();
        qualify(that,dec);
        out(dec.getName());
        out(";");
        endLine();
        share(d);
    }

    @Override
    public void visit(InterfaceDeclaration that) {
        Interface d = that.getDeclarationModel();
        comment(that);
        var(d);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel()
                .getDeclaration();
        qualify(that,dec);
        out(";");
        share(d);
    }
    
    private void newObject() {
        out("new CeylonObject();");
    }
    
    private void function() {
        out("function ");
    }

    @Override
    public void visit(InterfaceDefinition that) {
        Interface d = that.getDeclarationModel();
        comment(that);
        function();
        out(d.getName());
        out("()");
        beginBlock();
        declareSelf(d);
        interfaces(that.getSatisfiedTypes(), d);
        that.getInterfaceBody().visit(this);
        returnSelf(d);
        endBlock();
        share(d);
    }

    @Override
    public void visit(ClassDefinition that) {
        Class d = that.getDeclarationModel();
        comment(that);
        function();
        out(d.getName());
        that.getParameterList().visit(this);
        beginBlock();
        declareSelf(d);
        superclass(that.getExtendedType(), d);
        interfaces(that.getSatisfiedTypes(), d);
        that.getClassBody().visit(this);
        returnSelf(d);
        endBlock();
        share(d);
    }

    private void declareSelf(Declaration d) {
        selfVar(d);
        newObject();
        endLine();
    }

    private void returnSelf(Declaration d) {
        out("return ");
        self(d);
        out(";");
    }

    private void superclass(ExtendedType that, Declaration d) {
        if (that!=null) {
            out("var $super=");
            out(that.getType()
                    .getDeclarationModel().getName());
            that.getInvocationExpression().visit(this);
            out(";");
            endLine();
            out("for(var $m in $super){");
            self(d);
            out("[$m]=$super[$m]}");
            endLine();
        }
    }

    private void interfaces(SatisfiedTypes that, Declaration d) {
        if (that!=null)
        for (Tree.SimpleType st: that.getTypes()) {
            String name = st.getDeclarationModel().getName();
            out("var $super");
            out(name);
            out("=");
            out(name);
            out("();");
            endLine();
            out("for(var $m in $super");
            out(name);
            out("){");
            self(d);
            out("[$m]=$super");
            out(name);
            out("[$m]}");
            endLine();
        }
    }
    
    @Override
    public void visit(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        comment(that);
        out("var $");
        out(d.getName());
        out("=");
        function();
        out(d.getName());
        out("()");
        beginBlock();
        declareSelf(d);
        superclass(that.getExtendedType(), d);
        interfaces(that.getSatisfiedTypes(), d);
        that.getClassBody().visit(this);
        returnSelf(d);       
        indentLevel--;
        endLine();
        out("}();");
        endLine();
        if (d.isShared()) {
            outerSelf(d);
            out(".");
            out(getter(d));
            out("=");
        }
        function();
        out(getter(d));
        out("()");
        beginBlock();
        out("return $");
        out(d.getName());
        out(";");
        endBlock();
    }
    
    @Override
    public void visit(MethodDeclaration that) {}
    
    @Override
    public void visit(MethodDefinition that) {
        Method d = that.getDeclarationModel();
        comment(that);
        function();
        out(d.getName());
        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        super.visit(that);
        share(d);
    }
    
    @Override
    public void visit(AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        comment(that);
        function();
        out(getter(d));
        out("()");
        super.visit(that);
        shareGetter(d);
    }

    private void shareGetter(MethodOrValue d) {
        if (d.isShared()) {
            outerSelf(d);
            out(".");
            out(getter(d));
            out("=");
            out(getter(d));
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        comment(that);
        function();
        out(setter(d));
        out("(");
        out(d.getName());
        out(")");
        super.visit(that);
        shareSetter(d);
    }

    private void shareSetter(MethodOrValue d) {
        if (d.isShared()) {
            outerSelf(d);
            out(".");
            out(setter(d));
            out("=");
            out(setter(d));
            out(";");
            endLine();
        }
    }
    
    @Override
    public void visit(AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        comment(that);
        if (!d.isFormal()) {
            out("var $");
            out(d.getName());
            if (that.getSpecifierOrInitializerExpression()!=null) {
                out("=");
            }
            super.visit(that);
            out(";");
            endLine();
            function();
            out(getter(d));
            out("()");
            beginBlock();
            out("return $");
            out(d.getName());
            out(";");
            endBlock();
            shareGetter(d);
            if (d.isVariable()) {
                function();
                out(setter(d));
                out("(");
                out(d.getName());
                out(")");
                beginBlock();
                out("$");
                out(d.getName());
                out("=");
                out(d.getName());
                out(";");
                endBlock();
                shareSetter(d);
            }
        }
    }
    
    private void clAlias() {
        out("$$cl15");
    }
    
    @Override
    public void visit(CharLiteral that) {
        clAlias();
        out(".Character(");
        out(that.getText().replace('`', '"'));
        out(")");
    }
    
    @Override
    public void visit(StringLiteral that) {
        clAlias();
        out(".String(");
        out(that.getText());
        out(")");
    }
    
    @Override
    public void visit(FloatLiteral that) {
        clAlias();
        out(".Float(");
        out(that.getText());
        out(")");
    }
    
    @Override
    public void visit(NaturalLiteral that) {
        clAlias();
        out(".Integer(");
        out(that.getText());
        out(")");
    }
    
    @Override
    public void visit(This that) {
        self(that.getTypeModel().getDeclaration());
    }
    
    @Override
    public void visit(Super that) {
        out("$super");
    }
    
    @Override
    public void visit(Outer that) {
        self(that.getTypeModel().getDeclaration());
    }
    
    @Override
    public void visit(BaseMemberExpression that) {
        qualify(that, that.getDeclaration());
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
        qualify(that, that.getDeclaration());
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
                clAlias();
                out(".ArraySequence([");
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
        clAlias();
        out(".ArraySequence([");
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
        qualify(that, bme.getDeclaration());
        out(bme.getDeclaration().getName());
        out("=");
        that.getSpecifierExpression().visit(this);
    }
    
    @Override
    public void visit(AssignOp that) {
        BaseMemberExpression bme = (Tree.BaseMemberExpression) that.getLeftTerm();
        qualify(that, bme.getDeclaration());
        out(setter(bme.getDeclaration()));
        out("(");
        that.getRightTerm().visit(this);
        if (bme.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter) {}
        else {
            out(")");
        }
    }

    void qualify(Node that, Declaration d) {
        if (isImported(that, d)) {
            packageAlias(d.getUnit().getPackage());
            out(".");
        }
        else if (qualifyBaseMember(that, d)) {
            out("$this");
            out(that.getScope()
                    .getInheritingDeclaration(d).getName());
            out(".");
        }
    }

    boolean isImported(Node that, Declaration d) {
        return !d.getUnit().getPackage()
                .equals(that.getUnit().getPackage());
    }

    boolean qualifyBaseMember(Node that, Declaration d) {
        return !d.isDefinedInScope(that.getScope());
        /*return d.isClassOrInterfaceMember() &&
                d.isShared() &&
                that.getScope().isInherited(d);*/
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
    
    private void self(Declaration d) {
        out("$this"); 
        out(d.getName());
    }
    
    private void outerSelf(Declaration d) {
        //TODO: this is broken, since the container
        //      might not be the class
        if (d.isToplevel()) {
            out("this");
        }
        else {
            out("$this");
            out(((Declaration) d.getContainer()).getName());
        }
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
    
    @Override
    public void visit(DifferenceOp that) {
        that.getLeftTerm().visit(this);
        out(".minus(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
    @Override
    public void visit(ProductOp that) {
        that.getLeftTerm().visit(this);
        out(".times(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
    @Override
    public void visit(QuotientOp that) {
        that.getLeftTerm().visit(this);
        out(".divided(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
    @Override public void visit(NegativeOp that) {
        that.getTerm().visit(this);
        out(".negativeValue()");
    }
    
    @Override public void visit(PositiveOp that) {
        that.getTerm().visit(this);
        out(".positiveValue()");
    }
    
    @Override public void visit(EqualOp that) {
        that.getLeftTerm().visit(this);
        out(".equals(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
    @Override public void visit(NotEqualOp that) {
        that.getLeftTerm().visit(this);
        out(".equals(");
        that.getRightTerm().visit(this);
        out(").equals(");
        clAlias();
        out(".getFalse())");
    }
    
    @Override public void visit(NotOp that) {
        that.getTerm().visit(this);
        out(".equals(");
        clAlias();
        out(".getFalse())");
    }
    
    @Override public void visit(IdenticalOp that) {
        out("(");
        that.getLeftTerm().visit(this);
        out("===");
        that.getRightTerm().visit(this);
        out("?");
        clAlias();
        out(".getTrue():");
        clAlias();
        out(".getFalse())");
    }
    
}
