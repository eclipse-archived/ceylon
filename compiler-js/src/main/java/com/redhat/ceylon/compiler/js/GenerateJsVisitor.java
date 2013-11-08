package com.redhat.ceylon.compiler.js;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.eliminateParensAndWidening;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassAlias;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.ImportableScope;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Specification;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.*;

public class GenerateJsVisitor extends Visitor
        implements NaturalVisitor {

    private final Stack<Continuation> continues = new Stack<>();
    private final EnclosingFunctionVisitor encloser = new EnclosingFunctionVisitor();
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess = new HashSet<>();
    private final Set<String> generatedAttributes = new HashSet<>();
    private final RetainedVars retainedVars = new RetainedVars();
    final ConditionGenerator conds;
    private final InvocationGenerator invoker;
    private final List<CommonToken> tokens;
    private final ErrorVisitor errVisitor = new ErrorVisitor();
    private int dynblock;

    private final class SuperVisitor extends Visitor {
        private final List<Declaration> decs;

        private SuperVisitor(List<Declaration> decs) {
            this.decs = decs;
        }

        @Override
        public void visit(QualifiedMemberOrTypeExpression qe) {
            Term primary = eliminateParensAndWidening(qe.getPrimary());
            if (primary instanceof Super) {
                decs.add(qe.getDeclaration());
            }
            super.visit(qe);
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

    private List<? extends Statement> currentStatements = null;
    
    private final TypeUtils types;
    private Writer out;
    private final Writer originalOut;
    final Options opts;
    private CompilationUnit root;
    private static String clAlias="";
    private static final String function="function ";
    private boolean needIndent = true;
    private int indentLevel = 0;

    Package getCurrentPackage() {
        return root.getUnit().getPackage();
    }

    private static void setCLAlias(String alias) {
        clAlias = alias + ".";
    }
    /** Returns the module name for the language module. */
    static String getClAlias() { return clAlias; }

    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }

    private final JsOutput jsout;

    public GenerateJsVisitor(JsOutput out, Options options, JsIdentifierNames names,
            List<CommonToken> tokens, TypeUtils typeUtils) throws IOException {
        this.jsout = out;
        this.opts = options;
        this.out = out.getWriter();
        originalOut = out.getWriter();
        this.names = names;
        conds = new ConditionGenerator(this, names, directAccess);
        this.tokens = tokens;
        types = typeUtils;
        invoker = new InvocationGenerator(this, names, retainedVars);
    }

    TypeUtils getTypeUtils() { return types; }
    InvocationGenerator getInvoker() { return invoker; }

    /** Returns the helper component to handle naming. */
    JsIdentifierNames getNames() { return names; }

    private static interface GenerateCallback {
        public void generateValue();
    }
    
    /** Print generated code to the Writer specified at creation time.
     * Automatically prints indentation first if necessary.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    void out(String code, String... codez) {
        try {
            if (opts.isIndent() && needIndent) {
                for (int i=0;i<indentLevel;i++) {
                    out.write("    ");
                }
            }
            needIndent = false;
            out.write(code);
            for (String s : codez) {
                out.write(s);
            }
            if (opts.isVerbose() && out == originalOut) {
                //Print code to console (when printing to REAL output)
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

    private String generateToString(final GenerateCallback callback) {
        final Writer oldWriter = out;
        out = new StringWriter();
        callback.generateValue();
        final String str = out.toString();
        out = oldWriter;
        return str;
    }
    
    @Override
    public void visit(CompilationUnit that) {
        root = that;
        Module clm = that.getUnit().getPackage().getModule()
                .getLanguageModule();
        if (!JsCompiler.compilingLanguageModule) {
            setCLAlias(names.moduleAlias(clm));
            require(clm);
        }
        if (!that.getModuleDescriptors().isEmpty()) {
            ModuleDescriptor md = that.getModuleDescriptors().get(0);
            out("exports.$mod$ans$=");
            TypeUtils.outputAnnotationsFunction(md.getAnnotationList(), this);
            endLine(true);
            if (md.getImportModuleList() != null && !md.getImportModuleList().getImportModules().isEmpty()) {
                out("exports.$mod$imps=function(){return{");
                endLine();
                boolean first=true;
                for (ImportModule im : md.getImportModuleList().getImportModules()) {
                    final StringBuilder path=new StringBuilder("'");
                    for (Identifier id : im.getImportPath().getIdentifiers()) {
                        if (path.length()>1)path.append('.');
                        path.append(id.getText());
                    }
                    final String qv = im.getVersion().getText();
                    path.append('/').append(qv.substring(1, qv.length()-1)).append("'");
                    if (first)first=false;else{out(",");endLine();}
                    out(path.toString(), ":");
                    TypeUtils.outputAnnotationsFunction(im.getAnnotationList(), this);
                }
                endLine();
                out("};};");
                endLine();
            }
        }
        if (!that.getPackageDescriptors().isEmpty()) {
            final String pknm = that.getUnit().getPackage().getNameAsString().replaceAll("\\.", "\\$");
            out("exports.$pkg$ans$", pknm, "=");
            TypeUtils.outputAnnotationsFunction(that.getPackageDescriptors().get(0).getAnnotationList(), this);
            endLine(true);
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
        if (scope instanceof Package && !((Package)scope).getModule().equals(that.getUnit().getPackage().getModule())) {
            require(((Package) scope).getModule());
        }
    }

    private void require(Module mod) {
        final String path = scriptPath(mod);
        final String modAlias = names.moduleAlias(mod);
        if (jsout.requires.put(path, modAlias) == null) {
            out("var ", modAlias, "=require('", path, "');");
            endLine();
            if (modAlias != null && !modAlias.isEmpty()) {
                out(clAlias, "$addmod$(", modAlias,",'", mod.getNameAsString(), "/", mod.getVersion(), "');");
                endLine();
            }
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
        out(names.name(that.getParameterModel()));
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
            out(names.name(param.getParameterModel()));
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
        final List<? extends Statement> prevStatements = currentStatements;
        currentStatements = statements;

        for (int i=0; i<statements.size(); i++) {
            Statement s = statements.get(i);
            s.visit(this);
            beginNewLine();
            retainedVars.emitRetainedVars(this);
        }
        retainedVars.reset(oldRetainedVars);
        
        currentStatements = prevStatements;
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
        initSelf(block.getScope(), false);
    }
    private void initSelf(Scope scope, boolean force) {
        if (force || (scope != null && prototypeOwner == scope.getContainer()) &&
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
        if (!opts.isComment()) return;
        endLine();
        String dname = that.getNodeType();
        if (dname.endsWith("Declaration") || dname.endsWith("Definition")) {
            dname = dname.substring(0, dname.length()-7);
        }
        out("//", dname, " ", that.getDeclarationModel().getName());
        location(that);
        endLine();
    }

    private boolean share(Declaration d) {
        return share(d, true);
    }

    private boolean share(Declaration d, boolean excludeProtoMembers) {
        boolean shared = false;
        if (!(excludeProtoMembers && opts.isOptimize() && d.isClassOrInterfaceMember())
                && isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            String dname=names.name(d);
            if (dname.endsWith("()")){
                dname = dname.substring(0, dname.length()-2);
            }
            out(".", dname, "=", dname, ";");
            endLine();
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(ClassDeclaration that) {
        if (opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember()) return;
        classDeclaration(that);
    }

    private void addClassDeclarationToPrototype(TypeDeclaration outer, ClassDeclaration that) {
        classDeclaration(that);
        final String tname = names.name(that.getDeclarationModel());
        out(names.self(outer), ".", tname, "=", tname, ";");
        endLine();
    }

    private void addAliasDeclarationToPrototype(TypeDeclaration outer, Tree.TypeAliasDeclaration that) {
        comment(that);
        final TypeAlias d = that.getDeclarationModel();
        String path = qualifiedPath(that, d, true);
        if (path.length() > 0) {
            path += '.';
        }
        String tname = names.name(d);
        tname = tname.substring(0, tname.length()-2);
        String _tmp=names.createTempVariable();
        out(names.self(outer), ".", tname, "=function(){var ", _tmp, "=");
        TypeUtils.typeNameOrList(that, that.getTypeSpecifier().getType().getTypeModel(), this);
        out(";", _tmp, ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(that,d,this);
        out(";return ", _tmp, ";}");
        endLine(true);
    }

    @Override
    public void visit(InterfaceDeclaration that) {
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            interfaceDeclaration(that);
        }
    }

    private void addInterfaceDeclarationToPrototype(TypeDeclaration outer, InterfaceDeclaration that) {
        interfaceDeclaration(that);
        final String tname = names.name(that.getDeclarationModel());
        out(names.self(outer), ".", tname, "=", tname, ";");
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
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            interfaceDefinition(that);
        }
    }

    private void interfaceDefinition(InterfaceDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Interface d = that.getDeclarationModel();
        comment(that);

        out(function, names.name(d), "(");
        final List<TypeParameterDeclaration> tparms = that.getTypeParameterList() == null ? null :
            that.getTypeParameterList().getTypeParameterDeclarations();
        if (tparms != null && !tparms.isEmpty()) {
            out("$$targs$$,");
        }
        self(d);
        out(")");
        beginBlock();
        //declareSelf(d);
        referenceOuter(d);
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getInterfaceBody());
        }
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs);
        if (tparms != null && !tparms.isEmpty()) {
            out(clAlias, "set_type_args(");
            self(d);
            out(",$$targs$$)");
            endLine(true);
        }
        that.getInterfaceBody().visit(this);
        //returnSelf(d);
        endBlockNewLine();
        //Add reference to metamodel
        out(names.name(d), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
        share(d);

        typeInitialization(that);
    }

    private void addClassToPrototype(ClassOrInterface type, ClassDefinition classDef) {
        classDefinition(classDef);
        final String tname = names.name(classDef.getDeclarationModel());
        out(names.self(type), ".", tname, "=", tname, ";");
        endLine();
    }

    @Override
    public void visit(ClassDefinition that) {
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            classDefinition(that);
        }
    }

    private void interfaceDeclaration(InterfaceDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        comment(that);
        final Interface d = that.getDeclarationModel();
        final String aname = names.name(d);
        Tree.StaticType ext = that.getTypeSpecifier().getType();
        out(function, aname, "(");
        if (d.getTypeParameters() != null && !d.getTypeParameters().isEmpty()) {
            out("$$targs$$,");
        }
        self(d);
        out("){");
        final ProducedType pt = ext.getTypeModel();
        final TypeDeclaration aliased = pt.getDeclaration();
        qualify(that,aliased);
        out(names.name(aliased), "(");
        if (!pt.getTypeArguments().isEmpty()) {
            TypeUtils.printTypeArguments(that, pt.getTypeArguments(), this);
            out(",");
        }
        self(d);
        out(");}");
        endLine();
        out(aname,".$$metamodel$$=");
        TypeUtils.encodeForRuntime(that, d, this);
        endLine(true);
        share(d);
    }

    private void classDeclaration(ClassDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        comment(that);
        final Class d = that.getDeclarationModel();
        final String aname = names.name(d);
        final Tree.ClassSpecifier ext = that.getClassSpecifier();
        out(function, aname, "(");
        //Generate each parameter because we need to append one at the end
        for (Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        if (d.getTypeParameters() != null && !d.getTypeParameters().isEmpty()) {
            out("$$targs$$,");
        }
        self(d);
        out("){return ");
        TypeDeclaration aliased = ext.getType().getDeclarationModel();
        qualify(that, aliased);
        out(names.name(aliased), "(");
        if (ext.getInvocationExpression().getPositionalArgumentList() != null) {
            ext.getInvocationExpression().getPositionalArgumentList().visit(this);
            if (!ext.getInvocationExpression().getPositionalArgumentList().getPositionalArguments().isEmpty()) {
                out(",");
            }
        } else {
            out("/*PENDIENTE NAMED ARG CLASS DECL */");
        }
        Map<TypeParameter, ProducedType> invargs = ext.getType().getTypeModel().getTypeArguments();
        if (invargs != null && !invargs.isEmpty()) {
            TypeUtils.printTypeArguments(that, invargs, this);
            out(",");
        }
        self(d);
        out(");}");
        endLine();
        out(aname, ".$$=");
        qualify(that, aliased);
        out(names.name(aliased), ".$$;");
        endLine();
        out(aname,".$$metamodel$$=");
        TypeUtils.encodeForRuntime(that, d, this);
        endLine(true);
        share(d);
    }

    private void classDefinition(ClassDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Class d = that.getDeclarationModel();
        comment(that);
        out(function, names.name(d), "(");
        for (Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        final boolean withTargs = that.getTypeParameterList() != null &&
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
        initParameters(that.getParameterList(), d, null);
        
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        callSuperclass(that.getExtendedType(), d, that, superDecs);
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs);

        if (!opts.isOptimize()) {
            //Fix #231 for lexical scope
            for (Parameter p : that.getParameterList().getParameters()) {
                if (!p.getParameterModel().isHidden()){
                    generateAttributeForParameter(that, d, p.getParameterModel());
                }
            }
        }
        if (d.isNative()) {
            out("if (typeof($init$native$", names.name(d), "$before)==='function')$init$native$",
                    names.name(d), "$before(");
            self(d);
            if (withTargs)out(",$$targs$$");
            out(");");
            endLine();
        }
        that.getClassBody().visit(this);
        if (d.isNative()) {
            out("if (typeof($init$native$", names.name(d), "$after)==='function')$init$native$",
                    names.name(d), "$after(");
            self(d);
            if (withTargs)out(",$$targs$$");
            out(");");
            endLine();
            System.out.printf("%s is annotated native.", d.getQualifiedNameString());
            System.out.printf("You can implement two functions named $init$native$%s$before and $init$native$%<s$after",
                    names.name(d));
            System.out.print("that will be called (if they exist) before and after the class body. ");
            System.out.print("These functions will receive the instance being initialized");
            if (withTargs) {
                System.out.print(" and the type arguments with which it is being initialized");
            }
            System.out.println(".");
        }
        returnSelf(d);
        endBlockNewLine();
        //Add reference to metamodel
        out(names.name(d), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
        share(d);

        typeInitialization(that);
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        TypeAlias d = that.getDeclarationModel();
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) return;
        comment(that);
        final String tname=names.createTempVariable();
        out(function, names.name(d), "{var ", tname, "=");
        TypeUtils.typeNameOrList(that, that.getTypeSpecifier().getType().getTypeModel(), this);
        out(";", tname, ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(that, d, this);
        out(";return ", tname, ";}");
        endLine();
        share(d);
    }

    private void referenceOuter(TypeDeclaration d) {
        if (d.isClassOrInterfaceMember()) {
            self(d);
            out(".");
            out("$$outer");
            //outerSelf(d);
            out("=this;");
            endLine();
        }
    }

    private void copySuperMembers(TypeDeclaration typeDecl, final List<Declaration> decs, ClassOrInterface d) {
        if (!opts.isOptimize()) {
            for (Declaration dec: decs) {
                if (!typeDecl.isMember(dec)) { continue; }
                String suffix = names.scopeSuffix(dec.getContainer());
                if (dec instanceof Value && ((Value)dec).isTransient()) {
                    superGetterRef(dec,d,suffix);
                    if (((Value) dec).isVariable()) {
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
            PositionalArgumentList argList = extendedType.getInvocationExpression()
                    .getPositionalArgumentList();
            TypeDeclaration typeDecl = extendedType.getType().getDeclarationModel();
            out(memberAccessBase(extendedType.getType(), typeDecl, false, qualifiedPath(that, typeDecl)),
                    (opts.isOptimize() && (getSuperMemberScope(extendedType.getType()) != null))
                        ? ".call(this," : "(");

            invoker.generatePositionalArguments(extendedType.getInvocationExpression().getPrimary(),
                    argList, argList.getPositionalArguments(), false, false);
            if (argList.getPositionalArguments().size() > 0) {
                out(",");
            }
            //There may be defaulted args we must pass as undefined
            if (d.getExtendedTypeDeclaration().getParameterList().getParameters().size() > argList.getPositionalArguments().size()) {
                List<com.redhat.ceylon.compiler.typechecker.model.Parameter> superParams = d.getExtendedTypeDeclaration().getParameterList().getParameters();
                for (int i = argList.getPositionalArguments().size(); i < superParams.size(); i++) {
                    com.redhat.ceylon.compiler.typechecker.model.Parameter p = superParams.get(i);
                    if (p.isSequenced()) {
                        out(clAlias, "getEmpty(),");
                    } else {
                        out("undefined,");
                    }
                }
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

    private void callInterfaces(SatisfiedTypes satisfiedTypes, ClassOrInterface d, Tree.StatementOrArgument that,
            final List<Declaration> superDecs) {
        if (satisfiedTypes!=null) {
            for (StaticType st: satisfiedTypes.getTypes()) {
                TypeDeclaration typeDecl = st.getTypeModel().getDeclaration();
                qualify(that, typeDecl);
                out(names.name((ClassOrInterface)typeDecl), "(");
                if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                    if (d.getTypeParameters() != null && !d.getTypeParameters().isEmpty()) {
                        self(d);
                        out(".$$targs$$===undefined?$$targs$$:");
                    }
                    TypeUtils.printTypeArguments(that, st.getTypeModel().getTypeArguments(), this);
                    out(",");
                }
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
                            TypeUtils.typeNameOrList(that, e.getValue(), this);
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
        final ClassOrInterface decl;
        final List<Statement> stmts;
        if (type instanceof ClassDefinition) {
            ClassDefinition classDef = (ClassDefinition) type;
            extendedType = classDef.getExtendedType();
            satisfiedTypes = classDef.getSatisfiedTypes();
            decl = classDef.getDeclarationModel();
            stmts = classDef.getClassBody().getStatements();
        } else if (type instanceof InterfaceDefinition) {
            satisfiedTypes = ((InterfaceDefinition) type).getSatisfiedTypes();
            decl = ((InterfaceDefinition) type).getDeclarationModel();
            stmts = ((InterfaceDefinition) type).getInterfaceBody().getStatements();
        } else if (type instanceof ObjectDefinition) {
            ObjectDefinition objectDef = (ObjectDefinition) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = (ClassOrInterface)objectDef.getDeclarationModel().getTypeDeclaration();
            stmts = objectDef.getClassBody().getStatements();
        } else {
            stmts = null;
            decl = null;
        }
        final PrototypeInitCallback callback = new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                if (decl != null) {
                    addToPrototype(type, decl, stmts);
                }
            }
        };
        typeInitialization(extendedType, satisfiedTypes, decl, callback);
    }

    /** This is now the main method to generate the type initialization code.
     * @param extendedType The type that is being extended.
     * @param satisfiedTypes The types satisfied by the type being initialized.
     * @param d The declaration for the type being initialized
     * @param callback A callback to add something more to the type initializer in prototype style.
     */
    private void typeInitialization(ExtendedType extendedType, SatisfiedTypes satisfiedTypes,
            final ClassOrInterface d, PrototypeInitCallback callback) {

        final boolean isInterface = d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
        String initFuncName = isInterface ? "initTypeProtoI" : "initTypeProto";

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
            String fname = typeFunctionName(extendedType.getType(), !extendedType.getType().getDeclarationModel().isMember());
            out(",", fname);
        } else if (!isInterface) {
            out(",", clAlias, "Basic");
        }

        if (satisfiedTypes != null) {
            for (StaticType satType : satisfiedTypes.getTypes()) {
                String fname = typeFunctionName(satType, true);
                out(",", fname);
            }
        }

        out(");");
        //Add ref to outer type
        if (d.isMember()) {
            StringBuilder containers = new StringBuilder();
            Scope _d2 = d;
            while (_d2 instanceof ClassOrInterface) {
                if (containers.length() > 0) {
                    containers.insert(0, '.');
                }
                containers.insert(0, names.name((Declaration)_d2));
                _d2 = _d2.getScope();
            }
            endLine();
            out(containers.toString(), "=", names.name(d), ";");
        }

        //The class definition needs to be inside the init function if we want forwards decls to work in prototype style
        if (opts.isOptimize()) {
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

    /** Returns the name of the type or its $init$ function if it's local. */
    private String typeFunctionName(StaticType type, boolean removeAlias) {
        TypeDeclaration d = type.getTypeModel().getDeclaration();
        if (removeAlias && d.isAlias()) {
            d = d.getExtendedTypeDeclaration();
        }
        boolean inProto = opts.isOptimize()
                && (type.getScope().getContainer() instanceof TypeDeclaration);
        String tfn = memberAccessBase(type, d, false, qualifiedPath(type, d, inProto));
        if (removeAlias && !isImported(type.getUnit().getPackage(), d)) {
            int idx = tfn.lastIndexOf('.');
            if (idx > 0) {
                tfn = tfn.substring(0, idx+1) + "$init$" + tfn.substring(idx+1) + "()";
            } else {
                tfn = "$init$" + tfn + "()";
            }
        }
        return tfn;
    }

    private void addToPrototype(Node node, ClassOrInterface d, List<Statement> statements) {
        boolean enter = opts.isOptimize();
        ArrayList<com.redhat.ceylon.compiler.typechecker.model.Parameter> plist = null;
        if (enter) {
            enter = !statements.isEmpty();
            if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
                com.redhat.ceylon.compiler.typechecker.model.ParameterList _pl =
                        ((com.redhat.ceylon.compiler.typechecker.model.Class)d).getParameterList();
                if (_pl != null) {
                    plist = new ArrayList<>(_pl.getParameters().size());
                    plist.addAll(_pl.getParameters());
                    enter |= !plist.isEmpty();
                }
            }
        }
        if (enter) {
            final List<? extends Statement> prevStatements = currentStatements;
            currentStatements = statements;
            
            out("(function(", names.self(d), ")");
            beginBlock();
            for (Statement s: statements) {
                addToPrototype(d, s, plist);
            }
            //Generated attributes with corresponding parameters will remove them from the list
            if (plist != null) {
                for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : plist) {
                    generateAttributeForParameter(node, (com.redhat.ceylon.compiler.typechecker.model.Class)d, p);
                }
            }
            endBlock();
            out(")(", names.name(d), ".$$.prototype);");
            endLine();
            
            currentStatements = prevStatements;
        }
    }

    private void generateAttributeForParameter(Node node, com.redhat.ceylon.compiler.typechecker.model.Class d,
            com.redhat.ceylon.compiler.typechecker.model.Parameter p) {
        final String privname = names.name(p) + "_";
        out(clAlias, "defineAttr(", names.self(d), ",'", names.name(p.getModel()),
                "',function(){");
        if (p.getModel().isLate()) {
            generateUnitializedAttributeReadCheck("this."+privname, names.name(p));
        }
        out("return this.", privname, ";}");
        if (p.getModel().isVariable() || p.getModel().isLate()) {
            final String param = names.createTempVariable(d.getName());
            out(",function(", param, "){");
            if (p.getModel().isLate() && !p.getModel().isVariable()) {
                generateImmutableAttributeReassignmentCheck("this."+privname, names.name(p));
            }
            out("return this.", privname,
                    "=", param, ";}");
        } else {
            out(",undefined");
        }
        out(",");
        TypeUtils.encodeForRuntime(node, p.getModel(), this);
        out(");");
        endLine();
    }

    private ClassOrInterface prototypeOwner;

    private void addToPrototype(ClassOrInterface d, Statement s,
            List<com.redhat.ceylon.compiler.typechecker.model.Parameter> params) {
        ClassOrInterface oldPrototypeOwner = prototypeOwner;
        prototypeOwner = d;
        if (s instanceof MethodDefinition) {
            addMethodToPrototype(d, (MethodDefinition)s);
        } else if (s instanceof MethodDeclaration) {
            methodDeclaration(d, (MethodDeclaration) s);
        } else if (s instanceof AttributeGetterDefinition) {
            addGetterToPrototype(d, (AttributeGetterDefinition)s);
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
        } else if (s instanceof TypeAliasDeclaration) {
            addAliasDeclarationToPrototype(d, (TypeAliasDeclaration)s);
        }
        //This fixes #231 for prototype style
        if (params != null && s instanceof Tree.Declaration) {
            Declaration m = ((Tree.Declaration)s).getDeclarationModel();
            for (Iterator<com.redhat.ceylon.compiler.typechecker.model.Parameter> iter = params.iterator();
                    iter.hasNext();) {
                com.redhat.ceylon.compiler.typechecker.model.Parameter _p = iter.next();
                if (m.getName() != null && m.getName().equals(_p.getName())) {
                    iter.remove();
                    break;
                }
            }
        }
        prototypeOwner = oldPrototypeOwner;
    }

    private void declareSelf(ClassOrInterface d) {
        out("if (");
        self(d);
        out("===undefined)");
        if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class && d.isAbstract()) {
            out(clAlias, "throwexc(", clAlias, "InvocationException$meta$model(", clAlias, "String");
            if (JsCompiler.isCompilingLanguageModule()) {
                out("$");
            }
            out("(\"Cannot instantiate abstract class\")),'?','?')");
        } else {
            self(d);
            out("=new ");
            if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
                out("this.", names.name(d), ".$$;");
            } else {
                out(names.name(d), ".$$;");
            }
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
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
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
        out(names.self(type), ".", names.name(c), "=", names.name(c), ";",
                names.self(type), ".", names.name(c), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(objDef, d, this);
        endLine(true);
    }

    @Override
    public void visit(ObjectDefinition that) {
        Value d = that.getDeclarationModel();
        if (!(opts.isOptimize() && d.isClassOrInterfaceMember())) {
            objectDefinition(that);
        } else {
            //Don't even bother with nodes that have errors
            if (errVisitor.hasErrors(that))return;
            Class c = (Class) d.getTypeDeclaration();
            comment(that);
            outerSelf(d);
            out(".", names.privateName(d), "=");
            outerSelf(d);
            out(".", names.name(c), "();");
            endLine();
        }
    }

    private void objectDefinition(ObjectDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        comment(that);
        final Value d = that.getDeclarationModel();
        boolean addToPrototype = opts.isOptimize() && d.isClassOrInterfaceMember();
        final Class c = (Class) d.getTypeDeclaration();

        out(function, names.name(c));
        Map<TypeParameter, ProducedType> targs=new HashMap<TypeParameter, ProducedType>();
        if (that.getSatisfiedTypes() != null) {
            for (StaticType st : that.getSatisfiedTypes().getTypes()) {
                Map<TypeParameter, ProducedType> stargs = st.getTypeModel().getTypeArguments();
                if (stargs != null && !stargs.isEmpty()) {
                    targs.putAll(stargs);
                }
            }
        }
        out(targs.isEmpty()?"()":"($$targs$$)");
        beginBlock();
        instantiateSelf(c);
        referenceOuter(c);
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        if (!targs.isEmpty()) {
            self(c); out(".$$targs$$=$$targs$$;"); endLine();
        }
        callSuperclass(that.getExtendedType(), c, that, superDecs);
        callInterfaces(that.getSatisfiedTypes(), c, that, superDecs);
        
        that.getClassBody().visit(this);
        returnSelf(c);
        indentLevel--;
        endLine();
        out("};", names.name(c), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(that, c, this);
        endLine(true);

        typeInitialization(that);

        if (!addToPrototype) {
            out("var ", names.name(d));
            //If it's a property, create the object here
            if (defineAsProperty(d)) {
                out("=", names.name(c), "(");
                if (!targs.isEmpty()) {
                    TypeUtils.printTypeArguments(that, targs, this);
                }
                out(")");
            }
            endLine(true);
        }

        if (!defineAsProperty(d)) {
            final String objvar = (addToPrototype ? "this.":"")+names.name(d);
            out(function, names.getter(d), "()");
            beginBlock();
            //Create the object lazily
            out("if (", objvar, "===undefined){", objvar, "=$init$", names.name(c), "()(");
            if (!targs.isEmpty()) {
                TypeUtils.printTypeArguments(that, targs, this);
            }
            out(");", objvar, ".$$metamodel$$=", names.getter(d), ".$$metamodel$$;}");
            endLine();
            out("return ", objvar, ";");
            endBlockNewLine();            
            
            if (addToPrototype || d.isShared()) {
                outerSelf(d);
                out(".", names.getter(d), "=", names.getter(d), ";");
                endLine();
            }
            if (!d.isToplevel()) {
                if(outerSelf(d))out(".");
            }
            out(names.getter(d), ".$$metamodel$$=");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            endLine(true);
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out("$prop$", names.getter(d), "={get:");
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out(names.getter(d), ",$$metamodel$$:");
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out(names.getter(d), ".$$metamodel$$};");
            endLine();
            if (d.isToplevel()) {
                out("exports.$prop$", names.getter(d), "=$prop$", names.getter(d));
                endLine(true);
            }
        }
        else {
            out(clAlias, "defineAttr(");
            outerSelf(d);
            out(",'", names.name(d), "',function(){return ");
            if (addToPrototype) {
                out("this.", names.privateName(d));
            } else {
                out(names.name(d));
            }
            out(";},undefined,");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            out(");");
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
        if (defineAsProperty(d)) {
            out(clAlias, "copySuperAttr(", names.self(sub), ",'", names.name(d), "','",
                    parentSuffix, "');");
        }
        else {
            self(sub);
            out(".", names.getter(d), parentSuffix, "=");
            self(sub);
            out(".", names.getter(d), ";");
        }
        endLine();
    }

    private void superSetterRef(Declaration d, ClassOrInterface sub, String parentSuffix) {
        if (!defineAsProperty(d)) {
            self(sub);
            out(".", names.setter(d), parentSuffix, "=");
            self(sub);
            out(".", names.setter(d), ";");
            endLine();
        }
    }

    @Override
    public void visit(MethodDeclaration that) {
        methodDeclaration(null, that);
    }
    
    private void methodDeclaration(TypeDeclaration outer, MethodDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Method m = that.getDeclarationModel();
        if (that.getSpecifierExpression() != null) {
            // method(params) => expr
            if (outer == null) {
                // Not in a prototype definition. Null to do here if it's a
                // member in prototype style.
                if (opts.isOptimize() && m.isMember()) { return; }
                comment(that);
                initDefaultedParameters(that.getParameterLists().get(0), m);
                out("var ");
            }
            else {
                // prototype definition
                comment(that);
                initDefaultedParameters(that.getParameterLists().get(0), m);
                out(names.self(outer), ".");
            }
            out(names.name(m), "=");            
            singleExprFunction(that.getParameterLists(),
                    that.getSpecifierExpression().getExpression(), that.getScope());
            endLine(true);
            if (outer != null) {
                out(names.self(outer), ".");
            }
            out(names.name(m), ".$$metamodel$$=");
            TypeUtils.encodeForRuntime(m, that.getAnnotationList(), this);
            endLine(true);
            share(m);
        }
        else if (outer == null // don't do the following in a prototype definition
                && m == that.getScope()) { //Check for refinement of simple param declaration
            
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
            //Only the first paramlist can have defaults
            initDefaultedParameters(that.getParameterLists().get(0), m);
        } else if (m.isFormal() && m.isMember() && m == that.getScope()) {
            if (m.getContainer() instanceof TypeDeclaration) {
                self((TypeDeclaration)m.getContainer());
                out(".", names.name(m),"={$fml:1,$$metamodel$$:");
                TypeUtils.encodeForRuntime(that, m, this);
                out("};");
            }
        }
    }

    @Override
    public void visit(MethodDefinition that) {
        final Method d = that.getDeclarationModel();
        if (!((opts.isOptimize() && d.isClassOrInterfaceMember()) || isNative(d))) {
            comment(that);
            initDefaultedParameters(that.getParameterLists().get(0), d);
            methodDefinition(that);
            //Add reference to metamodel
            out(names.name(d), ".$$metamodel$$=");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            endLine(true);
        }
    }

    private void methodDefinition(MethodDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Method d = that.getDeclarationModel();
        if (that.getParameterLists().size() == 1) {
            out(function, names.name(d));
            ParameterList paramList = that.getParameterLists().get(0);
            paramList.visit(this);
            beginBlock();
            initSelf(that.getBlock());
            initParameters(paramList, null, d);
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
                initParameters(paramList, d.getTypeDeclaration(), d);
                count++;
            }
            visitStatements(that.getBlock().getStatements());
            for (int i=0; i < count; i++) {
                endBlock();
            }
        }

        if (!share(d)) { out(";"); }
    }

    /** Get the specifier expression for a Parameter, if one is available. */
    private SpecifierOrInitializerExpression getDefaultExpression(Parameter param) {
        final SpecifierOrInitializerExpression expr;
        if (param instanceof ParameterDeclaration || param instanceof InitializerParameter) {
            MethodDeclaration md = null;
            if (param instanceof ParameterDeclaration) {
                TypedDeclaration td = ((ParameterDeclaration) param).getTypedDeclaration();
                if (td instanceof AttributeDeclaration) {
                    expr = ((AttributeDeclaration) td).getSpecifierOrInitializerExpression();
                } else if (td instanceof MethodDeclaration) {
                    md = (MethodDeclaration)td;
                    expr = md.getSpecifierExpression();
                } else {
                    param.addUnexpectedError("Don't know what to do with TypedDeclaration " + td.getClass().getName());
                    expr = null;
                }
            } else {
                expr = ((InitializerParameter) param).getSpecifierExpression();
            }
        } else {
            param.addUnexpectedError("Don't know what to do with defaulted/sequenced param " + param);
            expr = null;
        }
        return expr;
    }

    /** Create special functions with the expressions for defaulted parameters in a parameter list. */
    private void initDefaultedParameters(ParameterList params, Method container) {
        if (!container.isMember())return;
        for (final Parameter param : params.getParameters()) {
            com.redhat.ceylon.compiler.typechecker.model.Parameter pd = param.getParameterModel();
            if (pd.isDefaulted()) {
                final SpecifierOrInitializerExpression expr = getDefaultExpression(param);
                if (expr == null) {
                    continue;
                }
                qualify(params, container);
                out(names.name(container), "$defs$", pd.getName(), "=function");
                params.visit(this);
                out("{");
                initSelf(container, false);
                out("return ");
                if (param instanceof ParameterDeclaration &&
                        ((ParameterDeclaration)param).getTypedDeclaration() instanceof MethodDeclaration) {
                    // function parameter defaulted using "=>"
                    singleExprFunction(
                            ((MethodDeclaration)((ParameterDeclaration)param).getTypedDeclaration()).getParameterLists(),
                            expr.getExpression(), null);
                } else {
                    expr.visit(this);
                }
                out(";}");
                endLine(true);
            }
        }
    }

    /** Initialize the sequenced, defaulted and captured parameters in a type declaration. */
    private void initParameters(ParameterList params, TypeDeclaration typeDecl, Method m) {
        for (final Parameter param : params.getParameters()) {
            com.redhat.ceylon.compiler.typechecker.model.Parameter pd = param.getParameterModel();
            final String paramName = names.name(pd);
            if (pd.isDefaulted() || pd.isSequenced()) {
                out("if(", paramName, "===undefined){", paramName, "=");
                if (pd.isDefaulted()) {
                    if (m !=null && m.isMember()) {
                        qualify(params, m);
                        out(names.name(m), "$defs$", pd.getName(), "(");
                        boolean firstParam=true;
                        for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : m.getParameterLists().get(0).getParameters()) {
                            if (firstParam){firstParam=false;}else out(",");
                            out(names.name(p));
                        }
                        out(")");
                    } else {
                        final SpecifierOrInitializerExpression expr = getDefaultExpression(param);
                        if (expr == null) {
                            param.addUnexpectedError("Default expression missing for " + pd.getName());
                            out("null");
                        } else if (param instanceof ParameterDeclaration &&
                                ((ParameterDeclaration)param).getTypedDeclaration() instanceof MethodDeclaration) {
                            // function parameter defaulted using "=>"
                            singleExprFunction(
                                    ((MethodDeclaration)((ParameterDeclaration)param).getTypedDeclaration()).getParameterLists(),
                                    expr.getExpression(), m.getContainer());
                        } else {
                            expr.visit(this);
                        }
                    }
                } else {
                    out(clAlias, "getEmpty()");
                }
                out(";}");
                endLine();
            }
            if ((typeDecl != null) && !pd.isHidden() && pd.getModel().isCaptured()) {
                self(typeDecl);
                out(".", paramName, "_=", paramName, ";");
                endLine();
            }
        }
    }

    private void addMethodToPrototype(TypeDeclaration outer,
            MethodDefinition that) {
        Method d = that.getDeclarationModel();
        if (!opts.isOptimize()||!d.isClassOrInterfaceMember()) return;
        comment(that);
        initDefaultedParameters(that.getParameterLists().get(0), d);
        out(names.self(outer), ".", names.name(d), "=");
        methodDefinition(that);
        //Add reference to metamodel
        out(names.self(outer), ".", names.name(d), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
    }

    @Override
    public void visit(AttributeGetterDefinition that) {
        Value d = that.getDeclarationModel();
        if (opts.isOptimize()&&d.isClassOrInterfaceMember()) return;
        comment(that);
        if (defineAsProperty(d)) {
            out(clAlias, "defineAttr(");
            outerSelf(d);
            out(",'", names.name(d), "',function()");
            super.visit(that);
            final AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
            if (setterDef == null) {
                out(",undefined");
            } else {
                out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
                if (setterDef.getSpecifierExpression() == null) {
                    super.visit(setterDef);
                } else {
                    out("{return ");
                    setterDef.getSpecifierExpression().visit(this);
                    out(";}");
                }
            }
            out(",");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            out(");");
        }
        else {
            out(function, names.getter(d), "()");
            super.visit(that);
            endLine();
            if (!shareGetter(d)) { out(";"); }
            generateAttributeMetamodel(that, true, false);
        }
    }

    private void addGetterToPrototype(TypeDeclaration outer,
            AttributeGetterDefinition that) {
        Value d = that.getDeclarationModel();
        if (!opts.isOptimize()||!d.isClassOrInterfaceMember()) return;
        comment(that);
        out(clAlias, "defineAttr(", names.self(outer), ",'", names.name(d),
                "',function()");
        super.visit(that);
        final AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
        if (setterDef == null) {
            out(",undefined");
        } else {
            out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
            if (setterDef.getSpecifierExpression() == null) {
                super.visit(setterDef);
            } else {
                out("{return ");
                setterDef.getSpecifierExpression().visit(this);
                out(";}");
            }
        }
        out(",");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        if (d.getSetter() != null && d.getSetter().getAnnotations() != null && !d.getSetter().getAnnotations().isEmpty()) {
            out(",");
            new TypeUtils.ModelAnnotationGenerator(this, d.getSetter(), that).omitKey().generateAnnotations();
        }
        out(");");
    }
    
    private AttributeSetterDefinition associatedSetterDefinition(
            Value valueDecl) {
        final Setter setter = valueDecl.getSetter();
        if ((setter != null) && (currentStatements != null)) {
            for (Statement stmt : currentStatements) {
                if (stmt instanceof AttributeSetterDefinition) {
                    final AttributeSetterDefinition setterDef =
                            (AttributeSetterDefinition) stmt;
                    if (setterDef.getDeclarationModel() == setter) {
                        return setterDef;
                    }
                }
            }
        }
        return null;
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
        if ((opts.isOptimize()&&d.isClassOrInterfaceMember()) || defineAsProperty(d)) return;
        comment(that);
        out("var ", names.setter(d.getGetter()), "=function(", names.name(d.getParameter()), ")");
        if (that.getSpecifierExpression() == null) {
            that.getBlock().visit(this);
        } else {
            out("{return ");
            that.getSpecifierExpression().visit(this);
            out(";}");
        }
        if (!shareSetter(d)) { out(";"); }
        if (!d.isToplevel())outerSelf(d);
        out(names.setter(d.getGetter()), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
        generateAttributeMetamodel(that, false, true);
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
        if (d.isFormal()) {
            if (!opts.isOptimize())generateAttributeMetamodel(that, false, false);
        } else {
            comment(that);
            final boolean isLate = d.isLate();
            SpecifierOrInitializerExpression specInitExpr =
                        that.getSpecifierOrInitializerExpression();
            final boolean addGetter = (specInitExpr != null) || (classParam != null) || !d.isMember()
                    || d.isVariable() || isLate;
            final boolean addSetter = (d.isVariable() || isLate) && !defineAsProperty(d);
            if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
                if ((specInitExpr != null
                        && !(specInitExpr instanceof LazySpecifierExpression)) || isLate) {
                    outerSelf(d);
                    out(".", names.privateName(d), "=");
                    if (isLate) {
                        out("undefined");
                    } else {
                        super.visit(that);
                    }
                    endLine(true);
                } else if (classParam != null) {
                    outerSelf(d);
                    out(".", names.privateName(d), "=", classParam);
                    endLine(true);
                }
            }
            else if (specInitExpr instanceof LazySpecifierExpression) {
                final boolean property = defineAsProperty(d);
                if (property) {
                    out(clAlias, "defineAttr(");
                    outerSelf(d);
                    out(",'", names.name(d), "',function(){return ");
                } else {
                    out(function, names.getter(d), "(){return ");
                }
                int boxType = boxStart(specInitExpr.getExpression().getTerm());
                specInitExpr.getExpression().visit(this);
                if (boxType == 4) out("/*TODO: callable targs 1*/");
                boxUnboxEnd(boxType);
                out(";}");
                if (property) {
                    boolean hasSetter = false;
                    if (d.isVariable()) {
                        Tree.AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
                        if (setterDef != null) {
                            hasSetter = true;
                            out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
                            if (setterDef.getSpecifierExpression() == null) {
                                super.visit(setterDef);
                            } else {
                                out("{return ");
                                setterDef.getSpecifierExpression().visit(this);
                                out(";}");
                            }
                        }
                    }
                    if (!hasSetter) {
                        out(",undefined");
                    }
                    out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
                    out(");");
                    endLine();
                } else {
                    endLine(true);
                    shareGetter(d);
                }
            }
            else {
                if (addGetter) {
                    generateAttributeGetter(that, d, specInitExpr, classParam);
                }
                if (addSetter) {
                    final String varName = names.name(d);
                    String paramVarName = names.createTempVariable(d.getName());
                    out(function, names.setter(d), "(", paramVarName, "){");
                    if (isLate) {
                        generateImmutableAttributeReassignmentCheck(varName, names.name(d));
                    }
                    out("return ", varName, "=", paramVarName, ";};");
                    endLine();
                    shareSetter(d);
                }
            }
            generateAttributeMetamodel(that, addGetter, addSetter);
        }
    }

    /** Generate runtime metamodel info for an attribute declaration or definition. */
    private void generateAttributeMetamodel(Tree.TypedDeclaration that, final boolean addGetter, final boolean addSetter) {
        //No need to define all this for local values
        if (that.getScope() instanceof Method)return;
        Declaration d = that.getDeclarationModel();
        if (d instanceof Setter) d = ((Setter)d).getGetter();
        final String pname = names.getter(d);
        if (!generatedAttributes.contains(pname)) {
            if (d.isToplevel()) {
                out("var ");
            } else if (outerSelf(d)) {
                out(".");
            }
            out("$prop$", pname, "={$$metamodel$$:");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            out("};"); endLine();
            if (d.isToplevel()) {
                out("exports.$prop$", pname, "=$prop$", pname);
                endLine(true);
            }
            generatedAttributes.add(pname);
        }
        if (addGetter) {
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out("$prop$", pname, ".get=");
            if (isCaptured(d) && !defineAsProperty(d)) {
                out(pname);
                endLine(true);
                out(pname, ".$$metamodel$$=$prop$", pname, ".$$metamodel$$");
            } else {
                out("function(){return ", names.name(d), "}");
            }
            endLine(true);
        }
        if (addSetter) {
            final String pset = names.setter(d instanceof Setter ? ((Setter)d).getGetter() : d);
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out("$prop$", pname, ".set=", pset);
            endLine(true);
            out("if (", pset, ".$$metamodel$$===undefined)", pset, ".$$metamodel$$=$prop$", pname, ".$$metamodel$$");
            endLine(true);
        }
    }

    private void generateAttributeGetter(AnyAttribute attributeNode, MethodOrValue decl,
                SpecifierOrInitializerExpression expr, String param) {
        final String varName = names.name(decl);
        final boolean initVal = expr != null && decl.isToplevel();
        out("var ", varName);
        if (expr != null) {
            if (initVal) {
                out(";function $valinit$", varName, "(){if (", varName, "===undefined)", varName, "=");
            } else {
                out("=");
            }
            int boxType = boxStart(expr.getExpression().getTerm());
            if (dynblock > 0 && TypeUtils.isUnknown(expr.getExpression().getTypeModel()) && !TypeUtils.isUnknown(decl.getType())) {
                TypeUtils.generateDynamicCheck(expr.getExpression(), decl.getType(), this);
            } else {
                expr.visit(this);
            }
            if (boxType == 4) {
                //Pass Callable argument types
                out(",");
                if (decl instanceof Method) {
                    //Add parameters
                    TypeUtils.encodeParameterListForRuntime(attributeNode, ((Method)decl).getParameterLists().get(0),
                            GenerateJsVisitor.this);
                } else {
                    //Type of value must be Callable
                    //And the Args Type Parameters is a Tuple
                    TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(expr.getExpression().getTypeModel(), this);
                }
                out(",");
                TypeUtils.printTypeArguments(expr, expr.getExpression().getTypeModel().getTypeArguments(), this);
            }
            boxUnboxEnd(boxType);
            if (initVal) {
                out(";return ", varName, ";};$valinit$", varName, "()");
            }
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
            if (isCaptured(decl) || decl.isToplevel()) {
                final boolean isLate = decl.isLate();
                if (defineAsProperty(decl)) {
                    out(clAlias, "defineAttr(");
                    outerSelf(decl);
                    out(",'", varName, "',function(){");
                    if (isLate) {
                        generateUnitializedAttributeReadCheck(varName, names.name(decl));
                    }
                    if (initVal) {
                        out("return $valinit$", varName, "();}");
                    } else {
                        out("return ", varName, ";}");
                    }
                    if (decl.isVariable() || isLate) {
                        final String par = names.createTempVariable(decl.getName());
                        out(",function(", par, "){");
                        if (isLate && !decl.isVariable()) {
                            generateImmutableAttributeReassignmentCheck(varName, names.name(decl));
                        }
                        out("return ", varName, "=", par, ";}");
                    } else {
                        out(",undefined");
                    }
                    out(",");
                    if (attributeNode == null) {
                        TypeUtils.encodeForRuntime(attributeNode, decl, this);
                    } else {
                        TypeUtils.encodeForRuntime(decl, attributeNode.getAnnotationList(), this);
                    }
                    out(");");
                    endLine();
                }
                else {
                    out(function, names.getter(decl),"(){return ");
                    if (initVal) {
                        out("$valinit$", varName, "();}");
                    } else {
                        out(varName, ";}");
                    }
                    endLine();
                    shareGetter(decl);
                }
            } else {
                directAccess.add(decl);
            }
        }
    }

    void generateUnitializedAttributeReadCheck(String privname, String pubname) {
        //TODO we can later optimize this, to replace this getter with the plain one
        //once the value has been defined
        out("if (", privname, "===undefined)throw ", clAlias, "InitializationException(");
        if (JsCompiler.compilingLanguageModule) {
            out("String$('");
        } else {
            out(clAlias, "String('");
        }
        out("Attempt to read unitialized attribute «", pubname, "»'));");
    }
    void generateImmutableAttributeReassignmentCheck(String privname, String pubname) {
        out("if(", privname, "!==undefined)throw ", clAlias, "InitializationException(");
        if (JsCompiler.compilingLanguageModule) {
            out("String$('");
        } else {
            out(clAlias, "String('");
        }
        out("Attempt to reassign immutable attribute «", pubname, "»'));");
    }

    private void addGetterAndSetterToPrototype(TypeDeclaration outer,
            AttributeDeclaration that) {
        Value d = that.getDeclarationModel();
        if (!opts.isOptimize()||d.isToplevel()) return;
        comment(that);
        if (d.isFormal()) {
            generateAttributeMetamodel(that, false, false);
        } else {
            String classParam = null;
            if (d.getContainer() instanceof Functional) {
                classParam = names.name(((Functional)d.getContainer()).getParameter(d.getName()));
            }
            final boolean isLate = d.isLate();
            if ((that.getSpecifierOrInitializerExpression() != null) || d.isVariable()
                        || classParam != null || isLate) {
                if (that.getSpecifierOrInitializerExpression()
                                instanceof LazySpecifierExpression) {
                    // attribute is defined by a lazy expression ("=>" syntax)
                    out(clAlias, "defineAttr(", names.self(outer), ",'", names.name(d),
                            "',function()");
                    beginBlock();
                    initSelf(that.getScope(), true);
                    out("return ");
                    Expression expr = that.getSpecifierOrInitializerExpression().getExpression();
                    int boxType = boxStart(expr.getTerm());
                    expr.visit(this);
                    endLine(true);
                    if (boxType == 4) out("/*TODO: callable targs 3*/");
                    boxUnboxEnd(boxType);
                    endBlock();
                    boolean hasSetter = false;
                    if (d.isVariable()) {
                        Tree.AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
                        if (setterDef != null) {
                            hasSetter = true;
                            out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
                            if (setterDef.getSpecifierExpression() == null) {
                                super.visit(setterDef);
                            } else {
                                out("{");
                                initSelf(that.getScope(), true);
                                out("return ");
                                setterDef.getSpecifierExpression().visit(this);
                                out(";}");
                            }
                        }
                    }
                    if (!hasSetter) {
                        out(",undefined");
                    }
                    out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
                    out(")");
                    endLine(true);
                }
                else {
                    final String privname = names.privateName(d);
                    out(clAlias, "defineAttr(", names.self(outer), ",'", names.name(d),
                            "',function(){");
                    if (isLate) {
                        generateUnitializedAttributeReadCheck("this."+privname, names.name(d));
                    }
                    out("return this.", privname, ";}");
                    if (d.isVariable() || isLate) {
                        final String param = names.createTempVariable(d.getName());
                        out(",function(", param, "){");
                        if (isLate && !d.isVariable()) {
                            generateImmutableAttributeReassignmentCheck("this."+privname, names.name(d));
                        }
                        out("return this.", privname,
                                "=", param, ";}");
                    } else {
                        out(",undefined");
                    }
                    out(",");
                    TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
                    out(");");
                    endLine();
                }
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
        for (int i=0; i < text.length();i++) {
            switch(text.charAt(i)) {
            case 8:text.replace(i, i+1, "\\b"); i++; break;
            case 9:text.replace(i, i+1, "\\t"); i++; break;
            case 10:text.replace(i, i+1, "\\n"); i++; break;
            case 12:text.replace(i, i+1, "\\f"); i++; break;
            case 13:text.replace(i, i+1, "\\r"); i++; break;
            case 34:text.replace(i, i+1, "\\\""); i++; break;
            case 39:text.replace(i, i+1, "\\'"); i++; break;
            case 92:text.replace(i, i+1, "\\\\"); i++; break;
            case 0x2028:text.replace(i, i+1, "\\u2028"); i++; break;
            case 0x2029:text.replace(i, i+1, "\\u2029"); i++; break;
            }
        }
        return text.toString();
    }

    @Override
    public void visit(StringLiteral that) {
        final int slen = that.getText().codePointCount(0, that.getText().length());
        if (JsCompiler.compilingLanguageModule) {
            out("String$(\"", escapeStringLiteral(that.getText()), "\",", Integer.toString(slen), ")");
        } else {
            out(clAlias, "String(\"", escapeStringLiteral(that.getText()), "\",", Integer.toString(slen), ")");
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
            if (!literal.getText().isEmpty()) {
                if (!first) { out(","); }
                first = false;
                literal.visit(this);
            }
            if (i < exprs.size()) {
                if (!first) { out(","); }
                first = false;
                exprs.get(i).visit(this);
                out(".string");
            }
        }
        out("]).string");
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
        boolean outer = false;
        if (opts.isOptimize()) {
            Scope scope = that.getScope();
            while ((scope != null) && !(scope instanceof TypeDeclaration)) {
                scope = scope.getContainer();
            }
            if (scope != null && ((TypeDeclaration)scope).isClassOrInterfaceMember()) {
                self((TypeDeclaration) scope);
                out(".");
                outer = true;
            }
        }
        if (outer) {
            out("$$outer");
        } else {
            self(that.getTypeModel().getDeclaration());
        }
    }

    @Override
    public void visit(Tree.BaseMemberExpression that) {
        Declaration decl = that.getDeclaration();
        if (decl != null) {
            String name = decl.getName();
            String pkgName = decl.getUnit().getPackage().getQualifiedNameString();

            // map Ceylon true/false/null directly to JS true/false/null
            if ("ceylon.language".equals(pkgName)) {
                if ("true".equals(name) || "false".equals(name) || "null".equals(name)) {
                    out(name);
                    return;
                }
            }
        }
        String exp = memberAccess(that, null);
        if (decl == null && isInDynamicBlock()) {
            out("(typeof ", exp, "==='undefined'||", exp, "===null?");
            generateThrow("Undefined or null reference: " + exp, that);
            out(":", exp, ")");
        } else {
            out(exp);
        }
    }

    private boolean accessDirectly(Declaration d) {
        return !accessThroughGetter(d) || directAccess.contains(d) || d.isParameter();
    }

    private boolean accessThroughGetter(Declaration d) {
        return (d instanceof MethodOrValue) && !(d instanceof Method)
                && !defineAsProperty(d);
    }
    
    private boolean defineAsProperty(Declaration d) {
        // for now, only define member attributes as properties, not toplevel attributes
        return d.isMember() && (d instanceof MethodOrValue) && !(d instanceof Method);
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
        return hasAnnotationByName(getToplevel(d), "nativejs") || TypeUtils.isUnknown(d);
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
        out(lhsVar, "!==null?", memberAccess(that, lhsVar), ":null)");
        if (isMethod) {
            out(")");
        }
    }

    @Override
    public void visit(final QualifiedMemberExpression that) {
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
        } else if (that.getStaticMethodReference()) {
            out("function($O$) {return ");
            if (that.getDeclaration() instanceof Method) {
                out(clAlias, "JsCallable($O$,$O$.", names.name(that.getDeclaration()), ");}");
            } else {
                out("$O$.", names.name(that.getDeclaration()), ";}");
            }
        } else {
            final String lhs = generateToString(new GenerateCallback() {
                @Override public void generateValue() {
                    GenerateJsVisitor.super.visit(that);
                }
            });
            out(memberAccess(that, lhs));
        }
    }

    /** SpreadOp cannot be a simple function call because we need to reference the object methods directly, so it's a function */
    private void generateSpread(QualifiedMemberOrTypeExpression that) {
        //Determine if it's a method or attribute
        boolean isMethod = that.getDeclaration() instanceof Method;
        //Define a function
        out("(function()");
        beginBlock();
        if (opts.isComment()) {
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
        out(".iterator();"); endLine();
        //Iterate
        String elem = names.createTempVariable("elem");
        out("var ", elem, ";"); endLine();
        out("while ((", elem, "=", iter, ".next())!==", clAlias, "getFinished())");
        beginBlock();
        //Add value or reference to the array
        out(tmplist, ".push(");
        if (isMethod) {
            out("{o:", elem, ", f:", memberAccess(that, elem), "}");
        } else {
            out(memberAccess(that, elem));
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
        if (that.getPrimary() instanceof Tree.BaseTypeExpression) {
            //it's a static method ref
            out("function(x){return ");
            that.getPrimary().visit(this);
            out(".$$.prototype.", (name == null) ? memberAccess(that, "") :name,".bind(x);}");
            return;
        }
        String primaryVar = createRetainedTempVar("opt");
        out("(", primaryVar, "=");
        that.getPrimary().visit(this);
        out(",", clAlias, "JsCallable(", primaryVar, ",", primaryVar, "!==null?",
                (name == null) ? memberAccess(that, primaryVar) : (primaryVar+"."+name), ":null))");
    }
    
    /**
     * Checks if the given node is a MemberOrTypeExpression or QualifiedType which
     * represents an access to a supertype member and returns the scope of that
     * member or null.
     */
    Scope getSuperMemberScope(Node node) {
        Scope scope = null;
        if (node instanceof QualifiedMemberOrTypeExpression) {
            // Check for "super.member"
            QualifiedMemberOrTypeExpression qmte = (QualifiedMemberOrTypeExpression) node;
            final Term primary = eliminateParensAndWidening(qmte.getPrimary());
            if (primary instanceof Super) {
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
    
    private String memberAccessBase(Node node, Declaration decl, boolean setter,
                String lhs) {
        final StringBuilder sb = new StringBuilder();
        
        if (lhs != null) {
            if (lhs.length() > 0) {
                sb.append(lhs).append(".");
            }
        }
        else if (node instanceof BaseMemberOrTypeExpression) {
            BaseMemberOrTypeExpression bmte = (BaseMemberOrTypeExpression) node;
            Declaration bmd = bmte.getDeclaration();
            if (bmd.isParameter() && bmd.getContainer() instanceof ClassAlias) {
                return names.name(bmd);
            }
            String path = qualifiedPath(node, bmd);
            if (path.length() > 0) {
                sb.append(path);
                sb.append(".");
            }
        }
        
        Scope scope = getSuperMemberScope(node);
        if (opts.isOptimize() && (scope != null)) {
            sb.append("getT$all()['");
            sb.append(scope.getQualifiedNameString());
            sb.append("']");
            if (defineAsProperty(decl)) {
                return clAlias + (setter ? "attrSetter(" : "attrGetter(")
                        + sb.toString() + ",'" + names.name(decl) + "')";
            }
            sb.append(".$$.prototype.");
        }
        final String member = (accessThroughGetter(decl) && !accessDirectly(decl))
                ? (setter ? names.setter(decl) : names.getter(decl)) : names.name(decl);
        sb.append(member);
        if (!opts.isOptimize() && (scope != null)) {
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
     * the given expression. If lhs==null and the expression is a BaseMemberExpression
     * then the qualified path is prepended.
     */
    private String memberAccess(StaticMemberOrTypeExpression expr, String lhs) {
        Declaration decl = expr.getDeclaration();
        String plainName = null;
        if (decl == null && dynblock > 0) {
            plainName = expr.getIdentifier().getText();
        }
        else if (isNative(decl)) {
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            return ((lhs != null) && (lhs.length() > 0))
                    ? (lhs + "." + plainName) : plainName;            
        }
        boolean protoCall = opts.isOptimize() && (getSuperMemberScope(expr) != null);
        if (accessDirectly(decl) && !(protoCall && defineAsProperty(decl))) {
            // direct access, without getter
            return memberAccessBase(expr, decl, false, lhs);
        }
        // access through getter
        return memberAccessBase(expr, decl, false, lhs)
                + (protoCall ? ".call(this)" : "()");
    }
    
    /**
     * Generates a write access to a member, as represented by the given expression.
     * The given callback is responsible for generating the assigned value.
     * If lhs==null and the expression is a BaseMemberExpression
     * then the qualified path is prepended.
     */
    private void generateMemberAccess(StaticMemberOrTypeExpression expr,
                GenerateCallback callback, String lhs) {
        Declaration decl = expr.getDeclaration();
        boolean paren = false;
        String plainName = null;
        if (decl == null && dynblock > 0) {
            plainName = expr.getIdentifier().getText();
        } else if (isNative(decl)) {
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            if ((lhs != null) && (lhs.length() > 0)) {
                out(lhs, ".");
            }
            out(plainName, "=");
        }
        else {
            boolean protoCall = opts.isOptimize() && (getSuperMemberScope(expr) != null);
            if (accessDirectly(decl) && !(protoCall && defineAsProperty(decl))) {
                // direct access, without setter
                out(memberAccessBase(expr, decl, true, lhs), "=");
            }
            else {
                // access through setter
                out(memberAccessBase(expr, decl, true, lhs),
                        protoCall ? ".call(this," : "(");
                paren = true;
            }
        }
        
        callback.generateValue();
        if (paren) { out(")"); }
    }
    private void generateMemberAccess(final StaticMemberOrTypeExpression expr,
            final String strValue, final String lhs) {
        generateMemberAccess(expr, new GenerateCallback() {
            @Override public void generateValue() { out(strValue); }
        }, lhs);
    }

    @Override
    public void visit(BaseTypeExpression that) {
        Declaration d = that.getDeclaration();
        if (d == null && isInDynamicBlock()) {
            //It's a native js type but will be wrapped in dyntype() call
            String id = that.getIdentifier().getText();
            out("(typeof ", id, "==='undefined'?");
            generateThrow("Undefined type " + id, that);
            out(":", id, ")");
        } else {
            qualify(that, d);
            out(names.name(d));
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

    public void visit(Dynamic that) {
        //this is value{xxx}
        invoker.nativeObject(that.getNamedArgumentList());
    }

    @Override
    public void visit(InvocationExpression that) {
        invoker.generateInvocation(that);
    }

    @Override
    public void visit(PositionalArgumentList that) {
        invoker.generatePositionalArguments(null, that, that.getPositionalArguments(), false, false);
    }

    /** Box a term, visit it, unbox it. */
    private void box(Term term) {
        final int t = boxStart(term);
        term.visit(this);
        if (t == 4) out("/*TODO: callable targs 4*/");
        boxUnboxEnd(t);
    }

    // Make sure fromTerm is compatible with toTerm by boxing it when necessary
    private int boxStart(Term fromTerm) {
        return boxUnboxStart(fromTerm, false);
    }

    // Make sure fromTerm is compatible with toTerm by boxing or unboxing it when necessary
    int boxUnboxStart(Term fromTerm, Term toTerm) {
        return boxUnboxStart(fromTerm, isNative(toTerm));
    }

    // Make sure fromTerm is compatible with toDecl by boxing or unboxing it when necessary
    int boxUnboxStart(Term fromTerm, com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration toDecl) {
        return boxUnboxStart(fromTerm, isNative(toDecl));
    }

    int boxUnboxStart(Term fromTerm, boolean toNative) {
        // Box the value
        final boolean fromNative = isNative(fromTerm);
        final ProducedType fromType = fromTerm.getTypeModel();
        final String fromTypeName = TypeUtils.isUnknown(fromType) ? "UNKNOWN" : fromType.getProducedTypeQualifiedName();
        if (fromNative != toNative || fromTypeName.startsWith("ceylon.language::Callable<")) {
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
                } else if (fromTypeName.startsWith("ceylon.language::Callable<")) {
                    out(clAlias, "$JsCallable(");
                    return 4;
                } else {
                    return 0;
                }
                return 1;
            } else if ("ceylon.language::String".equals(fromTypeName)
                        || "ceylon.language::Float".equals(fromTypeName)) {
                // conversion from Ceylon String or Float to native value
                return 2;
            } else if (fromTypeName.startsWith("ceylon.language::Callable<")) {
                out(clAlias, "$JsCallable(");
                return 4;
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
        case 4: out(")"); break;
        default: //nothing
        }
    }

    @Override
    public void visit(final ObjectArgument that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
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
        
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!opts.isOptimize()) {
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
        //Add reference to metamodel
        out(names.name(c), ".$$metamodel$$=");
        TypeUtils.encodeForRuntime(c, null, this);
        endLine(true);

        typeInitialization(xt, sts, c, new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                addToPrototype(that, c, body.getStatements());
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
            out(clAlias, "getEmpty()");
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
                if (dynblock > 0 && expr instanceof ListedArgument && TypeUtils.isUnknown(expr.getTypeModel())) {
                    TypeUtils.generateDynamicCheck(((ListedArgument)expr).getExpression(), types.anything.getType(), this);
                } else {
                    expr.visit(this);
                }
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
        // A lazy specifier expression in a class/interface should go into the
        // prototype in prototype style, so don't generate them here.
        if (!(opts.isOptimize() && (that.getSpecifierExpression() instanceof LazySpecifierExpression)
                && (that.getScope().getContainer() instanceof TypeDeclaration))) {
            specifierStatement(null, that);
        }
    }
    
    private void specifierStatement(final TypeDeclaration outer,
            final SpecifierStatement specStmt) {
        if (dynblock > 0 && TypeUtils.isUnknown(specStmt.getBaseMemberExpression().getTypeModel())) {
            specStmt.getBaseMemberExpression().visit(this);
            out("=");
            int box = boxUnboxStart(specStmt.getSpecifierExpression().getExpression(), specStmt.getBaseMemberExpression());
            specStmt.getSpecifierExpression().getExpression().visit(this);
            if (box == 4) out("/*TODO: callable targs 6*/");
            boxUnboxEnd(box);
            out(";");
            return;
        }
        if (specStmt.getBaseMemberExpression() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) specStmt.getBaseMemberExpression();
            Declaration bmeDecl = bme.getDeclaration();
            if (specStmt.getSpecifierExpression() instanceof LazySpecifierExpression) {
                // attr => expr;
                final boolean property = defineAsProperty(bmeDecl);
                if (property) {
                    out(clAlias, "defineAttr(", qualifiedPath(specStmt, bmeDecl), ",'",
                            names.name(bmeDecl), "',function()");
                } else  {
                    if (bmeDecl.isMember()) {
                        qualify(specStmt, bmeDecl);
                    } else {
                        out ("var ");
                    }
                    out(names.getter(bmeDecl), "=function()");
                }
                beginBlock();
                if (outer != null) { initSelf(specStmt.getScope(), true); }
                out ("return ");
                specStmt.getSpecifierExpression().visit(this);
                out(";");
                endBlock();
                if (property) {
                    out(",undefined,");
                    TypeUtils.encodeForRuntime(specStmt, bmeDecl, this);
                    out(")");
                }
                endLine(true);
                directAccess.remove(bmeDecl);
            }
            else if (outer != null) {
                // "attr = expr;" in a prototype definition
                if (bmeDecl.isMember() && (bmeDecl instanceof Value) && bmeDecl.isActual()) {
                    out("delete ", names.self(outer), ".", names.name(bmeDecl));
                    endLine(true);
                }                
            }
            else if (bmeDecl instanceof MethodOrValue) {
                // "attr = expr;" in an initializer or method
                final MethodOrValue moval = (MethodOrValue)bmeDecl;
                if (moval.isVariable()) {
                    // simple assignment to a variable attribute
                    generateMemberAccess(bme, new GenerateCallback() {
                        @Override public void generateValue() {
                            int boxType = boxUnboxStart(specStmt.getSpecifierExpression().getExpression().getTerm(),
                                    moval);
                            if (dynblock > 0 && !TypeUtils.isUnknown(moval.getType())
                                    && TypeUtils.isUnknown(specStmt.getSpecifierExpression().getExpression().getTypeModel())) {
                                TypeUtils.generateDynamicCheck(specStmt.getSpecifierExpression().getExpression(),
                                        moval.getType(), GenerateJsVisitor.this);
                            } else {
                                specStmt.getSpecifierExpression().getExpression().visit(GenerateJsVisitor.this);
                            }
                            if (boxType == 4) {
                                out(",");
                                if (moval instanceof Method) {
                                    //Add parameters
                                    TypeUtils.encodeParameterListForRuntime(specStmt,
                                            ((Method)moval).getParameterLists().get(0), GenerateJsVisitor.this);
                                    out(",");
                                } else {
                                    //TODO extract parameters from Value
                                    out("[/*VALUE Callable params", moval.getClass().getName(), "*/],");
                                }
                                TypeUtils.printTypeArguments(specStmt.getSpecifierExpression().getExpression(),
                                        specStmt.getSpecifierExpression().getExpression().getTypeModel().getTypeArguments(),
                                        GenerateJsVisitor.this);
                            }
                            boxUnboxEnd(boxType);
                        }
                    }, null);
                    out(";");
                } else if (moval.isMember()) {
                    if (moval instanceof Method) {
                        //same as fat arrow
                        qualify(specStmt, bmeDecl);
                        out(names.name(moval), "=function ", names.name(moval), "(");
                        //Build the parameter list, we'll use it several times
                        final StringBuilder paramNames = new StringBuilder();
                        for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : ((Method) moval).getParameterLists().get(0).getParameters()) {
                            if (paramNames.length() > 0) paramNames.append(",");
                            paramNames.append(names.name(p));
                        }
                        out(paramNames.toString());
                        out("){");
                        initSelf(moval.getContainer(), false);
                        for (com.redhat.ceylon.compiler.typechecker.model.Parameter p : ((Method) moval).getParameterLists().get(0).getParameters()) {
                            if (p.isDefaulted()) {
                                out("if (", names.name(p), "===undefined)", names.name(p),"=");
                                qualify(specStmt, moval);
                                out(names.name(moval), "$defs$", p.getName(), "(", paramNames.toString(), ")");
                                endLine(true);
                            }
                        }
                        out("return ");
                        specStmt.getSpecifierExpression().visit(this);
                        out("(", paramNames.toString(), ");}");
                        endLine(true);
                    } else {
                        // Specifier for a member attribute. This actually defines the
                        // member (e.g. in shortcut refinement syntax the attribute
                        // declaration itself can be omitted), so generate the attribute.
                        generateAttributeGetter(null, moval,
                                specStmt.getSpecifierExpression(), null);
                    }
                } else {
                    // Specifier for some other attribute, or for a method.
                    if (opts.isOptimize() 
                            || (bmeDecl.isMember() && (bmeDecl instanceof Method))) {
                        qualify(specStmt, bmeDecl);
                    }
                    out(names.name(bmeDecl), "=");
                    if (dynblock > 0 && TypeUtils.isUnknown(specStmt.getSpecifierExpression().getExpression().getTypeModel())) {
                        TypeUtils.generateDynamicCheck(specStmt.getSpecifierExpression().getExpression(),
                                bme.getTypeModel(), this);
                    } else {
                        specStmt.getSpecifierExpression().visit(this);
                    }
                    out(";");
                }
            }
        }
        else if ((specStmt.getBaseMemberExpression() instanceof ParameterizedExpression)
                && (specStmt.getSpecifierExpression() != null)) {
            final ParameterizedExpression paramExpr =
                    (ParameterizedExpression) specStmt.getBaseMemberExpression();
            if (paramExpr.getPrimary() instanceof BaseMemberExpression) {
                // func(params) => expr;
                BaseMemberExpression bme = (BaseMemberExpression) paramExpr.getPrimary();
                Declaration bmeDecl = bme.getDeclaration();
                if (bmeDecl.isMember()) {
                    qualify(specStmt, bmeDecl);
                } else {
                    out("var ");
                }
                out(names.name(bmeDecl), "=");
                singleExprFunction(paramExpr.getParameterLists(),
                        specStmt.getSpecifierExpression().getExpression(),
                        bmeDecl instanceof Scope ? (Scope)bmeDecl : null);
                out(";");
            }
        }
    }
    
    private void addSpecifierToPrototype(final TypeDeclaration outer,
                final SpecifierStatement specStmt) {
        specifierStatement(outer, specStmt);
    }

    @Override
    public void visit(final AssignOp that) {
        String returnValue = null;
        StaticMemberOrTypeExpression lhsExpr = null;
        
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            that.getLeftTerm().visit(this);
            out("=");
            int box = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
            that.getRightTerm().visit(this);
            if (box == 4) out("/*TODO: callable targs 6*/");
            boxUnboxEnd(box);
            return;
        }
        out("(");
        if (that.getLeftTerm() instanceof BaseMemberExpression) {
            BaseMemberExpression bme = (BaseMemberExpression) that.getLeftTerm();
            lhsExpr = bme;
            Declaration bmeDecl = bme.getDeclaration();
            boolean simpleSetter = hasSimpleGetterSetter(bmeDecl);
            if (!simpleSetter) {
                returnValue = memberAccess(bme, null);
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
                returnValue = memberAccess(qme, lhsVar);
            } else {
                super.visit(qme);
                out(".");
            }
        }
        
        generateMemberAccess(lhsExpr, new GenerateCallback() {
            @Override public void generateValue() {
                int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
                that.getRightTerm().visit(GenerateJsVisitor.this);
                if (boxType == 4) out("/*TODO: callable targs 7*/");
                boxUnboxEnd(boxType);
            }
        }, null);
        
        if (returnValue != null) { out(",", returnValue); }
        out(")");
    }

    /** Outputs the module name for the specified declaration. Returns true if something was output. */
    boolean qualify(Node that, Declaration d) {
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
        if (!isMember && isImported(that == null ? null : that.getUnit().getPackage(), d)) {
            return names.moduleAlias(d.getUnit().getPackage().getModule());
        }
        else if (opts.isOptimize() && !inProto) {
            if (isMember && !(d.isParameter() && !d.isCaptured())) {
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
                            path += ".$$outer";
                        } else {
                            path += names.self((TypeDeclaration) scope);
                        }
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
            TypeDeclaration id = d instanceof TypeAlias ? (TypeDeclaration)d : that.getScope().getInheritingDeclaration(d);
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

    /** Tells whether a declaration is in the specified package. */
    boolean isImported(final Package p2, final Declaration d) {
        if (d == null) {
            return false;
        }
        Package p1 = d.getUnit().getPackage();
        if (p2 == null)return p1 != null;
        if (p1.getModule()== null)return p2.getModule()!=null;
        return !p1.getModule().equals(p2.getModule());
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
//            // special treatment, even if opts.isOptimize()==false, because they
//            // may have been generated in prototype style. In particular,
//            // ceylon.language is always in prototype style.
//            if ((term.getDeclaration() instanceof Functional)
//                    && (opts.isOptimize() || !declaredInThisPackage(term.getDeclaration()))) {
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
        out("return");
        if (that.getExpression() == null) {
            endLine(true);
            return;
        }
        out(" ");
        if (dynblock > 0 && TypeUtils.isUnknown(that.getExpression().getTypeModel())) {
            TypeUtils.generateDynamicCheck(that.getExpression(), that.getExpression().getTypeModel(), this);
            endLine(true);
            return;
        }
        super.visit(that);
    }

    @Override
    public void visit(AnnotationList that) {}

    void self(TypeDeclaration d) {
        out(names.self(d));
    }

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
    public void visit(ScaleOp that) {
        binaryOp(that, new BinaryOpGenerator() {
            @Override
            public void generate(BinaryOpTermGenerator termgen) {
                final String lhs = names.createTempVariable();
                out("function(){var ", lhs, "=");
                termgen.left();
                out(";return ");
                termgen.right();
                out(".scale(", lhs, ");}()");
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

            final String getLHS = memberAccess(lhsBME, null);
            out("(");
            generateMemberAccess(lhsBME, new GenerateCallback() {
                @Override public void generateValue() {
                    out(getLHS, ".", functionName, "(");
                    that.getRightTerm().visit(GenerateJsVisitor.this);
                    out(")");
                }
            }, null);
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
                if (boxType == 4) out("/*TODO: callable targs 8*/");
                boxUnboxEnd(boxType);
                out(".", functionName, "(");
                that.getRightTerm().visit(this);
                out("))");
                
            } else {
                final String lhsPrimaryVar = createRetainedTempVar();
                final String getLHS = memberAccess(lhsQME, lhsPrimaryVar);
                out("(", lhsPrimaryVar, "=");
                lhsQME.getPrimary().visit(this);
                out(",");
                generateMemberAccess(lhsQME, new GenerateCallback() {
                    @Override public void generateValue() {
                        out(getLHS, ".", functionName, "(");
                        that.getRightTerm().visit(GenerateJsVisitor.this);
                        out(")");
                    }
                }, lhsPrimaryVar);
                
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
                    out(".negativeValue");
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
                    out(".positiveValue");
                }
            }
        });
    }

    @Override public void visit(EqualOp that) {
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use equals() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".equals&&", ltmp, ".equals(", rtmp, "))||", ltmp, "===", rtmp, ")");
        } else {
            leftEqualsRight(that);
        }
    }

    @Override public void visit(NotEqualOp that) {
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use equals() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".equals&&!", ltmp, ".equals(", rtmp, "))||", ltmp, "!==", rtmp, ")");
        } else {
            out("(!");
            leftEqualsRight(that);
            out(")");
        }
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
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ").equals(",
                    clAlias, "getSmaller()))||", ltmp, "<", rtmp, ")");
        } else {
            leftCompareRight(that);
            out(".equals(", clAlias, "getSmaller())");
        }
    }

    @Override public void visit(LargerOp that) {
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ").equals(",
                    clAlias, "getLarger()))||", ltmp, ">", rtmp, ")");
        } else {
            leftCompareRight(that);
            out(".equals(", clAlias, "getLarger())");
        }
    }

    @Override public void visit(SmallAsOp that) {
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ")!==",
                    clAlias, "getLarger())||", ltmp, "<=", rtmp, ")");
        } else {
            out("(");
            leftCompareRight(that);
            out("!==", clAlias, "getLarger()");
            out(")");
        }
    }

    @Override public void visit(LargeAsOp that) {
        if (dynblock > 0 && TypeUtils.isUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ")!==",
                    clAlias, "getSmaller())||", ltmp, ">=", rtmp, ")");
        } else {
            out("(");
            leftCompareRight(that);
            out("!==", clAlias, "getSmaller()");
            out(")");
        }
    }
    public void visit(final Tree.WithinOp that) {
        final String ttmp = names.createTempVariable();
        out("(", ttmp, "=");
        box(that.getTerm());
        out(",");
        if (dynblock > 0 && TypeUtils.isUnknown(that.getTerm().getTypeModel())) {
            final String tmpl = names.createTempVariable();
            final String tmpu = names.createTempVariable();
            out(tmpl, "=");
            box(that.getLowerBound().getTerm());
            out(",", tmpu, "=");
            box(that.getUpperBound().getTerm());
            out(",((", ttmp, ".compare&&",ttmp,".compare(", tmpl);
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                out(")===", clAlias, "getLarger())||", ttmp, ">", tmpl, ")");
            } else {
                out(")!==", clAlias, "getSmaller())||", ttmp, ">=", tmpl, ")");
            }
            out("&&((", ttmp, ".compare&&",ttmp,".compare(", tmpu);
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                out(")===", clAlias, "getSmaller())||", ttmp, "<", tmpu, ")");
            } else {
                out(")!==", clAlias, "getLarger())||", ttmp, "<=", tmpu, ")");
            }
        } else {
            out(ttmp, ".compare(");
            box(that.getLowerBound().getTerm());
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                out(")===", clAlias, "getLarger()");
            } else {
                out(")!==", clAlias, "getSmaller()");
            }
            out("&&");
            out(ttmp, ".compare(");
            box(that.getUpperBound().getTerm());
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                out(")===", clAlias, "getSmaller()");
            } else {
                out(")!==", clAlias, "getLarger()");
            }
        }
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
                if (boxTypeLeft == 4) out("/*TODO: callable targs 9*/");
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
        gen.generate(new BinaryOpTermGenerator() {
            @Override
            public void left() {
                box(that.getLeftTerm());
            }
            @Override
            public void right() {
                box(that.getRightTerm());
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
       out(".$get(");
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
       prefixIncrementOrDecrement(that.getTerm(), "successor");
   }

   @Override public void visit(DecrementOp that) {
       prefixIncrementOrDecrement(that.getTerm(), "predecessor");
   }

   private boolean hasSimpleGetterSetter(Declaration decl) {
       return (dynblock > 0 && TypeUtils.isUnknown(decl)) ||
               !((decl instanceof Value && ((Value)decl).isTransient()) || (decl instanceof Setter) || decl.isFormal());
   }

   private void prefixIncrementOrDecrement(Term term, String functionName) {
       if (term instanceof BaseMemberExpression) {
           BaseMemberExpression bme = (BaseMemberExpression) term;
           boolean simpleSetter = hasSimpleGetterSetter(bme.getDeclaration());
           String getMember = memberAccess(bme, null);
           String applyFunc = String.format("%s.%s", getMember, functionName);
           out("(");
           generateMemberAccess(bme, applyFunc, null);
           if (!simpleSetter) { out(",", getMember); }
           out(")");
           
       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           String primaryVar = createRetainedTempVar();
           String getMember = memberAccess(qme, primaryVar);
           String applyFunc = String.format("%s.%s", getMember, functionName);
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",");
           generateMemberAccess(qme, applyFunc, primaryVar);
           if (!hasSimpleGetterSetter(qme.getDeclaration())) {
               out(",", getMember);
           }
           out(")");
       }
   }

   @Override public void visit(PostfixIncrementOp that) {
       postfixIncrementOrDecrement(that.getTerm(), "successor");
   }

   @Override public void visit(PostfixDecrementOp that) {
       postfixIncrementOrDecrement(that.getTerm(), "predecessor");
   }

   private void postfixIncrementOrDecrement(Term term, String functionName) {
       if (term instanceof BaseMemberExpression) {
           BaseMemberExpression bme = (BaseMemberExpression) term;
           if (bme.getDeclaration() == null && dynblock > 0) {
               out(bme.getIdentifier().getText(), "successor".equals(functionName) ? "++" : "--");
               return;
           }
           String oldValueVar = createRetainedTempVar("old" + bme.getDeclaration().getName());
           String applyFunc = String.format("%s.%s", oldValueVar, functionName);
           out("(", oldValueVar, "=", memberAccess(bme, null), ",");
           generateMemberAccess(bme, applyFunc, null);
           out(",", oldValueVar, ")");

       } else if (term instanceof QualifiedMemberExpression) {
           QualifiedMemberExpression qme = (QualifiedMemberExpression) term;
           if (qme.getDeclaration() == null && dynblock > 0) {
               out(qme.getIdentifier().getText(), "successor".equals(functionName) ? "++" : "--");
               return;
           }
           String primaryVar = createRetainedTempVar();
           String oldValueVar = createRetainedTempVar("old" + qme.getDeclaration().getName());
           String applyFunc = String.format("%s.%s", oldValueVar, functionName);
           out("(", primaryVar, "=");
           qme.getPrimary().visit(this);
           out(",", oldValueVar, "=", memberAccess(qme, primaryVar), ",");
           generateMemberAccess(qme, applyFunc, primaryVar);
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
       if (boxType == 4) out("/*TODO: callable targs 10*/");
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
    void generateIsOfType(Node term, String termString, Type type, String tmpvar, final boolean negate) {
        if (negate) {
            out("!");
        }
        out(clAlias, "isOfType(");
        if (term instanceof Term) {
            conds.specialConditionRHS((Term)term, tmpvar);
        } else {
            conds.specialConditionRHS(termString, tmpvar);
        }
        out(",");
        if (type!=null) {
            TypeUtils.typeNameOrList(term, type.getTypeModel(), this);
        }
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
        if (opts.isComment()) {
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
        out(".iterator();");
        endLine();
        out("var ", itemVar, ";while ((", itemVar, "=", iterVar, ".next())!==", clAlias, "getFinished())");
        beginBlock();
        if (that instanceof ValueIterator) {
            directAccess.add(((ValueIterator)that).getVariable().getDeclarationModel());
        } else if (that instanceof KeyValueIterator) {
            String keyvar = names.name(((KeyValueIterator)that).getKeyVariable().getDeclarationModel());
            String valvar = names.name(((KeyValueIterator)that).getValueVariable().getDeclarationModel());
            out("var ", keyvar, "=", itemVar, ".key;");
            endLine();
            out("var ", valvar, "=", itemVar, ".item;");
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
        if (errVisitor.hasErrors(that))return;
        List<Resource> resources = that.getTryClause().getResourceList() == null ? null :
            that.getTryClause().getResourceList().getResources();
        if (resources != null && resources.isEmpty()) {
            resources = null;
        }
        List<String> resourceVars = null;
        String excVar = null;
        if (resources != null) {
            resourceVars = new ArrayList<>(resources.size());
            for (Resource res : resources) {
                final String resourceVar = names.createTempVariable(res.getVariable().getIdentifier().getText());
                out("var ", resourceVar, "=null");
                endLine(true);
                out("var ", resourceVar, "$cls=false");
                endLine(true);
                resourceVars.add(resourceVar);
            }
            excVar = names.createTempVariable();
            out("var ", excVar, "$exc=null");
            endLine(true);
        }
        out("try");
        if (resources != null) {
            int pos = 0;
            out("{");
            for (String resourceVar : resourceVars) {
                out(resourceVar, "=");
                resources.get(pos++).visit(this);
                endLine(true);
                out(resourceVar, ".open()");
                endLine(true);
                out(resourceVar, "$cls=true");
                endLine(true);
            }
        }
        encloseBlockInFunction(that.getTryClause().getBlock());
        if (resources != null) {
            for (String resourceVar : resourceVars) {
                out(resourceVar, "$cls=false");
                endLine(true);
                out(resourceVar, ".close()");
                endLine(true);
            }
            out("}");
        }

        if (!that.getCatchClauses().isEmpty() || resources != null) {
            String catchVarName = names.createTempVariable("ex");
            out("catch(", catchVarName, ")");
            beginBlock();
            //Check if it's native and if so, wrap it
            out("if (", catchVarName, ".getT$name === undefined) ", catchVarName, "=",
                    clAlias, "NativeException(", catchVarName, ")");
            endLine(true);
            if (excVar != null) {
                out(excVar, "$exc=", catchVarName);
                endLine(true);
            }
            if (resources != null) {
                for (String resourceVar : resourceVars) {
                    out("try{if(",resourceVar, "$cls)", resourceVar, ".close(",
                            excVar, "$exc);}catch(",resourceVar,"$swallow){",
                            clAlias, "addSuppressedException(", resourceVar, "$swallow,", catchVarName, ");}");
                }
            }
            boolean firstCatch = true;
            for (CatchClause catchClause : that.getCatchClauses()) {
                Variable variable = catchClause.getCatchVariable().getVariable();
                if (!firstCatch) {
                    out("else ");
                }
                firstCatch = false;
                out("if(");
                generateIsOfType(variable, catchVarName, variable.getType(), null, false);
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
            if (!that.getCatchClauses().isEmpty()) {
                out("else{throw ", catchVarName, "}");
            }
            endBlockNewLine();
        }

        if (that.getFinallyClause() != null) {
            out("finally");
            encloseBlockInFunction(that.getFinallyClause().getBlock());
        }
    }

    @Override public void visit(Throw that) {
        out("throw ", clAlias, "wrapexc(");
        if (that.getExpression() == null) {
            out(clAlias, "Exception()");
        } else {
            that.getExpression().visit(this);
        }
        that.getUnit().getFullPath();
        out(",'", that.getLocation(), "','", that.getUnit().getRelativePath(), "');");
    }

    private void visitIndex(IndexExpression that) {
        that.getPrimary().visit(this);
        ElementOrRange eor = that.getElementOrRange();
        if (eor instanceof Element) {
            if (TypeUtils.isUnknown(that.getPrimary().getTypeModel()) && dynblock > 0) {
                out("[");
                ((Element)eor).getExpression().visit(this);
                out("]");
            } else {
                out(".$get(");
                ((Element)eor).getExpression().visit(this);
                out(")");
            }
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
            generateIsOfType(switchTerm, expvar, isCaseItem.getType(), null, false);
            Variable caseVar = isCaseItem.getVariable();
            if (caseVar != null) {
                directAccess.add(caseVar.getDeclarationModel());
                names.forceName(caseVar.getDeclarationModel(), expvar);
            }
        } else if (item instanceof SatisfiesCase) {
            item.addError("case(satisfies) not yet supported");
            out("true");
        } else if (item instanceof MatchCase) {
            boolean first = true;
            for (Expression exp : ((MatchCase)item).getExpressionList().getExpressions()) {
                if (!first) out(" || ");
                if (exp.getTerm() instanceof Tree.Literal) {
                    if (switchTerm.getTypeModel().isUnknown()) {
                        out(expvar, "==");
                        exp.visit(this);
                    } else {
                        if (switchTerm.getUnit().isOptionalType(switchTerm.getTypeModel())) {
                            out(expvar,"!==null&&");
                        }
                        out(expvar, ".equals(");
                        exp.visit(this);
                        out(")");
                    }
                } else {
                    out(expvar, "===");
                    exp.visit(this);
                }
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
        if (opts.isComment()) out("//Switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
        endLine();
        //Put the expression in a tmp var
        final String expvar = names.createTempVariable("case");
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
        if (that.getSwitchCaseList().getElseClause() == null) {
            if (dynblock > 0 && expr.getTypeModel().getDeclaration() instanceof UnknownType) {
                out("else throw ", clAlias, "Exception('Ceylon switch over unknown type does not cover all cases')");
            }
        } else {
            out("else ");
            that.getSwitchCaseList().getElseClause().visit(this);
        }
        if (opts.isComment()) {
            out("//End switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            endLine();
        }
    }

    /** Generates the code for an anonymous function defined inside an argument list. */
    @Override
    public void visit(final Tree.FunctionArgument that) {
        out("(");
        if (that.getBlock() == null) {
            singleExprFunction(that.getParameterLists(), that.getExpression(), that.getScope());
        } else {
            multiStmtFunction(that.getParameterLists(), that.getBlock(), that.getScope());
        }
        out(")");
    }

    private void multiStmtFunction(final List<ParameterList> paramLists,
            final Block block, final Scope scope) {
        generateParameterLists(paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                beginBlock();
                if (paramLists.size() == 1) { initSelf(scope, false); }
                initParameters(paramLists.get(paramLists.size()-1),
                        scope instanceof TypeDeclaration ? (TypeDeclaration)scope : null, null);
                visitStatements(block.getStatements());
                endBlock();
            }
        });
    }
    private void singleExprFunction(final List<ParameterList> paramLists,
                                    final Expression expr, final Scope scope) {
        generateParameterLists(paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                beginBlock();
                if (paramLists.size() == 1) { initSelf(scope, false); }
                initParameters(paramLists.get(paramLists.size()-1),
                        null, scope instanceof Method ? (Method)scope : null);
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
        out("for (var i=1; i<", rhs, "; i++){", end, "=", end, ".successor;}");
        endLine();
        out("return ", clAlias, "Range(");
        out(lhs, ",", end, ",");
        TypeUtils.printTypeArguments(that,
                that.getTypeModel().getTypeArguments(),
                GenerateJsVisitor.this);
        out(")");
        endLine();
        out("}else return ", clAlias, "getEmpty();}())");
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
                    //TODO add metamodel
                    out("return function");
                }
                paramList.visit(this);
                if (count == 0) {
                    beginBlock();
                    initSelf(scope, false);
                    Scope parent = scope == null ? null : scope.getContainer();
                    initParameters(paramList, parent instanceof TypeDeclaration ? (TypeDeclaration)parent : null,
                            scope instanceof Method ? (Method)scope:null);
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
        	out(clAlias, "getEmpty()");
        } else {
        	List<Map<TypeParameter,ProducedType>> targs = new ArrayList<Map<TypeParameter,ProducedType>>(3);
        	List<PositionalArgument> positionalArguments = sarg.getPositionalArguments();
        	boolean spread = !positionalArguments.isEmpty() 
        			&& positionalArguments.get(positionalArguments.size()-1) instanceof Tree.ListedArgument == false;
        	int lim = positionalArguments.size()-1;
        	for (PositionalArgument expr : positionalArguments) {
        		if (count > 0) {
        			out(",");
        		}
        		ProducedType exprType = expr.getTypeModel();
        		if (count==lim && spread) {
        			if (exprType.getDeclaration().inherits(types.tuple)) {
        				expr.visit(this);
        			} else {
        				expr.visit(this);
        				out(".sequence");
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
        			if (dynblock > 0 && TypeUtils.isUnknown(exprType) && expr instanceof ListedArgument) {
                        exprType = types.anything.getType();
        			    TypeUtils.generateDynamicCheck(((ListedArgument)expr).getExpression(), exprType, this);
        			} else {
                        expr.visit(this);
        			}
        		}
        		count++;
        	}
        	if (!spread) {
        		if (count > 0) {
        			out(",");
        		}
        		out(clAlias, "getEmpty()");
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
        if (that.getAnnotationList() != null && that.getAnnotationList().getAnonymousAnnotation() != null) {
            custom = that.getAnnotationList().getAnonymousAnnotation().getStringLiteral().getText();
        } else {
            for (Annotation ann : that.getAnnotationList().getAnnotations()) {
                BaseMemberExpression bme = (BaseMemberExpression)ann.getPrimary();
                if ("doc".equals(bme.getDeclaration().getName())) {
                    custom = ((Tree.ListedArgument)ann.getPositionalArgumentList().getPositionalArguments().get(0)).getExpression().getTerm().getText();
                }
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
        out(") {throw ", clAlias, "wrapexc(", clAlias, "AssertionException(\"",
                escapeStringLiteral(sb.toString()), "\"),'",that.getLocation(), "','",
                that.getUnit().getFilename(), "'); }");
        endLine();
    }

    @Override
    public void visit(Tree.DynamicStatement that) {
        dynblock++;
        if (dynblock == 1) {
            out("/*Begin dynamic block*/");
            endLine();
        }
        for (Tree.Statement stmt : that.getDynamicClause().getBlock().getStatements()) {
            stmt.visit(this);
        }
        if (dynblock == 1) {
            out("/*End dynamic block*/");
            endLine();
        }
        dynblock--;
    }

    /** Closes a native array and invokes reifyCeylonType with the specified type parameters. */
    void closeSequenceWithReifiedType(Node that, Map<TypeParameter,ProducedType> types) {
        out("].reifyCeylonType(");
        TypeUtils.printTypeArguments(that, types, this);
        out(")");
    }

    boolean isInDynamicBlock() {
        return dynblock > 0;
    }

    /** Generate the call to the corresponding open type for the specified literal. */
    private void generateOpenType(Tree.MetaLiteral that) {
        final Declaration d = that.getDeclaration();
        final Module m = d.getUnit().getPackage().getModule();
        out(clAlias, "$init$Open");
        if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
            out("Interface");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            out("Class");
        } else if (d instanceof Method) {
            out("Function");
        } else if (d instanceof Value) {
            out("Value");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType) {
            out("Intersection");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType) {
            out("Union");
        } else if (d instanceof TypeParameter) {
            out("TypeParam");
        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType) {
            out("NothingType");
        } else if (d instanceof TypeAlias) {
            out("Alias()(");
            if (d.isMember()) {
                //Make the chain to the top-level container
                ArrayList<Declaration> parents = new ArrayList<Declaration>(2);
                Declaration pd = (Declaration)d.getContainer();
                while (pd!=null) {
                    parents.add(0,pd);
                    pd = pd.isMember()?(Declaration)pd.getContainer():null;
                }
                for (Declaration _d : parents) {
                    out(names.name(_d), ".$$.prototype.");
                }
            }
            out(names.name(d), ")");
            return;
        }
        out("()(", clAlias, "getModules$meta().find('", m.getNameAsString(),
                "','", m.getVersion(), "').findPackage('", d.getUnit().getPackage().getNameAsString(),
                "'),");
        if (d.isMember()) {
            Declaration _md = d;
            ArrayList<Declaration> parents = new ArrayList<>(3);
            while (_md.isMember()) {
                _md=(Declaration)_md.getContainer();
                parents.add(0, _md);
            }
            boolean first=true;
            boolean imported=false;
            for (Declaration _d : parents) {
                if (first){
                    imported = qualify(that, _d);
                    first=false;
                }
                if (!imported)out("$init$");
                out(names.name(_d), imported?".$$.prototype.":"().$$.prototype.");
                imported=true;
            }
        } else {
            qualify(that, d);
        }
        if (d instanceof Value) {
            out("$prop$", names.getter(d), ")");
        } else {
            out(names.name(d), ")");
        }
    }

    @Override
    public void visit(TypeLiteral that) {
        //Can be an alias, class, interface or type parameter
        if (that.getWantsDeclaration()) {
            generateOpenType(that);
        } else {
            final ProducedType ltype = that.getType().getTypeModel();
            final TypeDeclaration td = ltype.getDeclaration();
            if (td instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
                if (td.isToplevel()) {
                    out(clAlias, "$init$AppliedClass$meta$model()(");
                } else {
                    out(clAlias, "$init$AppliedMemberClass$meta$model()(");
                }
                TypeUtils.outputQualifiedTypename(isImported(getCurrentPackage(), td), ltype, this);
                out(",");
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), this);
                out(")");
            } else if (td instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
                if (td.isToplevel()) {
                    out(clAlias, "$init$AppliedInterface$meta$model()(");
                } else {
                    out(clAlias, "$init$AppliedMemberInterface$meta$model()(");
                }
                TypeUtils.outputQualifiedTypename(isImported(getCurrentPackage(), td), ltype, this);
                out(",");
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), this);
                out(")");
            } else if (td instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType) {
                out(clAlias,"getNothingType$meta$model()");
            } else if (that instanceof Tree.AliasLiteral) {
                out("/*TODO: applied alias*/");
            } else if (that instanceof Tree.TypeParameterLiteral) {
                out("/*TODO: applied type parameter*/");
            } else {
                out(clAlias, "/*TODO: closed type literal", that.getClass().getName(),"*/typeLiteral$meta({Type:");
                TypeUtils.typeNameOrList(that, ltype, this);
                out("})");
            }
        }
    }

    @Override
    public void visit(MemberLiteral that) {
        if (that.getWantsDeclaration()) {
            generateOpenType(that);
        } else {
            final com.redhat.ceylon.compiler.typechecker.model.ProducedReference ref = that.getTarget();
            final ProducedType ltype = that.getType() == null ? null : that.getType().getTypeModel();
            final Declaration d = ref.getDeclaration();
            if (that instanceof FunctionLiteral || d instanceof Method) {
                out(clAlias, d.isMember()?"AppliedMethod$meta$model(":"AppliedFunction$meta$model(");
                if (ltype == null) {
                    qualify(that, d);
                } else {
                    qualify(that, ltype.getDeclaration());
                    out(names.name(ltype.getDeclaration()));
                    out(".$$.prototype.");
                }
                if (d instanceof Value) {
                    out("$prop$", names.getter(d), ",");
                } else {
                    out(names.name(d),",");
                }
                if (d.isMember()) {
                    if (that.getTypeArgumentList()!=null) {
                        out("[");
                        boolean first=true;
                        for (ProducedType targ : that.getTypeArgumentList().getTypeModels()) {
                            if (first)first=false;else out(",");
                            out(clAlias,"typeLiteral$meta({Type:");
                            TypeUtils.typeNameOrList(that, targ, this);
                            out("})");
                        }
                        out("]");
                        out(",");
                    } else {
                        out("undefined,");
                    }
                    TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), this);
                } else {
                    TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), this);
                    if (ref.getTypeArguments() != null && !ref.getTypeArguments().isEmpty()) {
                        out(",undefined,");//TODO pass the container
                        TypeUtils.printTypeArguments(that, ref.getTypeArguments(), this);
                    }
                }
                out(")");
            } else if (that instanceof ValueLiteral || d instanceof Value) {
                Value vd = (Value)d;
                if (vd.isMember()) {
                    out(clAlias, "$init$AppliedAttribute$meta$model()('");
                    out(d.getName(), "',");
                } else {
                    out(clAlias, "$init$AppliedValue$meta$model()(undefined,");
                }
                if (ltype == null) {
                    qualify(that, d);
                } else {
                    qualify(that, ltype.getDeclaration());
                    out(names.name(ltype.getDeclaration()));
                    out(".$$.prototype.");
                }
                if (d instanceof Value) {
                    out("$prop$", names.getter(d),",");
                } else {
                    out(names.name(d),",");
                }
                TypeUtils.printTypeArguments(that, that.getTypeModel().getTypeArguments(), this);
                out(")");
            } else {
                out(clAlias, "/*TODO:closed member literal*/typeLiteral$meta({Type:");
                out("{t:");
                if (ltype == null) {
                    qualify(that, d);
                } else {
                    qualify(that, ltype.getDeclaration());
                    out(names.name(ltype.getDeclaration()));
                    out(".$$.prototype.");
                }
                if (d instanceof Value) {
                    out("$prop$", names.getter(d));
                } else {
                    out(names.name(d));
                }
                if (ltype != null && ltype.getTypeArguments() != null && !ltype.getTypeArguments().isEmpty()) {
                    out(",a:");
                    TypeUtils.printTypeArguments(that, ltype.getTypeArguments(), this);
                }
                out("}})");
            }
        }
    }

    @Override
    public void visit(Tree.PackageLiteral that) {
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = (com.redhat.ceylon.compiler.typechecker.model.Package)that.getImportPath().getModel();
        
        out(clAlias, "getModules$meta().find('", pkg.getModule().getNameAsString(),
                "','", pkg.getModule().getVersion(), "').findPackage('", pkg.getNameAsString(),
                "')");
    }

    @Override
    public void visit(Tree.ModuleLiteral that) {
        Module m = (Module)that.getImportPath().getModel();
        out(clAlias, "getModules$meta().find('", m.getNameAsString(),
                "','", m.getVersion(), "')");
    }

    /** Call internal function "throwexc" with the specified message and source location. */
    void generateThrow(String msg, Node node) {
        out(clAlias, "throwexc(", clAlias, "Exception(", clAlias, "String");
        if (JsCompiler.isCompilingLanguageModule()) {
            out("$");
        }
        out("(\"", escapeStringLiteral(msg), "\")),'", node.getLocation(), "','",
                node.getUnit().getFilename(), "')");
    }

    @Override public void visit(Tree.CompilerAnnotation that) {
        //just ignore this
    }

}

