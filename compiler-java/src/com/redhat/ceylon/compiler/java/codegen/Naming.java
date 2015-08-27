/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.model.FieldValue;
import com.redhat.ceylon.model.loader.model.JavaMethod;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.loader.model.LazyInterface;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.loader.model.LazyValue;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.ControlBlock;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public class Naming extends NamingBase implements LocalId {

    private static String prefixName(Prefix prefix, String s) {
        return prefix.toString() + s;
    }
    
    private static String prefixName(Prefix prefix, String... rest) {
        if (rest.length == 0) {
            throw new RuntimeException();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        for (String s : rest) {
            sb.append(s).append('$');
        }
        
        sb.setLength(sb.length()-1);// remove last $
        return sb.toString();
    }
    
    static String compoundName(String name, String name2) {
        return name + "$" + name2;
    }
    
    static String compoundName(String... names) {
        if (names.length == 0) {
            throw new RuntimeException();
        }
        StringBuilder sb = new StringBuilder();
        for (String s : names) {
            sb.append(s).append('$');
        }
        sb.setLength(sb.length()-1);// remove last $
        return sb.toString();
    }
    
    private static final String IMPL_POSTFIX = Suffix.$impl.toString();
    private static final String ANNO_POSTFIX = Suffix.$annotation$.toString();
    private static final String ANNOS_POSTFIX = Suffix.$annotations$.toString();
    private static final String DELEGATION_POSTFIX = Suffix.$delegation$.toString();

    static enum DeclNameFlag {
        /** 
         * A qualified name. 
         * <li>For a top level this includes the package name.
         * <li>For an inner this includes the package name and the qualifying type names
         * <li>For a (possibly indirect) local this includes the qualifying type names 
         */
        QUALIFIED,
        /** The name of the companion type of this thing */
        COMPANION,
        /** The name of the annotation type of this thing */
        ANNOTATION,
        /** The name of the annotation type of this thing */
        ANNOTATIONS,
        /** Quoted */
        DELEGATION
    }
    
    /** Quote the given name by prefixing it with a dollar ($) */
    public static String quote(String name) {
        return JVMModuleUtil.quote(name);
    }

    /** Determines whether the given name is a Java keyword */
    public static boolean isJavaKeyword(String name) {
        return JVMModuleUtil.isJavaKeyword(name);
    }

    /** Prefixes the given name with a dollar ($) if it is a Java keyword */
    public static String quoteIfJavaKeyword(String name){
        return JVMModuleUtil.quoteIfJavaKeyword(name);
    }

    private static final HashSet<String> QUOTABLE_METHOD_NAMES;

    private static final List<String> COM_REDHAT_CEYLON_LANGUAGE_PACKAGE = 
            Arrays.asList(new String[]{"com", "redhat", "ceylon", "compiler", "java", "language"});
    
    static {
        QUOTABLE_METHOD_NAMES = new HashSet<>(Arrays.asList(
                "hashCode",
                "toString",
                "equals",
                "wait",
                "notify",
                "notifyAll",
                "getClass",
                "finalize",
                "main",
                "clone"));
    }
    
    private static String getMethodName(TypedDeclaration decl, int namingOptions) {
        String methodName;
        if (decl.isClassOrInterfaceMember()) {
            String name = decl.getName();
            // ERASURE
            if ((namingOptions & NA_ANNOTATION_MEMBER) == 0 && "hash".equals(name)) {
                return "hashCode";
            } else if ((namingOptions & NA_ANNOTATION_MEMBER) == 0 && "string".equals(name)) {
                return "toString";
            } else if ("equals".equals(name)) {
                // This is a special case where we override the mangling of getMethodNameInternal()
                return "equals";
            }
            methodName = getMethodNameInternal(decl);
        } else {
            methodName = getMethodNameInternal(decl);
        }
        if ((namingOptions & NA_CANONICAL_METHOD) != 0) {
            methodName = suffixName(Suffix.$canonical$, methodName);
        }
        return methodName;
    }

    private static String getMethodNameInternal(TypedDeclaration decl) {
        String name;
        if (decl.isClassOrInterfaceMember()
                && decl instanceof Function) {
            Declaration refined = decl.getRefinedDeclaration();
            if (refined instanceof JavaMethod) {
                return ((JavaMethod)refined).getRealName();
            }
            name = quoteMethodNameIfProperty((Function)decl);
        } else {
            name = decl.getName();
        }
        // ERASURE
        if (QUOTABLE_METHOD_NAMES.contains(name)) {
            return quote(name);
        } else {
            return quoteIfJavaKeyword(name);
        }
    }

    public static String getVariableName(Tree.Variable var) {
        return (var != null) ? Naming.quoteIfJavaKeyword(var.getIdentifier().getText()) : null;
    }
    
    public static String getLocalValueName(Value val) {
        return Naming.quoteIfJavaKeyword(val.getName());
    }
    
    public static String quoteLocalValueName(String name) {
        return Naming.quoteIfJavaKeyword(name);
    }
    
    public static String quoteFieldName(String name) {
        return Naming.quoteIfJavaKeyword(name);
    }
    
    public static String quoteParameterName(String name) {
        return Naming.quoteIfJavaKeyword(name);
    }
    
    public static String quoteMethodName(String name) {
        return Naming.quoteIfJavaKeyword(name);
    }
    
    private final java.util.Map<Scope, Integer> locals = new java.util.HashMap<Scope, Integer>();
    
    /**
     * A Visitor which assigns ids sometimes needed by Naming.getLocalId() for 
     * determining the name munging necessary for "hoisting" interface declarations 
     * to top level.
     */
    private class LocalIdVisitor extends Visitor {
        
        private int localId = 0;
        
        private void resetLocals() {
            localId = 0;
        }
        
        private void local(Scope decl) {
            if (!locals.containsKey(decl)) {
                locals.put(decl, localId);
                localId++;
            }
            Scope container = decl.getContainer();
            if(container != null && ModelUtil.isLocalNotInitializerScope(container)){
                local(container);
            }
        }
        
        void noteDecl(Declaration decl) {
            if (decl.isToplevel()) {
                resetLocals();
            } else if (Decl.isLocalNotInitializer(decl)){
                local(decl.getContainer());
            }
        }
        
        @Override
        public void visit(Tree.ClassOrInterface that) {
            noteDecl(that.getDeclarationModel());
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.TypeAliasDeclaration that) {
            noteDecl(that.getDeclarationModel());
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.ObjectDefinition that) {
            noteDecl(that.getDeclarationModel());
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.AnyMethod that) {
            if (Decl.isLocalNotInitializer(that.getDeclarationModel())) {
                noteDecl(that.getDeclarationModel());
            }
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.AttributeGetterDefinition that) {
            if (Decl.isLocalNotInitializer(that.getDeclarationModel())) {
                noteDecl(that.getDeclarationModel());
            }
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.AttributeSetterDefinition that) {
            if (Decl.isLocalNotInitializer(that.getDeclarationModel())) {
                noteDecl(that.getDeclarationModel());
            }
            super.visit(that);
        }
        
        @Override
        public void visit(Tree.NamedArgument that) {
            local(that.getScope().getContainer());
            local(that.getScope());
            super.visit(that);
        }
    }
    
    public void assignNames(Tree.CompilationUnit cu) {
        this.locals.clear();
        LocalIdVisitor liv = new LocalIdVisitor();
        cu.visit(liv);
    }
    
    /** 
     * Helper class for {@link #makeTypeDeclarationExpression(JCExpression, TypeDeclaration, DeclNameFlag...)}
     */
    abstract class TypeDeclarationBuilder<R> {
        private final StringBuilder sb = new StringBuilder();
        protected final Declaration decl;
        TypeDeclarationBuilder(Declaration decl) {
            this.decl = decl;
        }
        abstract void select(String s);
        final void append(String s) {
            sb.append(s);
        }
        final void append(char ch) {
            sb.append(ch);
        }
        private boolean isCeylonTypeDecl() {
            return decl instanceof TypeDeclaration 
                    && Decl.isCeylon((TypeDeclaration)decl);
        }
        final void selectAppended() {
            String name = sb.toString();
            select(isCeylonTypeDecl() ? quoteClassName(name) : name);
            sb.setLength(0);
        }
        abstract R result();
        public void clear() {
            sb.setLength(0);
        }
        public final String toString() {
            return result().toString() + " plus, maybe " + (isCeylonTypeDecl() ? quoteClassName(sb.toString()) : sb.toString());
        }
    }
    /**
     * Implementation of DeclName which constructs a JCExpression
     */
    class TreeDeclName extends TypeDeclarationBuilder<JCExpression>{
        private final JCExpression qualifying;
        private JCExpression expr;
        TreeDeclName(Declaration decl, JCExpression expr) {
            super(decl);
            this.qualifying = expr;
            this.expr = expr;
        }
        void select(String s) {
            expr = makeQualIdent(expr, s);
        }
        JCExpression result() {
            return expr;
        }
        public void clear() {
            super.clear();
            expr = qualifying;
        }
    }
    /**
     * Implementation of DeclName which constructs a String
     */
    class StringDeclName extends TypeDeclarationBuilder<String>{
        private final String qualifying;
        private StringBuffer expr;
        StringDeclName(Declaration decl, String expr) {
            super(decl);
            this.qualifying = expr;
            this.expr = expr != null ? new StringBuffer(expr) : null;
        }
        void select(String s) {
            if (expr == null  && s != null) {
                expr = new StringBuffer(s);
            } else if (s == null && expr != null ) {
            } else {
                expr.append(".").append(s);
            }
        }
        String result() {
            return expr.toString();
        }
        public void clear() {
            super.clear();
            expr = qualifying != null ? new StringBuffer(qualifying) : null;
        }
    }
    
    JCExpression makeTypeDeclarationExpression(JCExpression qualifyingExpr, final TypeDeclaration decl, DeclNameFlag... options) {
        // be more resilient to errors
        if(decl == null)
            return make().Erroneous();
        TreeDeclName helper = new TreeDeclName(decl, qualifyingExpr); 
        return makeTypeDeclaration(helper, decl, options);
    }

    private <R> R makeTypeDeclaration(TypeDeclarationBuilder<R> declarationBuilder,
            final TypeDeclaration decl, DeclNameFlag... options) {
        EnumSet<DeclNameFlag> flags = EnumSet.noneOf(DeclNameFlag.class);
        flags.addAll(Arrays.asList(options));
        
        if (flags.contains(DeclNameFlag.ANNOTATION) && !Decl.isAnnotationClass(decl)) { 
            throw new BugException(decl.getName());
        }
        if (flags.contains(DeclNameFlag.ANNOTATIONS) 
                && !(Decl.isAnnotationClass(decl) 
                    || decl instanceof Class 
                    || gen().isSequencedAnnotation((Class)decl))) { 
            throw new BugException(decl.getName());
        }
        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        
        if (flags.contains(DeclNameFlag.QUALIFIED) && (!decl.isAnonymous() || decl.isNamed())) {
            final List<String> packageName;
            if(!AbstractTransformer.isJavaArray(decl))
                packageName = ((Package) s).getName();
            else
                packageName = COM_REDHAT_CEYLON_LANGUAGE_PACKAGE;
            if (packageName.isEmpty() || !packageName.get(0).isEmpty()) {
                declarationBuilder.select("");
            }
            for (int ii = 0; ii < packageName.size(); ii++) {
                declarationBuilder.select(quoteIfJavaKeyword(packageName.get(ii)));
            }
        }
        for (int ii = 0; ii < l.size(); ii++) {
            Scope scope = l.get(ii);
            final boolean last = ii == l.size() - 1;
            appendTypeDeclaration(decl, flags, declarationBuilder, scope, last);
        }
        return declarationBuilder.result();
    }

    private void appendTypeDeclaration(final TypeDeclaration decl, EnumSet<DeclNameFlag> flags, 
            TypeDeclarationBuilder<?> typeDeclarationBuilder, Scope scope, final boolean last) {
        if (scope instanceof Class 
                || scope instanceof TypeAlias 
                || (scope instanceof Constructor && (scope.equals(decl) || !Decl.isLocalNotInitializerScope(scope)))) {
            TypeDeclaration klass = (TypeDeclaration)scope;
            if(klass.isAnonymous() && !klass.isNamed())
                typeDeclarationBuilder.clear();
            typeDeclarationBuilder.append(escapeClassName(klass.getName() != null ? klass.getName() : ""));
            if (Decl.isCeylon(klass)) {
                if (flags.contains(DeclNameFlag.COMPANION)
                    && Decl.isLocalNotInitializer(klass)
                    && last) {
                    typeDeclarationBuilder.append(IMPL_POSTFIX);
                } else if (flags.contains(DeclNameFlag.ANNOTATION)
                        && last) {
                    typeDeclarationBuilder.append(ANNO_POSTFIX);
                } else if (flags.contains(DeclNameFlag.ANNOTATIONS)
                        && last) {
                    typeDeclarationBuilder.append(ANNOS_POSTFIX);
                } else if (flags.contains(DeclNameFlag.DELEGATION)
                        && last) {
                    typeDeclarationBuilder.append(DELEGATION_POSTFIX);
                }
            }
        } else if (scope instanceof Interface) {
            Interface iface = (Interface)scope;
            typeDeclarationBuilder.append(iface.getName());
            if (Decl.isCeylon(iface)
                && ((decl instanceof Class || decl instanceof Constructor || decl instanceof TypeAlias|| scope instanceof Constructor) 
                        || flags.contains(DeclNameFlag.COMPANION))) {
                typeDeclarationBuilder.append(IMPL_POSTFIX);
            }
        } else if (Decl.isLocalNotInitializerScope(scope)) {
            if (flags.contains(DeclNameFlag.COMPANION)
                || !(decl instanceof Interface)) {
                typeDeclarationBuilder.clear();
            } else if (flags.contains(DeclNameFlag.QUALIFIED)
                    || (decl instanceof Interface)) {
                Scope nonLocal = scope;
                while (!(nonLocal instanceof Declaration)) {
                    nonLocal = nonLocal.getContainer();
                }
                typeDeclarationBuilder.append(((Declaration)nonLocal).getPrefixedName());
                if (!Decl.equalScopes(scope, nonLocal)) {
                    typeDeclarationBuilder.append('$');
                    typeDeclarationBuilder.append(getLocalId(scope));
                }
                if (decl instanceof Interface) {
                    typeDeclarationBuilder.append('$');
                } else {
                    if (flags.contains(DeclNameFlag.QUALIFIED)) {
                        typeDeclarationBuilder.selectAppended();
                    } else {
                        typeDeclarationBuilder.clear();
                    }
                }
            }
            return;
        } else if (scope instanceof TypedDeclaration && ((Declaration) scope).isToplevel()) {
            // nothing? that's just weird
        }
        if (!last) {
            if (decl instanceof Interface
                    && Decl.isCeylon((TypeDeclaration)decl)
                    && !flags.contains(DeclNameFlag.COMPANION)) {
                typeDeclarationBuilder.append('$');
            } else if (decl instanceof Constructor
                    && ((Class)decl.getContainer()).isMember()
                    && decl.getContainer().equals(scope)) {
                typeDeclarationBuilder.append('$');
            } else {
                if (flags.contains(DeclNameFlag.QUALIFIED)) {
                    typeDeclarationBuilder.selectAppended();
                } else {
                    typeDeclarationBuilder.clear();
                }
            }
        } else {
            typeDeclarationBuilder.selectAppended();
        }
        return;
    }
    
    public static String toplevelClassName(String pkgName, Tree.Declaration declaration){
        StringBuilder b = new StringBuilder();
        if(!pkgName.isEmpty()){
            b.append(pkgName).append('.');
        }
        appendClassName(b, declaration);
        return b.toString();

    }
    
    private static void appendClassName(StringBuilder b, com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration decl) {
        if (decl instanceof Tree.AnyClass) {
            Tree.AnyClass klass = (Tree.AnyClass)decl;
            b.append(klass.getIdentifier().getText());
        }else if (decl instanceof Tree.TypeAliasDeclaration) {
            Tree.TypeAliasDeclaration klass = (Tree.TypeAliasDeclaration)decl;
            b.append(klass.getIdentifier().getText());
        } else if (decl instanceof Tree.AnyInterface) {
            Tree.AnyInterface iface = (Tree.AnyInterface)decl;
            b.append(iface.getIdentifier().getText());
        } else if (decl instanceof Tree.TypedDeclaration){
            String name = decl.getIdentifier().getText();
            b.append(name);
            // only for lowercase stuff, not for \iFoo names
            if(isLowerCase(name))
                b.append('_');
        } else {
            throw new RuntimeException("Don't know how to get a class name for tree of type " + decl.getClass());
        }
    }

    public static String toplevelClassName(String pkgName, Declaration declaration){
        StringBuilder b = new StringBuilder();
        if(!pkgName.isEmpty()){
            b.append(pkgName).append('.');
        }
        appendClassName(b, declaration);
        return b.toString();

    }
    
    private static void appendClassName(StringBuilder b, Declaration decl) {
        if (decl instanceof ClassOrInterface
                || decl instanceof TypeAlias) {
            b.append(decl.getName());
        } else if (decl instanceof TypedDeclaration){
            b.append(decl.getName());
            b.append('_');
        } else {
            throw new RuntimeException("Don't know how to get a class name for tree of type " + decl.getClass());
        }
    }

    /**
     * Generates a Java type name for the given declaration
     * @param decl The declaration
     * @param options Option flags
     */
    String makeTypeDeclarationName(final TypeDeclaration decl, DeclNameFlag... options) {
        StringDeclName helper = new StringDeclName(decl, null);
        return makeTypeDeclaration(helper, decl, options);
    }

    JCExpression makeDeclarationName(TypeDeclaration decl, DeclNameFlag... flags) {
        return makeTypeDeclarationExpression(null, decl, flags);
    }
    
    String getCompanionClassName(TypeDeclaration decl, boolean qualified) {
        if (qualified) {
            return makeTypeDeclarationName(decl, DeclNameFlag.QUALIFIED, DeclNameFlag.COMPANION);
        } else {
            return makeTypeDeclarationName(decl, DeclNameFlag.COMPANION);
        }
    }
    
    JCExpression makeCompanionClassName(TypeDeclaration decl) {
        return makeTypeDeclarationExpression(null, decl, DeclNameFlag.QUALIFIED, DeclNameFlag.COMPANION);
    }
    
    private static String quoteMethodNameIfProperty(Function method) {
        String name = method.getName();
        if (!method.isShared()) {
            name = suffixName(Suffix.$priv$, name);
        }
        // Toplevel methods keep their original name because their names might be mangled
        if (method instanceof LazyFunction) {
            return ((LazyFunction)method).getRealName();
        }
        // only quote if method is a member, we cannot get a conflict for local
        // since local methods have a $getter suffix
        if(!method.isClassOrInterfaceMember())
            return name;
        // do not quote method names if we have a refined constraint
        Function refinedMethod = (Function) method.getRefinedDeclaration();
        if(refinedMethod instanceof JavaMethod){
            return ((JavaMethod)refinedMethod).getRealName();
        }
        // get/is with at least one more letter, no parameter and non-void type
        if(((name.length() >= 4 && name.startsWith("get"))
             || name.length() >= 3 && name.startsWith("is"))
            && method.getFirstParameterList().getParameters().isEmpty()
            && !AbstractTransformer.isAnything(method.getType()))
            return quote(name);
        // set with one parameter and void type
        if((name.length() >= 4 && name.startsWith("set"))
           && method.getFirstParameterList().getParameters().size() == 1
           && AbstractTransformer.isAnything(method.getType()))
            return quote(name);
        return name;
    }

    private static String quoteMethodName(Function decl, int namingOptions){
        // always use the refined decl
        Declaration refinedDecl = decl.getRefinedDeclaration();  
        return getMethodName((Function)refinedDecl, namingOptions);
    
    }


    public static String getDefaultedParamMethodName(Declaration decl, Parameter param) {
        if (Decl.isConstructor(decl)) {
            Constructor constructor = Decl.getConstructor(decl);
            if (Decl.isDefaultConstructor(constructor)) {
                return compoundName(Decl.getConstructedClass(constructor).getName(), param.getName());
            } else {
                return compoundName(Decl.getConstructedClass(constructor).getName(), decl.getName(), param.getName());
            }
        } else if (decl instanceof Function) {
            if(decl.isAnonymous())
                return prefixName(Prefix.$default$, param.getName());
            return compoundName(((Function) decl).getName(), CodegenUtil.getTopmostRefinedDeclaration(param.getModel()).getName());
        } else if (decl instanceof ClassOrInterface ) {
            if (decl.isToplevel() || Decl.isLocalNotInitializer(decl)) {
                return prefixName(Prefix.$default$, param.getName());
            } else {
                return prefixName(Prefix.$default$, decl.getName() , param.getName());
            }
        } else if (decl == null) {
            return prefixName(Prefix.$default$, param.getName());
        } else {
            // Should never happen (for now at least)
            return null;
        }
    }
    
    String getInstantiatorMethodName(Functional classOrCtor) {
        // In the case of a constructor we use a constructor name parameter to tell the diff
        // the instantiator method still has the same name as the class.
        return suffixName(Suffix.$new$, classOrCtor.getName());
    }
    
    JCExpression makeInstantiatorMethodName(JCExpression qual, Class model) {
        return makeQualIdent(qual, getInstantiatorMethodName(model));
    }
    
    static String getAliasedParameterName(Parameter parameter) {
        return getAliasedParameterName(parameter.getModel());
    }
    
    static boolean aliasConstructorParameterName(FunctionOrValue mov) {
        return mov.getContainer() instanceof Constructor && !mov.isShared() && !mov.isCaptured();
    }
    
    private static String getAliasedParameterName(FunctionOrValue parameter) {
        if (!parameter.isParameter()) {
            throw new BugException();
        }
        FunctionOrValue mov = parameter;
        if ((mov instanceof Function && ((Function)mov).isDeferred())
                || (mov instanceof Value && mov.isVariable() && mov.isCaptured())
                || aliasConstructorParameterName(mov)) {
            return suffixName(Suffix.$param$, parameter.getName());
        }
        return quoteIfJavaKeyword(parameter.getName());
    }

    private TreeMaker maker;
    private Names names;
    private Context context;
    
    Naming(TreeMaker maker, Names names) {
        this.maker = maker;
        this.names = names;
    }
    
    Naming(Context context) {
        this.context = context;
        maker = TreeMaker.instance(context);
        names = Names.instance(context);
    }
    
    public static Naming instance(Context context) {
        Naming instance = context.get(Naming.class);
        if (instance == null) {
            instance = new Naming(context);
            context.put(Naming.class, instance);
        }
        return instance;
    }
    
    private TreeMaker make() {
        return maker;
    }
    
    private Names names() {
        return names;
    }
    
    private  CeylonTransformer gen() {
        return CeylonTransformer.getInstance(context);
    }
    
    /** 
     * Makes an <strong>unquoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCIdent makeUnquotedIdent(String ident) {
        return make().Ident(makeUnquotedName(ident));
    }

    JCIdent makeUnquotedIdent(Name ident) {
        return make().Ident(ident);
    }

    JCIdent makeUnquotedIdent(Unfix ident) {
        return make().Ident(makeUnquotedName(name(ident)));
    }

    /** 
     * Makes an <strong>unquoted</strong> simple name
     * @param ident The identifier
     * @return The name
     */
    Name makeUnquotedName(String ident) {
        return names().fromString(ident);
    }

    /** 
     * Makes an <strong>quoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCIdent makeQuotedIdent(String ident) {
        return make().Ident(makeQuotedName(ident));
    }

    /** 
     * Makes an <strong>quoted</strong> simple name
     * @param ident The identifier
     * @return The name
     */
    Name makeQuotedName(String ident) {
        return names().fromString(Naming.quoteIfJavaKeyword(ident));
    }

    /** 
     * Makes a <strong>quoted</strong> qualified (compound) identifier from 
     * the given qualified name. Each part of the name will be 
     * quoted if it is a Java keyword.
     * @param qualifiedName The qualified name 
     */
    JCExpression makeQuotedQualIdentFromString(String qualifiedName) {
        return makeQualIdent(null, Util.quoteJavaKeywords(qualifiedName.split("\\.")));
    }

    /** 
     * Makes an <strong>unquoted</strong> qualified (compound) identifier 
     * from the given qualified name components
     * @param expr A starting expression (may be null)
     * @param names The components of the name (may be null)
     * @see #makeQuotedQualIdentFromString(String)
     * @see #makeQualIdent(JCExpression, String)
     */
    private JCExpression makeQualIdent(JCExpression expr, String... names) {
        if (names != null) {
            for (String component : names) {
                if (component != null) {
                    if (expr == null) {
                        expr = makeUnquotedIdent(component);
                    } else {
                        expr = makeSelect(expr, component);
                    }
                }
            }
        }
        return expr;
    }
    
    JCExpression makeQuotedQualIdent(JCExpression expr, String... names) {
        if (names != null) {
            for (String component : names) {
                if (component != null) {
                    if (expr == null) {
                        expr = makeQuotedIdent(component);
                    } else {
                        expr = makeSelect(expr, Naming.quoteIfJavaKeyword(component));
                    }
                }
            }
        }
        return expr;
    }

    /**
     * Constructs a fully qualified, unquoted identifier from the given 
     * components (which should usually be literals).
     * @see #makeName()
     */
    JCExpression makeFQIdent(String... components) {
        return makeQualIdent(makeUnquotedIdent(""), components);
    }

    JCExpression makeQuotedFQIdent(String... components) {
        return makeQuotedQualIdent(makeUnquotedIdent(""), components);
    }

    JCExpression makeQuotedFQIdent(String qualifiedName) {
        return makeQuotedFQIdent(Util.quoteJavaKeywords(qualifiedName.split("\\.")));
    }

    JCExpression makeIdent(Type type) {
        return make().QualIdent(type.tsym);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(JCExpression s1, String s2) {
        return make().Select(s1, names().fromString(s2));
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(JCExpression s1, Name s2) {
        return make().Select(s1, s2);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(makeUnquotedIdent(s1), s2);
    }

    /**
     * Makes a <strong>unquoted</strong> field access
     * @param s1 The base expression
     * @param s2 The field to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, Name s2) {
        return makeSelect(makeUnquotedIdent(s1), s2);
    }

    JCExpression makeDefaultedParamMethod(JCExpression qualifier, Parameter param) {
        // TODO Can we merge this into makeName(..., NA_DPM) ?
        if (!Strategy.hasDefaultParameterValueMethod(param)) {
            throw new BugException();
        }
        Declaration decl = param.getDeclaration();
        String methodName = getDefaultedParamMethodName(decl, param);
        if (Strategy.defaultParameterMethodOnSelf(param.getModel())) {
            // method not within interface
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }
            JCExpression className = makeTypeDeclarationExpression(qualifier, (TypeDeclaration)container, DeclNameFlag.COMPANION); 
            return makeSelect(className, methodName);
        } else if (Strategy.defaultParameterMethodOnOuter(param.getModel())) {
            return makeQuotedQualIdent(qualifier, methodName);
        } else if (Strategy.defaultParameterMethodStatic(param.getModel())) {
            // top level method or class
            if (qualifier != null) {
                throw new BugException();
            }
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }
            if (container instanceof TypedDeclaration) {
                return makeSelect(makeName((TypedDeclaration)container, NA_FQ | NA_WRAPPER), methodName);
            } else {
                return makeSelect(gen().makeJavaType(((TypeDeclaration)container).getType(), AbstractTransformer.JT_RAW), methodName);
            }
        } else {
            // inner or local class or method, or method in an interface
            return makeQuotedQualIdent(qualifier, methodName);
        }
    }
    
    /**
     * Makes an ident for @{code ${qualifier}.this}, 
     * or plain {@code this} if the qualifier is null
     */
    JCExpression makeQualifiedThis(JCExpression qualifier) {
        if (qualifier == null) {
            return maker.Ident(names._this);
        } else {
            return maker.Select(qualifier, names._this);
        }
    }
    
    /**
     * Makes an ident for @{code this}.
     */
    JCExpression makeThis() {
        return makeQualifiedThis(null);
    }
    
    /**
     * Makes an ident for @{code $this}.
     */
    JCExpression makeQuotedThis() {
        return makeUnquotedIdent("$this");
    }

    JCExpression makeQualifiedDollarThis(JCExpression qualifier) {
        return maker.Select(qualifier, makeUnquotedName("$this"));
    }

    /**
     * Makes an ident for @{code super}.
     */
    JCExpression makeQualifiedSuper(JCExpression qualifier) {
        if (qualifier == null) {
            return maker.Ident(names._super);
        } else {
            return maker.Select(qualifier, names._super);
        }
    }
    JCExpression makeSuper() {
        return makeQualifiedSuper(null);
    }
    
    String getName(TypedDeclaration decl, int namingOptions) {
        TypeDeclarationBuilder<String> builder = new StringDeclName(decl, "");
        return makeQualifiedNameInternal(builder, decl, namingOptions);
        
    }
    
    JCExpression makeName(TypedDeclaration decl, int namingOptions) {
        TypeDeclarationBuilder<JCExpression> builder = new TreeDeclName(decl, 
                (namingOptions & NA_FQ) != 0 ? maker.Ident(names.empty) : null);
        return makeQualifiedNameInternal(builder, decl, namingOptions);
    }
    
    JCExpression makeQualifiedName(JCExpression qualifyingExpr, TypedDeclaration decl, int namingOptions) {
        TypeDeclarationBuilder<JCExpression> builder = new TreeDeclName(decl, 
                qualifyingExpr);
        return makeQualifiedNameInternal(builder, decl, namingOptions);
    }
    
    private <R> R makeQualifiedNameInternal(TypeDeclarationBuilder<R> builder, TypedDeclaration decl, int namingOptions) {
        // TODO Don't build a list but rather construct a JCExpression directly
        if (namingOptions == 0) {
            throw new BugException();
        }
        /*if (qualifyingExpr != null 
                && ((namingOptions & NA_FQ) != 0 
                    || (namingOptions & NA_WRAPPER) != 0
                    || (namingOptions & NA_WRAPPER_UNQUOTED) != 0)) {
            throw new BugException();
        }*/
        addNamesForWrapperClass(builder, decl, namingOptions);
        if ((namingOptions & (NA_MEMBER | NA_IDENT)) != 0) {
            addMemberName(builder, decl, namingOptions);
        }        
        return builder.result();
    }
    
    private <R> void addMemberName(TypeDeclarationBuilder<R> builder, TypedDeclaration decl, int namingOptions) {
        if (Decl.isJavaField(decl)) {
            String name = ((FieldValue)decl).getRealName();
            builder.select(name);
        } else if ((namingOptions & NA_IDENT) != 0) {
            if ((namingOptions & NA_GETTER | NA_SETTER) == 0) {
                throw new BugException();
            }
            String name;
            if ((namingOptions & __NA_IDENT_PARAMETER_ALIASED) != 0) {
                name = Naming.getAliasedParameterName((FunctionOrValue)decl);
            } else {
                name = substitute(decl);
                
            }
            builder.select(name);
        } else if ((namingOptions & NA_SETTER) != 0) {
            if (decl instanceof Function) {
                throw new BugException("A method has no setter");
            }
            builder.select(getSetterName(decl));
        } else if ((namingOptions & NA_GETTER) != 0) {
            if (decl instanceof Function) {
                throw new BugException("A method has no getter");
            }
            builder.select(getGetterName(decl));
        } else if (decl instanceof Value
                && !((Value)decl).isParameter()) {
            builder.select(getGetterName(decl));
        } else if (decl instanceof Setter) {
            builder.select(getSetterName(decl.getName()));
        } else if (decl instanceof Function
                && ((!decl.isParameter() || decl.isShared() || decl.isCaptured())
                        // if we want it aliased, it means we're in a constructor and we don't want
                        // the member name ever for parameters, so let's never fall into that branch and skip
                        // to the next one
                        && (namingOptions & NA_ALIASED) == 0)) {
            builder.select(getMethodName(decl, namingOptions));
        } else if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()) {
            if ((namingOptions & NA_ALIASED) != 0) {
                builder.select(getAliasedParameterName((FunctionOrValue)decl));
            } else {
                builder.select(decl.getName());
            }
        } 
    }
    
    private <R> void addNamesForWrapperClass(TypeDeclarationBuilder<R> builder, TypedDeclaration decl, int namingOptions) {
        if ((namingOptions & NA_FQ) != 0) {
            if ((namingOptions & NA_WRAPPER) == 0
                    && (namingOptions & NA_WRAPPER_UNQUOTED) == 0) {
                throw new BugException("If you pass FQ you must pass WRAPPER or WRAPPER_UNQUOTED too, or there's no class name to qualify!");
            }
            List<String> outerNames = null;
            Scope s = decl.getContainer();
            while (s != null) {
                if (s instanceof Package) {
                    final List<String> packageName = ((Package) s).getName();
                    for (int ii = 0; ii < packageName.size(); ii++) {
                        if (ii == 0 && packageName.get(ii).isEmpty()) {
                            continue;
                        }
                        builder.select(quoteIfJavaKeyword(packageName.get(ii)));
                    }
                    break;
                } else if (s instanceof ClassOrInterface) {
                    if (outerNames == null) {
                        outerNames = new ArrayList<String>(2);
                    }
                    outerNames.add(getQuotedClassName((ClassOrInterface) s, 0));
                } else if (s instanceof TypedDeclaration) {
                    if (outerNames == null) {
                        outerNames = new ArrayList<String>(2);
                    }
                    outerNames.add(quoteIfJavaKeyword(((TypedDeclaration) s).getName()));
                }
                s = s.getContainer();
            }
            if (outerNames != null) {
                for (int ii = outerNames.size()-1; ii >= 0; ii--) {
                    String outerName = outerNames.get(ii);
                    builder.select(outerName);
                }
            }
        }
        if ((namingOptions & NA_WRAPPER) != 0) {
            builder.select(getQuotedClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        } else if ((namingOptions & NA_WRAPPER_UNQUOTED) != 0) {
            builder.select(getRealName(decl, namingOptions & (NA_GETTER | NA_SETTER | NA_WRAPPER_UNQUOTED)));
        } else if ((namingOptions & NA_Q_LOCAL_INSTANCE) != 0) {
            if (Decl.isBoxedVariable(decl)) {
                builder.select(getVariableBoxName(decl));
            } else {
                builder.select(getAttrClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
            }
        } 
        if((namingOptions & NA_WRAPPER_WITH_THIS) != 0){
            builder.select("this");
        }
    }
    
    /**
     * A select <i>expr.name</i> if both arguments are non-null,
     * an unquoted ident for 'name' if the given expression is null,
     * or the given expression if the given name is null.
     * 
     * It is an error to call this method with two null arguments.
     * @see #makeQualIdent(JCExpression, String...)
     */
    JCExpression makeQualIdent(JCExpression expr, String name) {
        if (expr == null && name == null) {
            throw new BugException();
        }
        if (expr == null) {
            return makeUnquotedIdent(name);
        } else if (name == null) {
            return expr;
        } else {
            return makeSelect(expr, name);
        }
    }
    
    static String escapeClassName(String name) {
        // escape # of anonymous class names with underscore, we will save the name in @Name anyways
        return name.replace('#', '_');
    }

    static String quoteClassName(String name) {
        name = escapeClassName(name);
        return JvmBackendUtil.isInitialLowerCase(name) ? name + "_" : name;
    }
    
    /** 
     * Gets the class name of the given declaration, with the given options
     * @param decl The declaration
     * @param namingOptions Only {@link #NA_SETTER}, {@link #NA_GETTER} and 
     * {@link #NA_WRAPPER_UNQUOTED} are supported.
     */
    private static String getRealName(Declaration decl, int namingOptions) {
        String name;
        if (decl instanceof LazyValue) {
            name = ((LazyValue)decl).getRealName();
        } else if (decl instanceof LazyFunction) {
            name = ((LazyFunction)decl).getRealName();
        } else if (decl instanceof LazyClass) {
            name = ((LazyClass)decl).getRealName();
        } else if (decl instanceof LazyInterface) {
            name = ((LazyInterface)decl).getRealName();
        } else if (Decl.isGetter(decl)) {
            name = getAttrClassName((Value)decl, namingOptions);
        } else if (decl instanceof Setter) {
            name = getAttrClassName((Setter)decl, namingOptions);
        } else {
            name = decl.getName();
            if ((namingOptions & NA_WRAPPER_UNQUOTED) == 0) {
                name = quoteClassName(name);
            }
        }
        return name;
    }
    
    private static String getQuotedClassName(Declaration decl, int namingOptions) {
        return getRealName(decl, namingOptions);
    }
    
    String getVariableBoxName(TypedDeclaration declaration) {
        return declaration.getName();
    }
    
    JCExpression makeVariableBoxName(TypedDeclaration declaration) {
        return makeUnquotedIdent(getVariableBoxName(declaration));
    }
    
    /** Include the member part of the typed declaration (e.g. the method name) */
    static final int NA_MEMBER = 1<<0;
    /** Include the wrapper class of the typed declaration */
    static final int NA_WRAPPER = 1<<1;
    /** Include the wrapper class of the typed declaration, but don't quote it */
    static final int NA_WRAPPER_UNQUOTED = 1<<2;
    static final int NA_Q_LOCAL_INSTANCE = 1<<6;
    /** Generate a fully qualified name. Requires {@code NA_WRAPPER} */
    static final int NA_FQ = 1<<3;
    /** Generate the name of the getter (otherwise the type of the declaration 
     * will be used to determine the name to be generated) */
    static final int NA_GETTER = 1<<4;
    /** Generate the name of the setter (otherwise the type of the declaration 
     * will be used to determine the name to be generated) */
    static final int NA_SETTER = 1<<5;
    static final int NA_IDENT = 1<<7;
    /**
     * Reference the constructor parameter name directly, possibly aliased, rather
     * than the member it will end up stored in.
     */
    static final int NA_ALIASED = 1<<8;
    /** A shared parameter on an annotation class produces an annotation type 
     * method foo, not getFoo() */
    static final int NA_ANNOTATION_MEMBER = 1<<9;
    /** Add a ".this" selector to the wrapper class */
    static final int NA_WRAPPER_WITH_THIS = 1<<10;
    /** Add "$" to the end of the method name */
    static final int NA_CANONICAL_METHOD = 1<<11;

    private static final int __NA_IDENT_PARAMETER_ALIASED = 1<<12;
    static final int NA_IDENT_PARAMETER_ALIASED = NA_IDENT | __NA_IDENT_PARAMETER_ALIASED;
    
    
    /**
     * Returns the name of the Java method/field for the given declaration 
     * @param decl The declaration
     * @return The name of the corresponding Java declaration.
     */
    String selector(TypedDeclaration decl) {
        return selector(decl, 0);
    }
    public static String selector(TypedDeclaration decl, int namingOptions) {
        if ((namingOptions & NA_ANNOTATION_MEMBER) == 0 &&
                (Decl.isGetter(decl) || Decl.isValueOrSharedOrCapturedParam(decl))) {
            if ((namingOptions & NA_SETTER) != 0) {
                return getSetterName(decl);
            } else {
                return getGetterName(decl);
            }
        } else if (decl instanceof Function) {
            if (decl.isClassMember()) {
                // don't try to be smart with interop calls 
                if(decl instanceof JavaMethod)
                    return ((JavaMethod)decl).getRealName();
                return getMethodName(decl, namingOptions);
            }
            return quoteMethodName((Function)decl, namingOptions);
        } else if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter()) {
            return getMethodName(decl, namingOptions);
        }
        throw new BugException();
    }
    
    /**
     * Gets the class name for the attribute wrapper class. The class name 
     * will be quoted unless namingOptions includes NA_WRAPPER_UNQUOTED
     */
    static String getAttrClassName(TypedDeclaration decl, int namingOptions) {
        if ((namingOptions & ~(NA_SETTER | NA_GETTER | NA_WRAPPER_UNQUOTED)) != 0) {
            throw new BugException("unsupported namingOption");
        }
        String name = decl.getName();
        if (Decl.isLocal(decl)) {
            if ((Decl.isGetter(decl) && (namingOptions & NA_SETTER) == 0)
                    || (namingOptions & NA_GETTER) != 0){
                name = suffixName(Suffix.$getter$, name);
            } else if ((decl instanceof Setter && (namingOptions & NA_GETTER) == 0)
                    || (namingOptions & NA_SETTER) != 0) {
                name = suffixName(Suffix.$setter$, name);
            }
        }
        if ((namingOptions & NA_WRAPPER_UNQUOTED) == 0) {
            name = quoteClassName(name);
        }
        return name;
    }
    
    JCExpression makeSyntheticClassname(Declaration decl) {
        return makeUnquotedIdent(getQuotedClassName(decl, 0));
    }
    Name getSyntheticInstanceName(Declaration decl) {
        return names.fromString(decl.getName());
    }
    
    /**
     * Returns the name of the field in classes which holds the companion 
     * instance.
     */
    final String getCompanionFieldName(Interface def) {
        // resolve aliases
        if(def.isAlias())
            def = (Interface) def.getExtendedType().getDeclaration();
        return suffixName(Suffix.$this$, "$" + Decl.className(def).replace('.', '$'));
    }

    /**
     * Returns the name of the field in classes which holds the companion 
     * instance.
     */
    JCExpression makeCompanionFieldName(Interface def) {
        return makeUnquotedIdent(getCompanionFieldName(def));
    }
    
    /** 
     * Returns the name of the method in interfaces and classes used to get 
     * the companion instance.
     */
    final String getCompanionAccessorName(Interface def) {
        return getCompanionClassName(def, true).replace('.', '$');
    }
    
    JCExpression makeCompanionAccessorName(JCExpression qualExpr, Interface def) {
        return makeQualIdent(qualExpr, getCompanionAccessorName(def));
    }
    
    JCExpression makeCompanionAccessorCall(JCExpression qualExpr, Interface def) {
        return make().Apply(null, 
                makeCompanionAccessorName(qualExpr, def), 
                com.sun.tools.javac.util.List.<JCExpression>nil());
    }
    
    /** Generates an ident for the getter method of a Value */
    JCExpression makeLanguageValue(String string) {
        Declaration decl = gen().typeFact().getLanguageModuleDeclaration(string);
        if (!Decl.isValue(decl)) {
            throw new BugException();
        }
        return makeSelect(makeName((Value)decl, NA_FQ | NA_WRAPPER), getGetterName(decl));
    }
    
    /** Generates an ident for the getter method of a Value */
    JCExpression makeLanguageFunction(String string) {
        Declaration decl = gen().typeFact().getLanguageModuleDeclaration(string);
        if (!Decl.isMethod(decl)) {
            throw new BugException();
        }
        return makeSelect(makeName((TypedDeclaration)decl, NA_FQ | NA_WRAPPER), getMethodNameInternal((TypedDeclaration)decl));
    }
    
    JCExpression makeLanguageSerializationValue(String string) {
        Declaration decl = gen().typeFact().getLanguageModuleSerializationDeclaration(string);
        if (!Decl.isValue(decl)) {
            throw new BugException();
        }
        return makeSelect(makeName((Value)decl, NA_FQ | NA_WRAPPER), getGetterName(decl));
    }
    
    /*
     * Methods for making unique temporary and alias names
     */
    
    static class UniqueId {
        private long id = 0;
        private long nextId() {
            return id++;
        }
    }
    
    private long nextUniqueId() {
        UniqueId id = context.get(UniqueId.class);
        if (id == null) {
            id = new UniqueId();
            context.put(UniqueId.class, id);
        }
        return id.nextId();
    }
    
    String newTemp() {
        String result = prefixName(Prefix.$ceylontmp$, Long.toString(nextUniqueId()));
        return result;
    }

    String newTemp(String prefix) {
        String result = prefixName(Prefix.$ceylontmp$, prefix, Long.toString(nextUniqueId()));
        return result;
    }

    private String newAlias(String name) {
        String result = compoundName(name, Long.toString(nextUniqueId()));
        return result;
    }
    
    /** 
     * Allocates a new temporary name and returns the Name for it.
     * @see #temp()
     */
    Name tempName() {
        return names.fromString(newTemp());
    }
    
    /** 
     * Allocates a new temporary name with the given prefix
     * and returns the Name for it.
     * @see #tempName(String)
     */
    Name tempName(String prefix) { 
        return names.fromString(newTemp(prefix));
    }
    
    /** 
     * Allocates a new alias based on the given name 
     * and returns the Name for it.
     * @see #alias(String)
     */
    Name aliasName(String name) {
        return names.fromString(newAlias(name));
    }
    
    /** 
     * Allocates a new temporary name 
     * and returns a {@link SyntheticName} for it.
     * 
     * This is preferred over {@link #tempName()}
     * and {@link #makeTemp()} 
     * in situations where a Name and JCIdents are required.
     */
    SyntheticName temp() {
        return new SyntheticName(tempName());
    }
    
    /** 
     * Allocates a new temporary name with the given prefix
     * and returns a {@link SyntheticName} for it.
     * 
     * This is preferred over {@link #tempName(String)} 
     * and {@link #makeTemp(String)} 
     * in situations where a Name and JCIdents are required.
     */
    SyntheticName temp(String prefix) { 
        return new SyntheticName(tempName(prefix));
    }
    
    /** 
     * Allocates a new alias based on the given name 
     * and returns a {@link SyntheticName} for it.
     * 
     * This is preferred over {@link #aliasName(String)} 
     * and {@link #makeAlias(String)} 
     * in situations where a Name and JCIdents are required.
     */
    SyntheticName alias(String name) {
        return new SyntheticName(aliasName(name));
    }
    
    /**
     * Allocates a new temporary name and returns a JCIdent for it.
     * @see #temp() 
     */
    JCIdent makeTemp() {
        return new SyntheticName(tempName()).makeIdent();
    }
    
    /**
     * Allocates a new temporary name based on the given prefix 
     * and returns a JCIdent for it.
     * @see #temp(String)
     */
    JCIdent makeTemp(String prefix) { 
        return new SyntheticName(tempName(prefix)).makeIdent();
    }
    
    /** 
     * Allocates a new alias based on the given name 
     * and returns a JCIdent for it.
     * @see #alias(String)
     */
    JCIdent makeAlias(String name) {
        return new SyntheticName(aliasName(name)).makeIdent();
    }
    
    interface CName {
        
        /**
         * Returns the name
         */
        public String getName();
        
        public String getUnsubstitutedName();
        
        /**
         * This name as a Name
         */
        public Name asName();
        
        /**
         * A new JCIdent for this name.
         */
        public JCIdent makeIdent();
        
        public JCExpression makeIdentWithThis();
    }
    
    /**
     * Encapsulates a temporary name or alias 
     */
    class SyntheticName implements CName {
        
        private final Name name;
        
        private SyntheticName(Name name) {
            this.name = name;
        }
        
        /**
         * Returns the name
         */
        public String toString() {
            return name.toString();
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SyntheticName other = (SyntheticName) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }

        /**
         * Returns the name
         */
        public String getName() {
            return name.toString();
        }
        
        public String getUnsubstitutedName() {
            return getName();
        }
        
        /**
         * This name as a Name
         */
        public Name asName() {
            return name;
        }
        
        /**
         * A new JCIdent for this name.
         */
        public JCIdent makeIdent() {
            return make().Ident(asName());
        }
        
        public JCExpression makeIdentWithThis() {
            return makeSelect("this", asName());
        }
        
        /**
         * Returns a new SyntheticName which appends the given suffix onto 
         * this SyntheticName's name.
         */
        SyntheticName suffixedBy(Suffix suffix) {
            return new SyntheticName(names.fromString(suffixName(suffix, getName())));
        }
        
        SyntheticName suffixedBy(Suffix suffix, int i) {
            return new SyntheticName(names.fromString(getName() + suffix + Integer.toString(i)));
        }
        
        SyntheticName suffixedBy(int i) {
            return new SyntheticName(names.fromString(getName() + "$" + Integer.toString(i)));
        }
        
        SyntheticName alias() {
            return Naming.this.alias(getName());
        }
    }
    
    class SubstitutedName implements CName {
        
        private TypedDeclaration decl;
        
        private SubstitutedName(TypedDeclaration decl) {
            this.decl = decl;
        }
        
        /**
         * Returns the name
         */
        public String toString() {
            return getName();
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            String name = getName();
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SyntheticName other = (SyntheticName) obj;
            String aName = getName();
            String bName = other.getName();
            if (aName == null) {
                if (bName != null)
                    return false;
            } else if (!aName.equals(bName))
                return false;
            return true;
        }

        /**
         * Returns the name
         */
        public String getName() {
            return substitute(decl);
        }
        
        public String getUnsubstitutedName() {
            return decl.getName();
        }
        
        /**
         * This name as a Name
         */
        public Name asName() {
            return names.fromString(getName());
        }
        
        /**
         * A new JCIdent for this name.
         */
        public JCIdent makeIdent() {
            return make().Ident(asName());
        }
        
        public JCExpression makeIdentWithThis() {
            return makeSelect("this", asName());
        }
        
        public SyntheticName capture() {
            return new SyntheticName(asName());
        }
    }
    public SubstitutedName substituted(TypedDeclaration decl) {
        return new SubstitutedName(decl);
    }
    
    /*
     * Variable name substitution
     */
    
    protected static class VarMapper {
        /** 
         * A key for the substituton map, composed of the declaration's name and
         * its original declaration's containing scope. This allows 
         * substitutions to not be block structured (indeed for 
         * {@link Substitution#close()} to never be called) without 
         * the substitution leaking outside the (Ceylon) scope in which the 
         * variable is defined. 
         */
        private static class SubstitutionKey {
            private final String name;
            private final Scope scope;
            SubstitutionKey(TypedDeclaration decl) {
                super();
                this.name = decl.getName();
                TypedDeclaration orig = decl.getOriginalDeclaration();
                Scope stop = (orig != null ? orig : decl).getContainer();
                Scope scope = decl.getContainer();
                while (!Decl.equalScopes(scope, stop)) {
                    if (!(scope instanceof ControlBlock)) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                this.scope = scope;
            }
            @Override
            public String toString() {
                return name + " in " + scope;
            }
            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result
                        + ((name == null) ? 0 : name.hashCode());
                result = prime * result
                        + ((scope == null) ? 0 : scope.hashCode());
                return result;
            }
            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (getClass() != obj.getClass())
                    return false;
                SubstitutionKey other = (SubstitutionKey) obj;
                if (name == null) {
                    if (other.name != null)
                        return false;
                } else if (!name.equals(other.name))
                    return false;
                if (scope == null) {
                    if (other.scope != null)
                        return false;
                } else if (!scope.equals(other.scope))
                    return false;
                return true;
            }
            
        }
        private Map<SubstitutionKey, String> map = new HashMap<>();
        public String put(TypedDeclaration decl, String value) {
            return map.put(new SubstitutionKey(decl), value);
        }
        public String remove(TypedDeclaration decl) {
            return map.remove(new SubstitutionKey(decl));
        }
        public String get(TypedDeclaration decl) {
            SubstitutionKey key = new SubstitutionKey(decl);
            if (map.containsKey(key)) {
                return map.get(key);
            }
            return quoteIfJavaKeyword(key.name);
        }
        public void clear() {
            map.clear();
        }
        public boolean isSubstituted(TypedDeclaration decl) {
            SubstitutionKey key = new SubstitutionKey(decl);
            return map.containsKey(key);
        }
    }
    
    private VarMapper getVarMapper() {
        VarMapper map = context.get(VarMapper.class);
        if (map == null) {
            map = new VarMapper();
            context.put(VarMapper.class, map);
        }
        return map;
    }
    
    /**
     * Create a variable substitution for the given declaration, using the 
     * given substituted name. The substitution will be scoped to the end of 
     * the declaring scope of the original declaration of the given declaration,
     * or until {@link Substitution#close()} is called.
     */
    Substitution addVariableSubst(TypedDeclaration decl, String substVarName) {
        return new Substitution(decl, substVarName);
    }
    
    /** 
     * Removes a previously created Substitution 
     */
    void removeVariableSubst(Substitution substitution) {
        if (substitution.previous != null) {
            getVarMapper().put(substitution.original, substitution.previous);
        } else {
            getVarMapper().remove(substitution.original);
        }
    }
    
    /**
     * Checks a global map of variable name substitutions and returns
     * either the original name if none was found or the substitute.
     */
    String substitute(Declaration decl) {
        if (decl instanceof TypedDeclaration) {
            return getVarMapper().get((TypedDeclaration)decl);
        }
        return decl.getName();
    }

    /**
     * Returns true if the given declaration is currently substituted
     */
    boolean isSubstituted(Declaration decl) {
        if (decl instanceof TypedDeclaration) {
            return getVarMapper().isSubstituted((TypedDeclaration)decl);
        }
        return false;
    }

    /**
     * A substitution
     */
    class Substitution implements AutoCloseable {
        public final TypedDeclaration original;
        public final String substituted;
        public final String previous;
        private boolean closed = false;
        private boolean scoped = false;
        
        Substitution(TypedDeclaration decl, String substituted) {
            this.original = decl;
            this.substituted = substituted;
            this.previous = getVarMapper().put(decl, substituted);
        }
        
        @Override
        public String toString() {
            if (closed) {
                return "Spent substitution";
            }
            return "Substituting " + substituted + " for " + original + " (masking " + previous + ")" + original.getScope();
        }
        
        @Override
        public void close() {
            if (!scoped) {
                internalClose();
            }
        }

        private void internalClose() {
            if (closed) {
                throw new IllegalStateException();
            }
            removeVariableSubst(this);
            closed = true;
        }
        
        public void scopeClose(Scope scope) {
            if (closed) {
                throw new IllegalStateException();
            }
            this.scoped = true;
            com.sun.tools.javac.util.List<Substitution> list = scopedSubstitutions.get(scope);
            if (list == null) {
                list = com.sun.tools.javac.util.List.<Substitution>nil();
            }
            if (list.contains(this)) {
                throw new IllegalStateException();
            }
            list = list.prepend(this);
            scopedSubstitutions.put(scope, list);
        }
    }
    
    private Map<Scope, com.sun.tools.javac.util.List<Substitution>> scopedSubstitutions = new HashMap<>();
    
    public void closeScopedSubstitutions(Scope scope) {
        com.sun.tools.javac.util.List<Substitution> substitutions = scopedSubstitutions.remove(scope);
        if (substitutions != null) {
            for (Substitution s : substitutions) {
                s.internalClose();
            }
        }
        
        for (Map.Entry<Scope, com.sun.tools.javac.util.List<Substitution>> entry : scopedSubstitutions.entrySet()) {
            Scope s = entry.getKey();
            while (true) {
                if (s.equals(scope)) {
                    throw new RuntimeException("Unclosed scoped substitution(s) " + entry.getValue() + " whose scope is contained within " + scope);
                }
                s = s.getScope();
                if (s == null || s instanceof Package) {
                    break;
                }
            }
        }
    }
    
    /** 
     * Generates an alias for the given declaration, and adds a 
     * substitution for the given declaration using this alias. 
     */
    Substitution substituteAlias(TypedDeclaration decl) {
        return addVariableSubst(decl, alias(substitute(decl)).getName());
    }
    
    @Deprecated
    SyntheticName synthetic(String name) {
        return new SyntheticName(names.fromString(name));
    }
    
    SyntheticName synthetic(Prefix prefix, String... name) {
        return new SyntheticName(names.fromString(prefixName(prefix, name)));
    }
    
    SyntheticName synthetic(Unfix name) {
        return new SyntheticName(names.fromString(name(name)));
    }
    
    SyntheticName synthetic(Prefix name, int i) {
        return new SyntheticName(names.fromString(prefixName(name, Integer.toString(i))));
    }
    
    SyntheticName synthetic(Value value) {
        return new SyntheticName(names.fromString(quoteIfJavaKeyword(value.getName())));
    }
    
    SyntheticName synthetic(Tree.Variable var) {
        return new SyntheticName(names.fromString(getVariableName(var)));
    }
    
    SyntheticName synthetic(Tree.Pattern pattern) {
        if (pattern instanceof Tree.VariablePattern) {
            return synthetic(((Tree.VariablePattern)pattern).getVariable());
        } else if (pattern instanceof Tree.KeyValuePattern) {
            return synthetic(Prefix.$pattern$, "entry").alias();
        } else if (pattern instanceof Tree.TuplePattern) {
            return synthetic(Prefix.$pattern$, "tuple").alias();
        } else {
            throw BugException.unhandledCase(pattern);
        }
    }
    
    SyntheticName syntheticDestructure(Tree.Statement varOrDes) {
        if (varOrDes instanceof Tree.Variable) {
            return synthetic((Tree.Variable)varOrDes);
        } else if (varOrDes instanceof Tree.Destructure) {
            return synthetic(((Tree.Destructure)varOrDes).getPattern());
        } else {
            throw BugException.unhandledCase(varOrDes);
        }
    }
    
    SyntheticName synthetic(Tree.ForIterator forIterator) {
        if (forIterator instanceof Tree.ValueIterator) {
            return synthetic(((Tree.ValueIterator)forIterator).getVariable());
        } else if (forIterator instanceof Tree.PatternIterator) {
            return synthetic(((Tree.PatternIterator)forIterator).getPattern());
        } else {
            throw BugException.unhandledCase(forIterator);
        }
    }
    
    void clearSubstitutions(Declaration decl) {
        if (decl.isToplevel()) {
            // Clear all the substitutions before each top level declaration
            getVarMapper().clear();
        }
    }
    
    @Override
    public String getLocalId(Scope decl) {
        Integer id = locals.get(decl);
        if (id == null) {
            throw new RuntimeException(decl + " has no local id");
        }
        return String.valueOf(id);
    }

    /**
     * Makes a name for a local attribute where we store a method specifier
     */
    public String getMethodSpecifierAttributeName(Function m) {
        return suffixName(Suffix.$specifier$, m.getName());
    }
    
    public static String getCallableMethodName() {
        return name(Unfix.$call$);
    }

    public static String getCallableTypedMethodName() {
        return name(Unfix.$calltyped$);
    }
    
    public static String getCallableVariadicMethodName() {
        return name(Unfix.$callvariadic$);
    }
    
    public static String getCallableMethodName(Function method) {
        java.util.List<Parameter> parameters = method.getFirstParameterList().getParameters();
        boolean variadic = !parameters.isEmpty() && parameters.get(parameters.size()-1).isSequenced();
        return variadic ? getCallableVariadicMethodName() : getCallableMethodName();
    }
    
    public static String getCallableTempVarName(Parameter param){
        return prefixName(Prefix.$ceylontmp$, param.getName());
    }
    
    public static String getImplClassName(String name){
        return suffixName(Suffix.$impl, name);
    }
    
    public String getTypeArgumentDescriptorName(TypeParameter tp) {
        String name;
        if(tp.isCaptured()){
            // must build unique name
            StringBuilder sb = new StringBuilder();
            LinkedList<Declaration> decls = new LinkedList<Declaration>();
            Scope scope = tp;
            while(scope != null && scope instanceof Package == false){
                if(scope instanceof Declaration)
                    decls.add((Declaration) scope);
                scope = scope.getContainer();
            }
            Iterator<Declaration> iterator = decls.descendingIterator();
            while(iterator.hasNext()){
                sb.append(iterator.next().getName());
                if(iterator.hasNext())
                    sb.append("$");
            }
            name = sb.toString();
        }else{
            name = tp.getName();
        }
        return prefixName(Prefix.$reified$, name);
    }
    
    public String getGetTypeMethodName() {
        return name(Unfix.$getType$);
    }

    public String getRefineTypeParametersMethodName() {
        return name(Unfix.$refine$);
    }

    public String getTypeDescriptorAliasName() {
        return name(Unfix.$TypeDescriptor$);
    }
    
    /**
     * Computes the name of the constant field on the class for an 
     * annotation constructor. The name comprises a number of parts because 
     * defaulted parameters and literal arguments both require constant fields
     * and because of nested invocations we need to generate a unique name
     */
    public static String getAnnotationFieldName(java.util.List<AnnotationFieldName> parts) {
        StringBuilder sb = new StringBuilder();
        for (AnnotationFieldName part : parts) {
            sb.append(part.getFieldNamePrefix()).append(part.getFieldName()).append("$");
        }
        return sb.substring(0, sb.length()-1);
    }

    public String getSequencedAnnotationMethodName() {
        return name(Unfix.value);
    }

    public String getAnnotationSequenceMethodName() {
        return name(Unfix.$annotationSequence$);
    }
    
    public static String getToplevelAttributeSavedExceptionName() {
        return name(Unfix.$initException$);
    }
    
    public static boolean isLowerCase(String name){
        return !name.isEmpty() && Character.isLowerCase(name.codePointAt(0));
    }

    public static String getInitializationFieldName(String fieldName) {
        return prefixName(Prefix.$init$, quoteFieldName(fieldName));
    }
    
    public JCExpression makeNamedConstructorName(Constructor constructor, boolean delegation) {
        DeclNameFlag[] flags = delegation ? new DeclNameFlag[]{DeclNameFlag.QUALIFIED, DeclNameFlag.DELEGATION}: new DeclNameFlag[]{DeclNameFlag.QUALIFIED};
        Class cls = (Class)constructor.getContainer();
        if (cls.isToplevel() || 
                (cls.isMember() && ((TypeDeclaration)cls.getContainer()).isToplevel())) {
            return makeTypeDeclarationExpression(null, constructor, flags);
        } else {
            return maker.TypeCast(
                    makeTypeDeclarationExpression(null, constructor, flags),
                    //makeTypeDeclarationExpression(makeTypeDeclarationExpression(null, cls, DeclNameFlag.QUALIFIED), constructor, DeclNameFlag.QUALIFIED),
                    make().Literal(TypeTags.BOT, null));
        }
    }
    
    public JCExpression makeNamedConstructorType(Constructor constructor, boolean delegation) {
        DeclNameFlag[] flags = delegation ? new DeclNameFlag[]{DeclNameFlag.QUALIFIED, DeclNameFlag.DELEGATION}: new DeclNameFlag[]{DeclNameFlag.QUALIFIED};
        return makeTypeDeclarationExpression(null, constructor, flags);
    }

    /**
     * uRLDecoder is ambiguous because we generate the getURLDecoder getter which gets decoded to urlDecoder,
     * so for those we have to remember the exact name
     */
    public static boolean isAmbiguousGetterName(TypedDeclaration attr) {
        return !attr.isToplevel() && isAmbiguousGetterName(attr.getName());
    }

    /**
     * uRLDecoder is ambiguous because we generate the getURLDecoder getter which gets decoded to urlDecoder,
     * so for those we have to remember the exact name.
     */
    public static boolean isAmbiguousGetterName(String name) {
        int cpl = name.codePointCount(0, name.length());
        if(cpl < 2)
            return false;
        int f = name.codePointAt(0);
        if(!Character.isLowerCase(f) && f != '_')
            return false;
        int s = name.codePointAt(1);
        return Character.isUpperCase(s);
    }
}


