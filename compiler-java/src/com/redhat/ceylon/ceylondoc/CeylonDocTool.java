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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.CMRException;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.Tool;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
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
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import com.redhat.ceylon.tools.ModuleSpec;

@Summary("Generates Ceylon API documentation from Ceylon source files")
@Description("The default module repositories are `modules` and " +
        "http://modules.ceylon-lang.org, and the default source directory is `source`. " +
        "The default output module repository is `modules`." +
        "\n\n"+
        "The `<modules>` are the names (with an optional version) of the modules " +
        "to compile the documentation of." +
        "\n\n"+
        "The documentation compiler searches for compilation units belonging " +
        "to the specified modules in the specified source directories and in " +
        "source archives in the specified module repositories. For each " +
        "specified module, the compiler generates a set of XHTML pages in the " +
        "module documentation directory (the module-doc directory) of the " +
        "specified output module repository." +
        "\n\n" +
        "The compiler searches for source in the following locations:" +
        "\n\n" +
        "* source archives in the specified repositories, and\n" +
        "* module directories in the specified source directories." +
        "\n\n" +
        "If no version identifier is specified for a module, the module is " +
        "assumed to exist in a source directory.")
@RemainingSections(
"## EXAMPLE\n" +
"\n" +
"The following would compile the `org.hibernate` module source code found in " +
"the `~/projects/hibernate/src` directory to the " +
"repository `~/projects/hibernate/build`:\n" +
"\n" +
"    ceylon doc org.hibernate/3.0.0.beta \\\n"+
"        --src ~/projects/hibernate/src \\\n"+
"        --out ~/projects/hibernate/build")
public class CeylonDocTool implements Tool {

