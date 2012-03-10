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
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.*;

public class GenerateJsVisitor extends Visitor
        implements NaturalVisitor {

    private boolean sequencedParameter=false;
    private boolean indent=true;
    private boolean comment=true;
    private int tmpvarCount = 0;

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

        public void visit(Tree.ClassOrInterface qe) {
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

    /** Tells the receiver whether to add comments to certain declarations. Default is true. */
    public void setAddComments(boolean flag) { comment = flag; }
    /** Tells the receiver whether to indent the generated code. Default is true. */
    public void setIndent(boolean flag) { indent = flag; }

    /** Print generated code to the Writer specified at creation time.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    private void out(String code, String... codez) {
        try {
            out.write(code);
            for (String s : codez) {
                out.write(s);
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    int indentLevel = 0;

    /** Print out 4 spaces per indentation level. */
    private void indent() {
        if (!indent) return;
        for (int i=0;i<indentLevel;i++) {
            out("    ");
        }
    }

    /** Prints a newline and the necessary spaces to reach current indentation level. */
    private void endLine() {
        out("\n");
        indent();
    }

    /** Increases indentation level, prints opening brace, newline and necessary spaces to reach new indentation level. */
    private void beginBlock() {
        indentLevel++;
        out("{");
        endLine();
    }

    /** Decreases indentation level, prints closing brace in new line and necessary spaces to reach new indentation level. */
    private void endBlock() {
        endBlock(true);
    }
    private void endBlock(boolean newline) {
        indentLevel--;
        endLine();
        out("}");
        if (newline) {
            endLine();
        }
    }

    /** Prints source code location in the form "at [filename] ([location])" */
    private void location(Node node) {
        out(" at ", node.getUnit().getFilename(), " (", node.getLocation(), ")");
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
        out(memberName(that.getDeclarationModel(), false));
    }

    @Override
    public void visit(ParameterList that) {
        out("(");
        boolean first=true;
        for (Parameter param: that.getParameters()) {
            if (!first) out(",");
            out(memberName(param.getDeclarationModel(), false));
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
            initSelf(that);
            visitStatements(stmnts, false);
            endBlock();
        }
    }

    private void initSelf(Block block) {
        if ((prototypeOwner!=null) && (block.getScope() instanceof MethodOrValue)) {
            /*out("var ");
            self();
            out("=this;");
            endLine();*/
            out("var ");
            self(prototypeOwner);
            out("=this;");
            endLine();
        }
    }

    private void comment(Tree.Declaration that) {
        if (!comment) return;
        endLine();
        out("//", that.getNodeType(), " ", that.getDeclarationModel().getName());
        location(that);
        endLine();
    }

    private void var(Declaration d) {
        out("var ", memberName(d, false), "=");
    }

    private void share(Declaration d) {
        if (isCaptured(d) && !(prototypeStyle && d.isClassOrInterfaceMember())) {
            outerSelf(d);
            out(".", memberName(d, false), "=", memberName(d, false), ";");
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
        out(memberName(dec, false), ";");
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

    private void function() {
        out("function ");
    }

    private void addInterfaceToPrototype(ClassOrInterface type, InterfaceDefinition interfaceDef) {
        interfaceDefinition(interfaceDef);
        Interface d = interfaceDef.getDeclarationModel();
        out("$proto$.", memberName(d, false), "=", d.getName(), ";");
        endLine();
    }

    @Override
    public void visit(InterfaceDefinition that) {
        if (!(prototypeStyle && that.getDeclarationModel().isClassOrInterfaceMember())) {
            interfaceDefinition(that);
        }
    }

    private void interfaceDefinition(InterfaceDefinition that) {
        Interface d = that.getDeclarationModel();
        comment(that);

        function();
        out(d.getName(), "(");
        self(d);
        out(")");
        beginBlock();
        //declareSelf(d);
        referenceOuter(d);
        that.getInterfaceBody().visit(this);
        //returnSelf(d);
        endBlock();
        share(d);

        addTypeInfo(that);
        copyMembersToPrototype(d, null, that.getSatisfiedTypes());

        addToPrototype(d, that.getInterfaceBody().getStatements());
    }

    /** Add a comment to the generated code with info about the type parameters. */
    private void comment(TypeParameter tp) {
        if (!comment) return;
        out("<");
        if (tp.isCovariant()) {
            out("out ");
        } else if (tp.isContravariant()) {
            out("in ");
        }
        out(tp.getQualifiedNameString());
        for (TypeParameter st : tp.getTypeParameters()) {
            comment(st);
        }
        out("> ");
    }
    /** Add a comment to the generated code with info about the produced type parameters. */
    private void comment(ProducedType pt) {
        if (!comment) return;
        out("<");
        out(pt.getProducedTypeQualifiedName());
        //This is useful to iterate into the types of this type
        //but for comment it's unnecessary
        /*for (ProducedType spt : pt.getTypeArgumentList()) {
            comment(spt);
        }*/
        out("> ");
    }

    private void addClassToPrototype(ClassOrInterface type, ClassDefinition classDef) {
        classDefinition(classDef);
        Class d = classDef.getDeclarationModel();
        out("$proto$.", memberName(d, false), "=", d.getName(), ";");
        endLine();
    }

    @Override
    public void visit(ClassDefinition that) {
        if (!(prototypeStyle && that.getDeclarationModel().isClassOrInterfaceMember())) {
            classDefinition(that);
        }
    }

    private void classDefinition(ClassDefinition that) {
        Class d = that.getDeclarationModel();
        comment(that);

        function();
        out(d.getName(), "(");
        for (Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        self(d);
        out(")");
        beginBlock();
        if (!d.getTypeParameters().isEmpty()) {
            //out(",");
            //selfTypeParameters(d);
            out("/* REIFIED GENERICS SOON! ");
            for (TypeParameter tp : d.getTypeParameters()) {
                comment(tp);
            }
            out("*/");
            endLine();
        }
        declareSelf(d);
        referenceOuter(d);
        initParameters(that.getParameterList(), d);
        callSuperclass(that.getExtendedType(), d, that);
        copySuperMembers(that.getExtendedType(), that.getClassBody(), d);
        callInterfaces(that.getSatisfiedTypes(), d, that);
        that.getClassBody().visit(this);
        returnSelf(d);
        endBlock();
        share(d);

        addTypeInfo(that);
        copyMembersToPrototype(d, that.getExtendedType(), that.getSatisfiedTypes());

        addToPrototype(d, that.getClassBody().getStatements());
    }

    private void referenceOuter(TypeDeclaration d) {
        if (prototypeStyle && d.isClassOrInterfaceMember()) {
            self(d);
            out(".");
            outerSelf(d);
            out("=this;");
            endLine();
        }
    }

    private void copySuperMembers(ExtendedType extType, ClassBody body, Class d) {
        if (!prototypeStyle) {
            String parentName = "";
            if (extType != null) {
                TypeDeclaration parentTypeDecl = extType.getType().getDeclarationModel();
                if (declaredInCL(parentTypeDecl)) {
                    return;
                }
            	parentName = parentTypeDecl.getName();
            }

            final List<Declaration> decs = new ArrayList<Declaration>();
            new SuperVisitor(decs).visit(body);
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

    private void callSuperclass(ExtendedType extendedType, Class d, Node that) {
        if (extendedType!=null) {
            qualify(that, extendedType.getType().getDeclarationModel());
            out(memberName(extendedType.getType().getDeclarationModel(), false), "(");
            for (PositionalArgument arg: extendedType.getInvocationExpression()
                    .getPositionalArgumentList().getPositionalArguments()) {
                arg.visit(this);
                out(",");
            }
            self(d);
            out(");");
            endLine();
        }
    }

    private void callInterfaces(SatisfiedTypes satisfiedTypes, Class d, Node that) {
        if (satisfiedTypes!=null)
            for (SimpleType st: satisfiedTypes.getTypes()) {
                qualify(that, st.getDeclarationModel());
                out(memberName(st.getDeclarationModel(), false), "(");
                self(d);
                out(");");
                endLine();
            }
    }

    private void addTypeInfo(Tree.Declaration type) {

        ExtendedType extendedType = null;
        SatisfiedTypes satisfiedTypes = null;
        if (type instanceof ClassDefinition) {
            ClassDefinition classDef = (ClassDefinition) type;
            extendedType = classDef.getExtendedType();
            satisfiedTypes = classDef.getSatisfiedTypes();
        } else if (type instanceof InterfaceDefinition) {
            satisfiedTypes = ((InterfaceDefinition) type).getSatisfiedTypes();
        } else if (type instanceof ObjectDefinition) {
            ObjectDefinition objectDef = (ObjectDefinition) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
        }

        clAlias();
        out(".initType(", type.getDeclarationModel().getName(), ",'",
            type.getDeclarationModel().getQualifiedNameString(), "'");

        if (extendedType != null) {
            out(",", initFunctionName(extendedType.getType()));
        } else if (!(type instanceof InterfaceDefinition)) {
            out(",");
            clAlias();
            out(".IdentifiableObject");
        }

        if (satisfiedTypes != null) {
            for (SimpleType satType : satisfiedTypes.getTypes()) {
                out(",", initFunctionName(satType));
            }
        }

        out(");");
        endLine();
    }

    private String initFunctionName(SimpleType type) {
        TypeDeclaration d = type.getDeclarationModel();
        boolean inProto = prototypeStyle && d.isClassOrInterfaceMember();
        String constr = qualifiedPath(type, d, inProto);
        if (constr.length() > 0) {
            constr += '.';
        }
        if (inProto && (constr.length() == 0)) {
            constr += d.getName();
        } else {
            constr += memberName(d, false);
        }
        return constr;
    }

    private void addToPrototype(ClassOrInterface d, List<Statement> statements) {
        if (prototypeStyle && !statements.isEmpty()) {
            out(";(function($proto$)");
            beginBlock();
            for (Statement s: statements) {
                addToPrototype(d, s);
            }
            endBlock(false);
            out(")(", d.getName(), ".$$.prototype);");
            endLine();
        }
    }

    private ClassOrInterface prototypeOwner;

    private void addToPrototype(ClassOrInterface d, Statement s) {
        ClassOrInterface oldPrototypeOwner = prototypeOwner;
        prototypeOwner = d;
        if (s instanceof MethodDefinition) {
            addMethodToPrototype(d, (MethodDefinition)s);
        } else if (s instanceof AttributeGetterDefinition) {
            addGetterToPrototype(d, (AttributeGetterDefinition)s);
        } else if (s instanceof AttributeSetterDefinition) {
            addSetterToPrototype(d, (AttributeSetterDefinition)s);
        } else if (s instanceof AttributeDeclaration) {
            addGetterAndSetterToPrototype(d, (AttributeDeclaration)s);
        } else if (s instanceof ClassDefinition) {
            addClassToPrototype(d, (ClassDefinition) s);
        } else if (s instanceof InterfaceDefinition) {
            addInterfaceToPrototype(d, (InterfaceDefinition) s);
        } else if (s instanceof ObjectDefinition) {
            addObjectToPrototype(d, (ObjectDefinition) s);
        }
        prototypeOwner = oldPrototypeOwner;
    }

    private void declareSelf(ClassOrInterface d) {
        out("if (");
        self(d);
        out("===undefined)");
        self(d);
        out("=new ");
        if (prototypeStyle && d.isClassOrInterfaceMember()) {
            out("this.");
        }
        out(memberName(d, false), ".$$;");
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
        out("=new ");
        if (prototypeStyle && d.isClassOrInterfaceMember()) {
            out("this.");
        }
        out(memberName(d, false), ".$$;");
        endLine();
    }

    private void returnSelf(ClassOrInterface d) {
        out("return ");
        self(d);
        out(";");
    }

    private void copyMembersToPrototype(Declaration d, ExtendedType extType, SatisfiedTypes satTypes) {

        boolean copyFromExtType = !(d instanceof Interface)
                && ((extType == null) || prototypeStyle
                        || declaredInCL(extType.getType().getDeclarationModel()));
        boolean copyFromSatType = false;
        if ((satTypes != null) && !satTypes.getTypes().isEmpty()) {
            if (prototypeStyle) {
                copyFromSatType = true;
            } else {
                for (Tree.SimpleType st: satTypes.getTypes()) {
                    if (declaredInCL(st.getDeclarationModel())) {
                        copyFromSatType = true;
                        break;
                    }
                }
            }
        }
        if (!(copyFromExtType || copyFromSatType)) {
            return;
        }

        if (copyFromExtType) {
            String from = null;
            String suffix = null;
            if (extType == null) {
                from = "$$$cl15.IdentifiableObject";
                suffix = "$$$cl15$IdentifiableObject$";
            } else {
                SimpleType type = extType.getType();
                TypeDeclaration typeDecl = type.getDeclarationModel();
                from = initFunctionName(type);
                suffix = qualifiedPath(type, typeDecl) + '$' + typeDecl.getName() + '$';
            }

            clAlias();
            out(".inheritProto(", d.getName(), ",", from, ",'", suffix, "'");

        } else {
            clAlias();
            out(".inheritProtoI(", d.getName());
        }

        if (copyFromSatType) {
            for (Tree.SimpleType st: satTypes.getTypes()) {
                if (prototypeStyle || declaredInCL(st.getDeclarationModel())) {
                    out(",", initFunctionName(st));
                }
            }
        }
        out(");");
        endLine();
    }

    private void addObjectToPrototype(ClassOrInterface type, ObjectDefinition objDef) {
        objectDefinition(objDef);
        Value d = objDef.getDeclarationModel();
        out("$proto$.", memberName(d, false), "=", d.getName(), ";");
        endLine();
    }

    @Override
    public void visit(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        if (!(prototypeStyle && d.isClassOrInterfaceMember())) {
            objectDefinition(that);
        } else {
            String name = memberName(d, false);
            comment(that);
            outerSelf(d);
            out(".o$", name, "=");
            outerSelf(d);
            out(".", name, "();");
            endLine();
        }
    }

    private void objectDefinition(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        boolean addToPrototype = prototypeStyle && d.isClassOrInterfaceMember();
        Class c = (Class) d.getTypeDeclaration();
        comment(that);

        function();
        out(d.getName(), "()");
        beginBlock();
        instantiateSelf(c);
        referenceOuter(c);
        callSuperclass(that.getExtendedType(), c, that);
        copySuperMembers(that.getExtendedType(), that.getClassBody(), c);
        callInterfaces(that.getSatisfiedTypes(), c, that);
        that.getClassBody().visit(this);
        returnSelf(c);
        indentLevel--;
        endLine();
        out("}");
        endLine();

        addTypeInfo(that);
        copyMembersToPrototype(d, that.getExtendedType(), that.getSatisfiedTypes());

        if (!addToPrototype) {
            out("var o$", memberName(d, false), "=",
                    d.getName(), "(new ", d.getName(), ".$$);");
            endLine();
        }

        if (addToPrototype) {
            out("$proto$.", getter(d), "=");
        } else if (d.isShared()) {
            outerSelf(d);
            out(".", getter(d), "=");
        }
        function();
        out(getter(d), "()");
        beginBlock();
        out("return ");
        if (addToPrototype) {
            out("this.");
        }
        out("o$", memberName(d, false), ";");
        endBlock();

        addToPrototype(c, that.getClassBody().getStatements());
    }

    private void superRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".", memberName(d, false), "$", parent, "$=");
            self(sub);
            out(".", memberName(d, false), ";");
            endLine();
        //}
    }

    private void superGetterRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".", getter(d), "$", parent, "$=");
            self(sub);
            out(".", getter(d), ";");
            endLine();
        //}
    }

    private void superSetterRef(Declaration d, Class sub, String parent) {
        //if (d.isActual()) {
            self(sub);
            out(".", setter(d), "$", parent, "$=");
            self(sub);
            out(".", setter(d), ";");
            endLine();
        //}
    }

    @Override
    public void visit(MethodDeclaration that) {
        if (that.getSpecifierExpression() != null) {
            comment(that);
            out("var ", that.getIdentifier().getText(), "=");
            that.getSpecifierExpression().getExpression().visit(this);
            out(";");
            endLine();
        }
    }

    @Override
    public void visit(MethodDefinition that) {
        if (!(prototypeStyle && that.getDeclarationModel().isClassOrInterfaceMember())) {
            comment(that);
            methodDefinition(that);
        }
    }

    private void methodDefinition(MethodDefinition that) {
        Method d = that.getDeclarationModel();
        function();
        out(memberName(d, false));

        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        ParameterList paramList = that.getParameterLists().get(0);
        paramList.visit(this);

        beginBlock();
        initSelf(that.getBlock());
        initParameters(paramList, null);
        visitStatements(that.getBlock().getStatements(), false);
        endBlock();

        share(d);
    }

    private void initParameters(ParameterList params, TypeDeclaration typeDecl) {
        for (Parameter param : params.getParameters()) {
            String paramName = memberName(param.getDeclarationModel(), false);
            if (param.getDefaultArgument() != null) {
                out("if(", paramName, "===undefined){", paramName, "=");
                param.getDefaultArgument().getSpecifierExpression().getExpression().visit(this);
                out("}");
                endLine();
            }
            if ((typeDecl != null) && param.getDeclarationModel().isCaptured()) {
                self(typeDecl);
                out(".", paramName, "=", paramName, ";");
                endLine();
            }
        }
    }

    private void addMethodToPrototype(Declaration outer,
            MethodDefinition that) {
        Method d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$proto$.", memberName(d, false), "=");
        methodDefinition(that);
    }

    @Override
    public void visit(AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        function();
        out(getter(d), "()");
        super.visit(that);
        shareGetter(d);
    }

    private void addGetterToPrototype(Declaration outer,
            AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$proto$.", getter(d), "=");
        function();
        out(getter(d), "()");
        super.visit(that);
    }

    private void shareGetter(MethodOrValue d) {
        if (isCaptured(d)) {
            outerSelf(d);
            out(".", getter(d), "=", getter(d), ";");
            endLine();
        }
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        function();
        out(setter(d), "(", d.getName(), ")");
        super.visit(that);
        shareSetter(d);
    }

    private void addSetterToPrototype(Declaration outer,
            AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (!prototypeStyle || !d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$proto$.", setter(d), "=");
        function();
        out(setter(d), "(", d.getName(), ")");
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
            out(".", setter(d), "=", setter(d), ";");
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
                    out(".", memberName(d, false), "=");
                    super.visit(that);
                    out(";");
                    endLine();
                }
            }
            else {
                out("var $", d.getName());
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
                out("return $", d.getName(), ";");
                endBlock();
                shareGetter(d);
                if (d.isVariable()) {
                    function();
                    out(setter(d), "(", d.getName(), ")");
                    beginBlock();
                    out("$", d.getName(), "=", d.getName(), "; return ", d.getName(), ";");
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
            out("$proto$.", getter(d), "=");
            function();
            out(getter(d), "()");
            beginBlock();
            out("return this.", memberName(d, false), ";");
            endBlock();
            if (d.isVariable()) {
                out("$proto$.", setter(d), "=");
                function();
                out(setter(d), "(", d.getName(), ")");
                beginBlock();
                out("this.", memberName(d, false), "=", d.getName(), "; return ", d.getName(), ";");
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

        String text = that.getText();
        // pre-calculate string length
        // TODO: also for strings that contain escape sequences
        int codepoints = -1;
        if (text.indexOf('\\') < 0) {
            codepoints = text.codePointCount(0, text.length()) - 2;
        }
        text = that.getText().replaceAll("\n", "\\\\n");

        clAlias();
        out(".String(");
        out(text);
        if (codepoints >= 0) {
        	out(",");
        	out(String.valueOf(codepoints));
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
        out(".Float(", that.getText(), ")");
    }

    @Override
    public void visit(NaturalLiteral that) {
        clAlias();
        out(".Integer(", that.getText(), ")");
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
        if (prototypeStyle) {
            Scope scope = that.getScope();
            while ((scope != null) && !(scope instanceof TypeDeclaration)) {
                scope = scope.getContainer();
            }
            if (scope != null) {
                self((TypeDeclaration) scope);
                out(".");
            }
        }
        self(that.getTypeModel().getDeclaration());
    }

    @Override
    public void visit(BaseMemberExpression that) {
        Declaration decl = that.getDeclaration();
        qualify(that, decl);
        if (!accessThroughGetter(decl)) {
            out(memberName(decl, false));
        }
        else {
            out(getter(decl));
            out("()");
        }
    }

    private boolean accessThroughGetter(Declaration d) {
        return !((d instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter)
                  || (d instanceof Method));
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        //Big TODO: make sure the member is actually
        //          refined by the current class!
    	if (that.getMemberOperator() instanceof SafeMemberOp) {

            if (that.getDeclaration() instanceof Method) {
                String tmp=createTempVariable();
                out("(function(){var ", tmp, "=");
                super.visit(that);
                out("; return ");
                clAlias();
                out(".JsCallable(", tmp, ",", tmp, "===null?null:", tmp, ".");
                qualifiedMemberRHS(that);
                out(");}())");
            } else {
    		    out("(function($){return $===null?null:$.");
                qualifiedMemberRHS(that);
                out("}(");
                super.visit(that);
                out("))");
            }
    	} else if (that.getMemberOperator() instanceof SpreadOp) {
    	    generateSpread(that);
        } else if (that.getDeclaration() instanceof Method && that.getSignature() == null) {
    	    //TODO right now this causes that all method invocations are done this way
    	    //we need to filter somehow to only use this pattern when the result is supposed to be a callable
    	    //looks like checking for signature is a good way (not THE way though; named arg calls don't have signature)
            generateCallable(that, null);
        } else {
            super.visit(that);
            out(".");
            qualifiedMemberRHS(that);
        }
    }

    /** SpreadOp cannot be a simple function call because we need to reference the object methods directly, so it's a function */
    private void generateSpread(QualifiedMemberOrTypeExpression that) {
        //Determine if it's a method or attribute
        boolean isMethod = that.getDeclaration() instanceof Method;
        //Define a function
        out("(function()");
        beginBlock();
        if (comment) {
            out("//SpreadOp at ", that.getLocation());
            endLine();
        }
        //Declare an array to store the values/references
        String tmplist = createTempVariable();
        out("var ", tmplist, "=[];"); endLine();
        //Get an iterator
        String iter = createTempVariable();
        out("var ", iter, "=");
        super.visit(that);
        out(".getIterator();"); endLine();
        //Iterate
        String elem = createTempVariable();
        out("var ", elem, ";"); endLine();
        out("while ((", elem, "=", iter, ".next()) !== ");
        clAlias();
        out(".getExhausted())");
        beginBlock();
        //Add value or reference to the array
        out(tmplist, ".push(");
        if (isMethod) {
            out("{o:", elem, ", f:", elem, ".");
            qualifiedMemberRHS(that);
            out("}");
        } else {
            out(elem, ".");
            qualifiedMemberRHS(that);
        }
        out(");");
        endBlock();
        //Gather arguments to pass to the callable
        //Return the array of values or a Callable with the arguments
        out("return "); clAlias();
        if (isMethod) {
            out(".JsCallableList(", tmplist, ");");
        } else {
            out(".ArraySequence(", tmplist, ");");
        }
        endBlock(false);
        out("())");
    }

    private void generateCallable(QualifiedMemberOrTypeExpression that, String name) {
        out("(function(){var $=");
        that.getPrimary().visit(this);
        out(";return ");
        clAlias();
        out(".JsCallable($, $.");
        if (name == null) {
            qualifiedMemberRHS(that);
        } else {
            out(name);
        }
        out(")})()");
    }

    private void qualifiedMemberRHS(QualifiedMemberOrTypeExpression that) {
    	boolean sup = that.getPrimary() instanceof Super;
    	String postfix = "";
    	if (sup) {
    		 ClassOrInterface type = Util.getContainingClassOrInterface(that.getScope());
    		 ClassOrInterface parentType = type.getExtendedTypeDeclaration();
    		 if (parentType != null) {
    			 postfix = '$' + parentType.getName() + '$';
    		 }
    	}
        if (!accessThroughGetter(that.getDeclaration())) {
            out(memberName(that.getDeclaration(), false), postfix);
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
        out(memberName(that.getDeclaration(), false));
        if (!that.getTypeArguments().getTypeModels().isEmpty()) {
            out("/* REIFIED GENERICS SOON!! ");
            for (ProducedType pt : that.getTypeArguments().getTypeModels()) {
                comment(pt);
            }
            out("*/");
        }
    }

    @Override
    public void visit(QualifiedTypeExpression that) {
        super.visit(that);
        out(".", memberName(that.getDeclaration(), false));
    }

    @Override
    public void visit(InvocationExpression that) {
        sequencedParameter=false;
        if (that.getNamedArgumentList()!=null) {
            out("(function (){");
            that.getNamedArgumentList().visit(this);
            out("return ");
            that.getPrimary().visit(this);
            out("(");
            if (that.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
                if (mte.getDeclaration() instanceof Functional) {
                    Functional f = (Functional) mte.getDeclaration();
                    if (!f.getParameterLists().isEmpty()) {
                        List<String> argNames = that.getNamedArgumentList().getNamedArgumentList().getArgumentNames();
                        boolean first=true;
                        for (com.redhat.ceylon.compiler.typechecker.model.Parameter p:
                            f.getParameterLists().get(0).getParameters()) {
                            if (!first) out(",");
                            if (p.isSequenced() && that.getNamedArgumentList().getSequencedArgument()==null && that.getNamedArgumentList().getNamedArguments().isEmpty()) {
                                clAlias();
                                out(".empty");
                            } else if (p.isSequenced() || argNames.contains(p.getName())) {
                                out("$");
                                out(p.getName());
                            } else {
                                out("undefined");
                            }
                            first = false;
                        }
                    }
                }
            }
            out(")}())");
        }
        else {
            if (that.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
                if (mte.getDeclaration() instanceof Functional) {
                    Functional f = (Functional) mte.getDeclaration();
                    if (!f.getParameterLists().isEmpty()) {
                        com.redhat.ceylon.compiler.typechecker.model.ParameterList plist = f.getParameterLists().get(0);
                        if (!plist.getParameters().isEmpty() && plist.getParameters().get(plist.getParameters().size()-1).isSequenced()) {
                            sequencedParameter=true;
                        }
                    }
                }
            }
            super.visit(that);
        }
    }

    @Override
    public void visit(PositionalArgumentList that) {
        out("(");
        if (that.getPositionalArguments().isEmpty() && sequencedParameter) {
            clAlias();
            out(".empty");
        } else {
            boolean first=true;
            boolean sequenced=false;
            for (PositionalArgument arg: that.getPositionalArguments()) {
                if (!first) out(",");
                if (!sequenced && arg.getParameter().isSequenced() && that.getEllipsis() == null) {
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
        }
        out(")");
    }

    @Override
    public void visit(NamedArgumentList that) {
        for (NamedArgument arg: that.getNamedArguments()) {
            out("var $", arg.getParameter().getName(), "=");
            arg.visit(this);
            out(";");
        }
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg!=null) {
            out("var $", sarg.getParameter().getName(), "=");
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
        if (!(prototypeStyle && bme.getDeclaration().isClassOrInterfaceMember())) {
            out("$");
        }
        out(memberName(bme.getDeclaration(), false), "=");
        that.getSpecifierExpression().visit(this);
    }

    @Override
    public void visit(AssignOp that) {
        boolean paren=false;
        if (that.getLeftTerm() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getLeftTerm();
            qualify(that, bme.getDeclaration());
            out(setter(bme.getDeclaration()));
            out("(");
            paren = !(bme.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter);
        } else if (that.getLeftTerm() instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression qme = (QualifiedMemberExpression)that.getLeftTerm();
            super.visit(qme);
            out(".", setter(qme.getDeclaration()));
            out("(");
            paren = true;
        }
        that.getRightTerm().visit(this);
        if (paren) {
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
        return qualifiedPath(that, d, false);
    }

    private String qualifiedPath(Node that, Declaration d, boolean inProto) {
        if (isImported(that, d)) {
            return packageAliasString(d.getUnit().getPackage());
        }
        else if (prototypeStyle) {
            if (d.isClassOrInterfaceMember() &&
                    !(d instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter &&
                            !d.isCaptured())) {
                TypeDeclaration id = that.getScope().getInheritingDeclaration(d);
                if (id == null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    id = (TypeDeclaration) d.getContainer();
                } //else {
                    //an inherited declaration that might be
                    //inherited by an outer scope
                //}
                String path = "";
                Scope scope = that.getScope();
                if (inProto) {
                    while ((scope != null) && (scope instanceof TypeDeclaration)) {
                        scope = scope.getContainer();
                    }
                }
                while (scope != null) {
                    if (scope instanceof TypeDeclaration) {
                        if (path.length() > 0) {
                            path += '.';
                        }
                        path += selfString((TypeDeclaration) scope);
                    } else {
                        path = "";
                    }
                    if (scope == id) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                return path;
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
        if (that.getTerm() instanceof QualifiedMemberOrTypeExpression) {
            QualifiedMemberOrTypeExpression term = (QualifiedMemberOrTypeExpression) that.getTerm();
            // references to methods of types from ceylon.language always need
            // special treatment, even if prototypeStyle==false
            if ((term.getDeclaration() instanceof Functional)
                    && (prototypeStyle || declaredInCL(term.getDeclaration()))) {
                if (term.getMemberOperator() instanceof SpreadOp) {
                    generateSpread(term);
                } else {
                    generateCallable(term, memberName(term.getDeclaration(), false));
                }
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

    /* * Output the name of a variable that receives the type parameter info, usually in the class constructor. * /
    private void selfTypeParameters(TypeDeclaration d) {
        out(selfTypeParametersString(d));
    }
    private String selfTypeParametersString(TypeDeclaration d) {
        return "$$typeParms" + d.getName();
    }*/
    /*private void self() {
        out("$$");
    }*/

    private void outerSelf(Declaration d) {
        if (d.isToplevel()) {
            out("exports");
        }
        else if (d.isClassOrInterfaceMember()) {
            self((TypeDeclaration)d.getContainer());
        }
    }

    private String setter(Declaration d) {
    	String name = memberName(d, true);
        return "set" + toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String getter(Declaration d) {
    	String name = memberName(d, true);
        return "get" + toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String memberName(Declaration d, Boolean forGetterSetter) {
    	String name = d.getName();
    	Scope container = d.getContainer();
    	if (prototypeStyle && !d.isShared() && d.isMember()
    				&& (container instanceof ClassOrInterface)) {
    		ClassOrInterface parentType = (ClassOrInterface) container;
    		name += '$' + parentType.getName();
    		if (forGetterSetter || (d instanceof Method)) {
    			name += '$';
    		}

    	}
    	return name;
    }

    private boolean declaredInCL(Declaration typeDecl) {
        return typeDecl.getUnit().getPackage().getQualifiedNameString()
                .startsWith("ceylon.language");
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

    		out("(", lhsPath, setter(lhsBME.getDeclaration()), "(", lhsPath,
		        getter(lhsBME.getDeclaration()), "().", functionName, "(");
    		that.getRightTerm().visit(this);
    		out(")),", lhsPath, getter(lhsBME.getDeclaration()), "())");

    	} else if (lhs instanceof QualifiedMemberExpression) {
    		QualifiedMemberExpression lhsQME = (QualifiedMemberExpression) lhs;
    		out("(function($1,$2){var $=$1.", getter(lhsQME.getDeclaration()), "().",
		        functionName, "($2);$1.", setter(lhsQME.getDeclaration()), "($);return $}(");
    		lhsQME.getPrimary().visit(this);
    		out(",");
    		that.getRightTerm().visit(this);
    		out("))");
    	}
    }

    @Override public void visit(NegativeOp that) {
        that.getTerm().visit(this);
        out(".getNegativeValue()");
    }

    @Override public void visit(PositiveOp that) {
        that.getTerm().visit(this);
        out(".getPositiveValue()");
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
    /** Outputs the CL equivalent of 'a==b' in JS. */
    private void leftEqualsRight(BinaryOperatorExpression that) {
    	that.getLeftTerm().visit(this);
        out(".equals(");
        that.getRightTerm().visit(this);
        out(")");
    }
    /** Outputs the CL equivalent of 'true' in JS. */
    private void clTrue() {
        clAlias();
        out(".getTrue()");
    }
    /** Outputs the CL equivalent of 'false' in JS. */
    private void clFalse() {
        clAlias();
        out(".getFalse()");
    }
    /** Outputs the CL equivalent of '==false' in JS. */
    private void equalsFalse() {
        out(".equals(");
        clFalse();
        out(")");
    }
    /** Outputs the CL equivalent of '?true:false' in JS */
    private void thenTrueElseFalse() {
    	out("?");
    	clTrue();
        out(":");
        clFalse();
    }
    /** Outputs the CL equivalent of 'a <=> b' in JS. */
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

		   out("(", path, setter(bme.getDeclaration()), "(", path);
		   String bmeGetter = getter(bme.getDeclaration());
		   out(bmeGetter, "().", functionName, "()),", path, bmeGetter, "())");
	   } else if (term instanceof QualifiedMemberExpression) {
		   QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
		   out("function($){var $2=$.", getter(qme.getDeclaration()), "().",
	           functionName, "();$.", setter(qme.getDeclaration()), "($2);return $2}(");
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

		   out("(function($){", path, setter(bme.getDeclaration()), "($.", functionName,
	           "());return $}(", path, getter(bme.getDeclaration()), "()))");
	   } else if (term instanceof QualifiedMemberExpression) {
		   QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
		   out("function($){var $2=$.", getter(qme.getDeclaration()), "();$.",
	           setter(qme.getDeclaration()), "($2.", functionName, "());return $2}(");
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
	   if ((condition instanceof ExistsOrNonemptyCondition)
	           || (condition instanceof IsCondition)) {
		   // if (is/exists/nonempty ...)
	       specialConditionAndBlock(condition, ifBlock, "if");
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
	   if ((condition instanceof ExistsOrNonemptyCondition)
	           || (condition instanceof IsCondition)) {
		   // while (is/exists/nonempty...)
	       specialConditionAndBlock(condition, whileClause.getBlock(), "while");
	   } else {
		   out("while ((");
	       condition.visit(this);
	       out(")===");
	       clAlias();
	       out(".getTrue())");
	       whileClause.getBlock().visit(this);
	   }
   }

   // handles an "is", "exists" or "nonempty" condition
   private void specialConditionAndBlock(Condition condition,
               Block block, String keyword) {
       Variable variable = null;
       if (condition instanceof ExistsOrNonemptyCondition) {
           variable = ((ExistsOrNonemptyCondition) condition).getVariable();
       } else if (condition instanceof IsCondition) {
           variable = ((IsCondition) condition).getVariable();
       } else {
           condition.addUnexpectedError("No support for conditions of type " + condition.getClass().getSimpleName());
           return;
       }
	   String varName = variable.getDeclarationModel().getName();

	   boolean simpleCheck = false;
	   Term variableRHS = variable.getSpecifierExpression().getExpression().getTerm();
	   if (variableRHS instanceof BaseMemberExpression) {
		   BaseMemberExpression bme = (BaseMemberExpression) variableRHS;
		   if (bme.getDeclaration().getName().equals(varName)) {
			   // the simple case: if/while (is/exists/nonempty x)
			   simpleCheck = true;
		   }
	   }

	   if (simpleCheck) {
	       BaseMemberExpression bme = (BaseMemberExpression) variableRHS;

		   out(keyword);
           out("(");
           specialConditionCheck(condition, variableRHS, simpleCheck);
           out(")");

           if (matchingGetterExists(bme.getDeclaration(), variable.getDeclarationModel(),
                                    condition)) {
               // a getter for the variable already exists
               block.visit(this);
           } else {
               // no getter exists yet, so define one
               beginBlock();
               function();
               out(getter(variable.getDeclarationModel()));
               out("(){");
               out("return ", memberName(bme.getDeclaration(), false));
               out("}");
               endLine();

               visitStatements(block.getStatements(), false);
               endBlock();
           }

	   } else {
		   // if/while (is/exists/nonempty x=...)

		   out("var $cond$;");
		   endLine();

		   out(keyword);
		   out("(");
		   specialConditionCheck(condition, variableRHS, simpleCheck);
           out(")");

		   if (block.getStatements().isEmpty()) {
			   out("{}");

		   } else {
			   beginBlock();
			   out("var $");
			   out(varName);
			   out("=$cond$;");
			   endLine();

			   function();
			   out(getter(variable.getDeclarationModel()));
			   out("(){");
			   out("return $");
			   out(varName);
			   out("}");
			   endLine();

			   visitStatements(block.getStatements(), false);
			   endBlock();
		   }
	   }
   }

   private boolean matchingGetterExists(Declaration outerVar, Declaration innerVar, Node that) {
       return accessThroughGetter(outerVar) && getter(outerVar).equals(getter(innerVar))
               && (qualifiedPath(that, outerVar).length() == 0);
   }

    private void specialConditionCheck(Condition condition, Term variableRHS, boolean simpleCheck) {
        if (condition instanceof ExistsOrNonemptyCondition) {
            if (condition instanceof NonemptyCondition) {
                clAlias();
                out(".nonempty(");
                specialConditionRHS(variableRHS, simpleCheck);
                out(")===");
                clAlias();
                out(".getTrue()");
            } else {
                specialConditionRHS(variableRHS, simpleCheck);
                out("!==null");
            }

        } else {
            Type type = ((IsCondition) condition).getType();
            generateIsOfType(variableRHS, null, type, simpleCheck);
            out("===");
            clAlias();
            out(".getTrue()");
        }
    }

    private void specialConditionRHS(Term variableRHS, boolean simple) {
        if (simple) {
            variableRHS.visit(this);
        } else {
            out("($cond$=");
            variableRHS.visit(this);
            out(")");
        }
    }

    private void specialConditionRHS(String variableRHS, boolean simple) {
        if (simple) {
            out(variableRHS);
        } else {
            out("($cond$=");
            out(variableRHS);
            out(")");
        }
    }

    /** Appends an object with the type's type and list of union/instersection types. */
    private void getTypeList(StaticType type) {
        out("{ t:'");
        if (type instanceof UnionType) {
            out("u");
        } else {
            out("i");
        }
        out("', l:[");
        List<StaticType> types = type instanceof UnionType ? ((UnionType)type).getStaticTypes() : ((IntersectionType)type).getStaticTypes();
        boolean first = true;
        for (StaticType t : types) {
            if (!first) out(",");
            if (t instanceof SimpleType) {
                out("'");
                out(((SimpleType)t).getDeclarationModel().getQualifiedNameString());
                out("'");
            } else {
                getTypeList(t);
            }
            first = false;
        }
        out("]}");
    }

    /** Generates js code to check if a term is of a certain type. We solve this in JS by
     * checking against all types that Type satisfies (in the case of union types, matching any
     * type will do, and in case of intersection types, all types must be matched). */
    private void generateIsOfType(Term term, String termString, Type type, boolean simpleCheck) {
        clAlias();
        if (type instanceof SimpleType) {
            out(".isOfType(");
        } else {
            out(".Boolean(");
            clAlias();
            out(".isOfTypes(");
        }
        if (term != null) {
            specialConditionRHS(term, simpleCheck);
        } else {
            specialConditionRHS(termString, simpleCheck);
        }
        out(",");

        if (type instanceof SimpleType) {
            out("'");
            out(((SimpleType) type).getDeclarationModel().getQualifiedNameString());
            out("')");
            if (term != null && term.getTypeModel() != null && !term.getTypeModel().getTypeArguments().isEmpty()) {
                out("/* REIFIED GENERICS SOON!!! ");
                out(" term " + term.getTypeModel());
                out(" model " + term.getTypeModel().getTypeArguments());
                for (ProducedType pt : term.getTypeModel().getTypeArgumentList()) {
                    comment(pt);
                }
                out("*/");
            }
        } else {
            getTypeList((StaticType)type);
            out("))");
        }
    }
    @Override
    public void visit(IsOp that) {
        generateIsOfType(that.getTerm(), null, that.getType(), true); //TODO is it always simple?
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
       if (comment) out("//'for' statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
       endLine();
	   ForIterator foriter = that.getForClause().getForIterator();
	   SpecifierExpression iterable = foriter.getSpecifierExpression();
	   boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
	   //First we need to enclose this inside an anonymous function,
	   //to avoid problems with repeated iterator variables
	   out("(function()");
	   beginBlock();
	   final String iterVar = createTempVariable();
	   final String itemVar = createTempVariable();
	   out("var ", iterVar, " = ");
	   iterable.visit(this);
	   out(".getIterator();");
	   endLine();
	   out("var ", itemVar, ";");
	   endLine();
	   out("while (");
	   out("(", itemVar, "=", iterVar, ".next()) !== ");
	   clAlias();
	   out(".getExhausted())");
	   List<Statement> stmnts = that.getForClause().getBlock().getStatements();
	   if (stmnts.isEmpty()) {
		   out("{}");
		   endLine();
	   } else {
		   beginBlock();
		   if (foriter instanceof ValueIterator) {
			   Value model = ((ValueIterator)foriter).getVariable().getDeclarationModel();
			   function();
			   out(getter(model), "(){ return ", itemVar, "; }");
		   } else if (foriter instanceof KeyValueIterator) {
			   Value keyModel = ((KeyValueIterator)foriter).getKeyVariable().getDeclarationModel();
			   Value valModel = ((KeyValueIterator)foriter).getValueVariable().getDeclarationModel();
			   function();
			   out(getter(keyModel), "(){ return ", itemVar, ".getKey(); }");
			   endLine();
			   function();
			   out(getter(valModel), "(){ return ", itemVar, ".getItem(); }");
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
		   out("if (");
		   clAlias();
		   out(".getExhausted() === ", itemVar, ")");
		   that.getElseClause().getBlock().visit(this);
	   }
	   endBlock();
	   out("());");
   }

    public void visit(InOp that) {
        that.getRightTerm().visit(this);
        out(".contains(");
        that.getLeftTerm().visit(this);
        out(")");
    }

    @Override public void visit(TryCatchStatement that) {
        out("try");
        that.getTryClause().getBlock().visit(this);

        if (!that.getCatchClauses().isEmpty()) {
            out("catch($ex0$)");
            beginBlock();
            out("var $ex$=$ex0$;");
            endLine();
            boolean firstCatch = true;
            for (CatchClause catchClause : that.getCatchClauses()) {
                Variable variable = catchClause.getCatchVariable().getVariable();
                if (!firstCatch) {
                    out("else ");
                }
                firstCatch = false;
                out("if(");
                generateIsOfType(null, "$ex$", variable.getType(), true);
                out("===");
                clAlias();
                out(".getTrue())");

                if (catchClause.getBlock().getStatements().isEmpty()) {
                    out("{}");
                } else {
                    beginBlock();
                    function();
                    out(getter(variable.getDeclarationModel()));
                    out("(){return $ex$}");
                    endLine();

                    visitStatements(catchClause.getBlock().getStatements(), false);
                    endBlock();
                }
            }
            out("else{throw $ex$}");
            endBlock();
        }

        if (that.getFinallyClause() != null) {
            out("finally");
            that.getFinallyClause().getBlock().visit(this);
        }
    }

    @Override public void visit(Throw that) {
        out("throw ");
        if (that.getExpression() != null) {
            that.getExpression().visit(this);
        } else {
            clAlias();
            out(".Exception()");
        }
        out(";");
    }

    private void visitIndex(IndexExpression that) {
        that.getPrimary().visit(this);
        ElementOrRange eor = that.getElementOrRange();
        if (eor instanceof Element) {
            out(".item(");
            ((Element)eor).getExpression().visit(this);
            out(")");
        } else {//range, or spread?
            out(".span(");
            ((ElementRange)eor).getLowerBound().visit(this);
            if (((ElementRange)eor).getUpperBound() != null) {
                out(",");
                ((ElementRange)eor).getUpperBound().visit(this);
            }
            out(")");
        }
    }

    public void visit(IndexExpression that) {
        IndexOperator op = that.getIndexOperator();
        if (op instanceof SafeIndexOp) {
            clAlias();
            out(".exists(");
            that.getPrimary().visit(this);
            out(")===");
            clAlias();
            out(".getTrue()?");
        }
        visitIndex(that);
        if (op instanceof SafeIndexOp) {
            out(":");
            clAlias();
            out(".getNull()");
        }
    }

    /** Creates and returns a name for a tmp var. */
    private String createTempVariable() {
        tmpvarCount++;
        return "tmpvar$" + tmpvarCount;
    }

    /** Generates code for a case clause, as part of a switch statement. Each case
     * is rendered as an if. */
    private void caseClause(CaseClause cc, String expvar, Term switchTerm) {
        Declaration decl = null;
        out("if (");
        final CaseItem item = cc.getCaseItem();
        if (item instanceof IsCase) {
            IsCase isCaseItem = (IsCase) item;
            generateIsOfType(null, expvar, isCaseItem.getType(), true);
            out("===");
            clAlias(); out(".getTrue()");
            Variable caseVar = isCaseItem.getVariable();
            if ((switchTerm instanceof BaseMemberExpression) && (caseVar != null)) {
                BaseMemberExpression bme = (BaseMemberExpression) switchTerm;
                if (!matchingGetterExists(bme.getDeclaration(),
                        caseVar.getDeclarationModel(), cc)) {
                    decl = caseVar.getDeclarationModel();
                }
            }
        } else if (item instanceof SatisfiesCase) {
            item.addError("case(satisfies) not yet supported");
            out("true");
        } else if (item instanceof MatchCase){
            boolean first = true;
            for (Expression exp : ((MatchCase)item).getExpressionList().getExpressions()) {
                if (!first) out(" || ");
                out(expvar);
                out("==="); //TODO equality?
                /*out(".equals(");*/
                exp.visit(this);
                //out(")==="); clAlias(); out(".getTrue()");
                first = false;
            }
        } else {
            cc.addUnexpectedError("support for case of type " + cc.getClass().getSimpleName() + " not yet implemented");
        }
        out(") ");
        if (decl == null) {
            cc.getBlock().visit(this);
        } else if (cc.getBlock().getStatements().isEmpty()) {
            out("{}");
        } else {
            beginBlock();
            //out("function get", decl.getName().substring(0,1).toUpperCase(), decl.getName().substring(1));
            function();
            out(getter(decl));
            out("(){");
            out("return ", expvar, "; }");
            endLine();
            visitStatements(cc.getBlock().getStatements(), false);
            endBlock();
        }
    }

    @Override
    public void visit(SwitchStatement that) {
        if (comment) out("//Switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
        endLine();
        //Put the expression in a tmp var
        final String expvar = createTempVariable();
        out("var ", expvar, "=");
        Expression expr = that.getSwitchClause().getExpression();
        expr.visit(this);
        out(";"); endLine();
        //For each case, do an if
        boolean first = true;
        for (CaseClause cc : that.getSwitchCaseList().getCaseClauses()) {
            if (!first) out("else ");
            caseClause(cc, expvar, expr.getTerm());
            first = false;
        }
        if (that.getSwitchCaseList().getElseClause() != null) {
            out("else ");
            that.getSwitchCaseList().getElseClause().visit(this);
        }
        if (comment) out("//End switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
    }

}
