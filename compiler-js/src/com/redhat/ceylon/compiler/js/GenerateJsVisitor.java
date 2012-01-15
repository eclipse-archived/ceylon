package com.redhat.ceylon.compiler.js;

import static java.lang.Character.toUpperCase;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.*;

public class GenerateJsVisitor extends Visitor 
        implements NaturalVisitor {
    
    private final class SuperVisitor extends Visitor {
        private final List<Declaration> decs;

        private SuperVisitor(List<Declaration> decs) {
            this.decs = decs;
        }

        @Override 
        public void visit(QualifiedMemberOrTypeExpression qe) {
            if (qe.getPrimary() instanceof Super) {
                decs.add(qe.getDeclaration());
            }
            super.visit(qe);
        }

        public void visit(com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface qe) {
            //don't recurse
        }
    }
    
    private final class OuterVisitor extends Visitor {
        boolean found = false;
        private Declaration dec;
        
        private OuterVisitor(Declaration dec) {
            this.dec = dec;
        }

        @Override 
        public void visit(QualifiedMemberOrTypeExpression qe) {
            if (qe.getPrimary() instanceof Outer || 
                    qe.getPrimary() instanceof This) {
                if ( qe.getDeclaration().equals(dec) ) {
                    found = true;
                }
            }
            super.visit(qe);
        }
    }
    
    private final Writer out;
    private boolean prototypeStyle;
    private CompilationUnit root;
    
    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }
    
    public GenerateJsVisitor(Writer out, boolean prototypeStyle) {
        this.out = out;
        this.prototypeStyle=prototypeStyle;
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
        root = that;
        Module clm = that.getUnit().getPackage().getModule()
                .getLanguageModule();
        require(clm.getPackage(clm.getNameAsString()));
        super.visit(that);
    }
    
    @Override
    public void visit(Import that) {
        require(that.getImportList().getImportedPackage());
    }

    private void require(Package pkg) {
        out("var ");
        packageAlias(pkg);
        out("=require('");
        scriptPath(pkg);
        out("');");
        endLine();
    }
    
    private void packageAlias(Package pkg) {
    	out(packageAliasString(pkg));
    }
    
    private String packageAliasString(Package pkg) {
        StringBuilder sb = new StringBuilder("$$$");
        //out(pkg.getNameAsString().replace('.', '$'));
        for (String s: pkg.getName()) {
            sb.append(s.substring(0,1));
        }
        sb.append(pkg.getQualifiedNameString().length());
        return sb.toString();
    }

    private void scriptPath(Package pkg) {
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
        memberName(that.getDeclarationModel());
    }
    
    @Override
    public void visit(ParameterList that) {
        out("(");
        boolean first=true;
        for (Parameter param: that.getParameters()) {
            if (!first) out(",");
            memberName(param.getDeclarationModel());
            first = false;
        }
        out(")");
    }
    
    private void visitStatements(List<Statement> statements, boolean endLastLine) {
    	for (int i=0; i<statements.size(); i++) {
            Statement s = statements.get(i);
            s.visit(this);
            if ((endLastLine || (i<statements.size()-1)) && 
                    	s instanceof ExecutableStatement) {
                endLine();
            }
        }
    }
    
    @Override
    public void visit(Body that) {
        visitStatements(that.getStatements(), true);
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
            if (prototypeOwner!=null) {
                /*out("var ");
                self();
                out("=this;");
                endLine();*/
                out("var ");
                self(prototypeOwner);
                out("=this;");
                endLine();
            }
            visitStatements(stmnts, false);
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
        memberName(d);
        out("=");
    }

    private void share(Declaration d) {
        if (isCaptured(d)) {
            outerSelf(d);
            out(".");
            memberName(d);
            out("=");
            memberName(d);
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
        memberName(dec);
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
        out("new CeylonObject;");
    }
    
    private void function() {
        out("function ");
    }

    @Override
    public void visit(InterfaceDefinition that) {
        Interface d = that.getDeclarationModel();
        comment(that);
        defineType(d);
        copyInterfacePrototypes(that.getSatisfiedTypes(), d);
        for (Statement s: that.getInterfaceBody().getStatements()) {
            addToPrototype(d, s);
        }
        function();
        memberName(d);
        out("(");
        self(d);
        out(")");
        beginBlock();
        declareSelf(d);
        that.getInterfaceBody().visit(this);
        returnSelf(d);
        endBlock();
        share(d);
    }

    @Override
    public void visit(ClassDefinition that) {
        Class d = that.getDeclarationModel();
        comment(that);
        defineType(d);
        copySuperclassPrototype(that.getExtendedType(),d);
        copyInterfacePrototypes(that.getSatisfiedTypes(), d);
        for (Statement s: that.getClassBody().getStatements()) {
            addToPrototype(d, s);
        }
        function();
        memberName(d);
        out("(");
        for (Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        self(d);
        out(")");
        beginBlock();
        declareSelf(d);
        if (prototypeStyle) {
            for (Parameter p: that.getParameterList().getParameters()) {
                if (p.getDeclarationModel().isCaptured()) {
                    self(d);
                    out(".");
                    memberName(p.getDeclarationModel());
                    out("=");
                    memberName(p.getDeclarationModel());
                    out(";");
                    endLine();
                }
            }
        }
        callSuperclass(that.getExtendedType(), d);
        copySuperMembers(that, d);
        callInterfaces(that.getSatisfiedTypes(), d);
        that.getClassBody().visit(this);
        returnSelf(d);
        endBlock();
        share(d);
    }

    private void copySuperMembers(ClassDefinition that, Class d) {
        if (!prototypeStyle) {
            String parentName = "";
            ExtendedType extType = that.getExtendedType();
            if (extType != null) {
            	parentName = extType.getType().getDeclarationModel().getName();
            }
            
            final List<Declaration> decs = new ArrayList<Declaration>();
            new SuperVisitor(decs).visit(that.getClassBody());
            for (Declaration dec: decs) {
                if (dec instanceof Value) {
                    superGetterRef(dec,d,parentName);
                    if (((Value) dec).isVariable()) {
                        superSetterRef(dec,d,parentName);
                    }
                }
                else if (dec instanceof Getter) {
                    superGetterRef(dec,d,parentName);
                    if (((Getter) dec).isVariable()) {
                        superSetterRef(dec,d,parentName);
                    }
                }
                else {
                    superRef(dec,d,parentName);
                }
            }
        }
    }

    private void callSuperclass(ExtendedType extendedType, Class d) {
        if (extendedType!=null) {
            out(extendedType.getType().getDeclarationModel().getName());
            out("(");
            for (PositionalArgument arg: extendedType.getInvocationExpression()
                    .getPositionalArgumentList().getPositionalArguments()) {
                arg.visit(this);
                out(",");
            }
            self(d);
            out(")");
            out(";");
            endLine();
        }
    }
    
    private void callInterfaces(SatisfiedTypes satisfiedTypes, Class d) {
        if (satisfiedTypes!=null)
            for (SimpleType st: satisfiedTypes.getTypes()) {
                out(st.getDeclarationModel().getName());
                out("(");
                self(d);
                out(")");
                out(";");
                endLine();
            }
    }

    private void defineType(ClassOrInterface d) {
        if (prototypeStyle) {
            function();
            out("$");
            out(d.getName());
            out("(){}");
            endLine();
        }
    }
    
    private ClassOrInterface prototypeOwner;

    private void addToPrototype(ClassOrInterface d, Statement s) {
        prototypeOwner = d;
        if (s instanceof MethodDefinition) {
            addMethodToPrototype(d, (MethodDefinition)s);
        }
        if (s instanceof AttributeGetterDefinition) {
            addGetterToPrototype(d, (AttributeGetterDefinition)s);
        }
        if (s instanceof AttributeSetterDefinition) {
            addSetterToPrototype(d, (AttributeSetterDefinition)s);
        }
        if (s instanceof AttributeDeclaration) {
            addGetterAndSetterToPrototype(d, (AttributeDeclaration)s);
        }
        prototypeOwner = null;
    }

    private void declareSelf(ClassOrInterface d) {
        out("if ("); 
        self(d);
        out("===undefined)");
        self(d);
        out("=");
        if (prototypeStyle) {
            out("new $");
            out(d.getName());
            out(";");
        }
        else {
            newObject();
        }
        endLine();
        /*out("var ");
        self(d);
        out("=");
        self();
        out(";");
        endLine();*/
    }

    private void instantiateSelf(ClassOrInterface d) {
        out("var ");
        self(d);
        out("=");
        if (prototypeStyle) {
            out("new $");
            out(d.getName());
            out(";");
        }
        else {
            newObject();
        }
        endLine();
    }

    private void returnSelf(ClassOrInterface d) {
        out("return ");
        self(d);
        out(";");
    }

    private void copyMembersToPrototype(SimpleType that, Declaration d) {
        copyMembersToPrototype("$"+that.getDeclarationModel().getName(), d);
    }

    private void copyMembersToPrototype(String from, Declaration d) {
    	out("for(var $ in ");
    	out(from);
    	out(".prototype)");
    	beginBlock();
    	
    	out("var $m=");
    	out(from);
    	out(".prototype[$];");
    	endLine();
    	
    	out("$");
    	out(d.getName());
    	out(".prototype[$]=$m;");
        endLine();
        
        out("if($.charAt($.length-1)!=='$'){$");
    	out(d.getName());
    	out(".prototype[$+'");
    	if (!from.startsWith("$")) {
    		out("$");
    	}
    	out(from);
    	out("$']=$m}");
        endBlock();
    }

    private void copySuperclassPrototype(ExtendedType that, Declaration d) {
        if (prototypeStyle) {
            if (that==null) {
                copyMembersToPrototype("CeylonObject", d);
            }
            else {
                copyMembersToPrototype(that.getType(), d);
            }
        }
    }

    private void copyInterfacePrototypes(SatisfiedTypes that, Declaration d) {
        if (prototypeStyle && that!=null) {
            for (Tree.SimpleType st: that.getTypes()) {
                copyMembersToPrototype(st, d);
            }
        }
    }

    @Override
    public void visit(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        Class c = (Class) d.getTypeDeclaration();
        comment(that);
        defineType(c);
        copySuperclassPrototype(that.getExtendedType(),d);
        copyInterfacePrototypes(that.getSatisfiedTypes(),d);
        for (Statement s: that.getClassBody().getStatements()) {
            addToPrototype(c, s);
        }
        out("var $");
        out(d.getName());
        out("=");
        function();
        memberName(d);
        out("()");
        beginBlock();
        instantiateSelf(c);
        callSuperclass(that.getExtendedType(), c);
        callInterfaces(that.getSatisfiedTypes(), c);
        that.getClassBody().visit(this);
        returnSelf(c);       
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
    
    private void superRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".");
            memberName(d);
            out("$");
            out(parent);
            out("=");
            self(sub);
            out(".");
            memberName(d);
            out(";");
            endLine();
        //}
    }

    private void superGetterRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".");
            out(getter(d));
            out("$");
            out(parent);
            out("=");
            self(sub);
            out(".");
            out(getter(d));
            out(";");
            endLine();
        //}
    }

    private void superSetterRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".");
            out(setter(d));
            out("$");
            out(parent);
            out("=");
            self(sub);
            out(".");
            out(setter(d));
            out(";");
            endLine();
        //}
    }

    @Override
    public void visit(MethodDeclaration that) {}
    
    @Override
    public void visit(MethodDefinition that) {
        Method d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        function();
        memberName(d);
        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        super.visit(that);
        share(d);
    }
    
    private void addMethodToPrototype(Declaration outer, 
            MethodDefinition that) {
        Method d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$");
        out(outer.getName());
        out(".prototype.");
        memberName(d);
        out("=");
        function();
        memberName(d);
        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        super.visit(that);
    }
    
    @Override
    public void visit(AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        function();
        out(getter(d));
        out("()");
        super.visit(that);
        shareGetter(d);
    }

    private void addGetterToPrototype(Declaration outer, 
            AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$");
        out(outer.getName());
        out(".prototype.");
        out(getter(d));
        out("=");
        function();
        out(getter(d));
        out("()");
        super.visit(that);
    }

    private void shareGetter(MethodOrValue d) {
        if (isCaptured(d)) {
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
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        function();
        out(setter(d));
        out("(");
        memberName(d);
        out(")");
        super.visit(that);
        shareSetter(d);
    }

    private void addSetterToPrototype(Declaration outer, 
            AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (!prototypeStyle || !d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$");
        out(outer.getName());
        out(".prototype.");
        out(setter(d));
        out("=");
        function();
        out(setter(d));
        out("(");
        memberName(d);
        out(")");
        super.visit(that);
    }
    
    private boolean isCaptured(Declaration d) {
        if (d.isToplevel()||d.isClassOrInterfaceMember()) { //TODO: what about things nested inside control structures
            if (d.isShared() || d.isCaptured() ) {
                return true;
            }
            else {
                OuterVisitor ov = new OuterVisitor(d);
                ov.visit(root);
                return ov.found;
            }
        }
        else {
            return false;
        }
    }

    private void shareSetter(MethodOrValue d) {
        if (isCaptured(d)) {
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
        if (!d.isFormal()) {
            comment(that);
            if (prototypeStyle&&d.isClassOrInterfaceMember()) {
                if (that.getSpecifierOrInitializerExpression()!=null) {
                    outerSelf(d);
                    out(".");
                    memberName(d);
                    out("=");
                    super.visit(that);
                    out(";");
                    endLine();
                }
            }
            else {
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
    }
    
    private void addGetterAndSetterToPrototype(Declaration outer,
            AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        if (!prototypeStyle||d.isToplevel()) return;
        if (!d.isFormal()) {
            comment(that);
            out("$");
            out(outer.getName());
            out(".prototype.");
            out(getter(d));
            out("=");
            function();
            out(getter(d));
            out("()");
            beginBlock();
            out("return this.");
            memberName(d);
            out(";");
            endBlock();
            if (d.isVariable()) {
                out("$");
                out(outer.getName());
                out(".prototype.");
                out(setter(d));
                out("=");
                function();
                out(setter(d));
                out("(");
                out(d.getName());
                out(")");
                beginBlock();
                out("this.");
                memberName(d);
                out("=");
                out(d.getName());
                out(";");
                endBlock();
            }
        }
    }
    
    private void clAlias() {
        out("$$$cl15");
    }
    
    @Override
    public void visit(CharLiteral that) {
        clAlias();
        out(".Character(");
        //out(that.getText().replace('`', '"'));
        //TODO: what about escape sequences?
        out(String.valueOf(that.getText().codePointAt(1)));
        out(")");
    }
    
    @Override
    public void visit(StringLiteral that) {
        clAlias();
        out(".String(");
        String text = that.getText();
        out(text);
        // pre-calculate string length
        // TODO: also for strings that contain escape sequences
        if (text.indexOf('\\') < 0) {
        	out(",");
        	out(String.valueOf(text.codePointCount(0, text.length()) - 2));
        }
        out(")");
    }

    @Override
    public void visit(StringTemplate that) {
    	List<StringLiteral> literals = that.getStringLiterals();
    	List<Expression> exprs = that.getExpressions();
    	clAlias();
    	out(".StringBuilder().appendAll(");
    	clAlias();
    	out(".ArraySequence([");
    	for (int i = 0; i < literals.size(); i++) {
    		literals.get(i).visit(this);
    		if (i < exprs.size()) {
    			out(",");
    			exprs.get(i).visit(this);
    			out(".getString()");
    			out(",");
    		}
    	}
    	out("])).getString()");
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
        self(Util.getContainingClassOrInterface(that.getScope()));
    }
    
    @Override
    public void visit(Super that) {
        self(Util.getContainingClassOrInterface(that.getScope()));
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
            memberName(that.getDeclaration());
        }
        else {
            out(getter(that.getDeclaration()));
            out("()");
        }
    }
    
    @Override
    public void visit(QualifiedMemberExpression that) {
        //Big TODO: make sure the member is actually
        //          refined by the current class!
    	if (that.getMemberOperator() instanceof SafeMemberOp) {
    		out("function($){return $!==null?$.");
	        qualifiedMemberRHS(that);
	        out(":null}(");
	        super.visit(that);
	        out(")");
    	} else {
	        super.visit(that);
	        out(".");
	        qualifiedMemberRHS(that);
    	}
    }
    
    private void qualifiedMemberRHS(QualifiedMemberExpression that) {
    	boolean sup = that.getPrimary() instanceof Super;
    	String postfix = "";
    	if (sup) {
    		 ClassOrInterface type = Util.getContainingClassOrInterface(that.getScope());
    		 ClassOrInterface parentType = type.getExtendedTypeDeclaration();
    		 if (parentType != null) {
    			 postfix = '$' + parentType.getName();
    			 if (prototypeStyle) {
    				 postfix += '$';
    			 }
    		 }
    	}
        if (that.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter ||
                that.getDeclaration() instanceof Method) {
            memberName(that.getDeclaration());
            out(postfix);
        }
        else {
            out(getter(that.getDeclaration()));
            out(postfix);
            out("()");
        }
    }
    
    @Override
    public void visit(BaseTypeExpression that) {
        qualify(that, that.getDeclaration());
        memberName(that.getDeclaration());
    }
    
    @Override
    public void visit(QualifiedTypeExpression that) {
        super.visit(that);
        out(".");
        memberName(that.getDeclaration());
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
    public void visit(SequenceEnumeration that) {
        clAlias();
        out(".ArraySequence([");
        boolean first=true;
        if (that.getExpressionList() != null) {
            for (Expression arg: that.getExpressionList().getExpressions()) {
                if (!first) out(",");
                arg.visit(this);
                first = false;
            }
        }
        out("])");
    }
    
    @Override
    public void visit(SpecifierStatement that) {
        BaseMemberExpression bme = (Tree.BaseMemberExpression) that.getBaseMemberExpression();
        qualify(that, bme.getDeclaration());
        memberName(bme.getDeclaration());
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

    private void qualify(Node that, Declaration d) {
    	String path = qualifiedPath(that, d);
    	out(path);
    	if (path.length() > 0) {
    		out(".");
    	}
    }
    
    private String qualifiedPath(Node that, Declaration d) {
        if (isImported(that, d)) {
            return packageAliasString(d.getUnit().getPackage());
        }
        else if (prototypeStyle) {
            if (d.isClassOrInterfaceMember() && 
                    !(d instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter &&
                            !d.isCaptured())) {
                TypeDeclaration id = that.getScope().getInheritingDeclaration(d);
                /*if (d.getContainer().equals(prototypeOwner)) {
                    out("this");
                }
                else*/
                if (id==null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    if (!(d instanceof TypeDeclaration)) {
                        return selfString((TypeDeclaration)d.getContainer());
                    }
                    else {
                        //a local type declaration: for now, 
                        //we don't flatten out nested types
                        //onto the prototype
                        return "";
                    }
                }
                else {
                    //an inherited declaration that might be
                    //inherited by an outer scope
                    return selfString(id);
                }
            }
        }
        else {
            if (d.isShared() && d.isClassOrInterfaceMember()) {
                TypeDeclaration id = that.getScope().getInheritingDeclaration(d);
                if (id==null) {
                    //a shared local declaration
                    return selfString((TypeDeclaration)d.getContainer());
                }
                else {
                    //an inherited declaration that might be
                    //inherited by an outer scope
                    return selfString(id);
                }
            }
        }
        return "";
    }

    private boolean isImported(Node that, Declaration d) {
        return !d.getUnit().getPackage()
                .equals(that.getUnit().getPackage());
    }

    @Override
    public void visit(ExecutableStatement that) {
        super.visit(that);
        out(";");
    }
    
    @Override
    public void visit(Expression that) {
        if (prototypeStyle && 
                that.getTerm() instanceof QualifiedMemberOrTypeExpression) {
            QualifiedMemberOrTypeExpression term = (QualifiedMemberOrTypeExpression) that.getTerm();
            if (term.getDeclaration() instanceof Functional) {
                out("function(){var $=");
                term.getPrimary().visit(this);
                out(";$.");
                memberName(term.getDeclaration());
                out(".apply($,arguments)}");
                return;
            }
        }
        super.visit(that);
    }

    @Override
    public void visit(Return that) {
        out("return ");
        super.visit(that);
    }
    
    @Override
    public void visit(AnnotationList that) {}
    
    private void self(TypeDeclaration d) {
    	out(selfString(d));
    }
    
    private String selfString(TypeDeclaration d) {
        return "$$" + d.getName().substring(0,1).toLowerCase()
        		+ d.getName().substring(1);
    }
    
    /*private void self() {
        out("$$");
    }*/
    
    private void outerSelf(Declaration d) {
        if (d.isToplevel()) {
            out("this");
        }
        else if (d.isClassOrInterfaceMember()) {
            self((TypeDeclaration)d.getContainer());
        }
    }
    
    private String setter(Declaration d) {
    	String name = memberNameString(d, true);
        return "set" + toUpperCase(name.charAt(0)) + name.substring(1);
    }
    
    private String getter(Declaration d) {
    	String name = memberNameString(d, true);
        return "get" + toUpperCase(name.charAt(0)) + name.substring(1);
    }
    
    private void memberName(Declaration d) {
    	out(memberNameString(d, false));
    }
    
    private String memberNameString(Declaration d, Boolean forGetterSetter) {
    	String name = d.getName();
    	Scope container = d.getContainer();
    	if (prototypeStyle && !d.isShared() && d.isMember() && (container != null)
    				&& (container instanceof ClassOrInterface)) {
    		ClassOrInterface parentType = (ClassOrInterface) container;
    		name += '$' + parentType.getName();
    		if (prototypeStyle && (forGetterSetter || (d instanceof Method))) {
    			name += '$';
    		}
    		
    	}
    	return name;
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
    
    @Override public void visit(RemainderOp that) {
    	that.getLeftTerm().visit(this);
    	out(".remainder(");
    	that.getRightTerm().visit(this);
    	out(")");
    }
    
    @Override public void visit(PowerOp that) {
    	that.getLeftTerm().visit(this);
    	out(".power(");
    	that.getRightTerm().visit(this);
    	out(")");
    }
    
    @Override public void visit(AddAssignOp that) {
    	arithmeticAssignOp(that, "plus");
    }
    
    @Override public void visit(SubtractAssignOp that) {
    	arithmeticAssignOp(that, "minus");
    }
    
    @Override public void visit(MultiplyAssignOp that) {
    	arithmeticAssignOp(that, "times");
    }
    
    @Override public void visit(DivideAssignOp that) {
    	arithmeticAssignOp(that, "divided");
    }
    
    @Override public void visit(RemainderAssignOp that) {
    	arithmeticAssignOp(that, "remainder");
    }
    
    private void arithmeticAssignOp(ArithmeticAssignmentOp that,
    		                        String functionName) {
    	Term lhs = that.getLeftTerm();
    	if (lhs instanceof BaseMemberExpression) {
    		BaseMemberExpression lhsBME = (BaseMemberExpression) lhs;
    		String lhsPath = qualifiedPath(lhsBME, lhsBME.getDeclaration());
    		if (lhsPath.length() > 0) {
    			lhsPath += '.';
    		}
    		
    		out("(");
    		out(lhsPath);
    		out(setter(lhsBME.getDeclaration()));
    		out("(");
    		out(lhsPath);
    		out(getter(lhsBME.getDeclaration()));
    		out("()." + functionName + "(");
    		that.getRightTerm().visit(this);
    		out(")),");
    		out(lhsPath);
    		out(getter(lhsBME.getDeclaration()));
    		out("())");
    		
    	} else if (lhs instanceof QualifiedMemberExpression) {
    		QualifiedMemberExpression lhsQME = (QualifiedMemberExpression) lhs;
    		out("function($1,$2){var $=$1.");
    		out(getter(lhsQME.getDeclaration()));
    		out("()." + functionName + "($2);$1.");
    		out(setter(lhsQME.getDeclaration()));
    		out("($);return $}(");
    		lhsQME.getPrimary().visit(this);
    		out(",");
    		that.getRightTerm().visit(this);
    		out(")");
    	}
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
    	leftEqualsRight(that);
    }
    
    @Override public void visit(NotEqualOp that) {
    	leftEqualsRight(that);
        equalsFalse();
    }
    
    @Override public void visit(NotOp that) {
        that.getTerm().visit(this);
        equalsFalse();
    }
    
    @Override public void visit(IdenticalOp that) {
        out("(");
        that.getLeftTerm().visit(this);
        out("===");
        that.getRightTerm().visit(this);
        thenTrueElseFalse();
        out(")");
    }
    
    @Override public void visit(CompareOp that) {
    	leftCompareRight(that);
    }
    
    @Override public void visit(SmallerOp that) {
    	leftCompareRight(that);
    	out(".equals(");
    	clAlias();
    	out(".getSmaller())");
    }
    
    @Override public void visit(LargerOp that) {
    	leftCompareRight(that);
    	out(".equals(");
    	clAlias();
    	out(".getLarger())");
    }
    
    @Override public void visit(SmallAsOp that) {
    	out("(");
    	leftCompareRight(that);
    	out("!==");
    	clAlias();
    	out(".getLarger()");
    	thenTrueElseFalse();
    	out(")");
    }
    
    @Override public void visit(LargeAsOp that) {
    	out("(");
    	leftCompareRight(that);
    	out("!==");
    	clAlias();
    	out(".getSmaller()");
    	thenTrueElseFalse();
    	out(")");
    }
    
    private void leftEqualsRight(BinaryOperatorExpression that) {
    	that.getLeftTerm().visit(this);
        out(".equals(");
        that.getRightTerm().visit(this);
        out(")");
    }
    
    private void clTrue() {
        clAlias();
        out(".getTrue()");
    }
    
    private void clFalse() {
        clAlias();
        out(".getFalse()");
    }
    
    private void equalsFalse() {
        out(".equals(");
        clFalse();
        out(")");    
    }
    
    private void thenTrueElseFalse() {
    	out("?");
    	clTrue();
        out(":");
        clFalse();
    }
    
   private void leftCompareRight(BinaryOperatorExpression that) {
    	that.getLeftTerm().visit(this);
    	out(".compare(");
    	that.getRightTerm().visit(this);
    	out(")");    	
    }
   
   @Override public void visit(AndOp that) {
	   out("(");
	   that.getLeftTerm().visit(this);
	   out("===");
	   clTrue();
	   out("?");
	   that.getRightTerm().visit(this);
	   out(":");
	   clFalse();
	   out(")");
   }
   
   @Override public void visit(OrOp that) {
	   out("(");
	   that.getLeftTerm().visit(this);
	   out("===");
	   clTrue();
	   out("?");
	   clTrue();
	   out(":");
	   that.getRightTerm().visit(this);
	   out(")");
   }
   
   @Override public void visit(EntryOp that) {
       clAlias();
       out(".Entry(");
       that.getLeftTerm().visit(this);
       out(",");
       that.getRightTerm().visit(this);
       out(")");
   }
   
   @Override public void visit(Element that) {
	   out(".item(");
	   that.getExpression().visit(this);
	   out(")");
   }
   
   @Override public void visit(DefaultOp that) {
	   out("function($){return $!==null?$:");
	   that.getRightTerm().visit(this);
	   out("}(");
	   that.getLeftTerm().visit(this);
	   out(")");
   }
   
   @Override public void visit(ThenOp that) {
       out("(");
       that.getLeftTerm().visit(this);
       out("===");
       clTrue();
       out("?");
       that.getRightTerm().visit(this);
       out(":null)");
   }
   
   @Override public void visit(IncrementOp that) {
	   prefixIncrementOrDecrement(that.getTerm(), "getSuccessor");
   }
   
   @Override public void visit(DecrementOp that) {
	   prefixIncrementOrDecrement(that.getTerm(), "getPredecessor");
   }
   
   private void prefixIncrementOrDecrement(Term term, String functionName) {
	   if (term instanceof BaseMemberExpression) {
		   BaseMemberExpression bme = (BaseMemberExpression) term;
		   String path = qualifiedPath(bme, bme.getDeclaration());
		   if (path.length() > 0) {
			   path += '.';
		   }
		   
		   out("(");
		   out(path);
		   out(setter(bme.getDeclaration()));
		   out("(");
		   out(path);
		   String bmeGetter = getter(bme.getDeclaration());
		   out(bmeGetter);
		   out("()." + functionName + "()),");
		   out(path);
		   out(bmeGetter);
		   out("())");
	   } else if (term instanceof QualifiedMemberExpression) {
		   QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
		   out("function($){var $2=$.");
		   out(getter(qme.getDeclaration()));
		   out("()." + functionName + "();$.");
		   out(setter(qme.getDeclaration()));
		   out("($2);return $2}(");
		   qme.getPrimary().visit(this);
		   out(")");
	   }
   }
   
   @Override public void visit(PostfixIncrementOp that) {
	   postfixIncrementOrDecrement(that.getTerm(), "getSuccessor");
   }
   
   @Override public void visit(PostfixDecrementOp that) {
	   postfixIncrementOrDecrement(that.getTerm(), "getPredecessor");
   }
   
   private void postfixIncrementOrDecrement(Term term, String functionName) {
	   if (term instanceof BaseMemberExpression) {
		   BaseMemberExpression bme = (BaseMemberExpression) term;
		   String path = qualifiedPath(bme, bme.getDeclaration());
		   if (path.length() > 0) {
			   path += '.';
		   }
		   
		   out("function($){");
		   out(path);
		   out(setter(bme.getDeclaration()));
		   out("($." + functionName + "());return $}(");
		   out(path);
		   out(getter(bme.getDeclaration()));
		   out("())");
	   } else if (term instanceof QualifiedMemberExpression) {
		   QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
		   out("function($){var $2=$.");
		   out(getter(qme.getDeclaration()));
		   out("();$.");
		   out(setter(qme.getDeclaration()));
		   out("($2." + functionName + "());return $2}(");
		   qme.getPrimary().visit(this);			   
		   out(")");
	   }
   }

   @Override public void visit(Exists that) {
       clAlias();
       out(".exists(");
       that.getTerm().visit(this);
       out(")");
   }
   @Override public void visit(Nonempty that) {
       clAlias();
       out(".nonempty(");
       that.getTerm().visit(this);
       out(")");
   }

   @Override public void visit(IfStatement that) {
	   
	   IfClause ifClause = that.getIfClause();
	   Block ifBlock = ifClause.getBlock();
	   Condition condition = ifClause.getCondition();
	   if (condition instanceof ExistsCondition) {
		   // if (exists ...)		   
		   existsConditionAndBlock((ExistsCondition) condition, ifBlock, "if");
	   } else {
	       out("if ((");
		   condition.visit(this);
	       out(")===");
	       clAlias();
	       out(".getTrue())");
	       if (ifBlock != null) {
	    	   ifBlock.visit(this);
	       }
	   }
	   
       if (that.getElseClause() != null) {
           out("else ");
           that.getElseClause().visit(this);
       }
   }

   @Override public void visit(WhileStatement that) {
	   WhileClause whileClause = that.getWhileClause();
	   Condition condition = whileClause.getCondition();
	   if (condition instanceof ExistsCondition) {
		   // while (exists...)
		   existsConditionAndBlock((ExistsCondition) condition,
				                   whileClause.getBlock(), "while");
	   } else {
		   out("while ((");
	       condition.visit(this);
	       out(")===");
	       clAlias();
	       out(".getTrue())");
	       whileClause.getBlock().visit(this);
	   }
   }
   
   private void existsConditionAndBlock(ExistsCondition condition, Block block,
		                                String keyword) {
	   Variable existsVar = condition.getVariable();
	   String existsVarName = existsVar.getDeclarationModel().getName();
	   
	   boolean simpleCheck = false;
	   Term existsVarRHS = existsVar.getSpecifierExpression().getExpression().getTerm();
	   if (existsVarRHS instanceof BaseMemberExpression) {
		   BaseMemberExpression bme = (BaseMemberExpression) existsVarRHS;
		   if (bme.getDeclaration().getName().equals(existsVarName)) {
			   // the simple case: if/while (exists x)
			   simpleCheck = true;
		   }
	   }
	   
	   if (simpleCheck) {
		   out(keyword);
           out("(");
           existsVarRHS.visit(this);
           out("!==null)");
           block.visit(this);
		   
	   } else {
		   // if/while (exists x=...)
		   
		   out("var $ex$;");
		   endLine();
		   
		   out(keyword);
		   out("(($ex$=");
		   existsVarRHS.visit(this);
		   out(")!==null)");
		   if (block.getStatements().isEmpty()) {
			   out("{}");
			   
		   } else {
			   beginBlock();
			   out("var $");
			   out(existsVarName);
			   out("=$ex$;");
			   endLine();
			   
			   function();
			   out(getter(existsVar.getDeclarationModel()));
			   out("(){");
			   out("return $");
			   out(existsVarName);
			   out("}");
			   endLine();
			   
			   visitStatements(block.getStatements(), false);
			   endBlock();
		   }
	   }
   }

   @Override public void visit(Break that) {
       out("break;");
   }
   @Override public void visit(Continue that) {
       out("continue;");
   }

   @Override public void visit(RangeOp that) {
	   clAlias();
	   out(".Range(");
	   that.getLeftTerm().visit(this);
	   out(",");
	   that.getRightTerm().visit(this);
	   out(")");
   }

   @Override public void visit(ForStatement that) {
	   ForIterator foriter = that.getForClause().getForIterator();
	   SpecifierExpression iterable = foriter.getSpecifierExpression();
	   boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
	   //First we need to enclose this inside an anonymous function,
	   //to avoid problems with repeated iterator variables
	   out("(function(){"); indentLevel++;
	   endLine();
	   out("var _iter = ");
	   iterable.visit(this);
	   out(".getIterator();");
	   endLine();
	   out("var _item;");
	   endLine();
	   out("while ((_item = _iter.next()).equals(");
	   clAlias();
	   out(".getFinished()) !== ");
	   clAlias();
	   out(".getTrue())");
	   List<Statement> stmnts = that.getForClause().getBlock().getStatements();
	   if (stmnts.isEmpty()) {
		   out("{}");
		   endLine();
	   } else {
		   beginBlock();
		   if (foriter instanceof ValueIterator) {
			   Value model = ((ValueIterator)foriter).getVariable().getDeclarationModel();
			   function();
			   out(getter(model));
			   out("(){ return _item; }");
		   } else if (foriter instanceof KeyValueIterator) {
			   Value keyModel = ((KeyValueIterator)foriter).getKeyVariable().getDeclarationModel();
			   Value valModel = ((KeyValueIterator)foriter).getValueVariable().getDeclarationModel();
			   function();
			   out(getter(keyModel));
			   out("(){ return _item.getKey(); }");
			   endLine();
			   function();
			   out(getter(valModel));
			   out("(){ return _item.getItem(); }");
		   }
		   endLine();
		   for (int i=0; i<stmnts.size(); i++) {
			   Statement s = stmnts.get(i);
			   s.visit(this);
			   if (i<stmnts.size()-1 && s instanceof ExecutableStatement) {
				   endLine();
			   }
		   }
	   }
	   //If there's an else block, check for normal termination
	   indentLevel--;
	   endLine();
	   out("}");
	   if (hasElse) {
		   endLine();
		   out("if (_item.equals(");
		   clAlias();
		   out(".getFinished()) === ");
		   clAlias();
		   out(".getTrue())");
		   that.getElseClause().getBlock().visit(this);
	   }
	   indentLevel--;
	   endLine();
	   out("}());");
	   endLine();
   }

}
