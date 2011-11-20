/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.join;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Walker;

public class CeylonDocTool {

    private List<PhasedUnit> phasedUnits;
    private Modules modules;
    private String srcDir;
    private String destDir;
    /**
     * The {@linkplain #include(Declaration) visible} subclasses of the key
     */
    private Map<ClassOrInterface, List<ClassOrInterface>> subclasses = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    /**
     * The {@linkplain #include(Declaration) visible} class/interfaces 
     * that satisfy the key
     */
    private Map<TypeDeclaration, List<ClassOrInterface>> satisfyingClassesOrInterfaces = new HashMap<TypeDeclaration, List<ClassOrInterface>>();
    private boolean showPrivate;
    private boolean omitSource;
    private Map<Declaration, Node> sourceLocations = new HashMap<Declaration, Node>();

    public CeylonDocTool(List<PhasedUnit> phasedUnits, Modules modules, boolean showPrivate) {
        this.phasedUnits = phasedUnits;
        this.modules = modules;
        this.showPrivate = showPrivate;
    }

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public String getDestDir() {
        return destDir;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public boolean isShowPrivate() {
        return showPrivate;
    }

    public boolean isOmitSource() {
        return omitSource;
    }

    public void setOmitSource(boolean omitSource) {
        this.omitSource = omitSource;
    }

    private String getFileName(Scope klass) {
        List<String> name = new LinkedList<String>();
        while(klass instanceof Declaration){
            name.add(0, ((Declaration)klass).getName());
            klass = klass.getContainer();
        }
        return join(".", name);
    }

    private File getFolder(Package pkg) {
        File dir = new File(destDir, join("/",pkg.getName()));
        dir.mkdirs();
        return dir;
    }

    private File getFolder(ClassOrInterface klass) {
        return getFolder(getPackage(klass));
    }

    private String kind(Object obj) {
        if (obj instanceof Class) {
            return Character.isUpperCase(((Class)obj).getName().charAt(0)) ? "class" : "object";
        } else if (obj instanceof Interface) {
            return "interface";
        } else if (obj instanceof AttributeDeclaration
                || obj instanceof Getter) {
            return "attribute";
        } else if (obj instanceof Method) {
            return "function";
        } else if (obj instanceof Value) {
            return "value";
        } else if (obj instanceof Package) {
            return "package";
        } else if (obj instanceof Module) {
            return "module";
        }
        throw new RuntimeException("Unexpected: " + obj);
    }

    File getObjectFile(Object modPgkOrDecl) throws IOException {
        final File file;
        if (modPgkOrDecl instanceof ClassOrInterface) {
            ClassOrInterface klass = (ClassOrInterface)modPgkOrDecl;
            String filename = kind(modPgkOrDecl) + "_" + getFileName(klass) + ".html";
            file = new File(getFolder(klass), filename);
        } else if (modPgkOrDecl instanceof Package) {
            Package pkg = (Package)modPgkOrDecl;
            String filename = "index.html";
            file = new File(getFolder(pkg), filename);
        } else if (modPgkOrDecl instanceof Module) {
            String filename = "index.html";
            file = new File(new File(destDir), filename);
        } else {
            throw new RuntimeException("Unexpected: " + modPgkOrDecl);
        }
        return file.getCanonicalFile();
    }

    public void makeDoc() throws IOException{

        if (!omitSource) {
            buildSourceLocations();
            copySourceFiles();
        }
        
        for (PhasedUnit pu : phasedUnits) {
            for (Declaration decl : pu.getUnit().getDeclarations()) {
                if(!include(decl)) {
                    continue;
                }
                if (decl instanceof ClassOrInterface) {
                    getObjectFile(decl);
                    ClassOrInterface c = (ClassOrInterface) decl;            		 
                    // subclasses map
                    if (c instanceof Class) {
                        ClassOrInterface superclass = c.getExtendedTypeDeclaration();            		 
                        if (superclass != null) {
                            if (subclasses.get(superclass) ==  null) {
                                subclasses.put(superclass, new ArrayList<ClassOrInterface>());
                            }
                            subclasses.get(superclass).add(c);
                        }
                    }

                    List<TypeDeclaration> satisfiedTypes = new ArrayList<TypeDeclaration>(c.getSatisfiedTypeDeclarations());            		 
                    if (satisfiedTypes != null && satisfiedTypes.isEmpty() == false) {
                        // satisfying classes or interfaces map
                        for (TypeDeclaration satisfiedType : satisfiedTypes) {
                            if (satisfyingClassesOrInterfaces.get(satisfiedType) ==  null) {
                                satisfyingClassesOrInterfaces.put(satisfiedType, new ArrayList<ClassOrInterface>());
                            }
                            satisfyingClassesOrInterfaces.get(satisfiedType).add(c);
                        }
                    }
                }
            }
        }

        Module module = null;
        for (PhasedUnit pu : phasedUnits) {
            if (module == null) {
                module = pu.getPackage().getModule();
                getObjectFile(module);
                for (Package pkg : module.getPackages()) {
                    getObjectFile(pkg);
                }
            } else if (pu.getPackage().getModule() != module) {
                throw new RuntimeException("Documentation of multiple modules not supported yet");
            }
            for (Declaration decl : pu.getUnit().getDeclarations()) {
                doc(decl);
            }
        }

        for (Package pkg : module.getPackages()) {
            doc(pkg);
        }
        doc(module);

        copyResource("resources/style.css", "style.css");
        copyResource("resources/shCore.css", "shCore.css");
        copyResource("resources/shThemeDefault.css", "shThemeDefault.css");
        copyResource("resources/jquery-1.7.min.js", "jquery-1.7.min.js");
        copyResource("resources/ceylond.js", "ceylond.js");
        copyResource("resources/shCore.js", "shCore.js");
        copyResource("resources/shBrushCeylon.js", "shBrushCeylon.js");
    }

    private void buildSourceLocations() {
        for (PhasedUnit pu : phasedUnits) {
            CompilationUnit cu = pu.getCompilationUnit();
            Walker.walkCompilationUnit(new Visitor() {
                public void visit(Tree.Declaration decl) {
                    sourceLocations.put(decl.getDeclarationModel(), decl);
                    super.visit(decl);
                }
                public void visit(Tree.MethodDeclaration decl) {
                    sourceLocations.put(decl.getDeclarationModel(), decl);
                    super.visit(decl);
                }
                public void visit(Tree.AttributeDeclaration decl) {
                    sourceLocations.put(decl.getDeclarationModel(), decl);
                    super.visit(decl);
                }
            }, cu);
        }
    }

    private void copySourceFiles() throws FileNotFoundException, IOException {
        for (PhasedUnit pu : phasedUnits) {
            Markup markup = new Markup(new File(destDir, pu.getPathRelativeToSrcDir()+".html"));
            markup.setupWriter();
            try {
                markup.open("html", "head");
                markup.around("title", pu.getUnit().getFilename());
                Package decl = pu.getUnit().getPackage();
                markup.tag("link href='" + getResourceUrl(decl, "shCore.css") + "' rel='stylesheet' type='text/css'");
                markup.tag("link href='" + getResourceUrl(decl, "shThemeDefault.css") + "' rel='stylesheet' type='text/css'");
                markup.around("script type='text/javascript' src='"+getResourceUrl(decl, "jquery-1.7.min.js")+"'");
                markup.around("script type='text/javascript' src='"+getResourceUrl(decl, "ceylond.js")+"'");
                markup.around("script src='" + getResourceUrl(decl, "shCore.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(decl, "shBrushCeylon.js") + "' type='text/javascript'");
                markup.close("head");
                markup.open("body", "pre class='brush: ceylon'");
                // XXX source char encoding
                BufferedReader input = new BufferedReader(new InputStreamReader(pu.getUnitFile().getInputStream()));
                try{
                    String line = input.readLine();
                    while (line != null) {
                        markup.text(line, "\n");
                        line = input.readLine();
                    }
                } finally {
                    input.close();
                }
                markup.close("pre", "body", "html");
            } finally {
                markup.writer.close();
            }
        }
    }

    private void doc(Module module) throws IOException {
        new SummaryDoc(this, module).generate();
    }

    private void doc(Package pkg) throws IOException {
        new PackageDoc(this, pkg).generate();
    }

    private void copyResource(String path, String target) throws IOException {
        InputStream resource = getClass().getResourceAsStream(path);
        copy(resource, target);
    }

    private void copy(InputStream resource, String target)
            throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(new File(destDir, target));
        byte[] buf = new byte[1024];
        int read;
        while ((read = resource.read(buf)) > -1) {
            os.write(buf, 0, read);
        }
        os.flush();
        os.close();
    }

    private void doc(Declaration decl) throws IOException {
        if (decl instanceof ClassOrInterface) {
            if (include(decl)) {
                Scope scope = getPackage(decl);
                new ClassDoc(this,
                        (ClassOrInterface) decl,
                        subclasses.get(decl),
                        satisfyingClassesOrInterfaces.get(decl)).generate();
            }
        }
    }

    Package getPackage(Declaration decl) {
        Scope scope = decl.getContainer();
        while (!(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        return (Package)scope;
    }

    Module getModule(Declaration decl) {
        return getPackage(decl).getModule();
    }


    protected boolean include(Declaration decl){
        return showPrivate || decl.isShared();
    }
    
    /**
     * Returns the absolute URI of the page for the given thing
     * @param obj (Module, Package, Declaration etc)
     * @throws IOException 
     */
    private URI getAbsoluteObjectUrl(Object obj) throws IOException {
        File f = getObjectFile(obj);
        if (f == null) {
            throw new RuntimeException(obj + " doesn't have a ceylond page");
        }
        return f.toURI();
    }
    
    /**
     * Gets the base URL
     * @return Gets the base URL
     */
    protected URI getBaseUrl() throws IOException {
        return new File(getDestDir()).getCanonicalFile().toURI();
    }
    
    /**
     * Generates a relative URL such that:
     * <pre>
     *   uri1.resolve(relativize(url1, url2)).equals(uri2);
     * </pre>
     * @param uri
     * @param uri2
     * @return A URL suitable for a link from a page at uri to a page at uri2
     * @throws IOException 
     */
    private URI relativize(URI uri, URI uri2) throws IOException {
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
    
    protected String getObjectUrl(Object from, Object to) throws IOException {
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getAbsoluteObjectUrl(to);
        String result = relativize(fromUrl, toUrl).toString();
        return result;
    }
    
    protected String getResourceUrl(Object from, String to) throws IOException {
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
     * @throws IOException 
     */
    protected String getSrcUrl(Object from, Object modPkgOrDecl) throws IOException {
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
        File dir = new File(getDestDir(), pkgName.replace(".", "/"));
        File srcFile = new File(dir, filename + ".html").getCanonicalFile();
        String result;
        if (srcFile.exists()) {
            URI url = srcFile.toURI();
            result = relativize(fromUrl, url).toString();
        } else {
            result = null;
        }
        return result;
    }
    
    /**
     * Returns the starting and ending line number of the given declaration
     * @param decl The declaration
     * @return [start, end]
     */
    int[] getDeclarationSrcLocation(Declaration decl) {
        Node node = this.sourceLocations.get(decl);
        return new int[]{node.getToken().getLine(), node.getEndToken().getLine()};
    }
}
