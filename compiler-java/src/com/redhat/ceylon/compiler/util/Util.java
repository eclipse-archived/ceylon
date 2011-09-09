package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.sun.tools.javac.parser.Token;

public class Util {
    public static boolean isErasedAttribute(String name){
        // ERASURE
        return "hash".equals(name) || "string".equals(name);
    }
    
    public static String quoteMethodName(String name){
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
            return quoteIfJavaKeyword(name);
        }
    }
    
    public static String quoteIfJavaKeyword(String name){
        if(isJavaKeyword(name))
            return "$"+name;
        return name;
    }
    
    private static boolean isJavaKeyword(String name) {
        try{
            Token token = Token.valueOf(name.toUpperCase());
            return token != null && token.name != null && token.name.equals(name);
        }catch(IllegalArgumentException x){
            return false;
        }
    }

    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getGetterName(String property){
        // ERASURE
        if ("hash".equals(property)) {
            // FIXME This is NOT the way to handle this, we should check that we're
            // actually trying to override the hash attribute defined in Equality
            return "hashCode";
        } else if ("string".equals(property)) {
            return "toString";
        }
        
        return "get"+capitalize(strip(property));
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
    }
    
    public static String getAttributeName(String getterName) {
        return Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);
    }

    public static String getConcreteMemberInterfaceImplementationName(String name){
        return name + "$impl";
    }
    
    // FIXME: add this to Declaration?
    public static boolean isClassAttribute(Declaration decl) {
        return (decl.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Class)
        	 && (decl.isCaptured() || decl.isShared());
    }

    // FIXME: add this to Declaration?
	public static boolean isInnerMethod(Declaration decl) {
        return decl.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
	}
	
	public static String getQualifiedPrefixedName(Declaration decl){
	    String name = decl.getQualifiedNameString();
	    String prefix;
	    if(decl instanceof ClassOrInterface)
	        prefix = "C";
	    else if(decl instanceof Value)
	        prefix = "V";
        else if(decl instanceof Getter)
            prefix = "G";
        else if(decl instanceof Setter)
            prefix = "S";
        else if(decl instanceof Method)
            prefix = "M";
        else
            throw new RuntimeException("Don't know how to prefix decl: "+decl);
	    return prefix + name;
	}

    public static String getSimpleName(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public static TypedDeclaration getTopmostRefinedDeclaration(TypedDeclaration decl){
        if(decl instanceof Parameter && decl.getContainer() instanceof Method){
            // for some reason this isn't filled up properly for Parameter so we look up the method's refined decl
            Method refinedMethod = (Method) getTopmostRefinedDeclaration((TypedDeclaration) decl.getContainer());
            // shortcut if the method doesn't override anything
            if(refinedMethod == decl.getContainer())
                return decl;
            if(refinedMethod.getParameterLists().size() != 1)
                throw new RuntimeException("Multiple parameter lists not supported");
            for(Parameter param : refinedMethod.getParameterLists().get(0).getParameters()){
                if(param.getName().equals(decl.getName()))
                    return param;
            }
            throw new RuntimeException("Can't find refined parameter: "+decl);
        }
        TypedDeclaration refinedDecl = (TypedDeclaration) decl.getRefinedDeclaration();
        if(refinedDecl != null && refinedDecl != decl)
            return getTopmostRefinedDeclaration(refinedDecl);
        return decl;
    }

    private final static Object IS_UNBOXED = new Object(){ public String toString() {return "IS_UNBOXED";};};

    public static boolean isUnBoxed(Term node){
        return node.getUnboxed();
    }

    public static boolean isUnBoxed(TypedDeclaration decl){
        return decl.getUnboxed();
    }

    public static void markUnBoxed(Term node) {
        node.setUnboxed(true);
    }

    public static void markUnBoxed(TypedDeclaration decl) {
        decl.setUnboxed(true);
    }

    public static BoxingStrategy getBoxingStrategy(Term node) {
        return isUnBoxed(node) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }

    public static BoxingStrategy getBoxingStrategy(TypedDeclaration decl) {
        return isUnBoxed(decl) ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED;
    }
}
