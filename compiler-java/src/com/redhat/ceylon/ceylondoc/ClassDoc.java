package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class ClassDoc extends CeylonDoc {

	private ClassOrInterface klass;
    private List<Method> methods;
    private List<MethodOrValue> attributes;

	public ClassDoc(String destDir, ClassOrInterface klass) throws IOException {
		super(destDir);
		this.klass = klass;
		loadMembers();
	}
	
	private void loadMembers() {
	        methods = new ArrayList<Method>();
	        attributes = new ArrayList<MethodOrValue>();
	        for(Declaration m : klass.getMembers()){
	            if(m instanceof Value)
                    attributes.add((Value) m);
	            else if(m instanceof Getter)
	                attributes.add((Getter) m);
	            else if(m instanceof Method)
                    methods.add((Method) m);
	        }
	        Comparator<MethodOrValue> comparator = new Comparator<MethodOrValue>(){
	            @Override
	            public int compare(MethodOrValue a, MethodOrValue b) {
	                return a.getName().compareTo(b.getName());
	            }
	        };
	        Collections.sort(methods, comparator );
	        Collections.sort(attributes, comparator );
    }

    public void generate() throws IOException {
	    setupWriter();
		open("html");
		open("head");
		around("title", "Class for "+klass.getName());
		tag("link href='"+getPathToBase(klass)+"/style.css' rel='stylesheet' type='text/css'");
		close("head");
		open("body");
		summary();
		if(klass instanceof Class)
			constructor((Class)klass);
		attributes();
		methods();
		close("body");
		close("html");
		writer.flush();
		writer.close();
	}

	private void summary() throws IOException {
        open("div class='nav'");
        open("div");
        around("a href='"+getPathToBase()+"/overview-summary.html'", "Overview");
        close("div");
        open("div");
        around("a href='index.html'", "Package");
        close("div");
        open("div class='selected'");
        write("Class");
        close("div");
        close("div");

        open("div class='head'");
		around("div class='package'", getPackage(klass).getNameAsString());
		around("div class='type'", klass instanceof Class ? "Class " : "Interface ", klass.getName());
		LinkedList<ClassOrInterface> superTypes = new LinkedList<ClassOrInterface>();
		superTypes.add(klass);
		ClassOrInterface type = klass.getExtendedTypeDeclaration(); 
		while(type != null){
			superTypes.add(0, type);
			type = type.getExtendedTypeDeclaration();
		}
		int i=0;
		for(ClassOrInterface superType : superTypes){
			open("ul class='inheritance'", "li");
			link(superType, true);
			i++;
		}
		while(i-- > 0){
			close("li", "ul");
		}
		open("div class='type-parameters'");
		write("Type parameters:");
		open("ul");
		for(TypeParameter typeParam : klass.getTypeParameters()){
			around("li", typeParam.getName());
		}
		close("ul");
		close("div");
		open("div class='implements'");
		write("Implemented interfaces: ");
		boolean first = true;
		for (TypeDeclaration satisfied : klass.getSatisfiedTypeDeclarations()){
			if(!first){
				write(", ");
			}else{
				first = false;
			}
			link(satisfied, true);
		}
		close("div");
        around("div class='doc'", getDoc(klass));
		close("div");
	}

	private void constructor(Class klass) throws IOException {
		openTable("Constructor");
		open("tr", "td");
		write(klass.getName());
		writeParameterList(klass.getParameterLists());
		close("td", "tr", "table");
	}

	private void methods() throws IOException {
        openTable("Methods", "Modifier and Type", "Method and Description");
		for(Method m : methods){
		    doc(m);
		}
		close("table");
	}

	private void attributes() throws IOException {
	    openTable("Attributes", "Modifier and Type", "Attribute and Description");
		for(MethodOrValue attribute : attributes){
		    doc(attribute);
		}
		close("table");
	}

    private void doc(Method m) throws IOException {
        open("tr class='TableRowColor'");
		open("td");
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

	private String getDoc(Declaration decl) {
	    for (Annotation a : decl.getAnnotations()){
	        if(a.getName().equals("doc"))
	            return unquote(a.getPositionalArguments().get(0));
	    }
        return "";
    }

    private String unquote(String string) {
        return string.substring(1, string.length()-1);
    }

    private void writeParameterList(List<ParameterList> parameterLists) throws IOException {
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

	private void doc(MethodOrValue f) throws IOException {
        open("tr class='TableRowColor'");
		open("td");
		link(f.getType());
		close("td");
        open("td");
		write(f.getName());
        tag("br");
        around("span class='doc'", getDoc(f));
        close("td");
		close("tr");
	}

	@Override
	protected String getPathToBase() {
		return getPathToBase(klass);
	}

    @Override
    protected File getOutputFile() {
        return new File(getFolder(klass), getFileName(klass));
    }
}
