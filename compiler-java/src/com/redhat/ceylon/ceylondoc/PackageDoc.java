package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.getDoc;
import static com.redhat.ceylon.ceylondoc.Util.getModifiers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Value;


public class PackageDoc extends ClassOrPackageDoc {

	private Package pkg;
    private List<Class> classes;
    private List<Interface> interfaces;
    private List<Value> attributes;
    private List<Method> methods;

	public PackageDoc(String destDir, Package pkg, boolean showPrivate) throws IOException {
		super(destDir, showPrivate);
		this.pkg = pkg;
		loadMembers();
	}
	
	private void loadMembers() {
	    classes = new ArrayList<Class>();
        interfaces = new ArrayList<Interface>();
        attributes = new ArrayList<Value>();
        methods = new ArrayList<Method>();
        for(Declaration m : pkg.getMembers()){
            if(m instanceof Interface)
                interfaces.add((Interface) m);
            else if(m instanceof Class)
                classes.add((Class) m);
            else if(m instanceof Value)
                attributes.add((Value)m);
            else if(m instanceof Method)
                methods.add((Method)m);
        }
        Comparator<Declaration> comparator = new Comparator<Declaration>(){
            @Override
            public int compare(Declaration a, Declaration b) {
                return a.getName().compareTo(b.getName());
            }
        };
        Collections.sort(classes, comparator );
        Collections.sort(interfaces, comparator );
        Collections.sort(attributes, comparator );
        Collections.sort(methods, comparator );
    }

    public void generate() throws IOException {
	    setupWriter();
		open("html");
		open("head");
		around("title", "Package "+pkg.getName());
		if(pkg.getNameAsString().isEmpty())
		    tag("link href='style.css' rel='stylesheet' type='text/css'");
		else
		    tag("link href='"+getPathToBase(pkg)+"/style.css' rel='stylesheet' type='text/css'");
		close("head");
		open("body");
		summary();
		attributes();
        methods();
        interfaces();
		classes();
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
        open("div class='selected'");
        write("Package");
        close("div");
        open("div");
        write("Class");
        close("div");
        close("div");

        open("div class='head'");
		around("h1", "Package ", pkg.getNameAsString());
		close("div");
	}
	
	private void methods() throws IOException {
        if(methods.isEmpty())
            return;
        openTable("Methods", "Modifier and Type", "Method and Description");
		for(Method m : methods){
		    doc(m);
		}
		close("table");
	}

    private void attributes() throws IOException {
	    openTable("Attributes", "Modifier and Type", "Name and Description");
	    for(Value v : attributes){
	        doc(v);
	    }
	    close("table");
	}

	private void interfaces() throws IOException {
	    openTable("Interfaces", "Modifier and Type", "Description");
		for(Interface i : interfaces){
		    doc(i);
		}
		close("table");
	}

	private void classes() throws IOException {
        openTable("Classes", "Modifier and Type", "Description");
		for(Class c : classes){
		    doc(c);
		}
		close("table");
	}

	private void doc(ClassOrInterface d) throws IOException {
        open("tr class='TableRowColor'");
		open("td");
		around("span class='modifiers'",getModifiers(d));
		write(" ");
		link(d.getType());
		close("td");
		open("td");		
		write(getDoc(d));
		close("td");
		close("tr");
	}

	@Override
	protected String getPathToBase() {
		return getPathToBase(pkg);
	}

    @Override
    protected File getOutputFile() {
        return new File(getFolder(pkg), "index.html");
    }
}
