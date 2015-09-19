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

import java.awt.Desktop;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.ceylondoc.Util.ReferenceableComparatorByName;
import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.common.tools.ModuleWildcardsHelper;
import com.redhat.ceylon.compiler.loader.SourceDeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleSourceMapper;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Walker;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Referenceable;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

@Summary("Generates Ceylon API documentation from Ceylon source files")
@Description("The default module repositories are `modules` and `" +
        Constants.REPO_URL_CEYLON+"`, and the default source directory is `source`. " +
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
        "        --out ~/projects/hibernate/build" +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonDocTool extends OutputRepoUsingTool {

    private static final String OPTION_SECTION = "doctool.";
    private static final String OPTION_HEADER = OPTION_SECTION + "header";
    private static final String OPTION_FOOTER = OPTION_SECTION + "footer";
    private static final String OPTION_NON_SHARED = OPTION_SECTION + "non-shared";
    private static final String OPTION_SOURCE_CODE = OPTION_SECTION + "source-code";
    private static final String OPTION_IGNORE_MISSING_DOC = OPTION_SECTION + "ignore-missing-doc";
    private static final String OPTION_IGNORE_MISSING_THROWS = OPTION_SECTION + "ignore-missing-throws";
    private static final String OPTION_IGNORE_BROKEN_LINK = OPTION_SECTION + "ignore-broken-link";
    private static final String OPTION_LINK = OPTION_SECTION + "link";

    private String encoding;
    private String header;
    private String footer;
    private boolean includeNonShared;
    private boolean includeSourceCode;
    private boolean ignoreMissingDoc;
    private boolean ignoreMissingThrows;
    private boolean ignoreBrokenLink;
    private boolean browse;
    private boolean haltOnError = true;
    private boolean bootstrapCeylon;
    private List<File> sourceFolders = DefaultToolOptions.getCompilerSourceDirs();
    private List<File> docFolders = DefaultToolOptions.getCompilerDocDirs();
    private List<String> moduleSpecs = Arrays.asList("*");
    private List<String> links = new LinkedList<String>();
    
    private TypeChecker typeChecker;
    private Module currentModule;
    private File tempDestDir;
    private final List<PhasedUnit> phasedUnits = new LinkedList<PhasedUnit>();
    private final List<Module> modules = new LinkedList<Module>();
    private final List<String> compiledClasses = new LinkedList<String>();
    private final Map<TypeDeclaration, List<Class>> subclasses = new IdentityHashMap<TypeDeclaration, List<Class>>();
    private final Map<TypeDeclaration, List<ClassOrInterface>> satisfyingClassesOrInterfaces = new IdentityHashMap<TypeDeclaration, List<ClassOrInterface>>();
    private final Map<TypeDeclaration, List<Function>> annotationConstructors = new IdentityHashMap<TypeDeclaration, List<Function>>();
    private final Map<Referenceable, PhasedUnit> modelUnitMap = new IdentityHashMap<Referenceable, PhasedUnit>();
    private final Map<Referenceable, Node> modelNodeMap = new IdentityHashMap<Referenceable, Node>();
    private final Map<Parameter, PhasedUnit> parameterUnitMap = new IdentityHashMap<Parameter, PhasedUnit>();
    private final Map<Parameter, Node> parameterNodeMap = new IdentityHashMap<Parameter, Node>();
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
        
        log = new CeylondLogger();
    }

    @OptionArgument(argumentName="encoding")
    @Description("Sets the encoding used for reading source files (default: platform-specific)")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding(){
        return encoding;
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

    @Hidden
    @Option(longName = "bootstrap-ceylon")
    @Description("Is used when documenting the Ceylon language module.")
    public void setBootstrapCeylon(boolean bootstrapCeylon) {
        this.bootstrapCeylon = bootstrapCeylon;
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

    @Argument(argumentName="modules", multiplicity="*")
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

    @Option(longName="browse")
    @Description("Open module documentation in browser.")
    public void setBrowse(boolean browse) {
        this.browse = browse;
    }

    public List<String> getCompiledClasses() {
        return compiledClasses;
    }

    public String getOut() {
        return out;
    }

    @Override
    protected List<File> getSourceDirs() {
        return sourceFolders;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
        TypeCheckerBuilder builder = new TypeCheckerBuilder();
        for(File src : sourceFolders){
            builder.addSrcDirectory(src);
        }
        
        // set up the artifact repository
        RepositoryManager repository = getRepositoryManager();
        
        builder.setRepositoryManager(repository);
        
        // make a destination repo
        outputRepositoryManager = getOutputRepositoryManager();

        // create the actual list of modules to process
        List<File> srcs = FileUtil.applyCwd(cwd, sourceFolders);
        List<String> expandedModules = ModuleWildcardsHelper.expandWildcards(srcs , moduleSpecs, null);
        final List<ModuleSpec> modules = ModuleSpec.parseEachList(expandedModules);
        
        // we need to plug in the module manager which can load from .cars
        builder.moduleManagerFactory(new ModuleManagerFactory(){
            @Override
            public ModuleManager createModuleManager(Context context) {
                return new CeylonDocModuleManager(CeylonDocTool.this, context, modules, outputRepositoryManager, bootstrapCeylon, log);
            }

            @Override
            public ModuleSourceMapper createModuleManagerUtil(Context context, ModuleManager moduleManager) {
                return new CeylonDocModuleSourceMapper(context, (CeylonDocModuleManager) moduleManager, CeylonDocTool.this);
            }
        });
        
        // only parse what we asked for
        List<String> moduleFilters = new LinkedList<String>();
        for(ModuleSpec spec : modules){
            moduleFilters.add(spec.getName());
        }
        builder.setModuleFilters(moduleFilters);
        String fileEncoding  = getEncoding();
        if (fileEncoding == null) {
            fileEncoding = CeylonConfig.get(DefaultToolOptions.DEFAULTS_ENCODING);
        }
        if (fileEncoding != null) {
            builder.encoding(fileEncoding);
        }
        
        typeChecker = builder.getTypeChecker();
        // collect all units we are typechecking
        initTypeCheckedUnits(typeChecker);
        typeChecker.process();
        if (haltOnError && typeChecker.getErrors() > 0) {
            throw new CeylondException("error.failedParsing", new Object[] { typeChecker.getErrors() }, null);
        }
        
        initModules(modules);
        initPhasedUnits();
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

                @Override
                public void loadFromSource(ModuleDescriptor that) {
                    // don't think we care about these
                }

                @Override
                public void loadFromSource(PackageDescriptor that) {
                    // don't think we care about these
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
        File dir = new File(getApiOutputFolder(module), join("/", unprefixedName));
        if(shouldInclude(module))
            dir.mkdirs();
        return dir;
    }

    private File getFolder(TypeDeclaration type) {
        return getFolder(getPackage(type));
    }

    private File getApiOutputFolder(Module module) {
        return getOutputFolder(module, "api");
    }

    private File getDocOutputFolder(Module module) {
        return getOutputFolder(module, "doc");
    }

    private File getOutputFolder(Module module, String subDir) {
        File folder = new File(com.redhat.ceylon.compiler.java.util.Util.getModulePath(tempDestDir, module), "module-doc");
        if (subDir != null) {
            folder = new File(folder, subDir);
        }
        if (shouldInclude(module)) {
            folder.mkdirs();
        }
        return folder;
    }

    private File getObjectFile(Object modPgkOrDecl) throws IOException {
        final File file;
        if (modPgkOrDecl instanceof TypeDeclaration) {
            TypeDeclaration type = (TypeDeclaration)modPgkOrDecl;
            String filename = getFileName(type);
            file = new File(getFolder(type), filename);
        } else if (modPgkOrDecl instanceof Module) {
            String filename = "index.html";
            file = new File(getApiOutputFolder((Module)modPgkOrDecl), filename);
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
        // make a temp dest folder
        tempDestDir = Files.createTempDirectory("ceylon-doc-").toFile();
        try {
            // create the documentation
            makeDoc();
        } finally {
            FileUtil.deleteQuietly(tempDestDir);
        }
    }
    
    private void makeDoc() throws IOException {
        buildNodesMaps();
        if (includeSourceCode) {
            copySourceFiles();
        }

        collectSubclasses();
        collectAnnotationConstructors();

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
            
            // find all doc folders to copy
            File outputDocFolder = getDocOutputFolder(module);
            for (File docFolder : docFolders) {
                File moduleDocFolder = new File(docFolder, join("/", module.getName()));
                if (moduleDocFolder.exists()) {
                    FileUtil.copyAll(moduleDocFolder, outputDocFolder);
                }
            }

            repositoryRemoveArtifact(outputRepositoryManager, artifactDocs);
            
            repositoryPutArtifact(outputRepositoryManager, artifactDocs, getOutputFolder(module, null));
        }
        if (!documentedOne) {
            log.warning(CeylondMessages.msg("warn.couldNotFindAnyDeclaration"));
        }

        if (browse) {
            for(Module module : modules) {
                if (isEmpty(module)) {
                    continue;
                }
                ArtifactContext docArtifact = new ArtifactContext(module.getNameAsString(), module.getVersion(), ArtifactContext.DOCS);
                File docFolder = outputRepositoryManager.getArtifact(docArtifact);
                File docIndex = new File(docFolder, "api/index.html");
                if (docIndex.isFile()) {
                    try {
                        Desktop.getDesktop().browse(docIndex.toURI());
                    } catch (Exception e) {
                        log.error(CeylondMessages.msg("error.unableBrowseModuleDoc", docIndex.toURI()));
                    }
                }
            }
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
            makeApiIndex(module);
            makeIndex(module);
            makeSearch(module);
            
            File resourcesDir = getResourcesDir(module);
            copyResource("resources/ceylondoc.css", new File(resourcesDir, "ceylondoc.css"));
            copyResource("resources/ceylondoc.js", new File(resourcesDir, "ceylondoc.js"));
            
            copyResource("resources/bootstrap.min.css", new File(resourcesDir, "bootstrap.min.css"));
            copyResource("resources/bootstrap.min.js", new File(resourcesDir, "bootstrap.min.js"));
            copyResource("resources/jquery-1.8.2.min.js", new File(resourcesDir, "jquery-1.8.2.min.js"));
            
            copyResource("resources/ceylon.css", new File(resourcesDir, "ceylon.css"));
            copyResource("resources/rainbow.min.js", new File(resourcesDir, "rainbow.min.js"));
            copyResource("resources/rainbow.linenumbers.js", new File(resourcesDir, "rainbow.linenumbers.js"));
            copyResource("resources/ceylon.js", new File(resourcesDir, "ceylon.js"));
            
            copyResource("resources/favicon.ico", new File(resourcesDir, "favicon.ico"));
            copyResource("resources/ceylondoc-logo.png", new File(resourcesDir, "ceylondoc-logo.png"));
            copyResource("resources/ceylondoc-icons.png", new File(resourcesDir, "ceylondoc-icons.png"));
            copyResource("resources/NOTICE.txt", new File(getApiOutputFolder(module), "NOTICE.txt"));
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
                            Type superclass = c.getExtendedType();                    
                            if (superclass != null) {
                                TypeDeclaration superdec = superclass.getDeclaration();
                                if (subclasses.get(superdec) == null) {
                                    subclasses.put(superdec, new ArrayList<Class>());
                                }
                                subclasses.get(superdec).add((Class) c);
                            }
                        }

                        List<Type> satisfiedTypes = new ArrayList<Type>(c.getSatisfiedTypes());                     
                        if (satisfiedTypes != null && satisfiedTypes.isEmpty() == false) {
                            // satisfying classes or interfaces map
                            for (Type satisfiedType : satisfiedTypes) {
                                TypeDeclaration superdec = satisfiedType.getDeclaration();
                                if (satisfyingClassesOrInterfaces.get(superdec) ==  null) {
                                    satisfyingClassesOrInterfaces.put(superdec, new ArrayList<ClassOrInterface>());
                                }
                                satisfyingClassesOrInterfaces.get(superdec).add(c);
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
                    if (decl instanceof Function && decl.isAnnotation() && shouldInclude(decl)) {
                        Function annotationCtor = (Function) decl;
                        TypeDeclaration annotationType = annotationCtor.getTypeDeclaration();
                        List<Function> annotationConstructorList = annotationConstructors.get(annotationType);
                        if (annotationConstructorList == null) {
                            annotationConstructorList = new ArrayList<Function>();
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
        Writer writer = openWriter(new File(getApiOutputFolder(module), "search.html"));
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
                public void visit(Tree.SpecifierStatement that) {
                    modelUnitMap.put(that.getDeclaration(), pu);
                    modelNodeMap.put(that.getDeclaration(), that);
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
                markup.tag("link href='" + getResourceUrl(pkg, "ceylon.css") + "' rel='stylesheet' type='text/css'");
                markup.tag("link href='" + getResourceUrl(pkg, "ceylondoc.css") + "' rel='stylesheet' type='text/css'");
                markup.tag("link href='http://fonts.googleapis.com/css?family=Inconsolata' rel='stylesheet' type='text/css'");
                
                markup.open("script type='text/javascript'");
                markup.write("var resourceBaseUrl = '" + getResourceUrl(pkg, "") + "'");
                markup.close("script");
                
                markup.around("script src='" + getResourceUrl(pkg, "jquery-1.8.2.min.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "rainbow.min.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "rainbow.linenumbers.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "ceylon.js") + "' type='text/javascript'");
                markup.around("script src='" + getResourceUrl(pkg, "ceylondoc.js") + "' type='text/javascript'"); 
                markup.close("head");
                markup.open("body", "pre data-language='ceylon' style='font-family: Inconsolata, Monaco, Courier, monospace'");
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
                
                if (pkg.getNameAsString().equals(AbstractModelLoader.CEYLON_LANGUAGE)) {
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
    
    private void makeApiIndex(Module module) throws IOException {
        Writer writer = openWriter(new File(getApiOutputFolder(module), "api-index.html"));
        try {
            new IndexApiDoc(this, writer, module).generate();
        } finally {
            writer.close();
        }
    }

    private File getResourcesDir(Module module) throws IOException {
        File dir = new File(getApiOutputFolder(module), ".resources");
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
        try (InputStream resource = getClass().getResourceAsStream(path)) {
            copy(resource, file);
        }
    }

    private void copy(InputStream resource, File file)
            throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int read;
            while ((read = resource.read(buf)) > -1) {
                os.write(buf, 0, read);
            }
            os.flush();
        }
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

    protected boolean shouldInclude(Declaration decl) {
        if (!includeNonShared && !decl.isShared()) {
            return false;
        }
        if (decl.isNativeImplementation()) {
            return false;
        }
        return true;
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
        return getApiOutputFolder(module).getCanonicalFile().toURI();
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
            folder = getApiOutputFolder(moduleDecl);
            filename = Constants.MODULE_DESCRIPTOR;
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
    
    protected List<Function> getAnnotationConstructors(TypeDeclaration klass) {
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

    protected void warningMissingDoc(String name, Referenceable scope) {
        if (!ignoreMissingDoc) {
            log.warning(CeylondMessages.msg("warn.missingDoc", name, getPosition(getNode(scope))));
        }
    }

    protected void warningBrokenLink(String docLinkText, Tree.DocLink docLink, Referenceable scope) {
        if (!ignoreBrokenLink) {
            log.warning(CeylondMessages.msg("warn.brokenLink", docLinkText, getWhere(scope), getPosition(docLink)));
        }
    }
    
    protected void warningSetterDoc(String name, Declaration scope) {
        log.warning(CeylondMessages.msg("warn.setterDoc", name, getPosition(getNode(scope))));
    }

    protected void warningMissingThrows(Declaration d) {
        if (ignoreMissingThrows) {
            return;
        }
        
        final Scope scope = d.getScope();
        final PhasedUnit unit = getUnit(d);
        final Node node = getNode(d);
        if (scope == null || unit == null || unit.getUnit() == null || node == null || !(d instanceof FunctionOrValue)) {
            return;
        }
        
        List<Type> documentedExceptions = new ArrayList<Type>();
        for (Annotation annotation : d.getAnnotations()) {
            if (annotation.getName().equals("throws")) {
                String exceptionName = annotation.getPositionalArguments().get(0);
                Declaration exceptionDecl = scope.getMemberOrParameter(unit.getUnit(), exceptionName, null, false);
                if (exceptionDecl instanceof TypeDeclaration) {
                    documentedExceptions.add(((TypeDeclaration) exceptionDecl).getType());
                }
            }
        }
        
        final List<Type> thrownExceptions = new ArrayList<Type>();
        node.visitChildren(new Visitor() {
            @Override
            public void visit(Tree.Throw that) {
                Expression expression = that.getExpression();
                if (expression != null) {
                    thrownExceptions.add(expression.getTypeModel());
                } else {
                    thrownExceptions.add(unit.getUnit().getExceptionType());
                }
            }
            @Override
            public void visit(Tree.Declaration that) {
                // the end of searching
            }
        });

        for (Type thrownException : thrownExceptions) {
            boolean isDocumented = false;
            for (Type documentedException : documentedExceptions) {
                if (thrownException.isSubtypeOf(documentedException)) {
                    isDocumented = true;
                    break;
                }
            }
            if (!isDocumented) {
                log.warning(CeylondMessages.msg("warn.missingThrows", thrownException.asString(), getWhere(d), getPosition(getNode(d))));
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
        } else if (scope instanceof Function) {
            if (((Function) scope).isToplevel()) {
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
    
    private String getPosition(Node node) {
        if (node != null && 
                node.getToken() != null && 
                node.getUnit() != null && 
                node.getUnit().getFilename() != null) {
            return "(" + node.getUnit().getFilename() + ":" + node.getToken().getLine() + ")";
        }
        return "";
    }

    public ModuleSourceMapper getModuleSourceMapper() {
        return typeChecker.getPhasedUnits().getModuleSourceMapper();
    }
    
}