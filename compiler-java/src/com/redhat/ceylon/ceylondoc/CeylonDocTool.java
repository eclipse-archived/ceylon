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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.ceylondoc.Util.ReferenceableComparatorByName;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.RepoUsingTool;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.ShaSigner;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Referenceable;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Walker;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;

@Summary("Generates Ceylon API documentation from Ceylon source files")
@Description("The default module repositories are `modules` and " +
        Constants.REPO_URL_CEYLON+", and the default source directory is `source`. " +
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
public class CeylonDocTool extends RepoUsingTool {

    private static final String OPTION_SECTION = "doctool.";
    private static final String OPTION_HEADER = OPTION_SECTION + "header";
    private static final String OPTION_FOOTER = OPTION_SECTION + "footer";
    private static final String OPTION_NON_SHARED = OPTION_SECTION + "non-shared";
    private static final String OPTION_SOURCE_CODE = OPTION_SECTION + "source-code";
    private static final String OPTION_IGNORE_MISSING_DOC = OPTION_SECTION + "ignore-missing-doc";
    private static final String OPTION_IGNORE_MISSING_THROWS = OPTION_SECTION + "ignore-missing-throws";
    private static final String OPTION_IGNORE_BROKEN_LINK = OPTION_SECTION + "ignore-broken-link";
    private static final String OPTION_LINK = OPTION_SECTION + "link";

    private String outputRepository;
    private String encoding;
    private String user;
    private String pass;
    private String header;
    private String footer;
    private boolean includeNonShared;
    private boolean includeSourceCode;
    private boolean ignoreMissingDoc;
    private boolean ignoreMissingThrows;
    private boolean ignoreBrokenLink;
    private boolean haltOnError;
    private List<File> sourceFolders = DefaultToolOptions.getCompilerSourceDirs();
    private List<File> docFolders = DefaultToolOptions.getCompilerDocDirs();
    private List<String> moduleSpecs = new LinkedList<String>();
    private List<String> links = new LinkedList<String>();
    
    private TypeChecker typeChecker;
    private Module currentModule;
    private File tempDestDir;
    private final CeylondLogger log = new CeylondLogger();
    private final List<PhasedUnit> phasedUnits = new LinkedList<PhasedUnit>();
    private final List<Module> modules = new LinkedList<Module>();
    private final List<String> compiledClasses = new LinkedList<String>();
    private final Map<TypeDeclaration, List<Class>> subclasses = new HashMap<TypeDeclaration, List<Class>>();
    private final Map<TypeDeclaration, List<ClassOrInterface>> satisfyingClassesOrInterfaces = new HashMap<TypeDeclaration, List<ClassOrInterface>>();
    private final Map<TypeDeclaration, List<Method>> annotationConstructors = new HashMap<TypeDeclaration, List<Method>>();
    private final Map<Referenceable, PhasedUnit> modelUnitMap = new HashMap<Referenceable, PhasedUnit>();
    private final Map<Referenceable, Node> modelNodeMap = new HashMap<Referenceable, Node>();
    private final Map<Parameter, PhasedUnit> parameterUnitMap = new HashMap<Parameter, PhasedUnit>();
    private final Map<Parameter, Node> parameterNodeMap = new HashMap<Parameter, Node>();
    private final Map<String, Boolean> moduleUrlAvailabilityCache = new HashMap<String, Boolean>();
    private RepositoryManager outputRepositoryManager;

    public CeylonDocTool() {
        super(CeylondMessages.RESOURCE_BUNDLE);

        CeylonConfig config = CeylonConfig.get();
        
        header = config.getOption(OPTION_HEADER);
        footer = config.getOption(OPTION_FOOTER);
        includeNonShared = config.getBoolOption(OPTION_NON_SHARED, false);
        includeSourceCode = config.getBoolOption(OPTION_SOURCE_CODE, false);
        ignoreMissingDoc = config.getBoolOption(OPTION_IGNORE_MISSING_DOC, false);
        ignoreMissingThrows = config.getBoolOption(OPTION_IGNORE_MISSING_THROWS, false);
        ignoreBrokenLink = config.getBoolOption(OPTION_IGNORE_BROKEN_LINK, false);

        String[] linkValues = config.getOptionValues(OPTION_LINK);
        if (linkValues != null) {
            setLinks(Arrays.asList(linkValues));
        }
    }

    @OptionArgument(longName="out", argumentName="dir-or-url")
    @Description("The URL of the module repository where output should be published (default: `./out`)")
    public void setOutputRepository(String outputRepository) {
        this.outputRepository = outputRepository;
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files (default: platform-specific)")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding(){
        return encoding;
    }
    
    @OptionArgument(argumentName="name")
    @Description("Sets the user name for use with an authenticated output repository.")
    public void setUser(String user) {
        this.user = user;
    }

    @OptionArgument(argumentName="secret")
    @Description("Sets the password for use with an authenticated output repository.")
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    @OptionArgument(argumentName="header")
    @Description("Sets the header text to be placed at the top of each page.")
    public void setHeader(String header) {
        this.header = header;
    }
    
    public String getHeader() {
        return header;
    }
    
    @OptionArgument(argumentName="footer")
    @Description("Sets the footer text to be placed at the bottom of each page.")
    public void setFooter(String footer) {
        this.footer = footer;
    }
    
    public String getFooter() {
        return footer;
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

    @Option(longName = "ignore-missing-doc")
    @Description("Do not print warnings about missing documentation.")
    public void setIgnoreMissingDoc(boolean ignoreMissingDoc) {
        this.ignoreMissingDoc = ignoreMissingDoc;
    }

    @Option(longName = "ignore-missing-throws")
    @Description("Do not print warnings about missing throws annotation.")
    public void setIgnoreMissingThrows(boolean ignoreMissingThrows) {
        this.ignoreMissingThrows = ignoreMissingThrows;
    }

    @Option(longName = "ignore-broken-link")
    @Description("Do not print warnings about broken links.")
    public void setIgnoreBrokenLink(boolean ignoreBrokenLink) {
        this.ignoreBrokenLink = ignoreBrokenLink;
    }

    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    @OptionArgument(longName="source", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("An alias for `--src` (default: `./source`)")
    public void setSource(List<File> source) {
        setSourceFolders(source);
    }

    @OptionArgument(longName="src", argumentName="dir")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing Ceylon and/or Java source code (default: `./source`)")
    public void setSourceFolders(List<File> sourceFolders) {
        this.sourceFolders = sourceFolders;
    }
    
    @OptionArgument(longName="doc", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing your module documentation (default: `./doc`)")
    public void setDocFolders(List<File> docFolders) {
        this.docFolders = docFolders;
    }

    @Argument(argumentName="modules", multiplicity="+")
    public void setModuleSpecs(List<String> moduleSpecs) {
        this.moduleSpecs = moduleSpecs;
    }
    
    public List<String> getLinks() {
        return links;
    }
    
    @OptionArgument(longName="link", argumentName="dir-or-url")
    @Description("The URL or path of a module repository containing " +
            "documentation for external dependencies." +
    		"\n\n" +
    		"The URL must use one of the supported protocols " +
    		"(http://, https:// or file://) or be a path to a directory. " +
            "The argument can start with a module name prefix, " +
            "separated from the URL by a `=` character, so that only " +
            "those external modules " +
            "whose name begins with the prefix will be linked using that URL.\n" +
            "Can be specified multiple times." +
            "\n\n" +
            "Examples:\n" +
            "\n" +
            "    --link "+Constants.REPO_URL_CEYLON+"\n" +
            "    --link ceylon.math="+Constants.REPO_URL_CEYLON+"\n"+
            "    --link com.example=http://example.com/ceylondoc/")
    public void setLinks(List<String> linkArgs) {
        this.links = new ArrayList<String>();
        if( linkArgs != null ) {
            for(String link : linkArgs) {
                links.add(validateLink(link));
            }
        }
    }

    private String validateLink(String link) {
        String[] linkParts = LinkRenderer.divideToPatternAndUrl(link);
        String moduleNamePattern = linkParts[0];
        String moduleRepoUrl = linkParts[1];

        if (!LinkRenderer.isHttpProtocol(moduleRepoUrl) && !LinkRenderer.isFileProtocol(moduleRepoUrl)) {
            File moduleRepoFile = new File(moduleRepoUrl);
            if (moduleRepoFile.exists() && moduleRepoFile.isDirectory()) {
                moduleRepoUrl = moduleRepoFile.toURI().toString();
            } else if (moduleNamePattern == null) {
                throw new IllegalArgumentException(CeylondMessages.msg("error.unexpectedLink", link));
            }
        }

        return moduleNamePattern != null ? (moduleNamePattern + "=" + moduleRepoUrl) : moduleRepoUrl;
    }

    @Option(longName="offline")
    @Description("Enables offline mode that will prevent the module loader from connecting to remote repositories.")
    public void setOffline(boolean offline) {
        this.offline = offline;
    }
    
    public List<String> getCompiledClasses() {
        return compiledClasses;
    }

    public String getOutputRepository() {
        return outputRepository;
    }

    @Override
    public void initialize() {
        setSystemProperties();
        TypeCheckerBuilder builder = new TypeCheckerBuilder();
        for(File src : sourceFolders){
            builder.addSrcDirectory(src);
        }
        
        // set up the artifact repository
        RepositoryManager repository = createRepositoryManagerBuilder()
                .logger(log)
                .buildManager();
        
        builder.setRepositoryManager(repository);
        
        // make a destination repo
        outputRepositoryManager = createRepositoryManagerBuilder()
                .outRepo(this.outputRepository)
                .logger(log)
                .user(user)
                .password(pass)
                .buildOutputManager();

        // we need to plug in the module manager which can load from .cars
        final List<ModuleSpec> modules = ModuleSpec.parseEachList(moduleSpecs);
        builder.moduleManagerFactory(new ModuleManagerFactory(){
            @Override
            public ModuleManager createModuleManager(Context context) {
                return new CeylonDocModuleManager(CeylonDocTool.this, context, modules, outputRepositoryManager, log);
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
        initTypeCheckedUnits(typeChecker);
        typeChecker.process();
        if(haltOnError && typeChecker.getErrors() > 0)
            throw new RuntimeException(CeylondMessages.msg("error.failedParsing", typeChecker.getErrors()));
        
        initModules(modules);
        initPhasedUnits();

        // make a temp dest folder
        try {
            this.tempDestDir = File.createTempFile("ceylond", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempDestDir.delete();
        tempDestDir.mkdirs();
    }

    private void initTypeCheckedUnits(TypeChecker typeChecker) {
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

    private void initModules(List<ModuleSpec> moduleSpecs) {
        for (ModuleSpec moduleSpec : moduleSpecs) {
            Module foundModule = null;
            for (Module module : typeChecker.getContext().getModules().getListOfModules()) {
                if (module.getNameAsString().equals(moduleSpec.getName())) {
                    if (!moduleSpec.isVersioned() || moduleSpec.getVersion().equals(module.getVersion()))
                        foundModule = module;
                }
            }
            if (foundModule != null) {
                modules.add(foundModule);
            }
            else if (moduleSpec.isVersioned()) {
                throw new RuntimeException(CeylondMessages.msg("error.cantFindModule", moduleSpec.getName(),
                        moduleSpec.getVersion()));
            }
            else {
                throw new RuntimeException(CeylondMessages.msg("error.cantFindModuleNoVersion", moduleSpec.getName()));
            }
        }
    }
    
    private void initPhasedUnits() {
        for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
            if (modules.contains(pu.getUnit().getPackage().getModule())) {
                phasedUnits.add(pu);
            }
        }
    }
    
    public String getFileName(TypeDeclaration type) {
        // we need postfix, because objects can have same file name like classes/interfaces in not case-sensitive file systems
        String postfix;
        if (type instanceof Class && type.isAnonymous()) {
            postfix = ".object";
        } else {
            postfix = ".type";
        }

        List<String> names = new LinkedList<String>();
        Scope scope = type;
        while (scope instanceof TypeDeclaration) {
            names.add(0, ((TypeDeclaration) scope).getName());
            scope = scope.getContainer();
        }

        return join(".", names) + postfix + ".html";
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

    private File getOutputFolder(Module module) {
        File folder = new File(com.redhat.ceylon.compiler.java.util.Util.getModulePath(tempDestDir, module), "module-doc");
        if (shouldInclude(module)) {
            folder.mkdirs();
        }
        return folder;
    }

    private File getFolder(TypeDeclaration type) {
        return getFolder(getPackage(type));
    }

    private File getObjectFile(Object modPgkOrDecl) throws IOException {
        final File file;
        if (modPgkOrDecl instanceof TypeDeclaration) {
            TypeDeclaration type = (TypeDeclaration)modPgkOrDecl;
            String filename = getFileName(type);
            file = new File(getFolder(type), filename);
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
    
    private void makeDoc() throws IOException {
        buildNodesMaps();
        if (includeSourceCode) {
            copySourceFiles();
        }

        collectSubclasses();
        collectAnnotationConstructors();

        try{
            // document every module
            boolean documentedOne = false;
            for(Module module : modules){
                if (isEmpty(module)) {
                    log.warning(CeylondMessages.msg("warn.moduleHasNoDeclaration", module.getNameAsString()));
                } else {
                    documentedOne = true;
                }
                    
                documentModule(module);
                
                ArtifactContext artifactDocs = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.DOCS);
                ArtifactContext artifactDocsZip = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.DOCS_ZIPPED);
                
                File outputFolder = getOutputFolder(module);
                
                // find all doc folders to zip
                List<File> existingDocFolders = new ArrayList<File>(docFolders.size());
                for(File docFolder : docFolders){
                    File moduleDocFolder = new File(docFolder, join("/", module.getName()));
                    if(moduleDocFolder.exists())
                        existingDocFolders.add(moduleDocFolder);
                }

                // make the doc zip roots
                IOUtils.ZipRoot[] roots = new IOUtils.ZipRoot[1+existingDocFolders.size()];
                roots[0] = new IOUtils.ZipRoot(outputFolder, "api");
                int d=1;
                for(File docFolder : existingDocFolders){
                    roots[d] = new IOUtils.ZipRoot(docFolder, "doc");
                }
                File docZipFile = IOUtils.zipFolders(roots);
                File docZipSha1File = ShaSigner.sign(docZipFile, log, verbose != null);
                
                repositoryRemoveArtifact(outputRepositoryManager, artifactDocs);
                repositoryRemoveArtifact(outputRepositoryManager, artifactDocsZip);
                repositoryRemoveArtifact(outputRepositoryManager, artifactDocsZip.getSha1Context());
                
                repositoryPutArtifact(outputRepositoryManager, artifactDocs, outputFolder);
                repositoryPutArtifact(outputRepositoryManager, artifactDocsZip, docZipFile);
                repositoryPutArtifact(outputRepositoryManager, artifactDocsZip.getSha1Context(), docZipSha1File);
                
                docZipFile.delete();
                docZipSha1File.delete();
            }
            if (!documentedOne) {
                log.warning(CeylondMessages.msg("warn.couldNotFindAnyDeclaration"));
            }
        } finally {
            Util.delete(tempDestDir);
        }
    }

    private void repositoryRemoveArtifact(RepositoryManager outputRepository, ArtifactContext artifactContext) {
        try {
            outputRepository.removeArtifact(artifactContext);
        } catch (Exception e) {
            throw new CeylondException("error.failedRemoveArtifact", new Object[] { artifactContext, e.getLocalizedMessage() }, e);
        }
    }

    private void repositoryPutArtifact(RepositoryManager outputRepository, ArtifactContext artifactContext, File content) {
        try {
            outputRepository.putArtifact(artifactContext, content);
        } catch (Exception e) {
            throw new CeylondException("error.failedWriteArtifact", new Object[] { artifactContext, e.getLocalizedMessage() }, e);
        }
    }
    
    private boolean isEmpty(Module module) {
        for(Package pkg : getPackages(module))
            if(!pkg.getMembers().isEmpty())
                return false;
        return true;
    }

    private void documentModule(Module module) throws IOException {
        try {
            currentModule = module;
            clearModuleUrlAvailabilityCache();
            
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
            copyResource("resources/shAutoloader.js", new File(resourcesDir, "shAutoloader.js"));
            copyResource("resources/shBrushCeylon.js", new File(resourcesDir, "shBrushCeylon.js"));
            copyResource("resources/shBrushCss.js", new File(resourcesDir, "shBrushCss.js"));
            copyResource("resources/shBrushJava.js", new File(resourcesDir, "shBrushJava.js"));
            copyResource("resources/shBrushJScript.js", new File(resourcesDir, "shBrushJScript.js"));
            copyResource("resources/shBrushPlain.js", new File(resourcesDir, "shBrushPlain.js"));
            copyResource("resources/shBrushXml.js", new File(resourcesDir, "shBrushXml.js"));
            
            copyResource("resources/favicon.ico", new File(resourcesDir, "favicon.ico"));
            copyResource("resources/ceylondoc-logo.png", new File(resourcesDir, "ceylondoc-logo.png"));
            copyResource("resources/ceylondoc-icons.png", new File(resourcesDir, "ceylondoc-icons.png"));
            copyResource("resources/NOTICE.txt", new File(getOutputFolder(module), "NOTICE.txt"));
        }
        finally {
            currentModule = null;
        }
    }
    
    private void clearModuleUrlAvailabilityCache() {
        String[] moduleUrls = moduleUrlAvailabilityCache.keySet().toArray(new String[] {});
        for (String moduleUrl : moduleUrls) {
            if (LinkRenderer.isFileProtocol(moduleUrl)) {
                moduleUrlAvailabilityCache.remove(moduleUrl);
            }
        }
    }

    private void collectSubclasses() throws IOException {
        for (Module module : modules) {
            for (Package pkg : getPackages(module)) {
                for (Declaration decl : pkg.getMembers()) {
                    if(!shouldInclude(decl)) {
                        continue;
                    }
                    if (decl instanceof ClassOrInterface) {
                        ClassOrInterface c = (ClassOrInterface) decl;                    
                        // subclasses map
                        if (c instanceof Class) {
                            ClassOrInterface superclass = c.getExtendedTypeDeclaration();                    
                            if (superclass != null) {
                                if (subclasses.get(superclass) ==  null) {
                                    subclasses.put(superclass, new ArrayList<Class>());
                                }
                                subclasses.get(superclass).add((Class) c);
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
    
    private void collectAnnotationConstructors() {
        for (Module module : modules) {
            for (Package pkg : getPackages(module)) {
                for (Declaration decl : pkg.getMembers()) {
                    if (decl instanceof Method && decl.isAnnotation() && shouldInclude(decl)) {
                        Method annotationCtor = (Method) decl;
                        TypeDeclaration annotationType = annotationCtor.getTypeDeclaration();
                        List<Method> annotationConstructorList = annotationConstructors.get(annotationType);
                        if (annotationConstructorList == null) {
                            annotationConstructorList = new ArrayList<Method>();
                            annotationConstructors.put(annotationType, annotationConstructorList);
                        }
                        annotationConstructorList.add(annotationCtor);
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

    private void buildNodesMaps() {
        for (final PhasedUnit pu : phasedUnits) {
            CompilationUnit cu = pu.getCompilationUnit();
            Walker.walkCompilationUnit(new Visitor() {
                public void visit(Tree.Declaration that) {
                    modelUnitMap.put(that.getDeclarationModel(), pu);
                    modelNodeMap.put(that.getDeclarationModel(), that);
                    super.visit(that);
                }
                public void visit(Tree.ObjectDefinition that) {
                    if( that.getDeclarationModel() != null && that.getDeclarationModel().getTypeDeclaration() != null ) {
                        TypeDeclaration typeDecl = that.getDeclarationModel().getTypeDeclaration();
                        modelUnitMap.put(typeDecl, pu);
                        modelNodeMap.put(typeDecl, that);
                    }
                    super.visit(that);
                }
                public void visit(Tree.PackageDescriptor that) {
                    if (that.getImportPath() != null && that.getImportPath().getModel() != null) {
                        Referenceable model = that.getImportPath().getModel();
                        modelUnitMap.put(model, pu);
                        modelNodeMap.put(model, that);
                    }
                    super.visit(that);
                }
                public void visit(Tree.ModuleDescriptor that) {
                    if (that.getImportPath() != null && that.getImportPath().getModel() != null) {
                        Referenceable model = that.getImportPath().getModel();
                        modelUnitMap.put(model, pu);
                        modelNodeMap.put(model, that);
                    }
                    super.visit(that);
                }
                public void visit(Tree.Parameter param) {
                    parameterUnitMap.put(param.getParameterModel(), pu);
                    parameterNodeMap.put(param.getParameterModel(), param);
                    super.visit(param);
                }
            }, cu);
        }
    }

    private void copySourceFiles() throws FileNotFoundException, IOException {
        for (PhasedUnit pu : phasedUnits) {
            Package pkg = pu.getUnit().getPackage();
            if (!shouldInclude(pkg)) {
                continue;
            }

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
                markup.tag("link href='" + getResourceUrl(pkg, "favicon.ico") + "' rel='shortcut icon'");
                markup.tag("link href='" + getResourceUrl(pkg, "shCore.css") + "' rel='stylesheet' type='text/css'");
                markup.tag("link href='" + getResourceUrl(pkg, "shThemeDefault.css") + "' rel='stylesheet' type='text/css'");
                
                markup.open("script type='text/javascript'");
                markup.write("var resourceBaseUrl = '" + getResourceUrl(pkg, "") + "'");
                markup.close("script");
                
                markup.around("script src='" + getResourceUrl(pkg, "jquery-1.8.2.min.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "shCore.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "shAutoloader.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "shBrushCeylon.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "ceylondoc.js") + "' type='text/javascript'"); 
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
            for (Package pkg : getPackages(module)) {
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
                
                if (pkg.getNameAsString().equals("ceylon.language")) {
                    docNothingType(pkg);
                }
            }
        } finally {
            rootWriter.close();
        }
        
    }

    private void docNothingType(Package pkg) throws IOException {
        final Annotation nothingDoc = new Annotation();
        nothingDoc.setName("doc");
        nothingDoc.addPositionalArgment(
                "The special type _Nothing_ represents: \n" +
                " - the intersection of all types, or, equivalently \n" +
                " - the empty set \n" +
                "\n" +
                "_Nothing_ is assignable to all other types, but has no instances. \n" +
                "A reference to a member of an expression of type _Nothing_ is always an error, since there can never be a receiving instance. \n" +
                "_Nothing_ is considered to belong to the module _ceylon.language_. However, it cannot be defined within the language. \n" +
                "\n" +
                "Because of the restrictions imposed by Ceylon's mixin inheritance model: \n" +
                "- If X and Y are classes, and X is not a subclass of Y, and Y is not a subclass of X, then the intersection type X&Y is equivalent to _Nothing_. \n" +
                "- If X is an interface, the intersection type X&Nothing is equivalent to _Nothing_. \n" +
                "- If X&lt;T&gt; is invariant in its type parameter T, and the distinct types A and B do not involve type parameters, then X&lt;A&gt;&X&lt;B&gt; is equivalent to _Nothing_. \n");
        
        NothingType nothingType = new NothingType(pkg.getUnit()) {
            @Override
            public List<Annotation> getAnnotations() {
                return Collections.singletonList(nothingDoc);
            }
        };
        
        doc(nothingType);
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
    protected boolean isRootPackage(Module module, Package pkg) {
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
        if (decl instanceof TypeDeclaration) {
            if (shouldInclude(decl)) {
                Writer writer = openWriter(getObjectFile(decl));
                try {
                    new ClassDoc(this, writer, (TypeDeclaration) decl).generate();
                } finally {
                    writer.close();
                }
            }
        }
    }

    protected Package getPackage(Declaration decl) {
        Scope scope = decl.getContainer();
        while (!(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        return (Package)scope;
    }

    protected Module getModule(Object modPkgOrDecl) {
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
            if (shouldInclude(pkg)) {
                packages.add(pkg);
            }
        }
        Collections.sort(packages, ReferenceableComparatorByName.INSTANCE);
        return packages;
    }

    protected boolean shouldInclude(Declaration decl){
        return includeNonShared || decl.isShared();
    }
    
    protected boolean shouldInclude(Package pkg) {
        return (includeNonShared || pkg.isShared()) && pkg.getMembers().size() > 0;
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
    
    protected PhasedUnit getUnit(Referenceable referenceable) {
        return modelUnitMap.get(referenceable);
    }
    
    protected Node getNode(Referenceable referenceable) {
        return modelNodeMap.get(referenceable);
    }
    
    protected PhasedUnit getParameterUnit(Parameter parameter) {
        return parameterUnitMap.get(parameter);
    }
    
    protected Node getParameterNode(Parameter parameter) {
        return parameterNodeMap.get(parameter);
    }
    
    protected List<Method> getAnnotationConstructors(TypeDeclaration klass) {
        return annotationConstructors.get(klass);
    }

    protected List<ClassOrInterface> getSatisfyingClassesOrInterfaces(TypeDeclaration klass) {
        return satisfyingClassesOrInterfaces.get(klass);
    }
    
    protected List<Class> getSubclasses(TypeDeclaration klass) {
        return subclasses.get(klass);
    }
    
    protected Map<String, Boolean> getModuleUrlAvailabilityCache() {
        return moduleUrlAvailabilityCache;
    }
    
    /**
     * Returns the starting and ending line number of the given declaration
     * @param decl The declaration
     * @return [start, end]
     */
    protected int[] getDeclarationSrcLocation(Declaration decl) {
        Node node = modelNodeMap.get(decl);
        if (node == null) {
            return null;
        } else {
            return new int[] { node.getToken().getLine(), node.getEndToken().getLine() };
        }
    }
    
    protected Module getCurrentModule() {
        return currentModule;
    }
    
    public List<Module> getDocumentedModules(){
        return modules;
    }

    protected TypeChecker getTypeChecker() {
        return typeChecker;
    }
    
    protected Logger getLogger() {
        return log;
    }

    protected void warningMissingDoc(String name) {
        if (!ignoreMissingDoc) {
            log.warning(CeylondMessages.msg("warn.missingDoc", name));
        }
    }
    
    protected void warningBrokenLink(String link, Referenceable scope) {
        if (!ignoreBrokenLink) {
            log.warning(CeylondMessages.msg("warn.brokenLink", link, getWhere(scope)));
        }
    }
    
    protected void warningSetterDoc(String name) {
        log.warning(CeylondMessages.msg("warn.setterDoc", name));
    }

    protected void warningMissingThrows(Declaration d) {
        if (ignoreMissingThrows) {
            return;
        }
        
        final Scope scope = d.getScope();
        final PhasedUnit unit = getUnit(d);
        final Node node = getNode(d);
        if (scope == null || unit == null || unit.getUnit() == null || node == null || !(d instanceof MethodOrValue)) {
            return;
        }
        
        List<ProducedType> documentedExceptions = new ArrayList<ProducedType>();
        for (Annotation annotation : d.getAnnotations()) {
            if (annotation.getName().equals("throws")) {
                String exceptionName = annotation.getPositionalArguments().get(0);
                Declaration exceptionDecl = scope.getMemberOrParameter(unit.getUnit(), exceptionName, null, false);
                if (exceptionDecl instanceof TypeDeclaration) {
                    documentedExceptions.add(((TypeDeclaration) exceptionDecl).getType());
                }
            }
        }
        
        final List<ProducedType> thrownExceptions = new ArrayList<ProducedType>();
        node.visitChildren(new Visitor() {
            @Override
            public void visit(Tree.Throw that) {
                Expression expression = that.getExpression();
                if (expression != null) {
                    thrownExceptions.add(expression.getTypeModel());
                } else {
                    thrownExceptions.add(unit.getUnit().getExceptionDeclaration().getType());
                }
            }
            @Override
            public void visit(Tree.Declaration that) {
                // the end of searching
            }
        });

        for (ProducedType thrownException : thrownExceptions) {
            boolean isDocumented = false;
            for (ProducedType documentedException : documentedExceptions) {
                if (thrownException.isSubtypeOf(documentedException)) {
                    isDocumented = true;
                    break;
                }
            }
            if (!isDocumented) {
                log.warning(CeylondMessages.msg("warn.missingThrows", thrownException.getProducedTypeName(), getWhere(d)));
            }
        }
    }
    
    private String getWhere(Referenceable scope) {
        String where = "";
        if (scope instanceof Module) {
            where += "module ";
        } else if (scope instanceof Package) {
            where += "package ";
        } else if (scope instanceof Class) {
            where += "class ";
        } else if (scope instanceof Interface) {
            where += "interface ";
        } else if (scope instanceof TypeAlias) {
            where += "type alias ";
        } else if (scope instanceof TypeParameter) {
            where += "type parameter ";
        } else if (scope instanceof Method) {
            if (((Method) scope).isToplevel()) {
                where += "function ";
            } else {
                where += "method ";
            }
        } else if (scope instanceof Value) {
            if (((Value) scope).isToplevel()) {
                where += "value ";
            } else if (((Value) scope).isParameter()) {
                where += "parameter ";
            } else {
                where += "attribute ";
            }
        }
        if( scope instanceof Declaration ) {
            where += ((Declaration) scope).getQualifiedNameString();
        } else {
            where += scope.getNameAsString();            
        }
        return where;
    }
    
}