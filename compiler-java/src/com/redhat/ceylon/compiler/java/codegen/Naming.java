package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.sun.tools.javac.parser.Token;

public class Naming {


    static enum NameFlag {
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
    static String declName(LocalId gen, final Declaration decl, NameFlag... options) {
        EnumSet<NameFlag> flags = EnumSet.noneOf(NameFlag.class);
        flags.addAll(Arrays.asList(options));
        StringBuilder sb = new StringBuilder();

        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = (Scope)decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        
        if (flags.contains(NameFlag.QUALIFIED)) {
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
        if (!flags.contains(NameFlag.QUALIFIED)) {
            // this is a lot saner than trying to modify appendDeclName :(
            int lastDot = sb.lastIndexOf(".");
            if(lastDot != -1)
                sb.delete(0, lastDot+1);
        }
        return sb.toString();
    }

    static void appendDeclName(LocalId gen, final Declaration decl, EnumSet<NameFlag> flags, StringBuilder sb, Scope scope, final boolean last) {
        if (scope instanceof Class) {
            Class klass = (Class)scope;
            sb.append(klass.getName());
            if (flags.contains(NameFlag.COMPANION)
                    && last) {
                sb.append("$impl");
            }
        } else if (scope instanceof Interface) {
            Interface iface = (Interface)scope;
            sb.append(iface.getName());
            if (Decl.isCeylon(iface)
                &&
                 (decl instanceof Class) 
                 || flags.contains(NameFlag.COMPANION)) {
                sb.append("$impl");
            }
        } else if (Decl.isLocalScope(scope)) {
            if (flags.contains(NameFlag.COMPANION)
                || !(decl instanceof Interface)) {
                sb.setLength(0);
            } else
            if (flags.contains(NameFlag.QUALIFIED)
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
                sb.append(flags.contains(NameFlag.COMPANION) ? '.' : '$');
            } else {
                sb.append('.');
            }
        }
    }
    
    static String quoteMethodNameIfProperty(Method method, AbstractTransformer gen) {
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
            && !gen.isVoid(method.getType()))
            return Util.quote(name);
        // set with one parameter and void type
        if((name.length() >= 4 && name.startsWith("set"))
           && method.getParameterLists().get(0).getParameters().size() == 1
           && gen.isVoid(method.getType()))
            return Util.quote(name);
        return name;
    }

    static String quoteMethodName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        String name = decl.getName();
        if (Decl.withinClassOrInterface(decl)) {
            return Util.getErasedMethodName(name);
        } else {
            return Util.getMethodName(name);
        }
    }

    static String getGetterName(Declaration decl) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if (decl instanceof JavaBeanValue) {
            return ((JavaBeanValue)decl).getGetterName();
        }
        if (Decl.withinClassOrInterface(decl)) {
            return Util.getErasedGetterName(decl.getName());
        } else {
            return Util.getGetterName(decl.getName());
        }
    }

    static String getSetterName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getSetterName();
        }
        return Util.getSetterName(decl.getName());
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
    
}
