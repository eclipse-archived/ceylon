package com.redhat.ceylon.compiler.js;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.eliminateParensAndWidening;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.runtime.CommonToken;

import com.redhat.ceylon.compiler.js.util.ContinueBreakVisitor;
import com.redhat.ceylon.compiler.js.util.JsIdentifierNames;
import com.redhat.ceylon.compiler.js.util.JsOutput;
import com.redhat.ceylon.compiler.js.util.JsUtils;
import com.redhat.ceylon.compiler.js.util.JsWriter;
import com.redhat.ceylon.compiler.js.util.Options;
import com.redhat.ceylon.compiler.js.util.RetainedVars;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.js.util.TypeUtils.RuntimeMetamodelAnnotationGenerator;
import com.redhat.ceylon.compiler.typechecker.tree.*;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.*;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassAlias;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.MethodOrValue;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Util;
import com.redhat.ceylon.model.typechecker.model.Value;

public class GenerateJsVisitor extends Visitor
        implements NaturalVisitor {

    private final Stack<ContinueBreakVisitor> continues = new Stack<>();
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess = new HashSet<>();
    private final Set<Declaration> generatedAttributes = new HashSet<>();
    private final RetainedVars retainedVars = new RetainedVars();
    final ConditionGenerator conds;
    private final InvocationGenerator invoker;
    private final List<CommonToken> tokens;
    private final ErrorVisitor errVisitor = new ErrorVisitor();
    private int dynblock;
    private int exitCode = 0;
    protected static final BigInteger minLong = new BigInteger(Long.toString(Long.MIN_VALUE));
    protected static final BigInteger maxLong = new BigInteger(Long.toString(Long.MAX_VALUE));
    protected static final BigInteger maxUnsignedLong = new BigInteger("ffffffffffffffff", 16);

    static final class SuperVisitor extends Visitor {
        private final List<Declaration> decs;

        SuperVisitor(List<Declaration> decs) {
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

    public final JsWriter out;
    private final PrintWriter verboseOut;
    public final Options opts;
    private CompilationUnit root;
    static final String function="function ";

    public Package getCurrentPackage() {
        return root.getUnit().getPackage();
    }

    /** Returns the module name for the language module. */
    public String getClAlias() { return jsout.getLanguageModuleAlias(); }

    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this));
    }

    private final JsOutput jsout;

    public GenerateJsVisitor(JsOutput out, Options options, JsIdentifierNames names,
            List<CommonToken> tokens) throws IOException {
        this.jsout = out;
        this.opts = options;
        if (options.hasVerboseFlag("code")) {
            Writer vw = options.getOutWriter();
            verboseOut = vw instanceof PrintWriter ? (PrintWriter)vw :
                new PrintWriter(vw == null ? new OutputStreamWriter(System.out) : vw);
        } else {
            verboseOut = null;
        }
        this.names = names;
        conds = new ConditionGenerator(this, names, directAccess);
        this.tokens = tokens;
        invoker = new InvocationGenerator(this, names, retainedVars);
        this.out = new JsWriter(out.getWriter(), verboseOut, opts.isMinify());
        out.setJsWriter(this.out);
    }

    public InvocationGenerator getInvoker() { return invoker; }

    /** Returns the helper component to handle naming. */
    public JsIdentifierNames getNames() { return names; }

    public static interface GenerateCallback {
        public void generateValue();
    }
    
    /** Print generated code to the Writer specified at creation time.
     * Automatically prints indentation first if necessary.
     * @param code The main code
     * @param codez Optional additional strings to print after the main code. */
    public void out(String code, String... codez) {
        out.write(code,  codez);
    }

    /** Prints a newline. Indentation will automatically be printed by {@link #out(String, String...)}
     * when the next line is started. */
    void endLine() {
        out.endLine(false);
    }
    /** Prints a newline. Indentation will automatically be printed by {@link #out(String, String...)}
     * when the next line is started.
     * @param semicolon  if <code>true</code> then a semicolon is printed at the end
     *                  of the previous line*/
    public void endLine(boolean semicolon) {
        out.endLine(semicolon);
    }
    /** Calls {@link #endLine()} if the current position is not already the beginning
     * of a line. */
    void beginNewLine() {
        out.endLine(false);
    }

    /** Increases indentation level, prints opening brace and newline. Indentation will
     * automatically be printed by {@link #out(String, String...)} when the next line is started. */
    void beginBlock() {
        out.beginBlock();
    }

    /** Decreases indentation level, prints a closing brace in new line (using
     * {@link #beginNewLine()}) and calls {@link #endLine()}. */
    void endBlockNewLine() {
        out.endBlock(false, true);
    }
    /** Decreases indentation level, prints a closing brace in new line (using
     * {@link #beginNewLine()}) and calls {@link #endLine()}.
     * @param semicolon  if <code>true</code> then prints a semicolon after the brace*/
    void endBlockNewLine(boolean semicolon) {
        out.endBlock(semicolon, true);
    }
    /** Decreases indentation level and prints a closing brace in new line (using
     * {@link #beginNewLine()}). */
    void endBlock() {
        out.endBlock(false, false);
    }
    /** Decreases indentation level and prints a closing brace in new line (using
     * {@link #beginNewLine()}).
     * @param semicolon  if <code>true</code> then prints a semicolon after the brace
     * @param newline  if <code>true</code> then additionally calls {@link #endLine()} */
    void endBlock(boolean semicolon, boolean newline) {
        out.endBlock(semicolon, newline);
    }

    void spitOut(String s) {
        out.spitOut(s);
    }

    /** Prints source code location in the form "at [filename] ([location])" */
    public void location(Node node) {
        out(" at ", node.getUnit().getFilename(), " (", node.getLocation(), ")");
    }

    private String generateToString(final GenerateCallback callback) {
        return out.generateToString(callback);
    }
    
    @Override
    public void visit(final Tree.CompilationUnit that) {
        root = that;
        if (!that.getModuleDescriptors().isEmpty()) {
            final ModuleDescriptor md = that.getModuleDescriptors().get(0);
            out("ex$.$mod$ans$=");
            TypeUtils.outputAnnotationsFunction(md.getAnnotationList(), new TypeUtils.AnnotationFunctionHelper() {
                @Override
                public String getPathToModelDoc() {
                    return "''";
                }
                @Override
                public String getPackedAnnotationsKey() {
                    return null;
                }
                @Override
                public String getAnnotationsKey() {
                    return null;
                }
                @Override
                public List<com.redhat.ceylon.model.typechecker.model.Annotation> getAnnotations() {
                    return md.getUnit().getPackage().getModule().getAnnotations();
                }
                @Override
                public Object getAnnotationSource() {
                    return md.getUnit().getPackage().getModule();
                }
                @Override
                public String getAnPath() {
                    return "'$mod-anns'";
                }
            }, this);
            endLine(true);
            if (md.getImportModuleList() != null && !md.getImportModuleList().getImportModules().isEmpty()) {
                out("ex$.$mod$imps=function(){return{");
                if (!opts.isMinify())endLine();
                boolean first=true;
                for (final ImportModule im : md.getImportModuleList().getImportModules()) {
                    final StringBuilder path=new StringBuilder("'");
                    if (im.getImportPath()==null) {
                        if (im.getQuotedLiteral()==null) {
                            throw new CompilerErrorException("Invalid imported module");
                        } else {
                            final String ql = im.getQuotedLiteral().getText();
                            path.append(ql.substring(1, ql.length()-1));
                        }
                    } else {
                        for (Identifier id : im.getImportPath().getIdentifiers()) {
                            if (path.length()>1)path.append('.');
                            path.append(id.getText());
                        }
                    }
                    final String qv = im.getVersion().getText();
                    path.append('/').append(qv.substring(1, qv.length()-1)).append("'");
                    if (first)first=false;else{out(",");endLine();}
                    out(path.toString(), ":");
                    TypeUtils.outputAnnotationsFunction(im.getAnnotationList(), 
                            new TypeUtils.AnnotationFunctionHelper(){
                        @Override
                        public String getPathToModelDoc() {
                            return null;
                        }
                        @Override
                        public String getPackedAnnotationsKey() {
                            return null;
                        }
                        @Override
                        public String getAnnotationsKey() {
                            return null;
                        }
                        @Override
                        public List<com.redhat.ceylon.model.typechecker.model.Annotation> getAnnotations() {
                            if (im.getImportPath().getModel() instanceof Module) {
                                return ((Module)im.getImportPath().getModel()).getAnnotations();
                            }
                            return null;
                        }
                        @Override
                        public Object getAnnotationSource() {
                            return im;
                        }
                        @Override
                        public String getAnPath() {
                            return null;
                        }
                    }, this);
                }
                if (!opts.isMinify())endLine();
                out("};};");
                if (!opts.isMinify())endLine();
            }
        }
        if (!that.getPackageDescriptors().isEmpty()) {
            final String pknm = that.getUnit().getPackage().getNameAsString().replaceAll("\\.", "\\$");
            out("ex$.$pkg$ans$", pknm, "=");
            TypeUtils.outputAnnotationsFunction(that.getPackageDescriptors().get(0).getAnnotationList(),
                    new TypeUtils.AnnotationFunctionHelper(){
                @Override
                public String getPathToModelDoc() {
                    return "'" + that.getUnit().getPackage().getQualifiedNameString() + "'";
                }
                @Override
                public String getPackedAnnotationsKey() {
                    return null;
                }
                @Override
                public String getAnnotationsKey() {
                    return null;
                }
                @Override
                public List<com.redhat.ceylon.model.typechecker.model.Annotation> getAnnotations() {
                    return that.getUnit().getPackage().getAnnotations();
                }
                @Override
                public Object getAnnotationSource() {
                    return that.getUnit().getPackage();
                }
                @Override
                public String getAnPath() {
                    return "'$pkg-anns'";
                }
            }, this);
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

    public void visit(final Tree.Import that) {
    }

    @Override
    public void visit(final Tree.Parameter that) {
        out(names.name(that.getParameterModel()));
    }

    @Override
    public void visit(final Tree.ParameterList that) {
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

    /** Generate the statements for a class initializer, considering the specified constructor, if any.
     * @param cdef the class definition for which the body is generated.
     * @param cnstr The constructor for which the statements are being generated (optional)
     * @param cnst2 An optional constructor for which statements should also be generated
     * (used in pseudo abstract constructor generation)
     * @param filter Indicates if the generation should start or stop at a certain point:
     * 0 means don't stop, 1 means stop after generating the constructor's statements,
     * 2 means start after the constructor's statements, and include the second constructor's statements. */
    void generateClassStatements(final Tree.ClassDefinition cdef, final Tree.Constructor cnstr,
            final Tree.Constructor cnst2, final int filter) {
        if (cnstr == null) {
            visitStatements(cdef.getClassBody().getStatements());
        } else if (cnstr.getDeclarationModel().isAbstract()) {
            ReturnConstructorVisitor rcv = new ReturnConstructorVisitor(cnstr);
            if (rcv.isReturns()) {
                out("(function(){");
            }
            for (Tree.Statement s2 : cnstr.getBlock().getStatements()) {
                //Ignore return statements directly in main constructor scope
                s2.visit(this);
                if (!opts.isMinify())beginNewLine();
                retainedVars.emitRetainedVars(this);
            }
            if (rcv.isReturns()) {
                out("}())");
            }
        } else {
            List<String> oldRetainedVars = retainedVars.reset(null);
            final List<? extends Statement> prevStatements = currentStatements;
            currentStatements = cdef.getClassBody().getStatements();
            boolean go = filter < 2;
            for (Tree.Statement st : cdef.getClassBody().getStatements()) {
                if (st == cnstr || st == cnst2) {
                    if (filter == 2 && st == cnstr) {
                        go=true;
                    } else {
                        ReturnConstructorVisitor rcv = new ReturnConstructorVisitor(cnstr);
                        if (rcv.isReturns()) {
                            out("(function(){");
                        }
                        for (Tree.Statement s2 : ((Tree.Constructor)st).getBlock().getStatements()) {
                            //Ignore return statements directly in main constructor scope
                            s2.visit(this);
                            if (!opts.isMinify())beginNewLine();
                            retainedVars.emitRetainedVars(this);
                        }
                        go=filter!=1;
                        if (rcv.isReturns()) {
                            out("}());");
                        }
                    }
                } else if (st instanceof Tree.Constructor == false && go) {
                    //Ignore return statements directly in main constructor scope
                    st.visit(this);
                    if (!opts.isMinify())beginNewLine();
                    retainedVars.emitRetainedVars(this);
                    //Remove attribute declaration in lexical style so that
                    //every constructor adds metamodel to that attribute
                    if (!opts.isOptimize() && cnstr != null && st instanceof Tree.AttributeDeclaration) {
                        generatedAttributes.remove(((Tree.AttributeDeclaration)st).getDeclarationModel());
                    }
                }
            }
            retainedVars.reset(oldRetainedVars);
            currentStatements = prevStatements;
        }
    }

    void visitStatements(List<? extends Statement> statements) {
        List<String> oldRetainedVars = retainedVars.reset(null);
        final List<? extends Statement> prevStatements = currentStatements;
        currentStatements = statements;

        for (int i=0; i<statements.size(); i++) {
            Statement s = statements.get(i);
            s.visit(this);
            if (!opts.isMinify())beginNewLine();
            retainedVars.emitRetainedVars(this);
        }
        retainedVars.reset(oldRetainedVars);
        currentStatements = prevStatements;
    }

    @Override
    public void visit(final Tree.Body that) {
        visitStatements(that.getStatements());
    }

    @Override
    public void visit(final Tree.Block that) {
        List<Statement> stmnts = that.getStatements();
        if (stmnts.isEmpty()) {
            out("{}");
        }
        else {
            beginBlock();
            visitStatements(stmnts);
            endBlock();
        }
    }

    void initSelf(Node node) {
        final NeedsThisVisitor ntv = new NeedsThisVisitor(node);
        if (ntv.needsThisReference()) {
            final String me=names.self(prototypeOwner);
            out("var ", me, "=this");
            //Code inside anonymous inner types sometimes gens direct refs to the outer instance
            //We can detect if that's going to happen and declare the ref right here
            if (ntv.needsOuterReference()) {
                final Declaration od = Util.getContainingDeclaration(prototypeOwner);
                if (od instanceof TypeDeclaration) {
                    out(",", names.self((TypeDeclaration)od), "=", me, ".outer$");
                }
            }
            endLine(true);
        }
    }

    /** Visitor that determines if a method definition will need the "this" reference. */
    class NeedsThisVisitor extends Visitor {
        private boolean refs=false;
        private boolean outerRefs=false;
        NeedsThisVisitor(Node n) {
            if (prototypeOwner != null) {
                n.visit(this);
            }
        }
        @Override public void visit(Tree.This that) {
            refs = true;
        }
        @Override public void visit(Tree.Outer that) {
            refs = true;
        }
        @Override public void visit(Tree.Super that) {
            refs = true;
        }
        private boolean check(final Scope origScope) {
            Scope s = origScope;
            while (s != null) {
                if (s == prototypeOwner || (s instanceof TypeDeclaration && prototypeOwner.inherits((TypeDeclaration)s))) {
                    refs = true;
                    if (prototypeOwner.isAnonymous() && prototypeOwner.isMember()) {
                        outerRefs=true;
                    }
                    return true;
                }
                s = s.getContainer();
            }
            //Check the other way around as well
            s = prototypeOwner;
            while (s != null) {
                if (s == origScope ||
                        (s instanceof TypeDeclaration && origScope instanceof TypeDeclaration
                                && ((TypeDeclaration)s).inherits((TypeDeclaration)origScope))) {
                    refs = true;
                    return true;
                }
                s = s.getContainer();
            }
            return false;
        }
        public void visit(Tree.MemberOrTypeExpression that) {
            if (refs)return;
            if (that.getDeclaration() == null) {
                //Some expressions in dynamic blocks can have null declarations
                super.visit(that);
                return;
            }
            if (!check(that.getDeclaration().getContainer())) {
                super.visit(that);
            }
        }
        public void visit(Tree.Type that) {
            if (!check(that.getTypeModel().getDeclaration())) {
                super.visit(that);
            }
        }
        public void visit(Tree.ParameterList plist) {
            for (Tree.Parameter param : plist.getParameters()) {
                if (param.getParameterModel().isDefaulted()) {
                    refs = true;
                    return;
                }
            }
            super.visit(plist);
        }
        boolean needsThisReference() {
            return refs;
        }
        boolean needsOuterReference() {
            return outerRefs;
        }
    }

    void comment(Tree.Declaration that) {
        if (!opts.isComment() || opts.isMinify()) return;
        endLine();
        String dname = that.getNodeType();
        if (dname.endsWith("Declaration") || dname.endsWith("Definition")) {
            dname = dname.substring(0, dname.length()-7);
        }
        if (that instanceof Tree.Constructor) {
            String cname = ((com.redhat.ceylon.model.typechecker.model.Class)((Tree.Constructor)that)
                    .getDeclarationModel().getContainer()).getName();
            out("//Constructor ", cname, ".",
                    that.getDeclarationModel().getName() == null ? "<default>" : that.getDeclarationModel().getName());
        } else {
            out("//", dname, " ", that.getDeclarationModel().getName());
        }
        location(that);
        endLine();
    }

    boolean share(Declaration d) {
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
            out(".", dname, "=", dname);
            endLine(true);
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(final Tree.ClassDeclaration that) {
        if (!TypeUtils.acceptNative(that)) return;
        if (opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember()) return;
        classDeclaration(that);
    }

    private void addClassDeclarationToPrototype(TypeDeclaration outer, final Tree.ClassDeclaration that) {
        classDeclaration(that);
        final String tname = names.name(that.getDeclarationModel());
        out(names.self(outer), ".", tname, "=", tname);
        endLine(true);
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
        out(names.self(outer), ".", tname, "=function(){var ");
        ProducedType pt = that.getTypeSpecifier().getType().getTypeModel();
        boolean skip=true;
        if (pt.containsTypeParameters() && outerSelf(d)) {
            out("=this,");
            skip=false;
        }
        out(_tmp, "=");
        TypeUtils.typeNameOrList(that, pt, this, skip);
        out(";", _tmp, ".$crtmm$=");
        TypeUtils.encodeForRuntime(that,d,this);
        out(";return ", _tmp, ";}");
        endLine(true);
    }

    @Override
    public void visit(final Tree.InterfaceDeclaration that) {
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            interfaceDeclaration(that);
        }
    }

    private void addInterfaceDeclarationToPrototype(TypeDeclaration outer, final Tree.InterfaceDeclaration that) {
        interfaceDeclaration(that);
        final String tname = names.name(that.getDeclarationModel());
        out(names.self(outer), ".", tname, "=", tname);
        endLine(true);
    }

    private void addInterfaceToPrototype(ClassOrInterface type, final Tree.InterfaceDefinition interfaceDef) {
        if (type.isDynamic())return;
        TypeGenerator.interfaceDefinition(interfaceDef, this);
        Interface d = interfaceDef.getDeclarationModel();
        out(names.self(type), ".", names.name(d), "=", names.name(d));
        endLine(true);
    }

    @Override
    public void visit(final Tree.InterfaceDefinition that) {
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            TypeGenerator.interfaceDefinition(that, this);
        }
    }

    private void addClassToPrototype(ClassOrInterface type, final Tree.ClassDefinition classDef) {
        if (type.isDynamic())return;
        TypeGenerator.classDefinition(classDef, this);
        final String tname = names.name(classDef.getDeclarationModel());
        out(names.self(type), ".", tname, "=", tname);
        endLine(true);
    }

    @Override
    public void visit(final Tree.ClassDefinition that) {
        if (!TypeUtils.acceptNative(that)) return;
        if (opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember()) return;
        TypeGenerator.classDefinition(that, this);
    }

    private void interfaceDeclaration(final Tree.InterfaceDeclaration that) {
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
        out(names.self(d), "){");
        final ProducedType pt = ext.getTypeModel();
        final TypeDeclaration aliased = pt.getDeclaration();
        qualify(that,aliased);
        out(names.name(aliased), "(");
        if (!pt.getTypeArguments().isEmpty()) {
            TypeUtils.printTypeArguments(that, pt.getTypeArguments(), this, true,
                    pt.getVarianceOverrides());
            out(",");
        }
        out(names.self(d), ");}");
        endLine();
        out(aname,".$crtmm$=");
        TypeUtils.encodeForRuntime(that, d, this);
        endLine(true);
        share(d);
    }

    private void classDeclaration(final Tree.ClassDeclaration that) {
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
        out(names.self(d), "){");
        initSelf(that);
        out("return ");
        TypeDeclaration aliased = ext.getType().getDeclarationModel();
        final String aliasedName;
        if (aliased instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
            aliasedName = names.name((Class)aliased.getContainer()) + "_" + names.name(aliased);
        } else {
            aliasedName = names.name(aliased);
        }
        qualify(that, aliased);
        Scope superscope = getSuperMemberScope(ext.getType());
        if (superscope != null) {
            out("getT$all()['");
            if (superscope instanceof Declaration) {
                out(TypeUtils.qualifiedNameSkippingMethods((Declaration)superscope));
            } else {
                out(superscope.getQualifiedNameString());
            }
            out("'].$$.prototype.", aliasedName, ".call(", names.self(prototypeOwner), ",");
        } else {
            out(aliasedName, "(");
        }
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
            TypeUtils.printTypeArguments(that, invargs, this, true,
                    ext.getType().getTypeModel().getVarianceOverrides());
            out(",");
        }
        out(names.self(d), ");}");
        endLine();
        out(aname, ".$$=");
        qualify(that, aliased);
        out(aliasedName, ".$$");
        endLine(true);
        out(aname,".$crtmm$=");
        TypeUtils.encodeForRuntime(that, d, this);
        endLine(true);
        share(d);
    }

    @Override
    public void visit(final Tree.TypeAliasDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final TypeAlias d = that.getDeclarationModel();
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) return;
        comment(that);
        final String tname=names.createTempVariable();
        out(function, names.name(d), "{var ", tname, "=");
        TypeUtils.typeNameOrList(that, that.getTypeSpecifier().getType().getTypeModel(), this, false);
        out(";", tname, ".$crtmm$=");
        TypeUtils.encodeForRuntime(that, d, this, new RuntimeMetamodelAnnotationGenerator() {
            @Override public void generateAnnotations() {
                TypeUtils.outputAnnotationsFunction(that.getAnnotationList(), d, GenerateJsVisitor.this);
            }
        });
        out(";return ", tname, ";}");
        endLine();
        share(d);
    }

    void referenceOuter(TypeDeclaration d) {
        if (!d.isToplevel()) {
            final ClassOrInterface coi = Util.getContainingClassOrInterface(d.getContainer());
            if (coi != null) {
                out(names.self(d), ".outer$");
                if (d.isClassOrInterfaceMember()) {
                    out("=this");
                } else {
                    out("=", names.self(coi));
                }
                endLine(true);
            }
        }
    }

    /** Returns the name of the type or its $init$ function if it's local. */
    String typeFunctionName(final Tree.StaticType type, boolean removeAlias, final ClassOrInterface coi) {
        TypeDeclaration d = type.getTypeModel().getDeclaration();
        if ((removeAlias && d.isAlias()) || d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
            d = d.getExtendedTypeDeclaration();
        }
        final boolean inProto = opts.isOptimize()
                && (type.getScope().getContainer() instanceof TypeDeclaration);
        if (inProto && coi.isMember() && !d.isAlias() && coi.getContainer() == Util.getContainingDeclaration(d)) {
            //A member class that extends or satisfies another member of its same container,
            //use its $init$ function
            return "$init$" +  names.name(d) + "()";
        }
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

    void addToPrototype(Node node, ClassOrInterface d, List<Tree.Statement> statements) {
        final boolean isSerial = d instanceof com.redhat.ceylon.model.typechecker.model.Class
                && ((com.redhat.ceylon.model.typechecker.model.Class)d).isSerializable();
        boolean enter = opts.isOptimize();
        ArrayList<com.redhat.ceylon.model.typechecker.model.Parameter> plist = null;
        if (enter) {
            enter = !statements.isEmpty();
            if (d instanceof com.redhat.ceylon.model.typechecker.model.Class) {
                com.redhat.ceylon.model.typechecker.model.ParameterList _pl =
                        ((com.redhat.ceylon.model.typechecker.model.Class)d).getParameterList();
                if (_pl != null) {
                    plist = new ArrayList<>(_pl.getParameters().size());
                    plist.addAll(_pl.getParameters());
                    enter |= !plist.isEmpty();
                }
            }
        }
        if (enter || isSerial) {
            final List<? extends Statement> prevStatements = currentStatements;
            currentStatements = statements;
            
            out("(function(", names.self(d), ")");
            beginBlock();
            if (enter) {
                //Generated attributes with corresponding parameters will remove them from the list
                if (plist != null) {
                    for (com.redhat.ceylon.model.typechecker.model.Parameter p : plist) {
                        generateAttributeForParameter(node, (com.redhat.ceylon.model.typechecker.model.Class)d, p);
                    }
                }
                for (Statement s: statements) {
                    if (s instanceof Tree.ClassOrInterface == false && !(s instanceof Tree.AttributeDeclaration &&
                            ((Tree.AttributeDeclaration)s).getDeclarationModel().isParameter())) {
                        addToPrototype(d, s, plist);
                    }
                }
                for (Statement s: statements) {
                    if (s instanceof Tree.ClassOrInterface) {
                        addToPrototype(d, s, plist);
                    }
                }
                if (d.isMember()) {
                    ClassOrInterface coi = Util.getContainingClassOrInterface(d.getContainer());
                    if (coi != null && d.inherits(coi)) {
                        out(names.self(d), ".", names.name(d),"=", names.name(d), ";");
                    }
                }
            }
            if (isSerial) {
                SerializationHelper.addSerializer(node, (com.redhat.ceylon.model.typechecker.model.Class)d, this);
            }
            endBlock();
            out(")(", names.name(d), ".$$.prototype)");
            endLine(true);
            
            currentStatements = prevStatements;
        }
    }

    void generateAttributeForParameter(Node node, com.redhat.ceylon.model.typechecker.model.Class d,
            com.redhat.ceylon.model.typechecker.model.Parameter p) {
        final String privname = names.name(p) + "_";
        final MethodOrValue pdec = p.getModel();
        defineAttribute(names.self(d), names.name(pdec));
        out("{");
        if (pdec.isLate()) {
            generateUnitializedAttributeReadCheck("this."+privname, names.name(p));
        }
        out("return this.", privname, ";}");
        if (pdec.isVariable() || pdec.isLate()) {
            final String param = names.createTempVariable();
            out(",function(", param, "){");
            //Because of this one case, we need to pass 3 args to this method
            generateImmutableAttributeReassignmentCheck(pdec, "this."+privname, names.name(p));
            out("return this.", privname,
                    "=", param, ";}");
        } else {
            out(",undefined");
        }
        out(",");
        TypeUtils.encodeForRuntime(node, pdec, this);
        out(")");
        endLine(true);
    }

    private ClassOrInterface prototypeOwner;

    private void addToPrototype(ClassOrInterface d, final Tree.Statement s,
            List<com.redhat.ceylon.model.typechecker.model.Parameter> params) {
        ClassOrInterface oldPrototypeOwner = prototypeOwner;
        prototypeOwner = d;
        if (s instanceof MethodDefinition) {
            addMethodToPrototype(d, (MethodDefinition)s);
        } else if (s instanceof MethodDeclaration) {
            //Don't even bother with nodes that have errors
            if (errVisitor.hasErrors(s))return;
            FunctionHelper.methodDeclaration(d, (MethodDeclaration) s, this);
        } else if (s instanceof AttributeGetterDefinition) {
            addGetterToPrototype(d, (AttributeGetterDefinition)s);
        } else if (s instanceof AttributeDeclaration) {
            AttributeGenerator.addGetterAndSetterToPrototype(d, (AttributeDeclaration) s, this);
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
            for (Iterator<com.redhat.ceylon.model.typechecker.model.Parameter> iter = params.iterator();
                    iter.hasNext();) {
                com.redhat.ceylon.model.typechecker.model.Parameter _p = iter.next();
                if (m.getName() != null && m.getName().equals(_p.getName())) {
                    iter.remove();
                    break;
                }
            }
        }
        prototypeOwner = oldPrototypeOwner;
    }

    void declareSelf(ClassOrInterface d) {
        out("if(", names.self(d), "===undefined)");
        if (d instanceof com.redhat.ceylon.model.typechecker.model.Class && d.isAbstract()) {
            out(getClAlias(), "throwexc(", getClAlias(), "InvocationException$meta$model(");
            out("\"Cannot instantiate abstract class ", d.getQualifiedNameString(), "\"),'?','?')");
        } else {
            out(names.self(d), "=new ");
            if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
                out("this.");
            }
            out(names.name(d), ".$$;");
        }
        endLine();
    }

    void instantiateSelf(ClassOrInterface d) {
        out("var ", names.self(d), "=new ");
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
            out("this.");
        }
        out(names.name(d), ".$$;");
    }

    private void addObjectToPrototype(ClassOrInterface type, final Tree.ObjectDefinition objDef) {
        TypeGenerator.objectDefinition(objDef, this);
        Value d = objDef.getDeclarationModel();
        Class c = (Class) d.getTypeDeclaration();
        out(names.self(type), ".", names.name(c), "=", names.name(c), ";",
                names.self(type), ".", names.name(c), ".$crtmm$=");
        TypeUtils.encodeForRuntime(objDef, d, this);
        endLine(true);
    }

    @Override
    public void visit(final Tree.ObjectDefinition that) {
        if (errVisitor.hasErrors(that))return;
        Value d = that.getDeclarationModel();
        if (!(opts.isOptimize() && d.isClassOrInterfaceMember())) {
            TypeGenerator.objectDefinition(that, this);
        } else {
            //Don't even bother with nodes that have errors
            if (errVisitor.hasErrors(that))return;
            Class c = (Class) d.getTypeDeclaration();
            comment(that);
            outerSelf(d);
            out(".", names.privateName(d), "=");
            outerSelf(d);
            out(".", names.name(c), "()");
            endLine(true);
        }
    }

    @Override
    public void visit(final Tree.ObjectExpression that) {
        if (errVisitor.hasErrors(that))return;
        out("function(){");
        try {
            TypeGenerator.defineObject(that, null, that.getSatisfiedTypes(),
                    that.getExtendedType(), that.getClassBody(), null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out("}()");
    }

    @Override
    public void visit(final Tree.MethodDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        FunctionHelper.methodDeclaration(null, that, this);
    }

    boolean shouldStitch(Declaration d) {
        return TypeUtils.isNativeExternal(d);
    }

    private File getStitchedFilename(final Declaration d, final String suffix) {
        String fqn = d.getQualifiedNameString();
        if (d.getName() == null && d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
            String cname = "default$constructor";
            fqn = fqn.substring(0, fqn.length()-4) + cname;
        }
        if (fqn.startsWith("ceylon.language"))fqn = fqn.substring(15);
        if (fqn.startsWith("::"))fqn=fqn.substring(2);
        fqn = fqn.replace('.', '/').replace("::", "/");
        if (JsCompiler.isCompilingLanguageModule()) {
            return new File(Stitcher.LANGMOD_JS_SRC, fqn + suffix);
        } else {
            return new File(new File(d.getUnit().getFullPath()).getParentFile(), fqn + suffix);
        }
    }

    /** Reads a file with hand-written snippet and outputs it to the current writer. */
    boolean stitchNative(final Declaration d, final Tree.Declaration n) {
        final File f = getStitchedFilename(d, ".js");
        if (f.exists() && f.canRead()) {
            jsout.outputFile(f);
            if (d.isClassOrInterfaceMember()) {
                if (d instanceof Value || n instanceof Tree.Constructor) {
                    //Native values are defined as attributes
                    //Constructor metamodel is done in TypeGenerator.classConstructor
                    return true;
                }
                out(names.self((TypeDeclaration)d.getContainer()), ".");
            } else if (n instanceof Tree.AttributeDeclaration || n instanceof Tree.AttributeGetterDefinition) {
                return true;
            }
            out(names.name(d), ".$crtmm$=");
            TypeUtils.encodeForRuntime(d, n.getAnnotationList(), this);
            endLine(true);
            return true;
        } else {
            if (!(d instanceof ClassOrInterface || n instanceof Tree.MethodDefinition
                    || (n instanceof Tree.MethodDeclaration && ((Tree.MethodDeclaration)n).getSpecifierExpression() != null)
                    || n instanceof Tree.AttributeGetterDefinition
                    || (n instanceof Tree.AttributeDeclaration
                            && ((Tree.AttributeDeclaration)n).getSpecifierOrInitializerExpression() != null))) {
                String missingDeclarationName = d.getQualifiedNameString();
                if (d.getName() == null && d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                    missingDeclarationName = missingDeclarationName.substring(0, missingDeclarationName.length()-4)
                            + "<default constructor>";
                }
                final String err = "REQUIRED NATIVE FILE MISSING FOR "
                        + missingDeclarationName + " => " + f + ", containing " + names.name(d);
                n.addError(err);
                spitOut(err);
                out("/*", err, "*/");
            }
            return false;
        }
    }

    /** Stitch a snippet of code to initialize type (usually a call to initTypeProto). */
    boolean stitchInitializer(TypeDeclaration d) {
        final File f = getStitchedFilename(d, "$init.js");
        if (f.exists() && f.canRead()) {
            jsout.outputFile(f);
            return true;
        }
        return false;
    }

    boolean stitchConstructorHelper(final Tree.ClassOrInterface coi, final String partName) {
        final File f;
        if (JsCompiler.isCompilingLanguageModule()) {
            f = getStitchedFilename(coi.getDeclarationModel(), partName + ".js");
        } else {
            f = new File(new File(coi.getUnit().getFullPath()).getParentFile(),
                    String.format("%s%s.js", names.name(coi.getDeclarationModel()), partName));
        }
        if (f.exists() && f.isFile() && f.canRead()) {
            if (verboseOut != null || JsCompiler.isCompilingLanguageModule()) {
                spitOut("Stitching in " + f + ". It must contain an anonymous function "
                        + "which will be invoked with the same arguments as the "
                        + names.name(coi.getDeclarationModel()) + " constructor.");
            }
            out("(");
            jsout.outputFile(f);
            out(")");
            TypeGenerator.generateParameters(coi.getTypeParameterList(), coi instanceof Tree.ClassDefinition ?
                    ((Tree.ClassDefinition)coi).getParameterList() : null, coi.getDeclarationModel(), this);
            endLine(true);
        }
        return false;
    }

    @Override
    public void visit(final Tree.MethodDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        final Method d = that.getDeclarationModel();
        if (!((opts.isOptimize() && d.isClassOrInterfaceMember()) || TypeUtils.isNativeExternal(d))) {
            comment(that);
            initDefaultedParameters(that.getParameterLists().get(0), d);
            FunctionHelper.methodDefinition(that, this, true);
            //Add reference to metamodel
            out(names.name(d), ".$crtmm$=");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            endLine(true);
        }
    }

    /** Get the specifier expression for a Parameter, if one is available. */
    private SpecifierOrInitializerExpression getDefaultExpression(final Tree.Parameter param) {
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
    void initDefaultedParameters(final Tree.ParameterList params, Method container) {
        if (!container.isMember())return;
        for (final Parameter param : params.getParameters()) {
            com.redhat.ceylon.model.typechecker.model.Parameter pd = param.getParameterModel();
            if (pd.isDefaulted()) {
                final SpecifierOrInitializerExpression expr = getDefaultExpression(param);
                if (expr == null) {
                    continue;
                }
                qualify(params, container);
                out(names.name(container), "$defs$", pd.getName(), "=function");
                params.visit(this);
                out("{");
                initSelf(expr);
                out("return ");
                if (param instanceof ParameterDeclaration &&
                        ((ParameterDeclaration)param).getTypedDeclaration() instanceof MethodDeclaration) {
                    // function parameter defaulted using "=>"
                    FunctionHelper.singleExprFunction(
                            ((MethodDeclaration)((ParameterDeclaration)param).getTypedDeclaration()).getParameterLists(),
                            expr.getExpression(), null, true, true, this);
                } else if (!isNaturalLiteral(expr.getExpression().getTerm())) {
                    expr.visit(this);
                }
                out(";}");
                endLine(true);
            }
        }
    }

    /** Initialize the sequenced, defaulted and captured parameters in a type declaration. */
    void initParameters(final Tree.ParameterList params, TypeDeclaration typeDecl, Method m) {
        for (final Parameter param : params.getParameters()) {
            com.redhat.ceylon.model.typechecker.model.Parameter pd = param.getParameterModel();
            final String paramName = names.name(pd);
            if (pd.isDefaulted() || pd.isSequenced()) {
                out("if(", paramName, "===undefined){", paramName, "=");
                if (pd.isDefaulted()) {
                    if (m !=null && m.isMember()) {
                        qualify(params, m);
                        out(names.name(m), "$defs$", pd.getName(), "(");
                        boolean firstParam=true;
                        for (com.redhat.ceylon.model.typechecker.model.Parameter p :
                                m.getParameterLists().get(0).getParameters()) {
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
                            FunctionHelper.singleExprFunction(
                                    ((MethodDeclaration)((ParameterDeclaration)param).getTypedDeclaration()).getParameterLists(),
                                    expr.getExpression(), m, true, true, this);
                        } else {
                            expr.visit(this);
                        }
                    }
                } else {
                    out(getClAlias(), "empty()");
                }
                out(";}");
                endLine();
            }
            if ((typeDecl != null) && (pd.getModel().isCaptured() ||
                    pd.getDeclaration() instanceof com.redhat.ceylon.model.typechecker.model.Class)) {
                out(names.self(typeDecl), ".", paramName, "_=", paramName);
                if (!opts.isOptimize() && pd.isHidden()) { //belt and suspenders...
                    out(";", names.self(typeDecl), ".", paramName, "=", paramName);
                }
                endLine(true);
            }
        }
    }

    private void addMethodToPrototype(TypeDeclaration outer,
            final Tree.MethodDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        Method d = that.getDeclarationModel();
        if (!opts.isOptimize()||!d.isClassOrInterfaceMember()) return;
        comment(that);
        initDefaultedParameters(that.getParameterLists().get(0), d);
        out(names.self(outer), ".", names.name(d), "=");
        FunctionHelper.methodDefinition(that, this, false);
        //Add reference to metamodel
        out(names.self(outer), ".", names.name(d), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
    }

    @Override
    public void visit(final Tree.AttributeGetterDefinition that) {
        if (errVisitor.hasErrors(that))return;
        Value d = that.getDeclarationModel();
        if (opts.isOptimize()&&d.isClassOrInterfaceMember()) return;
        comment(that);
        if (defineAsProperty(d)) {
            defineAttribute(names.self((TypeDeclaration)d.getContainer()), names.name(d));
            AttributeGenerator.getter(that, this, true);
            final AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
            if (setterDef == null) {
                out(",undefined");
            } else {
                out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
                AttributeGenerator.setter(setterDef, this);
            }
            out(",");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            if (setterDef != null) {
                out(",");
                TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), setterDef.getAnnotationList(), this);
            }
            out(");");
        }
        else {
            out(function, names.getter(d, false), "()");
            if (shouldStitch(d)) {
                out("{");
                if (stitchNative(d, that)) {
                    spitOut("Stitching in native attribute " + d.getQualifiedNameString()
                            + ", ignoring Ceylon declaration");
                } else {
                    AttributeGenerator.getter(that, this, false);
                }
                out("}");
            } else {
                AttributeGenerator.getter(that, this, true);
            }
            endLine();
            out(names.getter(d, false), ".$crtmm$=");
            TypeUtils.encodeForRuntime(that, d, this);
            if (!shareGetter(d)) { out(";"); }
            generateAttributeMetamodel(that, true, false);
        }
    }

    private void addGetterToPrototype(TypeDeclaration outer,
            final Tree.AttributeGetterDefinition that) {
        Value d = that.getDeclarationModel();
        if (!opts.isOptimize()||!d.isClassOrInterfaceMember()) return;
        comment(that);
        defineAttribute(names.self(outer), names.name(d));
        if (shouldStitch(d)) {
            out("{");
            if (stitchNative(d, that)) {
                spitOut("Stitching in native getter " + d.getQualifiedNameString() +
                        ", ignoring Ceylon declaration");
            } else {
                AttributeGenerator.getter(that, this, false);
            }
            out("}");
        } else {
            AttributeGenerator.getter(that, this, true);
        }
        final AttributeSetterDefinition setterDef = associatedSetterDefinition(d);
        if (setterDef == null) {
            out(",undefined");
        } else {
            out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
            AttributeGenerator.setter(setterDef, this);
        }
        out(",");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        if (setterDef != null) {
            out(",");
            TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(), setterDef.getAnnotationList(), this);
        }
        out(");");
    }
    
    Tree.AttributeSetterDefinition associatedSetterDefinition(
            final Value valueDecl) {
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
    boolean shareGetter(final MethodOrValue d) {
        boolean shared = false;
        if (isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            out(".", names.getter(d, false), "=", names.getter(d, false));
            endLine(true);
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(final Tree.AttributeSetterDefinition that) {
        if (errVisitor.hasErrors(that))return;
        Setter d = that.getDeclarationModel();
        if ((opts.isOptimize()&&d.isClassOrInterfaceMember()) || defineAsProperty(d)) return;
        comment(that);
        out("function ", names.setter(d.getGetter()), "(", names.name(d.getParameter()), ")");
        AttributeGenerator.setter(that, this);
        if (!shareSetter(d)) { out(";"); }
        if (!d.isToplevel())outerSelf(d);
        out(names.setter(d.getGetter()), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
        endLine(true);
        generateAttributeMetamodel(that, false, true);
    }

    boolean isCaptured(Declaration d) {
        if (d.isToplevel()||d.isClassOrInterfaceMember()) {
            if (d.isShared() || d.isCaptured()) {
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

    boolean shareSetter(final MethodOrValue d) {
        boolean shared = false;
        if (isCaptured(d)) {
            beginNewLine();
            outerSelf(d);
            out(".", names.setter(d), "=", names.setter(d));
            endLine(true);
            shared = true;
        }
        return shared;
    }

    @Override
    public void visit(final Tree.AttributeDeclaration that) {
        if (errVisitor.hasErrors(that))return;
        final Value d = that.getDeclarationModel();
        //Check if the attribute corresponds to a class parameter
        //This is because of the new initializer syntax
        final com.redhat.ceylon.model.typechecker.model.Parameter param = d.isParameter() ?
                ((Functional)d.getContainer()).getParameter(d.getName()) : null;
        final boolean asprop = defineAsProperty(d);
        if (d.isFormal()) {
            if (!opts.isOptimize()) {
                comment(that);
                generateAttributeMetamodel(that, false, false);
            }
        } else {
            SpecifierOrInitializerExpression specInitExpr =
                        that.getSpecifierOrInitializerExpression();
            final boolean addGetter = (specInitExpr != null) || (param != null) || !d.isMember()
                    || d.isVariable() || d.isLate();
            final boolean addSetter = (d.isVariable() || d.isLate()) && !asprop;
            if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
                //Stitch native member attribute declaration with no value
                final boolean eagerExpr = specInitExpr != null
                        && !(specInitExpr instanceof LazySpecifierExpression); 
                if (eagerExpr && !shouldStitch(d)) {
                    comment(that);
                    outerSelf(d);
                    out(".", names.privateName(d), "=");
                    if (d.isLate()) {
                        out("undefined");
                    } else {
                        super.visit(specInitExpr);
                    }
                    endLine(true);
                }
            }
            else if (specInitExpr instanceof LazySpecifierExpression) {
                comment(that);
                if (asprop) {
                    defineAttribute(names.self((TypeDeclaration)d.getContainer()), names.name(d));
                    out("{");
                } else {
                    out(function, names.getter(d, false), "(){");
                }
                initSelf(that);
                boolean genatr=true;
                if (shouldStitch(d)) {
                    if (stitchNative(d, that)) {
                        spitOut("Stitching in native attribute " + d.getQualifiedNameString() +
                                ", ignoring Ceylon declaration");
                        genatr=false;
                        out(";};");
                    }
                }
                if (genatr) {
                    out("return ");
                    if (!isNaturalLiteral(specInitExpr.getExpression().getTerm())) {
                        int boxType = boxStart(specInitExpr.getExpression().getTerm());
                        specInitExpr.getExpression().visit(this);
                        if (boxType == 4) out("/*TODO: callable targs 1*/");
                        boxUnboxEnd(boxType);
                    }
                    out(";}");
                    if (asprop) {
                        Tree.AttributeSetterDefinition setterDef = null;
                        if (d.isVariable()) {
                            setterDef = associatedSetterDefinition(d);
                            if (setterDef != null) {
                                out(",function(", names.name(setterDef.getDeclarationModel().getParameter()), ")");
                                AttributeGenerator.setter(setterDef, this);
                            }
                        }
                        if (setterDef == null) {
                            out(",undefined");
                        }
                        out(",");
                        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
                        if (setterDef != null) {
                            out(",");
                            TypeUtils.encodeForRuntime(setterDef.getDeclarationModel(),
                                    setterDef.getAnnotationList(), this);
                        }
                        out(")");
                        endLine(true);
                    } else {
                        endLine(true);
                        shareGetter(d);
                    }
                }
            }
            else if (!(d.isParameter() && d.getContainer() instanceof Method)) {
                if (addGetter) {
                    AttributeGenerator.generateAttributeGetter(that, d, specInitExpr,
                            names.name(param), this, directAccess);
                }
                if (addSetter) {
                    AttributeGenerator.generateAttributeSetter(that, d, this);
                }
            }
            boolean addMeta=!opts.isOptimize() || d.isToplevel();
            if (!d.isToplevel()) {
                addMeta |= Util.getContainingDeclaration(d).isAnonymous();
            }
            if (addMeta) {
                generateAttributeMetamodel(that, addGetter, addSetter);
            }
        }
    }

    /** Generate runtime metamodel info for an attribute declaration or definition. */
    void generateAttributeMetamodel(final Tree.TypedDeclaration that,
            final boolean addGetter, final boolean addSetter) {
        //No need to define all this for local values
        Scope _scope = that.getScope();
        while (_scope != null) {
            //TODO this is bound to change for local decl metamodel
            if (_scope instanceof Declaration) {
                if (_scope instanceof Method)return;
                else break;
            }
            _scope = _scope.getContainer();
        }
        Declaration d = that.getDeclarationModel();
        if (d instanceof Setter) d = ((Setter)d).getGetter();
        final String pname = names.getter(d, false);
        final String pnameMeta = names.getter(d, true);
        if (!generatedAttributes.contains(d)) {
            if (d.isToplevel()) {
                out("var ");
            } else if (outerSelf(d)) {
                out(".");
            }
            //issue 297 this is only needed in some cases
            out(pnameMeta, "={$crtmm$:");
            TypeUtils.encodeForRuntime(d, that.getAnnotationList(), this);
            out("}"); endLine(true);
            if (d.isToplevel()) {
                out("ex$.", pnameMeta, "=", pnameMeta);
                endLine(true);
            }
            generatedAttributes.add(d);
        }
        if (addGetter) {
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out(pnameMeta, ".get=");
            if (isCaptured(d) && !defineAsProperty(d)) {
                out(pname);
                endLine(true);
                out(pname, ".$crtmm$=", pnameMeta, ".$crtmm$");
            } else {
                if (d.isToplevel()) {
                    out(pname);
                } else {
                    out("function(){return ", names.name(d), "}");
                }
            }
            endLine(true);
        }
        if (addSetter) {
            final String pset = names.setter(d instanceof Setter ? ((Setter)d).getGetter() : d);
            if (!d.isToplevel()) {
                if (outerSelf(d))out(".");
            }
            out(pnameMeta, ".set=", pset);
            endLine(true);
            out("if(", pset, ".$crtmm$===undefined)", pset, ".$crtmm$=", pnameMeta, ".$crtmm$");
            endLine(true);
        }
    }

    void generateUnitializedAttributeReadCheck(String privname, String pubname) {
        //TODO we can later optimize this, to replace this getter with the plain one
        //once the value has been defined
        out("if(", privname, "===undefined)throw ", getClAlias(),
                "InitializationError('Attempt to read unitialized attribute «", pubname, "»');");
    }
    void generateImmutableAttributeReassignmentCheck(MethodOrValue decl, String privname, String pubname) {
        if (decl.isLate() && !decl.isVariable()) {
            out("if(", privname, "!==undefined)throw ", getClAlias(),
                    "InitializationError('Attempt to reassign immutable attribute «", pubname, "»');");
        }
    }

    @Override
    public void visit(final Tree.CharLiteral that) {
        out(getClAlias(), "Character(");
        out(String.valueOf(that.getText().codePointAt(1)));
        out(",true)");
    }

    @Override
    public void visit(final Tree.StringLiteral that) {
        out("\"", JsUtils.escapeStringLiteral(that.getText()), "\"");
    }

    @Override
    public void visit(final Tree.StringTemplate that) {
        if (errVisitor.hasErrors(that))return;
        //TODO optimize to avoid generating initial "" and final .plus("")
        List<StringLiteral> literals = that.getStringLiterals();
        List<Expression> exprs = that.getExpressions();
        for (int i = 0; i < literals.size(); i++) {
            StringLiteral literal = literals.get(i);
            literal.visit(this);
            if (i>0)out(")");
            if (i < exprs.size()) {
                out(".plus(");
                final Expression expr = exprs.get(i);
                expr.visit(this);
                if (expr.getTypeModel() == null || !"ceylon.language::String".equals(
                        expr.getTypeModel().getProducedTypeQualifiedName())) {
                    out(".string");
                }
                out(").plus(");
            }
        }
    }

    @Override
    public void visit(final Tree.FloatLiteral that) {
        out(getClAlias(), "Float(", that.getText(), ")");
    }

    long parseNaturalLiteral(Tree.NaturalLiteral that, boolean neg) throws NumberFormatException {
        String nt = that.getText();
        char prefix = nt.charAt(0);
        int radix = 10;
        if (prefix == '$' || prefix == '#') {
            radix = prefix == '$' ? 2 : 16;
            nt = nt.substring(1);
        }
        BigInteger lit = new java.math.BigInteger(nt, radix);
        if (neg) {
            lit = lit.negate();
        }
        if (radix == 10) {
            if (lit.compareTo(maxLong) > 0 || lit.compareTo(minLong) < 0) {
                that.addError("literal outside representable range: " + lit
                        + " is too large to be represented as an Integer");
                return 0;
            }
        } else {
            if ((neg?lit.negate():lit).compareTo(maxUnsignedLong) == 0) {
                return neg?1:-1;
            } else if ((neg?lit.negate():lit).compareTo(maxUnsignedLong) > 0) {
                that.addError("invalid hexadecimal literal: '" + (radix==2?"$":"#") + nt + "' has more than 64 bits");
                return 0;
            }
        }
        return lit.longValue();
    }

    @Override
    public void visit(final Tree.NaturalLiteral that) {
        try {
            out("(", Long.toString(parseNaturalLiteral(that, false)), ")");
        } catch (NumberFormatException ex) {
            that.addError("Invalid numeric literal " + that.getText());
        }
    }

    @Override
    public void visit(final Tree.This that) {
        out(names.self(Util.getContainingClassOrInterface(that.getScope())));
    }

    @Override
    public void visit(final Tree.Super that) {
        out(names.self(Util.getContainingClassOrInterface(that.getScope())));
    }

    @Override
    public void visit(final Tree.Outer that) {
        boolean outer = false;
        if (opts.isOptimize()) {
            Scope scope = that.getScope();
            while ((scope != null) && !(scope instanceof TypeDeclaration)) {
                scope = scope.getContainer();
            }
            if (scope != null && ((TypeDeclaration)scope).isClassOrInterfaceMember()) {
                out(names.self((TypeDeclaration) scope), ".");
                outer = true;
            }
        }
        if (outer) {
            out("outer$");
        } else {
            out(names.self(that.getTypeModel().getDeclaration()));
        }
    }

    @Override
    public void visit(final Tree.BaseMemberExpression that) {
        BmeGenerator.generateBme(that, this, false);
    }

    /** Tells whether a declaration can be accessed directly (using its name) or
     * through its getter. */
    boolean accessDirectly(Declaration d) {
        return !accessThroughGetter(d) || directAccess.contains(d) || d.isParameter();
    }

    private boolean accessThroughGetter(Declaration d) {
        return (d instanceof MethodOrValue) && !(d instanceof Method)
                && !defineAsProperty(d);
    }
    
    private boolean defineAsProperty(Declaration d) {
        return AttributeGenerator.defineAsProperty(d);
    }

    /** Returns true if the top-level declaration for the term is annotated "nativejs" */
    static boolean isNativeJs(final Tree.Term t) {
        if (t instanceof MemberOrTypeExpression) {
            return isNativeJs(((MemberOrTypeExpression)t).getDeclaration());
        }
        return false;
    }

    /** Returns true if the declaration is annotated "nativejs" */
    static boolean isNativeJs(Declaration d) {
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
            for(com.redhat.ceylon.model.typechecker.model.Annotation annotation : d.getAnnotations()){
                if(annotation.getName().equals(name))
                    return true;
            }
        }
        return false;
    }

    private void generateSafeOp(final Tree.QualifiedMemberOrTypeExpression that) {
        boolean isMethod = that.getDeclaration() instanceof Method;
        String lhsVar = createRetainedTempVar();
        out("(", lhsVar, "=");
        super.visit(that);
        out(",");
        if (isMethod) {
            out(getClAlias(), "JsCallable(", lhsVar, ",");
        }
        out(getClAlias(),"nn$(", lhsVar, ")?");
        if (isMethod && !((Method)that.getDeclaration()).getTypeParameters().isEmpty()) {
            //Method ref with type parameters
            BmeGenerator.printGenericMethodReference(this, that, lhsVar, memberAccess(that, lhsVar));
        } else {
            out(memberAccess(that, lhsVar));
        }
        out(":null)");
        if (isMethod) {
            out(")");
        }
    }

    void supervisit(final Tree.QualifiedMemberOrTypeExpression that) {
        super.visit(that);
    }

    @Override
    public void visit(final Tree.QualifiedMemberExpression that) {
        if (errVisitor.hasErrors(that))return;
        //Big TODO: make sure the member is actually
        //          refined by the current class!
        if (that.getMemberOperator() instanceof SafeMemberOp) {
            generateSafeOp(that);
        } else if (that.getMemberOperator() instanceof SpreadOp) {
            SequenceGenerator.generateSpread(that, this);
        } else if (that.getDeclaration() instanceof Method && that.getSignature() == null) {
            //TODO right now this causes that all method invocations are done this way
            //we need to filter somehow to only use this pattern when the result is supposed to be a callable
            //looks like checking for signature is a good way (not THE way though; named arg calls don't have signature)
            generateCallable(that, null);
        } else if (that.getStaticMethodReference() && that.getDeclaration()!=null) {
            out("function($O$){return ");
            if (that.getDeclaration() instanceof Method) {
                if (BmeGenerator.hasTypeParameters(that)) {
                    BmeGenerator.printGenericMethodReference(this, that, "$O$", "$O$."+names.name(that.getDeclaration()));
                } else {
                    out(getClAlias(), "JsCallable($O$,$O$.", names.name(that.getDeclaration()), ")");
                }
                out(";}");
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

    void generateCallable(final Tree.QualifiedMemberOrTypeExpression that, String name) {
        final Declaration d = that.getDeclaration();
        if (that.getPrimary() instanceof Tree.BaseTypeExpression) {
            //it's a static method ref
            if (name == null) {
                name = memberAccess(that, "");
            }
            if (d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                qualify(that, d);
                out(names.name(d));
            } else {
                out("function(x){return ");
                if (BmeGenerator.hasTypeParameters(that)) {
                    BmeGenerator.printGenericMethodReference(this, that, "x", "x."+name);
                } else {
                    out(getClAlias(), "JsCallable(x,x.", name, ")");
                }
                out(";}");
            }
            return;
        }
        if (d.isToplevel() && d instanceof Method) {
            //Just output the name
            out(names.name(d));
            return;
        }
        String primaryVar = createRetainedTempVar();
        out("(", primaryVar, "=");
        that.getPrimary().visit(this);
        out(",");
        final String member = (name == null) ? memberAccess(that, primaryVar) : (primaryVar+"."+name);
        if (that.getDeclaration() instanceof Method
                && !((Method)that.getDeclaration()).getTypeParameters().isEmpty()) {
            //Method ref with type parameters
            BmeGenerator.printGenericMethodReference(this, that, primaryVar, member);
        } else {
            out(getClAlias(), "JsCallable(", primaryVar, ",", getClAlias(), "nn$(", primaryVar, ")?", member, ":null)");
        }
        out(")");
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

    String getMember(Node node, Declaration decl, String lhs) {
        final StringBuilder sb = new StringBuilder();
        if (lhs != null) {
            if (lhs.length() > 0) {
                sb.append(lhs);
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
            }
        }
        return sb.toString();
    }

    String memberAccessBase(Node node, Declaration decl, boolean setter,
                String lhs) {
        final StringBuilder sb = new StringBuilder(getMember(node, decl, lhs));
        final boolean isConstructor = decl instanceof com.redhat.ceylon.model.typechecker.model.Constructor;
        if (sb.length() > 0) {
            if (node instanceof BaseMemberOrTypeExpression) {
                Declaration bmd = ((BaseMemberOrTypeExpression)node).getDeclaration();
                if (bmd.isParameter() && bmd.getContainer() instanceof ClassAlias) {
                    return sb.toString();
                }
            }
            sb.append(isConstructor ? '_':'.');
        }
        boolean metaGetter = false;
        Scope scope = getSuperMemberScope(node);
        if (opts.isOptimize() && (scope != null) && !isConstructor) {
            sb.append("getT$all()['");
            if (scope instanceof Declaration) {
                sb.append(TypeUtils.qualifiedNameSkippingMethods((Declaration)scope));
            } else {
                sb.append(scope.getQualifiedNameString());
            }
            sb.append("']");
            if (defineAsProperty(decl)) {
                return getClAlias() + (setter ? "attrSetter(" : "attrGetter(")
                        + sb.toString() + ",'" + names.name(decl) + "')";
            }
            sb.append(".$$.prototype.");
            metaGetter = true;
        }
        final String member = (accessThroughGetter(decl) && !accessDirectly(decl))
                ? (setter ? names.setter(decl) : names.getter(decl, metaGetter)) : names.name(decl);
        sb.append(member);
        if (!opts.isOptimize() && (scope != null)) {
            sb.append(names.scopeSuffix(scope));
        }
        return sb.toString();
    }

    /**
     * Returns a string representing a read access to a member, as represented by
     * the given expression. If lhs==null and the expression is a BaseMemberExpression
     * then the qualified path is prepended.
     */
    String memberAccess(final Tree.StaticMemberOrTypeExpression expr, String lhs) {
        Declaration decl = expr.getDeclaration();
        String plainName = null;
        if (decl == null && dynblock > 0) {
            plainName = expr.getIdentifier().getText();
        }
        else if (isNativeJs(decl)) {
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
    
    @Override
    public void visit(final Tree.BaseTypeExpression that) {
        Declaration d = that.getDeclaration();
        if (d == null && isInDynamicBlock()) {
            //It's a native js type but will be wrapped in dyntype() call
            String id = that.getIdentifier().getText();
            out("(typeof ", id, "==='undefined'?");
            generateThrow(null, "Undefined type " + id, that);
            out(":", id, ")");
        } else {
            boolean wrap = false;
            String pname = null;
            List<com.redhat.ceylon.model.typechecker.model.Parameter> params = null;
            TypeDeclaration td = null;
            if (!that.getDirectlyInvoked() && d instanceof TypeDeclaration) {
                td = (TypeDeclaration)d;
                if (td.getTypeParameters() != null && td.getTypeParameters().size() > 0) {
                    wrap = true;
                    pname = names.createTempVariable();
                    out("function(");
                    if (td instanceof com.redhat.ceylon.model.typechecker.model.Class) {
                        params = ((com.redhat.ceylon.model.typechecker.model.Class)td).getParameterList().getParameters();
                    } else if (td instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                        params = ((com.redhat.ceylon.model.typechecker.model.Constructor)td).getParameterLists().get(0).getParameters();
                    }
                    for (int i=0;i<params.size(); i++) {
                        if (i>0)out(",");
                        out(pname, "$", Integer.toString(i));
                    }
                    out("){return ");
                }
            }
            if (d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                //This is an ugly-ass hack for when the typechecker incorrectly reports
                //the declaration as the constructor instead of the class;
                //this happens with classes that have a default constructor with the same name as the type
                if (names.name(d).equals(names.name((TypeDeclaration)d.getContainer()))) {
                    qualify(that, (TypeDeclaration)d.getContainer());
                } else {
                    qualify(that, d);
                }
            } else {
                qualify(that, d);
            }
            out(names.name(d));
            if (wrap) {
                out("(");
                for (int i=0;i<params.size(); i++) {
                    out(pname, "$", Integer.toString(i), ",");
                }
                List<ProducedType> targs = that.getTypeArguments() == null ? null :
                    that.getTypeArguments().getTypeModels();
                TypeUtils.printTypeArguments(that, TypeUtils.matchTypeParametersWithArguments(
                        td.getTypeParameters(), targs), this, false, null);
                out(");}");
            }
        }
    }

    @Override
    public void visit(final Tree.QualifiedTypeExpression that) {
        BmeGenerator.generateQte(that, this, false);
    }

    public void visit(final Tree.Dynamic that) {
        invoker.nativeObject(that.getNamedArgumentList());
    }

    @Override
    public void visit(final Tree.InvocationExpression that) {
        invoker.generateInvocation(that);
    }

    @Override
    public void visit(final Tree.PositionalArgumentList that) {
        invoker.generatePositionalArguments(null, that, that.getPositionalArguments(), false, false);
    }

    /** Box a term, visit it, unbox it. */
    void box(final Tree.Term term) {
        final int t = boxStart(term);
        term.visit(this);
        if (t == 4) {
            final ProducedType ct = term.getTypeModel();
            out(",");
            TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(term, ct, this);
            out(",");
            TypeUtils.printTypeArguments(term, ct.getTypeArguments(), this, true, null);
        }
        boxUnboxEnd(t);
    }

    // Make sure fromTerm is compatible with toTerm by boxing it when necessary
    int boxStart(final Tree.Term fromTerm) {
        return boxUnboxStart(fromTerm, false);
    }

    // Make sure fromTerm is compatible with toTerm by boxing or unboxing it when necessary
    int boxUnboxStart(final Tree.Term fromTerm, final Tree.Term toTerm) {
        return boxUnboxStart(fromTerm, isNativeJs(toTerm));
    }

    // Make sure fromTerm is compatible with toDecl by boxing or unboxing it when necessary
    int boxUnboxStart(final Tree.Term fromTerm, com.redhat.ceylon.model.typechecker.model.TypedDeclaration toDecl) {
        return boxUnboxStart(fromTerm, isNativeJs(toDecl));
    }

    int boxUnboxStart(final Tree.Term fromTerm, boolean toNative) {
        // Box the value
        final boolean fromNative = isNativeJs(fromTerm);
        final ProducedType fromType = fromTerm.getTypeModel();
        final String fromTypeName = Util.isTypeUnknown(fromType) ? "UNKNOWN" : fromType.getProducedTypeQualifiedName();
        if (fromNative != toNative || fromTypeName.startsWith("ceylon.language::Callable<")) {
            if (fromNative) {
                // conversion from native value to Ceylon value
                if (fromTypeName.equals("ceylon.language::Integer")) {
                    out("(");
                } else if (fromTypeName.equals("ceylon.language::Float")) {
                    out(getClAlias(), "Float(");
                } else if (fromTypeName.equals("ceylon.language::Boolean")) {
                    out("(");
                } else if (fromTypeName.equals("ceylon.language::Character")) {
                    out(getClAlias(), "Character(");
                } else if (fromTypeName.startsWith("ceylon.language::Callable<")) {
                    out(getClAlias(), "$JsCallable(");
                    return 4;
                } else {
                    return 0;
                }
                return 1;
            } else if ("ceylon.language::Float".equals(fromTypeName)) {
                // conversion from Ceylon Float to native value
                return 2;
            } else if (fromTypeName.startsWith("ceylon.language::Callable<")) {
                Term _t = fromTerm;
                if (_t instanceof Tree.InvocationExpression) {
                    _t = ((Tree.InvocationExpression)_t).getPrimary();
                }
                //Don't box callables if they're not members or anonymous
                if (_t instanceof Tree.MemberOrTypeExpression) {
                    final Declaration d = ((Tree.MemberOrTypeExpression)_t).getDeclaration();
                    if (d != null && !(d.isClassOrInterfaceMember() || d.isAnonymous())) {
                        return 0;
                    }
                }
                out(getClAlias(), "$JsCallable(");
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
    public void visit(final Tree.ObjectArgument that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.objectArgument(that, this);
    }

    @Override
    public void visit(final Tree.AttributeArgument that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.attributeArgument(that, this);
    }

    @Override
    public void visit(final Tree.SequencedArgument that) {
        if (errVisitor.hasErrors(that))return;
        SequenceGenerator.sequencedArgument(that, this);
    }

    @Override
    public void visit(final Tree.SequenceEnumeration that) {
        if (errVisitor.hasErrors(that))return;
        SequenceGenerator.sequenceEnumeration(that, this);
    }


    @Override
    public void visit(final Tree.Comprehension that) {
        new ComprehensionGenerator(this, names, directAccess).generateComprehension(that);
    }


    @Override
    public void visit(final Tree.SpecifierStatement that) {
        // A lazy specifier expression in a class/interface should go into the
        // prototype in prototype style, so don't generate them here.
        if (!(opts.isOptimize() && (that.getSpecifierExpression() instanceof LazySpecifierExpression)
                && (that.getScope().getContainer() instanceof TypeDeclaration))) {
            specifierStatement(null, that);
        }
    }
    
    private void specifierStatement(final TypeDeclaration outer,
            final Tree.SpecifierStatement specStmt) {
        final Tree.Expression expr = specStmt.getSpecifierExpression().getExpression();
        final Tree.Term term = specStmt.getBaseMemberExpression();
        final Tree.StaticMemberOrTypeExpression smte = term instanceof Tree.StaticMemberOrTypeExpression
                ? (Tree.StaticMemberOrTypeExpression)term : null;
        if (dynblock > 0 && Util.isTypeUnknown(term.getTypeModel())) {
            if (smte != null && smte.getDeclaration() == null) {
                out(smte.getIdentifier().getText());
            } else {
                term.visit(this);
            }
            out("=");
            int box = boxUnboxStart(expr, term);
            expr.visit(this);
            if (box == 4) out("/*TODO: callable targs 6*/");
            boxUnboxEnd(box);
            out(";");
            return;
        }
        if (smte != null) {
            Declaration bmeDecl = smte.getDeclaration();
            if (specStmt.getSpecifierExpression() instanceof LazySpecifierExpression) {
                // attr => expr;
                final boolean property = defineAsProperty(bmeDecl);
                if (property) {
                    defineAttribute(qualifiedPath(specStmt, bmeDecl), names.name(bmeDecl));
                } else  {
                    if (bmeDecl.isMember()) {
                        qualify(specStmt, bmeDecl);
                    } else {
                        out ("var ");
                    }
                    out(names.getter(bmeDecl, false), "=function()");
                }
                beginBlock();
                if (outer != null) { initSelf(specStmt); }
                out ("return ");
                if (!isNaturalLiteral(specStmt.getSpecifierExpression().getExpression().getTerm())) {
                    specStmt.getSpecifierExpression().visit(this);
                }
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
                //since #451 we now generate an attribute here
                if (bmeDecl.isMember() && (bmeDecl instanceof Value) && bmeDecl.isActual()) {
                    Value vdec = (Value)bmeDecl;
                    final String atname = vdec.isShared() ? names.name(vdec)+"_" : names.privateName(vdec);
                    defineAttribute(names.self(outer), names.name(vdec));
                    out("{");
                    if (vdec.isLate()) {
                        generateUnitializedAttributeReadCheck("this."+atname, names.name(vdec));
                    }
                    out("return this.", atname, ";}");
                    if (vdec.isVariable() || vdec.isLate()) {
                        final String par = getNames().createTempVariable();
                        out(",function(", par, "){");
                        generateImmutableAttributeReassignmentCheck(vdec, "this."+atname, names.name(vdec));
                        out("return this.", atname, "=", par, ";}");
                    } else {
                        out(",undefined");
                    }
                    out(",");
                    TypeUtils.encodeForRuntime(expr, vdec, this);
                    out(")");
                    endLine(true);
                }
            }
            else if (bmeDecl instanceof MethodOrValue) {
                // "attr = expr;" in an initializer or method
                final MethodOrValue moval = (MethodOrValue)bmeDecl;
                if (moval.isVariable()) {
                    // simple assignment to a variable attribute
                    BmeGenerator.generateMemberAccess(smte, new GenerateCallback() {
                        @Override public void generateValue() {
                            int boxType = boxUnboxStart(expr.getTerm(), moval);
                            if (dynblock > 0 && !Util.isTypeUnknown(moval.getType())
                                    && Util.isTypeUnknown(expr.getTypeModel())) {
                                TypeUtils.generateDynamicCheck(expr, moval.getType(), GenerateJsVisitor.this, false,
                                        expr.getTypeModel().getTypeArguments());
                            } else {
                                expr.visit(GenerateJsVisitor.this);
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
                                    ProducedType ps = moval.getType().getTypeArgumentList().get(1);
                                    TypeDeclaration pd = ps.getDeclaration();
                                    if (moval.getUnit().getEmptyDeclaration().equals(pd)) {
                                        out("[],");
                                    } else {
                                        out("[/*VALUE Callable params ", pd+"*/],");
                                    }
                                }
                                TypeUtils.printTypeArguments(expr, expr.getTypeModel().getTypeArguments(),
                                        GenerateJsVisitor.this, false, expr.getTypeModel().getVarianceOverrides());
                            }
                            boxUnboxEnd(boxType);
                        }
                    }, qualifiedPath(smte, moval), this);
                    out(";");
                } else if (moval.isMember()) {
                    if (moval instanceof Method) {
                        //same as fat arrow
                        qualify(specStmt, bmeDecl);
                        out(names.name(moval), "=function ", names.name(moval), "(");
                        //Build the parameter list, we'll use it several times
                        final StringBuilder paramNames = new StringBuilder();
                        final List<com.redhat.ceylon.model.typechecker.model.Parameter> params =
                                ((Method) moval).getParameterLists().get(0).getParameters();
                        for (com.redhat.ceylon.model.typechecker.model.Parameter p : params) {
                            if (paramNames.length() > 0) paramNames.append(",");
                            paramNames.append(names.name(p));
                        }
                        out(paramNames.toString());
                        out("){");
                        for (com.redhat.ceylon.model.typechecker.model.Parameter p : params) {
                            if (p.isDefaulted()) {
                                out("if(", names.name(p), "===undefined)", names.name(p),"=");
                                qualify(specStmt, moval);
                                out(names.name(moval), "$defs$", p.getName(), "(", paramNames.toString(), ")");
                                endLine(true);
                            }
                        }
                        out("return ");
                        if (!isNaturalLiteral(specStmt.getSpecifierExpression().getExpression().getTerm())) {
                            specStmt.getSpecifierExpression().visit(this);
                        }
                        out("(", paramNames.toString(), ");}");
                        endLine(true);
                    } else {
                        // Specifier for a member attribute. This actually defines the
                        // member (e.g. in shortcut refinement syntax the attribute
                        // declaration itself can be omitted), so generate the attribute.
                        if (opts.isOptimize()) {
                            //#451
                            out(names.self(Util.getContainingClassOrInterface(moval.getScope())), ".",
                                    names.name(moval));
                            if (!(moval.isVariable() || moval.isLate())) {
                                    out("_");
                            }
                            out("=");
                            specStmt.getSpecifierExpression().visit(this);
                            endLine(true);
                        } else {
                            AttributeGenerator.generateAttributeGetter(null, moval,
                                    specStmt.getSpecifierExpression(), null, this, directAccess);
                        }
                    }
                } else {
                    // Specifier for some other attribute, or for a method.
                    if (opts.isOptimize() 
                            || (bmeDecl.isMember() && (bmeDecl instanceof Method))) {
                        qualify(specStmt, bmeDecl);
                    }
                    out(names.name(bmeDecl), "=");
                    if (dynblock > 0 && Util.isTypeUnknown(expr.getTypeModel())
                            && !Util.isTypeUnknown(((MethodOrValue) bmeDecl).getType())) {
                        TypeUtils.generateDynamicCheck(expr, ((MethodOrValue) bmeDecl).getType(), this, false,
                                expr.getTypeModel().getTypeArguments());
                    } else {
                        specStmt.getSpecifierExpression().visit(this);
                    }
                    out(";");
                }
            }
        }
        else if ((term instanceof ParameterizedExpression)
                && (specStmt.getSpecifierExpression() != null)) {
            final ParameterizedExpression paramExpr = (ParameterizedExpression)term;
            if (paramExpr.getPrimary() instanceof BaseMemberExpression) {
                // func(params) => expr;
                final BaseMemberExpression bme2 = (BaseMemberExpression) paramExpr.getPrimary();
                final Declaration bmeDecl = bme2.getDeclaration();
                if (bmeDecl.isMember()) {
                    qualify(specStmt, bmeDecl);
                } else {
                    out("var ");
                }
                out(names.name(bmeDecl), "=");
                FunctionHelper.singleExprFunction(paramExpr.getParameterLists(), expr,
                        bmeDecl instanceof Scope ? (Scope)bmeDecl : null, true, true, this);
                out(";");
            }
        }
    }
    
    private void addSpecifierToPrototype(final TypeDeclaration outer,
                final Tree.SpecifierStatement specStmt) {
        specifierStatement(outer, specStmt);
    }

    @Override
    public void visit(final AssignOp that) {
        String returnValue = null;
        StaticMemberOrTypeExpression lhsExpr = null;
        
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
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
        
        BmeGenerator.generateMemberAccess(lhsExpr, new GenerateCallback() {
            @Override public void generateValue() {
                if (!isNaturalLiteral(that.getRightTerm())) {
                    int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
                    that.getRightTerm().visit(GenerateJsVisitor.this);
                    if (boxType == 4) out("/*TODO: callable targs 7*/");
                    boxUnboxEnd(boxType);
                }
            }
        }, null, this);
        
        if (returnValue != null) { out(",", returnValue); }
        out(")");
    }

    /** Outputs the module name for the specified declaration. Returns true if something was output. */
    public boolean qualify(final Node that, final Declaration d) {
        String path = qualifiedPath(that, d);
        if (path.length() > 0) {
            out(path, d instanceof com.redhat.ceylon.model.typechecker.model.Constructor ? "_" : ".");
        }
        return path.length() > 0;
    }

    private String qualifiedPath(final Node that, final Declaration d) {
        return qualifiedPath(that, d, false);
    }

    public String qualifiedPath(final Node that, final Declaration d, final boolean inProto) {
        final boolean isMember = d.isClassOrInterfaceMember();
        final boolean imported = isImported(that == null ? null : that.getUnit().getPackage(), d);
        if (!isMember && imported) {
            return names.moduleAlias(d.getUnit().getPackage().getModule());
        }
        else if (opts.isOptimize() && !inProto) {
            if (isMember && !(d.isParameter() && !d.isCaptured())) {
                TypeDeclaration id = that.getScope().getInheritingDeclaration(d);
                if (id == null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    id = (TypeDeclaration) d.getContainer();
                }
                Scope scope = that.getScope();
                if ((scope != null) && (that instanceof Tree.ClassDeclaration
                        || that instanceof Tree.InterfaceDeclaration || that instanceof Tree.Constructor)) {
                    // class/interface aliases have no own "this"
                    scope = scope.getContainer();
                }
                final StringBuilder path = new StringBuilder();
                final Declaration innermostDeclaration = Util.getContainingDeclarationOfScope(scope);
                while (scope != null) {
                    if (scope instanceof com.redhat.ceylon.model.typechecker.model.Constructor
                            && scope == innermostDeclaration) {
                        if (that instanceof BaseTypeExpression) {
                            path.append(names.name((TypeDeclaration)scope.getContainer()));
                        } else {
                            path.append(names.self((TypeDeclaration)scope.getContainer()));
                        }
                        if (scope == id) {
                            break;
                        }
                        scope = scope.getContainer();
                    } else if (scope instanceof TypeDeclaration) {
                        if (path.length() > 0) {
                            path.append(".outer$");
                        } else if (d instanceof com.redhat.ceylon.model.typechecker.model.Constructor
                                && Util.getContainingDeclaration(d) == scope) {
                            if (!d.getName().equals(((TypeDeclaration)scope).getName())) {
                                path.append(names.name((TypeDeclaration) scope));
                            }
                        } else {
                            path.append(names.self((TypeDeclaration) scope));
                        }
                    } else {
                        path.setLength(0);
                    }
                    if (scope == id) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                if (id != null && path.length() == 0 && id.isToplevel() && !Util.contains(id, that.getScope())) {
                    //Import of toplevel object or constructor
                    if (imported) {
                        path.append(names.moduleAlias(id.getUnit().getPackage().getModule())).append('.');
                    }
                    path.append(id.isAnonymous() ? names.objectName(id) : names.name(id));
                }
                return path.toString();
            }
        }
        else if (d != null) {
            if (isMember && (d.isShared() || inProto || (!d.isParameter() && defineAsProperty(d)))) {
                TypeDeclaration id = d instanceof TypeAlias ? (TypeDeclaration)d :
                    that.getScope().getInheritingDeclaration(d);
                if (id==null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    id = (TypeDeclaration) d.getContainer();
                    if (id.isToplevel() && !Util.contains(id, that.getScope())) {
                        //Import of toplevel object or constructor
                        final StringBuilder sb = new StringBuilder();
                        if (imported) {
                            sb.append(names.moduleAlias(id.getUnit().getPackage().getModule())).append('.');
                        }
                        sb.append(id.isAnonymous() ? names.objectName(id) : names.name(id));
                        return sb.toString();
                    } else if (d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                        return names.name(id);
                    } else {
                        //a shared local declaration
                        return names.self(id);
                    }
                } else {
                    //an inherited declaration that might be
                    //inherited by an outer scope
                    return names.self(id);
                }
            }
        }
        return "";
    }

    /** Tells whether a declaration is in the specified package. */
    public boolean isImported(final Package p2, final Declaration d) {
        if (d == null) {
            return false;
        }
        Package p1 = d.getUnit().getPackage();
        if (p2 == null)return p1 != null;
        if (p1.getModule()== null)return p2.getModule()!=null;
        return !p1.getModule().equals(p2.getModule());
    }

    @Override
    public void visit(final Tree.ExecutableStatement that) {
        super.visit(that);
        endLine(true);
    }

    /** Creates a new temporary variable which can be used immediately, even
     * inside an expression. The declaration for that temporary variable will be
     * emitted after the current Ceylon statement has been completely processed.
     * The resulting code is valid because JavaScript variables may be used before
     * they are declared. */
    String createRetainedTempVar() {
        String varName = names.createTempVariable();
        retainedVars.add(varName);
        return varName;
    }

    @Override
    public void visit(final Tree.Return that) {
        if (that.getExpression() == null) {
            out("return;");
            endLine();
            return;
        }
        out("return ");
        if (dynblock > 0 && Util.isTypeUnknown(that.getExpression().getTypeModel())) {
            Scope cont = Util.getRealScope(that.getScope()).getScope();
            if (cont instanceof Declaration) {
                final ProducedType dectype = ((Declaration)cont).getReference().getType();
                if (!Util.isTypeUnknown(dectype)) {
                    TypeUtils.generateDynamicCheck(that.getExpression(), dectype, this, false,
                            that.getExpression().getTypeModel().getTypeArguments());
                    endLine(true);
                    return;
                }
            }
        }
        if (isNaturalLiteral(that.getExpression().getTerm())) {
            out(";");
        } else {
            super.visit(that);
        }
    }

    @Override
    public void visit(final Tree.AnnotationList that) {}

    boolean outerSelf(Declaration d) {
        if (d.isToplevel()) {
            out("ex$");
            return true;
        }
        else if (d.isClassOrInterfaceMember()) {
            out(names.self((TypeDeclaration)d.getContainer()));
            return true;
        }
        return false;
    }

    @Override
    public void visit(final Tree.SumOp that) {
        Operators.simpleBinaryOp(that, null, ".plus(", ")", this);
    }

    @Override
    public void visit(final Tree.ScaleOp that) {
        final String lhs = names.createTempVariable();
        Operators.simpleBinaryOp(that, "function(){var "+lhs+"=", ";return ", ".scale("+lhs+");}()", this);
    }

    @Override
    public void visit(final Tree.DifferenceOp that) {
        Operators.simpleBinaryOp(that, null, ".minus(", ")", this);
    }

    @Override
    public void visit(final Tree.ProductOp that) {
        Operators.simpleBinaryOp(that, null, ".times(", ")", this);
    }

    @Override
    public void visit(final Tree.QuotientOp that) {
        Operators.simpleBinaryOp(that, null, ".divided(", ")", this);
    }

    @Override public void visit(final Tree.RemainderOp that) {
        Operators.simpleBinaryOp(that, null, ".remainder(", ")", this);
    }

    @Override public void visit(final Tree.PowerOp that) {
        Operators.simpleBinaryOp(that, null, ".power(", ")", this);
    }

    @Override public void visit(final Tree.AddAssignOp that) {
        assignOp(that, "plus", null);
    }

    @Override public void visit(final Tree.SubtractAssignOp that) {
        assignOp(that, "minus", null);
    }

    @Override public void visit(final Tree.MultiplyAssignOp that) {
        assignOp(that, "times", null);
    }

    @Override public void visit(final Tree.DivideAssignOp that) {
        assignOp(that, "divided", null);
    }

    @Override public void visit(final Tree.RemainderAssignOp that) {
        assignOp(that, "remainder", null);
    }

    public void visit(Tree.ComplementAssignOp that) {
        assignOp(that, "complement", TypeUtils.mapTypeArgument(that, "complement", "Element", "Other"));
    }
    public void visit(Tree.UnionAssignOp that) {
        assignOp(that, "union", TypeUtils.mapTypeArgument(that, "union", "Element", "Other"));
    }
    public void visit(Tree.IntersectAssignOp that) {
        assignOp(that, "intersection", TypeUtils.mapTypeArgument(that, "intersection", "Element", "Other"));
    }

    public void visit(Tree.AndAssignOp that) {
        assignOp(that, "&&", null);
    }
    public void visit(Tree.OrAssignOp that) {
        assignOp(that, "||", null);
    }

    private void assignOp(final Tree.AssignmentOp that, final String functionName,
            final Map<TypeParameter, ProducedType> targs) {
        Term lhs = that.getLeftTerm();
        final boolean isNative="||".equals(functionName)||"&&".equals(functionName);
        if (lhs instanceof BaseMemberExpression) {
            BaseMemberExpression lhsBME = (BaseMemberExpression) lhs;
            Declaration lhsDecl = lhsBME.getDeclaration();

            final String getLHS = memberAccess(lhsBME, null);
            out("(");
            BmeGenerator.generateMemberAccess(lhsBME, new GenerateCallback() {
                @Override public void generateValue() {
                    if (isNative) {
                        out(getLHS, functionName);
                    } else {
                        out(getLHS, ".", functionName, "(");
                    }
                    if (!isNaturalLiteral(that.getRightTerm())) {
                        that.getRightTerm().visit(GenerateJsVisitor.this);
                    }
                    if (!isNative) {
                        if (targs != null) {
                            out(",");
                            TypeUtils.printTypeArguments(that, targs, GenerateJsVisitor.this, false, null);
                        }
                        out(")");
                    }
                }
            }, null, this);
            if (!hasSimpleGetterSetter(lhsDecl)) { out(",", getLHS); }
            out(")");

        } else if (lhs instanceof QualifiedMemberExpression) {
            QualifiedMemberExpression lhsQME = (QualifiedMemberExpression) lhs;
            if (isNativeJs(lhsQME)) {
                // ($1.foo = Box($1.foo).operator($2))
                final String tmp = names.createTempVariable();
                final String dec = isInDynamicBlock() && lhsQME.getDeclaration() == null ?
                        lhsQME.getIdentifier().getText() : lhsQME.getDeclaration().getName();
                out("(", tmp, "=");
                lhsQME.getPrimary().visit(this);
                out(",", tmp, ".", dec, "=");
                int boxType = boxStart(lhsQME);
                out(tmp, ".", dec);
                if (boxType == 4) out("/*TODO: callable targs 8*/");
                boxUnboxEnd(boxType);
                out(".", functionName, "(");
                if (!isNaturalLiteral(that.getRightTerm())) {
                    that.getRightTerm().visit(this);
                }
                out("))");
                
            } else {
                final String lhsPrimaryVar = createRetainedTempVar();
                final String getLHS = memberAccess(lhsQME, lhsPrimaryVar);
                out("(", lhsPrimaryVar, "=");
                lhsQME.getPrimary().visit(this);
                out(",");
                BmeGenerator.generateMemberAccess(lhsQME, new GenerateCallback() {
                    @Override public void generateValue() {
                        out(getLHS, ".", functionName, "(");
                        if (!isNaturalLiteral(that.getRightTerm())) {
                            that.getRightTerm().visit(GenerateJsVisitor.this);
                        }
                        out(")");
                    }
                }, lhsPrimaryVar, this);
                
                if (!hasSimpleGetterSetter(lhsQME.getDeclaration())) {
                    out(",", getLHS);
                }
                out(")");
            }
        }
    }

    @Override public void visit(final Tree.NegativeOp that) {
        Tree.Term term = that.getTerm();
        if (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        if (term instanceof Tree.NaturalLiteral) {
            long t = parseNaturalLiteral((Tree.NaturalLiteral)term, true);
            out("(", Long.toString(t), ")");
            if (t == 0) {
                //Force -0
                out(".negated");
            }
            return;
        }
        final TypeDeclaration d = term.getTypeModel().getDeclaration();
        final boolean isint = d.inherits(that.getUnit().getIntegerDeclaration());
        Operators.unaryOp(that, isint?"(-":null, isint?")":".negated", this);
    }

    @Override public void visit(final Tree.PositiveOp that) {
        final TypeDeclaration d = that.getTerm().getTypeModel().getDeclaration();
        final boolean nat = d.inherits(that.getUnit().getIntegerDeclaration());
        //TODO if it's positive we leave it as is?
        Operators.unaryOp(that, nat?"(+":null, nat?")":null, this);
    }

    @Override public void visit(final Tree.EqualOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use equals() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".equals&&", ltmp, ".equals(", rtmp, "))||", ltmp, "===", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            Operators.simpleBinaryOp(that, usenat?"((":null, usenat?").valueOf()==(":".equals(",
                    usenat?").valueOf())":")", this);
        }
    }

    @Override public void visit(final Tree.NotEqualOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use equals() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".equals&&!", ltmp, ".equals(", rtmp, "))||", ltmp, "!==", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            Operators.simpleBinaryOp(that, usenat?"!(":"(!", usenat?"==":".equals(", usenat?")":"))", this);
        }
    }

    @Override public void visit(final Tree.NotOp that) {
        final Term t = that.getTerm();
        final boolean omitParens = t instanceof BaseMemberExpression
                || t instanceof QualifiedMemberExpression
                || t instanceof IsOp || t instanceof Exists || t instanceof IdenticalOp
                || t instanceof InOp || t instanceof Nonempty
                || (t instanceof InvocationExpression && ((InvocationExpression)t).getNamedArgumentList() == null);
        if (omitParens) {
            Operators.unaryOp(that, "!", null, this);
        } else {
            Operators.unaryOp(that, "(!", ")", this);
        }
    }

    @Override public void visit(final Tree.IdenticalOp that) {
        Operators.simpleBinaryOp(that, "(", "===", ")", this);
    }

    @Override public void visit(final Tree.CompareOp that) {
        Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
    }

    /** Returns true if both Terms' types is either Integer or Boolean. */
    private boolean canUseNativeComparator(final Tree.Term left, final Tree.Term right) {
        if (left == null || right == null || left.getTypeModel() == null || right.getTypeModel() == null) {
            return false;
        }
        final ProducedType lt = left.getTypeModel();
        final ProducedType rt = right.getTypeModel();
        final TypeDeclaration intdecl = left.getUnit().getIntegerDeclaration();
        final TypeDeclaration booldecl = left.getUnit().getBooleanDeclaration();
        return (intdecl.equals(lt.getDeclaration()) && intdecl.equals(rt.getDeclaration()))
                || (booldecl.equals(lt.getDeclaration()) && booldecl.equals(rt.getDeclaration()));
    }

    @Override public void visit(final Tree.SmallerOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ").equals(",
                    getClAlias(), "smaller()))||", ltmp, "<", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                Operators.simpleBinaryOp(that, "(", "<", ")", this);
            } else {
                Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
                out(".equals(", getClAlias(), "smaller())");
            }
        }
    }

    @Override public void visit(final Tree.LargerOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ").equals(",
                    getClAlias(), "larger()))||", ltmp, ">", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                Operators.simpleBinaryOp(that, "(", ">", ")", this);
            } else {
                Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
                out(".equals(", getClAlias(), "larger())");
            }
        }
    }

    @Override public void visit(final Tree.SmallAsOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ")!==",
                    getClAlias(), "larger())||", ltmp, "<=", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                Operators.simpleBinaryOp(that, "(", "<=", ")", this);
            } else {
                out("(");
                Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
                out("!==", getClAlias(), "larger()");
                out(")");
            }
        }
    }

    @Override public void visit(final Tree.LargeAsOp that) {
        if (dynblock > 0 && Util.isTypeUnknown(that.getLeftTerm().getTypeModel())) {
            //Try to use compare() if it exists
            String ltmp = names.createTempVariable();
            String rtmp = names.createTempVariable();
            out("(", ltmp, "=");
            box(that.getLeftTerm());
            out(",", rtmp, "=");
            box(that.getRightTerm());
            out(",(", ltmp, ".compare&&", ltmp, ".compare(", rtmp, ")!==",
                    getClAlias(), "smaller())||", ltmp, ">=", rtmp, ")");
        } else {
            final boolean usenat = canUseNativeComparator(that.getLeftTerm(), that.getRightTerm());
            if (usenat) {
                Operators.simpleBinaryOp(that, "(", ">=", ")", this);
            } else {
                out("(");
                Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
                out("!==", getClAlias(), "smaller()");
                out(")");
            }
        }
    }
    public void visit(final Tree.WithinOp that) {
        final String ttmp = names.createTempVariable();
        out("(", ttmp, "=");
        box(that.getTerm());
        out(",");
        if (dynblock > 0 && Util.isTypeUnknown(that.getTerm().getTypeModel())) {
            final String tmpl = names.createTempVariable();
            final String tmpu = names.createTempVariable();
            out(tmpl, "=");
            box(that.getLowerBound().getTerm());
            out(",", tmpu, "=");
            box(that.getUpperBound().getTerm());
            out(",((", ttmp, ".compare&&",ttmp,".compare(", tmpl);
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                out(")===", getClAlias(), "larger())||", ttmp, ">", tmpl, ")");
            } else {
                out(")!==", getClAlias(), "smaller())||", ttmp, ">=", tmpl, ")");
            }
            out("&&((", ttmp, ".compare&&",ttmp,".compare(", tmpu);
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                out(")===", getClAlias(), "smaller())||", ttmp, "<", tmpu, ")");
            } else {
                out(")!==", getClAlias(), "larger())||", ttmp, "<=", tmpu, ")");
            }
        } else {
            out(ttmp, ".compare(");
            box(that.getLowerBound().getTerm());
            if (that.getLowerBound() instanceof Tree.OpenBound) {
                out(")===", getClAlias(), "larger()");
            } else {
                out(")!==", getClAlias(), "smaller()");
            }
            out("&&");
            out(ttmp, ".compare(");
            box(that.getUpperBound().getTerm());
            if (that.getUpperBound() instanceof Tree.OpenBound) {
                out(")===", getClAlias(), "smaller()");
            } else {
                out(")!==", getClAlias(), "larger()");
            }
        }
        out(")");
    }

   @Override public void visit(final Tree.AndOp that) {
       Operators.simpleBinaryOp(that, "(", "&&", ")", this);
   }

   @Override public void visit(final Tree.OrOp that) {
       Operators.simpleBinaryOp(that, "(", "||", ")", this);
   }

   @Override public void visit(final Tree.EntryOp that) {
       out(getClAlias(), "Entry(");
       Operators.genericBinaryOp(that, ",", that.getTypeModel().getTypeArguments(),
               that.getTypeModel().getVarianceOverrides(), this);
   }

   @Override public void visit(final Tree.RangeOp that) {
       out(getClAlias(), "span(");
       that.getLeftTerm().visit(this);
       out(",");
       that.getRightTerm().visit(this);
       out(",{Element$span:");
       TypeUtils.typeNameOrList(that,
               Util.unionType(that.getLeftTerm().getTypeModel(), that.getRightTerm().getTypeModel(), that.getUnit()),
               this, false);
       out("})");
   }

   @Override
   public void visit(final Tree.SegmentOp that) {
       final Tree.Term left  = that.getLeftTerm();
       final Tree.Term right = that.getRightTerm();
       out(getClAlias(), "measure(");
       left.visit(this);
       out(",");
       right.visit(this);
       out(",{Element$measure:");
       TypeUtils.typeNameOrList(that,
               Util.unionType(left.getTypeModel(), right.getTypeModel(), that.getUnit()),
               this, false);
       out("})");
   }

   @Override public void visit(final Tree.ThenOp that) {
       Operators.simpleBinaryOp(that, "(", "?", ":null)", this);
   }

   @Override public void visit(final Tree.Element that) {
       out(".$_get(");
       if (!isNaturalLiteral(that.getExpression().getTerm())) {
           that.getExpression().visit(this);
       }
       out(")");
   }

   @Override public void visit(final Tree.DefaultOp that) {
       String lhsVar = createRetainedTempVar();
       out("(", lhsVar, "=");
       box(that.getLeftTerm());
       out(",", getClAlias(), "nn$(", lhsVar, ")?", lhsVar, ":");
       box(that.getRightTerm());
       out(")");
   }

   @Override public void visit(final Tree.IncrementOp that) {
       Operators.prefixIncrementOrDecrement(that.getTerm(), "successor", this);
   }

   @Override public void visit(final Tree.DecrementOp that) {
       Operators.prefixIncrementOrDecrement(that.getTerm(), "predecessor", this);
   }

   boolean hasSimpleGetterSetter(Declaration decl) {
       return (dynblock > 0 && TypeUtils.isUnknown(decl)) ||
               !((decl instanceof Value && ((Value)decl).isTransient()) || (decl instanceof Setter) || decl.isFormal());
   }

   @Override public void visit(final Tree.PostfixIncrementOp that) {
       Operators.postfixIncrementOrDecrement(that.getTerm(), "successor", this);
   }

   @Override public void visit(final Tree.PostfixDecrementOp that) {
       Operators.postfixIncrementOrDecrement(that.getTerm(), "predecessor", this);
   }

    @Override
    public void visit(final Tree.UnionOp that) {
        Operators.genericBinaryOp(that, ".union(",
                TypeUtils.mapTypeArgument(that, "union", "Element", "Other"),
                that.getTypeModel().getVarianceOverrides(), this);
    }

    @Override
    public void visit(final Tree.IntersectionOp that) {
        Operators.genericBinaryOp(that, ".intersection(",
                TypeUtils.mapTypeArgument(that, "intersection", "Element", "Other"),
                that.getTypeModel().getVarianceOverrides(), this);
    }

    @Override
    public void visit(final Tree.ComplementOp that) {
        Operators.genericBinaryOp(that, ".complement(",
                TypeUtils.mapTypeArgument(that, "complement", "Element", "Other"),
                that.getTypeModel().getVarianceOverrides(), this);
    }

   @Override public void visit(final Tree.Exists that) {
       Operators.unaryOp(that, getClAlias()+"nn$(", ")", this);
   }
   @Override public void visit(final Tree.Nonempty that) {
       Operators.unaryOp(that, getClAlias()+"ne$(", ")", this);
   }

   //Don't know if we'll ever see this...
   @Override public void visit(final Tree.ConditionList that) {
       spitOut("ZOMG condition list in the wild! " + that.getLocation()
               + " of " + that.getUnit().getFilename());
       super.visit(that);
   }

   @Override public void visit(final Tree.BooleanCondition that) {
       int boxType = boxStart(that.getExpression().getTerm());
       super.visit(that);
       if (boxType == 4) out("/*TODO: callable targs 10*/");
       boxUnboxEnd(boxType);
   }

   @Override public void visit(final Tree.IfStatement that) {
       if (errVisitor.hasErrors(that))return;
       conds.generateIf(that);
   }
   @Override public void visit(final Tree.IfExpression that) {
       if (errVisitor.hasErrors(that))return;
       conds.generateIfExpression(that, false);
   }

   @Override public void visit(final Tree.WhileStatement that) {
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
    void generateIsOfType(Node term, String termString, final ProducedType type, String tmpvar, final boolean negate) {
        if (negate) {
            out("!");
        }
        out(getClAlias(), "is$(");
        if (term instanceof Term) {
            conds.specialConditionRHS((Term)term, tmpvar);
        } else {
            conds.specialConditionRHS(termString, tmpvar);
        }
        out(",");
        TypeUtils.typeNameOrList(term, type, this, false);
        if (type.getQualifyingType() != null) {
            out(",[");
            ProducedType outer = type.getQualifyingType();
            boolean first=true;
            while (outer != null) {
                if (first) {
                    first=false;
                } else{
                    out(",");
                }
                TypeUtils.typeNameOrList(term, outer, this, false);
                outer = outer.getQualifyingType();
            }
            out("]");
        } else if (type.getDeclaration() != null && type.getDeclaration().getContainer() != null) {
            Declaration d = Util.getContainingDeclarationOfScope(type.getDeclaration().getContainer());
            if (d != null && d instanceof Method && !((Method)d).getTypeParameters().isEmpty()) {
                out(",$$$mptypes");
            }
        }
        out(")");
    }

    @Override
    public void visit(final Tree.IsOp that) {
        generateIsOfType(that.getTerm(), null, that.getType().getTypeModel(), null, false);
    }

    @Override public void visit(final Tree.Break that) {
        if (continues.isEmpty()) {
            out("break;");
        } else {
            ContinueBreakVisitor top=continues.peek();
            if (that.getScope()==top.getScope()) {
                out(top.getBreakName(), "=true; return;");
            } else {
                out("break;");
            }
        }
    }
    @Override public void visit(final Tree.Continue that) {
        if (continues.isEmpty()) {
            out("continue;");
        } else {
            ContinueBreakVisitor top=continues.peek();
            if (that.getScope()==top.getScope()) {
                out(top.getContinueName(), "=true; return;");
            } else {
                out("continue;");
            }
        }
    }

    @Override public void visit(final Tree.ForStatement that) {
        if (errVisitor.hasErrors(that))return;
        new ForGenerator(this, directAccess).generate(that);
    }

    public void visit(final Tree.InOp that) {
        box(that.getRightTerm());
        out(".contains(");
        if (!isNaturalLiteral(that.getLeftTerm())) {
            box(that.getLeftTerm());
        }
        out(")");
    }

    @Override public void visit(final Tree.TryCatchStatement that) {
        if (errVisitor.hasErrors(that))return;
        new TryCatchGenerator(this, directAccess).generate(that);
    }

    @Override public void visit(final Tree.Throw that) {
        out("throw ", getClAlias(), "wrapexc(");
        if (that.getExpression() == null) {
            out(getClAlias(), "Exception()");
        } else {
            that.getExpression().visit(this);
        }
        that.getUnit().getFullPath();
        out(",'", that.getLocation(), "','", that.getUnit().getRelativePath(), "');");
    }

    private void visitIndex(final Tree.IndexExpression that) {
        that.getPrimary().visit(this);
        ElementOrRange eor = that.getElementOrRange();
        if (eor instanceof Element) {
            final Tree.Expression _elemexpr = ((Tree.Element)eor).getExpression();
            final String _end;
            if (Util.isTypeUnknown(that.getPrimary().getTypeModel()) && dynblock > 0) {
                out("[");
                _end = "]";
            } else {
                out(".$_get(");
                _end = ")";
            }
            if (!isNaturalLiteral(_elemexpr.getTerm())) {
                _elemexpr.visit(this);
            }
            out(_end);
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
                out(".measure(");
            }
            if (er.getLowerBound() != null) {
                if (!isNaturalLiteral(er.getLowerBound().getTerm())) {
                    er.getLowerBound().visit(this);
                }
                if (er.getUpperBound() != null || sexpr != null) {
                    out(",");
                }
            }
            if (er.getUpperBound() != null) {
                if (!isNaturalLiteral(er.getUpperBound().getTerm())) {
                    er.getUpperBound().visit(this);
                }
            } else if (sexpr != null) {
                sexpr.visit(this);
            }
            out(")");
        }
    }

    public void visit(final Tree.IndexExpression that) {
        visitIndex(that);
    }

    @Override
    public void visit(final Tree.SwitchStatement that) {
        if (errVisitor.hasErrors(that))return;
        if (opts.isComment() && !opts.isMinify()) {
            out("//Switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            endLine();
        }
        conds.switchStatement(that);
        if (opts.isComment() && !opts.isMinify()) {
            out("//End switch statement at ", that.getUnit().getFilename(), " (", that.getLocation(), ")");
            endLine();
        }
    }
    @Override public void visit(final Tree.SwitchExpression that) {
        conds.switchExpression(that);
    }

    /** Generates the code for an anonymous function defined inside an argument list. */
    @Override
    public void visit(final Tree.FunctionArgument that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.functionArgument(that, this);
    }

    public void visit(final Tree.SpecifiedArgument that) {
        if (errVisitor.hasErrors(that))return;
        int _box=0;
        final Tree.SpecifierExpression expr = that.getSpecifierExpression();
        if (that.getParameter() != null && expr != null) {
            _box = boxUnboxStart(expr.getExpression().getTerm(), that.getParameter().getModel());
        }
        expr.visit(this);
        if (_box == 4) {
            out(",");
            //Add parameters
            invoker.describeMethodParameters(expr.getExpression().getTerm());
            out(",");
            TypeUtils.printTypeArguments(that, expr.getExpression().getTypeModel().getTypeArguments(), this, false,
                    expr.getExpression().getTypeModel().getVarianceOverrides());
        }
        boxUnboxEnd(_box);
    }

    /** Generates the code for a function in a named argument list. */
    @Override
    public void visit(final Tree.MethodArgument that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.methodArgument(that, this);
    }

    /** Determines whether the specified block should be enclosed in a function. */
    public boolean shouldEncloseBlock(Block block) {
        // just check if the block contains a captured declaration
        for (Tree.Statement statement : block.getStatements()) {
            if (statement instanceof Tree.Declaration) {
                if (((Tree.Declaration) statement).getDeclarationModel().isCaptured()) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Encloses the block in a function, IF NEEDED. */
    void encloseBlockInFunction(final Tree.Block block, final boolean markBlock) {
        final boolean wrap=shouldEncloseBlock(block);
        final ContinueBreakVisitor cbv = new ContinueBreakVisitor(block, names);
        if (wrap) {
            continues.push(cbv);
            if (markBlock) {
                beginBlock();
            }
            boolean vars=false;
            if (cbv.isContinues()) {
                out("var ", cbv.getContinueName(), "=false");
                vars=true;
            }
            if (cbv.isBreaks()) {
                out(vars?",":"var ", cbv.getBreakName(), "=false");
                vars=true;
            }
            out(vars?",":"var ", cbv.getReturnName(), "=(function()");
            if (!markBlock) {
                beginBlock();
            }
        }
        if (markBlock) {
            block.visit(this);
        } else {
            visitStatements(block.getStatements());
        }
        if (wrap) {
            continues.pop();
            if (!markBlock)endBlock();
            out("());if(", cbv.getReturnName(), "!==undefined){return ", cbv.getReturnName(), ";}");
            if (cbv.isContinues()) {
                out("else if(", cbv.getContinueName(),"===true){continue;}");
            }
            if (cbv.isBreaks()) {
                out("else if(", cbv.getBreakName(),"===true){break;}");
            }
            if (markBlock) {
                endBlockNewLine();
            }
        }
    }

    /** This interface is used inside type initialization method. */
    static interface PrototypeInitCallback {
        /** Generates a function that adds members to a prototype, then calls it. */
        void addToPrototypeCallback();
    }

    @Override
    public void visit(final Tree.Tuple that) {
        SequenceGenerator.tuple(that, this);
    }

    @Override
    public void visit(final Tree.Assertion that) {
        if (opts.isComment() && !opts.isMinify()) {
            out("//assert");
            location(that);
            endLine();
        }
        String custom = "Assertion failed";
        //Scan for a "doc" annotation with custom message
        if (that.getAnnotationList() != null && that.getAnnotationList().getAnonymousAnnotation() != null) {
            custom = that.getAnnotationList().getAnonymousAnnotation().getStringLiteral().getText();
        } else {
            for (Annotation ann : that.getAnnotationList().getAnnotations()) {
                BaseMemberExpression bme = (BaseMemberExpression)ann.getPrimary();
                if ("doc".equals(bme.getDeclaration().getName())) {
                    custom = ((Tree.ListedArgument)ann.getPositionalArgumentList().getPositionalArguments().get(0))
                            .getExpression().getTerm().getText();
                }
            }
        }
        StringBuilder sb = new StringBuilder(custom).append(": '");
        for (int i = that.getConditionList().getToken().getTokenIndex()+1;
                i < that.getConditionList().getEndToken().getTokenIndex(); i++) {
            sb.append(tokens.get(i).getText());
        }
        sb.append("' at ").append(that.getUnit().getFilename()).append(" (").append(
                that.getConditionList().getLocation()).append(")");
        conds.specialConditionsAndBlock(that.getConditionList(), null, getClAlias()+"asrt$(");
        //escape
        out(",\"", JsUtils.escapeStringLiteral(sb.toString()), "\",'",that.getLocation(), "','",
                that.getUnit().getFilename(), "');");
        endLine();
    }

    @Override
    public void visit(Tree.DynamicStatement that) {
        dynblock++;
        if (dynblock == 1 && !opts.isMinify()) {
            if (opts.isComment()) {
                out("/*BEG dynblock*/");
            }
            endLine();
        }
        for (Tree.Statement stmt : that.getDynamicClause().getBlock().getStatements()) {
            stmt.visit(this);
        }
        if (dynblock == 1 && !opts.isMinify()) {
            if (opts.isComment()) {
                out("/*END dynblock*/");
            }
            endLine();
        }
        dynblock--;
    }

    public boolean isInDynamicBlock() {
        return dynblock > 0;
    }

    @Override
    public void visit(final Tree.TypeLiteral that) {
        //Can be an alias, class, interface or type parameter
        if (that.getWantsDeclaration()) {
            MetamodelHelper.generateOpenType(that, that.getDeclaration(), this);
        } else {
            MetamodelHelper.generateClosedTypeLiteral(that, this);
        }
    }

    @Override
    public void visit(final Tree.MemberLiteral that) {
        if (that.getWantsDeclaration()) {
            MetamodelHelper.generateOpenType(that, that.getDeclaration(), this);
        } else {
            MetamodelHelper.generateMemberLiteral(that, this);
        }
    }

    @Override
    public void visit(final Tree.PackageLiteral that) {
        com.redhat.ceylon.model.typechecker.model.Package pkg =
                (com.redhat.ceylon.model.typechecker.model.Package)that.getImportPath().getModel();
        MetamodelHelper.findModule(pkg.getModule(), this);
        out(".findPackage('", pkg.getNameAsString(), "')");
    }

    @Override
    public void visit(Tree.ModuleLiteral that) {
        Module m = (Module)that.getImportPath().getModel();
        MetamodelHelper.findModule(m, this);
    }

    /** Call internal function "throwexc" with the specified message and source location. */
    void generateThrow(String exceptionClass, String msg, Node node) {
        out(getClAlias(), "throwexc(", exceptionClass==null ? getClAlias() + "Exception":exceptionClass, "(");
        out("\"", JsUtils.escapeStringLiteral(msg), "\"),'", node.getLocation(), "','",
                node.getUnit().getFilename(), "')");
    }

    @Override public void visit(Tree.CompilerAnnotation that) {
        //just ignore this
    }

    /** Outputs the initial part of an attribute definition. */
    void defineAttribute(final String owner, final String name) {
        out(getClAlias(), "atr$(", owner, ",'", name, "',function()");
    }

    public int getExitCode() {
        return exitCode;
    }

    @Override public void visit(Tree.ListedArgument that) {
        if (!isNaturalLiteral(that.getExpression().getTerm())) {
            super.visit(that);
        }
    }

    @Override public void visit(Tree.LetExpression that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.generateLet(that, directAccess, this);
    }

    @Override public void visit(final Tree.Destructure that) {
        if (errVisitor.hasErrors(that))return;
        final String expvar = names.createTempVariable();
        out("var ", expvar, "=");
        that.getSpecifierExpression().visit(this);
        new Destructurer(that.getPattern(), this, directAccess, expvar, false);
        endLine(true);
    }

    boolean isNaturalLiteral(Tree.Term that) {
        if (that instanceof Tree.NaturalLiteral) {
            out(Long.toString(parseNaturalLiteral((Tree.NaturalLiteral)that, false)));
            return true;
        } else if (that instanceof Tree.NegativeOp) {
            that.visit(this);
            return true;
        }
        return false;
    }
}
