package com.redhat.ceylon.ceylondoc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.petebevin.markdown.MarkdownProcessor;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class Util {
	
	public static String getDoc(Declaration decl) {
		for (Annotation a : decl.getAnnotations()) {
			if (a.getName().equals("doc"))
				return new MarkdownProcessor().markdown(unquote(a.getPositionalArguments().get(0)));
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
	
	public static List<ProducedType> getSuperInterfaces(TypeDeclaration decl) {
		Set<ProducedType> superInterfaces = new HashSet<ProducedType>();
		List<ProducedType> satisfiedTypes = decl.getSatisfiedTypes();
		for(ProducedType satisfiedType : satisfiedTypes){
			superInterfaces.add(satisfiedType);
			superInterfaces.addAll(getSuperInterfaces(satisfiedType.getDeclaration()));
		}
		ArrayList<ProducedType> list = new ArrayList<ProducedType>();
		list.addAll(superInterfaces);
		removeDuplicates(list);
		return list;
	}	
	
	
    private static void removeDuplicates(List<ProducedType> superInterfaces) {
    	OUTER:
    	for(int i=0;i<superInterfaces.size();i++){
    		ProducedType pt1 = superInterfaces.get(i);
    		// compare it with each type after it
        	for(int j=i+1;j<superInterfaces.size();j++){
        		ProducedType pt2 = superInterfaces.get(j);
        		if(pt1.getDeclaration().equals(pt2.getDeclaration())){
        			if(pt1.isSubtypeOf(pt2)){
        				// we keep the first one because it is more specific
        				superInterfaces.remove(j);
        			}else{
        				// we keep the second one because it is more specific
        				superInterfaces.remove(i);
        				// since we removed the first type we need to stay at the same index
        				i--;
        			}
        			// go to next type
        			continue OUTER;
        		}
        	}
    	}
	}

	public static boolean isNullOrEmpty(Collection<? extends Object> collection ) {
    	return collection == null || collection.isEmpty(); 
    }
    
}
