package com.redhat.ceylon.compiler.util;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;

public class Util {
    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getGetterName(String property){
        return "get"+capitalize(strip(property));
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
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
	
	// FIXME: make this easier in Scope?
	public static String getQualifiedName(Declaration decl){
		StringBuffer str = new StringBuffer();
		for(String part : decl.getQualifiedName()){
			if(str.length() > 0)
				str.append(".");
			str.append(part);
		}
		return str.toString();
	}
}
