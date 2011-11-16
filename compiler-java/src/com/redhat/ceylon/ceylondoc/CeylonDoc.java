package com.redhat.ceylon.ceylondoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;

public abstract class CeylonDoc {

    protected Writer writer;
    private File file;
    protected final CeylonDocTool tool;
    protected final Module module;

    public CeylonDoc(Module module, CeylonDocTool tool, File file) {
        this.module = module;
        this.tool = tool;
        this.file = file;
    }
    
    protected void write(String... text) throws IOException {
        for (String s : text)
            writer.append(s);
    }

    protected void tag(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("<").append(tag).append("/>\n");
    }

    // FIXME: copied from ProducedType, we should make it public
    private static boolean isElementOfUnion(UnionType ut, TypeDeclaration td) {
        for (TypeDeclaration ct: ut.getCaseTypeDeclarations()) {
            if (ct.equals(td)) return true;
        }
        return false;
    }

    protected void link(ProducedType type) throws IOException {
        TypeDeclaration decl = type.getDeclaration();
        if(decl instanceof UnionType){
            UnionType ut = (UnionType) decl;
            // try to simplify if possible
            if (ut.getCaseTypes().size()==2) {
                Unit unit = decl.getUnit();
                if (isElementOfUnion(ut, unit.getNothingDeclaration())) {
                    link(unit.getDefiniteType(type));
                    write("?");
                    return;
                }
                if (isElementOfUnion(ut, unit.getEmptyDeclaration()) &&
                        isElementOfUnion(ut, unit.getSequenceDeclaration())) {
                    link(unit.getElementType(type));
                    write("[]");
                    return;
                }
            }
            // simplification failed, do it the hard way
            boolean first = true;
            for(ProducedType ud : ut.getCaseTypes()){
                if(first){
                    first = false;
                }else{
                    write("|");
                }
                link(ud);
            }
        }else if(decl instanceof ClassOrInterface){
            link((ClassOrInterface) decl, type.getTypeArgumentList());
        } else if (decl instanceof TypeParameter) {
            around("span class='type-parameter'", decl.getName());
        } else {
            write(type.getProducedTypeName());
        }
    }

    protected void link(ClassOrInterface decl, List<ProducedType> typeParameters) throws IOException {
        String name = decl.getName();
        around("a href='" + getObjectUrl(decl) + "'", name);
        if (typeParameters != null && !typeParameters.isEmpty()) {
            write("&lt;");
            boolean once = false;
            for (ProducedType typeParam : typeParameters) {
                if (!once)
                    once = true;
                else
                    write(",");
                link(typeParam);
            }
            write("&gt;");
        }
    }

    protected void linkToMember(Declaration decl) throws IOException {
        ClassOrInterface container = (ClassOrInterface) decl.getContainer();
        String name = decl.getName();
        around("a href='" + getObjectUrl(container) + "#"+ name + "'", name);
    }

    protected String getFileName(Scope klass) {
        List<String> name = new LinkedList<String>();
        while (klass instanceof Declaration) {
            name.add(0, ((Declaration) klass).getName());
            klass = klass.getContainer();
        }
        return join(".", name) + ".html";
    }

    protected File getFolder(Package pkg) {
        File dir = new File(tool.getDestDir(), join("/", pkg.getName()));
        dir.mkdirs();
        return dir;
    }

    protected File getFolder(ClassOrInterface klass) {
        return getFolder(getPackage(klass));
    }

    protected static Package getPackage(Scope decl) {
        while (!(decl instanceof Package)) {
            decl = decl.getContainer();
        }
        return (Package) decl;
    }

    protected void around(String tag, String... text) throws IOException {
        open(tag);
        for (String s : text)
            writer.append(s);
        int space = tag.indexOf(" ");
        if (space > -1)
            tag = tag.substring(0, space);
        close(tag);
    }

    protected void close(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("</").append(tag).append(">");
    }

    protected void open(String... tags) throws IOException {
        for (String tag : tags)
            writer.append("<").append(tag).append(">");
    }

