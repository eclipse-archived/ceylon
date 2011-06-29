package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;

public class Util {
    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getGetterName(String property){
        return "get"+capitalize(property);
    }

    public static String getSetterName(String property){
        return "set"+capitalize(property);
    }

    // FIXME: add this to Declaration?
    public static boolean isClassAttribute(Declaration decl) {
        return (decl.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Class)
        	 && (decl.isCaptured() || decl.isShared());
    }

    // FIXME: add this to Declaration?
    public static boolean isToplevelAttribute(Declaration decl) {
        return decl.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Package;
    }

    // FIXME: add this to Declaration?
	public static boolean isInnerMethod(Declaration decl) {
        return decl.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Method;
	}
}
