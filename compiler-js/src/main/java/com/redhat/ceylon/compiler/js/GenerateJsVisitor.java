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
import com.redhat.ceylon.compiler.typechecker.model.ImportableScope;
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
    /** This set is meant to store declarations in a comprehension,
     * because the code referencing them needs to be treated differently; similar to the directAccess but it gets cleared
     * after the comprehension has been generated. */
    private final List<Declaration> comprehensions = new ArrayList<Declaration>();
    private final List<String> retainedTempVars = new ArrayList<String>();
    private final Set<Module> importedModules = new HashSet<Module>();

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

    public GenerateJsVisitor(Writer out, boolean prototypeStyle, JsIdentifierNames names) {
        this.out = out;
        this.prototypeStyle=prototypeStyle;
        this.names = names;
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
        if (require(clm)) {
            setCLAlias(names.moduleAlias(clm));
        }
        super.visit(that);
    }

    public void visit(Import that) {
    	ImportableScope scope =
    			that.getImportMemberOrTypeList().getImportList().getImportedScope();
    	if (scope instanceof Package) {
    		require(((Package) scope).getModule());
    	}
    }

    private boolean require(Module mod) {
        if (importedModules.contains(mod)) {
            return false;
        }
        out("var ", names.moduleAlias(mod), "=require('", scriptPath(mod), "');");
        endLine();
        importedModules.add(mod);
        return true;
    }

    private String scriptPath(Module mod) {
        StringBuilder path = new StringBuilder(mod.getNameAsString().replace('.', '/')).append('/');
        if (!mod.isDefault()) {
            path.append(mod.getVersion()).append('/');
        }
        path.append(mod.getNameAsString());
        if (!(mod.isDefault() || mod==mod.getLanguageModule())) {
            path.append('-').append(mod.getVersion());
        }
        return path.toString();
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

            boolean needNewline = s instanceof ExecutableStatement;
            if (!retainedTempVars.isEmpty()) {
                if (needNewline) { endLine(); }
                needNewline = true;
                out("var ");
                boolean first = true;
                for (String varName : retainedTempVars) {
                    if (!first) { out(","); }
                    first = false;
                    out(varName);
                }
                out(";");
                retainedTempVars.clear();
            }

            if (needNewline && (endLastLine || (i<statements.size()-1))) {
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
        share(d, true);
    }

    private void share(Declaration d, boolean excludeProtoMembers) {
        if (!(excludeProtoMembers && prototypeStyle && d.isClassOrInterfaceMember())
                && isCaptured(d)) {
            outerSelf(d);
            out(".", names.name(d), "=", names.name(d), ";");
            endLine();
        }
    }

    @Override
    public void visit(ClassDeclaration that) {
        Class d = that.getDeclarationModel();
        if (prototypeStyle && d.isClassOrInterfaceMember()) return;
        comment(that);
        var(d);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel()
                .getDeclaration();
        qualify(that,dec);
        out(names.name(dec), ";");
        endLine();
        share(d);
    }

    private void addClassDeclarationToPrototype(TypeDeclaration outer, ClassDeclaration that) {
        comment(that);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel().getDeclaration();
        String path = qualifiedPath(that, dec, true);
        if (path.length() > 0) {
            path += '.';
        }
        out(names.self(outer), ".", names.name(that.getDeclarationModel()), "=",
                path, names.name(dec), ";");
        endLine();
    }

    @Override
    public void visit(InterfaceDeclaration that) {
        Interface d = that.getDeclarationModel();
        if (prototypeStyle && d.isClassOrInterfaceMember()) return;
        comment(that);
        var(d);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel()
                .getDeclaration();
        qualify(that,dec);
        out(names.name(dec), ";");
        endLine();
        share(d);
    }

    private void addInterfaceDeclarationToPrototype(TypeDeclaration outer, InterfaceDeclaration that) {
        comment(that);
        TypeDeclaration dec = that.getTypeSpecifier().getType().getTypeModel().getDeclaration();
        String path = qualifiedPath(that, dec, true);
        if (path.length() > 0) {
            path += '.';
        }
        out(names.self(outer), ".", names.name(that.getDeclarationModel()), "=",
                path, names.name(dec), ";");
        endLine();
    }

    private void addInterfaceToPrototype(ClassOrInterface type, InterfaceDefinition interfaceDef) {
        interfaceDefinition(interfaceDef);
        Interface d = interfaceDef.getDeclarationModel();
        out(names.self(type), ".", names.name(d), "=", names.name(d), ";");
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

        typeInitialization(that);
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
        out(names.self(type), ".", names.name(d), "=", names.name(d), ";");
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
        //This takes care of top-level attributes defined before the class definition
        out("$init$", names.name(d), "();");
        endLine();
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

        typeInitialization(that);
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
                parentSuffix = names.typeSuffix(parentTypeDecl);
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

    private void typeInitialization(final Tree.Declaration type) {

        ExtendedType extendedType = null;
        SatisfiedTypes satisfiedTypes = null;
        boolean isInterface = false;
        TypeDeclaration decl = null;
        if (type instanceof ClassDefinition) {
            ClassDefinition classDef = (ClassDefinition) type;
            extendedType = classDef.getExtendedType();
            satisfiedTypes = classDef.getSatisfiedTypes();
            decl = classDef.getDeclarationModel();
        } else if (type instanceof InterfaceDefinition) {
            satisfiedTypes = ((InterfaceDefinition) type).getSatisfiedTypes();
            isInterface = true;
            decl = ((InterfaceDefinition) type).getDeclarationModel();
        } else if (type instanceof ObjectDefinition) {
            ObjectDefinition objectDef = (ObjectDefinition) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = objectDef.getDeclarationModel().getTypeDeclaration();
        }
        final PrototypeInitCallback callback = new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                if (type instanceof ClassDefinition) {
                    addToPrototype(((ClassDefinition)type).getDeclarationModel(), ((ClassDefinition)type).getClassBody().getStatements());
                } else if (type instanceof InterfaceDefinition) {
                    addToPrototype(((InterfaceDefinition)type).getDeclarationModel(), ((InterfaceDefinition)type).getInterfaceBody().getStatements());
                }
            }
        };
        typeInitialization(extendedType, satisfiedTypes, isInterface, type.getDeclarationModel(), decl, callback);
    }

    /** This is now the main method to generate the type initialization code. */
    private void typeInitialization(ExtendedType extendedType, SatisfiedTypes satisfiedTypes, boolean isInterface,
            Declaration type, TypeDeclaration d, PrototypeInitCallback callback) {

        boolean inheritProto = prototypeStyle || (extendedType == null)
                || !declaredInThisPackage(extendedType.getType().getDeclarationModel());
        if (!inheritProto && (satisfiedTypes != null)) {
            for (SimpleType st : satisfiedTypes.getTypes()) {
                if (!declaredInThisPackage(st.getDeclarationModel())) {
                    inheritProto = true;
                    break;
                }
            }
        }
        String initFuncName = inheritProto ? "initTypeProto" : "initType";
        if (isInterface) {
            initFuncName += 'I';
        }

        out("function $init$", names.name(d), "()");
        beginBlock();
        out("if (", names.name(d), ".$$===undefined)");
        beginBlock();
        out(clAlias, ".", initFuncName, "(", names.name(d), ",'",
            type.getQualifiedNameString(), "'");

        if (extendedType != null) {
            out(",", typeFunctionName(extendedType.getType(), false));
        } else if (!isInterface) {
            out(",", clAlias, ".IdentifiableObject");
        }

        if (satisfiedTypes != null) {
            for (SimpleType satType : satisfiedTypes.getTypes()) {
                String fname = typeFunctionName(satType, true);
                //Actually it could be "if not in same module"
                if (declaredInCL(satType.getDeclarationModel())) {
                    out(",", fname);
                } else {
                    int idx = fname.lastIndexOf('.');
                    if (idx > 0) {
                        fname = fname.substring(0, idx+1) + "$init$" + fname.substring(idx+1);
                    } else {
                        fname = "$init$" + fname;
                    }
                    out(",", fname, "()");
                }
            }
        }

        out(");");
        
        //The class definition needs to be inside the init function if we want forwards decls to work in prototype style
        if (prototypeStyle && (d instanceof ClassOrInterface)) {
            endLine();
            callback.addToPrototypeCallback();
        }
        endBlock();
        out("return ", names.name(d), ";");
        endBlock();
        //If it's nested, share the init function
        if (outerSelf(d)) {
            out(".$init$", names.name(d), "=$init$", names.name(d), ";");
            endLine();
        }
        out("$init$", names.name(d), "();");
        endLine();
    }

    private String typeFunctionName(SimpleType type, boolean removeAlias) {
        TypeDeclaration d = type.getDeclarationModel();
        if (removeAlias && d.isAlias()) {
            d = d.getExtendedTypeDeclaration();
        }
        boolean inProto = prototypeStyle
                && (type.getScope().getContainer() instanceof TypeDeclaration);
        String constr = qualifiedPath(type, d, inProto);
        if (constr.length() > 0) {
            constr += '.';
        }
        constr += names.name(d);
        return constr;
    }

    private void addToPrototype(ClassOrInterface d, List<Statement> statements) {
        if (prototypeStyle && !statements.isEmpty()) {
            out("(function(", names.self(d), ")");
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
        } else if (s instanceof ClassDeclaration) {
            addClassDeclarationToPrototype(d, (ClassDeclaration) s);
        } else if (s instanceof InterfaceDeclaration) {
            addInterfaceDeclarationToPrototype(d, (InterfaceDeclaration) s);
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

    private void addObjectToPrototype(ClassOrInterface type, ObjectDefinition objDef) {
        objectDefinition(objDef);
        Value d = objDef.getDeclarationModel();
        Class c = (Class) d.getTypeDeclaration();
        out(names.self(type), ".", names.name(c), "=", names.name(c), ";");
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

        typeInitialization(that);

        addToPrototype(c, that.getClassBody().getStatements());

        if (!addToPrototype) {
            out("var ", names.name(d), "=",
                    names.name(c), "(new ", names.name(c), ".$$);");
            endLine();
        }

        out("var ", names.getter(d), "=function()");
        beginBlock();
        out("return ");
        if (addToPrototype) {
            out("this.");
        }
        out(names.name(d), ";");
        endBlock();
        if (addToPrototype || d.isShared()) {
            outerSelf(d);
            out(".", names.getter(d), "=", names.getter(d), ";");
            endLine();
        }
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
            share(that.getDeclarationModel(), false);
        } else {
            //Check for refinement of simple param declaration
            Method m = that.getDeclarationModel();
            if (m == that.getScope()) {
                if (m.getContainer() instanceof Class && m.isClassOrInterfaceMember()) {
                    //Declare the method just by pointing to the param function
                    final String name = names.name(((Class)m.getContainer()).getParameter(m.getName()));
                    if (name != null) {
                        self((Class)m.getContainer());
                        out(".", names.name(m), "=", name, ";");
                        endLine();
                    }
                } else if (m.getContainer() instanceof Method) {
                    //Declare the function just by forcing the name we used in the param list
                    final String name = names.name(((Method)m.getContainer()).getParameter(m.getName()));
                    if (names != null) {
                        names.forceName(m, name);
                    }
                }
            }
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
            com.redhat.ceylon.compiler.typechecker.model.Parameter pd = param.getDeclarationModel();
            /*if (param instanceof ValueParameterDeclaration && ((ValueParameterDeclaration)param).getDeclarationModel().isHidden()) {
                //TODO support new syntax for class and method parameters
                //the declaration is actually different from the one we usually use
                out("//HIDDEN! ", pd.getName(), "(", names.name(pd), ")"); endLine();
            }*/
            String paramName = names.name(pd);
            if (param.getDefaultArgument() != null || pd.isSequenced()) {
                out("if(", paramName, "===undefined){", paramName, "=");
                if (param.getDefaultArgument() == null) {
                    out(clAlias, ".empty");
                } else {
                    param.getDefaultArgument().getSpecifierExpression().getExpression().visit(this);
                }
                out(";}");
                endLine();
            }
            if ((typeDecl != null) && pd.isCaptured()) {
                self(typeDecl);
                out(".", paramName, "=", paramName, ";");
                endLine();
            }
        }
    }

    private void addMethodToPrototype(TypeDeclaration outer,
            MethodDefinition that) {
        Method d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out(names.self(outer), ".", names.name(d), "=");
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

    private void addGetterToPrototype(TypeDeclaration outer,
            AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out(names.self(outer), ".", names.getter(d), "=",
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

    private void addSetterToPrototype(TypeDeclaration outer,
            AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (!prototypeStyle || !d.isClassOrInterfaceMember()) return;
        comment(that);
        String setterName = names.setter(d.getGetter());
        out(names.self(outer), ".", setterName, "=",
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
        //Check if the attribute corresponds to a class parameter
        //This is because of the new initializer syntax
        String classParam = null;
        if (that.getScope() instanceof Functional) {
            classParam = names.name(((Functional)that.getScope()).getParameter(d.getName()));
        }
        if (!d.isFormal()) {
            comment(that);
            if (prototypeStyle && d.isClassOrInterfaceMember()) {
                if (that.getSpecifierOrInitializerExpression()!=null) {
                    outerSelf(d);
                    out(".", names.name(d), "=");
                    super.visit(that);
                    out(";");
                    endLine();
                } else if (classParam != null) {
                    outerSelf(d);
                    out(".", names.name(d), "=", classParam, ";");
                    endLine();
                }
            }
            else {
                final String varName = names.name(d);
                out("var ", varName);
                if (that.getSpecifierOrInitializerExpression()!=null) {
                    out("=");
                    int boxType = boxStart(that.getSpecifierOrInitializerExpression().getExpression().getTerm());
                    super.visit(that);
                    boxUnboxEnd(boxType);
                } else if (classParam != null) {
                    out("=", classParam);
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
                    out("var ", names.setter(d), "=function(", paramVarName, "){return ");
                    out(varName, "=", paramVarName, ";};");
                    endLine();
                    shareSetter(d);
                }
            }
        }
    }

    private void addGetterAndSetterToPrototype(TypeDeclaration outer,
            AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        if (!prototypeStyle||d.isToplevel()) return;
        if (!d.isFormal()) {
            comment(that);
            out(names.self(outer), ".", names.getter(d), "=",
                    function, names.getter(d), "()");
            beginBlock();
            out("return this.", names.name(d), ";");
            endBlock();
            if (d.isVariable()) {
                String paramVarName = names.createTempVariable(d.getName());
                out(names.self(outer), ".", names.setter(d), "=");
                out(function, names.setter(d), "(", paramVarName, ")");
                beginBlock();
                out("return this.", names.name(d), "=", paramVarName, ";");
                endBlock();
            }
        }
    }

    @Override
    public void visit(CharLiteral that) {
        out(clAlias, ".Character(");
        out(String.valueOf(that.getText().codePointAt(1)));
        out(")");
    }

    @Override
    public void visit(StringLiteral that) {

        StringBuilder text = new StringBuilder(that.getText());
        final int slen = text.codePointCount(1, text.length()-1);
        //Escape special chars
        for (int i=1; i < text.length()-1;i++) {
            switch(text.charAt(i)) {
            case 8:text.replace(i, i+1, "\\b"); i++; break;
            case 9:text.replace(i, i+1, "\\t"); i++; break;
            case 10:text.replace(i, i+1, "\\n"); i++; break;
            case 12:text.replace(i, i+1, "\\f"); i++; break;
            case 13:text.replace(i, i+1, "\\r"); i++; break;
            case 34:text.replace(i, i+1, "\\\""); i++; break;
            case 39:text.replace(i, i+1, "\\'"); i++; break;
            case 92:text.replace(i, i+1, "\\\\"); i++; break;
            }
        }

        out(clAlias, ".String(", text.toString(), ",", Integer.toString(slen), ")");
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
        } else if (comprehensions.contains(decl)) {
            out("this.", names.name(decl));
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

    /** Returns true if the top-level declaration for the term is annotated "nativejs" */
    private static boolean isNative(Term t) {
        if (t instanceof MemberOrTypeExpression) {
            return isNative(((MemberOrTypeExpression)t).getDeclaration());
        }
        return false;
    }

    /** Returns true if the declaration is annotated "nativejs" */
    private static boolean isNative(Declaration d) {
        return hasAnnotationByName(getToplevel(d), "nativejs");
    }

    private static Declaration getToplevel(Declaration d) {
        while (d != null && !d.isToplevel()) {
            Scope s = d.getContainer();
            // Skip any non-declaration elements
            while (s != null && !(s instanceof Declaration)) {
                s = s.getContainer();
            }
            d = (Declaration) s;
        }
        return d;
    }

    private static boolean hasAnnotationByName(Declaration d, String name){
        if (d != null) {
            for(com.redhat.ceylon.compiler.typechecker.model.Annotation annotation : d.getAnnotations()){
                if(annotation.getName().equals(name))
                    return true;
            }
        }
        return false;
    }

    private void generateSafeOp(QualifiedMemberOrTypeExpression that) {
        boolean isMethod = that.getDeclaration() instanceof Method;
        String lhsVar = createRetainedTempVar("opt");
        out("(", lhsVar, "=");
        super.visit(that);
        out(",");
        if (isMethod) {
            out(clAlias, ".JsCallable(", lhsVar, ",");
        }
        out(lhsVar, "!==null?", lhsVar, ".");
        qualifiedMemberRHS(that);
        out(":null)");
        if (isMethod) {
            out(")");
        }
    }

    @Override
    public void visit(QualifiedMemberExpression that) {
        //Big TODO: make sure the member is actually
        //          refined by the current class!
        if (that.getMemberOperator() instanceof SafeMemberOp) {

            generateSafeOp(that);
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
        String primaryVar = createRetainedTempVar("opt");
        out("(", primaryVar, "=");
        that.getPrimary().visit(this);
        out(",", clAlias, ".JsCallable(", primaryVar, ",", primaryVar, "!==null?",
                primaryVar, ".");
        if (name == null) {
            qualifiedMemberRHS(that);
        } else {
            out(name);
        }
        out(":null))");
    }

    private void qualifiedMemberRHS(QualifiedMemberOrTypeExpression that) {
        boolean sup = that.getPrimary() instanceof Super;
        String postfix = "";
        if (sup) {
             ClassOrInterface type = Util.getContainingClassOrInterface(that.getScope());
             ClassOrInterface parentType = type.getExtendedTypeDeclaration();
             if (parentType != null) {
                 postfix = names.typeSuffix(parentType);
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
            if (that instanceof QualifiedTypeExpression) {
                //Nothing at the moment
            } else {
                out("()");
            }
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
        if (that.getMemberOperator() instanceof SafeMemberOp) {
            generateCallable(that, names.name(that.getDeclaration()));
        } else {
            super.visit(that);
            out(".", names.name(that.getDeclaration()));
        }
    }

    @Override
    public void visit(InvocationExpression that) {
        if (that.getNamedArgumentList()!=null) {
            out("(function (){");
            that.getNamedArgumentList().visit(this);
            out("return ");
            that.getPrimary().visit(this);
            if (that.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
                if (mte.getDeclaration() instanceof Functional) {
                    Functional f = (Functional) mte.getDeclaration();
                    for (com.redhat.ceylon.compiler.typechecker.model.ParameterList plist : f.getParameterLists()) {
                        List<String> argNames = that.getNamedArgumentList().getNamedArgumentList().getArgumentNames();
                        boolean first=true;
                        out("(");
                        for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : plist.getParameters()) {
                            if (!first) out(",");
                            if (p.isSequenced() && that.getNamedArgumentList().getSequencedArgument()==null && that.getNamedArgumentList().getNamedArguments().isEmpty()) {
                                if (that.getNamedArgumentList().getComprehension() == null) {
                                    out(clAlias, ".empty");
                                } else {
                                    out("$$$comp$$$");
                                }
                            } else if (p.isSequenced() || argNames.contains(p.getName())) {
                                out(names.name(p));
                            } else {
                                out("undefined");
                            }
                            first = false;
                        }
                        out(")");
                    }
                }
            }
            out("}())");
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
                if (!first) out(",");
                int boxType = boxUnboxStart(arg.getExpression().getTerm(), arg.getParameter());
                if (!sequenced && arg.getParameter() != null && arg.getParameter().isSequenced() && that.getEllipsis() == null) {
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
        if (that.getComprehension() != null) {
            that.getComprehension().visit(this);
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
    public void visit(ObjectArgument that) {
        final Class c = (Class)that.getDeclarationModel().getTypeDeclaration();
        
        out("(function()");
        beginBlock();
        out("//ObjectArgument ", that.getIdentifier().getText());
        location(that);
        endLine();
        out(function, names.name(c), "()");
        beginBlock();
        instantiateSelf(c);
        referenceOuter(c);
        ExtendedType xt = that.getExtendedType();
        final ClassBody body = that.getClassBody();
        SatisfiedTypes sts = that.getSatisfiedTypes();
        callSuperclass(xt, c, that);
        copySuperMembers(xt, body, c);
        callInterfaces(sts, c, that);
        body.visit(this);
        returnSelf(c);
        indentLevel--;
        endLine();
        out("}");
        endLine();
        
        typeInitialization(xt, sts, false, that.getDeclarationModel(), c, new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                addToPrototype(c, body.getStatements());
            }
        });
        out("return ", names.name(c), "(new ", names.name(c), ".$$);");
        endBlock();
        out("());");
    }

    @Override
    public void visit(AttributeArgument that) {
        out("(function()");
        beginBlock();
        out("//AttributeArgument ", that.getParameter().getName());
        location(that);
        endLine();
        visitStatements(that.getBlock().getStatements(), false);
        endBlock();
        out("());");
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
        if (that.getComprehension() != null) {
            out("var $$$comp$$$=");
            that.getComprehension().visit(this);
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
        if (that.getComprehension() != null) {
            that.getComprehension().visit(this);
        } else if (that.getSequencedArgument() != null) {
            out(clAlias, ".ArraySequence([");
            boolean first=true;
            for (Expression arg: that.getSequencedArgument().getExpressionList().getExpressions()) {
                if (!first) out(",");
                arg.visit(this);
                first = false;
            }
            out("])");
        } else {
            out(clAlias, ".empty");
        }
    }

    @Override
    public void visit(Comprehension that) {
        out("(function()"); beginBlock();
        out("//Comprehension");
        location(that);
        endLine();
        final String compName = names.createTempVariable("compr");
        out("function ", compName, "()"); beginBlock();
        out("var $cmp$=new ", compName, ".$$;"); endLine();
        out(clAlias, ".IdentifiableObject($cmp$);"); endLine();
        comprehensions.clear();
        //Get the first clause to start getting into the other ones
        ComprehensionClause clause = that.getForComprehensionClause();
        int idx = 0;
        ExpressionComprehensionClause excc = null;
        while (clause != null) {
            idx++;
            if (clause instanceof ForComprehensionClause) {
                ForComprehensionClause fcl = (ForComprehensionClause)clause;
                SpecifierExpression specexpr = fcl.getForIterator().getSpecifierExpression();
                if (clause == that.getForComprehensionClause()) {
                    //The first iterator can be initialized without problems
                    out("$cmp$.iter", Integer.toString(idx), "=");
                    specexpr.visit(this);
                    out(".getIterator();");
                    endLine();
                } else {
                    //The subsequent iterators need to be inside a function, in case they depend on the outer current element
                    out("$cmp$.getIter", Integer.toString(idx), "=function()"); beginBlock();
                    if (true) {
                        out("if(this.", names.name(comprehensions.get(comprehensions.size()-1)), "===undefined)this.next$", Integer.toString(idx-1), "();"); endLine();
                    }
                    out("return ");
                    specexpr.visit(this);
                    out(".getIterator();");
                    endBlock(false); out(";");
                    endLine();
                }
                String itemVar = null;
                if (fcl.getForIterator() instanceof ValueIterator) {
                    Value item = ((ValueIterator)fcl.getForIterator()).getVariable().getDeclarationModel();
                    comprehensions.add(item);
                    itemVar = names.name(item);
                } else if (fcl.getForIterator() instanceof KeyValueIterator) {
                    itemVar = String.format("item$%d", idx);
                    KeyValueIterator kviter = (KeyValueIterator)fcl.getForIterator();
                    out("$cmp$.", names.getter(kviter.getKeyVariable().getDeclarationModel()), "=function(){return this.", itemVar, ".getKey();}");
                    endLine();
                    out("$cmp$.", names.getter(kviter.getValueVariable().getDeclarationModel()), "=function(){return this,", itemVar, ".getItem();}");
                } else {
                    that.addError("No support yet for iterators of type " + fcl.getForIterator().getClass().getName());
                    return;
                }
                endLine();
                //Now the context for this iterator
                out("$cmp$.next$", Integer.toString(idx), "=function()"); beginBlock();
                if (idx>1) {
                    out("if(this.iter", Integer.toString(idx), "===undefined)this.iter", Integer.toString(idx),
                            "=this.getIter", Integer.toString(idx), "();");
                    endLine();
                }
                if (fcl.getForIterator() instanceof ValueIterator) {
                    out("this.", names.name(comprehensions.get(comprehensions.size()-1)), "=this.iter", Integer.toString(idx), ".next();");
                } else {
                    out("this.item$", Integer.toString(idx), "=this.iter", Integer.toString(idx), ".next();");
                }
                endLine();
                out("if(this.", itemVar, "===", clAlias, ".getExhausted())"); beginBlock();
                if (idx>1) {
                    //Add an inner check after the first context
                    out("if(this.next$", Integer.toString(idx-1), "())"); beginBlock();
                    out("this.iter", Integer.toString(idx), "=this.getIter", Integer.toString(idx), "();"); endLine();
                    out("this.", names.name(comprehensions.get(comprehensions.size()-1)), "=this.iter", Integer.toString(idx), ".next();"); endLine();
                    out("return this.", names.name(comprehensions.get(comprehensions.size()-1)), "!==", clAlias, ".getExhausted();");
                    endBlock();
                }
                out("return false;");
                endBlock();
                out("return true;");
                endBlock(false); out(";"); endLine();
                clause = fcl.getComprehensionClause();
            } else if (clause instanceof IfComprehensionClause) {
                Condition cond = ((IfComprehensionClause)clause).getCondition();
                //The context of an if is an iteration through the parent, checking each element against the condition
                out("$cmp$.next$", Integer.toString(idx), "=function()");beginBlock();
                Variable var = null;
                if (cond instanceof IsCondition || cond instanceof ExistsOrNonemptyCondition) {
                    var = cond instanceof IsCondition ? ((IsCondition)cond).getVariable()
                            : ((ExistsOrNonemptyCondition)cond).getVariable();
                    comprehensions.add(var.getDeclarationModel());
                    //Initialize the condition's attribute to finished so that this is returned
                    //in case the condition is not met and the iterator is exhausted
                    out("this.", names.name(var.getDeclarationModel()), "=", clAlias, ".getExhausted();");
                    endLine();
                }
                out("while(this.next$", Integer.toString(idx-1), "() && !(");
                if (cond instanceof IsCondition || cond instanceof ExistsOrNonemptyCondition) {
                    specialConditionCheck(cond, var.getSpecifierExpression().getExpression().getTerm(),
                            "this."+names.name(var.getDeclarationModel()));
                } else {
                    cond.visit(this);
                    out("===", clTrue);
                }
                out("));"); endLine();
                //Remove the condition's attribute to generate the return statement with the original attribute
                //If we generate the return statement checking if the condition's attribute is exhausted, it returns
                //the wrong result if the condition was not met with the last element.
                if (var!=null) {
                    comprehensions.remove(comprehensions.size()-1);
                }
                out("return this.", names.name(comprehensions.get(comprehensions.size()-1)), "!==", clAlias, ".getExhausted();");
                //Add back the condition's attribute if present
                if (var!=null) {
                    comprehensions.add(var.getDeclarationModel());
                }
                endBlock(false); out(";"); endLine();
                clause = ((IfComprehensionClause)clause).getComprehensionClause();
            } else if (clause instanceof ExpressionComprehensionClause) {
                //Just keep a ref to the expression
                excc = (ExpressionComprehensionClause)clause;
                clause = null;
            } else {
                that.addError("No support for comprehension clause of type " + clause.getClass().getName());
                return;
            }
        }
        //Implement Iterator.next()
        out("$cmp$.next=function()"); beginBlock();
        out("if(this.next$", Integer.toString(idx-1), "())"); beginBlock();
        //The expression
        out("return ");
        excc.getExpression().visit(this);
        out(";");
        endBlock(false);
        out("else return ", clAlias, ".getExhausted();");
        endBlock(false); out(";"); endLine();
        //Return the new object
        out("return $cmp$;");
        endBlock();
        //Initialize this iterable
        out(clAlias, ".initTypeProto(", compName, ", 'ceylon.language.ComprehensionIterator', ", clAlias, ".IdentifiableObject, ", clAlias, ".Iterator);");
        endLine();
        //Create the Iterable and return it
        out("return ", clAlias, ".Comprehension(", compName, "());");
        endBlock(false);
        out("())");
    }
    
    @Override
    public void visit(SpecifierStatement that) {
        BaseMemberExpression bme = (Tree.BaseMemberExpression) that.getBaseMemberExpression();
        if (prototypeStyle) {
            qualify(that, bme.getDeclaration());
        }
        String svar = names.name(bme.getDeclaration());
        out(svar, "=");
        that.getSpecifierExpression().visit(this);
    }

    @Override
    public void visit(AssignOp that) {
        boolean paren=false;
        String returnValue = null;
        if (that.getLeftTerm() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getLeftTerm();
            Declaration bmeDecl = bme.getDeclaration();
            boolean simpleSetter = hasSimpleGetterSetter(bmeDecl);
            if (!simpleSetter) {
                out("(");
            }
            String path = qualifiedPath(that, bmeDecl);
            if (path.length() > 0) { path += '.'; }
            out(path);
            if (isNative(bme.getDeclaration())) {
                out(bmeDecl.getName());
                out("=");
            } else {
                out(names.setter(bmeDecl));
                out("(");
                if (!simpleSetter) {
                    returnValue = accessDirectly(bmeDecl)
                            ? (path + names.name(bmeDecl))
                            : (path + names.getter(bmeDecl) + "()");
                }
                paren = true;//!(bmeDecl instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter);
            }
        } else if (that.getLeftTerm() instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression qme = (QualifiedMemberExpression)that.getLeftTerm();
            boolean simpleSetter = hasSimpleGetterSetter(qme.getDeclaration());
            String lhsVar = null;
            if (!simpleSetter) {
                lhsVar = createRetainedTempVar();
                out("(", lhsVar, "=");
                super.visit(qme);
                out(",", lhsVar);
                paren=true;
            } else {
                super.visit(qme);
            }
            if (isNative(qme.getDeclaration())) {
                out(".", qme.getDeclaration().getName());
                out("=");
            } else {
                out(".", names.setter(qme.getDeclaration()));
                out("(");
                if (!simpleSetter) {
                    returnValue = lhsVar + "." + names.getter(qme.getDeclaration()) + "()";
                }
                paren = true;
            }
        }
        int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
        that.getRightTerm().visit(this);
        boxUnboxEnd(boxType);
        if (paren) {
            out(")");
        }
        if (returnValue != null) {
            out(",", returnValue, ")");
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
            return names.moduleAlias(d.getUnit().getPackage().getModule());
        }
        else if (prototypeStyle && !inProto) {
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
//                if (inProto) {
//                    while ((scope != null) && (scope instanceof TypeDeclaration)) {
//                        scope = scope.getContainer();
//                    }
//                }
                if ((scope != null) && ((that instanceof ClassDeclaration)
                                        || (that instanceof InterfaceDeclaration))) {
                    // class/interface aliases have no own "this"
                    scope = scope.getContainer();
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
        else if (d != null && (d.isShared() || inProto) && d.isClassOrInterfaceMember()) {
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
        return "";
    }

    private boolean isImported(Node that, Declaration d) {
        if (d == null) {
            return false;
        }
        Package p1 = d.getUnit().getPackage();
        Package p2 = that.getUnit().getPackage();
        return !p1.equals(p2);
    }

    @Override
    public void visit(ExecutableStatement that) {
        super.visit(that);
        out(";");
    }
    
    /** Creates a new temporary variable which can be used immediately, even
     * inside an expression. The declaration for that temporary variable will be
     * emitted after the current Ceylon statement has been completely processed.
     * The resulting code is valid because JavaScript variables may be used before
     * they are declared. */
    private String createRetainedTempVar(String baseName) {
        String varName = names.createTempVariable(baseName);
        retainedTempVars.add(varName);
        return varName;
    }
    private String createRetainedTempVar() {
        return createRetainedTempVar("tmp");
    }

//    @Override
//    public void visit(Expression that) {
//        if (that.getTerm() instanceof QualifiedMemberOrTypeExpression) {
//            QualifiedMemberOrTypeExpression term = (QualifiedMemberOrTypeExpression) that.getTerm();
//            // References to methods of types from other packages always need
//            // special treatment, even if prototypeStyle==false, because they
//            // may have been generated in prototype style. In particular,
//            // ceylon.language is always in prototype style.
//            if ((term.getDeclaration() instanceof Functional)
//                    && (prototypeStyle || !declaredInThisPackage(term.getDeclaration()))) {
//                if (term.getMemberOperator() instanceof SpreadOp) {
//                    generateSpread(term);
//                } else {
//                    generateCallable(term, names.name(term.getDeclaration()));
//                }
//                return;
//            }
//        }
//        super.visit(that);
//    }

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

    private boolean outerSelf(Declaration d) {
        if (d.isToplevel()) {
            out("exports");
            return true;
        }
        else if (d.isClassOrInterfaceMember()) {
            self((TypeDeclaration)d.getContainer());
            return true;
        }
        return false;
    }

    private boolean declaredInCL(Declaration decl) {
        return decl.getUnit().getPackage().getQualifiedNameString()
                .startsWith("ceylon.language");
    }

    private boolean declaredInThisPackage(Declaration decl) {
        return decl.getUnit().getPackage().equals(root.getUnit().getPackage());
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

            boolean simpleSetter = hasSimpleGetterSetter(lhsDecl);
            String svar = accessDirectly(lhsDecl)
                    ? names.name(lhsDecl) : (names.getter(lhsDecl)+"()");
            if (!simpleSetter) { out("("); }
            out(lhsPath, names.setter(lhsDecl), "(", lhsPath,
                    svar, ".", functionName, "(");
            that.getRightTerm().visit(this);
            out("))");
            if (!simpleSetter) {
                out(",", lhsPath, svar, ")");
            }
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
                String lhsPrimaryVar = createRetainedTempVar();
                String member = names.getter(lhsQME.getDeclaration()) + "()";
                out("(", lhsPrimaryVar, "=");
                lhsQME.getPrimary().visit(this);
                out(",", lhsPrimaryVar, ".", names.setter(lhsQME.getDeclaration()), "(",
                        lhsPrimaryVar, ".", member, ".", functionName, "(");
                that.getRightTerm().visit(this);
                out("))");
                if (!hasSimpleGetterSetter(lhsQME.getDeclaration())) {
                    out(",", lhsPrimaryVar, ".", member);
                }
                out(")");
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
               String lhsVar = createRetainedTempVar("opt");
               out("(", lhsVar, "=");
               termgen.left();
               out(",", lhsVar, "!==null?", lhsVar, ":");
               termgen.right();
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

   private boolean hasSimpleGetterSetter(Declaration decl) {
       return !((decl instanceof Getter) || (decl instanceof Setter) || decl.isFormal()); 
   }
   
   private void prefixIncrementOrDecrement(Term term, String functionName) {
       if (term instanceof BaseMemberExpression) {
           BaseMemberExpression bme = (BaseMemberExpression) term;
           String path = qualifiedPath(bme, bme.getDeclaration());
           if (path.length() > 0) {
               path += '.';
           }

           boolean simpleSetter = hasSimpleGetterSetter(bme.getDeclaration());
           String member = accessDirectly(bme.getDeclaration())
                   ? names.name(bme.getDeclaration())
                   : (names.getter(bme.getDeclaration()) + "()");
           if (!simpleSetter) { out("("); }
           out(path, names.setter(bme.getDeclaration()), "(", path, member, ".",
                   functionName, "())");
           if (!simpleSetter) {
               out(",", path, member, ")");
           }
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           String member = names.getter(qme.getDeclaration()) + "()";
           String primaryVar = createRetainedTempVar();
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",", primaryVar, ".", names.setter(qme.getDeclaration()), "(",
                   primaryVar, ".", member, ".", functionName, "())");
           if (!hasSimpleGetterSetter(qme.getDeclaration())) {
               out(",", primaryVar, ".", member);
           }
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

           String oldValueVar = createRetainedTempVar("old" + bme.getDeclaration().getName());
           out("(", oldValueVar, "=", path);
           if (!accessDirectly(bme.getDeclaration())) {
               out(names.getter(bme.getDeclaration()), "()");
           } else {
               out(names.name(bme.getDeclaration()));
           }
           out(",", path, names.setter(bme.getDeclaration()), "(",
                   oldValueVar, ".", functionName, "()),",
                   oldValueVar, ")");
           
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           String primaryVar = createRetainedTempVar();
           String oldValueVar = createRetainedTempVar("old" + qme.getDeclaration().getName());
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",", oldValueVar, "=",
                   primaryVar, ".", names.getter(qme.getDeclaration()), "(),",
                   primaryVar, ".", names.setter(qme.getDeclaration()), "(",
                   oldValueVar, ".", functionName, "()),",
                   oldValueVar, ")");
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

   /** Handles the "is", "exists" and "nonempty" conditions */
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
       if (block != null) {
           encloseBlockInFunction(block);
       }
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

    /** Appends an object with the type's type and list of union/intersection types. */
    private void getTypeList(StaticType type) {
        out("{ t:'");
        if (type instanceof IntersectionType) {
            out("i");
        } else {
            out("u");
        }
        out("', l:[");
        if (type instanceof OptionalType) {
        	out("'ceylon.language.Nothing',");
        	typeNameOrList(((OptionalType) type).getDefiniteType());
        } else {
	        List<StaticType> types = type instanceof UnionType
	        		? ((UnionType)type).getStaticTypes()
	        		: ((IntersectionType)type).getStaticTypes();
	        boolean first = true;
	        for (StaticType t : types) {
	            if (!first) out(",");
	            typeNameOrList(t);
	            first = false;
	        }
        }
        out("]}");
    }
    
    private void typeNameOrList(StaticType type) {
    	if (type instanceof SimpleType) {
            out("'");
            out(((SimpleType) type).getDeclarationModel().getQualifiedNameString());
            out("'");
        } else if (type instanceof EntryType) {
        	out("'ceylon.language.Entry'"); //TODO: type parameters
        } else {
            getTypeList(type);
        }
    }

    /** Generates js code to check if a term is of a certain type. We solve this in JS by
     * checking against all types that Type satisfies (in the case of union types, matching any
     * type will do, and in case of intersection types, all types must be matched).
     * @param term The term that is to be checked against a type
     * @param termString (optional) a string to be used as the term to be checked
     * @param type The type to check against
     * @param tmpvar (optional) a variable to which the term is assigned
     */
    private void generateIsOfType(Term term, String termString, Type type, String tmpvar) {
        if ((type instanceof SimpleType) || (type instanceof EntryType))  {
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
        } else if (type instanceof EntryType) {
        	out("'ceylon.language.Entry')"); //TODO: type parameters
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
        if (comment) {
            out("//'for' statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            if (that.getExits()) out("//EXITS!");
            endLine();
        }
        ForIterator foriter = that.getForClause().getForIterator();
        final String itemVar = generateForLoop(foriter);

        boolean hasElse = that.getElseClause() != null && !that.getElseClause().getBlock().getStatements().isEmpty();
        visitStatements(that.getForClause().getBlock().getStatements(), false);
        //If there's an else block, check for normal termination
        endBlock(false);
        if (hasElse) {
            endLine();
            out("if (", clAlias, ".getExhausted() === ", itemVar, ")");
            encloseBlockInFunction(that.getElseClause().getBlock());
        }
    }

    /** Generates code for the beginning of a "for" loop, returning the name of the variable used for the item. */
    private String generateForLoop(ForIterator that) {
        SpecifierExpression iterable = that.getSpecifierExpression();
        final String iterVar = names.createTempVariable("it");
        final String itemVar;
        if (that instanceof ValueIterator) {
            itemVar = names.name(((ValueIterator)that).getVariable().getDeclarationModel());
        } else {
            itemVar = names.createTempVariable("item");
        }
        out("var ", iterVar, " = ");
        iterable.visit(this);
        out(".getIterator();");
        endLine();
        out("var ", itemVar, ";while ((", itemVar, "=", iterVar, ".next())!==", clAlias, ".getExhausted())");
        beginBlock();
        if (that instanceof ValueIterator) {
            directAccess.add(((ValueIterator)that).getVariable().getDeclarationModel());
        } else if (that instanceof KeyValueIterator) {
            String keyvar = names.name(((KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            String valvar = names.name(((KeyValueIterator)that).getValueVariable().getDeclarationModel());
            out("var ", keyvar, "=", itemVar, ".getKey();");
            endLine();
            out("var ", valvar, "=", itemVar, ".getItem();");
            directAccess.add(((KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            directAccess.add(((KeyValueIterator)that).getValueVariable().getDeclarationModel());
            endLine();
        }
        return itemVar;
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

    /** Generates the code for an anonymous function defined inside an argument list. */
    @Override
    public void visit(final FunctionArgument that) {
        generateParameterLists(that.getParameterLists(), new ParameterListCallback() {
            @Override
            public void completeFunction() {
                out("{return ");
                that.getExpression().visit(GenerateJsVisitor.this);
                out("}");
            }
        });
    }

    /** Generates the code for a function in a named argument list. */
    @Override
    public void visit(final MethodArgument that) {
        generateParameterLists(that.getParameterLists(), new ParameterListCallback() {
            @Override
            public void completeFunction() {
                that.getBlock().visit(GenerateJsVisitor.this);
            }
        });
    }

    /** Generates the code for single or multiple parameter lists, with a callback function to generate the function blocks. */
    private void generateParameterLists(List<ParameterList> plist, ParameterListCallback callback) {
        if (plist.size() == 1) {
            out(function);
            ParameterList paramList = plist.get(0);
            paramList.visit(this);
            callback.completeFunction();
        } else {
            int count=0;
            for (ParameterList paramList : plist) {
                if (count==0) {
                    out(function);
                } else {
                    out("return function");
                }
                paramList.visit(this);
                out("{");
                count++;
            }
            callback.completeFunction();
            for (int i=0; i < count; i++) {
                endBlock(i==count-1);
            }
        }
    }

    /** Encloses the block in a function, IF NEEDED. */
    private void encloseBlockInFunction(Block block) {
        boolean wrap=encloser.encloseBlock(block);
        if (wrap) {
            beginBlock();
            Continuation c = new Continuation(block.getScope(), names);
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
        private final String cvar;
        private final String rvar;
        private final String bvar;
        private final Scope scope;
        private boolean cused, bused;
        public Continuation(Scope scope, JsIdentifierNames names) {
            this.scope=scope;
            cvar = names.createTempVariable("cntvar");
            rvar = names.createTempVariable("retvar");
            bvar = names.createTempVariable("brkvar");
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

    private static interface ParameterListCallback {
        void completeFunction();
    }
    /** This interface is used inside type initialization method. */
    private interface PrototypeInitCallback {
        void addToPrototypeCallback();
    }

}

