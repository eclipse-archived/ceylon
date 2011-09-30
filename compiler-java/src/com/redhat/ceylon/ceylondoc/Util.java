package com.redhat.ceylon.ceylondoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class Util {
	
	public static String getDoc(Declaration decl) {
		for (Annotation a : decl.getAnnotations()) {
			if (a.getName().equals("doc"))
				return unquote(a.getPositionalArguments().get(0));
		}
		return "";
	}

	private static String unquote(String string) {
		return string.substring(1, string.length() - 1);
	}
	
	public static String getModifiers(Declaration d) {
		StringBuilder modifiers = new StringBuilder();
		if (d.isShared()) {
			modifiers.append("shared ");
		}
		if (d.isFormal()) {
			modifiers.append("formal ");
		} else {
			if (d.isActual()) {
				modifiers.append("actual ");
			}
			if (d.isDefault()) {
				modifiers.append("default ");
			}
		}	
		if (d instanceof Value) {
			Value v = (Value) d;
			if (v.isVariable()) {
				modifiers.append("variable ");
			}
		} else if (d instanceof Class) {
			Class c = (Class) d;
			if (c.isAbstract()) {
				modifiers.append("abstract ");
			}
		}
		return modifiers.toString().trim();
	}
	
	public static List<TypeDeclaration> getAncestors(TypeDeclaration decl) {
		List<TypeDeclaration> ancestors =  new ArrayList<TypeDeclaration>();
		TypeDeclaration ancestor = decl.getExtendedTypeDeclaration();
		while (ancestor != null) {
			ancestors.add(ancestor);
			ancestor = ancestor.getExtendedTypeDeclaration();
		}
		return ancestors;
	}	
	
	public static List<TypeDeclaration> getSuperInterfaces(TypeDeclaration decl) {
		List<TypeDeclaration> superInterfaces = new ArrayList<TypeDeclaration>();
		List<TypeDeclaration> satisfiedTypes = decl.getSatisfiedTypeDeclarations();
		while (satisfiedTypes.isEmpty() == false) {
			 List<TypeDeclaration> superSatisfiedTypes = new ArrayList<TypeDeclaration>(); 
			 for (TypeDeclaration satisfiedType : satisfiedTypes) {
				 if (superInterfaces.contains(satisfiedType) == false && superSatisfiedTypes.contains(satisfiedType) == false) { 
					 superInterfaces.add(satisfiedType);
					 if (satisfiedType.getSatisfiedTypeDeclarations().isEmpty() == false) {
						 for (TypeDeclaration superSatisfiedType: satisfiedType.getSatisfiedTypeDeclarations() ) {
							 if (superInterfaces.contains(superSatisfiedType) == false && superSatisfiedTypes.contains(superSatisfiedType) == false) {
								 superSatisfiedTypes.add(superSatisfiedType);
							 }
						 }
					 }
				 }	 

				 
			 }
			 satisfiedTypes = superSatisfiedTypes; 
		}
		return superInterfaces;
	}	
	
	
    public static boolean isNullOrEmpty(Collection<? extends Object> collection ) {
    	return collection == null || collection.isEmpty(); 
    }
    
}
