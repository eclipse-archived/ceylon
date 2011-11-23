/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.ceylondoc;

import static com.redhat.ceylon.ceylondoc.Util.join;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

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
        Module module = pkg.getModule();
        List<String> unprefixedName = pkg.getName().subList(module.getName().size(), pkg.getName().size());
        File dir = new File(destDir, join("/", unprefixedName));
        dir.mkdirs();
        return dir;
    }
    
    private File getFolder(ClassOrInterface klass) {
        return getFolder(getPackage(klass));
    }

    public String kind(Object obj) {
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
        } else if (modPgkOrDecl instanceof Module) {
            String filename = "index.html";
            file = new File(new File(destDir), filename);
        } else if (modPgkOrDecl instanceof Package) {
            String filename = "index.html";
            file = new File(getFolder((Package)modPgkOrDecl), filename);
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

        doc(module);
        makeIndex(module);

        copyResource("resources/style.css", new File(getResourcesDir(), "style.css"));
        copyResource("resources/shCore.css", new File(getResourcesDir(), "shCore.css"));
        copyResource("resources/shThemeDefault.css", new File(getResourcesDir(), "shThemeDefault.css"));
        copyResource("resources/jquery-1.7.min.js", new File(getResourcesDir(), "jquery-1.7.min.js"));
        copyResource("resources/ceylond.js", new File(getResourcesDir(), "ceylond.js"));
        copyResource("resources/shCore.js", new File(getResourcesDir(), "shCore.js"));
        copyResource("resources/shBrushCeylon.js", new File(getResourcesDir(), "shBrushCeylon.js"));
        copyResource("resources/search.html", new File(destDir, "search.html"));
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
            File file = new File(getFolder(pu.getPackage()), pu.getUnitFile().getName()+".html");
            File dir = file.getParentFile();
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Couldn't create directory for file: " + file);
            }
            FileWriter writer = new FileWriter(file);
            try {
            Markup markup = new Markup(writer);
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
                writer.close();
            }
        }
    }

    private void doc(Module module) throws IOException {
        FileWriter rootWriter = new FileWriter(getObjectFile(module));
        try {
            SummaryDoc summaryDoc = new SummaryDoc(this, rootWriter, module);
            summaryDoc.generate();
            for (Package pkg : module.getPackages()) {
                if (isRootPackage(module, pkg)) {
                    new PackageDoc(this, rootWriter, pkg).generate();
                } else {
                    FileWriter packageWriter = new FileWriter(getObjectFile(pkg));
                    try {
                        new PackageDoc(this, packageWriter, pkg).generate();
                    } finally {
                        packageWriter.close();
                    }
                }
            }
            summaryDoc.complete();
        } finally {
            rootWriter.close();
        }
        
    }

    private void makeIndex(Module module) throws IOException {
        File dir = getResourcesDir();
        FileWriter writer = new FileWriter(new File(dir, "index.js"));
        try {
            new IndexDoc(this, writer, module).generate();
        } finally {
            writer.close();
        }
    }

    private File getResourcesDir() throws IOException {
        File dir = new File(destDir, ".resources");
        if (!dir.exists()
                && !dir.mkdirs()) {
            throw new IOException();
        }
        return dir;
    }
    
    /**
     * Determines whether the given package is the 'root package' (i.e. has the 
     * same fully qualified name as) of the given module.
     * @param module
     * @param pkg
     * @return
     */
    boolean isRootPackage(Module module, Package pkg) {
        return pkg.getNameAsString().equals(module.getNameAsString());
    }

    private void copyResource(String path, File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()
                && !dir.mkdirs()) {
            throw new IOException();
        }
        InputStream resource = getClass().getResourceAsStream(path);
        copy(resource, file);
    }

    private void copy(InputStream resource, File file)
            throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(file);
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
                FileWriter writer = new FileWriter(getObjectFile(decl));
                try {
                    new ClassDoc(this, writer,
                            (ClassOrInterface) decl,
                            subclasses.get(decl),
                            satisfyingClassesOrInterfaces.get(decl)).generate();
                } finally {
                    writer.close();
                }
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

    Module getModule(Object modPkgOrDecl) {
        if (modPkgOrDecl instanceof Module) {
            return (Module)modPkgOrDecl;
        } else if (modPkgOrDecl instanceof Package) {
            return ((Package)modPkgOrDecl).getModule();
        } else if (modPkgOrDecl instanceof Declaration) {
            return getPackage((Declaration)modPkgOrDecl).getModule();
        }
        throw new RuntimeException();
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
        if (to instanceof Package 
                && isRootPackage(getModule(from), (Package)to)) {
            result += "#package";
        }
        return result;
    }
    
    protected String getResourceUrl(Object from, String to) throws IOException {
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getBaseUrl().resolve(".resources/" + to);
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
        Package pkg;
        String filename;
        if (modPkgOrDecl instanceof Element) {
            Unit unit = ((Element)modPkgOrDecl).getUnit();
            pkg = unit.getPackage();
            filename = unit.getFilename();
        } else if (modPkgOrDecl instanceof Package) {
            pkg = (Package)modPkgOrDecl;
            filename = "package.ceylon";
        } else if (modPkgOrDecl instanceof Module) {
            Module module = (Module)modPkgOrDecl;
            pkg = module.getPackage(module.getNameAsString());
            filename = "module.ceylon";
        } else {
            throw new RuntimeException("Unexpected: " + modPkgOrDecl);
        }

        File srcFile = new File(getFolder(pkg), filename + ".html").getCanonicalFile();
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
