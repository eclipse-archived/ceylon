package com.redhat.ceylon.compiler.js;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisWarning;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.ImportableScope;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.InterfaceAlias;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.*;

public class GenerateJsVisitor extends Visitor
        implements NaturalVisitor {

    private boolean indent=true;
    private boolean comment=true;
    private boolean verbose=false;
    private final Stack<Continuation> continues = new Stack<Continuation>();
    private final EnclosingFunctionVisitor encloser = new EnclosingFunctionVisitor();
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess = new HashSet<Declaration>();
    private final RetainedVars retainedVars = new RetainedVars();
    private final Map<String, String> importedModules;
    final ConditionGenerator conds;
    private final InvocationGenerator invoker;
    private final List<CommonToken> tokens;

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
        
        @Override
        public void visit(BaseMemberOrTypeExpression that) {
            if (that.getSupertypeQualifier() != null) {
                decs.add(that.getDeclaration());
            }
            super.visit(that);
        }

        @Override
        public void visit(QualifiedType that) {
            if (that.getOuterType() instanceof SuperType) {
                decs.add(that.getDeclarationModel());
            }
            super.visit(that);
        }

        public void visit(Tree.ClassOrInterface qe) {
            //don't recurse
            if (qe instanceof ClassDefinition) {
                ExtendedType extType = ((ClassDefinition) qe).getExtendedType();
                if (extType != null) { super.visit(extType); }
            }
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

    private final TypeUtils types;
    private final Writer out;
    private final boolean prototypeStyle;
    private CompilationUnit root;
    private static String clAlias="";
    private static final String function="function ";
    private boolean needIndent = true;
    private int indentLevel = 0;

    private static void setCLAlias(String alias) {
        clAlias = alias + ".";
    }
    /** Returns the module name for the language module. */
    static String getClAlias() { return clAlias; }

    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }

    public GenerateJsVisitor(Writer out, boolean prototypeStyle, JsIdentifierNames names,
            List<CommonToken> tokens, Map<String,String> imports, TypeUtils typeUtils) {
        this.out = out;
        this.prototypeStyle=prototypeStyle;
        this.names = names;
        conds = new ConditionGenerator(this, names, directAccess);
        this.tokens = tokens;
        importedModules = imports;
        types = typeUtils;
        invoker = new InvocationGenerator(this, names, retainedVars);
    }

    TypeUtils getTypeUtils() { return types; }

    /** Tells the receiver whether to add comments to certain declarations. Default is true. */
    public void setAddComments(boolean flag) { comment = flag; }
    public boolean isAddComments() { return comment; }
    /** Tells the receiver whether to indent the generated code. Default is true. */
    public void setIndent(boolean flag) { indent = flag; }
    /** Tells the receiver to be verbose (prints generated code to STDOUT in addition to writer) */
    public void setVerbose(boolean flag) { verbose = flag; }

    /** Returns the helper component to handle naming. */
    JsIdentifierNames getNames() { return names; }

    /** Print generated code to the Writer specified at creation time.
     * Automatically prints indentation first if necessary.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    void out(String code, String... codez) {
        try {
            if (indent && needIndent) {
                for (int i=0;i<indentLevel;i++) {
                    out.write("    ");
                }
            }
            needIndent = false;
            out.write(code);
            for (String s : codez) {
                out.write(s);
            }
            if (verbose) {
                System.out.print(code);
                for (String s : codez) {
                    System.out.print(s);
                }
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("Generating JS code", ioe);
        }
    }

    /** Prints a newline. Indentation will automatically be printed by {@link #out(String, String...)}
     * when the next line is started. */
    void endLine() {
        endLine(false);
    }
    /** Prints a newline. Indentation will automatically be printed by {@link #out(String, String...)}
     * when the next line is started.
     * @param semicolon  if <code>true</code> then a semicolon is printed at the end
     *                  of the previous line*/
    void endLine(boolean semicolon) {
        if (semicolon) { out(";"); }
        out("\n");
        needIndent = true;
    }
    /** Calls {@link #endLine()} if the current position is not already the beginning
     * of a line. */
    void beginNewLine() {
        if (!needIndent) { endLine(); }
    }

    /** Increases indentation level, prints opening brace and newline. Indentation will
     * automatically be printed by {@link #out(String, String...)} when the next line is started. */
    void beginBlock() {
        indentLevel++;
        out("{");
        endLine();
    }

    /** Decreases indentation level, prints a closing brace in new line (using
     * {@link #beginNewLine()}) and calls {@link #endLine()}. */
    void endBlockNewLine() {
        endBlock(false, true);
    }
    /** Decreases indentation level, prints a closing brace in new line (using
     * {@link #beginNewLine()}) and calls {@link #endLine()}.
     * @param semicolon  if <code>true</code> then prints a semicolon after the brace*/
    void endBlockNewLine(boolean semicolon) {
        endBlock(semicolon, true);
    }
    /** Decreases indentation level and prints a closing brace in new line (using
     * {@link #beginNewLine()}). */
    void endBlock() {
        endBlock(false, false);
    }
    /** Decreases indentation level and prints a closing brace in new line (using
     * {@link #beginNewLine()}).
     * @param semicolon  if <code>true</code> then prints a semicolon after the brace
     * @param newline  if <code>true</code> then additionally calls {@link #endLine()} */
    void endBlock(boolean semicolon, boolean newline) {
        indentLevel--;
        beginNewLine();
        out(semicolon ? "};" : "}");
        if (newline) { endLine(); }
    }

    /** Prints source code location in the form "at [filename] ([location])" */
    void location(Node node) {
        out(" at ", node.getUnit().getFilename(), " (", node.getLocation(), ")");
    }

    @Override
    public void visit(CompilationUnit that) {
        root = that;
        Module clm = that.getUnit().getPackage().getModule()
                .getLanguageModule();
        if (!JsCompiler.compilingLanguageModule) {
            require(clm);
            setCLAlias(names.moduleAlias(clm));
        }

        for (CompilerAnnotation ca: that.getCompilerAnnotations()) {
            ca.visit(this);
        }
        if (that.getImportList() != null) {
            that.getImportList().visit(this);
        }
        visitStatements(that.getDeclarations());
    }

    public void visit(Import that) {
    	ImportableScope scope =
    			that.getImportMemberOrTypeList().getImportList().getImportedScope();
    	if (scope instanceof Package) {
    		require(((Package) scope).getModule());
    	}
    }

    private void require(Module mod) {
        final String path = scriptPath(mod);
        final String modAlias = names.moduleAlias(mod);
        if (importedModules.put(path, modAlias) == null) {
            out("var ", modAlias, "=require('", path, "');");
            endLine();
        }
    }

    private String scriptPath(Module mod) {
        StringBuilder path = new StringBuilder(mod.getNameAsString().replace('.', '/')).append('/');
        if (!mod.isDefault()) {
            path.append(mod.getVersion()).append('/');
        }
        path.append(mod.getNameAsString());
        if (!mod.isDefault()) {
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
        boolean ptypes = false;
        //Check if this is the first parameter list
        if (that.getScope() instanceof Method && that.getModel().isFirst()) {
            ptypes = ((Method)that.getScope()).getTypeParameters() != null && 
                    !((Method)that.getScope()).getTypeParameters().isEmpty();
        }
        for (Parameter param: that.getParameters()) {
            if (!first) out(",");
            out(names.name(param.getDeclarationModel()));
            first = false;
        }
        if (ptypes) {
            if (!first) out(",");
            out("$$$mptypes");
        }
        out(")");
    }

    private void visitStatements(List<? extends Statement> statements) {
        List<String> oldRetainedVars = retainedVars.reset(null);

        for (int i=0; i<statements.size(); i++) {
            Statement s = statements.get(i);
            s.visit(this);
            beginNewLine();
            retainedVars.emitRetainedVars(this);
        }
        retainedVars.reset(oldRetainedVars);
    }

    @Override
    public void visit(Body that) {
        visitStatements(that.getStatements());
    }

    @Override
    public void visit(Block that) {
        List<Statement> stmnts = that.getStatements();
        if (stmnts.isEmpty()) {
            out("{}");
        }
        else {
            beginBlock();
            initSelf(that);
            visitStatements(stmnts);
            endBlock();
        }
    }

    private void initSelf(Block block) {
        initSelf(block.getScope());
    }
    private void initSelf(Scope scope) {
        if ((prototypeOwner != null) &&
                    ((scope instanceof MethodOrValue)
                     || (scope instanceof TypeDeclaration)
                     || (scope instanceof Specification))) {
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

    private boolean share(Declaration d) {
        return share(d, true);
    }

    private boolean share(Declaration d, boolean excludeProtoMembers) {
        boolean shared = false;
        if (!(excludeProtoMembers && prototypeStyle && d.isClassOrInterfaceMember())
                && isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            out(".", names.name(d), "=", names.name(d), ";");
            endLine();
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(ClassDeclaration that) {
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) {
            //But warnings are ok
            for (Message err : that.getErrors()) {
                if (!(err instanceof AnalysisWarning)) {
                    return;
                }
            }
        }
        Class d = that.getDeclarationModel();
        if (prototypeStyle && d.isClassOrInterfaceMember()) return;
        comment(that);
        out(function, names.name(d), "(");
        //Generate each parameter because we need to append one at the end
        for (Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        self(d);
        out(")");
        ExtendedType ext = that.getExtendedType();
        TypeDeclaration aliased = ext.getType().getDeclarationModel();
        out("{return ");
        qualify(ext.getType(), aliased);
        out(names.name(aliased), "(");
        if (ext.getInvocationExpression().getPositionalArgumentList() != null) {
            ext.getInvocationExpression().getPositionalArgumentList().visit(this);
            if (!ext.getInvocationExpression().getPositionalArgumentList().getPositionalArguments().isEmpty()) {
                out(",");
            }
        } else {
            out("/*PENDIENTE*/");
        }
        self(d);
        out(");}");
        endLine();
        out(names.name(d), ".$$=");
        qualify(ext, aliased);
        out(names.name(aliased), ".$$;");
        endLine();
        share(d);
    }

    private void addClassDeclarationToPrototype(TypeDeclaration outer, ClassDeclaration that) {
        comment(that);
        TypeDeclaration dec = that.getExtendedType().getType().getTypeModel().getDeclaration();
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
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
        Interface d = that.getDeclarationModel();
        if (prototypeStyle && d.isClassOrInterfaceMember()) return;
        //It's pointless declaring interface aliases outside of classes/interfaces
        Scope scope = that.getScope();
        if (scope instanceof InterfaceAlias) {
            scope = scope.getContainer();
            if (!(scope instanceof ClassOrInterface)) return;
        }
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
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
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
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!prototypeStyle) {
            new SuperVisitor(superDecs).visit(that.getInterfaceBody());
        }
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs);
        that.getInterfaceBody().visit(this);
        //returnSelf(d);
        endBlockNewLine();
        share(d);

        typeInitialization(that);
    }

    private void addClassToPrototype(ClassOrInterface type, ClassDefinition classDef) {
        classDefinition(classDef);
        Class d = classDef.getDeclarationModel();
        out(names.self(type), ".", names.name(d), "=", names.name(d), ";");
        endLine();
    }

    @Override
    public void visit(ClassDefinition that) {
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
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
        boolean withTargs = that.getTypeParameterList() != null &&
                !that.getTypeParameterList().getTypeParameterDeclarations().isEmpty();
        if (withTargs) {
            out("$$targs$$,");
        }
        self(d);
        out(")");
        beginBlock();
        //This takes care of top-level attributes defined before the class definition
        out("$init$", names.name(d), "();");
        endLine();
        declareSelf(d);
        if (withTargs) {
            out(clAlias, "set_type_args(");
            self(d); out(",$$targs$$);"); endLine();
        } else {
            //Check if any of the satisfied types have type arguments
            if (that.getSatisfiedTypes() != null) {
                for(Tree.StaticType sat : that.getSatisfiedTypes().getTypes()) {
                    boolean first = true;
                    Map<TypeParameter,ProducedType> targs = sat.getTypeModel().getTypeArguments();
                    if (targs != null && !targs.isEmpty()) {
                        if (first) {
                            self(d); out(".$$targs$$=");
                            TypeUtils.printTypeArguments(that, targs, this);
                            endLine(true);
                        } else {
                            out("/*TODO: more type arguments*/");
                            endLine();
                        }
                    }
                }
            }
        }
        referenceOuter(d);
        initParameters(that.getParameterList(), d);
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!prototypeStyle) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        callSuperclass(that.getExtendedType(), d, that, superDecs);
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs);
        
        that.getClassBody().visit(this);
        returnSelf(d);
        endBlockNewLine();
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

    private void copySuperMembers(TypeDeclaration typeDecl, final List<Declaration> decs, ClassOrInterface d) {
        if (!prototypeStyle) {
            for (Declaration dec: decs) {
                if (!typeDecl.isMember(dec)) { continue; }
                String suffix = names.scopeSuffix(dec.getContainer());
                if (dec instanceof Value) {
                    superGetterRef(dec,d,suffix);
                    if (((Value) dec).isVariable()) {
                        superSetterRef(dec,d,suffix);
                    }
                }
                else if (dec instanceof Getter) {
                    superGetterRef(dec,d,suffix);
                    if (((Getter) dec).isVariable()) {
                        superSetterRef(dec,d,suffix);
                    }
                }
                else {
                    superRef(dec,d,suffix);
                }
            }
        }
    }

    private void callSuperclass(ExtendedType extendedType, Class d, Node that,
                final List<Declaration> superDecs) {
        if (extendedType!=null) {
            TypeDeclaration typeDecl = extendedType.getType().getDeclarationModel();
            List<PositionalArgument> argList = extendedType.getInvocationExpression()
                    .getPositionalArgumentList().getPositionalArguments();
            
            qualify(that, typeDecl);
            out(memberAccessBase(extendedType.getType(), names.name(typeDecl), false),
                    (prototypeStyle && (getSuperMemberScope(extendedType.getType()) != null))
                        ? ".call(this," : "(");
            
            for (PositionalArgument arg: argList) {
                arg.visit(this);
                out(",");
            }
            //If the supertype has type arguments, add them to the call
            if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                extendedType.getType().getTypeArgumentList().getTypeModels();
                TypeUtils.printTypeArguments(that, TypeUtils.matchTypeParametersWithArguments(typeDecl.getTypeParameters(),
                        extendedType.getType().getTypeArgumentList().getTypeModels()), this);
                out(",");
            }
            self(d);
            out(");");
            endLine();
           
            copySuperMembers(typeDecl, superDecs, d);
        }
    }

    private void callInterfaces(SatisfiedTypes satisfiedTypes, ClassOrInterface d, Node that,
            final List<Declaration> superDecs) {
        if (satisfiedTypes!=null) {
            for (StaticType st: satisfiedTypes.getTypes()) {
                TypeDeclaration typeDecl = st.getTypeModel().getDeclaration();
                if (typeDecl.isAlias()) {
                    typeDecl = typeDecl.getExtendedTypeDeclaration();
                }
                qualify(that, typeDecl);
                out(names.name((ClassOrInterface)typeDecl), "(");
                self(d);
                out(");");
                endLine();
                //Set the reified types from interfaces
                Map<TypeParameter, ProducedType> reifs = st.getTypeModel().getTypeArguments();
                if (reifs != null && !reifs.isEmpty()) {
                    for (Map.Entry<TypeParameter, ProducedType> e : reifs.entrySet()) {
                        if (e.getValue().getDeclaration() instanceof ClassOrInterface) {
                            out(clAlias, "add_type_arg(");
                            self(d);
                            out(",'", e.getKey().getName(), "',");
                            TypeUtils.typeNameOrList(that, e.getValue(), this, true);
                            out(");");
                            endLine();
                        }
                    }
                }
                copySuperMembers(typeDecl, superDecs, d);
            }
        }
    }

    /** Generates a function to initialize the specified type. */
    private void typeInitialization(final Tree.Declaration type) {

        ExtendedType extendedType = null;
        SatisfiedTypes satisfiedTypes = null;
        boolean isInterface = false;
        ClassOrInterface decl = null;
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
            decl = (ClassOrInterface)objectDef.getDeclarationModel().getTypeDeclaration();
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
        typeInitialization(extendedType, satisfiedTypes, isInterface, decl, callback);
    }

    /** This is now the main method to generate the type initialization code.
     * @param extendedType The type that is being extended.
     * @param satisfiedTypes The types satisfied by the type being initialized.
     * @param isInterface Tells whether the type being initialized is an interface
     * @param d The declaration for the type being initialized
     * @param callback A callback to add something more to the type initializer in prototype style.
     */
    private void typeInitialization(ExtendedType extendedType, SatisfiedTypes satisfiedTypes, boolean isInterface,
            ClassOrInterface d, PrototypeInitCallback callback) {

        //Let's always use initTypeProto to avoid #113
        String initFuncName = "initTypeProto";

        out("function $init$", names.name(d), "()");
        beginBlock();
        out("if (", names.name(d), ".$$===undefined)");
        beginBlock();
        String qns = d.getQualifiedNameString();
        if (JsCompiler.compilingLanguageModule && qns.indexOf("::") < 0) {
            //Language module files get compiled in default module
            //so they need to have this added to their qualified name
            qns = "ceylon.language::" + qns;
        }
        out(clAlias, initFuncName, "(", names.name(d), ",'", qns, "'");

        if (extendedType != null) {
            out(",", typeFunctionName(extendedType.getType(), false));
        } else if (!isInterface) {
            out(",", clAlias, "Basic");
        }

        if (satisfiedTypes != null) {
            for (StaticType satType : satisfiedTypes.getTypes()) {
                TypeDeclaration tdec = satType.getTypeModel().getDeclaration();
                if (tdec.isAlias()) {
                    tdec = tdec.getExtendedTypeDeclaration();
                }
                String fname = typeFunctionName(satType, true);
                //Actually it could be "if not in same module"
                if (!JsCompiler.compilingLanguageModule && declaredInCL(tdec)) {
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
        if (prototypeStyle) {
            endLine();
            callback.addToPrototypeCallback();
        }
        endBlockNewLine();
        out("return ", names.name(d), ";");
        endBlockNewLine();
        //If it's nested, share the init function
        if (outerSelf(d)) {
            out(".$init$", names.name(d), "=$init$", names.name(d), ";");
            endLine();
        }
        out("$init$", names.name(d), "();");
        endLine();
    }

    private String typeFunctionName(StaticType type, boolean removeAlias) {
        TypeDeclaration d = type.getTypeModel().getDeclaration();
        if (removeAlias && d.isAlias()) {
            d = d.getExtendedTypeDeclaration();
        }
        boolean inProto = prototypeStyle
                && (type.getScope().getContainer() instanceof TypeDeclaration);
        String constr = qualifiedPath(type, d, inProto);
        if (constr.length() > 0) {
            constr += '.';
        }
        constr += memberAccessBase(type, names.name(d), false);
        return constr;
    }

    private void addToPrototype(ClassOrInterface d, List<Statement> statements) {
        if (prototypeStyle && !statements.isEmpty()) {
            out("(function(", names.self(d), ")");
            beginBlock();
            for (Statement s: statements) {
                addToPrototype(d, s);
            }
            endBlock();
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
        } else if (s instanceof MethodDeclaration) {
            methodDeclaration(d, (MethodDeclaration) s);
        } else if (s instanceof AttributeGetterDefinition) {
            addGetterToPrototype(d, (AttributeGetterDefinition)s);
        } else if (s instanceof AttributeSetterDefinition) {
            addSetterToPrototype(d, (AttributeSetterDefinition)s);
        } else if (s instanceof AttributeDeclaration) {
            addGetterAndSetterToPrototype(d, (AttributeDeclaration) s);
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
        } else if (s instanceof SpecifierStatement) {
            addSpecifierToPrototype(d, (SpecifierStatement) s);
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
            out("this.", names.name(d), ".$$;");
        } else {
            out(names.name(d), ".$$;");
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
        out("=new ");
        if (prototypeStyle && d.isClassOrInterfaceMember()) {
            out("this.", names.name(d), ".$$;");
        } else {
            out(names.name(d), ".$$;");
        }
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
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
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
        comment(that);
        Value d = that.getDeclarationModel();
        boolean addToPrototype = prototypeStyle && d.isClassOrInterfaceMember();
        Class c = (Class) d.getTypeDeclaration();

        out(function, names.name(c), "()");
        beginBlock();
        instantiateSelf(c);
        referenceOuter(c);
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!prototypeStyle) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        callSuperclass(that.getExtendedType(), c, that, superDecs);
        callInterfaces(that.getSatisfiedTypes(), c, that, superDecs);
        
        that.getClassBody().visit(this);
        returnSelf(c);
        indentLevel--;
        endLine();
        out("}");
        endLine();

        typeInitialization(that);

        addToPrototype(c, that.getClassBody().getStatements());

        if (!addToPrototype) {
            out("var ", names.name(d), "=", names.name(c), "();");
            endLine();
        }

        out("var ", names.getter(d), "=function()");
        beginBlock();
        out("return ");
        if (addToPrototype) {
            out("this.");
        }
        out(names.name(d), ";");
        endBlockNewLine();
        if (addToPrototype || d.isShared()) {
            outerSelf(d);
            out(".", names.getter(d), "=", names.getter(d), ";");
            endLine();
        }
    }

    private void superRef(Declaration d, ClassOrInterface sub, String parentSuffix) {
        //if (d.isActual()) {
            self(sub);
            out(".", names.name(d), parentSuffix, "=");
            self(sub);
            out(".", names.name(d), ";");
            endLine();
        //}
    }

    private void superGetterRef(Declaration d, ClassOrInterface sub, String parentSuffix) {
        //if (d.isActual()) {
            self(sub);
            out(".", names.getter(d), parentSuffix, "=");
            self(sub);
            out(".", names.getter(d), ";");
            endLine();
        //}
    }

    private void superSetterRef(Declaration d, ClassOrInterface sub, String parentSuffix) {
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
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
        methodDeclaration(null, that);
    }
    
    private void methodDeclaration(TypeDeclaration outer, MethodDeclaration that) {
        Method m = that.getDeclarationModel();
        if (that.getSpecifierExpression() != null) {
            // method(params) => expr
            if (outer == null) {
                // Not in a prototype definition. Null to do here if it's a
                // member in prototype style.
                if (prototypeStyle && m.isMember()) { return; }
                comment(that);
                out("var ");
            }
            else {
                // prototype definition
                comment(that);
                out(names.self(outer), ".");
            }
            out(names.name(m), "=");            
            singleExprFunction(that.getParameterLists(),
                    that.getSpecifierExpression().getExpression(), that.getScope());
            endLine(true);
            share(m);
        }
        else if (outer == null) { // don't do the following in a prototype definition
            //Check for refinement of simple param declaration
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
        Method d = that.getDeclarationModel();
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
        if (!((prototypeStyle && that.getDeclarationModel().isClassOrInterfaceMember()) || isNative(d))) {
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
            visitStatements(that.getBlock().getStatements());
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
            visitStatements(that.getBlock().getStatements());
            for (int i=0; i < count; i++) {
                endBlock();
            }
        }

        if (!share(d)) { out(";"); }
    }

    private void initParameters(ParameterList params, TypeDeclaration typeDecl) {
        for (final Parameter param : params.getParameters()) {
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
                    out(clAlias, "empty");
                } else {
                    final SpecifierExpression defaultExpr =
                            param.getDefaultArgument().getSpecifierExpression();
                    if ((param instanceof FunctionalParameterDeclaration)
                            && (defaultExpr instanceof LazySpecifierExpression)) {
                        // function parameter defaulted using "=>"
                        singleExprFunction(
                                ((FunctionalParameterDeclaration) param).getParameterLists(),
                                defaultExpr.getExpression(), null);
                    }
                    else {
                        defaultExpr.visit(this);
                    }
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
        if (!shareGetter(d)) { out(";"); }
    }

    private void addGetterToPrototype(TypeDeclaration outer,
            AttributeGetterDefinition that) {
        Getter d = that.getDeclarationModel();
        if (!prototypeStyle||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out(names.self(outer), ".", names.getter(d), "=",
                function, names.getter(d), "()");
        super.visit(that);
        out(";");
    }

    /** Exports a getter function; useful in non-prototype style. */
    private boolean shareGetter(MethodOrValue d) {
        boolean shared = false;
        if (isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            out(".", names.getter(d), "=", names.getter(d), ";");
            endLine();
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        Setter d = that.getDeclarationModel();
        if (prototypeStyle&&d.isClassOrInterfaceMember()) return;
        comment(that);
        out("var ", names.setter(d.getGetter()), "=function(", names.name(d.getParameter()), ")");
        super.visit(that);
        if (!shareSetter(d)) { out(";"); }
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
        out(";");
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

    private boolean shareSetter(MethodOrValue d) {
        boolean shared = false;
        if (isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            out(".", names.setter(d), "=", names.setter(d), ";");
            endLine();
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        //Check if the attribute corresponds to a class parameter
        //This is because of the new initializer syntax
        String classParam = null;
        if (d.getContainer() instanceof Functional) {
            classParam = names.name(((Functional)d.getContainer()).getParameter(d.getName()));
        }
        if (!d.isFormal()) {
            comment(that);
            SpecifierOrInitializerExpression specInitExpr =
                        that.getSpecifierOrInitializerExpression();
            if (prototypeStyle && d.isClassOrInterfaceMember()) {
                if ((specInitExpr != null)
                        && !(specInitExpr instanceof LazySpecifierExpression)) {
                    outerSelf(d);
                    out(".", names.name(d), "=");
                    super.visit(that);
                    endLine(true);
                } else if (classParam != null) {
                    outerSelf(d);
                    out(".", names.name(d), "=", classParam);
                    endLine(true);
                }
            }
            else if (specInitExpr instanceof LazySpecifierExpression) {
                out("var ", names.getter(d), "=function(){return ");
                int boxType = boxStart(specInitExpr.getExpression().getTerm());
                specInitExpr.getExpression().visit(this);
                boxUnboxEnd(boxType);
                out(";}");
                endLine(true);
                shareGetter(d);
            }
            else {
                if ((specInitExpr != null) || (classParam != null) || !d.isMember()
                            || d.isVariable()) {
                    generateAttributeGetter(d, specInitExpr, classParam);
                }
                if (d.isVariable()) {
                    final String varName = names.name(d);
                    String paramVarName = names.createTempVariable(d.getName());
                    out("var ", names.setter(d), "=function(", paramVarName, "){return ");
                    out(varName, "=", paramVarName, ";};");
                    endLine();
                    shareSetter(d);
                }
            }
        }
    }
    
    private void generateAttributeGetter(MethodOrValue decl,
                SpecifierOrInitializerExpression expr, String param) {
        final String varName = names.name(decl);
        out("var ", varName);
        if (expr != null) {
            out("=");
            int boxType = boxStart(expr.getExpression().getTerm());
            expr.visit(this);
            boxUnboxEnd(boxType);
        } else if (param != null) {
            out("=", param);
        }
        endLine(true);
        if (decl instanceof Method) {
            if (decl.isClassOrInterfaceMember() && isCaptured(decl)) {
                beginNewLine();
                outerSelf(decl);
                out(".", names.name(decl), "=", names.name(decl), ";");
                endLine();
            }
        } else {
            if (isCaptured(decl)) {
                out("var ", names.getter(decl),"=function(){return ", varName, ";};");
                endLine();
            } else {
                directAccess.add(decl);
            }
            shareGetter(decl);
        }
    }
    
    private void addGetterAndSetterToPrototype(TypeDeclaration outer,
            AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        if (!prototypeStyle||d.isToplevel()) return;
        if (!d.isFormal()) {
            comment(that);
            String classParam = null;
            if (d.getContainer() instanceof Functional) {
                classParam = names.name(((Functional)d.getContainer()).getParameter(d.getName()));
            }
            if ((that.getSpecifierOrInitializerExpression() != null) || d.isVariable()
                        || (classParam != null)) {
                out(names.self(outer), ".", names.getter(d), "=",
                        function, names.getter(d), "()");
                beginBlock();
                if (that.getSpecifierOrInitializerExpression()
                                instanceof LazySpecifierExpression) {
                    // attribute is defined by a lazy expression ("=>" syntax)
                    initSelf(that.getScope());
                    out("return ");
                    Expression expr = that.getSpecifierOrInitializerExpression().getExpression();
                    int boxType = boxStart(expr.getTerm());
                    expr.visit(this);
                    boxUnboxEnd(boxType);
                    out(";");
                }
                else {            
                    out("return this.", names.name(d), ";");
                }
                endBlockNewLine(true);
            }

            if (d.isVariable()) {
                String paramVarName = names.createTempVariable(d.getName());
                out(names.self(outer), ".", names.setter(d), "=");
                out(function, names.setter(d), "(", paramVarName, ")");
                beginBlock();
                out("return this.", names.name(d), "=", paramVarName, ";");
                endBlockNewLine(true);
            }
        }
    }

    @Override
    public void visit(CharLiteral that) {
        out(clAlias, "Character(");
        out(String.valueOf(that.getText().codePointAt(1)));
        out(")");
    }

    /** Escapes a StringLiteral (needs to be quoted). */
    String escapeStringLiteral(String s) {
        StringBuilder text = new StringBuilder(s);
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
        return text.toString();
    }

    @Override
    public void visit(StringLiteral that) {
        final int slen = that.getText().codePointCount(1, that.getText().length()-1);
        if (JsCompiler.compilingLanguageModule) {
            out("String$(", escapeStringLiteral(that.getText()), ",", Integer.toString(slen), ")");
        } else {
            out(clAlias, "String(", escapeStringLiteral(that.getText()), ",", Integer.toString(slen), ")");
        }
    }

    @Override
    public void visit(StringTemplate that) {
        List<StringLiteral> literals = that.getStringLiterals();
        List<Expression> exprs = that.getExpressions();
        out(clAlias, "StringBuilder().appendAll([");
        boolean first = true;
        for (int i = 0; i < literals.size(); i++) {
            StringLiteral literal = literals.get(i);
            if (literal.getText().length() > 2) {
                if (!first) { out(","); }
                first = false;
                literal.visit(this);
            }
            if (i < exprs.size()) {
                if (!first) { out(","); }
                first = false;
                exprs.get(i).visit(this);
                out(".getString()");
            }
        }
        out("]).getString()");
    }

    @Override
    public void visit(FloatLiteral that) {
        out(clAlias, "Float(", that.getText(), ")");
    }

    @Override
    public void visit(NaturalLiteral that) {
        char prefix = that.getText().charAt(0);
        if (prefix == '$' || prefix == '#') {
            int radix= prefix == '$' ? 2 : 16;
            try {
                out("(", new java.math.BigInteger(that.getText().substring(1), radix).toString(), ")");
            } catch (NumberFormatException ex) {
                that.addError("Invalid numeric literal " + that.getText());
            }
        } else {
            out("(", that.getText(), ")");
        }
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
            if (scope != null && ((TypeDeclaration)scope).isClassOrInterfaceMember()) {
                self((TypeDeclaration) scope);
                out(".");
            }
        }
        self(that.getTypeModel().getDeclaration());
    }

    @Override
    public void visit(BaseMemberExpression that) {
        if (that.getErrors() != null && !that.getErrors().isEmpty()) {
            //Don't even bother processing a node with errors
            return;
        }
        Declaration decl = that.getDeclaration();
        String name = decl.getName();
        String pkgName = decl.getUnit().getPackage().getQualifiedNameString();

        // map Ceylon true/false/null directly to JS true/false/null
        if ("ceylon.language".equals(pkgName)) {
            if ("true".equals(name) || "false".equals(name) || "null".equals(name)) {
                out(name);
                return;
            }
        }

        out(memberAccess(that));
    }

    private boolean accessDirectly(Declaration d) {
        return !accessThroughGetter(d) || directAccess.contains(d);
    }

    private boolean accessThroughGetter(Declaration d) {
        return (d instanceof MethodOrValue) && !(d instanceof Method);
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
            out(clAlias, "JsCallable(", lhsVar, ",");
        }
        out(lhsVar, "!==null?", lhsVar, ".", memberAccess(that), ":null)");
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
            out(".", memberAccess(that));
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
        out("while ((", elem, "=", iter, ".next())!==", clAlias, "getFinished())");
        beginBlock();
        //Add value or reference to the array
        out(tmplist, ".push(");
        if (isMethod) {
            out("{o:", elem, ", f:", elem, ".", memberAccess(that), "}");
        } else {
            out(elem, ".", memberAccess(that));
        }
        out(");");
        endBlockNewLine();
        //Gather arguments to pass to the callable
        //Return the array of values or a Callable with the arguments
        out("return ", clAlias);
        if (isMethod) {
            out("JsCallableList(", tmplist, ");");
        } else {
            out("ArraySequence(", tmplist, ");");
        }
        endBlock();
        out("())");
    }

    private void generateCallable(QualifiedMemberOrTypeExpression that, String name) {
        String primaryVar = createRetainedTempVar("opt");
        out("(", primaryVar, "=");
        that.getPrimary().visit(this);
        out(",", clAlias, "JsCallable(", primaryVar, ",", primaryVar, "!==null?",
                primaryVar, ".", (name == null) ? memberAccess(that) : name, ":null))");
    }
    
    /**
     * Checks if the given node is a MemberOrTypeExpression or QualifiedType which
     * represents an access to a supertype member and returns the scope of that
     * member or null.
     */
    private Scope getSuperMemberScope(Node node) {
        Scope scope = null;
        if (node instanceof BaseMemberOrTypeExpression) {
            // Check for "Supertype::member"
            BaseMemberOrTypeExpression bmte = (BaseMemberOrTypeExpression) node;
            if (bmte.getSupertypeQualifier() != null) {
                scope = bmte.getDeclaration().getContainer();
            }
        }
        else if (node instanceof QualifiedMemberOrTypeExpression) {
            // Check for "super.member"
            QualifiedMemberOrTypeExpression qmte = (QualifiedMemberOrTypeExpression) node;
            if (qmte.getPrimary() instanceof Super) {
                scope = qmte.getDeclaration().getContainer();
            }
        }
        else if (node instanceof QualifiedType) {
            // Check for super.Membertype
            QualifiedType qtype = (QualifiedType) node;
            if (qtype.getOuterType() instanceof SuperType) { 
                scope = qtype.getDeclarationModel().getContainer();
            }
        }
        return scope;
    }
    
    private String memberAccessBase(Node node, String member,
                boolean qualifyBaseExpr) {
        StringBuilder sb = new StringBuilder();
        
        if (qualifyBaseExpr && (node instanceof BaseMemberOrTypeExpression)) {
            BaseMemberOrTypeExpression bmte = (BaseMemberOrTypeExpression) node;
            String path = qualifiedPath(node, bmte.getDeclaration());
            if (path.length() > 0) {
                sb.append(path);
                sb.append(".");
            }
        }
        
        Scope scope = getSuperMemberScope(node);
        if (prototypeStyle && (scope != null)) {
            sb.append("getT$all()['");
            sb.append(scope.getQualifiedNameString());
            sb.append("'].$$.prototype.");
        }
        sb.append(member);
        if (!prototypeStyle && (scope != null)) {
            sb.append(names.scopeSuffix(scope));
        }
        //When compiling the language module we need to modify certain base type names
        String rval = sb.toString();
        if (TypeUtils.isReservedTypename(rval)) {
            rval = sb.append("$").toString();
        }
        return rval;
    }

    /**
     * Returns a string representing a read access to a member, as represented by
     * the given expression. If the expression is a QualifiedMemberOrTypeExpression
     * then the LHS is *not* included. If it is a BaseMemberOrTypeExpression and
     * qualifyBaseExpr==true then the qualified path is included.
     */
    private String memberAccess(MemberOrTypeExpression expr, boolean qualifyBaseExpr) {
        Declaration decl = expr.getDeclaration();
        if (isNative(decl)) {
            // direct access to a native element
            return decl.getName();
        }
        if (accessDirectly(decl)) {
            // direct access, without getter
            return memberAccessBase(expr, names.name(decl), qualifyBaseExpr);
        }
        // access through getter
        boolean protoCall = prototypeStyle && (getSuperMemberScope(expr) != null);
        return memberAccessBase(expr, names.getter(decl), qualifyBaseExpr)
                + (protoCall ? ".call(this)" : "()");
    }
    private String memberAccess(MemberOrTypeExpression expr) {
        return memberAccess(expr, true);
    }
    
    private static interface MemberAccessCallback {
        public void generateValue();
    }
    
    /**
     * Generates a write access to a member, as represented by the given expression.
     * The given callback is responsible for generating the assigned value.
     * If the expression is a QualifiedMemberOrTypeExpression then the
     * LHS is *not* included. If it is a BaseMemberOrTypeExpression and
     * qualifyBaseExpr==true then the qualified path is included.
     */
    private void generateMemberAccess(MemberOrTypeExpression expr,
                MemberAccessCallback callback, boolean qualifyBaseExpr) {
        Declaration decl = expr.getDeclaration();
        boolean paren = false;
        if (isNative(decl)) {
            // direct access to a native element
            out(decl.getName(), "=");
        }
        else if (accessDirectly(decl)) {
            // direct access, without setter
            out(memberAccessBase(expr, names.name(decl), qualifyBaseExpr), "=");
        }
        else {
            // access through setter
            boolean protoCall = prototypeStyle && (getSuperMemberScope(expr) != null);
            out(memberAccessBase(expr, names.setter(decl), qualifyBaseExpr),
                    protoCall ? ".call(this," : "(");
            paren = true;
        }
        
        callback.generateValue();
        if (paren) { out(")"); }
    }
    private void generateMemberAccess(MemberOrTypeExpression expr, final String strValue,
            boolean qualifyBaseExpr) {
        generateMemberAccess(expr, new MemberAccessCallback() {
            @Override public void generateValue() { out(strValue); }
        }, qualifyBaseExpr);
    }
    
    @Override
    public void visit(BaseTypeExpression that) {
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
        qualify(that, that.getDeclaration());
        out(names.name(that.getDeclaration()));
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
            NamedArgumentList argList = that.getNamedArgumentList();
            out("(");

            Map<String, String> argVarNames = invoker.defineNamedArguments(argList);

            TypeArguments targs = that.getPrimary() instanceof BaseMemberOrTypeExpression ? ((BaseMemberOrTypeExpression)that.getPrimary()).getTypeArguments() : null;
            that.getPrimary().visit(this);
            if (that.getPrimary() instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
                if (mte.getDeclaration() instanceof Functional) {
                    Functional f = (Functional) mte.getDeclaration();
                    invoker.applyNamedArguments(argList, f, argVarNames, getSuperMemberScope(mte)!=null, targs);
                }
            }
            out(")");
        }
        else {
            PositionalArgumentList argList = that.getPositionalArgumentList();
            that.getPrimary().visit(this);
            if (prototypeStyle && (getSuperMemberScope(that.getPrimary()) != null)) {
                out(".call(this");
                if (!argList.getPositionalArguments().isEmpty()) {
                    out(",");
                }
            }
            else {
                out("(");
            }
            argList.visit(this);
            TypeArguments targs = that.getPrimary() instanceof StaticMemberOrTypeExpression ? ((StaticMemberOrTypeExpression)that.getPrimary()).getTypeArguments() : null;
            if (targs != null && targs.getTypeModels() != null && !targs.getTypeModels().isEmpty()) {
                if (argList.getPositionalArguments().size() > 0) {
                    out(",");
                }
                Declaration bmed = ((StaticMemberOrTypeExpression)that.getPrimary()).getDeclaration();
                if (bmed instanceof Functional) {
                    if (((Functional) bmed).getParameterLists().get(0).getParameters().size() > argList.getPositionalArguments().size()
                    		// has no comprehension
                    		&& (argList.getPositionalArguments().isEmpty()
                    			|| argList.getPositionalArguments().get(argList.getPositionalArguments().size()-1) instanceof Tree.Comprehension == false)) {
                        out("undefined,");
                    }
                    if (targs != null && targs.getTypeModels() != null && !targs.getTypeModels().isEmpty()) {
                        Map<TypeParameter, ProducedType> invargs = TypeUtils.matchTypeParametersWithArguments(
                                ((Functional) bmed).getTypeParameters(), targs.getTypeModels());
                        if (invargs != null) {
                            TypeUtils.printTypeArguments(that, invargs, this);
                        } else {
                            out("/*TARGS != TPARAMS!!!! WTF?????*/");
                        }
                    }
                }
            }
            out(")");
        }
    }

    @Override
    public void visit(PositionalArgumentList that) {
        invoker.generatePositionalArguments(that, that.getPositionalArguments(), false);
    }

    // Make sure fromTerm is compatible with toTerm by boxing it when necessary
    private int boxStart(Term fromTerm) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = false;
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }

    // Make sure fromTerm is compatible with toTerm by boxing or unboxing it when necessary
    int boxUnboxStart(Term fromTerm, Term toTerm) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = isNative(toTerm);
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }

    // Make sure fromTerm is compatible with toDecl by boxing or unboxing it when necessary
    int boxUnboxStart(Term fromTerm, com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration toDecl) {
        boolean fromNative = isNative(fromTerm);
        boolean toNative = isNative(toDecl);
        ProducedType fromType = fromTerm.getTypeModel();
        return boxUnboxStart(fromNative, fromType, toNative);
    }

    int boxUnboxStart(boolean fromNative, ProducedType fromType, boolean toNative) {
        if (fromNative != toNative) {
            // Box the value
            String fromTypeName = fromType.getProducedTypeQualifiedName();
            if (fromNative) {
                // conversion from native value to Ceylon value
                if (fromTypeName.equals("ceylon.language::String")) {
                    if (JsCompiler.compilingLanguageModule) {
                        out("String$(");
                    } else {
                        out(clAlias, "String(");
                    }
                } else if (fromTypeName.equals("ceylon.language::Integer")) {
                    out("(");
                } else if (fromTypeName.equals("ceylon.language::Float")) {
                    out(clAlias, "Float(");
                } else if (fromTypeName.equals("ceylon.language::Boolean")) {
                    out("(");
                } else if (fromTypeName.equals("ceylon.language::Character")) {
                    out(clAlias, "Character(");
                } else {
                    return 0;
                }
                return 1;
            } else if ("ceylon.language::String".equals(fromTypeName)
                        || "ceylon.language::Float".equals(fromTypeName)) {
                // conversion from Ceylon String or Float to native value
                return 2;
            } else {
                return 3;
            }
        }
        return 0;
    }

    void boxUnboxEnd(int boxType) {
        switch (boxType) {
        case 1: out(")"); break;
        case 2: out(".valueOf()"); break;
        default: //nothing
        }
    }

    @Override
    public void visit(ObjectArgument that) {
        //Don't even bother with nodes that have errors
        if (that.getErrors() != null && !that.getErrors().isEmpty()) return;
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
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!prototypeStyle) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        callSuperclass(xt, c, that, superDecs);
        callInterfaces(sts, c, that, superDecs);
        
        body.visit(this);
        returnSelf(c);
        indentLevel--;
        endLine();
        out("}");
        endLine();

        typeInitialization(xt, sts, false, c, new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                addToPrototype(c, body.getStatements());
            }
        });
        out("return ", names.name(c), "(new ", names.name(c), ".$$);");
        endBlock();
        out("())");
    }

    @Override
    public void visit(AttributeArgument that) {
        out("(function()");
        beginBlock();
        out("//AttributeArgument ", that.getParameter().getName());
        location(that);
        endLine();
        
        Block block = that.getBlock();
        SpecifierExpression specExpr = that.getSpecifierExpression();
        if (specExpr != null) {
            out("return ");
            specExpr.getExpression().visit(this);
            out(";");
        }
        else if (block != null) {
            visitStatements(block.getStatements());
        }
        
        endBlock();
        out("())");
    }

    @Override
    public void visit(SequencedArgument that) {
    	List<PositionalArgument> positionalArguments = that.getPositionalArguments();
    	boolean spread = !positionalArguments.isEmpty() 
    			&& positionalArguments.get(positionalArguments.size()-1) instanceof Tree.ListedArgument == false;
        if (!spread) { out("["); }
        boolean first=true;
        for (PositionalArgument arg: positionalArguments) {
            if (!first) out(",");
            if (arg instanceof Tree.ListedArgument) {
            	((Tree.ListedArgument) arg).getExpression().visit(this);
            } else if(arg instanceof Tree.SpreadArgument)
            	((Tree.SpreadArgument) arg).getExpression().visit(this);
            else // comprehension
            	arg.visit(this);
            first = false;
        }
        if (!spread) { out("]"); }
    }

    @Override
    public void visit(SequenceEnumeration that) {
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
            out(clAlias, "empty");
        } else {
            List<PositionalArgument> positionalArguments = sarg.getPositionalArguments();
            int lim = positionalArguments.size()-1;
            boolean spread = !positionalArguments.isEmpty() 
                    && positionalArguments.get(positionalArguments.size()-1) instanceof Tree.ListedArgument == false;
            int count=0;
            ProducedType chainedType = null;
            if (lim>0 || !spread) {
                out("[");
            }
            for (PositionalArgument expr : positionalArguments) {
                if (count==lim && spread) {
                    if (lim > 0) {
                        ProducedType seqType = TypeUtils.findSupertype(types.iterable, that.getTypeModel());
                        closeSequenceWithReifiedType(that, seqType.getTypeArguments());
                        out(".chain(");
                        chainedType = TypeUtils.findSupertype(types.iterable, expr.getTypeModel());
                    }
                    count--;
                } else {
                    if (count > 0) {
                        out(",");
                    }
                }
                expr.visit(this);
                count++;
            }
            if (chainedType == null) {
                if (!spread) {
                    closeSequenceWithReifiedType(that, that.getTypeModel().getTypeArguments());
                }
            } else {
                out(",");
                TypeUtils.printTypeArguments(that, chainedType.getTypeArguments(), this);
                out(")");
            }
        }
    }


    @Override
    public void visit(Comprehension that) {
        new ComprehensionGenerator(this, names, directAccess).generateComprehension(that);
    }


    @Override
    public void visit(final SpecifierStatement that) {
        if (prototypeStyle && (that.getSpecifierExpression() instanceof LazySpecifierExpression)
                && (that.getScope().getContainer() instanceof TypeDeclaration)) {
            // A lazy specifier expression in a class/interface. In prototype style
            // these should go into the prototype, so don't generate them here.
            return;
        }
        if (that.getBaseMemberExpression() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getBaseMemberExpression();
            Declaration bmeDecl = bme.getDeclaration();
            if (that.getSpecifierExpression() instanceof LazySpecifierExpression) {
                // attr => expr;
                if (bmeDecl.isMember()) {
                    qualify(that, bmeDecl);
                }
                else {
                    out("var ");
                }
                out(names.getter(bmeDecl), "=function(){return ");
                that.getSpecifierExpression().visit(this);
                out(";};");
                directAccess.remove(bmeDecl);
            }
            else {
                // attr = expr;
                if (bmeDecl instanceof MethodOrValue) {
                    final MethodOrValue moval = (MethodOrValue)bmeDecl;
                    if (moval.isVariable()) {
                        // simple assignment to a variable attribute
                        generateMemberAccess(bme, new MemberAccessCallback() {
                            @Override public void generateValue() {
                                int boxType = boxUnboxStart(that.getSpecifierExpression().getExpression().getTerm(),
                                        moval);
                                that.getSpecifierExpression().getExpression().visit(GenerateJsVisitor.this);
                                boxUnboxEnd(boxType);
                            }
                        }, true);
                        out(";");
                    } else if (moval.isMember()) {
                        // Specifier for a member attribute. This actually defines the
                        // member (e.g. in shortcut refinement syntax the attribute
                        // declaration itself can be omitted), so generate the attribute.
                        generateAttributeGetter(moval,
                                that.getSpecifierExpression(), null);
                    } else {
                        // Specifier for some other attribute, or for a method.
                        if (prototypeStyle 
                                || (bmeDecl.isMember() && (bmeDecl instanceof Method))) {
                            qualify(that, bmeDecl);
                        }
                        out(names.name(bmeDecl), "=");
                        that.getSpecifierExpression().visit(this);
                        out(";");
                    }
                }
            }
        }
        else if ((that.getBaseMemberExpression() instanceof ParameterizedExpression)
                    && (that.getSpecifierExpression() != null)) {
            ParameterizedExpression paramExpr =
                        (ParameterizedExpression) that.getBaseMemberExpression();
            if (paramExpr.getPrimary() instanceof BaseMemberExpression) {
                // func(params) => expr;
                BaseMemberExpression bme = (BaseMemberExpression) paramExpr.getPrimary();
                Declaration bmeDecl = bme.getDeclaration();
                if (bmeDecl.isMember()) {
                    qualify(that, bmeDecl);
                }
                else {
                    out("var ");
                }
                out(names.name(bmeDecl), "=");
                singleExprFunction(paramExpr.getParameterLists(),
                        that.getSpecifierExpression().getExpression(),
                        that.getScope());
                out(";");
            }
        }
    }
    
    private void addSpecifierToPrototype(final TypeDeclaration outer,
                final SpecifierStatement specStmt) {
        if (specStmt.getBaseMemberExpression() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) specStmt.getBaseMemberExpression();
            Declaration bmeDecl = bme.getDeclaration();
            if (specStmt.getSpecifierExpression() instanceof LazySpecifierExpression) {
                // attr => expr;
                out(names.self(outer), ".", names.getter(bmeDecl), "=function()");
                beginBlock();
                initSelf(specStmt.getScope());
                out("return ");
                specStmt.getSpecifierExpression().visit(this);
                out(";");
                endBlockNewLine(true);
            }
            else if (bmeDecl.isMember() && (bmeDecl instanceof Value)) {
                // attr = expr;
                out(names.self(outer), ".", names.getter(bmeDecl),
                        "=function(){return this.", names.name(bmeDecl), ";};");
                endLine();
            }
        }
        else if ((specStmt.getBaseMemberExpression() instanceof ParameterizedExpression)
                    && (specStmt.getSpecifierExpression() != null)) {
            final ParameterizedExpression paramExpr =
                    (ParameterizedExpression) specStmt.getBaseMemberExpression();
            if (paramExpr.getPrimary() instanceof BaseMemberExpression) {
                // func(params) => expr;
                BaseMemberExpression bme = (BaseMemberExpression) paramExpr.getPrimary();
                out(names.self(outer), ".", names.name(bme.getDeclaration()), "=");
                singleExprFunction(paramExpr.getParameterLists(),
                        specStmt.getSpecifierExpression().getExpression(), specStmt.getScope());
                out(";");
            }
        }
    }

    @Override
    public void visit(final AssignOp that) {
        String returnValue = null;
        MemberOrTypeExpression lhsExpr = null;
        
        out("(");
        if (that.getLeftTerm() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getLeftTerm();
            lhsExpr = bme;
            Declaration bmeDecl = bme.getDeclaration();
            boolean simpleSetter = hasSimpleGetterSetter(bmeDecl);
            if (!simpleSetter) {
                returnValue = memberAccess(bme);
            }

        } else if (that.getLeftTerm() instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression qme = (QualifiedMemberExpression)that.getLeftTerm();
            lhsExpr = qme;
            boolean simpleSetter = hasSimpleGetterSetter(qme.getDeclaration());
            String lhsVar = null;
            if (!simpleSetter) {
                lhsVar = createRetainedTempVar();
                out(lhsVar, "=");
                super.visit(qme);
                out(",", lhsVar, ".");
                returnValue = lhsVar + "." + memberAccess(qme);
            } else {
                super.visit(qme);
                out(".");
            }
        }
        
        generateMemberAccess(lhsExpr, new MemberAccessCallback() {
            @Override public void generateValue() {
                int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
                that.getRightTerm().visit(GenerateJsVisitor.this);
                boxUnboxEnd(boxType);
            }
        }, true);
        
        if (returnValue != null) { out(",", returnValue); }
        out(")");
    }

    /** Outputs the module name for the specified declaration. Returns true if something was output. */
    boolean qualify(Node that, Declaration d) {
        if (d.getUnit().getPackage().getModule().isDefault()) {
            return false;
        }
        String path = qualifiedPath(that, d);
        if (path.length() > 0) {
            out(path, ".");
        }
        return path.length() > 0;
    }

    private String qualifiedPath(Node that, Declaration d) {
        return qualifiedPath(that, d, false);
    }

    private String qualifiedPath(Node that, Declaration d, boolean inProto) {
        boolean isMember = d.isClassOrInterfaceMember();
        if (!isMember && isImported(that, d)) {
            return names.moduleAlias(d.getUnit().getPackage().getModule());
        }
        else if (prototypeStyle && !inProto) {
            if (isMember && !(d instanceof com.redhat.ceylon.compiler.typechecker.model.Parameter
                              && !d.isCaptured())) {
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
        else if (d != null && (d.isShared() || inProto) && isMember) {
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

    /** Tells whether a declaration is in the same package as a node. */
    private boolean isImported(Node that, Declaration d) {
        if (d == null) {
            return false;
        }
        Package p1 = d.getUnit().getPackage();
        Package p2 = that == null ? null : that.getUnit().getPackage();
        return !p1.equals(p2);
    }

    @Override
    public void visit(ExecutableStatement that) {
        super.visit(that);
        endLine(true);
    }

    /** Creates a new temporary variable which can be used immediately, even
     * inside an expression. The declaration for that temporary variable will be
     * emitted after the current Ceylon statement has been completely processed.
     * The resulting code is valid because JavaScript variables may be used before
     * they are declared. */
    private String createRetainedTempVar(String baseName) {
        String varName = names.createTempVariable(baseName);
        retainedVars.add(varName);
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

    void self(TypeDeclaration d) {
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

    private void arithmeticAssignOp(final ArithmeticAssignmentOp that,
                                    final String functionName) {
        Term lhs = that.getLeftTerm();
        if (lhs instanceof BaseMemberExpression) {
            BaseMemberExpression lhsBME = (BaseMemberExpression) lhs;
            Declaration lhsDecl = lhsBME.getDeclaration();

            final String getLHS = memberAccess(lhsBME);
            out("(");
            generateMemberAccess(lhsBME, new MemberAccessCallback() {
                @Override public void generateValue() {
                    out(getLHS, ".", functionName, "(");
                    that.getRightTerm().visit(GenerateJsVisitor.this);
                    out(")");
                }
            }, true);
            if (!hasSimpleGetterSetter(lhsDecl)) { out(",", getLHS); }
            out(")");

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
                final String lhsPrimaryVar = createRetainedTempVar();
                final String getLHS = lhsPrimaryVar + "." + memberAccess(lhsQME);
                out("(", lhsPrimaryVar, "=");
                lhsQME.getPrimary().visit(this);
                out(",", lhsPrimaryVar, ".");
                generateMemberAccess(lhsQME, new MemberAccessCallback() {
                    @Override public void generateValue() {
                        out(getLHS, ".", functionName, "(");
                        that.getRightTerm().visit(GenerateJsVisitor.this);
                        out(")");
                    }
                }, false);
                
                if (!hasSimpleGetterSetter(lhsQME.getDeclaration())) {
                    out(",", getLHS);
                }
                out(")");
            }
        }
    }

    @Override public void visit(final NegativeOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                TypeDeclaration d = that.getTerm().getTypeModel().getDeclaration();
                if (d.inherits(types._integer)) {
                    out("(-");
                    termgen.term();
                    out(")");
                //This is not really optimal yet, since it generates
                //stuff like Float(-Float((5.1)))
                /*} else if (d.inherits(types._float)) {
                    out(clAlias, "Float(-");
                    termgen.term();
                    out(")");*/
                } else {
                    termgen.term();
                    out(".getNegativeValue()");
                }
            }
        });
    }

    @Override public void visit(final PositiveOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                TypeDeclaration d = that.getTerm().getTypeModel().getDeclaration();
                if (d.inherits(types._integer) || d.inherits(types._float)) {
                    out("(+");
                    termgen.term();
                    out(")");
                } else {
                    termgen.term();
                    out(".getPositiveValue()");
                }
            }
        });
    }

    @Override public void visit(EqualOp that) {
        leftEqualsRight(that);
    }

    @Override public void visit(NotEqualOp that) {
        out("(!");
        leftEqualsRight(that);
        out(")");
    }

    @Override public void visit(NotOp that) {
        unaryOp(that, new UnaryOpGenerator() {
            @Override
            public void generate(UnaryOpTermGenerator termgen) {
                out("(!");
                termgen.term();
                out(")");
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
                out(")");
            }
        });
    }

    @Override public void visit(CompareOp that) {
        leftCompareRight(that);
    }

    @Override public void visit(SmallerOp that) {
        leftCompareRight(that);
        out(".equals(", clAlias, "getSmaller())");
    }

    @Override public void visit(LargerOp that) {
        leftCompareRight(that);
        out(".equals(", clAlias, "getLarger())");
    }

    @Override public void visit(SmallAsOp that) {
        out("(");
        leftCompareRight(that);
        out("!==", clAlias, "getLarger()");
        out(")");
    }

    @Override public void visit(LargeAsOp that) {
        out("(");
        leftCompareRight(that);
        out("!==", clAlias, "getSmaller()");
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
               out("&&");
               termgen.right();
               out(")");
           }
       });
   }

   @Override public void visit(OrOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out("(");
               termgen.left();
               out("||");
               termgen.right();
               out(")");
           }
       });
   }

   @Override public void visit(final EntryOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out(clAlias, "Entry(");
               termgen.left();
               out(",");
               termgen.right();
               out(",");
               TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(),
                       GenerateJsVisitor.this);
               out(")");
           }
       });
   }

   @Override public void visit(Element that) {
       out(".get(");
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
               out("?");
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
           boolean simpleSetter = hasSimpleGetterSetter(bme.getDeclaration());
           String getMember = memberAccess(bme);
           String applyFunc = String.format("%s.%s()", getMember, functionName);
           out("(");
           generateMemberAccess(bme, applyFunc, true);
           if (!simpleSetter) { out(",", getMember); }
           out(")");
           
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           String primaryVar = createRetainedTempVar();
           String getMember = primaryVar + "." + memberAccess(qme);
           String applyFunc = String.format("%s.%s()", getMember, functionName);
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",", primaryVar, ".");
           generateMemberAccess(qme, applyFunc, false);
           if (!hasSimpleGetterSetter(qme.getDeclaration())) {
               out(",", getMember);
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
           String oldValueVar = createRetainedTempVar("old" + bme.getDeclaration().getName());
           String applyFunc = String.format("%s.%s()", oldValueVar, functionName);
           out("(", oldValueVar, "=", memberAccess(bme), ",");
           generateMemberAccess(bme, applyFunc, true);
           out(",", oldValueVar, ")");

       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           String primaryVar = createRetainedTempVar();
           String oldValueVar = createRetainedTempVar("old" + qme.getDeclaration().getName());
           String applyFunc = String.format("%s.%s()", oldValueVar, functionName);
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",", oldValueVar, "=", primaryVar, ".", memberAccess(qme), ",",
                   primaryVar, ".");
           generateMemberAccess(qme, applyFunc, false);
           out(",", oldValueVar, ")");
       }
   }

    @Override
    public void visit(final UnionOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".union(");
                termgen.right();
                out(",");
                TypeUtils.printTypeArguments(that, that.getRightTerm().getTypeModel().getTypeArguments(),
                        GenerateJsVisitor.this);
                out(")");
            }
        });
    }

    @Override
    public void visit(final IntersectionOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".intersection(");
                termgen.right();
                out(",");
                TypeUtils.printTypeArguments(that, that.getRightTerm().getTypeModel().getTypeArguments(),
                        GenerateJsVisitor.this);
                out(")");
            }
        });
    }

    @Override
    public void visit(final XorOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".exclusiveUnion(");
                termgen.right();
                out(",");
                TypeUtils.printTypeArguments(that, that.getRightTerm().getTypeModel().getTypeArguments(),
                        GenerateJsVisitor.this);
                out(")");
            }
        });
    }

    @Override
    public void visit(final ComplementOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                termgen.left();
                out(".complement(");
                termgen.right();
                out(",");
                TypeUtils.printTypeArguments(that, that.getRightTerm().getTypeModel().getTypeArguments(),
                        GenerateJsVisitor.this);
                out(")");
            }
        });
    }

   @Override public void visit(Exists that) {
       unaryOp(that, new UnaryOpGenerator() {
           @Override
           public void generate(UnaryOpTermGenerator termgen) {
               out(clAlias, "exists(");
               termgen.term();
               out(")");
           }
       });
   }
   @Override public void visit(Nonempty that) {
       unaryOp(that, new UnaryOpGenerator() {
           @Override
           public void generate(UnaryOpTermGenerator termgen) {
               out(clAlias, "nonempty(");
               termgen.term();
               out(")");
           }
       });
   }

   //Don't know if we'll ever see this...
   @Override public void visit(ConditionList that) {
       System.out.println("ZOMG condition list in the wild! " + that.getLocation() + " of " + that.getUnit().getFilename());
       super.visit(that);
   }

   @Override public void visit(BooleanCondition that) {
       int boxType = boxStart(that.getExpression().getTerm());
       super.visit(that);
       boxUnboxEnd(boxType);
   }

   @Override public void visit(IfStatement that) {
       conds.generateIf(that);
   }

   @Override public void visit(WhileStatement that) {
       conds.generateWhile(that);
   }

    /** Generates js code to check if a term is of a certain type. We solve this in JS by
     * checking against all types that Type satisfies (in the case of union types, matching any
     * type will do, and in case of intersection types, all types must be matched).
     * @param term The term that is to be checked against a type
     * @param termString (optional) a string to be used as the term to be checked
     * @param type The type to check against
     * @param tmpvar (optional) a variable to which the term is assigned
     * @param negate If true, negates the generated condition
     */
    void generateIsOfType(Term term, String termString, Type type, String tmpvar, final boolean negate) {
        if (negate) {
            out("!");
        }
        out(clAlias, "isOfType(");
        if (term != null) {
            conds.specialConditionRHS(term, tmpvar);
        } else {
            conds.specialConditionRHS(termString, tmpvar);
        }
        out(",");

        TypeUtils.typeNameOrList(term, type.getTypeModel(), this, true);
        out(")");
    }

    @Override
    public void visit(IsOp that) {
        generateIsOfType(that.getTerm(), null, that.getType(), null, false);
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

   @Override public void visit(final RangeOp that) {
       binaryOp(that, new BinaryOpGenerator() {
           @Override
           public void generate(BinaryOpTermGenerator termgen) {
               out(clAlias, "Range(");
               termgen.left();
               out(",");
               termgen.right();
               out(",");
               TypeUtils.printTypeArguments(that,
                       that.getTypeModel().getTypeArguments(),
                       GenerateJsVisitor.this);
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
        visitStatements(that.getForClause().getBlock().getStatements());
        //If there's an else block, check for normal termination
        endBlock();
        if (hasElse) {
            endLine();
            out("if (", clAlias, "getFinished() === ", itemVar, ")");
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
        out("var ", itemVar, ";while ((", itemVar, "=", iterVar, ".next())!==", clAlias, "getFinished())");
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
                generateIsOfType(null, catchVarName, variable.getType(), null, false);
                out(")");

                if (catchClause.getBlock().getStatements().isEmpty()) {
                    out("{}");
                } else {
                    beginBlock();
                    directAccess.add(variable.getDeclarationModel());
                    names.forceName(variable.getDeclarationModel(), catchVarName);

                    visitStatements(catchClause.getBlock().getStatements());
                    endBlockNewLine();
                }
            }
            out("else{throw ", catchVarName, "}");
            endBlockNewLine();
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
            out(clAlias, "Exception()");
        }
        out(";");
    }

    private void visitIndex(IndexExpression that) {
        that.getPrimary().visit(this);
        ElementOrRange eor = that.getElementOrRange();
        if (eor instanceof Element) {
            out(".get(");
            ((Element)eor).getExpression().visit(this);
            out(")");
        } else {//range, or spread?
            ElementRange er = (ElementRange)eor;
            Expression sexpr = er.getLength();
            if (sexpr == null) {
                if (er.getLowerBound() == null) {
                    out(".spanTo(");
                } else if (er.getUpperBound() == null) {
                    out(".spanFrom(");
                } else {
                    out(".span(");
                }
            } else {
                out(".segment(");
            }
            if (er.getLowerBound() != null) {
                er.getLowerBound().visit(this);
                if (er.getUpperBound() != null || sexpr != null) {
                    out(",");
                }
            }
            if (er.getUpperBound() != null) {
                er.getUpperBound().visit(this);
            } else if (sexpr != null) {
                sexpr.visit(this);
            }
            out(")");
        }
    }

    public void visit(IndexExpression that) {
        visitIndex(that);
    }

    /** Generates code for a case clause, as part of a switch statement. Each case
     * is rendered as an if. */
    private void caseClause(CaseClause cc, String expvar, Term switchTerm) {
        out("if (");
        final CaseItem item = cc.getCaseItem();
        if (item instanceof IsCase) {
            IsCase isCaseItem = (IsCase) item;
            generateIsOfType(null, expvar, isCaseItem.getType(), null, false);
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
                //out(")==="); clAlias(); out("getTrue()");
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
        endLine(true);
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
        if (comment) {
            out("//End switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            endLine();
        }
    }

    /** Generates the code for an anonymous function defined inside an argument list. */
    @Override
    public void visit(final FunctionArgument that) {
        singleExprFunction(that.getParameterLists(), that.getExpression(), that.getScope());
    }
    
    private void singleExprFunction(final List<ParameterList> paramLists,
                                    final Expression expr, final Scope scope) {
        generateParameterLists(paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                beginBlock();
                if (paramLists.size() == 1) { initSelf(scope); }
                initParameters(paramLists.get(paramLists.size()-1), null);
                out("return ");
                expr.visit(GenerateJsVisitor.this);
                out(";");
                endBlock();
            }
        });
    }

    /** Generates the code for a function in a named argument list. */
    @Override
    public void visit(final MethodArgument that) {
        generateParameterLists(that.getParameterLists(), that.getScope(),
                new ParameterListCallback() {
            @Override
            public void completeFunction() {
                Block block = that.getBlock();
                SpecifierExpression specExpr = that.getSpecifierExpression();
                if (specExpr != null) {
                    out("{return ");
                    specExpr.getExpression().visit(GenerateJsVisitor.this);
                    out(";}");
                }
                else if (block != null) {
                    block.visit(GenerateJsVisitor.this);
                }
            }
        });
    }

    @Override
    public void visit(SegmentOp that) {
        String rhs = names.createTempVariable();
        out("(function(){var ", rhs, "=");
        that.getRightTerm().visit(this);
        endLine(true);
        out("if (", rhs, ">0){");
        endLine();
        String lhs = names.createTempVariable();
        String end = names.createTempVariable();
        out("var ", lhs, "=");
        that.getLeftTerm().visit(this);
        endLine(true);
        out("var ", end, "=", lhs);
        endLine(true);
        out("for (var i=1; i<", rhs, "; i++){", end, "=", end, ".getSuccessor();}");
        endLine();
        out("return ", clAlias, "Range(");
        out(lhs, ",", end, ")");
        endLine();
        out("}else return ", clAlias, "empty;}())");
    }

    /** Generates the code for single or multiple parameter lists, with a callback function to generate the function blocks. */
    private void generateParameterLists(List<ParameterList> plist, Scope scope,
                ParameterListCallback callback) {
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
                if (count == 0) {
                    beginBlock();
                    initSelf(scope);
                    initParameters(paramList, null);
                }
                else {
                    out("{");
                }
                count++;
            }
            callback.completeFunction();
            for (int i=0; i < count; i++) {
                endBlock(false, i==count-1);
            }
        }
    }

    /** Encloses the block in a function, IF NEEDED. */
    void encloseBlockInFunction(Block block) {
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
            endBlockNewLine();
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

    @Override
    public void visit(Tuple that) {
        int count = 0;
        SequencedArgument sarg = that.getSequencedArgument();
        if (sarg == null) {
        	out(clAlias, "empty");
        } else {
        	List<Map<TypeParameter,ProducedType>> targs = new ArrayList<Map<TypeParameter,ProducedType>>();
        	List<PositionalArgument> positionalArguments = sarg.getPositionalArguments();
        	boolean spread = !positionalArguments.isEmpty() 
        			&& positionalArguments.get(positionalArguments.size()-1) instanceof Tree.ListedArgument == false;
        	int lim = positionalArguments.size()-1;
        	for (PositionalArgument expr : positionalArguments) {
        		if (count > 0) {
        			out(",");
        		}
        		if (count==lim && spread) {
        			if (expr.getTypeModel().getDeclaration().inherits(types.tuple)) {
        				expr.visit(this);
        			} else {
        				expr.visit(this);
        				out(".getSequence()");
        			}
        		} else {
        			out(clAlias, "Tuple(");
        			if (count > 0) {
        			    for (Map.Entry<TypeParameter,ProducedType> e : targs.get(0).entrySet()) {
        			        if (e.getKey().getName().equals("Rest")) {
                                targs.add(0, e.getValue().getTypeArguments());
        			        }
        			    }
        			} else {
        				targs.add(that.getTypeModel().getTypeArguments());
        			}
        			expr.visit(this);
        		}
        		count++;
        	}
        	if (!spread) {
        		if (count > 0) {
        			out(",");
        		}
        		out(clAlias, "empty");
        	} else {
        		count--;
        	}
        	for (Map<TypeParameter,ProducedType> t : targs) {
        		out(",");
        		TypeUtils.printTypeArguments(that, t, this);
        		out(")");
        	}
        }
    }

    @Override
    public void visit(Assertion that) {
        out("//assert");
        location(that);
        String custom = "Assertion failed";
        //Scan for a "doc" annotation with custom message
        for (Annotation ann : that.getAnnotationList().getAnnotations()) {
            BaseMemberExpression bme = (BaseMemberExpression)ann.getPrimary();
            if ("doc".equals(bme.getDeclaration().getName())) {
                custom = ((Tree.ListedArgument)ann.getPositionalArgumentList().getPositionalArguments().get(0)).getExpression().getTerm().getText();
                //unquote
                custom = custom.substring(1, custom.length() - 1);
            }
        }
        endLine();
        StringBuilder sb = new StringBuilder(custom).append(": '");
        for (int i = that.getConditionList().getToken().getTokenIndex()+1;
                i < that.getConditionList().getEndToken().getTokenIndex(); i++) {
            sb.append(tokens.get(i).getText());
        }
        sb.append("' at ").append(that.getUnit().getFilename()).append(" (").append(
                that.getConditionList().getLocation()).append(")");
        conds.specialConditionsAndBlock(that.getConditionList(), null, "if (!");
        //escape
        custom = escapeStringLiteral(sb.toString());
        out(") { throw ", clAlias, "Exception('", custom, "'); }");
        endLine();
    }

    void closeSequenceWithReifiedType(Node that, Map<TypeParameter,ProducedType> types) {
        out("].reifyCeylonType(");
        TypeUtils.printTypeArguments(that, types, this);
        out(")");
    }

}

