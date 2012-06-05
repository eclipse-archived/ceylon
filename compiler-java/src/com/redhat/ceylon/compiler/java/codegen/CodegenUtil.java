package com.redhat.ceylon.compiler.java.codegen;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilerAnnotation;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;

/**
 * Utility functions that are specific to the codegen package
 * @see Util
 */
class CodegenUtil {

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
    
    private CodegenUtil(){}
    
    /**
     * Generates a Java type name for the given declaration
     * @param gen Something which knows about local declarations
     * @param decl The declaration
     * @param options Option flags
     */
    static String declName(LocalId gen, final Declaration decl, NameFlag... options) {
        EnumSet<NameFlag> flags = EnumSet.noneOf(NameFlag.class);
        flags.addAll(Arrays.asList(options));
        java.util.List<Scope> l = new java.util.ArrayList<Scope>();
        Scope s = (Scope)decl;
        do {
            l.add(s);
            s = s.getContainer();
        } while (!(s instanceof Package));
        Collections.reverse(l);
        StringBuilder sb = new StringBuilder();
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
                if ((decl instanceof Class) || 
                        flags.contains(NameFlag.COMPANION)) {
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
                continue;
            }
            if (!last) {
                if (decl instanceof Interface) {
                    sb.append(flags.contains(NameFlag.COMPANION) ? '.' : '$');
                } else {
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }

    static boolean isErasedAttribute(String name){
        // ERASURE
        return "hash".equals(name) || "string".equals(name);
    }

    static boolean isSmall(TypedDeclaration declaration) {
        return "hash".equals(declaration.getName());
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

    static String quoteMethodName(String name){
        // ERASURE
        if ("hash".equals(name)) {
            return "hashCode";
        } else if ("string".equals(name)) {
            return "toString";
        } else if ("hashCode".equals(name)) {
            return "$hashCode";
        } else if ("toString".equals(name)) {
            return "$toString";
        } else {
            return Util.quoteIfJavaKeyword(name);
        }
    }

    static String getGetterName(Declaration decl) {
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getGetterName();
        }
        return Util.getGetterName(decl.getName());
    }

    static String getSetterName(Declaration decl){
        // always use the refined decl
        decl = decl.getRefinedDeclaration();
        if(decl instanceof JavaBeanValue){
            return ((JavaBeanValue)decl).getSetterName();
        }
        return Util.getSetterName(decl.getName());
    }

    /**
     * Deprecated in favour of 
     * {@link AbstractTransformer#getCompanionClassName(Declaration)} which 
     * understands interface renaming and local types.
     * @param name
     * @return
     */
    @Deprecated
    static String getCompanionClassName(String name){
        return name + "$impl";
    }

    static boolean isUnBoxed(Term node){
        return node.getUnboxed();
    }

    static boolean isUnBoxed(TypedDeclaration decl){
        // null is considered boxed
        return decl.getUnboxed() == Boolean.TRUE;
    }

    static void markUnBoxed(Term node) {
        node.setUnboxed(true);
    }

    static BoxingStrategy getBoxingStrategy(Term node) {
        return isUnBoxed(node) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    static BoxingStrategy getBoxingStrategy(TypedDeclaration decl) {
        return isUnBoxed(decl) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    static boolean hasCompilerAnnotation(Tree.Declaration decl, String name){
        for(CompilerAnnotation annotation : decl.getCompilerAnnotations()){
            if(annotation.getIdentifier().getText().equals(name))
                return true;
        }
        return false;
    }

    static String getDefaultedParamMethodName(Declaration decl, Parameter param) {
        if (decl instanceof Method) {
            return decl.getName() + "$" + param.getName();
        } else if (decl instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) {
            return "$init$" + param.getName();
        } else {
            // Should never happen (for now at least)
            return null;
        }
    }

    static boolean isDirectAccessVariable(Term term) {
        if(!(term instanceof BaseMemberExpression))
            return false;
        Declaration decl = ((BaseMemberExpression)term).getDeclaration();
        if(decl == null) // typechecker error
            return false;
        // make sure we don't try to optimise things which can't be optimised
        return decl instanceof Value
                && !decl.isToplevel()
                && !decl.isClassOrInterfaceMember()
                && !decl.isCaptured()
                && !decl.isShared();
    }

    static Declaration getTopmostRefinedDeclaration(Declaration decl){
        if(decl instanceof Parameter && decl.getContainer() instanceof Functional){
            // Parameters in a refined class, interface or method are not considered refinements themselves
            // so we have to look up the corresponding parameter in the container's refined declaration
            Functional func = (Functional)decl.getContainer();
            Parameter param = (Parameter)decl;
            Functional refinedFunc = (Functional) getTopmostRefinedDeclaration((Declaration)decl.getContainer());
            // shortcut if the functional doesn't override anything
            if(refinedFunc == decl.getContainer())
                return decl;
            if (func.getParameterLists().size() != refinedFunc.getParameterLists().size()) {
                throw new RuntimeException("Different numbers of parameter lists");
            }
            for (int ii = 0; ii < func.getParameterLists().size(); ii++) {
                // find the index of the parameter
                int index = func.getParameterLists().get(ii).getParameters().indexOf(param);
                if (index == -1) {
                    continue;
                }
                return refinedFunc.getParameterLists().get(ii).getParameters().get(index);
            }
        }
        Declaration refinedDecl = decl.getRefinedDeclaration();
        if(refinedDecl != null && refinedDecl != decl)
            return getTopmostRefinedDeclaration(refinedDecl);
        return decl;
    }
    
}
