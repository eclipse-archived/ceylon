package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.parser.Token;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

public class Naming {


    static enum DeclNameFlag {
        /** 
         * A qualified name. 
         * <li>For a top level this includes the package name.
         * <li>For an inner this includes the package name and the qualifying type names
         * <li>For a (possibly indirect) local this includes the qualifying type names 
         */
        QUALIFIED,
        /** The name of the companion type of this thing */
        COMPANION
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
    
    public static String getErasedMethodName(String name) {
        // ERASURE
        if ("hash".equals(name)) {
            return "hashCode";
        } else if ("string".equals(name)) {
            return "toString";
        } else if ("equals".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            return "equals";
        } else if ("clone".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            // FIXME we should only do this when implementing Cloneable!
            return "clone";
        } else {
            return getMethodName(name);
        }
    }

    public static String getMethodName(String name) {
        // ERASURE
        if ("hashCode".equals(name)) {
            return "$hashCode";
        } else if ("toString".equals(name)) {
            return "$toString";
        } else if ("equals".equals(name)) {
            return "$equals";
        } else if ("wait".equals(name)) {
            return "$wait";
        } else if ("notify".equals(name)) {
            return "$notify";
        } else if ("notifyAll".equals(name)) {
            return "$notifyAll";
        } else if ("getClass".equals(name)) {
            return "$getClass";
        } else if ("finalize".equals(name)) {
            return "$finalize";
        } else if ("clone".equals(name)) {
            return "$clone";
        } else {
            return quoteIfJavaKeyword(name);
        }
    }

    public static String getErasedGetterName(String property) {
        // ERASURE
        if ("hash".equals(property)) {
            return "hashCode";
        } else if ("string".equals(property)) {
            return "toString";
        }
        
        return getGetterName(property);
    }

    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String getGetterName(String property) {
        return "get"+capitalize(strip(property));
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
    }
    
    /**
     * Generates a Java type name for the given declaration
     * @param gen Something which knows about local declarations
     * @param decl The declaration
     * @param options Option flags
     */
    static String declName(LocalId gen, final TypeDeclaration decl, DeclNameFlag... options) {
        // TODO This should probably be generating a JCExpression, not
        // a String (which will inevitable end up being split up to produce a
        // JCExpression by the caller
        EnumSet<DeclNameFlag> flags = EnumSet.noneOf(DeclNameFlag.class);
        flags.addAll(Arrays.asList(options));
        StringBuilder sb = new StringBuilder();

        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = (Scope)decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        
        if (flags.contains(DeclNameFlag.QUALIFIED)) {
            Package pkg = (Package)s;
            final String pname = pkg.getQualifiedNameString();
            sb.append('.').append(pname);
            if (!pname.isEmpty()) {
                sb.append('.');
            }
        }    
        for (int ii = 0; ii < l.size(); ii++) {
            Scope scope = l.get(ii);
            final boolean last = ii == l.size() - 1;
            appendDeclName(gen, decl, flags, sb, scope, last);
        }
        if (!flags.contains(DeclNameFlag.QUALIFIED)) {
            // this is a lot saner than trying to modify appendDeclName :(
            int lastDot = sb.lastIndexOf(".");
            if(lastDot != -1)
                sb.delete(0, lastDot+1);
        }
        return sb.toString();
    }

    static void appendDeclName(LocalId gen, final TypeDeclaration decl, EnumSet<DeclNameFlag> flags, StringBuilder sb, Scope scope, final boolean last) {
        if (scope instanceof Class) {
            Class klass = (Class)scope;
            sb.append(klass.getName());
            if (flags.contains(DeclNameFlag.COMPANION)
                    && last) {
                sb.append("$impl");
            }
        } else if (scope instanceof Interface) {
            Interface iface = (Interface)scope;
            sb.append(iface.getName());
            if (Decl.isCeylon(iface)
                &&
                 (decl instanceof Class) 
                 || flags.contains(DeclNameFlag.COMPANION)) {
                sb.append("$impl");
            }
        } else if (Decl.isLocalScope(scope)) {
            if (flags.contains(DeclNameFlag.COMPANION)
                || !(decl instanceof Interface)) {
                sb.setLength(0);
            } else
            if (flags.contains(DeclNameFlag.QUALIFIED)
                    || (decl instanceof Interface)) {
                Scope nonLocal = scope;
                while (!(nonLocal instanceof Declaration)) {
                    nonLocal = nonLocal.getContainer();
                }
                if (decl instanceof Interface) {
                    sb.append(((Declaration)nonLocal).getName()).append('$').append(gen.getLocalId(scope)).append('$');
                } else {
                    sb.append(((Declaration)nonLocal).getName()).append("$").append(gen.getLocalId(scope)).append('.');
                }
            }
            return;
        }
        if (!last) {
            if (decl instanceof Interface && Decl.isCeylon((Interface)decl)) {
                sb.append(flags.contains(DeclNameFlag.COMPANION) ? '.' : '$');
            } else {
                sb.append('.');
            }
        }
    }

