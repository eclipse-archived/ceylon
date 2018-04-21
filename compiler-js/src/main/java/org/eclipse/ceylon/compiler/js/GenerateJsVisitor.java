/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.eliminateParensAndWidening;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.runtime.Token;
import org.eclipse.ceylon.cmr.impl.NpmRepository;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.js.loader.JsonModule;
import org.eclipse.ceylon.compiler.js.util.ContinueBreakVisitor;
import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;
import org.eclipse.ceylon.compiler.js.util.JsOutput;
import org.eclipse.ceylon.compiler.js.util.JsUtils;
import org.eclipse.ceylon.compiler.js.util.JsWriter;
import org.eclipse.ceylon.compiler.js.util.Options;
import org.eclipse.ceylon.compiler.js.util.RetainedVars;
import org.eclipse.ceylon.compiler.js.util.TypeUtils;
import org.eclipse.ceylon.compiler.js.util.TypeUtils.RuntimeMetamodelAnnotationGenerator;
import org.eclipse.ceylon.compiler.typechecker.analyzer.Warning;
import org.eclipse.ceylon.compiler.typechecker.io.VirtualFile;
import org.eclipse.ceylon.compiler.typechecker.tree.CustomTree.GuardedVariable;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Expression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ExtendedType;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Statement;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.StaticMemberOrTypeExpression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Term;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.util.NativeUtil;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassAlias;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Import;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Referenceable;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Specification;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class GenerateJsVisitor extends Visitor {

    private final Stack<ContinueBreakVisitor> continues = new Stack<>();
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess = new HashSet<>();
    private final Set<Declaration> generatedAttributes = new HashSet<>();
    private final RetainedVars retainedVars = new RetainedVars();
    final ConditionGenerator conds;
    private final InvocationGenerator invoker;
    private final List<? extends Token> tokens;
    private final ErrorVisitor errVisitor = new ErrorVisitor();
    private int dynblock;
    private int exitCode = 0;
    private final Map<String, Tree.Declaration> headers = new HashMap<String, Tree.Declaration>();
    protected static final BigInteger minLong = new BigInteger(Long.toString(Long.MIN_VALUE));
    protected static final BigInteger maxLong = new BigInteger(Long.toString(Long.MAX_VALUE));
    protected static final BigInteger maxUnsignedLong = new BigInteger("ffffffffffffffff", 16);

    static final class SuperVisitor extends Visitor {
        private final List<Declaration> decs;

        SuperVisitor(List<Declaration> decs) {
            this.decs = decs;
        }

        @Override
        public void visit(Tree.QualifiedMemberOrTypeExpression qe) {
            Term primary = eliminateParensAndWidening(qe.getPrimary());
            if (primary instanceof Tree.Super) {
                decs.add(qe.getDeclaration());
            }
            super.visit(qe);
        }

        @Override
        public void visit(Tree.QualifiedType that) {
            if (that.getOuterType() instanceof Tree.SuperType) {
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
        public void visit(Tree.QualifiedMemberOrTypeExpression qe) {
            if (qe.getPrimary() instanceof Tree.Outer ||
                    qe.getPrimary() instanceof Tree.This) {
                if ( qe.getDeclaration().equals(dec) ) {
                    found = true;
                }
            }
            super.visit(qe);
        }
    }

    private List<? extends Statement> currentStatements = null;

    private final JsCompiler compiler;
    public final JsWriter out;
    private final PrintWriter verboseOut;
    private final boolean verboseStitcher;
    public final Options opts;
    private Tree.CompilationUnit root;
    static final String function="function ";

    public Package getCurrentPackage() {
        return root.getUnit().getPackage();
    }

    /** Returns the module name for the language module. */
    public String getClAlias() { return jsout.getLanguageModuleAlias(); }

    @Override
    public void handleException(Exception e, Node that) {
        that.addUnexpectedError(that.getMessage(e, this), Backend.JavaScript);
    }

    private final JsOutput jsout;

    public GenerateJsVisitor(JsCompiler compiler, JsOutput out, 
    		Options options, JsIdentifierNames names,
            List<? extends Token> tokens) throws IOException {
        this.compiler = compiler;
        this.jsout = out;
        this.opts = options;
        if (options.hasVerboseFlag("code")) {
            Writer vw = options.getOutWriter();
            verboseOut = vw instanceof PrintWriter ? (PrintWriter)vw :
                new PrintWriter(vw == null ? new OutputStreamWriter(System.out) : vw);
        } else {
            verboseOut = null;
        }
        verboseStitcher = options.hasVerboseFlag("stitcher");
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
            final Tree.ModuleDescriptor md = that.getModuleDescriptors().get(0);
            Tree.QuotedLiteral gql = md.getGroupQuotedLiteral();
            Tree.QuotedLiteral aql = md.getArtifact();
            Tree.Identifier namespace = md.getNamespace();
            if (namespace != null
                    && NpmRepository.NAMESPACE.equals(namespace.getText())
                    && gql != null) {
                String npmName = quotedText(gql);
                if (aql != null) {
                    npmName = "@" + npmName + "/" + quotedText(aql);
                }
                ((JsonModule)that.getUnit().getPackage().getModule()).setNpmPath(npmName);
            }
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
                public List<Annotation> getAnnotations() {
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
            if (md.getImportModuleList() != null) {
                List<Tree.ImportModule> importModules = md.getImportModuleList().getImportModules();
                if (!importModules.isEmpty()) {
                    out("ex$.$mod$imps=function(){return{");
                    if (!opts.isMinify())endLine();
                    boolean first=true;
                    for (final Tree.ImportModule im : importModules) {
                        final StringBuilder path=new StringBuilder("'");
                        if (im.getName()!=null) {
                            path.append(im.getName());
                        }
                        else {
                            throw new CompilerErrorException("Invalid imported module");
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
                            public List<Annotation> getAnnotations() {
                                Referenceable model = im.getImportPath().getModel();
                                if (model instanceof Module) {
                                    return ((Module)model).getAnnotations();
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
                public List<Annotation> getAnnotations() {
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

        for (Tree.CompilerAnnotation ca: that.getCompilerAnnotations()) {
            ca.visit(this);
        }
        if (that.getImportList() != null) {
            that.getImportList().visit(this);
        }
        visitStatements(that.getDeclarations());
    }

    private String quotedText(Tree.QuotedLiteral ql) {
        String npmName = ql.getText();
        if (npmName.charAt(0)=='"' && npmName.charAt(npmName.length()-1) == '"') {
            return npmName.substring(1, npmName.length()-1);
        }
        else {
            return npmName;
        }
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
        String ptypes = null;
        //Check if this is the first parameter list
        if (that.getScope() instanceof Function && that.getModel().isFirst()) {
            Function fun = (Function)that.getScope();
            List<TypeParameter> typeParameters = fun.getTypeParameters();
            if (typeParameters != null && !typeParameters.isEmpty()) {
                ptypes = names.typeArgsParamName(fun);
            }
        }
        for (Tree.Parameter param: that.getParameters()) {
            if (!first) out(",");
            out(names.name(param.getParameterModel()));
            first = false;
        }
        if (ptypes != null) {
            if (!first) out(",");
            out(ptypes);
        }
        out(")");
    }

    /** Like visitStatements but for constructors, because we need to check if a statement is
     * a specifier expression and generate it regardless of lexical/prototype mode. */
    void generateConstructorStatements(final Tree.Declaration cnstr,
            List<? extends Tree.Statement> stmts) {
        final List<String> oldRetainedVars = retainedVars.reset(null);
        final List<? extends Statement> prevStatements = currentStatements;
        currentStatements = stmts;
        ReturnConstructorVisitor rcv = new ReturnConstructorVisitor(cnstr);
        if (rcv.isReturns()) {
            out("(function(){");
        }
        for (Tree.Statement s2 : stmts) {
            if (s2 instanceof Tree.SpecifierStatement) {
                Tree.SpecifierStatement ss = (Tree.SpecifierStatement)s2;
                if (cnstr instanceof Tree.Constructor) {
                    specifierStatement(((Tree.Constructor)cnstr).getConstructor(), ss);
                } else if (cnstr instanceof Tree.Enumerated) {
                    specifierStatement(((Tree.Enumerated)cnstr).getEnumerated(), ss);
                }
            } else {
                s2.visit(this);
            }
            if (!opts.isMinify())beginNewLine();
            retainedVars.emitRetainedVars(this);
            //Remove attribute declaration in lexical style so that
            //every constructor adds metamodel to that attribute
            if (!opts.isOptimize() && cnstr != null && s2 instanceof Tree.AttributeDeclaration) {
                generatedAttributes.remove(((Tree.AttributeDeclaration)s2).getDeclarationModel());
            }
        }
        if (rcv.isReturns()) {
            out("}());");
        }
        retainedVars.reset(oldRetainedVars);
        currentStatements = prevStatements;
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

    public void visitSingleExpression(Tree.Expression that) {
        List<String> oldRetainedVars = retainedVars.reset(null);
        final int boxType = boxStart(that.getTerm());
        that.visit(this);
        if (boxType == 4) out("/*TODO: callable targs 3*/");
        boxUnboxEnd(boxType);
        endLine(true);
        retainedVars.emitRetainedVars(this);
        retainedVars.reset(oldRetainedVars);
    }

    @Override
    public void visit(final Tree.Body that) {
        visitStatements(that.getStatements());
    }

    private final Deque<Tree.ImportList> blockImports = new ArrayDeque<>();

    public void pushImports(Tree.Block block) {
        if (block.getImportList() != null) {
            blockImports.push(block.getImportList());
        }
    }
    public void popImports(Tree.Block block) {
        if (block.getImportList() != null) {
            Tree.ImportList top = blockImports.pop();
            if (top != block.getImportList()) {
                throw new CompilerErrorException("POPS != PUSHES in block imports");
            }
        }
    }

    public Import findImport(Node node, Declaration d) {
        Import found = null;
        for (Tree.ImportList il : blockImports) {
            for (Tree.Import i : il.getImports()) {
                for (Tree.ImportMemberOrType imot : i.getImportMemberOrTypeList().getImportMemberOrTypes()) {
                    found = findImport(d, imot);
                    if (found != null) {
                        break;
                    }
                }
            }
        }
        if (found == null) {
            for (Import i : node.getUnit().getImports()) {
                if (!i.isAmbiguous() && i.getTypeDeclaration() != null && i.getDeclaration().equals(d)) {
                    found = i;
                    break;
                }
            }
        }
        return found;
    }
    
    private Import findImport(Declaration d, Tree.ImportMemberOrType imot) {
        Import found = null;
        Import imp = imot.getImportModel();
        if (!imp.isAmbiguous() && imp.getTypeDeclaration() != null
                && imp.getDeclaration().equals(d)) {
            found = imp;
        }
        if (found == null) {
            for (Tree.ImportMemberOrType nested : imot.getImportMemberOrTypeList().getImportMemberOrTypes()) {
                found = findImport(d, nested);
                if (found != null) {
                    break;
                }
            }
        }
        return found;
    }

    @Override
    public void visit(final Tree.Block that) {
        List<Statement> stmnts = that.getStatements();
        if (stmnts.isEmpty()) {
            out("{}");
        }
        else {
            pushImports(that);
            beginBlock();
            visitStatements(stmnts);
            endBlock();
            popImports(that);
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
                final Declaration od = ModelUtil.getContainingDeclaration(prototypeOwner);
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
                Scope scope = ModelUtil.getRealScope(n.getScope());
                if (scope instanceof Specification) {
                    scope = ModelUtil.getRealScope(scope.getContainer());
                }
                final boolean isMember = scope instanceof ClassOrInterface 
                        || scope instanceof Declaration 
                            && (((Declaration)scope).isClassOrInterfaceMember() 
                                    || ((Declaration)scope).isParameter());
                if (isMember) {
                    n.visit(this);
                }
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
                if (s == prototypeOwner 
                        || s instanceof TypeDeclaration && prototypeOwner.inherits((TypeDeclaration)s)) {
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
            Scope endScope = origScope;
            if (endScope instanceof TypeParameter) {
                endScope = endScope.getContainer();
            }
            while (s != null) {
                if (s == endScope ||
                        (s instanceof TypeDeclaration && endScope instanceof TypeDeclaration
                                && ((TypeDeclaration)s).inherits((TypeDeclaration)endScope))) {
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
            Function dec = ((Tree.Constructor)that).getDeclarationModel();
            String cname = ((Class)dec.getContainer()).getName();
            out("//Constructor ", cname, ".",
                    dec.getName() == null ? "<default>" : dec.getName());
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
        final boolean shared = sharePrefix(d, excludeProtoMembers);
        if (shared) {
            String dname=names.name(d);
            if (dname.endsWith("()")){
                dname = dname.substring(0, dname.length()-2);
            }
            if (names.isJsGlobal(d)) {
                out(dname.substring(2), "=", dname);
            } else {
                out(dname, "=", dname);
            }
            endLine(true);
        }
        return shared;
    }

    private boolean sharePrefix(Declaration d, boolean excludeProtoMembers) {
        boolean shared = false;
        if (!(excludeProtoMembers && opts.isOptimize() && d.isClassOrInterfaceMember())
                && (d instanceof ClassOrInterface || isCaptured(d))) {
            beginNewLine();
            if (d.isStatic()) {
                out(names.name(ModelUtil.getContainingClassOrInterface((Scope)d)), ".$st$.");
                shared = true;
            } else if (outerSelf(d)) {
                out(".");
                shared = true;
            }
        }
        return shared;
    }

    @Override
    public void visit(final Tree.ClassDeclaration that) {
        if (opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember()) return;
        classDeclaration(that);
    }

    private void addClassDeclarationToPrototype(TypeDeclaration outer, final Tree.ClassDeclaration that) {
        classDeclaration(that);
        final String tname = names.name(that.getDeclarationModel());
        if (that.getDeclarationModel().isStatic()) {
            out(names.name(outer), ".$st$.", tname, "=", tname);
        } else {
            out(names.self(outer), ".", tname, "=", tname);
        }
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
        Type pt = that.getTypeSpecifier().getType().getTypeModel();
        boolean skip=true;
        if (pt.involvesTypeParameters() && outerSelf(d)) {
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
        if (that.getDeclarationModel().isStatic()) {
            out(names.name(outer), ".$st$.", tname, "=", tname);
        } else {
            out(names.self(outer), ".", tname, "=", tname);
        }
        endLine(true);
    }

    private void addInterfaceToPrototype(ClassOrInterface type, final Tree.InterfaceDefinition interfaceDef, InitDeferrer initDeferrer) {
        if (type.isDynamic())return;
        TypeGenerator.interfaceDefinition(interfaceDef, this, initDeferrer);
        Interface d = interfaceDef.getDeclarationModel();
        if (d.isStatic()) {
            out(names.name(type), ".$st$.", names.name(d), "=", names.name(d));
        } else {
            out(names.self(type), ".", names.name(d), "=", names.name(d));
        }
        endLine(true);
    }

    @Override
    public void visit(final Tree.InterfaceDefinition that) {
        if (!(opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember())) {
            TypeGenerator.interfaceDefinition(that, this, null);
        }
    }

    static class InitDeferrer{
        List<String> deferred = new LinkedList<String>();
    }
    
    private void addClassToPrototype(ClassOrInterface type, final Tree.ClassDefinition classDef, InitDeferrer initDeferrer) {
        if (type.isDynamic())return;
        ClassGenerator.classDefinition(classDef, this, initDeferrer);
        final String tname = names.name(classDef.getDeclarationModel());
        if (classDef.getDeclarationModel().isStatic()) {
            out(names.name(type), ".$st$.", tname, "=", tname);
        } else {
            out(names.self(type), ".", tname, "=", tname);
        }
        endLine(true);
    }

    @Override
    public void visit(final Tree.ClassDefinition that) {
        if (opts.isOptimize() && that.getDeclarationModel().isClassOrInterfaceMember()) return;
        ClassGenerator.classDefinition(that, this, null);
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
        final Type pt = ext.getTypeModel();
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
        for (Tree.Parameter p: that.getParameterList().getParameters()) {
            p.visit(this);
            out(", ");
        }
        if (d.getTypeParameters() != null && !d.getTypeParameters().isEmpty()) {
            out("$$targs$$,");
        }
        out(names.self(d), "){");
        initSelf(that);
        initParameters(that.getParameterList(), d, null);
        out("return ");
        TypeDeclaration aliased = ext.getType().getDeclarationModel();
        final String aliasedName;
        aliasedName = names.name(aliased);
        qualify(that, aliased);
        Scope superscope = getSuperMemberScope(ext.getType());
        if (superscope != null) {
            out("getT$all()['");
            out(superscope.getQualifiedNameString());
            out("'].$$.prototype.", aliasedName, ".call(", names.self(prototypeOwner), ",");
        } else {
            out(aliasedName, "(");
        }
        Tree.PositionalArgumentList posArgs = ext.getInvocationExpression().getPositionalArgumentList();
        if (posArgs != null) {
            posArgs.visit(this);
            if (!posArgs.getPositionalArguments().isEmpty()) {
                out(",");
            }
        } else {
            out("/*PENDIENTE NAMED ARG CLASS DECL */");
        }
        Map<TypeParameter, Type> invargs = ext.getType().getTypeModel().getTypeArguments();
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
        if (aliased instanceof Class && ((Class)aliased).hasConstructors() || aliased instanceof Constructor) {
            Class ac = aliased instanceof Constructor ? (Class)((Constructor)aliased).getContainer() :
                (Class)aliased;
            for (Declaration cm : ac.getMembers()) {
                if (cm instanceof Constructor && cm!=ac.getDefaultConstructor() && cm.isShared()) {
                    Constructor cons = (Constructor)cm;
                    final String constructorName = aname + names.constructorSeparator(cons) + names.name(cons);
                    out("function ", constructorName, "(");
                    List<Parameter> parameters = cons.getFirstParameterList().getParameters();
                    ArrayList<String> pnames = new ArrayList<>(parameters.size()+1);
                    boolean first=true;
                    for (int i=0;i<parameters.size();i++) {
                        final String pname = names.createTempVariable();
                        pnames.add(pname);
                        if (first) {
                            first=false;
                        } else {
                            out(",");
                        }
                        out(pname);
                    }
                    out("){return ");
                    qualify(that, cons);
                    out(names.name(cons), "(");
                    first=true;
                    for (String pname : pnames) {
                        if (first) {
                            first=false;
                        } else {
                            out(",");
                        }
                        out(pname);
                    }
                    out(");}");
                    if (ac.isShared()) {
                        sharePrefix(ac, true);
                        out(constructorName, "=", constructorName,";");
                        endLine();
                    }
                }
            }
        }
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
        if (!d.isToplevel() && !d.isStatic()) {
            final ClassOrInterface coi = ModelUtil.getContainingClassOrInterface(d.getContainer());
            if (coi != null) {
                out(names.self(d), ".outer$");
                if (d.isClassOrInterfaceMember()) {
                    if (d.isStatic()) {
                        out("=", names.name(coi));
                    } else {
                        out("=this");
                    }
                } else {
                    out("=", names.self(coi));
                }
                endLine(true);
            }
        }
    }

    void addToPrototype(Node node, ClassOrInterface d, List<Tree.Statement> statements) {
        final boolean isSerial = d instanceof Class
                && ((Class)d).isSerializable();
        boolean enter = opts.isOptimize();
        ArrayList<Parameter> plist = null;
        final boolean isAbstractNative = TypeUtils.makeAbstractNative(d);
        final String typename = names.name(d);
        final boolean overrideToString = d.getDirectMember("toString", null, true) == null;
        if (enter) {
            enter = !statements.isEmpty() | overrideToString;
            if (d instanceof Class) {
                ParameterList cpl = ((Class)d).getParameterList();
                if (cpl != null) {
                    plist = new ArrayList<>(cpl.getParameters().size());
                    plist.addAll(cpl.getParameters());
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
                //First of all, add an object to store statics only if needed
//                for (Statement s : statements) {
//                }
                //Generated attributes with corresponding parameters will remove them from the list
                if (plist != null) {
                    for (Parameter p : plist) {
                        generateAttributeForParameter(node, (Class)d, p);
                    }
                }
                boolean statics = false;
                InitDeferrer initDeferrer = new InitDeferrer();
                for (Statement s: statements) {
                    if (!statics && s instanceof Tree.Declaration) {
                        Declaration sd = ((Tree.Declaration)s).getDeclarationModel();
                        if (sd.isStatic()) {
                            statics = true;
                            out(names.name(d), ".$st$={};");
                        }
                    }
                    if (!(s instanceof Tree.ClassOrInterface) 
                            && !(s instanceof Tree.AttributeDeclaration 
                                && ((Tree.AttributeDeclaration)s).getDeclarationModel().isParameter())) {
                        addToPrototype(d, s, plist, initDeferrer);
                    }
                }
                for (Statement s: statements) {
                    if (s instanceof Tree.ClassOrInterface) {
                        addToPrototype(d, s, plist, initDeferrer);
                    } else if (s instanceof Tree.Enumerated) {
                        //Add a simple attribute which really returns the singleton from the class
                        final Tree.Enumerated vc = (Tree.Enumerated)s;
                        Value vcd = vc.getDeclarationModel();
                        defineAttribute(names.self(d), names.name(vcd));
                        out("{return ", typename, names.constructorSeparator(vcd),
                                names.name(vcd), "();},undefined,");
                        TypeUtils.encodeForRuntime(vc, vcd, vc.getAnnotationList(), this);
                        out(");");
                    }
                }
                for(String stmt : initDeferrer.deferred){
                    out(stmt);
                    endLine();
                }
                if (d.isMember()) {
                    ClassOrInterface coi = ModelUtil.getContainingClassOrInterface(d.getContainer());
                    if (coi != null && d.inherits(coi)) {
                        out(names.self(d), ".", typename,"=", typename, ";");
                    }
                }
            }
            if (isSerial && !isAbstractNative) {
                SerializationHelper.addSerializer(node, (Class)d, this);
            }
            if (overrideToString) {
                out(names.self(d), ".", "toString=function(){return this.string.valueOf();};");
            }
            endBlock();
            out(")(", typename, ".$$.prototype)");
            endLine(true);
            
            currentStatements = prevStatements;
        }
    }

    void generateAttributeForParameter(Node node, Class d, Parameter p) {
        if (p.getDeclaration() instanceof Function 
                && ModelUtil.isConstructor(p.getDeclaration())) {
            return;
        }
        final FunctionOrValue pdec = p.getModel();
        final String privname = names.valueName(pdec);
        defineAttribute(names.self(d), names.name(pdec));
        out("{");
        if (pdec.isLate()) {
            generateUnitializedAttributeReadCheck("this."+privname, names.name(p), null);
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

    private void addToPrototype(ClassOrInterface d, final Tree.Statement s, List<Parameter> params, InitDeferrer initDeferrer) {
        ClassOrInterface oldPrototypeOwner = prototypeOwner;
        prototypeOwner = d;
        if (s instanceof Tree.MethodDefinition) {
            addMethodToPrototype(d, (Tree.MethodDefinition)s);
        } else if (s instanceof Tree.MethodDeclaration) {
            //Don't even bother with nodes that have errors
            if (errVisitor.hasErrors(s))return;
            FunctionHelper.methodDeclaration(d, (Tree.MethodDeclaration) s, this, verboseStitcher);
        } else if (s instanceof Tree.AttributeGetterDefinition) {
            addGetterToPrototype(d, (Tree.AttributeGetterDefinition)s);
        } else if (s instanceof Tree.AttributeDeclaration) {
            AttributeGenerator.addGetterAndSetterToPrototype(d, (Tree.AttributeDeclaration) s, this, verboseStitcher);
        } else if (s instanceof Tree.ClassDefinition) {
            addClassToPrototype(d, (Tree.ClassDefinition) s, initDeferrer);
        } else if (s instanceof Tree.InterfaceDefinition) {
            addInterfaceToPrototype(d, (Tree.InterfaceDefinition) s, initDeferrer);
        } else if (s instanceof Tree.ObjectDefinition) {
            addObjectToPrototype(d, (Tree.ObjectDefinition) s, initDeferrer);
        } else if (s instanceof Tree.ClassDeclaration) {
            addClassDeclarationToPrototype(d, (Tree.ClassDeclaration) s);
        } else if (s instanceof Tree.InterfaceDeclaration) {
            addInterfaceDeclarationToPrototype(d, (Tree.InterfaceDeclaration) s);
        } else if (s instanceof Tree.SpecifierStatement) {
            addSpecifierToPrototype(d, (Tree.SpecifierStatement) s);
        } else if (s instanceof Tree.TypeAliasDeclaration) {
            addAliasDeclarationToPrototype(d, (Tree.TypeAliasDeclaration)s);
        }
        //This fixes #231 for prototype style
        if (params != null && s instanceof Tree.Declaration) {
            Declaration m = ((Tree.Declaration)s).getDeclarationModel();
            for (Iterator<Parameter> iter = params.iterator(); iter.hasNext();) {
                Parameter _p = iter.next();
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
        if (d instanceof Class && d.isAbstract()) {
            out(getClAlias(), "throwexc(", getClAlias(), "InvocationException$meta$model(");
            out("\"Cannot instantiate abstract class ", d.getQualifiedNameString(), "\"),'?','?')");
        } else {
            out(names.self(d), "=new ");
            if (opts.isOptimize() && d.isClassOrInterfaceMember() && !d.isStatic()) {
                out("this.");
            }
            out(names.name(d), ".$$;");
        }
        endLine();
    }

    void instantiateSelf(ClassOrInterface d) {
        out("var ", names.self(d), "=new ");
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
            if (d.isStatic()) {
                out(names.name(ModelUtil.getContainingClassOrInterface(d.getContainer())), ".$st$.");
            } else {
                out("this.");
            }
        }
        out(names.name(d), ".$$;");
    }

    private void addObjectToPrototype(ClassOrInterface type, final Tree.ObjectDefinition objDef, InitDeferrer initDeferrer) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(objDef))return;
        comment(objDef);
        Singletons.objectDefinition(objDef, this, initDeferrer);
        Value d = objDef.getDeclarationModel();
        Class c = (Class) d.getTypeDeclaration();
        if (d.isStatic()) {
            out(names.name(type), ".$st$.", names.name(d),
                    "=function(){if(", names.name(type), ".$st$.$INIT$",
                    names.name(d), "===undefined)", names.name(type), ".$st$.$INIT$",
                    names.name(d), "=$init$", names.name(d), "()();return ",
                    names.name(type), ".$st$.$INIT$", names.name(d), "};");
            out(names.name(type), ".$st$.", names.name(c), "=", names.name(c), ";");
        } else {
            out(names.self(type), ".", names.name(c), "=", names.name(c), ";");
        }
        if (d.isStatic()) {
            out(names.name(type), ".$st$.", names.name(d));
        } else {
            out(names.self(type), ".", names.name(c));
        }
        out(".$crtmm$=");
        TypeUtils.encodeForRuntime(objDef, d, this);
        endLine(true);
    }

    @Override
    public void visit(final Tree.ObjectDefinition that) {
        if (errVisitor.hasErrors(that))return;
        if (NativeUtil.isNativeHeader(that) &&
                ModelUtil.getNativeDeclaration(that.getDeclarationModel(), Backend.JavaScript) != null) {
            // It's a native header, remember it for later when we deal with its implementation
            headers.put(that.getDeclarationModel().getQualifiedNameString(), that);
            return;
        }
        // To accept this object it is either not native or native for this backend
        if (!(NativeUtil.isForBackend(that, Backend.JavaScript) 
                || NativeUtil.isHeaderWithoutBackend(that, Backend.JavaScript))) {
            return;
        }
        Value d = that.getDeclarationModel();
        if (!(opts.isOptimize() && d.isClassOrInterfaceMember())) {
            comment(that);
            Singletons.objectDefinition(that, this, null);
        } else {
            //Don't even bother with nodes that have errors
            if (errVisitor.hasErrors(that))return;
            Class c = (Class) d.getTypeDeclaration();
            //Skip static objects
            if (d.isStatic())return;
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
        out("(function(){");
        try {
            final Tree.SatisfiedTypes sts = that.getSatisfiedTypes();
            final Tree.ExtendedType et = that.getExtendedType();
            Singletons.defineObject(that, null, sts == null ? null : TypeUtils.getTypes(sts.getTypes()),
                    et == null ? null : et.getType(), et == null ? null : et.getInvocationExpression(),
                    that.getClassBody(), null, this, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out("}())");
    }

    @Override
    public void visit(final Tree.MethodDeclaration that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        FunctionHelper.methodDeclaration(null, that, this, verboseStitcher);
    }

    /** Reads a file with hand-written snippet and outputs it to the current writer. */
    boolean stitchNative(final Declaration d, final Tree.Declaration n) {
        final VirtualFile f = compiler.getStitchedFile(d, ".js");
        if (f != null && f.exists()) {
            if (compiler.isCompilingLanguageModule()) {
                jsout.outputFile(f);
            }
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
            TypeUtils.encodeForRuntime(n, d, n.getAnnotationList(), this);
            endLine(true);
            return true;
        } else {
            if (!(d instanceof ClassOrInterface || n instanceof Tree.MethodDefinition
                    || (n instanceof Tree.MethodDeclaration 
                            && ((Tree.MethodDeclaration)n).getSpecifierExpression() != null)
                    || n instanceof Tree.AttributeGetterDefinition
                    || (n instanceof Tree.AttributeDeclaration
                            && ((Tree.AttributeDeclaration)n).getSpecifierOrInitializerExpression() != null))) {
                String missingDeclarationName = d.getName();
                if (missingDeclarationName == null && d instanceof Constructor) {
                    missingDeclarationName = "default constructor";
                }
                else {
                    missingDeclarationName = "'" + missingDeclarationName + "'";
                }
                final String err = 
                        "no native implementation for backend: native "
                        + missingDeclarationName 
                        + " is not implemented for the 'js' backend";
                n.addError(err, Backend.JavaScript);
                out("/*", err, "*/");
            }
            return false;
        }
    }

    /** Stitch a snippet of code to initialize type (usually a call to initTypeProto). */
    boolean stitchInitializer(TypeDeclaration d) {
        final VirtualFile f = compiler.getStitchedFile(d, "$init.js");
        if (f != null && f.exists()) {
            jsout.outputFile(f);
            return true;
        }
        return false;
    }

    boolean stitchConstructorHelper(final Tree.ClassOrInterface coi, final String partName) {
        final VirtualFile f = compiler.getStitchedConstructorFile(coi.getDeclarationModel(), partName);
        if (f != null && f.exists() && !f.isFolder()) {
            if (verboseStitcher) {
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
        final Function d = that.getDeclarationModel();
        if (!((opts.isOptimize() && d.isClassOrInterfaceMember()) ||
                TypeUtils.isNativeExternal(d) && compiler.isCompilingLanguageModule())) {
            comment(that);
            initDefaultedParameters(that.getParameterLists().get(0), that);
            FunctionHelper.methodDefinition(that, this, true, verboseStitcher);
            //Add reference to metamodel
            out(names.name(d), ".$crtmm$=");
            TypeUtils.encodeMethodForRuntime(that, this);
            endLine(true);
        }
    }

    /** Get the specifier expression for a Parameter, if one is available. */
    SpecifierOrInitializerExpression getDefaultExpression(final Tree.Parameter param) {
        final SpecifierOrInitializerExpression expr;
        if (param instanceof Tree.ParameterDeclaration || param instanceof Tree.InitializerParameter) {
            Tree.MethodDeclaration md = null;
            if (param instanceof Tree.ParameterDeclaration) {
                Tree.TypedDeclaration td = ((Tree.ParameterDeclaration) param).getTypedDeclaration();
                if (td instanceof AttributeDeclaration) {
                    expr = ((Tree.AttributeDeclaration) td).getSpecifierOrInitializerExpression();
                } else if (td instanceof Tree.MethodDeclaration) {
                    md = (Tree.MethodDeclaration)td;
                    expr = md.getSpecifierExpression();
                } else {
                    param.addUnexpectedError("Don't know what to do with TypedDeclaration " + td.getClass().getName(), Backend.JavaScript);
                    expr = null;
                }
            } else {
                expr = ((Tree.InitializerParameter) param).getSpecifierExpression();
            }
        } else {
            param.addUnexpectedError("Don't know what to do with defaulted/sequenced param " + param, Backend.JavaScript);
            expr = null;
        }
        return expr;
    }

    /** Create special functions with the expressions for defaulted parameters in a parameter list. */
    void initDefaultedParameters(final Tree.ParameterList params, Tree.AnyMethod container) {
        if (!(container instanceof Tree.MethodDeclaration || container.getDeclarationModel().isMember())) {
            return;
        }
        final boolean isMember = container.getDeclarationModel().isMember();
        for (final Tree.Parameter param : params.getParameters()) {
            Parameter pd = param.getParameterModel();
            if (pd.isDefaulted()) {
                final SpecifierOrInitializerExpression expr = getDefaultExpression(param);
                if (expr == null) {
                    continue;
                }
                if (isMember) {
                    qualify(params, container.getDeclarationModel());
                    out(names.name(container.getDeclarationModel()), "$defs$", pd.getName(), "=function");
                } else {
                    out("function ", names.name(container.getDeclarationModel()), "$defs$", pd.getName());
                }
                params.visit(this);
                out("{");
                initSelf(expr);
                out("return ");
                if (param instanceof Tree.ParameterDeclaration) {
                    Tree.TypedDeclaration node = ((Tree.ParameterDeclaration)param).getTypedDeclaration();
                    if (node instanceof Tree.MethodDeclaration) {
                        // function parameter defaulted using "=>"
                        FunctionHelper.singleExprFunction(
                                ((Tree.MethodDeclaration)node).getParameterLists(),
                                expr.getExpression(), null, true, true, this);
                    } else /*if (!isNaturalLiteral(expr.getExpression().getTerm()))*/ {
                        expr.visit(this);
                    }
                } else /*if (!isNaturalLiteral(expr.getExpression().getTerm()))*/ {
                    expr.visit(this);
                }
                out(";}");
                endLine(true);
            }
        }
    }

    /** Initialize the sequenced, defaulted and captured parameters in a type declaration.
     * @return The defaulted parameters that belong to a class, since their values have to be
     * set later on. */
    List<Tree.Parameter> initParameters(final Tree.ParameterList params, TypeDeclaration typeDecl, Functional m) {
        List<Tree.Parameter> rparams = null;
        for (final Tree.Parameter param : params.getParameters()) {
            Parameter pd = param.getParameterModel();
            String paramName = names.name(pd);
            if (pd.isDefaulted() || pd.isSequenced()) {
                out("if(", paramName, "===undefined){", paramName, "=");
                if (pd.isDefaulted()) {
                    boolean done = false;
                    if (m instanceof Function) {
                        Function mf = (Function)m;
                        if (mf.getRefinedDeclaration() != mf) {
                            mf = (Function)mf.getRefinedDeclaration();
                            if (mf.isMember() || !mf.isImplemented()) {
                                qualify(params, mf);
                                out(names.name(mf), "$defs$", pd.getName(), "(");
                                boolean firstParam=true;
                                for (Parameter p : m.getFirstParameterList().getParameters()) {
                                    if (firstParam){firstParam=false;}else out(",");
                                    out(names.name(p));
                                }
                                out(")");
                                done = true;
                            }
                        }
                    } else if (pd.getDeclaration() instanceof Class) {
                        Class cdec = (Class)pd.getDeclaration().getRefinedDeclaration();
                        out(TypeGenerator.pathToType(params, cdec, this),
                                ".$defs$", pd.getName(), "(", names.self(cdec));
                        for (Parameter p : ((Class)pd.getDeclaration()).getParameterList().getParameters()) {
                            if (!p.equals(pd)) {
                                out(",", names.name(p));
                            }
                        }
                        out(")");
                        if (rparams == null) {
                            rparams = new ArrayList<>(3);
                        }
                        rparams.add(param);
                        done = true;
                    }
                    if (!done) {
                        final SpecifierOrInitializerExpression expr = getDefaultExpression(param);
                        if (expr == null) {
                            param.addUnexpectedError("Default expression missing for " + pd.getName(), Backend.JavaScript);
                            out("undefined");
                        } else {
                            generateParameterExpression(param, expr,
                                    pd.getDeclaration() instanceof Scope? (Scope)pd.getDeclaration() : null);
                        }
                    }
                } else {
                    out(getClAlias(), "empty()");
                }
                out(";}");
                endLine();
            }
            if (typeDecl != null && !(typeDecl instanceof ClassAlias)
                    && (pd.getModel().isJsCaptured() || pd.getDeclaration() instanceof Class)) {
                out(names.self(typeDecl), ".", names.valueName(pd.getModel()), "=", paramName);
                if (!opts.isOptimize() && pd.isHidden()) { //belt and suspenders...
                    out(";", names.self(typeDecl), ".", paramName, "=", paramName);
                }
                endLine(true);
            }
        }
        return rparams;
    }

    void generateParameterExpression(Tree.Parameter param, SpecifierOrInitializerExpression expr,
            Scope m) {
        if (param instanceof Tree.ParameterDeclaration) {
            Tree.TypedDeclaration node = ((Tree.ParameterDeclaration)param).getTypedDeclaration();
            if (node instanceof Tree.MethodDeclaration) {
                // function parameter defaulted using "=>"
                FunctionHelper.singleExprFunction(
                        ((Tree.MethodDeclaration)node).getParameterLists(),
                        expr.getExpression(), m, false, true, this);
            } else {
                expr.visit(this);
            }
        } else {
            expr.visit(this);
        }
    }

    private void addMethodToPrototype(TypeDeclaration outer,
            final Tree.MethodDefinition that) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        Function d = that.getDeclarationModel();
        if (!opts.isOptimize() || !d.isClassOrInterfaceMember()) return;
        comment(that);
        initDefaultedParameters(that.getParameterLists().get(0), that);
        if (d.isStatic()) {
            out(names.name(outer), ".$st$.", names.name(d), "=");
        } else {
            out(names.self(outer), ".", names.name(d), "=");
        }
        FunctionHelper.methodDefinition(that, this, false, verboseStitcher);
        //Add reference to metamodel
        if (d.isStatic()) {
            out(names.name(outer), ".$st$.", names.name(d), ".$crtmm$=");
        } else {
            out(names.self(outer), ".", names.name(d), ".$crtmm$=");
        }
        TypeUtils.encodeMethodForRuntime(that, this);
        endLine(true);
    }

    @Override
    public void visit(final Tree.AttributeGetterDefinition that) {
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        Value d = that.getDeclarationModel();
        if (opts.isOptimize() && d.isClassOrInterfaceMember()) return;
        comment(that);
        if (AttributeGenerator.defineAsProperty(d)) {
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
            TypeUtils.encodeForRuntime(that, that.getDeclarationModel(), that.getAnnotationList(), this);
            if (setterDef != null) {
                out(",");
                TypeUtils.encodeForRuntime(setterDef, setterDef.getDeclarationModel(), setterDef.getAnnotationList(), this);
            }
            out(");");
        }
        else {
            out(function, names.getter(d, false), "()");
            if (TypeUtils.isNativeExternal(d)) {
                out("{");
                if (stitchNative(d, that)) {
                    if (verboseStitcher) {
                        spitOut("Stitching in native attribute " + d.getQualifiedNameString()
                            + ", ignoring Ceylon declaration");
                    }
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
            AttributeGenerator.generateAttributeMetamodel(that, true, false, this);
        }
    }

    private void addGetterToPrototype(TypeDeclaration outer,
            final Tree.AttributeGetterDefinition that) {
        Value d = that.getDeclarationModel();
        if (!opts.isOptimize() || !d.isClassOrInterfaceMember()) return;
        comment(that);
        defineAttribute(d.isStatic() ? names.name(outer)+".$st$" : names.self(outer),
                names.name(d));
        if (TypeUtils.isNativeExternal(d)) {
            out("{");
            if (stitchNative(d, that)) {
                if (verboseStitcher) {
                    spitOut("Stitching in native getter " + d.getQualifiedNameString() +
                        ", ignoring Ceylon declaration");
                }
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
        TypeUtils.encodeForRuntime(that, that.getDeclarationModel(), that.getAnnotationList(), this);
        if (setterDef != null) {
            out(",");
            TypeUtils.encodeForRuntime(setterDef, setterDef.getDeclarationModel(), setterDef.getAnnotationList(), this);
        }
        out(");");
    }
    
    Tree.AttributeSetterDefinition associatedSetterDefinition(
            final Value valueDecl) {
        final Setter setter = valueDecl.getSetter();
        if (setter != null && currentStatements != null) {
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
    boolean shareGetter(final FunctionOrValue d) {
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
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        Setter d = that.getDeclarationModel();
        if (opts.isOptimize() && d.isClassOrInterfaceMember() ||
                AttributeGenerator.defineAsProperty(d)) return;
        comment(that);
        out("function ", names.setter(d.getGetter()), "(", names.name(d.getParameter()), ")");
        AttributeGenerator.setter(that, this);
        if (!shareSetter(d)) { out(";"); }
        if (!d.isToplevel())outerSelf(d);
        out(names.setter(d.getGetter()), ".$crtmm$=");
        TypeUtils.encodeForRuntime(that, that.getDeclarationModel(), that.getAnnotationList(), this);
        endLine(true);
        AttributeGenerator.generateAttributeMetamodel(that, false, true, this);
    }

    boolean isCaptured(Declaration d) {
        if (d.isToplevel()||d.isClassOrInterfaceMember()) {
            if (d.isShared() || d.isJsCaptured()) {
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

    boolean shareSetter(final FunctionOrValue d) {
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

    void addGeneratedAttribute(Declaration d) {
        generatedAttributes.add(d);
    }
    boolean isGeneratedAttribute(Declaration d) {
        return generatedAttributes.contains(d);
    }

    @Override
    public void visit(final Tree.AttributeDeclaration that) {
        if (errVisitor.hasErrors(that) || !TypeUtils.acceptNative(that))return;
        final Value d = that.getDeclarationModel();
        //Check if the attribute corresponds to a class parameter
        //This is because of the new initializer syntax
        final Parameter param = d.isParameter() ? ((Functional)d.getContainer()).getParameter(d.getName()) : null;
        final boolean asprop = AttributeGenerator.defineAsProperty(d);
        if (d.isFormal()) {
            if (!opts.isOptimize()) {
                comment(that);
                AttributeGenerator.generateAttributeMetamodel(that, false, false, this);
            }
        } else if (!d.isStatic()) {
            SpecifierOrInitializerExpression specInitExpr =
                        that.getSpecifierOrInitializerExpression();
            final boolean addGetter = specInitExpr != null || param != null || !d.isMember()
                    || d.isVariable() || d.isLate();
            boolean setterGend=false;
            if (opts.isOptimize() && d.isClassOrInterfaceMember()) {
                //Stitch native member attribute declaration with no value
                final boolean eagerExpr = specInitExpr != null
                        && !(specInitExpr instanceof LazySpecifierExpression); 
                if (eagerExpr && !TypeUtils.isNativeExternal(d)) {
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
                if (TypeUtils.isNativeExternal(d)) {
                    if (stitchNative(d, that)) {
                        if (verboseStitcher) {
                            spitOut("Stitching in native attribute " + d.getQualifiedNameString() +
                                ", ignoring Ceylon declaration");
                        }
                        genatr=false;
                        out(";};");
                    }
                }
                if (genatr) {
                    out("return ");
                    //if (!isNaturalLiteral(specInitExpr.getExpression().getTerm())) {
                        visitSingleExpression(specInitExpr.getExpression());
                    //}
                    out("}");
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
                        TypeUtils.encodeForRuntime(that, that.getDeclarationModel(), that.getAnnotationList(), this);
                        if (setterDef != null) {
                            out(",");
                            TypeUtils.encodeForRuntime(setterDef, setterDef.getDeclarationModel(),
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
            else if (!(d.isParameter() && d.getContainer() instanceof Function)) {
                if (addGetter) {
                    AttributeGenerator.generateAttributeGetter(that, d, specInitExpr,
                            names.name(param), this, directAccess, verboseStitcher);
                }
                if ((d.isVariable() || d.isLate()) && !asprop) {
                    setterGend=AttributeGenerator.generateAttributeSetter(that, d, this);
                }
            }
            boolean addMeta=!opts.isOptimize() || d.isToplevel();
            if (!d.isToplevel()) {
                addMeta |= ModelUtil.getContainingDeclaration(d).isAnonymous();
            }
            if (addMeta) {
                AttributeGenerator.generateAttributeMetamodel(that, addGetter, setterGend, this);
            }
        }
    }

    void generateUnitializedAttributeReadCheck(String privname, String pubname,
                                               Tree.SpecifierOrInitializerExpression expr) {
        //TODO we can later optimize this, to replace this getter with the plain one
        //once the value has been defined
        out("if(", privname, "===undefined)");
        if (expr == null) {
            out("throw ", getClAlias(),
                    "InitializationError('Attempt to read uninitialized attribute \\u00ab", pubname, "\\u00bb');");
        } else {
            if (new NeedsThisVisitor(expr).needsThisReference()) {
                out("{");
                initSelf(expr);
                out(privname, "=");
            } else {
                out("{",privname,"=");
            }
            expr.visit(this);
            out(";}");
        }
    }
    void generateImmutableAttributeReassignmentCheck(FunctionOrValue decl, String privname, String pubname) {
        if (decl.isLate() && !decl.isVariable()) {
            out("if(", privname, "!==undefined)throw ", getClAlias(),
                    "InitializationError('Attempt to reassign immutable attribute \\u00ab", pubname, "\\u00bb');");
        }
    }

    @Override
    public void visit(final Tree.CharLiteral that) {
        out(getClAlias(), "c$(");
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
        List<Tree.StringLiteral> literals = that.getStringLiterals();
        List<Expression> exprs = that.getExpressions();
        out("(");
        for (int i = 0; i < literals.size(); i++) {
            Tree.StringLiteral literal = literals.get(i);
            boolean skip = (i==0 || i==literals.size()-1) 
            		&& literal.getText().isEmpty();
            if (i>0 && !skip) {
                out("+");
            }
            if (!skip) {
                literal.visit(this);
            }
//            if (i>0 && !skip) {
//                out(")");
//            }
            if (i < exprs.size()) {
                if (!skip) {
                    out("+");
                }
                final Expression expr = exprs.get(i);
                final Type t = expr.getTypeModel();
                box(expr);
                if (t == null || t.isUnknown()) {
                    out(".toString()");
                } else if (!t.isString()) {
                    out(".string");
                }
            }
        }
        out(")");
    }

	String parseFloatLiteral(final Tree.FloatLiteral that, boolean neg) {
		final String f = that.getText();
//        final int dot = f.indexOf('.');
//        boolean wrap = true;
        double parsed=Double.parseDouble(f);
        if (parsed == 0.0 && !f.equals("0.0")) {
            that.addUsageWarning(Warning.zeroFloatLiteral, 
            		"literal so small it is indistinguishable from zero: '" + f + "' (use 0.0)");
        }
//        if (f.indexOf('E', dot) < 0 && f.indexOf('e', dot) < 0) {
//            for (int i = dot+1; i < f.length(); i++) {
//                if (f.charAt(i) != '0') {
//                    wrap = false;
//                    break;
//                }
//            }
//        }
//        if (wrap) {
//            out(getClAlias(), "Float");
//        }
        return neg ? "-"+f : f;
	}

    long parseNaturalLiteral(Tree.NaturalLiteral that, boolean neg) throws NumberFormatException {
        String nt = that.getText();
        char prefix = nt.charAt(0);
        int radix;
        switch (prefix) {
	        case '$': radix = 2; nt = nt.substring(1); break;
	        case '#': radix = 16; nt = nt.substring(1); break;
	        default: radix = 10;
        }

        BigInteger lit = new java.math.BigInteger(nt, radix);
        if (neg) {
            lit = lit.negate();
        }
        if (radix == 10) {
            if (lit.compareTo(maxLong) > 0 || lit.compareTo(minLong) < 0) {
                that.addError("literal outside representable range: " + lit
                        + " is too large to be represented as an Integer", Backend.JavaScript);
                return 0;
            }
        } else {
            if ((neg?lit.negate():lit).compareTo(maxUnsignedLong) == 0) {
                return neg?1:-1;
            } else if ((neg?lit.negate():lit).compareTo(maxUnsignedLong) > 0) {
                that.addError("invalid hexadecimal literal: '" + (radix==2?"$":"#") + nt + "' has more than 64 bits", Backend.JavaScript);
                return 0;
            }
        }
        return lit.longValue();
    }

    @Override
    public void visit(final Tree.FloatLiteral that) {
        out(parseFloatLiteral(that, false));
    }

    @Override
    public void visit(final Tree.NaturalLiteral that) {
        try {
            out(Long.toString(parseNaturalLiteral(that, false)));
        } catch (NumberFormatException ex) {
            that.addError("Invalid numeric literal " + that.getText(), Backend.JavaScript);
        }
    }

    @Override
    public void visit(final Tree.This that) {
        out(names.self(ModelUtil.getContainingClassOrInterface(that.getScope())));
    }

    @Override
    public void visit(final Tree.Super that) {
        out(names.self(ModelUtil.getContainingClassOrInterface(that.getScope())));
    }

    @Override
    public void visit(final Tree.Outer that) {
        boolean outer = false;
        if (opts.isOptimize()) {
            Scope scope = that.getScope();
            while (scope != null && !(scope instanceof TypeDeclaration)) {
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
        BmeGenerator.generateBme(that, this);
    }

    /** Tells whether a declaration can be accessed directly (using its name) or
     * through its getter. */
    boolean accessDirectly(Declaration d) {
        return directAccess.contains(d) 
            || !accessThroughGetter(d) 
            || d.isParameter();
    }

    private boolean accessThroughGetter(Declaration d) {
        return d instanceof FunctionOrValue 
            && !(d instanceof Function)
            && !AttributeGenerator.defineAsProperty(d)
            && !d.isDynamic();
    }
    
    void supervisit(final Tree.QualifiedMemberOrTypeExpression that) {
        super.visit(that);
    }

    @Override
    public void visit(final Tree.QualifiedMemberExpression that) {
        if (errVisitor.hasErrors(that))return;
        //Big TODO: make sure the member is actually
        //          refined by the current class!
        final Declaration d = that.getDeclaration();
        if (that.getMemberOperator() instanceof Tree.SafeMemberOp) {
            Operators.generateSafeOp(that, this);
        } else if (that.getMemberOperator() instanceof Tree.SpreadOp) {
            SequenceGenerator.generateSpread(that, this);
        } else if (d instanceof Function && that.getSignature() == null) {
            //TODO right now this causes that all method invocations are done this way
            //we need to filter somehow to only use this pattern when the result is supposed to be a callable
            //looks like checking for signature is a good way (not THE way though; named arg calls don't have signature)
            FunctionHelper.generateCallable(that, null, this);
        } else if (that.getStaticMethodReference() && d!=null) {
            if (d instanceof Value && ModelUtil.isConstructor(d)) {
                Constructor cnst = (Constructor)((Value)d).getTypeDeclaration();
                if (cnst.getTypescriptEnum() != null && cnst.getTypescriptEnum().matches("[0-9.-]+")) {
                    out(cnst.getTypescriptEnum());
                } else {
                    //TODO this won't work anymore I think
                    boolean wrap = false;
                    if (that.getPrimary() instanceof Tree.QualifiedMemberOrTypeExpression) {
                        Tree.QualifiedMemberOrTypeExpression prim = (Tree.QualifiedMemberOrTypeExpression)that.getPrimary();
                        if (prim.getStaticMethodReference()) {
                            wrap=true;
                            out("function(_$){return _$");
                        } else {
                            prim.getPrimary().visit(this);
                        }
                        out(".");
                    } else {
                        if (d.getContainer() instanceof Declaration) {
                            qualify(that.getPrimary(), (Declaration)d.getContainer());
                        } else if (d.getContainer() instanceof Package) {
                            out(names.moduleAlias(((Package)d.getContainer()).getModule()));
                        }
                    }
                    if (cnst.getTypescriptEnum() != null) {
                        out(names.name((TypeDeclaration)d.getContainer()), ".", cnst.getTypescriptEnum());
                    } else {
                        out(names.name((TypeDeclaration)d.getContainer()), names.constructorSeparator(d),
                            names.name(d), "()");
                    }
                    if (wrap) {
                        out(";}");
                    }
                }
            } else if (d instanceof Function) {
                Function fd = (Function)d;
                if (fd.getTypeDeclaration() instanceof Constructor) {
                    that.getPrimary().visit(this);
                    out(names.constructorSeparator(fd), names.name(fd.getTypeDeclaration()));
                } else if (fd.isStatic()) {
                    BmeGenerator.generateStaticReference(that, fd, this);
                } else {
                    out("function($O$){return ");
                    if (BmeGenerator.hasTypeParameters(that)) {
                        BmeGenerator.printGenericMethodReference(this, that, "$O$", "$O$."+names.name(d));
                    } else {
                        out(getClAlias(), "f3$($O$,$O$.", names.name(d), ")");
                    }
                    out(";}");
                }
            } else {
                if (d.isStatic()) {
                    BmeGenerator.generateStaticReference(that, d, this);
                } else {
                    out("function($O$){return $O$.", names.name(d), ";}");
                }
            }
        } else {
            final boolean isDynamic = that.getPrimary() instanceof Tree.Dynamic;
            String lhs = generateToString(new GenerateCallback() {
                @Override public void generateValue() {
                    if (isDynamic) {
                        out("(");
                    }
                    box(that.getPrimary());
                    if (isDynamic) {
                        out(")");
                    }
                }
            });
            if (d != null && d.isStatic()) {
                BmeGenerator.generateStaticReference(that, d, this);
            } else {
                out(memberAccess(that, lhs));
            }
        }
    }

    /**
     * Checks if the given node is a MemberOrTypeExpression or QualifiedType which
     * represents an access to a supertype member and returns the scope of that
     * member or null.
     */
    Scope getSuperMemberScope(Node node) {
        Scope scope = null;
        if (node instanceof Tree.QualifiedMemberOrTypeExpression) {
            // Check for "super.member"
            Tree.QualifiedMemberOrTypeExpression qmte = (Tree.QualifiedMemberOrTypeExpression) node;
            final Term primary = eliminateParensAndWidening(qmte.getPrimary());
            if (primary instanceof Tree.Super) {
                scope = qmte.getDeclaration().getContainer();
            }
        }
        else if (node instanceof Tree.QualifiedType) {
            // Check for super.Membertype
            Tree.QualifiedType qtype = (Tree.QualifiedType) node;
            if (qtype.getOuterType() instanceof Tree.SuperType) { 
                scope = qtype.getDeclarationModel().getContainer();
            }
        }
        return scope;
    }

    String getMember(Node node, String lhs) {
        final StringBuilder sb = new StringBuilder();
        if (lhs != null && !lhs.isEmpty()) {
            sb.append(lhs);
        }
        else if (node instanceof Tree.BaseMemberOrTypeExpression) {
            Tree.BaseMemberOrTypeExpression bmte = (Tree.BaseMemberOrTypeExpression) node;
            Declaration bmd = bmte.getDeclaration();
            if (bmd.isParameter() && bmd.getContainer() instanceof ClassAlias) {
                return names.name(bmd);
            }
            String path = qualifiedPath(node, bmd);
            if (!path.isEmpty()) {
                sb.append(path);
            }
        }
        return sb.toString();
    }

    String memberAccessBase(Node node, Declaration decl, boolean setter,
                String lhs) {
        final StringBuilder sb = new StringBuilder(getMember(node, lhs));
        final boolean isConstructor = decl instanceof Constructor;
        if (sb.length() > 0) {
            if (node instanceof Tree.BaseMemberOrTypeExpression) {
                Declaration bmd = ((Tree.BaseMemberOrTypeExpression)node).getDeclaration();
                if (bmd.isParameter() && bmd.getContainer() instanceof ClassAlias) {
                    return sb.toString();
                }
            }
            sb.append(isConstructor ? names.constructorSeparator(decl) : ".");
        }
        boolean metaGetter = false;
        Scope scope = getSuperMemberScope(node);
        if (opts.isOptimize() && scope != null && !isConstructor) {
            sb.append("getT$all()['")
                .append(scope.getQualifiedNameString())
                .append("']");
            if (AttributeGenerator.defineAsProperty(decl)) {
                return getClAlias() + (setter ? "attrSetter(" : "attrGetter(")
                        + sb.toString() + ",'" + names.name(decl) + "')";
            }
            sb.append(".$$.prototype.");
            metaGetter = true;
        }
        final String member = accessThroughGetter(decl) && !accessDirectly(decl)
                ? (setter ? names.setter(decl) : names.getter(decl, metaGetter)) : names.name(decl);
        if (!isConstructor && ModelUtil.isConstructor(decl)) {
            sb.append(names.name((Declaration)decl.getContainer())).append(names.constructorSeparator(decl));
        }
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
        if (decl == null && isInDynamicBlock()) {
            plainName = expr.getIdentifier().getText();
        }
        else if (TypeUtils.isNativeJs(decl)) {
            if (decl==null) {
                expr.addUnexpectedError("Expression with no declaration outside of dynamic block");
                return "(throw new TypeError('<NULL>'))";
            }
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            return ((lhs != null) && (lhs.length() > 0))
                    ? (lhs + "." + plainName) : plainName;
        }
        boolean protoCall = opts.isOptimize() && (getSuperMemberScope(expr) != null);
        if (accessDirectly(decl) && !(protoCall && AttributeGenerator.defineAsProperty(decl))) {
            // direct access, without getter
            return memberAccessBase(expr, decl, false, lhs);
        }
        // access through getter
        return memberAccessBase(expr, decl, false, lhs)
                + (protoCall ? ".call(this)" : "()");
    }
    
    @Override
    public void visit(final Tree.BaseTypeExpression that) {
        BmeGenerator.generateBte(that, this, false);
    }

    @Override
    public void visit(final Tree.QualifiedTypeExpression that) {
        BmeGenerator.generateQte(that, this);
    }

    public void visit(final Tree.Dynamic that) {
        Tree.NamedArgumentList argList = that.getNamedArgumentList();
        if (argList == null) {
            out("[]");
        } else if (argList.getSequencedArgument()==null &&
                argList.getNamedArguments().isEmpty()) {
            out("{}");
        } else {
            invoker.nativeObject(argList);
        }
    }

    @Override
    public void visit(final Tree.InvocationExpression that) {
        invoker.generateInvocation(that);
    }

    @Override
    public void visit(final Tree.PositionalArgumentList that) {
        invoker.generatePositionalArguments(null, that, that.getPositionalArguments(), false, false);
    }

    void box(final Tree.Term term) {
    	box(term, true);
    }
    
    /** Box a term, visit it, unbox it. */
    void box(final Tree.Term term, boolean wrapFloat) {
        final int t = boxUnboxStart(term, false, wrapFloat);
        term.visit(this);
        if (t == 4) {
            final Type ct = term.getTypeModel();
            out(",");
            TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(term, ct, this);
            out(",");
            TypeUtils.printTypeArguments(term, ct.getTypeArguments(), this, true, null);
        }
        boxUnboxEnd(t);
    }

    // Make sure fromTerm is compatible with toTerm by boxing it when necessary
    int boxStart(final Tree.Term fromTerm) {
        return boxUnboxStart(fromTerm, false, true);
    }
    // Make sure fromTerm is compatible with toTerm by boxing or unboxing it when necessary
    int boxUnboxStart(final Tree.Term fromTerm, final Tree.Term toTerm) {
        return boxUnboxStart(fromTerm, TypeUtils.isNativeJs(toTerm), true);
    }

    // Make sure fromTerm is compatible with toDecl by boxing or unboxing it when necessary
    int boxUnboxStart(final Tree.Term fromTerm, TypedDeclaration toDecl) {
        return boxUnboxStart(fromTerm, TypeUtils.isNativeJs(toDecl), true);
    }

    private int boxUnboxStart(final Tree.Term fromTerm, 
    		boolean toNative, boolean wrapFloatCheckInt) {
        // Box the value
        final Type fromType = fromTerm.getTypeModel();
        if (fromType == null) {
            return 0;
        }
        
        if (wrapFloatCheckInt && !toNative) {
	    	if (fromType.isFloat()) {
	        	if (isFloatLiteral(fromTerm)) {
        			out(getClAlias(), "f$(");
	        		return 1;
	        	} else if (isFloatOperatorExpression(fromTerm)) {
        			out(getClAlias(), "f$");
	        		return 0;
	        	}
	        }
	        if (fromType.isInteger()) {
	        	if (isIntegerLiteral(fromTerm)) {
	        		//we can't write 123.method
	        		out("(");
	        		return 1;
	        	}
	        }
        }
        
        final boolean fromNative = TypeUtils.isNativeJs(fromTerm);
        if (fromNative != toNative || fromType.isCallable()) {
            if (fromNative) {
                // conversion from native value to Ceylon value
                if (fromType.isInteger()) {
                    out("i$(");
                } else if (fromType.isFloat()) {
                    out("f$(");
                } else if (fromType.isBoolean()) {
                    out("(");
                } else if (fromType.isCharacter()) {
                    out(getClAlias(), "c$(");
                } else if (fromType.isCallable()) {
                    out(getClAlias(), "f2$(");
                    return 4;
                } else {
                    return 0;
                }
                return 1;
            } else if (fromType.isFloat()) {
                // conversion from Ceylon Float to native value
                return toNative ? 2 : 1;
            } else if (fromType.isCallable()) {
                Term t = fromTerm;
                if (t instanceof Tree.InvocationExpression) {
                    t = ((Tree.InvocationExpression)t).getPrimary();
                }
                //Don't box callables if they're not members or anonymous
                if (t instanceof Tree.MemberOrTypeExpression) {
                    final Declaration d = ((Tree.MemberOrTypeExpression)t).getDeclaration();
                    if (d != null && !(d.isClassOrInterfaceMember() || d.isAnonymous())) {
                        return 0;
                    }
                }
                out(getClAlias(), "f2$(");
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
        case 2: out(".valueOf(/*UNFLOAT*/)"); break;
        case 4: out(")"); break;
        default: //nothing
        }
    }

	private boolean isFloatOperatorExpression(Tree.Term fromTerm) {
        Tree.Term term = unwrapExpressionUntilTerm(fromTerm);
		return term instanceof Tree.ArithmeticOp
				&& !(term instanceof Tree.PowerOp)
				|| term instanceof Tree.NegativeOp;
	}

	private boolean isFloatLiteral(Tree.Term fromTerm) {
		Tree.Term term = unwrapExpressionUntilTerm(fromTerm);
		return term instanceof Tree.FloatLiteral
				|| term instanceof Tree.NegativeOp 
				&& isFloatLiteral(((Tree.NegativeOp)term).getTerm())
				|| term instanceof Tree.PositiveOp 
				&& isFloatLiteral(((Tree.PositiveOp)term).getTerm());
	}

	private boolean isIntegerLiteral(Tree.Term fromTerm) {
		Tree.Term term = unwrapExpressionUntilTerm(fromTerm);
		return term instanceof Tree.NaturalLiteral
				|| term instanceof Tree.NegativeOp 
				&& isFloatLiteral(((Tree.NegativeOp)term).getTerm())
				|| term instanceof Tree.PositiveOp 
				&& isFloatLiteral(((Tree.PositiveOp)term).getTerm());
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
        if (!(opts.isOptimize() 
                && that.getSpecifierExpression() instanceof LazySpecifierExpression
                && that.getScope().getContainer() instanceof TypeDeclaration)) {
            specifierStatement(null, that);
        }
    }

    private void assignment(final TypeDeclaration outer, final Declaration d, final Tree.Expression expr) {
        FunctionOrValue vdec = (FunctionOrValue)d;
        final String atname = names.valueName(vdec);
        if (outer instanceof Constructor) {
            if (d.isClassOrInterfaceMember()) {
                out(names.self(outer), ".", atname, "=");
            } else {
                out(names.name(d), "=");
            }
            expr.visit(this);
            endLine(true);
        }
        if (d.isClassOrInterfaceMember()) {
            defineAttribute(names.self(outer), names.name(d));
            out("{");
            if (vdec.isLate()) {
                generateUnitializedAttributeReadCheck("this."+atname, names.name(d), null);
            }
            out("return this.", atname, ";}");
            if (vdec.isVariable() || vdec.isLate()) {
                final String par = getNames().createTempVariable();
                out(",function(", par, "){");
                generateImmutableAttributeReassignmentCheck(vdec, "this."+atname, names.name(d));
                out("return this.", atname, "=", par, ";}");
            } else {
                out(",undefined");
            }
            out(",");
            TypeUtils.encodeForRuntime(expr, d, this);
            out(")");
            endLine(true);
        }
    }

    private void specifierStatement(final TypeDeclaration outer,
            final Tree.SpecifierStatement specStmt) {
        final Tree.Expression expr = specStmt.getSpecifierExpression().getExpression();
        final Tree.Term term = specStmt.getBaseMemberExpression();
        final Tree.StaticMemberOrTypeExpression smte = term instanceof Tree.StaticMemberOrTypeExpression
                ? (Tree.StaticMemberOrTypeExpression)term : null;
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(term.getTypeModel())) {
            if (smte != null && smte.getDeclaration() == null) {
                out(smte.getIdentifier().getText());
            } else {
                term.visit(this);
                if (term instanceof BaseMemberExpression) {
                    Declaration dec = ((BaseMemberExpression)term).getDeclaration();
                    if (dec instanceof Value) {
                        Value v = (Value)dec;
                        if (v.isMember()) {
                            //Assignment to dynamic member
                            out("_");
                        }
                    }
                }
            }
            out("=");
            int box = boxUnboxStart(expr, term);
            expr.visit(this);
            if (box == 4) out("/*TODO: callable targs 6.1*/");
            boxUnboxEnd(box);
            out(";");
            return;
        }
        if (smte != null) {
            final Declaration bmeDecl = smte.getDeclaration();
            if (specStmt.getSpecifierExpression() instanceof LazySpecifierExpression) {
                // attr => expr;
                final boolean property = AttributeGenerator.defineAsProperty(bmeDecl);
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
                //if (!isNaturalLiteral(specStmt.getSpecifierExpression().getExpression().getTerm())) {
                    specStmt.getSpecifierExpression().visit(this);
                //}
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
                if (outer instanceof Constructor 
                        || bmeDecl.isMember() && bmeDecl instanceof Value && bmeDecl.isActual()) {
                    assignment(outer, bmeDecl, expr);
                }
            }
            else if (bmeDecl instanceof FunctionOrValue) {
                // "attr = expr;" in an initializer or method
                final FunctionOrValue moval = (FunctionOrValue)bmeDecl;
                if (moval.isVariable() || moval.isLate()) {
                    // simple assignment to a variable attribute
                    BmeGenerator.generateMemberAccess(smte, new GenerateCallback() {
                        @Override public void generateValue() {
                            int boxType = boxUnboxStart(expr.getTerm(), moval);
                            if (isInDynamicBlock() 
                                    && !ModelUtil.isTypeUnknown(moval.getType())
                                    && ModelUtil.isTypeUnknown(expr.getTypeModel())) {
                                TypeUtils.generateDynamicCheck(expr, moval.getType(), GenerateJsVisitor.this, false,
                                        expr.getTypeModel().getTypeArguments());
                            } else {
                                expr.visit(GenerateJsVisitor.this);
                            }
                            if (boxType == 4) {
                                out(",");
                                if (moval instanceof Function) {
                                    //Add parameters
                                    TypeUtils.encodeParameterListForRuntime(true, specStmt,
                                            ((Function)moval).getFirstParameterList(), GenerateJsVisitor.this);
                                    out(",");
                                } else {
                                    //TODO extract parameters from Value
                                    final Type ps = moval.getUnit().getCallableTuple(moval.getType());
                                    if (ps == null || ps.isSubtypeOf(moval.getUnit().getEmptyType())) {
                                        out("[],");
                                    } else {
                                        out("[/*VALUE Callable params ", ps.asString()+"*/],");
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
                    if (moval instanceof Function) {
                        //same as fat arrow
                        qualify(specStmt, bmeDecl);
                        if (expr.getTerm() instanceof Tree.FunctionArgument) {
                            ((Tree.FunctionArgument)expr.getTerm()).getDeclarationModel().setRefinedDeclaration(moval);
                            out(names.name(moval), "=");
                            specStmt.getSpecifierExpression().visit(this);
                            out(";");
                        } else {
                            out(names.name(moval), "=function ", names.name(moval), "(");
                            //Build the parameter list, we'll use it several times
                            final StringBuilder paramNames = new StringBuilder();
                            final List<Parameter> params = ((Function) moval).getFirstParameterList().getParameters();
                            for (Parameter p : params) {
                                if (paramNames.length() > 0) paramNames.append(",");
                                paramNames.append(names.name(p));
                            }
                            out(paramNames.toString());
                            out("){");
                            for (Parameter p : params) {
                                if (p.isDefaulted()) {
                                    out("if(", names.name(p), "===undefined)", names.name(p),"=");
                                    qualify(specStmt, moval);
                                    out(names.name(moval), "$defs$", p.getName(), "(", paramNames.toString(), ")");
                                    endLine(true);
                                }
                            }
                            out("return ");
                            //if (!isNaturalLiteral(specStmt.getSpecifierExpression().getExpression().getTerm())) {
                                specStmt.getSpecifierExpression().visit(this);
                            //}
                            out("(", paramNames.toString(), ");}");
                            endLine(true);
                        }
                    } else {
                        // Specifier for a member attribute. This actually defines the
                        // member (e.g. in shortcut refinement syntax the attribute
                        // declaration itself can be omitted), so generate the attribute.
                        if (opts.isOptimize()) {
                            //#451
                            out(names.self(ModelUtil.getContainingClassOrInterface(moval.getScope())), ".",
                                    names.valueName(moval), "=");
                            specStmt.getSpecifierExpression().visit(this);
                            endLine(true);
                        } else {
                            AttributeGenerator.generateAttributeGetter(null, moval,
                                    specStmt.getSpecifierExpression(), null, this, directAccess, verboseStitcher);
                        }
                    }
                } else {
                    // Specifier for some other attribute, or for a method.
                    if (opts.isOptimize() 
                            || bmeDecl.isMember() && bmeDecl instanceof Function) {
                        qualify(specStmt, bmeDecl);
                    }
                    out(names.name(bmeDecl), "=");
                    if (isInDynamicBlock() && ModelUtil.isTypeUnknown(expr.getTypeModel())
                            && !ModelUtil.isTypeUnknown(((FunctionOrValue) bmeDecl).getType())) {
                        TypeUtils.generateDynamicCheck(expr, ((FunctionOrValue) bmeDecl).getType(), this, false,
                                expr.getTypeModel().getTypeArguments());
                    } else {
                        if (expr.getTerm() instanceof Tree.FunctionArgument) {
                            Function fun = ((Tree.FunctionArgument) expr.getTerm()).getDeclarationModel();
                            if (fun.isAnonymous()) {
                                fun.setRefinedDeclaration(moval);
                            }
                        }
                        specStmt.getSpecifierExpression().visit(this);
                    }
                    out(";");
                }
            }
        }
        else if ((term instanceof Tree.ParameterizedExpression)
                && (specStmt.getSpecifierExpression() != null)) {
            final Tree.ParameterizedExpression paramExpr = (Tree.ParameterizedExpression)term;
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
    public void visit(final Tree.AssignOp that) {
        if (errVisitor.hasErrors(that))return;
        String returnValue = null;
        StaticMemberOrTypeExpression lhsExpr = null;
        final boolean leftDynamic = isInDynamicBlock() &&
                ModelUtil.isTypeUnknown(that.getLeftTerm().getTypeModel());
        if (that.getLeftTerm() instanceof Tree.IndexExpression) {
            Tree.IndexExpression iex = (Tree.IndexExpression)that.getLeftTerm();
            if (leftDynamic) {
                iex.getPrimary().visit(this);
                out("[");
                ((Tree.Element)iex.getElementOrRange()).getExpression().visit(this);
                out("]=");
                that.getRightTerm().visit(this);
            } else {
                final String tv = createRetainedTempVar();
                out("(", tv, "=");
                that.getRightTerm().visit(this);
                out(",");
                iex.getPrimary().visit(this);
                TypeDeclaration td = iex.getPrimary().getTypeModel().getDeclaration();
                if (td != null && td.inherits(iex.getUnit().getKeyedCorrespondenceMutatorDeclaration())) {
                    out(".put(");
                } else {
                    out(".set(");
                }
                ((Tree.Element)iex.getElementOrRange()).getExpression().visit(this);
                out(",", tv, "), ", tv, ")");
            }
            return;
        }
        if (leftDynamic) {
            that.getLeftTerm().visit(this);
            out("=");
            int box = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
            that.getRightTerm().visit(this);
            if (box == 4) out("/*TODO: callable targs 6.2*/");
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
            } else if (qme.getPrimary() instanceof Tree.Package == false) {
                super.visit(qme);
                out(".");
                if (qme.getDeclaration() != null && qme.getDeclaration().isStatic()) {
                    out("$st$.");
                }
            }
        }
        
        BmeGenerator.generateMemberAccess(lhsExpr, new GenerateCallback() {
            @Override public void generateValue() {
                //if (!isNaturalLiteral(that.getRightTerm())) {
                    int boxType = boxUnboxStart(that.getRightTerm(), that.getLeftTerm());
                    that.getRightTerm().visit(GenerateJsVisitor.this);
                    if (boxType == 4) out("/*TODO: callable targs 7*/");
                    boxUnboxEnd(boxType);
                //}
            }
        }, null, this);
        
        if (returnValue != null) { out(",", returnValue); }
        out(")");
    }

    /** Outputs the module name for the specified declaration. Returns true if something was output. */
    public boolean qualify(final Node that, final Declaration d) {
        String path = qualifiedPath(that, d);
        if (path.length() > 0) {
            out(path, d instanceof Constructor ? names.constructorSeparator(d) : ".");
        }
        return path.length() > 0;
    }

    String qualifiedPath(final Node that, final Declaration d) {
        return qualifiedPath(that, d, false);
    }

    public String qualifiedPath(final Node that, final Declaration d, final boolean inProto) {
        if (d instanceof Constructor) {
            Class c = (Class)d.getContainer();
            final String rval = qualifiedPath(that, c, inProto);
            return rval.isEmpty() ? names.name(c) : rval + "." + names.name(c);
        }
        final boolean isMember = d.isClassOrInterfaceMember();
        final boolean imported = isImported(that == null ? null : that.getUnit().getPackage(), d);
        if (!isMember && imported) {
            return names.moduleAlias(d.getUnit().getPackage().getModule());
        }
        else if (opts.isOptimize() && !inProto) {
            if (isMember && !(d.isParameter() && !d.isJsCaptured())) {
                TypeDeclaration id = that.getScope().getInheritingDeclaration(d);
                TypeDeclaration nd = null;
                if (id == null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    id = (TypeDeclaration) d.getContainer();
                    if (id.isNativeHeader()) {
                        nd = (TypeDeclaration)ModelUtil.getNativeDeclaration(id, Backend.JavaScript);
                    }
                }
                Scope scope = ModelUtil.getRealScope(that.getScope());
                if (scope instanceof Value && !(ModelUtil.getRealScope(scope) instanceof ClassOrInterface)) {
                    scope = ModelUtil.getRealScope(scope.getContainer());
                }
                if ((scope != null) 
                        && (that instanceof Tree.ClassDeclaration
                         || that instanceof Tree.InterfaceDeclaration 
                         || that instanceof Tree.Constructor)) {
                    // class/interface aliases have no own "this"
                    scope = scope.getContainer();
                }
                final StringBuilder path = new StringBuilder();
                final Declaration innermostDeclaration = ModelUtil.getContainingDeclarationOfScope(scope);
                while (scope != null) {
                    if (scope instanceof Constructor
                            && scope == innermostDeclaration) {
                        TypeDeclaration consCont = (TypeDeclaration)scope.getContainer();
                        if (that instanceof Tree.BaseTypeExpression) {
                            path.append(names.name(consCont));
                        } else if (d.isStatic()) {
                            path.append(names.name(consCont)).append(".$st$");
                        } else {
                            path.append(names.self(consCont));
                        }
                        if (scope == id || (nd != null && scope == nd)) {
                            break;
                        }
                        scope = consCont;
                    } else if (scope instanceof TypeDeclaration) {
                        if (path.length() > 0) {
                            if (scope instanceof Constructor==false) {
                                Constructor constr = scope instanceof Class ? ((Class)scope).getDefaultConstructor() : null;
                                if ((constr == null || !ModelUtil.contains(constr, (Scope)innermostDeclaration)) && !d.isStatic()) {
                                    path.append(".outer$");
                                }
                            }
                        } else if (d instanceof Constructor && ModelUtil.getContainingDeclaration(d) == scope) {
                            if (!d.getName().equals(((TypeDeclaration)scope).getName())) {
                                if (path.length()>0) {
                                    path.append('.');
                                }
                                path.append(names.name((TypeDeclaration) scope));
                            }
                        } else {
                            if (path.length()>0) {
                                path.append('.');
                            }
                            if (d.isStatic()) {
                                if (d instanceof TypedDeclaration) {
                                    TypedDeclaration orig = ((TypedDeclaration) d).getOriginalDeclaration();
                                    path.append(names.name((ClassOrInterface) (orig == null ? d : orig).getContainer()))
                                            .append(".$st$");
                                } else if (d instanceof TypeDeclaration) {
                                    path.append(names.name((ClassOrInterface)d.getContainer()))
                                            .append(".$st$");
                                }
                            } else {
                                path.append(names.self((TypeDeclaration) scope));
                            }
                        }
                    } else {
                        path.setLength(0);
                    }
                    if (scope == id || (nd != null && scope==nd)) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                if (id != null && path.length() == 0 && !ModelUtil.contains(id, that.getScope())) {
                    //Import of toplevel object or constructor
                    if (imported) {
                        path.append(names.moduleAlias(id.getUnit().getPackage().getModule())).append('.');
                    }
                    if (id.isAnonymous()) {
                        path.append(names.objectName(id));
                    } else {
                        Import imp = findImport(that, d);
                        if (imp == null) {
                            path.append(names.name(id));
                        } else {
                            path.append(names.objectName(imp.getTypeDeclaration()));
                        }
                    }
                }
                return path.toString();
            }
        }
        else if (d != null) {
            if (isMember && (d.isShared() || inProto || !d.isParameter() && AttributeGenerator.defineAsProperty(d))) {
                TypeDeclaration id = d instanceof TypeAlias ? (TypeDeclaration)d :
                    that.getScope().getInheritingDeclaration(d);
                if (id==null) {
                    //a local declaration of some kind,
                    //perhaps in an outer scope
                    id = (TypeDeclaration) d.getContainer();
                    if (id.isToplevel() && !ModelUtil.contains(id, that.getScope())) {
                        //Import of toplevel object or constructor
                        final StringBuilder sb = new StringBuilder();
                        if (imported) {
                            sb.append(names.moduleAlias(id.getUnit().getPackage().getModule())).append('.');
                        }
                        sb.append(id.isAnonymous() ? names.objectName(id) : names.name(id));
                        return sb.toString();
                    } else if (d instanceof Constructor) {
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
        if (p1.getModule()== null)
            return (p2.getModule()!=null &&
                    (!p2.getModule().isNative() ||
                        p2.getModule().getNativeBackends()
                            .supports(Backend.JavaScript)));
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
        Tree.Expression ex = that.getExpression();
		if (ex == null) {
            final Declaration contDecl = ModelUtil.getContainingDeclarationOfScope(that.getScope());
            if (contDecl instanceof Class) {
                out("return ", names.self((Class)contDecl), ";");
            } else {
                out("return;");
            }
            endLine();
            return;
        }
        out("return ");
        final Type returnType = ex.getTypeModel();
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(returnType)) {
            Declaration cont = ModelUtil.getContainingDeclarationOfScope(that.getScope());
            Type dectype = ((Declaration)cont).getReference().getType();
            if (dectype != null && dectype.isTypeParameter() && !dectype.getSatisfiedTypes().isEmpty()) {
                //Check for dynamic interfaces
                List<Type> dyntypes = null;
                for (Type tp : dectype.getSatisfiedTypes()) {
                    if (tp.getDeclaration() != null && tp.getDeclaration().isDynamic()) {
                        if (dyntypes == null) {
                            dyntypes = new ArrayList<>(dectype.getSatisfiedTypes().size());
                        }
                        dyntypes.add(tp);
                    }
                }
                if (dyntypes != null) {
                    if (dyntypes.size() == 1) {
                        dectype = dyntypes.get(0);
                    } else {
                        IntersectionType itype = new IntersectionType(that.getUnit());
                        itype.setSatisfiedTypes(dyntypes);
                        dectype = itype.getType();
                    }
                }
            }
            if (!ModelUtil.isTypeUnknown(dectype)) {
                TypeUtils.generateDynamicCheck(ex, dectype, this, false,
                        returnType.getTypeArguments());
                endLine(true);
                return;
            }
        }
        box(ex.getTerm());
        out(";");
        endLine();
//        if (isNaturalLiteral(that.getExpression().getTerm())) {
//            out(";");
//        } else {
//            super.visit(that);
//        }
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
        } else if (d instanceof ClassOrInterface) {
            ClassOrInterface coi = ModelUtil.getContainingClassOrInterface(d.getContainer());
            if (coi != null) {
                out(names.self(coi));
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(final Tree.SumOp that) {
        Type lt = that.getLeftTerm().getTypeModel();
        Type rt = that.getRightTerm().getTypeModel();
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "plus", "+", null, this);
        } else {
            if (TypeUtils.intsOrFloats(lt,rt)) {
                Operators.simpleBinaryOp(that, "(", "+", ")", this);
            } else {
                Operators.simpleBinaryOp(that, null, ".plus(", ")", this);
            }
        }
    }

    @Override
    public void visit(final Tree.DifferenceOp that) {
        Type lt = that.getLeftTerm().getTypeModel();
        Type rt = that.getRightTerm().getTypeModel();
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "minus", "-", null, this);
        } else {
            if (TypeUtils.intsOrFloats(lt,rt)) {
                Operators.simpleBinaryOp(that, "(", "-", ")", this);
            } else {
                Operators.simpleBinaryOp(that, null, ".minus(", ")", this);
            }
        }
    }

    @Override
    public void visit(final Tree.ProductOp that) {
        Type lt = that.getLeftTerm().getTypeModel();
        Type rt = that.getRightTerm().getTypeModel();
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "times", "*", null, this);
        } else {
            if (TypeUtils.intsOrFloats(lt,rt)) {
                Operators.simpleBinaryOp(that, "(", "*", ")", this);
            } else {
                Operators.simpleBinaryOp(that, null, ".times(", ")", this);
            }
        }
    }

    @Override
    public void visit(final Tree.QuotientOp that) {
        Type lt = that.getLeftTerm().getTypeModel();
        Type rt = that.getRightTerm().getTypeModel();
        if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "divided", "/", null, this);
        } else {
        	if (TypeUtils.bothInts(lt, rt)) {
        		out(getClAlias());
        		Operators.simpleBinaryOp(that, "i$(", "/", ")", this);
        	} else if (TypeUtils.intsOrFloats(lt, rt)) {
	        	Operators.simpleBinaryOp(that, "(", "/", ")", this);
	        } else {
	            Operators.simpleBinaryOp(that, null, ".divided(", ")", this);
	        }
        }
    }

    @Override public void visit(final Tree.RemainderOp that) {
        Type lt = that.getLeftTerm().getTypeModel();
        Type rt = that.getRightTerm().getTypeModel();
		if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "remainder", "%", null, this);
        } else {
        	if (TypeUtils.bothInts(lt, rt)) {
        		out(getClAlias());
        		Operators.simpleBinaryOp(that, "i$(", "%", ")", this);
        	}
        	else {
        		Operators.simpleBinaryOp(that, null, ".remainder(", ")", this);
        	}
        }
    }

    @Override
    public void visit(final Tree.ScaleOp that) {
        final String lhs = names.createTempVariable();
        Operators.simpleBinaryOp(that, "(function(){var "+lhs+"=", ";return ", ".scale("+lhs+");}())", this);
    }

    @Override public void visit(final Tree.PowerOp that) {
        final Type lt = that.getLeftTerm().getTypeModel();
        Operators.simpleBinaryOp(that, null, lt.isFloat()?".$fpower(":".power(", ")", this);
    }

    @Override public void visit(final Tree.AddAssignOp that) {
        if (!arithmeticAssignOp(that, "+")) {
            assignOp(that, "plus", null);
        }
    }

    @Override public void visit(final Tree.SubtractAssignOp that) {
        if (!arithmeticAssignOp(that, "-")) {
            assignOp(that, "minus", null);
        }
    }

    @Override public void visit(final Tree.MultiplyAssignOp that) {
        if (!arithmeticAssignOp(that, "*")) {
            assignOp(that, "times", null);
        }
    }

    @Override public void visit(final Tree.DivideAssignOp that) {
//    	if (!arithmeticAssignOp(that, "/")) {
    		assignOp(that, "divided", null);
//    	}
    }

    @Override public void visit(final Tree.RemainderAssignOp that) {
        if (!arithmeticAssignOp(that, "%")) {
            assignOp(that, "remainder", null);
        }
    }

    public void visit(Tree.ComplementAssignOp that) {
    	assignOp(that, "complement", TypeUtils.mapTypeArgument(that, "complement", "Element", "Other"));
    }
    public void visit(Tree.UnionAssignOp that) {
    	if (that.getBinary()) {
    		if (!arithmeticAssignOp(that, "|")) {
    			assignOp(that, "or", null);
    		}
    	}
    	else {
    		assignOp(that, "union", TypeUtils.mapTypeArgument(that, "union", "Element", "Other"));
    	}
    }
    public void visit(Tree.IntersectAssignOp that) {
    	if (that.getBinary()) {
    		if (!arithmeticAssignOp(that, "&")) {
    			assignOp(that, "and", null);
    		}
    	}
    	else {
    		assignOp(that, "intersection", TypeUtils.mapTypeArgument(that, "intersection", "Element", "Other"));
    	}
    }

    public void visit(Tree.AndAssignOp that) {
        assignOp(that, "&&", null);
    }
    public void visit(Tree.OrAssignOp that) {
        assignOp(that, "||", null);
    }

    private boolean arithmeticAssignOp(final Tree.AssignmentOp that, final String operand) {
        final Term lhs = that.getLeftTerm();
        final Type ltype = lhs.getTypeModel();
        final Type rtype = that.getRightTerm().getTypeModel();
        final boolean oneFloat = ltype.isFloat() || rtype.isFloat();
        if (TypeUtils.intsOrFloats(ltype, rtype)) {
            if (lhs instanceof BaseMemberExpression) {
                BaseMemberExpression lhsBME = (BaseMemberExpression) lhs;
                Declaration lhsDecl = lhsBME.getDeclaration();

                final String getLHS = memberAccess(lhsBME, null);
                out("(");
                BmeGenerator.generateMemberAccess(lhsBME, new GenerateCallback() {
                    @Override public void generateValue() {
                        if (oneFloat) {
                            out(getClAlias(), "f$(");
                        }
                        out(getLHS, operand);
//                        if (!isNaturalLiteral(that.getRightTerm())) {
                            that.getRightTerm().visit(GenerateJsVisitor.this);
//                        }
                        if (oneFloat) {
                            out(")");
                        }
                    }
                }, null, this);
                if (!hasSimpleGetterSetter(lhsDecl)) { out(",", getLHS); }
                out(")");

            } else if (lhs instanceof QualifiedMemberExpression) {
                QualifiedMemberExpression lhsQME = (QualifiedMemberExpression) lhs;
                if (TypeUtils.isNativeJs(lhsQME)) {
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
                    out(operand);
//                    if (!isNaturalLiteral(that.getRightTerm())) {
                        that.getRightTerm().visit(this);
//                    }
                    out(")");
                } else {
                    final String lhsPrimaryVar = createRetainedTempVar();
                    final String getLHS = memberAccess(lhsQME, lhsPrimaryVar);
                    out("(", lhsPrimaryVar, "=");
                    lhsQME.getPrimary().visit(this);
                    out(",");
                    BmeGenerator.generateMemberAccess(lhsQME, new GenerateCallback() {
                        @Override public void generateValue() {
                            out(getLHS, operand);
//                            if (!isNaturalLiteral(that.getRightTerm())) {
                                that.getRightTerm().visit(GenerateJsVisitor.this);
//                            }
                        }
                    }, lhsPrimaryVar, this);

                    if (!hasSimpleGetterSetter(lhsQME.getDeclaration())) {
                        out(",", getLHS);
                    }
                    out(")");
                }
            } else if (lhs instanceof Tree.IndexExpression) {
                lhs.addUnsupportedError("Index expressions are not supported in this kind of assignment.");
            }
            return true;
        }
        return false;
    }

    private void assignOp(final Tree.AssignmentOp that, final String functionName,
            final Map<TypeParameter, Type> targs) {
        if (errVisitor.hasErrors(that))return;
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
//                    if (!isNaturalLiteral(that.getRightTerm())) {
                        that.getRightTerm().visit(GenerateJsVisitor.this);
//                    }
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
            if (TypeUtils.isNativeJs(lhsQME)) {
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
//                if (!isNaturalLiteral(that.getRightTerm())) {
                    that.getRightTerm().visit(this);
//                }
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
                        Tree.Term term = that.getRightTerm();
//                        if (!isNaturalLiteral(term)) {
                            term.visit(GenerateJsVisitor.this);
//                        }
                        out(")");
                    }
                }, lhsPrimaryVar, this);
                
                if (!hasSimpleGetterSetter(lhsQME.getDeclaration())) {
                    out(",", getLHS);
                }
                out(")");
            }
        } else if (lhs instanceof Tree.IndexExpression) {
            lhs.addUnsupportedError("Index expressions are not supported in this kind of assignment.");
        }
    }

    @Override public void visit(final Tree.PositiveOp that) {
        box(that.getTerm());
    }

    @Override public void visit(final Tree.NegativeOp that) {
        Operators.neg(that, this);
    }

//    @Override public void visit(final Tree.PositiveOp that) {
//        final boolean nat = that.getTerm().getTypeModel().isInteger();
//        //TODO if it's positive we leave it as is?
//        Operators.unaryOp(that, nat?"(+":null, nat?")":null, this);
//    }

    @Override public void visit(final Tree.FlipOp that) {
    	Operators.flip(that, this);
    }

    @Override public void visit(final Tree.EqualOp that) {
        Operators.equal(that, this);
    }

    @Override public void visit(final Tree.NotEqualOp that) {
        Operators.notEqual(that, this);
    }

    @Override public void visit(final Tree.NotOp that) {
    	Operators.not(that, this);
    }

    @Override public void visit(final Tree.IdenticalOp that) {
        Operators.simpleBinaryOp(that, "(", "===", ")", this);
    }

    @Override public void visit(final Tree.CompareOp that) {
        Operators.simpleBinaryOp(that, null, ".compare(", ")", this);
    }

    @Override public void visit(final Tree.SmallerOp that) {
        Operators.smaller(that, this);
    }

    @Override public void visit(final Tree.LargerOp that) {
        Operators.larger(that, this);
    }

    @Override public void visit(final Tree.SmallAsOp that) {
        Operators.smallAs(that, this);
    }

    @Override public void visit(final Tree.LargeAsOp that) {
        Operators.largeAs(that, this);
    }
    public void visit(final Tree.WithinOp that) {
        Operators.withinOp(that, this);
    }

   @Override public void visit(final Tree.AndOp that) {
       Operators.simpleBinaryOp(that, "(", "&&", ")", this);
   }

   @Override public void visit(final Tree.OrOp that) {
       Operators.simpleBinaryOp(that, "(", "||", ")", this);
   }

   @Override public void visit(final Tree.EntryOp that) {
       out(getClAlias(), "Entry(");
       Operators.genericBinaryOp(that, ",", 
    		   that.getTypeModel().getTypeArguments(),
               that.getTypeModel().getVarianceOverrides(), 
               this);
   }

   @Override public void visit(final Tree.RangeOp that) {
       Operators.segmentOrRange(that, "span", "Element", this);
   }

   @Override
   public void visit(final Tree.SegmentOp that) {
       Operators.segmentOrRange(that, "measure", "Element", this);
   }

   @Override public void visit(final Tree.ThenOp that) {
       Operators.simpleBinaryOp(that, "(", "?", ":null)", this);
   }

   @Override public void visit(final Tree.Element that) {
       out(".$_get(");
//       if (!isNaturalLiteral(that.getExpression().getTerm())) {
           that.getExpression().visit(this);
//       }
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
       return isInDynamicBlock() && TypeUtils.isUnknown(decl) 
           || !(decl instanceof Value && ((Value)decl).isTransient() || decl instanceof Setter || decl.isFormal());
   }

   @Override public void visit(final Tree.PostfixIncrementOp that) {
       Operators.postfixIncrementOrDecrement(that.getTerm(), "successor", this);
   }

   @Override public void visit(final Tree.PostfixDecrementOp that) {
       Operators.postfixIncrementOrDecrement(that.getTerm(), "predecessor", this);
   }

    @Override
    public void visit(final Tree.UnionOp that) {
    	Type lt = that.getLeftTerm().getTypeModel();
    	Type rt = that.getRightTerm().getTypeModel();
		if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "or", "|", null, this);
        } else {
	    	if (that.getBinary()) {
	    		if (TypeUtils.bothInts(lt, rt)) {
	    			Operators.simpleBinaryOp(that, "(", "|", ")", this);
	    		}
	    		else {
	    			Operators.simpleBinaryOp(that, null, ".or(", ")", this);
	    		}
	    	}
	    	else {
	    		Operators.genericBinaryOp(that, ".union(",
	    				TypeUtils.mapTypeArgument(that, "union", "Element", "Other"),
	    				that.getTypeModel().getVarianceOverrides(), this);
	    	}
        }
    }

    @Override
    public void visit(final Tree.IntersectionOp that) {
    	Type lt = that.getLeftTerm().getTypeModel();
    	Type rt = that.getRightTerm().getTypeModel();
		if (isInDynamicBlock() && ModelUtil.isTypeUnknown(lt)) {
            Operators.nativeBinaryOp(that, "and", "&", null, this);
        } else {
	    	if (that.getBinary()) {
	    		if (TypeUtils.bothInts(lt, rt)) {
	    			Operators.simpleBinaryOp(that, "(", "&", ")", this);
	    		}
	    		else {
	    			Operators.simpleBinaryOp(that, null, ".and(", ")", this);
	    		}
	    	}
	    	else {
	    		Operators.genericBinaryOp(that, ".intersection(",
	    				TypeUtils.mapTypeArgument(that, "intersection", "Element", "Other"),
	    				that.getTypeModel().getVarianceOverrides(), this);
	    	}
        }
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

   @Override public void visit(final Tree.Variable that) {
       final boolean guarded = that instanceof GuardedVariable;
       if (guarded) {
           out("function ", names.name(that.getDeclarationModel()), "(){return ");
       }
       super.visit(that);
       if (guarded) {
           out(";}");
           endLine();
       }
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
    void generateIsOfType(Node term, String termString, final Type type, String tmpvar, final boolean negate,
                          final boolean coerceDynamic) {
        if (negate) {
            out("!");
        }
        out(getClAlias(), "is$(");
        if (isInDynamicBlock() && coerceDynamic) {
            out(getClAlias(), "dre$$(");
        }
        if (term instanceof Term) {
            conds.specialConditionRHS((Term)term, tmpvar);
        } else {
            conds.specialConditionRHS(termString, tmpvar);
        }
        if (isInDynamicBlock() && coerceDynamic) {
            out(",");
            TypeUtils.typeNameOrList(term, type, this, false);
            out(",false)");
        }
        out(",");
        TypeUtils.typeNameOrList(term, type, this, false);
        if (type.getQualifyingType() != null) {
            Type outer = type.getQualifyingType();
            boolean first=true;
            while (outer != null) {
                if (first) {
                    out(",[");
                    first=false;
                } else{
                    out(",");
                }
                TypeUtils.typeNameOrList(term, outer, this, false);
                outer = outer.getQualifyingType();
            }
            if (!first) {
                out("]");
            }
        } else if (type.getDeclaration() != null) {
            Scope container = type.getDeclaration().getContainer();
            if (container != null) {
                Declaration d = ModelUtil.getContainingDeclarationOfScope(container);
                if (d != null && d instanceof Function 
                        && !((Function)d).getTypeParameters().isEmpty()) {
                    out(",", names.typeArgsParamName((Function)d));
                }
            }
        }
        out(")");
    }

    @Override
    public void visit(final Tree.IsOp that) {
        generateIsOfType(that.getTerm(), null, that.getType().getTypeModel(), null, false, false);
    }

    @Override public void visit(final Tree.Break that) {
        if (continues.isEmpty()) {
            out("break;");
        } else {
            ContinueBreakVisitor top=continues.peek();
            if (top.belongs(that)) {
                out(top.getBreakName(), "=true;return;");
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
            if (top.belongs(that)) {
                out(top.getContinueName(), "=true;return;");
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
        out(getClAlias(), "$cnt$2(");
//        if (!isNaturalLiteral(that.getLeftTerm())) {
            box(that.getLeftTerm());
//        }
        out(",");
        box(that.getRightTerm());
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
        if (errVisitor.hasErrors(that))return;
        Operators.indexOp(that, this);
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

    void outputCapturedValues(Set<Value> caps) {
        if (caps == null || caps.isEmpty()) {
            return;
        }
        boolean first=true;
        for (Value v : caps) {
            if (first) {
                out("var ");
            } else {
                out(",");
            }
            first=false;
            final String cn = names.createTempVariable();
            out(cn, "=", names.name(v));
            names.forceName(v, cn);
        }
        out(";");
    }
    void forgetCapturedValues(Set<Value> caps) {
        if (caps == null) {
            return;
        }
        for (Value v : caps) {
            directAccess.remove(v);
            names.forceName(v, null);
        }
    }

    /** Encloses the block in a function, IF NEEDED. */
    void encloseBlockInFunction(final Tree.Block block, final boolean markBlock,
            final Set<Value> capturedValues) {
        boolean wrap = capturedValues != null && !capturedValues.isEmpty()
                || new BlockWithCaptureVisitor(block).hasCapture();
        if (markBlock) {
            beginBlock();
        }
        if (wrap) {
            final ContinueBreakVisitor cbv = new ContinueBreakVisitor(block, names);
            continues.push(cbv);
            boolean vars=false;
            if (cbv.isContinues()) {
                out("var ", cbv.getContinueName(), "=false");
                vars=true;
            }
            if (cbv.isBreaks()) {
                out(vars?",":"var ", cbv.getBreakName(), "=false");
                vars=true;
            }
            out(vars?",":"var ", cbv.getReturnName(), "=(function(){");
            outputCapturedValues(capturedValues);
        }
        visitStatements(block.getStatements());
        if (wrap) {
            final ContinueBreakVisitor cbv = continues.pop();
            final ContinueBreakVisitor prev = continues.isEmpty()?null:continues.peek();
            out("}());if(", cbv.getReturnName(), "!==undefined){return ", cbv.getReturnName(), ";}");
            if (cbv.isContinues()) {
                out("else if(", cbv.getContinueName());
                if (prev != null && prev.isContinues()) {
                    out("){", prev.getContinueName(), "=true;return ", cbv.getReturnName(), ";}");
                } else {
                    out("){continue;}");
                }
            }
            if (cbv.isBreaks()) {
                out("else if(", cbv.getBreakName());
                if (prev != null && prev.isBreaks()) {
                    out("){", prev.getBreakName(), "=true;return ", cbv.getReturnName(), ";}");
                } else {
                    out("){break;}");
                }
            }
            forgetCapturedValues(capturedValues);
        }
        if (markBlock) {
            endBlockNewLine();
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
        
        //Scan for a "doc" annotation with custom message
        Tree.AnnotationList annotationList = that.getAnnotationList();
        Tree.ConditionList conditionList = that.getConditionList();
        if (conditionList != null) {
            Iterator<ConditionGenerator.VarHolder> ivars = conds.gatherVariables(
                    conditionList, true, true).iterator();
            out(getClAlias(), "asrt$2(");
            String custom = docText(annotationList);
            if (custom==null) {
                visit(annotationList.getAnonymousAnnotation().getStringTemplate());
            } else {
                out("\"", JsUtils.escapeStringLiteral(custom), "\"");
            }
            out(",'", that.getLocation(), "','",
                                that.getUnit().getFilename(), "'");
            for (Tree.Condition cond : conditionList.getConditions()) {
                out(",[");
                if (cond instanceof Tree.BooleanCondition) {
                    cond.visit(this);
                } else {
                    conds.specialCondition(ivars.next(), cond, true);
                }
                out(",'");
                for (int i = cond.getToken().getTokenIndex();
                        i <= cond.getEndToken().getTokenIndex(); 
                		i++) {
                    out(JsUtils.escapeStringLiteral(tokens.get(i).getText()));
                }
                out("']");
            }
            out(");");
            //conds.specialConditionsAndBlock(conditionList, null, getClAlias()+"asrt$2", true);
        }
        endLine();
    }

    private String docText(Tree.AnnotationList annotationList) {
        if (annotationList!=null) {
            if (annotationList.getAnonymousAnnotation() != null) {
                Tree.StringLiteral lit = annotationList.getAnonymousAnnotation().getStringLiteral();
                if (lit == null) {
                    return null;
                } else {
                    return lit.getText();
                }
            } else {
                for (Tree.Annotation ann : annotationList.getAnnotations()) {
                    BaseMemberExpression bme = (BaseMemberExpression)ann.getPrimary();
                    if ("doc".equals(bme.getDeclaration().getName())) {
                        Tree.ListedArgument arg = 
                                (Tree.ListedArgument) 
                                    ann.getPositionalArgumentList().getPositionalArguments().get(0);
                        return arg.getExpression().getTerm().getText();
                    }
                }
            }
        }
        return "";
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
            MetamodelHelper.generateOpenType(that, that.getDeclaration(), this, compiler.isCompilingLanguageModule());
        } else {
            MetamodelHelper.generateClosedTypeLiteral(that, this);
        }
    }

    @Override
    public void visit(final Tree.MemberLiteral that) {
        if (that.getWantsDeclaration()) {
            MetamodelHelper.generateOpenType(that, that.getDeclaration(), this, compiler.isCompilingLanguageModule());
        } else {
            MetamodelHelper.generateMemberLiteral(that, this);
        }
    }

    @Override
    public void visit(final Tree.PackageLiteral that) {
        Package pkg = (Package)that.getImportPath().getModel();
        MetamodelHelper.findModule(pkg.getModule(), this);
        out(".findPackage('", pkg.getNameAsString(), "')");
    }

    @Override
    public void visit(Tree.ModuleLiteral that) {
        Module m = (Module)that.getImportPath().getModel();
        if (m!=null) {
        	MetamodelHelper.findModule(m, this);
        }
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

//    @Override public void visit(Tree.ListedArgument that) {
//        if (!isNaturalLiteral(that.getExpression().getTerm())) {
//            super.visit(that);
//        }
//    }

    @Override public void visit(Tree.LetExpression that) {
        if (errVisitor.hasErrors(that))return;
        FunctionHelper.generateLet(that, directAccess, this);
    }

    @Override public void visit(final Tree.Destructure that) {
        if (errVisitor.hasErrors(that))return;
        final String expvar = names.createTempVariable();
        out("var ", expvar, "=");
        that.getSpecifierExpression().visit(this);
        new Destructurer(that.getPattern(), this, directAccess, expvar, false, false);
        endLine(true);
    }

    public void visit(Tree.Enumerated that) {
        if (errVisitor.hasErrors(that))return;
        if (opts.isOptimize())return;
        comment(that);
        TypeDeclaration klass = (TypeDeclaration)that.getEnumerated().getContainer();
        defineAttribute(names.self(klass), names.name(that.getDeclarationModel()));
        out("{return ", names.name(klass), names.constructorSeparator(that.getDeclarationModel()),
                names.name(that.getDeclarationModel()), "();},undefined,");
        TypeUtils.encodeForRuntime(that, that.getDeclarationModel(), that.getAnnotationList(), this);
        out(");");
    }

    public void visit(final Tree.ExtendedTypeExpression that) {
        if (errVisitor.hasErrors(that))return;
        final Declaration d = that.getDeclaration();
        if (d instanceof Constructor) {
            qualify(that, (Declaration)d.getContainer());
            out(names.name((Declaration)d.getContainer()), names.constructorSeparator(d));
        } else {
            qualify(that, d);
        }
        out(names.name(d));
    }

//    boolean isNaturalLiteral(Tree.Term that) {
//    	that = TreeUtil.unwrapExpressionUntilTerm(that);
//        if (that instanceof Tree.NaturalLiteral) {
//            out(Long.toString(parseNaturalLiteral((Tree.NaturalLiteral)that, false)));
//            return true;
//        } else if (that instanceof Tree.NegativeOp) {
//            that.visit(this);
//            return true;
//        }
//        return false;
//    }

    public void saveNativeHeader(Tree.Declaration that) {
        headers.put(that.getDeclarationModel().getQualifiedNameString(), that);
    }
    public Tree.Declaration getNativeHeader(Declaration that) {
        return headers.get(that.getQualifiedNameString());
    }

    public void visit(Tree.SequenceType that) {
        //This is just to avoid the NaturalLiteral from being visited
    }

}
