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

	public CeylonDoc(String destDir) {
		this.destDir = destDir;
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
		for(int i=pkg.getQualifiedName().size()-1;i>=0;i--){
			stringBuilder.append("..");
			if(i > 0)
				stringBuilder.append("/");
		}
		return stringBuilder.toString();
	}

	protected void link(ProducedType type) throws IOException {
		TypeDeclaration decl = type.getDeclaration();
		link(decl, false);
	}

	protected void link(TypeDeclaration decl, boolean qualified) throws IOException {
		if(decl instanceof UnionType){
			boolean first = true;
			for(TypeDeclaration ud : ((UnionType)decl).getCaseTypeDeclarations()){
				if(first){
					first = false;
				}else{
					write("|");
				}
				link(ud, qualified);
			}
		}else if(decl instanceof ClassOrInterface){
			link((ClassOrInterface)decl, qualified);
        }else if(decl instanceof TypeParameter){
            around("span class='type-parameter'", decl.getName());
		}else{
			write(decl.toString());
		}
	}

	protected void link(ClassOrInterface decl, boolean qualified) throws IOException {
		String path = getPathToBase() + "/" + join("/", getPackage(decl).getQualifiedName())+"/"+getFileName(decl);
		around("a href='"+path+"'", qualified ? join(".", decl.getQualifiedName()) : decl.getName());
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
		File dir = new File(destDir, join("/",pkg.getQualifiedName()));
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
			writer.append("</").append(tag).append(">\n");
	}

	protected void open(String... tags) throws IOException {
		for(String tag : tags)
			writer.append("<").append(tag).append(">\n");
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

}