    private List<PhasedUnit> phasedUnits;
    private List<Module> modules;
    private String outputRepository;
    private String user,pass;
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} subclasses of the key
     */
    private Map<ClassOrInterface, List<ClassOrInterface>> subclasses = new HashMap<ClassOrInterface, List<ClassOrInterface>>();
    /**
     * The {@linkplain #shouldInclude(Declaration) visible} class/interfaces 
     * that satisfy the key
     */
    private Map<TypeDeclaration, List<ClassOrInterface>> satisfyingClassesOrInterfaces = new HashMap<TypeDeclaration, List<ClassOrInterface>>();    
    private boolean includeNonShared;
    private boolean includeSourceCode;
    private Map<Declaration, PhasedUnit> declarationUnitMap = new HashMap<Declaration, PhasedUnit>();
    private Map<Declaration, Node> declarationNodeMap = new HashMap<Declaration, Node>();
    private File tempDestDir;
    private CeylondLogger log;
    private List<String> compiledClasses = new LinkedList<String>();
    private List<File> sourceFolders = Collections.singletonList(new File("source"));
    private boolean haltOnError;
    private List<String> repositories = new LinkedList<String>();
    private String systemRepository;
    private List<String> moduleSpecs = new LinkedList<String>();
    private Module currentModule;
    private List<String> links = new LinkedList<String>();
    private TypeChecker typeChecker;
    private String encoding;

    public CeylonDocTool() {
    }
    
    public CeylonDocTool(List<File> sourceFolders, List<String> repositories, List<String> moduleSpecs,
            boolean haltOnError) {
        this();
        setSourceFolders(sourceFolders);
        setRepositories(repositories);
        setModuleSpecs(moduleSpecs);
        setHaltOnError(haltOnError);
        init();
    }
    
    public List<File> getSourceFolders() {
        return sourceFolders;
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files (default: platform-specific)")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding(){
        return encoding;
    }
    
    @OptionArgument(longName="src", argumentName="dir")
    @Description("A directory containing Ceylon and/or Java source code (default: `./source`)")
    public void setSourceFolders(List<File> sourceFolders) {
        this.sourceFolders = sourceFolders;
    }

    public boolean isHaltOnError() {
        return haltOnError;
    }

    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    @OptionArgument(longName="rep", argumentName="dir-or-url")
    @Description("The URL of a module repository containing dependencies")
    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public String getSystemRepository() {
        return systemRepository;
    }

    @OptionArgument(longName="sysrep", argumentName="dir-or-url")
    @Description("The URL of the system repository containing essential modules")
    public void setSystemRepository(String systemRepository) {
        this.systemRepository = systemRepository;
    }

    public List<String> getModuleSpecs() {
        return moduleSpecs;
    }

    @Argument(argumentName="modules", multiplicity="+")
    public void setModuleSpecs(List<String> moduleSpecs) {
        this.moduleSpecs = moduleSpecs;
    }
    
    public List<String> getLinks() {
        return links;
    }
    
    @OptionArgument(longName="link", argumentName="url")
    @Description("The URL of a module repository containing documentation for external dependencies." +
    		"\n\n" +
    		"Parameter url must be one of supported protocols (http://, https:// or file://). " +
            "Parameter url can be prefixed with module name pattern, separated by a '=' character, determine for which external modules will be use." +
            "\n\n" +
            "Examples:\n" +
            "\n" +
            "    --link https://modules.ceylon-lang.org/\n" +
            "    --link ceylon.math=https://modules.ceylon-lang.org/\n")
    public void setLinks(List<String> links) {
        validateLinks(links);        
        this.links = links;
    }

    private void validateLinks(List<String> links) {
        if( links != null ) {
            for(String link : links) {
                String[] linkParts = LinkRenderer.divideToPatternAndUrl(link);
                String moduleRepoUrl = linkParts[1];

                if (!LinkRenderer.isHttpProtocol(moduleRepoUrl) && !LinkRenderer.isFileProtocol(moduleRepoUrl)) {
                    throw new IllegalArgumentException(CeylondMessages.msg("error.unexpectedLinkProtocol", link));  
                }
            }
        }
    }

    @PostConstruct
    public void init() {
        TypeCheckerBuilder builder = new TypeCheckerBuilder();
        for(File src : sourceFolders){
            builder.addSrcDirectory(src);
        }
        this.log = new CeylondLogger();
        
        // set up the artifact repository
        RepositoryManager repository = CeylonUtils.repoManager()
                .systemRepo(systemRepository)
                .userRepos(repositories)
                .logger(log).buildManager();
        
        builder.setRepositoryManager(repository);
        
        // we need to plug in the module manager which can load from .cars
        final List<ModuleSpec> modules = ModuleSpec.parseEachList(moduleSpecs);
        builder.moduleManagerFactory(new ModuleManagerFactory(){
            @Override
            public ModuleManager createModuleManager(Context context) {
                return new CeylonDocModuleManager(CeylonDocTool.this, context, modules, log);
            }
        });
        
        // only parse what we asked for
        List<String> moduleFilters = new LinkedList<String>();
        for(ModuleSpec spec : modules){
            moduleFilters.add(spec.getName());
        }
        builder.setModuleFilters(moduleFilters);
        
        typeChecker = builder.getTypeChecker();
        // collect all units we are typechecking
        collectTypeCheckedUnits(typeChecker);
        typeChecker.process();
        if(haltOnError && typeChecker.getErrors() > 0)
            throw new RuntimeException(CeylondMessages.msg("error.failedParsing", typeChecker.getErrors()));
        
        this.modules = getModules(modules, typeChecker.getContext().getModules());
        // only for source code mapping
        this.phasedUnits = getPhasedUnits(typeChecker.getPhasedUnits().getPhasedUnits());

        // make a temp dest folder
        try {
            this.tempDestDir = File.createTempFile("ceylond", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempDestDir.delete();
        tempDestDir.mkdirs();
    }

    private void collectTypeCheckedUnits(TypeChecker typeChecker) {
        for(PhasedUnit unit : typeChecker.getPhasedUnits().getPhasedUnits()){
            // obtain the unit container path
            final String pkgName = Util.getUnitPackageName(unit); 
            unit.getCompilationUnit().visit(new SourceDeclarationVisitor(){
                @Override
                public void loadFromSource(com.redhat.ceylon.compiler.typechecker.tree.Tree.Declaration decl) {
                    compiledClasses.add(Util.getQuotedFQN(pkgName, decl));
                }
            });
        }
    }

    private List<Module> getModules(List<ModuleSpec> moduleSpecs, Modules modules){
        // find the required modules
        List<Module> documentedModules = new LinkedList<Module>();
        for(ModuleSpec moduleSpec : moduleSpecs){
            Module foundModule = null;
            for(Module module : modules.getListOfModules()){
                if(module.getNameAsString().equals(moduleSpec.getName())){
                    if(!moduleSpec.isVersioned() || moduleSpec.getVersion().equals(module.getVersion()))
                        foundModule = module;
                }
            }
            if(foundModule != null)
                documentedModules.add(foundModule);
            else if(moduleSpec.isVersioned())
                throw new RuntimeException(CeylondMessages.msg("error.cantFindModule", moduleSpec.getName(), moduleSpec.getVersion()));
            else
                throw new RuntimeException(CeylondMessages.msg("error.cantFindModuleNoVersion", moduleSpec.getName()));
        }
        return documentedModules;
    }
    
    private List<PhasedUnit> getPhasedUnits(List<PhasedUnit> phasedUnits) {
        List<PhasedUnit> documentedPhasedUnit = new LinkedList<PhasedUnit>();
        for(PhasedUnit pu : phasedUnits){
            if(modules.contains(pu.getUnit().getPackage().getModule()))
                documentedPhasedUnit.add(pu);
        }
        return documentedPhasedUnit;
    }

    public void setOutputRepository(String outputRepository, String user, String pass) {
        this.outputRepository = outputRepository;
        this.user = user;
        this.pass = pass;
    }
    
    @OptionArgument(longName="out", argumentName="dir-or-url")
    @Description("The URL of the module repository where output should be published (default: `./out`")
    public void setOutputRepository(String outputRepository) {
        this.outputRepository = outputRepository;
    }
    
    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository.")
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository.")
    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getCompiledClasses(){
        return compiledClasses;
    }
    
    public String getOutputRepository() {
        return outputRepository;
    }

    @Option(longName="non-shared")
    @Description("Includes documentation for package-private declarations.")
    public void setIncludeNonShared(boolean includeNonShared) {
        this.includeNonShared = includeNonShared;
    }

    public boolean isIncludeNonShared() {
        return includeNonShared;
    }

    @Option(longName="source-code")
    @Description("Includes source code in the generated documentation.")
    public void setIncludeSourceCode(boolean includeSourceCode) {
        this.includeSourceCode = includeSourceCode;
    }
    
    public boolean isIncludeSourceCode() {
        return includeSourceCode;
    }

    protected String getFileName(Scope klass) {
        List<String> name = new LinkedList<String>();
        while(klass instanceof Declaration){
            name.add(0, ((Declaration)klass).getName());
            klass = klass.getContainer();
        }
        return join(".", name);
    }

    private File getFolder(Package pkg) {
        Module module = pkg.getModule();
        List<String> unprefixedName;
        if(module.isDefault())
            unprefixedName = pkg.getName();
        else{
            // remove the leading module name part
            unprefixedName = pkg.getName().subList(module.getName().size(), pkg.getName().size());
        }
        File dir = new File(getOutputFolder(module), join("/", unprefixedName));
        if(shouldInclude(module))
            dir.mkdirs();
        return dir;
    }

    public File getOutputFolder(Module module) {
        File folder = new File(com.redhat.ceylon.compiler.java.util.Util.getModulePath(tempDestDir, module),
                "module-doc");
        if(shouldInclude(module))
            folder.mkdirs();
        return folder;
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
                || (obj instanceof Declaration && Decl.isGetter((Declaration)obj))) {
            return "attribute";
        } else if (obj instanceof Method) {
            return "function";
        } else if (obj instanceof Declaration && Decl.isValue((Declaration)obj)) {
            return "value";
        } else if (obj instanceof Package) {
            return "package";
        } else if (obj instanceof Module) {
            return "module";
        }
        throw new RuntimeException(CeylondMessages.msg("error.unexpected", obj));
    }

    File getObjectFile(Object modPgkOrDecl) throws IOException {
        final File file;
        if (modPgkOrDecl instanceof ClassOrInterface) {
            ClassOrInterface klass = (ClassOrInterface)modPgkOrDecl;
            String filename = kind(modPgkOrDecl) + "_" + getFileName(klass) + ".html";
            file = new File(getFolder(klass), filename);
        } else if (modPgkOrDecl instanceof Module) {
            String filename = "index.html";
            file = new File(getOutputFolder((Module)modPgkOrDecl), filename);
        } else if (modPgkOrDecl instanceof Package) {
            String filename = "index.html";
            file = new File(getFolder((Package)modPgkOrDecl), filename);
        } else {
            throw new RuntimeException(CeylondMessages.msg("error.unexpected", modPgkOrDecl));
        }
        return file.getCanonicalFile();
    }

    @Override
    public void run() throws Exception {
        makeDoc();    
    }
    
    public void makeDoc() throws IOException {
        
        buildDeclarationMaps();
        if (includeSourceCode) {
            copySourceFiles();
        }

        collectSubclasses();

        // make a destination repo
        RepositoryManager outputRepository = CeylonUtils.repoManager()
                .outRepo(this.outputRepository)
                .logger(log)
                .user(user)
                .password(pass)
                .buildOutputManager();

        try{
            // document every module
            boolean documentedOne = false;
            for(Module module : modules){
                if(isEmpty(module))
                    log.warning(CeylondMessages.msg("warn.moduleHasNoDeclaration", module.getNameAsString()));
                else
                    documentedOne = true;
                documentModule(module);
                ArtifactContext context = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.DOCS);
                try{
                    outputRepository.removeArtifact(context);
                }catch(CMRException x){
                    throw new CeylondException("error.failedRemoveArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
                }catch(Exception x){
                    // FIXME: remove when the whole CMR is using CMRException
                    throw new CeylondException("error.failedRemoveArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
                }
                try{
                    outputRepository.putArtifact(context, getOutputFolder(module));
                }catch(CMRException x){
                    throw new CeylondException("error.failedWriteArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
                }catch(Exception x){
                    // FIXME: remove when the whole CMR is using CMRException
                    throw new CeylondException("error.failedWriteArtifact", new Object[]{context, x.getLocalizedMessage()}, x);
                }
            }
            if(!documentedOne)
                log.warning(CeylondMessages.msg("warn.couldNotFindAnyDeclaration"));
        }finally{
            Util.delete(tempDestDir);
        }
    }

    private boolean isEmpty(Module module) {
        for(Package pkg : module.getPackages())
            if(!pkg.getMembers().isEmpty())
                return false;
        return true;
    }

    private void documentModule(Module module) throws IOException {
        try {
            currentModule = module;
            
            doc(module);
            makeIndex(module);
            makeSearch(module);
            
            File resourcesDir = getResourcesDir(module);
            copyResource("resources/ceylondoc.css", new File(resourcesDir, "ceylondoc.css"));
            copyResource("resources/ceylondoc.js", new File(resourcesDir, "ceylondoc.js"));
            
            copyResource("resources/bootstrap.min.css", new File(resourcesDir, "bootstrap.min.css"));
            copyResource("resources/bootstrap.min.js", new File(resourcesDir, "bootstrap.min.js"));
            copyResource("resources/jquery-1.8.2.min.js", new File(resourcesDir, "jquery-1.8.2.min.js"));
            
            copyResource("resources/shCore.css", new File(resourcesDir, "shCore.css"));
            copyResource("resources/shThemeDefault.css", new File(resourcesDir, "shThemeDefault.css"));
            copyResource("resources/shCore.js", new File(resourcesDir, "shCore.js"));
            copyResource("resources/shBrushCeylon.js", new File(resourcesDir, "shBrushCeylon.js"));
            
            copyResource("resources/ceylondoc-logo.png", new File(resourcesDir, "ceylondoc-logo.png"));
            copyResource("resources/ceylondoc-icons.png", new File(resourcesDir, "ceylondoc-icons.png"));
            copyResource("resources/NOTICE.txt", new File(getOutputFolder(module), "NOTICE.txt"));
        }
        finally {
            currentModule = null;
        }
    }

    private void collectSubclasses() throws IOException {
        for (Module module : modules) {
            for (Package pkg : module.getPackages()) {
                for (Declaration decl : pkg.getMembers()) {
                    if(!shouldInclude(decl)) {
                        continue;
                    }
                    if (decl instanceof ClassOrInterface) {
                        // FIXME: why this call?
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
        }
    }
    private Writer openWriter(File file) throws IOException {
        return new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); 
    }
    
    private void makeSearch(Module module) throws IOException {
        Writer writer = openWriter(new File(getOutputFolder(module), "search.html"));
        try {
            new Search(module, this, writer).generate();
        } finally {
            writer.close();
        }
    }

    private void buildDeclarationMaps() {
        for (final PhasedUnit pu : phasedUnits) {
            CompilationUnit cu = pu.getCompilationUnit();
            Walker.walkCompilationUnit(new Visitor() {
                public void visit(Tree.Declaration decl) {
                    declarationUnitMap.put(decl.getDeclarationModel(), pu);
                    declarationNodeMap.put(decl.getDeclarationModel(), decl);
                    super.visit(decl);
                }
                public void visit(Tree.MethodDeclaration decl) {
                    declarationUnitMap.put(decl.getDeclarationModel(), pu);
                    declarationNodeMap.put(decl.getDeclarationModel(), decl);
                    super.visit(decl);
                }
                public void visit(Tree.AttributeDeclaration decl) {
                    declarationUnitMap.put(decl.getDeclarationModel(), pu);
                    declarationNodeMap.put(decl.getDeclarationModel(), decl);
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
                throw new IOException(CeylondMessages.msg("error.couldNotCreateDirectory", file));
            }
            Writer writer = openWriter(file);
            try {
            Markup markup = new Markup(writer);
                markup.write("<!DOCTYPE html>");
                markup.open("html xmlns='http://www.w3.org/1999/xhtml'");
                markup.open("head");
                markup.tag("meta charset='UTF-8'");
                markup.around("title", pu.getUnit().getFilename());
                Package decl = pu.getUnit().getPackage();
                markup.tag("link href='" + getResourceUrl(decl, "shCore.css") + "' rel='stylesheet' type='text/css'");
                markup.tag("link href='" + getResourceUrl(decl, "shThemeDefault.css") + "' rel='stylesheet' type='text/css'");
                markup.around("script type='text/javascript' src='"+getResourceUrl(decl, "jquery-1.8.2.min.js")+"'");
                markup.around("script type='text/javascript' src='"+getResourceUrl(decl, "ceylondoc.js")+"'"); 
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
        Writer rootWriter = openWriter(getObjectFile(module));
        try {
            ModuleDoc moduleDoc = new ModuleDoc(this, rootWriter, module);
            moduleDoc.generate();
            for (Package pkg : module.getPackages()) {
                if(pkg.getMembers().isEmpty()){
                    continue;
                }
                // document the package
                if (!isRootPackage(module, pkg)) {
                    Writer packageWriter = openWriter(getObjectFile(pkg));
                    try {
                        new PackageDoc(this, packageWriter, pkg).generate();
                    } finally {
                        packageWriter.close();
                    }
                }
                // document its members
                for (Declaration decl : pkg.getMembers()) {
                    doc(decl);
                }
            }
        } finally {
            rootWriter.close();
        }
        
    }

    private void makeIndex(Module module) throws IOException {
        File dir = getResourcesDir(module);
        Writer writer = openWriter(new File(dir, "index.js"));
        try {
            new IndexDoc(this, writer, module).generate();
        } finally {
            writer.close();
        }
    }

    private File getResourcesDir(Module module) throws IOException {
        File dir = new File(getOutputFolder(module), ".resources");
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
        if(module.isDefault())
            return pkg.getNameAsString().isEmpty();
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

    public void doc(Declaration decl) throws IOException {
        if (decl instanceof ClassOrInterface) {
            if (shouldInclude(decl)) {
                Writer writer = openWriter(getObjectFile(decl));
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
    
    List<Package> getPackages(Module module) {
        List<Package> packages = new ArrayList<Package>();
        for (Package pkg : module.getPackages()) {
            if (pkg.getMembers().size() > 0
                    && shouldInclude(pkg))
                packages.add(pkg);
        }
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package a, Package b) {
                return a.getNameAsString().compareTo(b.getNameAsString());
            }

        });
        return packages;
    }


    protected boolean shouldInclude(Declaration decl){
        return includeNonShared || decl.isShared();
    }
    
    protected boolean shouldInclude(Package pkg){
        return true; // TODO includeNonShared || pkg.isShared();
    }
    
    protected boolean shouldInclude(Module module){
        return modules.contains(module);
    }

    /**
     * Returns the absolute URI of the page for the given thing
     * @param obj (Module, Package, Declaration etc)
     * @throws IOException 
     */
    private URI getAbsoluteObjectUrl(Object obj) throws IOException {
        File f = getObjectFile(obj);
        if (f == null) {
            throw new RuntimeException(CeylondMessages.msg("error.noPage", obj));
        }
        return f.toURI();
    }
    
    /**
     * Gets the base URL
     * @return Gets the base URL
     */
    private URI getBaseUrl(Module module) throws IOException {
        return getOutputFolder(module).getCanonicalFile().toURI();
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
    private URI relativize(Module module, URI uri, URI uri2) throws IOException {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException(CeylondMessages.msg("error.expectedUriToBeAbsolute", uri));
        }
        if (!uri2.isAbsolute()) {
            throw new IllegalArgumentException(CeylondMessages.msg("error.expectedUriToBeAbsolute", uri2));
        }
        URI baseUrl = getBaseUrl(module);
        StringBuilder sb = new StringBuilder();
        URI r = uri;
        if (!r.equals(baseUrl)) {
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
            // FIXME: this throws in some cases even for absolute URIs, not sure why
            //throw new RuntimeException("Result not absolute: "+result);
        }
        if (!uri.resolve(result).equals(uri2)) {
            throw new RuntimeException(CeylondMessages.msg("error.failedUriRelativize", uri, uri2, result));
        }
        return result;
    }
    
    protected String getObjectUrl(Object from, Object to) throws IOException {
        return getObjectUrl(from, to, true);
    }
    
    protected String getObjectUrl(Object from, Object to, boolean withFragment) throws IOException {
        Module module = getModule(from);
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getAbsoluteObjectUrl(to);
        String result = relativize(module, fromUrl, toUrl).toString();
        if (withFragment
                && to instanceof Package 
                && isRootPackage(module, (Package)to)) {
            result += "#section-package";
        }
        return result;
    }
    
    protected String getResourceUrl(Object from, String to) throws IOException {
        Module module = getModule(from);
        URI fromUrl = getAbsoluteObjectUrl(from);
        URI toUrl = getBaseUrl(module).resolve(".resources/" + to);
        String result = relativize(module, fromUrl, toUrl).toString();
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
        Module module = getModule(from);
        String filename;
        File folder;
        if (modPkgOrDecl instanceof Element) {
            Unit unit = ((Element)modPkgOrDecl).getUnit();
            filename = unit.getFilename();
            folder = getFolder(unit.getPackage());
        } else if (modPkgOrDecl instanceof Package) {
            filename = "package.ceylon";
            folder = getFolder((Package)modPkgOrDecl);
        } else if (modPkgOrDecl instanceof Module) {
            Module moduleDecl = (Module)modPkgOrDecl;
            folder = getOutputFolder(moduleDecl);
            filename = "module.ceylon";
        } else {
            throw new RuntimeException(CeylondMessages.msg("error.unexpected", modPkgOrDecl));
        }

        File srcFile = new File(folder, filename + ".html").getCanonicalFile();
        String result;
        if (srcFile.exists()) {
            URI url = srcFile.toURI();
            result = relativize(module, fromUrl, url).toString();
        } else {
            result = null;
        }
        return result;
    }
    
    protected PhasedUnit getDeclarationUnit(Declaration decl) {
        return declarationUnitMap.get(decl);
    }
    
    protected Node getDeclarationNode(Declaration decl) {
        return declarationNodeMap.get(decl);
    }
    
    /**
     * Returns the starting and ending line number of the given declaration
     * @param decl The declaration
     * @return [start, end]
     */
    protected int[] getDeclarationSrcLocation(Declaration decl) {
        Node node = declarationNodeMap.get(decl);
        if (node == null) {
            return null;
        } else {
            return new int[] { node.getToken().getLine(), node.getEndToken().getLine() };
        }
    }
    
    protected Module getCurrentModule() {
        return currentModule;
    }
    
    protected TypeChecker getTypeChecker() {
        return typeChecker;
    }
    
    protected Logger getLogger() {
        return log;
    }
    
}