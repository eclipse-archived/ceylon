package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

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

    private boolean indent=true;
    private boolean comment=true;
    private final Stack<Continuation> continues = new Stack<Continuation>();
    private final EnclosingFunctionVisitor encloser = new EnclosingFunctionVisitor();
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess = new HashSet<Declaration>();
    
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
    private static String clAlias="";
    private static final String function="function ";
    private static String clTrue="";
    private static String clFalse="";

    private static void setCLAlias(String alias) {
        clAlias = alias;
        clTrue = String.format("%s.getTrue()", clAlias);
        clFalse = String.format("%s.getFalse()", clAlias);
    }
    
    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }

    public GenerateJsVisitor(Writer out, boolean prototypeStyle) {
        this.out = out;
        this.prototypeStyle=prototypeStyle;
        names = new JsIdentifierNames(prototypeStyle);
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
        Package clPackage = clm.getPackage(clm.getNameAsString());
        setCLAlias(names.packageAlias(clPackage));
        require(clPackage);
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
        out(names.packageAlias(pkg));
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
        out(names.name(that.getDeclarationModel()));
    }

    @Override
    public void visit(ParameterList that) {
        out("(");
        boolean first=true;
        for (Parameter param: that.getParameters()) {
            if (!first) out(",");
            out(names.name(param.getDeclarationModel()));
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
        out("var ", names.name(d), "=");
    }

    private void share(Declaration d) {
        if (isCaptured(d) && !(prototypeStyle && d.isClassOrInterfaceMember())) {
            outerSelf(d);
            out(".", names.name(d), "=", names.name(d), ";");
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
        out(names.name(dec), ";");
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

    private void addInterfaceToPrototype(ClassOrInterface type, InterfaceDefinition interfaceDef) {
        interfaceDefinition(interfaceDef);
        Interface d = interfaceDef.getDeclarationModel();
        out("$proto$.", names.name(d), "=", names.name(d), ";");
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

        out(function, names.name(d), "(");
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
        out("$proto$.", names.name(d), "=", names.name(d), ";");
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

        out(function, names.name(d), "(");
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
            String parentSuffix = "";
            if (extType != null) {
                TypeDeclaration parentTypeDecl = extType.getType().getDeclarationModel();
                if (declaredInCL(parentTypeDecl)) {
                    return;
                }
                parentSuffix = names.memberSuffix(parentTypeDecl);
            }

            final List<Declaration> decs = new ArrayList<Declaration>();
            new SuperVisitor(decs).visit(body);
            for (Declaration dec: decs) {
                if (dec instanceof Value) {
                    superGetterRef(dec,d,parentSuffix);
                    if (((Value) dec).isVariable()) {
                        superSetterRef(dec,d,parentSuffix);
                    }
                }
                else if (dec instanceof Getter) {
                    superGetterRef(dec,d,parentSuffix);
                    if (((Getter) dec).isVariable()) {
                        superSetterRef(dec,d,parentSuffix);
                    }
                }
                else {
                    superRef(dec,d,parentSuffix);
                }
            }
        }
    }

    private void callSuperclass(ExtendedType extendedType, Class d, Node that) {
        if (extendedType!=null) {
            qualify(that, extendedType.getType().getDeclarationModel());
            out(names.name(extendedType.getType().getDeclarationModel()), "(");
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
                out(names.name(st.getDeclarationModel()), "(");
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

        Declaration d = type.getDeclarationModel();
        if (type instanceof ObjectDefinition) {
            d = ((ObjectDefinition) type).getDeclarationModel().getTypeDeclaration();
        }
        out(clAlias, ".initType(", names.name(d), ",'",
            type.getDeclarationModel().getQualifiedNameString(), "'");

        if (extendedType != null) {
            out(",", initFunctionName(extendedType.getType()));
        } else if (!(type instanceof InterfaceDefinition)) {
            out(",", clAlias, ".IdentifiableObject");
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
        constr += names.name(d);
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
            out(")(", names.name(d), ".$$.prototype);");
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
        out(names.name(d), ".$$;");
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
        out(names.name(d), ".$$;");
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
                from = clAlias + ".IdentifiableObject";
                suffix = clAlias + "$IdentifiableObject$";
            } else {
                SimpleType type = extType.getType();
                TypeDeclaration typeDecl = type.getDeclarationModel();
                from = initFunctionName(type);
                suffix = names.memberSuffix(typeDecl);
            }

            out(clAlias, ".inheritProto(", names.name(d), ",", from, ",'", suffix, "'");

        } else {
            out(clAlias, ".inheritProtoI(", names.name(d));
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
        Class c = (Class) d.getTypeDeclaration();
        out("$proto$.", names.name(c), "=", names.name(c), ";");
        endLine();
    }

    @Override
    public void visit(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        if (!(prototypeStyle && d.isClassOrInterfaceMember())) {
            objectDefinition(that);
        } else {
            Class c = (Class) d.getTypeDeclaration();
            comment(that);
            outerSelf(d);
            out(".", names.name(d), "=");
            outerSelf(d);
            out(".", names.name(c), "();");
            endLine();
        }
    }

    private void objectDefinition(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        boolean addToPrototype = prototypeStyle && d.isClassOrInterfaceMember();
        Class c = (Class) d.getTypeDeclaration();
        comment(that);

        out(function, names.name(c), "()");
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
        copyMembersToPrototype(c, that.getExtendedType(), that.getSatisfiedTypes());

        if (!addToPrototype) {
            out("var ", names.name(d), "=",
                    names.name(c), "(new ", names.name(c), ".$$);");
            endLine();
        }

        if (addToPrototype) {
            out("$proto$.", names.getter(d), "=");
        } else if (d.isShared()) {
            outerSelf(d);
            out(".", names.getter(d), "=");
        }
        out(function, names.getter(d), "()");
        beginBlock();
        out("return ");
        if (addToPrototype) {
            out("this.");
        }
        out(names.name(d), ";");
        endBlock();

        addToPrototype(c, that.getClassBody().getStatements());
    }

    private void superRef(Declaration d, Class sub, String parentSuffix) {
        //if (d.isActual()) {
            self(sub);
            out(".", names.name(d), parentSuffix, "=");
            self(sub);
            out(".", names.name(d), ";");
            endLine();
        //}
    }

    private void superGetterRef(Declaration d, Class sub, String parentSuffix) {
        //if (d.isActual()) {
            self(sub);
            out(".", names.getter(d), parentSuffix, "=");
            self(sub);
            out(".", names.getter(d), ";");
            endLine();
        //}
    }

    private void superSetterRef(Declaration d, Class sub, String parentSuffix) {
        //if (d.isActual()) {
            self(sub);
            out(".", names.setter(d), parentSuffix, "=");
            self(sub);
            out(".", names.setter(d), ";");
            endLine();
        //}
    }

    @Override
    public void visit(MethodDeclaration that) {
        if (that.getSpecifierExpression() != null) {
            comment(that);
            out("var ", names.name(that.getDeclarationModel()), "=");
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

        //TODO: if there are multiple parameter lists
        //      do the inner function declarations
        if (that.getParameterLists().size() == 1) {
            out(function, names.name(d));
            ParameterList paramList = that.getParameterLists().get(0);
            paramList.visit(this);
            beginBlock();
            initSelf(that.getBlock());
            initParameters(paramList, null);
            visitStatements(that.getBlock().getStatements(), false);
            endBlock();
        } else {
            int count=0;
            for (ParameterList paramList : that.getParameterLists()) {
                if (count==0) {
                    out(function, names.name(d));
                } else {
                    out("return function");
                }
                paramList.visit(this);
                beginBlock();
                initSelf(that.getBlock());
                initParameters(paramList, null);
                count++;
            }
            visitStatements(that.getBlock().getStatements(), false);
            for (int i=0; i < count; i++) {
                endBlock(i==count-1);
            }
        }


        share(d);
    }

    private void initParameters(ParameterList params, TypeDeclaration typeDecl) {
        for (Parameter param : params.getParameters()) {
            String paramName = names.name(param.getDeclarationModel());
            if (param.getDefaultArgument() != null || param.getDeclarationModel().isSequenced()) {
                out("if(", paramName, "===undefined){", paramName, "=");
                if (param.getDefaultArgument() == null) {
                    out(clAlias, ".empty");
                } else {
                    param.getDefaultArgument().getSpecifierExpression().getExpression().visit(this);
                }
                out(";}");
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
        out("$proto$.", names.name(d), "=");
        methodDefinition(that);
    }

    @Override
    public void visit(AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        out("var ", names.getter(d), "=function()");
        super.visit(that);
        shareGetter(d);
    }

    private void addGetterToPrototype(Declaration outer,
            AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out("$proto$.", names.getter(d), "=",
                function, names.getter(d), "()");
        super.visit(that);
    }

    private void shareGetter(MethodOrValue d) {
        if (isCaptured(d)) {
            outerSelf(d);
            out(".", names.getter(d), "=", names.getter(d), ";");
            endLine();
        }
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        out("var ", names.setter(d.getGetter()), "=function(", names.name(d.getParameter()), ")");
        super.visit(that);
        shareSetter(d);
    }

    private void addSetterToPrototype(Declaration outer,
            AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (!prototypeStyle || !d.isClassOrInterfaceMember()) return;
        comment(that);
        String setterName = names.setter(d.getGetter());
        out("$proto$.", setterName, "=",
                function, setterName, "(", names.name(d.getParameter()), ")");
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
            out(".", names.setter(d), "=", names.setter(d), ";");
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
                    out(".", names.name(d), "=");
                    super.visit(that);
                    out(";");
                    endLine();
                }
            }
            else {
                String varName = names.name(d);
                out("var ", varName);
                if (that.getSpecifierOrInitializerExpression()!=null) {
                    out("=");
                    int boxType = boxStart(that.getSpecifierOrInitializerExpression().getExpression().getTerm());
                    super.visit(that);
                    boxUnboxEnd(boxType);
                } else {
                    super.visit(that);
                }
                out(";");
                endLine();
                if (isCaptured(d)) {
                    out("var ", names.getter(d),"=function(){return ", varName, ";};");
                    endLine();
                } else {
                    directAccess.add(d);
                }
                shareGetter(d);
                if (d.isVariable()) {
                    String paramVarName = names.createTempVariable(d.getName());
                    out("var ", names.setter(d), "=function(", paramVarName, "){");
                    out(varName, "=", paramVarName, "; return ", varName, ";};");
                    endLine();
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
            out("$proto$.", names.getter(d), "=",
                    function, names.getter(d), "()");
            beginBlock();
            out("return this.", names.name(d), ";");
            endBlock();
            if (d.isVariable()) {
                String paramVarName = names.createTempVariable(d.getName());
                out("$proto$.", names.setter(d), "=");
                out(function, names.setter(d), "(", paramVarName, ")");
                beginBlock();
                out("this.", names.name(d), "=", paramVarName, "; return ", paramVarName, ";");
                endBlock();
            }
        }
    }

    @Override
    public void visit(CharLiteral that) {
        out(clAlias, ".Character(");
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

        out(clAlias, ".String(");
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
        out(clAlias, ".StringBuilder().appendAll(", clAlias, ".ArraySequence([");
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
        out(clAlias, ".Float(", that.getText(), ")");
    }

    @Override
    public void visit(NaturalLiteral that) {
        out(clAlias, ".Integer(", that.getText(), ")");
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
        if (isNative(decl)) {
            out(decl.getName());
        } else if (accessDirectly(decl)) {
                out(names.name(decl));
        } else {
            out(names.getter(decl));
            out("()");
        }
    }
    
    private boolean accessDirectly(Declaration d) {
        return !accessThroughGetter(d) || directAccess.contains(d);
    }

    private boolean accessThroughGetter(Declaration d) {
        return !((d instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter)
                  || (d instanceof Method));
    }
    
    private static boolean isNative(Term t) {
        if (t instanceof MemberOrTypeExpression) {
            return isNative(((MemberOrTypeExpression)t).getDeclaration());
        }
        return false;
    }
    
    private static boolean isNative(Declaration d) {
        return hasAnnotationByName(getToplevel(d), "nativejs");
    }

    private static Declaration getToplevel(Declaration d) {
        while (!d.isToplevel()) {
            Scope s = d.getContainer();
            // Skip any non-declaration elements
            while (!(s instanceof Declaration)) {
                s = s.getContainer();
            }
            d = (Declaration) s;
        }
        return d;
    }

    private static boolean hasAnnotationByName(Declaration d, String name){
        for(com.redhat.ceylon.compiler.typechecker.model.Annotation annotation : d.getAnnotations()){
            if(annotation.getName().equals(name))
                return true;
        }
        return false;
    }
    
    @Override
    public void visit(QualifiedMemberExpression that) {
        //Big TODO: make sure the member is actually
        //          refined by the current class!
        if (that.getMemberOperator() instanceof SafeMemberOp) {

            if (that.getDeclaration() instanceof Method) {
                String tmp=names.createTempVariable();
                out("(function(){var ", tmp, "=");
                super.visit(that);
                out("; return ", clAlias, ".JsCallable(", tmp, ",", tmp, "===null?null:", tmp, ".");
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
        String tmplist = names.createTempVariable("lst");
        out("var ", tmplist, "=[];"); endLine();
        //Get an iterator
        String iter = names.createTempVariable("it");
        out("var ", iter, "=");
        super.visit(that);
        out(".getIterator();"); endLine();
        //Iterate
        String elem = names.createTempVariable("elem");
        out("var ", elem, ";"); endLine();
        out("while ((", elem, "=", iter, ".next())!==", clAlias, ".getExhausted())");
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
        out("return ", clAlias);
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
        out(";return ", clAlias, ".JsCallable($, $.");
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
                 postfix = names.memberSuffix(parentType);
             }
        }
        if (isNative(that.getDeclaration())) {
            out(that.getDeclaration().getName());
        } else if (!accessThroughGetter(that.getDeclaration())) {
            out(names.name(that.getDeclaration()), postfix);
        }
        else {
            out(names.getter(that.getDeclaration()));
            out(postfix);
            out("()");
        }
    }

    @Override
    public void visit(BaseTypeExpression that) {
        qualify(that, that.getDeclaration());
        out(names.name(that.getDeclaration()));
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
        out(".", names.name(that.getDeclaration()));
    }

    @Override
    public void visit(InvocationExpression that) {
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
                                out(clAlias, ".empty");
                            } else if (p.isSequenced() || argNames.contains(p.getName())) {
                                out(names.name(p));
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
            super.visit(that);
        }
    }

    @Override
    public void visit(PositionalArgumentList that) {
        out("(");
        if (!that.getPositionalArguments().isEmpty()) {
            boolean first=true;
            boolean sequenced=false;
            for (PositionalArgument arg: that.getPositionalArguments()) {
                if (arg.getParameter() == null) {
                    //This is temporary, typechecker will give us parameters for multiple parameter lists eventually
                    that.addError("Multiple parameter lists cannot be invoked yet.");
                    return;
                }
                if (!first) out(",");
                int boxType = boxUnboxStart(arg.getExpression().getTerm(), arg.getParameter());
                if (!sequenced && arg.getParameter().isSequenced() && that.getEllipsis() == null) {
                    sequenced=true;
                    out(clAlias, ".ArraySequence([");
                }
                arg.visit(this);
                boxUnboxEnd(boxType);
                first = false;
            }
            if (sequenced) {
                out("])");
            }
        }
        out(")");
    }

    // Make sure fromTerm is compatible with toTerm by boxing it when necessary
    private int boxStart(Term fromTerm) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = false;
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }
    
    // Make sure fromTerm is compatible with toTerm by boxing or unboxing it when necessary
    private int boxUnboxStart(Term fromTerm, Term toTerm) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = isNative(toTerm);
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }
    
    // Make sure fromTerm is compatible with toDecl by boxing or unboxing it when necessary
    private int boxUnboxStart(Term fromTerm, com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration toDecl) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = isNative(toDecl);
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }
    
    private int boxUnboxStart(boolean fromNative, ProducedType fromType, boolean toNative) {
        if (fromNative != toNative) {
            // Box the value
            if (fromNative) {
                if (fromType.getProducedTypeQualifiedName().equals("ceylon.language.String")) {
                    out(clAlias, ".String(");
                } else if (fromType.getProducedTypeQualifiedName().equals("ceylon.language.Integer")) {
                    out(clAlias, ".Integer(");
                } else if (fromType.getProducedTypeQualifiedName().equals("ceylon.language.Float")) {
                    out(clAlias, ".Float(");
                } else if (fromType.getProducedTypeQualifiedName().equals("ceylon.language.Boolean")) {
                    out(clAlias, ".Boolean(");
                } else if (fromType.getProducedTypeQualifiedName().equals("ceylon.language.Character")) {
                    out(clAlias, ".Character(");
                } else {
                    return 0;
                }
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }

    private void boxUnboxEnd(int boxType) {
        if (boxType== 1) {
            out(")");
        } else if (boxType== 2) {
            out(".value");
        }
    }
    
    @Override
    public void visit(NamedArgumentList that) {
        for (NamedArgument arg: that.getNamedArguments()) {
            out("var ", names.name(arg.getParameter()), "=");
            arg.visit(this);
            out(";");
        }
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg!=null) {
            out("var ", names.name(sarg.getParameter()), "=");
            sarg.visit(this);
            out(";");
        }
    }

    @Override
    public void visit(SequencedArgument that) {
        out(clAlias, ".ArraySequence([");
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
        out(clAlias, ".ArraySequence([");
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
        String svar = names.name(bme.getDeclaration());
        //    if (!(prototypeStyle && bme.getDeclaration().isClassOrInterfaceMember())) {
        //        out("$");
        //    }
        out(svar, "=");
        that.getSpecifierExpression().visit(this);
    }

    @Override
    public void visit(AssignOp that) {
        boolean paren=false;
        if (that.getLeftTerm() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getLeftTerm();
            qualify(that, bme.getDeclaration());
            if (isNative(bme.getDeclaration())) {
                out(bme.getDeclaration().getName());
                out("=");
            } else {
                out(names.setter(bme.getDeclaration()));
                out("(");
                paren = !(bme.getDeclaration() instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter);
            }
        } else if (that.getLeftTerm() instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression qme = (QualifiedMemberExpression)that.getLeftTerm();
            super.visit(qme);
            if (isNative(qme.getDeclaration())) {
                out(".", qme.getDeclaration().getName());
                out("=");
            } else {
                out(".", names.setter(qme.getDeclaration()));
                out("(");
                paren = true;
            }
        }
        int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
        that.getRightTerm().visit(this);
        boxUnboxEnd(boxType);
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
            return names.packageAlias(d.getUnit().getPackage());
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
                        path += names.self((TypeDeclaration) scope);
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
                    return names.self((TypeDeclaration)d.getContainer());
                }
                else {
                    //an inherited declaration that might be
                    //inherited by an outer scope
                    return names.self(id);
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
                    generateCallable(term, names.name(term.getDeclaration()));
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
        out(names.self(d));
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

    private boolean declaredInCL(Declaration typeDecl) {
        return typeDecl.getUnit().getPackage().getQualifiedNameString()
                .startsWith("ceylon.language");
    }

    @Override
    public void visit(SumOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".plus(");
                termgen.right();
                out(")");
            }
        });
    }

    @Override
    public void visit(DifferenceOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".minus(");
                termgen.right();
                out(")");
            }
        });
    }

    @Override
    public void visit(ProductOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".times(");
                termgen.right();
                out(")");
            }
        });
    }

    @Override
    public void visit(QuotientOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".divided(");
                termgen.right();
                out(")");
            }
        });
    }

    @Override public void visit(RemainderOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".remainder(");
                termgen.right();
                out(")");
            }
        });
    }

    @Override public void visit(PowerOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".power(");
                termgen.right();
                out(")");
            }
        });
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
            Declaration lhsDecl = lhsBME.getDeclaration();
            String lhsPath = qualifiedPath(lhsBME, lhsDecl);
            if (lhsPath.length() > 0) {
                lhsPath += '.';
            }

            String svar = accessDirectly(lhsDecl)
                    ? names.name(lhsDecl) : (names.getter(lhsDecl)+"()");
            out("(", lhsPath, names.setter(lhsDecl), "(", lhsPath,
                    svar, ".", functionName, "(");
            that.getRightTerm().visit(this);
            out(")),", lhsPath, svar, ")");
        } else if (lhs instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression lhsQME = (QualifiedMemberExpression) lhs;
            if (isNative(lhsQME)) {
                // ($1.foo = Box($1.foo).operator($2))
                out("(");
                lhsQME.getPrimary().visit(this);
                out(".", lhsQME.getDeclaration().getName());
                out("=");
                int boxType = boxStart(lhsQME);
                lhsQME.getPrimary().visit(this);
                out(".", lhsQME.getDeclaration().getName());
                boxUnboxEnd(boxType);
                out(".", functionName, "(");
                that.getRightTerm().visit(this);
                out("))");
            } else {
                out("(function($1,$2){var $=");
                out("$1.", names.getter(lhsQME.getDeclaration()), "()");
                out(".", functionName, "($2);");
                out("$1.", names.setter(lhsQME.getDeclaration()), "($)");
                out(";return $}(");
                lhsQME.getPrimary().visit(this);
                out(",");
                that.getRightTerm().visit(this);
                out("))");
            }
        }
    }

    @Override public void visit(NegativeOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                termgen.term();
                out(".getNegativeValue()");
            }
        });
    }

    @Override public void visit(PositiveOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                termgen.term();
                out(".getPositiveValue()");
            }
        });
    }

    @Override public void visit(EqualOp that) {
        leftEqualsRight(that);
    }

    @Override public void visit(NotEqualOp that) {
        leftEqualsRight(that);
        equalsFalse();
    }

    @Override public void visit(NotOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                termgen.term();
                equalsFalse();
            }
        });
    }

    @Override public void visit(IdenticalOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                out("(");
                termgen.left();
                out("===");
                termgen.right();
                thenTrueElseFalse();
                out(")");
            }
        });
    }

    @Override public void visit(CompareOp that) {
        leftCompareRight(that);
    }

    @Override public void visit(SmallerOp that) {
        leftCompareRight(that);
        out(".equals(", clAlias, ".getSmaller())");
    }

    @Override public void visit(LargerOp that) {
        leftCompareRight(that);
        out(".equals(", clAlias, ".getLarger())");
    }

    @Override public void visit(SmallAsOp that) {
        out("(");
        leftCompareRight(that);
        out("!==", clAlias, ".getLarger()");
        thenTrueElseFalse();
        out(")");
    }

    @Override public void visit(LargeAsOp that) {
        out("(");
        leftCompareRight(that);
        out("!==", clAlias, ".getSmaller()");
        thenTrueElseFalse();
        out(")");
    }
    /** Outputs the CL equivalent of 'a==b' in JS. */
    private void leftEqualsRight(BinaryOperatorExpression that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".equals(");
                termgen.right();
                out(")");
            }
        });
    }
    /** Outputs the CL equivalent of '==false' in JS. */
    private void equalsFalse() {
        out(".equals(", clFalse, ")");
    }
    /** Outputs the CL equivalent of '?true:false' in JS */
    private void thenTrueElseFalse() {
        out("?", clTrue, ":", clFalse);
    }
    
    interface UnaryOpTermGenerator {
        void term();
    }
    interface UnaryOpGenerator {
        void generate(UnaryOpTermGenerator termgen);
    }
    private void unaryOp(final UnaryOperatorExpression that, final UnaryOpGenerator gen) {
        final GenerateJsVisitor visitor = this;
        gen.generate(new UnaryOpTermGenerator() {
            @Override
            public void term() {
                int boxTypeLeft = boxStart(that.getTerm());
                that.getTerm().visit(visitor);
                boxUnboxEnd(boxTypeLeft);
            }
        });
    }
    
    interface BinaryOpTermGenerator {
        void left();
        void right();
    }
    interface BinaryOpGenerator {
        void generate(BinaryOpTermGenerator termgen);
    }
    private void binaryOp(final BinaryOperatorExpression that, final BinaryOpGenerator gen) {
        final GenerateJsVisitor visitor = this;
        gen.generate(new BinaryOpTermGenerator() {
            @Override
            public void left() {
                int boxTypeLeft = boxStart(that.getLeftTerm());
                that.getLeftTerm().visit(visitor);
                boxUnboxEnd(boxTypeLeft);
            }
            @Override
            public void right() {
                int boxTypeRight = boxStart(that.getRightTerm());
                that.getRightTerm().visit(visitor);
                boxUnboxEnd(boxTypeRight);
            }
        });
    }
    
    /** Outputs the CL equivalent of 'a <=> b' in JS. */
    private void leftCompareRight(BinaryOperatorExpression that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".compare(");
                termgen.right();
                out(")");
            }
        });
    }

   @Override public void visit(AndOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out("(");
               termgen.left();
               out("===", clTrue, "?");
               termgen.right();
               out(":", clFalse, ")");
           }
       });
   }

   @Override public void visit(OrOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out("(");
               termgen.left();
               out("===", clTrue, "?", clTrue, ":");
               termgen.right();
               out(")");
           }
       });
   }

   @Override public void visit(EntryOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out(clAlias, ".Entry(");
               termgen.left();
               out(",");
               termgen.right();
               out(")");
           }
       });
   }

   @Override public void visit(Element that) {
       out(".item(");
       that.getExpression().visit(this);
       out(")");
   }

   @Override public void visit(DefaultOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out("function($){return $!==null?$:");
               termgen.right();
               out("}(");
               termgen.left();
               out(")");
           }
       });
   }

   @Override public void visit(ThenOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out("(");
               termgen.left();
               out("===", clTrue, "?");
               termgen.right();
               out(":null)");
           }
       });
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

           out("(", path, names.setter(bme.getDeclaration()), "(", path);
           if (!accessDirectly(bme.getDeclaration())) {
               String bmeGetter = names.getter(bme.getDeclaration());
               out(bmeGetter, "().", functionName, "()),", path, bmeGetter, "())");
           } else {
               String bmeGetter = names.name(bme.getDeclaration());
               out(bmeGetter, ".", functionName, "()),", path, bmeGetter, ")");
           }
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           out("function($){var $2=$.", names.getter(qme.getDeclaration()), "().",
               functionName, "();$.", names.setter(qme.getDeclaration()), "($2);return $2}(");
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

           out("(function($){", path, names.setter(bme.getDeclaration()), "($.", functionName,
               "());return $}(", path);
           if (!accessDirectly(bme.getDeclaration())) {
               out(names.getter(bme.getDeclaration()), "()))");
           } else {
               out(names.name(bme.getDeclaration()), "))");
           }
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           out("function($){var $2=$.", names.getter(qme.getDeclaration()), "();$.",
                   names.setter(qme.getDeclaration()), "($2.", functionName, "());return $2}(");
           qme.getPrimary().visit(this);
           out(")");
       }
   }

   @Override public void visit(Exists that) {
       unaryOp(that, new UnaryOpGenerator() {
           @Override
           public void generate(UnaryOpTermGenerator termgen) {
               out(clAlias, ".exists(");
               termgen.term();
               out(")");
           }
       });
   }
   @Override public void visit(Nonempty that) {
       unaryOp(that, new UnaryOpGenerator() {
           @Override
           public void generate(UnaryOpTermGenerator termgen) {
               out(clAlias, ".nonempty(");
               termgen.term();
               out(")");
           }
       });
   }

   @Override public void visit(BooleanCondition that) {
       int boxType = boxStart(that.getExpression().getTerm());
       super.visit(that);
       boxUnboxEnd(boxType);
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
           out(")===", clTrue, ")");
           if (ifBlock != null) {
               encloseBlockInFunction(ifBlock);
           }
       }

       if (that.getElseClause() != null) {
           out("else ");
           encloseBlockInFunction(that.getElseClause().getBlock());
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
           out(")===", clTrue, ")");
           encloseBlockInFunction(whileClause.getBlock());
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
       Term variableRHS = variable.getSpecifierExpression().getExpression().getTerm();

       String varName = names.name(variable.getDeclarationModel());
       out("var ", varName, ";");
       endLine();

       out(keyword);
       out("(");
       specialConditionCheck(condition, variableRHS, varName);
       out(")");
       directAccess.add(variable.getDeclarationModel());
       encloseBlockInFunction(block);
   }

    private void specialConditionCheck(Condition condition, Term variableRHS, String varName) {
        if (condition instanceof ExistsOrNonemptyCondition) {
            if (condition instanceof NonemptyCondition) {
                out(clAlias, ".nonempty(");
                specialConditionRHS(variableRHS, varName);
                out(")===", clTrue);
            } else {
                specialConditionRHS(variableRHS, varName);
                out("!==null");
            }

        } else {
            Type type = ((IsCondition) condition).getType();
            generateIsOfType(variableRHS, null, type, varName);
            out("===", clTrue);
        }
    }

    private void specialConditionRHS(Term variableRHS, String varName) {
        if (varName == null) {
            variableRHS.visit(this);
        } else {
            out("(", varName, "=");
            variableRHS.visit(this);
            out(")");
        }
    }

    private void specialConditionRHS(String variableRHS, String varName) {
        if (varName == null) {
            out(variableRHS);
        } else {
            out("(", varName, "=");
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
    private void generateIsOfType(Term term, String termString, Type type, String tmpvar) {
        if (type instanceof SimpleType) {
            out(clAlias, ".isOfType(");
        } else {
            out(clAlias, ".Boolean(", clAlias, ".isOfTypes(");
        }
        if (term != null) {
            specialConditionRHS(term, tmpvar);
        } else {
            specialConditionRHS(termString, tmpvar);
        }
        out(",");

        if (type instanceof SimpleType) {
            out("'");
            out(((SimpleType) type).getDeclarationModel().getQualifiedNameString());
            out("')");
            if (term != null && term.getTypeModel() != null && !term.getTypeModel().getTypeArguments().isEmpty()) {
                out("/* REIFIED GENERICS SOON!!!");
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
        generateIsOfType(that.getTerm(), null, that.getType(), null);
    }

    @Override public void visit(Break that) {
        if (continues.isEmpty()) {
            out("break;");
        } else {
            Continuation top=continues.peek();
            if (that.getScope()==top.getScope()) {
                top.useBreak();
                out(top.getBreakName(), "=true; return;");
            } else {
                out("break;");
            }
        }
    }
    @Override public void visit(Continue that) {
        if (continues.isEmpty()) {
            out("continue;");
        } else {
            Continuation top=continues.peek();
            if (that.getScope()==top.getScope()) {
                top.useContinue();
                out(top.getContinueName(), "=true; return;");
            } else {
                out("continue;");
            }
        }
    }

   @Override public void visit(RangeOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out(clAlias, ".Range(");
               termgen.left();
               out(",");
               termgen.right();
               out(")");
           }
       });
   }

   @Override public void visit(ForStatement that) {
       if (comment) out("//'for' statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
       endLine();
       ForIterator foriter = that.getForClause().getForIterator();
       SpecifierExpression iterable = foriter.getSpecifierExpression();
       boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
       final String iterVar = names.createTempVariable("it");
       final String itemVar;
       if (foriter instanceof ValueIterator) {
           itemVar = names.name(((ValueIterator)foriter).getVariable().getDeclarationModel());
       } else {
           itemVar = names.createTempVariable("item");
       }
       out("var ", iterVar, " = ");
       iterable.visit(this);
       out(".getIterator();");
       endLine();
       out("var ", itemVar, ";while ((", itemVar, "=", iterVar, ".next())!==", clAlias, ".getExhausted())");
       List<Statement> stmnts = that.getForClause().getBlock().getStatements();
       beginBlock();
       if (foriter instanceof ValueIterator) {
           directAccess.add(((ValueIterator)foriter).getVariable().getDeclarationModel());
       } else if (foriter instanceof KeyValueIterator) {
           String keyvar = names.name(((KeyValueIterator)foriter).getKeyVariable().getDeclarationModel());
           String valvar = names.name(((KeyValueIterator)foriter).getValueVariable().getDeclarationModel());
           out("var ", keyvar, "=", itemVar, ".getKey();");
           endLine();
           out("var ", valvar, "=", itemVar, ".getItem();");
           directAccess.add(((KeyValueIterator)foriter).getKeyVariable().getDeclarationModel());
           directAccess.add(((KeyValueIterator)foriter).getValueVariable().getDeclarationModel());
       }
       endLine();
       for (int i=0; i<stmnts.size(); i++) {
           Statement s = stmnts.get(i);
           s.visit(this);
           if (i<stmnts.size()-1 && s instanceof ExecutableStatement) {
               endLine();
           }
       }
       //If there's an else block, check for normal termination
       indentLevel--;
       endLine();
       out("}");
       if (hasElse) {
           endLine();
           out("if (", clAlias, ".getExhausted() === ", itemVar, ")");
           encloseBlockInFunction(that.getElseClause().getBlock());
       }
   }

    public void visit(InOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.right();
                out(".contains(");
                termgen.left();
                out(")");
            }
        });
    }

    @Override public void visit(TryCatchStatement that) {
        out("try");
        encloseBlockInFunction(that.getTryClause().getBlock());

        if (!that.getCatchClauses().isEmpty()) {
            String catchVarName = names.createTempVariable("ex");
            out("catch(", catchVarName, ")");
            beginBlock();
            boolean firstCatch = true;
            for (CatchClause catchClause : that.getCatchClauses()) {
                Variable variable = catchClause.getCatchVariable().getVariable();
                if (!firstCatch) {
                    out("else ");
                }
                firstCatch = false;
                out("if(");
                generateIsOfType(null, catchVarName, variable.getType(), null);
                out("===", clTrue, ")");

                if (catchClause.getBlock().getStatements().isEmpty()) {
                    out("{}");
                } else {
                    beginBlock();
                    directAccess.add(variable.getDeclarationModel());
                    names.forceName(variable.getDeclarationModel(), catchVarName);

                    visitStatements(catchClause.getBlock().getStatements(), false);
                    endBlock();
                }
            }
            out("else{throw ", catchVarName, "}");
            endBlock();
        }

        if (that.getFinallyClause() != null) {
            out("finally");
            encloseBlockInFunction(that.getFinallyClause().getBlock());
        }
    }

    @Override public void visit(Throw that) {
        out("throw ");
        if (that.getExpression() != null) {
            that.getExpression().visit(this);
        } else {
            out(clAlias, ".Exception()");
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
            out(clAlias, ".exists(");
            that.getPrimary().visit(this);
            out(")===", clTrue, "?");
        }
        visitIndex(that);
        if (op instanceof SafeIndexOp) {
            out(":", clAlias, ".getNull()");
        }
    }

    /** Generates code for a case clause, as part of a switch statement. Each case
     * is rendered as an if. */
    private void caseClause(CaseClause cc, String expvar, Term switchTerm) {
        out("if (");
        final CaseItem item = cc.getCaseItem();
        if (item instanceof IsCase) {
            IsCase isCaseItem = (IsCase) item;
            generateIsOfType(null, expvar, isCaseItem.getType(), null);
            out("===", clTrue);
            Variable caseVar = isCaseItem.getVariable();
            if (caseVar != null) {
                directAccess.add(caseVar.getDeclarationModel());
                names.forceName(caseVar.getDeclarationModel(), expvar);
            }
        } else if (item instanceof SatisfiesCase) {
            item.addError("case(satisfies) not yet supported");
            out("true");
        } else if (item instanceof MatchCase){
            boolean first = true;
            for (Expression exp : ((MatchCase)item).getExpressionList().getExpressions()) {
                if (!first) out(" || ");
                out(expvar, "==="); //TODO equality?
                /*out(".equals(");*/
                exp.visit(this);
                //out(")==="); clAlias(); out(".getTrue()");
                first = false;
            }
        } else {
            cc.addUnexpectedError("support for case of type " + cc.getClass().getSimpleName() + " not yet implemented");
        }
        out(") ");
        encloseBlockInFunction(cc.getBlock());
    }

    @Override
    public void visit(SwitchStatement that) {
        if (comment) out("//Switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
        endLine();
        //Put the expression in a tmp var
        final String expvar = names.createTempVariable("switch");
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

    /** Encloses the block in a function, IF NEEDED. */
    private void encloseBlockInFunction(Block block) {
        boolean wrap=encloser.encloseBlock(block);
        if (wrap) {
            beginBlock();
            Continuation c = new Continuation(block.getScope());
            continues.push(c);
            out("var ", c.getContinueName(), "=false;"); endLine();
            out("var ", c.getBreakName(), "=false;"); endLine();
            out("var ", c.getReturnName(), "=(function()");
        }
        block.visit(this);
        if (wrap) {
            Continuation c = continues.pop();
            out("());if(", c.getReturnName(), "!==undefined){return ", c.getReturnName(), ";}");
            if (c.isContinued()) {
                out("else if(", c.getContinueName(),"===true){continue;}");
            }
            if (c.isBreaked()) {
                out("else if (", c.getBreakName(),"===true){break;}");
            }
            endBlock();
        }
    }

    private static class Continuation {
        private static int conts=1;
        private final String cvar = String.format("cntvar$%d", conts);
        private final String rvar = String.format("retvar$%d", conts);
        private final String bvar = String.format("brkvar$%d", conts);
        private final Scope scope;
        private boolean cused, bused;
        public Continuation(Scope scope) {
            this.scope=scope;
            conts++;
        }
        public Scope getScope() { return scope; }
        public String getContinueName() { return cvar; }
        public String getBreakName() { return bvar; }
        public String getReturnName() { return rvar; }
        public void useContinue() { cused = true; }
        public void useBreak() { bused=true; }
        public boolean isContinued() { return cused; }
        public boolean isBreaked() { return bused; } //"isBroken" sounds really really bad in this case
    }

}

