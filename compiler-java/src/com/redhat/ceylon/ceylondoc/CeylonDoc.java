package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;

public abstract class CeylonDoc {

	protected Writer writer;
    protected String destDir;
    protected boolean showPrivate;

	public CeylonDoc(String destDir, boolean showPrivate) {
		this.destDir = destDir;
		this.showPrivate = showPrivate;
	}

	protected void setupWriter() throws IOException{
	    this.writer = new FileWriter(getOutputFile());
	}
	
	protected abstract File getOutputFile();

    protected void write(String... text) throws IOException {
		for(String s : text)
			writer.append(s);
	}

	protected void tag(String... tags) throws IOException {
		for(String tag : tags)
			writer.append("<").append(tag).append("/>\n");
	}

	protected String getPathToBase(ClassOrInterface klass) {
		return getPathToBase(getPackage(klass));
	}
	protected String getPathToBase(Package pkg) {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i=pkg.getName().size()-1;i>=0;i--){
			stringBuilder.append("..");
			if(i > 0)
				stringBuilder.append("/");
		}
		if (stringBuilder.length() == 0)  {
			return "."; //make links relative and not absolute
		}
		else {
			return stringBuilder.toString();
		}
	}

	protected void link(ProducedType type) throws IOException {
		link(type, false);
	}

	protected void link(ProducedType type, boolean qualified) throws IOException {
		TypeDeclaration decl = type.getDeclaration();
		if (decl instanceof ClassOrInterface) {
			link((ClassOrInterface)decl, type.getTypeArgumentList(), qualified);
        } else if (decl instanceof TypeParameter) {
            around("span class='type-parameter'", decl.getName());
		} else {
			write(type.getProducedTypeName());
		}
	}

	protected void link(ClassOrInterface decl, List<ProducedType> typeParameters, boolean qualified) throws IOException {
		String path = getPathToBase() + "/" + join("/", getPackage(decl).getName())+"/"+getFileName(decl);
		String name = qualified ? decl.getQualifiedNameString() : decl.getName();
		around("a href='"+path+"'", name);
		if(typeParameters != null && !typeParameters.isEmpty()){
			write("&lt;");
			boolean once = false;
			for(ProducedType typeParam : typeParameters){
				if(!once)
					once = true;
				else
					write(",");
				link(typeParam, qualified);
			}
			write("&gt;");
		}
	}

	protected abstract String getPathToBase();

	protected String getFileName(Scope klass) {
		List<String> name = new LinkedList<String>();
		while(klass instanceof Declaration){
			name.add(0, ((Declaration)klass).getName());
			klass = klass.getContainer();
		}
		return join(".", name)+".html";
	}

	protected File getFolder(Package pkg) {
		File dir = new File(destDir, join("/",pkg.getName()));
		dir.mkdirs();
		return dir;
	}

	protected File getFolder(ClassOrInterface klass) {
		return getFolder(getPackage(klass));
	}

	protected static Package getPackage(Scope decl) {
		while(!(decl instanceof Package)){
			decl = decl.getContainer();
		}
		return (Package) decl;
	}

	protected void around(String tag, String... text) throws IOException {
		open(tag);
		for(String s : text)
			writer.append(s);
		int space = tag.indexOf(" ");
		if(space > -1)
			tag = tag.substring(0, space);
		close(tag);
	}

	protected void close(String... tags) throws IOException {
		for(String tag : tags)
			writer.append("</").append(tag).append(">");
	}

	protected void open(String... tags) throws IOException {
		for(String tag : tags)
			writer.append("<").append(tag).append(">");
	}

	protected static String join(String str, List<String> parts) {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<String> iterator = parts.iterator();
		while(iterator.hasNext()){
			stringBuilder.append(iterator.next());
			if(iterator.hasNext())
				stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}

	protected void openTable(String title) throws IOException {
	    open("table");
	    open("tr class='TableHeadingColor'");
	    around("th", title);
	    close("tr");
	}

	protected void openTable(String title, String firstColumnTitle, String secondColumnTitle) throws IOException {
	    open("table");
	    open("tr class='TableHeadingColor'");
	    around("th colspan='2'", title);
	    close("tr");
	    open("tr class='TableSubHeadingColor'");
	    around("th", firstColumnTitle);
	    around("th", secondColumnTitle);
	    close("tr");
	}
	

	

   
}
