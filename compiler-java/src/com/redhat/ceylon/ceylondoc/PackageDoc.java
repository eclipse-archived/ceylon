package com.redhat.ceylon.ceylondoc;

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
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Value;

public class PackageDoc extends CeylonDoc {

	private Package pkg;
    private List<Class> classes;
    private List<Interface> interfaces;
    private ArrayList<Value> attributes;

	public PackageDoc(String destDir, Package pkg) throws IOException {
		super(destDir);
		this.pkg = pkg;
		loadMembers();
	}
	
	private void loadMembers() {
	    classes = new ArrayList<Class>();
        interfaces = new ArrayList<Interface>();
        attributes = new ArrayList<Value>();
        for(Declaration m : pkg.getMembers()){
            if(m instanceof Interface)
                interfaces.add((Interface) m);
            else if(m instanceof Class)
                classes.add((Class) m);
            else if(m instanceof Value){
                attributes.add((Value)m);
            }
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
	
	private void attributes() throws IOException {
	    openTable("Attributes", "Attribute", "Description");
	    for(Value v : attributes){
	        doc(v);
	    }
	    close("table");
	}

	private void interfaces() throws IOException {
	    openTable("Interfaces", "Interface", "Description");
		for(Interface i : interfaces){
		    doc(i);
		}
		close("table");
	}

	private void classes() throws IOException {
        openTable("Classes", "Class", "Description");
		for(Class c : classes){
		    doc(c);
		}
		close("table");
	}

	private void doc(ClassOrInterface c) throws IOException {
        open("tr class='TableRowColor'");
		open("td");
		link(c.getType());
		close("td");
		open("td");
		write(c.getName());
		close("td");
		close("tr");
	}

	private void doc(Value c) throws IOException {
	    open("tr class='TableRowColor'");
	    open("td");
	    link(c.getType());
	    close("td");
	    open("td");
	    write(c.getName());
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
