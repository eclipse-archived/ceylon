package com.redhat.ceylon.ceylondoc;


import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.IOException;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public abstract class ClassOrPackageDoc extends CeylonDoc {

	public ClassOrPackageDoc(String destDir, boolean showPrivate) {
		super(destDir, showPrivate);		
	}
	
    protected void doc(Method m) throws IOException {
        open("tr class='TableRowColor' id='method-"+m.getName()+"'");
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
        open("tr class='TableRowColor' id='attribute-"+f.getName()+"'");
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
    

	

}
