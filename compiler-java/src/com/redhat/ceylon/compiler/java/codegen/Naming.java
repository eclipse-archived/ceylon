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
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.ControlBlock;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.parser.Token;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public class Naming implements LocalId {

    private static final String IMPL_POSTFIX = "$impl";
    private static final String ANNO_POSTFIX = "$annotation";
    private static final String ANNOS_POSTFIX = "$annotations";

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
        ANNOTATIONS
    }
    
    /** Quote the given name by prefixing it with a dollar ($) */
    public static String quote(String name) {
        return "$"+name;
    }

    private static final HashSet<String> tokens;
    static {
        tokens = new HashSet<String>();
        for (Token t : Token.values()) {
            tokens.add(t.name);
        }
    }
    /** Determines whether the given name is a Java keyword */
    public static boolean isJavaKeyword(String name) {
        return tokens.contains(name);
    }
    
    /** Prefixes the given name with a dollar ($) if it is a Java keyword */
    public static String quoteIfJavaKeyword(String name){
        if(isJavaKeyword(name))
            return quote(name);
        return name;
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
                "clone"));
    }
    
    private static String getMethodName(TypedDeclaration decl, int namingOptions) {
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
            } else if ("clone".equals(name)
                    && Decl.getClassOrInterfaceContainer(decl).getType().isSubtypeOf(
                            ((TypeDeclaration)decl.getUnit().getLanguageModuleDeclaration("Cloneable")).getType())) {
                return "clone";    
            } 
            return getMethodNameInternal(decl);
        } else {
            return getMethodNameInternal(decl);
        }
    }

    private static String getMethodNameInternal(TypedDeclaration decl) {
        String name;
        if (decl.isClassOrInterfaceMember()
                && decl instanceof Method) {
            name = quoteMethodNameIfProperty((Method)decl);
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

    private static String getErasedGetterName(Declaration decl) {
        String property = decl.getName();
        // ERASURE
        if (!(decl instanceof Parameter) || ((Parameter)decl).isShared()) {
            if ("hash".equals(property)) {
                return "hashCode";
            } else if ("string".equals(property)) {
                return "toString";
            }
        }
        
        String getterName = getGetterName(property);
        if (decl.isMember() && !decl.isShared()) {
            getterName += "$priv";
        }
        return getterName;
    }

    /**
     * Removes any leading $ from the given string.
     */
    public static String stripLeadingDollar(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    /** 
     * @deprecated Use of this method outside this package is 
     * <strong>strongly</strong> discouraged. 
     * Its public modifier will be removed at a future date.
     */
    public static String getGetterName(String property) {
        return "get"+capitalize(stripLeadingDollar(property));
    }

    /** 
     * @deprecated Use of this method outside this package is 
     * <strong>strongly</strong> discouraged. 
     * Its public modifier will be removed at a future date.
     */
    public static String getSetterName(String property){
        return "set"+capitalize(stripLeadingDollar(property));
    }
    
    /** 
     * Helper class for {@link #makeDeclName(JCExpression, TypeDeclaration, DeclNameFlag...)}
     */
    class DeclName {
        private final StringBuilder sb = new StringBuilder();
        private final TypeDeclaration decl;
        private final JCExpression qualifying;
        private JCExpression expr;
        DeclName(TypeDeclaration decl, JCExpression expr) {
            this.decl = decl;
            this.qualifying = expr;
            this.expr = expr;
        }
        void select(String s) {
            expr = makeQualIdent(expr, s);
        }
        DeclName append(String s) {
            sb.append(s);
            return this;
        }
        DeclName append(char ch) {
            sb.append(ch);
            return this;
        }
        void selectAppended() {
            String name = sb.toString();
            select(Decl.isCeylon(decl) ? quoteClassName(name) : name);
            sb.setLength(0);
        }
        JCExpression result() {
            return expr;
        }
        public void clear() {
            expr = qualifying;
            sb.setLength(0);
        }
    }
    
    JCExpression makeDeclName(JCExpression qualifyingExpr, final TypeDeclaration decl, DeclNameFlag... options) {
        // be more resilient to errors
        if(decl == null)
            return make().Erroneous();
         
        // TODO This should probably be generating a JCExpression, not
        // a String (which will inevitable end up being split up to produce a
        // JCExpression by the caller
        EnumSet<DeclNameFlag> flags = EnumSet.noneOf(DeclNameFlag.class);
        flags.addAll(Arrays.asList(options));
        
        Assert.that(!flags.contains(DeclNameFlag.ANNOTATION) || Decl.isAnnotationClass(decl), decl.toString());
        Assert.that(!flags.contains(DeclNameFlag.ANNOTATIONS) 
                || Decl.isAnnotationClass(decl) 
                    && decl instanceof Class 
                    && gen().isSequencedAnnotation((Class)decl), decl.toString());
        
        DeclName helper = new DeclName(decl, qualifyingExpr);
        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        
        if (flags.contains(DeclNameFlag.QUALIFIED)) {
            final List<String> packageName;
            if(!AbstractTransformer.isJavaArray(decl)
                    && !AbstractTransformer.isJavaArrays(decl))
                packageName = ((Package) s).getName();
            else
                packageName = COM_REDHAT_CEYLON_LANGUAGE_PACKAGE;
            if (!packageName.get(0).isEmpty()) {
                helper.select("");
            }
            for (int ii = 0; ii < packageName.size(); ii++) {
                helper.select(quoteIfJavaKeyword(packageName.get(ii)));
            }
        }
        for (int ii = 0; ii < l.size(); ii++) {
            Scope scope = l.get(ii);
            final boolean last = ii == l.size() - 1;
            appendDeclName2(decl, flags, helper, scope, last);
        }
        return helper.result();
    }

    private void appendDeclName2(final TypeDeclaration decl, EnumSet<DeclNameFlag> flags, DeclName helper, Scope scope, final boolean last) {
        if (scope instanceof Class || scope instanceof TypeAlias) {
            TypeDeclaration klass = (TypeDeclaration)scope;
            helper.append(klass.getName());
            if (Decl.isCeylon(klass)) {
                if (flags.contains(DeclNameFlag.COMPANION)
                    && last) {
                    helper.append(IMPL_POSTFIX);
                } else if (flags.contains(DeclNameFlag.ANNOTATION)
                        && last) {
                    helper.append(ANNO_POSTFIX);
                } else if (flags.contains(DeclNameFlag.ANNOTATIONS)
                        && last) {
                    helper.append(ANNOS_POSTFIX);
                }
            }
        } else if (scope instanceof Interface) {
            Interface iface = (Interface)scope;
            helper.append(iface.getName());
            if (Decl.isCeylon(iface)
                && ((decl instanceof Class || decl instanceof TypeAlias) 
                        || flags.contains(DeclNameFlag.COMPANION))) {
                helper.append(IMPL_POSTFIX);
            }
        } else if (Decl.isLocalScope(scope)) {
            if (flags.contains(DeclNameFlag.COMPANION)
                || !(decl instanceof Interface)) {
                helper.clear();
            } else if (flags.contains(DeclNameFlag.QUALIFIED)
                    || (decl instanceof Interface)) {
                Scope nonLocal = scope;
                while (!(nonLocal instanceof Declaration)) {
                    nonLocal = nonLocal.getContainer();
                }
                helper.append(((Declaration)nonLocal).getName()).append('$').append(getLocalId(scope));
                if (decl instanceof Interface) {
                    helper.append('$');
                } else {
                    if (flags.contains(DeclNameFlag.QUALIFIED)) {
                        helper.selectAppended();
                    } else {
                        helper.clear();
                    }
                }
            }
            return;
        }
        if (!last) {
            if (decl instanceof Interface 
                    && Decl.isCeylon((Interface)decl)
                    && !flags.contains(DeclNameFlag.COMPANION)) {
                helper.append('$');
            } else {
                if (flags.contains(DeclNameFlag.QUALIFIED)) {
                    helper.selectAppended();
                } else {
                    helper.clear();
                }
            }
        } else {
            helper.selectAppended();
        }
        return;
    }
    
    public static String toplevelClassName(String pkgName, Tree.Declaration declaration){
        StringBuilder b = new StringBuilder();
        if(!pkgName.isEmpty()){
            b.append(pkgName).append('.');
        }
        appendClassName(b, declaration, declaration, true);
        return b.toString();

    }
    
    private static void appendClassName(StringBuilder b, com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration decl, 
            com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration lastDeclaration, boolean isLast) {
        if (decl instanceof Tree.AnyClass) {
            Tree.AnyClass klass = (Tree.AnyClass)decl;
            b.append(klass.getIdentifier().getText());
        }else if (decl instanceof Tree.TypeAliasDeclaration) {
            Tree.TypeAliasDeclaration klass = (Tree.TypeAliasDeclaration)decl;
            b.append(klass.getIdentifier().getText());
        } else if (decl instanceof Tree.AnyInterface) {
            Tree.AnyInterface iface = (Tree.AnyInterface)decl;
            b.append(iface.getIdentifier().getText());
            // FIXME: IMO this is wrong, and in appendDeclName2 as well, since for Interface>Class>Interface 
            // we also have to have the $impl, no? Ask Tom 
            if (lastDeclaration instanceof Tree.AnyClass) {
                b.append(IMPL_POSTFIX);
            }
        } else if (decl instanceof Tree.TypedDeclaration){
            b.append(decl.getIdentifier().getText());
            b.append('_');
        } else {
            throw new RuntimeException("Don't know how to get a class name for tree of type " + decl.getClass());
        }
        if (!isLast) {
            if (decl instanceof Tree.AnyInterface) {
                b.append('$');
            } else {
                b.append('.');
            }
        }
    }

    /**
     * Generates a Java type name for the given declaration
     * @param gen Something which knows about local declarations
     * @param decl The declaration
     * @param options Option flags
     */
    String declName(final TypeDeclaration decl, DeclNameFlag... options) {
        return makeDeclName(null, decl, options).toString();
    }

    JCExpression makeDeclarationName(TypeDeclaration decl, DeclNameFlag... flags) {
        return makeDeclName(null, decl, flags);
    }
    
    String getCompanionClassName(TypeDeclaration decl) {
        return declName(decl, DeclNameFlag.QUALIFIED, DeclNameFlag.COMPANION);
    }
    
    JCExpression makeCompanionClassName(TypeDeclaration decl) {
        return makeDeclName(null, decl, DeclNameFlag.QUALIFIED, DeclNameFlag.COMPANION);
    }
    
    private static String quoteMethodNameIfProperty(Method method) {
        String name = method.getName();
        if (!method.isShared()) {
            name += "$priv";
        }
        // Toplevel methods keep their original name because their names might be mangled
        if (method instanceof LazyMethod) {
            return ((LazyMethod)method).getRealName();
        }
        // only quote if method is a member, we cannot get a conflict for local
        // since local methods have a $getter suffix
        if(!method.isClassOrInterfaceMember())
            return name;
        // do not quote method names if we have a refined constraint
        Method refinedMethod = (Method) method.getRefinedDeclaration();
        if(refinedMethod instanceof JavaMethod){
            return ((JavaMethod)refinedMethod).getRealName();
        }
        // get/is with at least one more letter, no parameter and non-void type
        if(((name.length() >= 4 && name.startsWith("get"))
             || name.length() >= 3 && name.startsWith("is"))
            && method.getParameterLists().get(0).getParameters().isEmpty()
            && !AbstractTransformer.isAnything(method.getType()))
            return quote(name);
        // set with one parameter and void type
        if((name.length() >= 4 && name.startsWith("set"))
           && method.getParameterLists().get(0).getParameters().size() == 1
           && AbstractTransformer.isAnything(method.getType()))
            return quote(name);
        return name;
    }

    private static String quoteMethodName(Method decl, int namingOptions){
        // always use the refined decl
        Declaration refinedDecl = decl.getRefinedDeclaration();  
        return getMethodName((Method)refinedDecl, namingOptions);
    
    }

    public static String getGetterName(Declaration decl) {
        return getGetterName(decl, false);
    }

    public static String getGetterName(Declaration decl, boolean indirect) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if (decl instanceof JavaBeanValue) {
            return ((JavaBeanValue)decl).getGetterName();
        }
        if (Decl.withinClassOrInterface(decl) && !Decl.isLocalToInitializer(decl) && !indirect) {
            return getErasedGetterName(decl);
        } else if (decl instanceof TypedDeclaration && Decl.isBoxedVariable((TypedDeclaration)decl)) {
            return "ref";
        } else {
            return "$get";
        }
    }

    public static String getSetterName(Declaration decl){
        // use the refined decl except when the current declaration is variable and the refined isn't
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if (Decl.isValue(decl) && Decl.isValue(refinedDecl)) {
            Value v = (Value)decl;
            Value rv = (Value)refinedDecl;
            if (!v.isVariable() || rv.isVariable()) {
                decl = refinedDecl;
            }
        } else {
            decl = refinedDecl;
        }
        if (decl instanceof JavaBeanValue
                // only if the declaration actually has a setter name, if it's a non-variable
                // one it will not. This is also used for late setters...
                && ((JavaBeanValue)decl).getSetterName() != null) {
            return ((JavaBeanValue)decl).getSetterName();
        } else if (Decl.withinClassOrInterface(decl) && !Decl.isLocalToInitializer(decl)) {
            String setterName = getSetterName(decl.getName());
            if (decl.isMember() && !decl.isShared()) {
                setterName += "$priv";
            }
            return setterName;
        } else if (decl instanceof TypedDeclaration && Decl.isBoxedVariable((TypedDeclaration)decl)) {
            return "ref";
        }  else {
            return "$set";
        }
    }

    static String getDefaultedParamMethodName(Declaration decl, Parameter param) {
        if (decl instanceof Method) {
            return ((Method) decl).getName() + "$" + CodegenUtil.getTopmostRefinedDeclaration(param).getName();
        } else if (decl instanceof ClassOrInterface) {
            if (decl.isToplevel() || Decl.isLocal(decl)) {
                return "$init$" + param.getName();
            } else {
                return "$" + decl.getName() + "$init$" + param.getName();
            }
        } else if (decl == null) {
            // for Callables, we get a double dollar to not walk on $call methods if we have a "call" parameter
            return "$$" + param.getName();
        } else {
            // Should never happen (for now at least)
            return null;
        }
    }
    
    String getInstantiatorMethodName(Class model) {
        return model.getName()+"$new";
    }
    
    JCExpression makeInstantiatorMethodName(JCExpression qual, Class model) {
        return makeQualIdent(qual, getInstantiatorMethodName(model));
    }
    
    static String getAliasedParameterName(Parameter parameter) {
        MethodOrValue mov = CodegenUtil.findMethodOrValueForParam(parameter);
        if ((mov instanceof Method && ((Method)mov).isDeferred())
                || (Decl.isValue(mov) && mov.isVariable() && mov.isCaptured())) {
            return parameter.getName()+"$";
        }
        return parameter.getName();
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
    JCFieldAccess makeSelect(String s1, String s2) {
        return makeSelect(makeUnquotedIdent(s1), s2);
    }
    
    JCExpression makeDefaultedParamMethod(JCExpression qualifier, Parameter param) {
        // TODO Can we merge this into makeName(..., NA_DPM) ?
        Declaration decl = (Declaration)param.getContainer();
        String methodName = getDefaultedParamMethodName(decl, param);
        if (Strategy.defaultParameterMethodOnSelf(param)) {
            // method not within interface
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }
            JCExpression className = makeDeclName(qualifier, (TypeDeclaration)container, DeclNameFlag.COMPANION); 
            return makeSelect(className, methodName);
        } else if (Strategy.defaultParameterMethodOnOuter(param)) {
            return makeQuotedQualIdent(qualifier, methodName);            
        } else if (Strategy.defaultParameterMethodStatic(param)) {
            // top level method or class
            Assert.that(qualifier == null);
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
    
    JCExpression makeName(TypedDeclaration decl, int namingOptions) {
        return makeQualifiedName(null, decl, namingOptions);
    }
    
    JCExpression makeQualifiedName(JCExpression qualifyingExpr, TypedDeclaration decl, int namingOptions) {
        // TODO Don't build a list but rather construct a JCExpression directly
        Assert.that(namingOptions != 0);
        Assert.that(qualifyingExpr == null || ((namingOptions & NA_FQ) == 0 
                && (namingOptions & NA_WRAPPER) == 0
                && (namingOptions & NA_WRAPPER_UNQUOTED) == 0));
        
        JCExpression expr = qualifyingExpr;
        expr = addNamesForWrapperClass(expr, decl, namingOptions);
        if ((namingOptions & (NA_MEMBER | NA_IDENT)) != 0) {
            expr = addMemberName(expr, decl, namingOptions);
        }
        
        return expr;
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
        Assert.that(expr != null || name != null);
        if (expr == null) {
            return makeUnquotedIdent(name);
        } else if (name == null) {
            return expr;
        } else {
            return makeSelect(expr, name);
        }
    }
    
    private JCExpression addMemberName(JCExpression expr, TypedDeclaration decl, int namingOptions) {
        if ((namingOptions & NA_IDENT) != 0) {
            Assert.not((namingOptions & NA_GETTER | NA_SETTER) == 0);
            expr = makeQualIdent(expr, decl.getName());
        } else if ((namingOptions & NA_SETTER) != 0) {
            Assert.not(decl instanceof Method, "A method has no setter");
            expr = makeQualIdent(expr, getSetterName(decl));
        } else if ((namingOptions & NA_GETTER) != 0) {
            Assert.not(decl instanceof Method, "A method has no getter");
            expr = makeQualIdent(expr, getGetterName(decl));
        } else if (decl instanceof Value) {
            expr = makeQualIdent(expr, getGetterName(decl));
        } else if (decl instanceof Setter) {
            expr = makeQualIdent(expr, getSetterName(decl.getName()));
        } else if (decl instanceof Method) {
            expr = makeQualIdent(expr, getMethodName((Method)decl, namingOptions));
        } else if (decl instanceof Parameter) {
            if ((namingOptions & NA_ALIASED) != 0) {
                expr = makeQualIdent(expr, getAliasedParameterName((Parameter)decl));
            } else {
                expr = makeQualIdent(expr, decl.getName());
            }
        }
        return expr;
    }
    
    private JCExpression addNamesForWrapperClass(JCExpression expr, TypedDeclaration decl, int namingOptions) {
        if ((namingOptions & NA_FQ) != 0) {
            Assert.that(((namingOptions & NA_WRAPPER) != 0)
                    || ((namingOptions & NA_WRAPPER_UNQUOTED) != 0), 
                    "If you pass FQ you must pass WRAPPER or WRAPPER_UNQUOTED too, or there's no class name to qualify!");
            List<String> outerNames = null;
            Scope s = decl.getContainer();
            while (s != null) {
                if (s instanceof Package) {
                    final List<String> packageName;
                    if(!AbstractTransformer.isJavaArrays(decl.getTypeDeclaration()))
                        packageName = ((Package) s).getName();
                    else
                        packageName = COM_REDHAT_CEYLON_LANGUAGE_PACKAGE;

                    if (!packageName.get(0).isEmpty()) {
                        expr = maker.Ident(names.empty);
                    }
                    for (int ii = 0; ii < packageName.size(); ii++) {
                        expr = makeQualIdent(expr, quoteIfJavaKeyword(packageName.get(ii)));
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
                    expr = makeQualIdent(expr, outerName);
                }
            }
        }
        if ((namingOptions & NA_WRAPPER) != 0) {
            expr = makeQualIdent(expr, getQuotedClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        } else if ((namingOptions & NA_WRAPPER_UNQUOTED) != 0) {
            expr = makeQualIdent(expr, getRealName(decl, namingOptions & (NA_GETTER | NA_SETTER | NA_WRAPPER_UNQUOTED)));
        } else if ((namingOptions & NA_Q_LOCAL_INSTANCE) != 0) {
            expr = makeQualIdent(expr, getAttrClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        }
        if((namingOptions & NA_WRAPPER_WITH_THIS) != 0){
            expr = makeQualIdent(expr, "this");
        }
        return expr;
    }

    static String quoteClassName(String name) {
        return Util.isInitialLowerCase(name) ? name + "_" : name;
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
        } else if (decl instanceof LazyMethod) {
            name = ((LazyMethod)decl).getRealName();
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
    
    /** Include the member part of the typed declaration (e.g. the method name) */
    static final int NA_MEMBER = 1<<0;
    /** Include the wrapper class of the typed declaration */
    static final int NA_WRAPPER = 1<<1;
    /** Include the wrapper class of the typed declaration, but don't quote it */
    static final int NA_WRAPPER_UNQUOTED = 1<<2;
    static final int NA_Q_LOCAL_INSTANCE = 1<<6;
    /** Generate a fully qualified name. Requires NO_WRAPPER */
    static final int NA_FQ = 1<<3;
    /** Generate the name of the getter (otherwise the type of the declaration 
     * will be used to determine the name to be generated) */
    static final int NA_GETTER = 1<<4;
    /** Generate the name of the setter (otherwise the type of the declaration 
     * will be used to determine the name to be generated) */
    static final int NA_SETTER = 1<<5;
    static final int NA_IDENT = 1<<7;
    static final int NA_ALIASED = 1<<8;
    /** A shared parameter on an annotation class produces an annotation type 
     * method foo, not getFoo() */
    static final int NA_ANNOTATION_MEMBER = 1<<9;
    /** Add a ".this" selector to the wrapper class */
    static final int NA_WRAPPER_WITH_THIS = 1<<10;

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
        } else if (Decl.isMethod(decl)
                || decl instanceof FunctionalParameter) {
            if (decl.isClassMember()) {
                // don't try to be smart with interop calls 
                if(decl instanceof JavaMethod)
                    return ((JavaMethod)decl).getRealName();
                return getMethodName(decl, namingOptions);
            }
            return quoteMethodName((Method)decl, namingOptions);
        } else if (decl instanceof Parameter) {
            return getMethodName(decl, namingOptions);
        }
        Assert.fail();
        return null;
    }
    
    /**
     * Gets the class name for the attribute wrapper class. The class name 
     * will be quoted unless namingOptions includes NA_WRAPPER_UNQUOTED
     */
    static String getAttrClassName(TypedDeclaration decl, int namingOptions) {
        Assert.that((namingOptions & ~(NA_SETTER | NA_GETTER | NA_WRAPPER_UNQUOTED)) == 0, 
                "Unsupported namingOption");
        String name = decl.getName();
        if (Decl.isLocal(decl) || Decl.isLocalToInitializer(decl)) {
            if ((Decl.isGetter(decl) && (namingOptions & NA_SETTER) == 0)
                    || (namingOptions & NA_GETTER) != 0){
                name = name + "$getter";
            } else if ((decl instanceof Setter && (namingOptions & NA_GETTER) == 0)
                    || (namingOptions & NA_SETTER) != 0) {
                name = name + "$setter";
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
        return getCompanionFieldName(def, "$this");
    }

    final String getCompanionFieldName(Interface def, String name) {
        // resolve aliases
        if(def.isAlias())
            def = (Interface) def.getExtendedTypeDeclaration();
        return "$" + Decl.className(def).replace('.', '$') + name;
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
        return getCompanionClassName(def).replace('.', '$');
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
        Assert.that(Decl.isValue(decl));
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
        String result = "$ceylontmp" + nextUniqueId();
        return result;
    }

    String newTemp(String prefix) {
        String result = "$ceylontmp" + prefix + nextUniqueId();
        return result;
    }

    private String newAlias(String name) {
        String result = "$" + name + "$" + nextUniqueId();
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
            result = prime * result + ((name.toString() == null) ? 0 : name.toString().hashCode());
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
            if (name.toString() == null) {
                if (other.name.toString() != null)
                    return false;
            } else if (!name.toString().equals(other.name.toString()))
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
            return makeSelect("this", getName());
        }
        
        /**
         * Returns a new SyntheticName which appends the given suffix onto 
         * this SyntheticName's name.
         */
        SyntheticName suffixedBy(String suffix) {
            return new SyntheticName(names.fromString(getName() + suffix));
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
            result = prime * result + ((getName().toString() == null) ? 0 : getName().toString().hashCode());
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
            if (getName().toString() == null) {
                if (other.getName().toString() != null)
                    return false;
            } else if (!getName().toString().equals(other.getName().toString()))
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
            return makeSelect("this", getName());
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
        private class SubstitutionKey {
            private final String name;
            private final Scope scope;
            SubstitutionKey(TypedDeclaration decl) {
                super();
                this.name = decl.getName();
                TypedDeclaration orig = decl.getOriginalDeclaration();
                Scope stop = (orig != null ? orig : decl).getContainer();
                Scope scope = decl.getContainer();
                while (scope != stop) {
                    if (!(scope instanceof ControlBlock)) {
                        break;
                    }
                    scope = scope.getContainer();
                }
                this.scope = scope;
            }
            public String toString() {
                return name + " in " + scope;
            }
            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + getOuterType().hashCode();
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
                if (!getOuterType().equals(other.getOuterType()))
                    return false;
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
            private VarMapper getOuterType() {
                return VarMapper.this;
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
            return key.name;
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
        
        Substitution(TypedDeclaration decl, String substituted) {
            this.original = decl;
            this.substituted = substituted;
            this.previous = getVarMapper().put(decl, substituted);
        }
        
        public String toString() {
            if (closed) {
                return "Spent substitution";
            }
            return "Substituting " + substituted + " for " + original + " (masking " + previous + ")";
        }
        
        public void close() {
            if (closed) {
                throw new IllegalStateException();
            }
            removeVariableSubst(this);
            closed = true;
        }
    }
    /** 
     * Generates an alias for the given declaration, and adds a 
     * substitution for the given declaration using this alias. 
     */
    Substitution substituteAlias(TypedDeclaration decl) {
        return addVariableSubst(decl, alias(substitute(decl)).getName());
    }
    
    SyntheticName synthetic(String name) {
        return new SyntheticName(names.fromString(name));
    }
    
    private java.util.Map<Scope, Integer> locals;
    
    private void resetLocals() {
        locals = new java.util.HashMap<Scope, Integer>();
    }
    
    private void local(Scope decl) {
        if (!locals.containsKey(decl)) {
            locals.put(decl, locals.size());
        }
    }
    
    void noteDecl(Declaration decl) {
        if (decl.isToplevel()) {
            resetLocals();
            // Clear all the substitutions before each top level declaration
            getVarMapper().clear();
        } else if (Decl.isLocal(decl)){
            local(decl.getContainer());
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
    public String getMethodSpecifierAttributeName(Method m) {
        return m.getName()+"$specifier";
    }
    
    public static String getCallableMethodName() {
        return "$call";
    }

    public static String getCallableTypedMethodName() {
        return "$call$typed";
    }
    
    public static String getCallableTempVarName(Parameter param){
        return "$$" + param.getName();
    }
    
    public static String getImplClassName(String name){
        return name + IMPL_POSTFIX;
    }
    
    public String getTypeArgumentDescriptorName(String name) {
        return "$reified" + name;
    }
    
    public String getGetTypeMethodName() {
        return "$getType";
    }

    public String getRefineTypeParametersMethodName() {
        return "$refine";
    }

    public String getTypeDescriptorAliasName() {
        return "$TypeDescriptor";
    }
  
}