    protected static String join(String str, List<String> parts) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = parts.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext())
                stringBuilder.append(str);
        }
        return stringBuilder.toString();
    }

    protected void openTable(String title) throws IOException {
        open("table class='category'");
        open("tr class='TableHeadingColor'");
        around("th", title);
        close("tr");
    }

    protected void openTable(String title, String firstColumnTitle, String secondColumnTitle) throws IOException {
        open("table class='category'");
        open("tr class='TableHeadingColor'");
        around("th colspan='2'", title);
        close("tr");
        open("tr class='TableSubHeadingColor'");
        around("th", firstColumnTitle);
        around("th", secondColumnTitle);
        close("tr");
    }

    protected boolean include(Declaration decl){
        return tool.isShowPrivate() || decl.isShared();
    }

	protected void setupWriter() throws IOException{
	    this.writer = new FileWriter(getOutputFile());
	}
	
	protected final File getOutputFile() {
	    return this.file;
	}


    /**
     * Returns the absolute URI of the page for the given thing
     * @param obj (Module, Package, Declaration etc)
     */
    private URI getAbsoluteObjectUrl(Object obj) {
        File f = tool.getObjectFile(obj);
        if (f == null) {
            throw new RuntimeException(obj + " doesn't have a ceylond page");
        }
        return f.toURI();
    }
    
    /**
     * Gets the base URL
     * @return Gets the base URL
     */
    protected URI getBaseUrl() {
        return new File(this.tool.getDestDir()).toURI();
    }
    
    /**
     * Generates a relative URL such that:
     * <pre>
     *   uri1.resolve(relativize(url1, url2)).equals(uri2);
     * </pre>
     * @param uri
     * @param uri2
     * @return A URL suitable for a link from a page at uri to a page at uri2
     */
    private URI relativize(URI uri, URI uri2) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("Expected " + uri + " to be absolute");
        }
        if (!uri2.isAbsolute()) {
            throw new IllegalArgumentException("Expected " + uri2 + " to be absolute");
        }
        URI baseUrl = getBaseUrl();
        StringBuilder sb = new StringBuilder();
        URI r = uri;
        if (!r.equals(baseUrl)) {
            sb.append("./");
            r = uri.resolve(URI.create(sb.toString()));
            if (!r.equals(baseUrl)) {
                r = uri;
            }
        }
        while (!r.equals(baseUrl)) {
            sb.append("../");
            r = uri.resolve(URI.create(sb.toString()));
        }
        URI result = URI.create(sb.toString() + baseUrl.relativize(uri2));
        if (result.isAbsolute()) {
            throw new RuntimeException();
        }
        if (!uri.resolve(result).equals(uri2)) {
            throw new RuntimeException("Assertion fails: url=\""+uri + "\", uri2=\"" + uri2 + "\", result=\"" + result + "\"");
        }
        return result;
    }
    
    protected String getObjectUrl(Object from, Object to) {
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getAbsoluteObjectUrl(to);
        String result = relativize(fromUrl, toUrl).toString();
        return result;
    }
    
    protected String getResourceUrl(Object from, String to) {
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getBaseUrl().resolve(to);
        String result = relativize(fromUrl, toUrl).toString();
        return result;
    }
    
    /**
     * Gets a URL for the source file containing the given thing
     * @param from Where the link is relative to
     * @param modPkgOrDecl e.g. Module, Package or Declaration
     * @return A (relative) URL, or null if no source file exists (e.g. for a
     * package or a module without a descriptor)
     */
    protected String getSrcUrl(Object from, Object modPkgOrDecl) {
        URI fromUrl = getAbsoluteObjectUrl(from);
        String pkgName;
        String filename;
        if (modPkgOrDecl instanceof Element) {
            Unit unit = ((Element)modPkgOrDecl).getUnit();
            pkgName = unit.getPackage().getNameAsString();
            filename = unit.getFilename();
        } else if (modPkgOrDecl instanceof Package) {
            pkgName = ((Package)modPkgOrDecl).getNameAsString();
            filename = "package.ceylon";
        } else if (modPkgOrDecl instanceof Module) {
            pkgName = ((Module)modPkgOrDecl).getNameAsString();
            filename = "module.ceylon";
        } else {
            throw new RuntimeException("Unexpected: " + modPkgOrDecl);
        }
        File dir = new File(tool.getDestDir(), pkgName.replace(".", "/"));
        File srcFile = new File(dir, filename);
        String result;
        if (srcFile.exists()) {
            URI url = srcFile.toURI();
            result = relativize(fromUrl, url).toString();
        } else {
            result = null;
        }
        return result;
    }
    
    protected abstract String getObjectUrl(Object to);
    
    protected abstract String getResourceUrl(String to);
    
    protected abstract String getSrcUrl(Object to);


}