    String getCompanionClassName(TypeDeclaration decl) {
        return declName(gen(), decl, DeclNameFlag.QUALIFIED, DeclNameFlag.COMPANION);
    }
    
    String quoteMethodNameIfProperty(Method method) {
        String name = method.getName();
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
            && !gen().isVoid(method.getType()))
            return quote(name);
        // set with one parameter and void type
        if((name.length() >= 4 && name.startsWith("set"))
           && method.getParameterLists().get(0).getParameters().size() == 1
           && gen().isVoid(method.getType()))
            return quote(name);
        return name;
    }

    private String quoteMethodName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        String name = decl.getName();
        if (Decl.withinClassOrInterface(decl)) {
            return getErasedMethodName(name);
        } else {
            return getMethodName(name);
        }
    }

    static String getGetterName(Declaration decl) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if (decl instanceof JavaBeanValue) {
            return ((JavaBeanValue)decl).getGetterName();
        }
        if (Decl.withinClassOrInterface(decl)) {
            return getErasedGetterName(decl.getName());
        } else {
            return getGetterName(decl.getName());
        }
    }

    static String getSetterName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getSetterName();
        }
        return getSetterName(decl.getName());
    }

    static String getDefaultedParamMethodName(Declaration decl, Parameter param) {
        if (decl instanceof Method) {
            return ((Method) decl).getName() + "$" + CodegenUtil.getTopmostRefinedDeclaration(param).getName();
        } else if (decl instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            return "$init$" + param.getName();
        } else {
            // Should never happen (for now at least)
            return null;
        }
    }

    
    static String getAliasedParameterName(Parameter parameter) {
        MethodOrValue mov = CodegenUtil.findMethodOrValueForParam(parameter);
        if (mov instanceof Method
                || mov instanceof Value && mov.isVariable() && mov.isCaptured()) {
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
        return make().Ident(names().fromString(ident));
    }

    /** 
     * Makes an <strong>quoted</strong> simple identifier
     * @param ident The identifier
     * @return The ident
     */
    JCIdent makeQuotedIdent(String ident) {
        return make().Ident(names().fromString(Naming.quoteIfJavaKeyword(ident)));
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
     * @param components The components of the name.
     * @see #makeQuotedQualIdentFromString(String)
     */
    JCExpression makeQualIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = makeUnquotedIdent(component);
            else
                type = makeSelect(type, component);
        }
        return type;
    }
    
    JCExpression makeQuotedQualIdent(Iterable<String> components) {
        JCExpression type = null;
        for (String component : components) {
            if (type == null)
                type = makeQuotedIdent(component);
            else
                type = makeSelect(type, Naming.quoteIfJavaKeyword(component));
        }
        return type;
    }

    /** 
     * Makes an <strong>unquoted</strong> qualified (compound) identifier 
     * from the given qualified name components
     * @param expr A starting expression (may be null)
     * @param names The components of the name (may be null)
     * @see #makeQuotedQualIdentFromString(String)
     */
    JCExpression makeQualIdent(JCExpression expr, String... names) {
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

    /**
     * Makes a sequence of <strong>unquoted</strong> field accesses
     * @param s1 The base expression
     * @param s2 The first field to access
     * @param rest The remaining fields to access
     * @return The field access
     */
    JCFieldAccess makeSelect(String s1, String s2, String... rest) {
        return makeSelect(makeSelect(s1, s2), rest);
    }

    JCFieldAccess makeSelect(JCFieldAccess s1, String[] rest) {
        JCFieldAccess acc = s1;
        for (String s : rest) {
            acc = makeSelect(acc, s);
        }
        return acc;
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
            String className = declName(gen(), (TypeDeclaration)container, DeclNameFlag.COMPANION); 
            return  makeQuotedQualIdent(qualifier, className, methodName);
        } else if (Strategy.defaultParameterMethodStatic(param)) {
            // top level method or class
            Assert.that(qualifier == null);
            Declaration container = param.getDeclaration().getRefinedDeclaration();
            if (!container.isToplevel()) {
                container = (Declaration)container.getContainer();
            }            
            return makeQuotedQualIdent(makeQuotedFQIdent(container.getQualifiedNameString()), methodName);
        } else {
            // inner or local class or method, or method in an interface
            return makeQuotedQualIdent(qualifier, methodName);
        }
    }
    
    JCExpression makeQualifiedName(JCExpression expr, TypedDeclaration decl, int namingOptions) {
        // TODO Don't build a list but rather construct a JCExpression directly
        LinkedList<String> parts = new LinkedList<String>();
        Assert.that(namingOptions != 0);
        Assert.that(expr == null || ((namingOptions & NA_FQ) == 0 
                && (namingOptions & NA_WRAPPER) == 0
                && (namingOptions & NA_WRAPPER_UNQUOTED) == 0)); 
        if ((namingOptions & (NA_MEMBER | NA_IDENT)) != 0) {
            pushMemberName(decl, namingOptions, parts);
        }
        addNamesForWrapperClass(decl, parts, namingOptions);
        return mkSelect(expr, parts);
    }
    
    JCExpression makeQualifiedThis(JCExpression qualifier) {
        return qualifier != null ? makeSelect(qualifier, "this") : makeUnquotedIdent("this");
    }
    
    JCExpression makeThis() {
        return makeQualifiedThis(null);
    }
    
    JCExpression makeQuotedThis() {
        return makeUnquotedIdent("$this");
    }
    
    JCTree makeSuper() {
        return makeUnquotedIdent("super");
    }
    
    JCExpression makeName(TypedDeclaration decl, int namingOptions) {
        return makeQualifiedName(null, decl, namingOptions);
    }
    private static void pushMemberName(TypedDeclaration decl, int namingOptions,
            LinkedList<String> parts) {
        if ((namingOptions & NA_IDENT) != 0) {
            Assert.not((namingOptions & NA_GETTER | NA_SETTER) == 0);
            parts.push(decl.getName());
        } else if ((namingOptions & NA_SETTER) != 0) {
            Assert.not(decl instanceof Method, "A method has no setter");
            parts.push(getSetterName(decl.getName()));
        } else if ((namingOptions & NA_GETTER) != 0) {
            Assert.not(decl instanceof Method, "A method has no getter");
            parts.push(getGetterName(decl));
        } else if (decl instanceof Getter
                || decl instanceof Value) {
            if (decl.getType().isCallable()) {
                parts.push(decl.getName());
            } else {
                parts.push(getGetterName(decl));
            }
        } else if (decl instanceof Setter) {
            parts.push(getSetterName(decl.getName()));
        } else if (decl instanceof Method) {
            parts.push(getMethodName(decl.getName()));
        } else if (decl instanceof Parameter) {
            if ((namingOptions & NA_ALIASED) != 0) {
                parts.push(getAliasedParameterName((Parameter)decl));
            } else {
                parts.push(decl.getName());
            }
        }
    }

    private static void addNamesForWrapperClass(TypedDeclaration decl,
            LinkedList<String> parts, int namingOptions) {
        if ((namingOptions & NA_WRAPPER) != 0) {
            parts.push(getQuotedClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        } else if ((namingOptions & NA_WRAPPER_UNQUOTED) != 0) {
            parts.push(getRealName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        } else if ((namingOptions & NA_Q_LOCAL_INSTANCE) != 0) {
            parts.push(getAttrClassName(decl, namingOptions & (NA_GETTER | NA_SETTER)));
        }
        if ((namingOptions & NA_FQ) != 0) {
            Assert.that(((namingOptions & NA_WRAPPER) != 0)
                    || ((namingOptions & NA_WRAPPER_UNQUOTED) != 0), 
                    "If you pass FQ you must pass WRAPPER or WRAPPER_UNQUOTED too, or there's no class name to qualify!");
            Scope s = decl.getContainer();
            while (s != null) {
                if (s instanceof Package) {
                    final List<String> packageName = ((Package) s).getName();
                    for (int ii = packageName.size() - 1; ii >= 0; ii--) {
                        parts.push(quoteIfJavaKeyword(packageName.get(ii)));
                    }
                    if (!packageName.get(0).isEmpty()) {
                        parts.push("");// so generated name begins with a .
                    }
                    break;
                } else if (s instanceof ClassOrInterface) {
                    parts.push(getQuotedClassName((ClassOrInterface) s, 0));
                } else if (s instanceof TypedDeclaration) {
                    parts.push(quoteIfJavaKeyword(((TypedDeclaration) s).getName()));
                }
                s = s.getContainer();
            }
        } 
    }

    static String quoteClassName(String name) {
        return quoteIfJavaKeyword(name);
    }
    
    /** 
     * Gets the class name of the given declaration, with the given options
     * @param decl The declaration
     * @param namingOptions Only NA_SETTER and NA_GETTER are supported.
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
        } else if (decl instanceof Getter) {
            name = getAttrClassName((Getter)decl, namingOptions);
        } else if (decl instanceof Setter) {
            name = getAttrClassName((Setter)decl, namingOptions);
        } else {
            name = decl.getName();
        }
        return name;
    }
    
    private static String getQuotedClassName(Declaration decl, int namingOptions) {
        String name = getRealName(decl, namingOptions);
        return quoteClassName(name);
    }
    
    /**
     * Returns a fully qualified JCExpression for referring to the given name
     */
    private JCExpression mkSelect(JCExpression result, List<String> rest) {
        for (String part : rest) {
            if (result == null) {
                result = makeUnquotedIdent(part);
            } else {
                result = makeSelect(result, part);
            }
        }
        return result;
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

    /**
     * Returns the name of the Java method/field for the given declaration 
     * @param decl The declaration
     * @return The name of the corresponding Java declaration.
     */
    String selector(TypedDeclaration decl) {
        return selector(decl, 0);
    }
    String selector(TypedDeclaration decl, int namingOptions) {
        if (decl instanceof Getter) {
            if ((namingOptions & NA_SETTER) != 0) {
                return getSetterName(decl);
            } else {
                return getGetterName(decl);
            }
        } else if (decl instanceof Value) {
            if ((namingOptions & NA_SETTER) != 0) {
                return getSetterName(decl);
            } else {
                return getGetterName(decl);
            }
        } else if (decl instanceof Method) {
            if (decl.isClassMember()) {
                return getErasedMethodName(quoteMethodNameIfProperty((Method) decl));
            }
            return quoteMethodName(decl);
        }
        Assert.fail();
        return null;
    }
    
    static String getAttrClassName(TypedDeclaration decl, int namingOptions) {
        Assert.that((namingOptions & ~(NA_SETTER | NA_GETTER)) == 0);
        String name = decl.getName();
        if (Decl.isLocal(decl)) {
            if ((decl instanceof Getter && (namingOptions & NA_SETTER) == 0)
            		|| (namingOptions & NA_GETTER) != 0){
                name = name + "$getter";
            } else if ((decl instanceof Setter && (namingOptions & NA_GETTER) == 0)
                    || (namingOptions & NA_SETTER) != 0) {
                name = name + "$setter";
            }
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
        return "$" + def.getQualifiedNameString().replace('.', '$') + "$this";
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
    public JCExpression makeLanguageValue(String string) {
        if ("empty".equals(string)) {
            string = "$empty"; // a little hack for now
        }
        return makeFQIdent("ceylon", "language", string, getGetterName(string));
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
    
    private String newTemp() {
        String result = "$ceylontmp" + nextUniqueId();
        return result;
    }

    private String newTemp(String prefix) {
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
    
    /**
     * Encapsulates a temporary name or alias 
     */
    class SyntheticName {
        
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
        String getName() {
            return name.toString();
        }
        
        /**
         * This name as a Name
         */
        Name asName() {
            return name;
        }
        
        /**
         * A new JCIdent for this name.
         */
        JCIdent makeIdent() {
            return make().Ident(name);
        }
        
        /**
         * Returns a new SyntheticName which appends the given suffix onto 
         * this SyntheticName's name.
         */
        SyntheticName suffixedBy(String suffix) {
            return new SyntheticName(names.fromString(name.toString() + suffix));
        }
    }
    
    /*
     * Variable name substitution
     */
    
    @SuppressWarnings("serial")
    protected static class VarMapper extends HashMap<String, String> {
    }
    
    private Map<String, String> getVarMapper() {
        VarMapper map = context.get(VarMapper.class);
        if (map == null) {
            map = new VarMapper();
            context.put(VarMapper.class, map);
        }
        return map;
    }
    
    String addVariableSubst(String origVarName, String substVarName) {
        return getVarMapper().put(origVarName, substVarName);
    }

    void removeVariableSubst(String origVarName, String prevSubst) {
        if (prevSubst != null) {
            getVarMapper().put(origVarName, prevSubst);
        } else {
            getVarMapper().remove(origVarName);
        }
    }
    
    /**
     * Checks a global map of variable name substitutions and returns
     * either the original name if none was found or the substitute.
     */
    String substitute(String varName) {
        if (getVarMapper().containsKey(varName)) {
            return getVarMapper().get(varName);            
        } else {
            return varName;
        }
    }
    
}
