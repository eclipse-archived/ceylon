package com.redhat.ceylon.ceylondoc;

import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public abstract class ClassOrPackageDoc extends CeylonDoc {

	public ClassOrPackageDoc(String destDir) {
		super(destDir);	
	}
	
	protected String getModifiers(Declaration d) {
		StringBuilder modifiers = new StringBuilder();
		if (d.isShared()) {
			modifiers.append("shared ");
		}
		if (d instanceof Value) {
			Value v = (Value) d;
			if (v.isVariable()) {
				modifiers.append("variable");
			}
		} else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
			com.redhat.ceylon.compiler.typechecker.model.Class c  = (com.redhat.ceylon.compiler.typechecker.model.Class) d;
			if (c.isAbstract()) {
				modifiers.append("abstract");
			}
		}			
		return modifiers.toString().trim();
	}
	

    protected void doc(Method m) throws IOException {
        open("tr class='TableRowColor'");
		open("td");
		around("span class='modifiers'",getModifiers(m));
		write(" ");
		link(m.getType());
		List<TypeParameter> typeParameters = m.getTypeParameters();
		if(!typeParameters.isEmpty()){
		    write("&lt;");
		    boolean first = true;
		    for(TypeParameter type : typeParameters){
		        if(first)
		            first = false;
		        else
		            write(", ");
		        write(type.getName());
		    }
            write("&gt;");
		}
		close("td");
		open("td");
		write(m.getName());
		writeParameterList(m.getParameterLists());
		tag("br");
		around("span class='doc'", getDoc(m));
		close("td");
		close("tr");
	}
    
	protected void doc(MethodOrValue f) throws IOException {
		if (f instanceof Value) {
			f = (Value) f;
		}
        open("tr class='TableRowColor'");
		open("td");
		around("span class='modifiers'",getModifiers(f));
		write(" ");
		link(f.getType());
		close("td");
        open("td");
		write(f.getName());
        tag("br");
        around("span class='doc'", getDoc(f));
        close("td");
		close("tr");
	}
    
    protected void writeParameterList(List<ParameterList> parameterLists) throws IOException {
		for(ParameterList lists : parameterLists){
			write("(");
			boolean first = true;
			for(Parameter param : lists.getParameters()){
				if(!first){
					write(", ");
				}else{
					first = false;
				}
				link(param.getType());
				write(" ", param.getName());
			}
			write(")");
		}
	}
    
	protected String getDoc(Declaration decl) {
	    for (Annotation a : decl.getAnnotations()){
	        if(a.getName().equals("doc"))
	            return unquote(a.getPositionalArguments().get(0));
	    }
        return "";
    }
	

}
