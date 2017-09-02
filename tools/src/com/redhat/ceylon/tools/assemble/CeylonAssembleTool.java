package com.redhat.ceylon.tools.assemble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.cmr.api.ArtifactContext;
import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.impl.IOUtils;
import com.redhat.ceylon.cmr.impl.MavenRepository;
import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.common.Constants;
import com.redhat.ceylon.common.FileUtil;
import com.redhat.ceylon.common.JVMModuleUtil;
import com.redhat.ceylon.common.ModuleSpec;
import com.redhat.ceylon.common.ModuleUtil;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

/**
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
@Summary("Generate a Ceylon assembly for a given module")
@Description("Generate an executable _assembly_ which contains the given module and all its run-time"
        + " dependencies, including the Ceylon run-time, which makes that jar self-sufficient and"
        + " executable by `java` as if the Ceylon module was run by `ceylon run`."
)
public class CeylonAssembleTool extends ModuleLoadingTool {
    private List<ModuleSpec> modules;
    private boolean force;
	private File out;
    private final List<String> excludedModules = new ArrayList<>();
    /** The (Ceylon) name of the functional to run, e.g. {@code foo.bar::baz} */
    private String run;
    private boolean includeLanguage;
    private boolean includeRuntime;
    private Boolean jvm;
    private Boolean js;
    private Boolean dart;
    
    private String[] loaderSuffixes;
    private String[] assemblySuffixes;
    private ModuleQuery.Type mqt;
    
    public static final String CEYLON_ASSEMBLY_SUFFIX = ".cas";

    @Argument(order = 1, argumentName="module", multiplicity="+")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules));
    }

    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @OptionArgument(longName = "run", argumentName = "toplevel")
    @Description("Specifies the fully qualified name of a toplevel method or class with no parameters. " +
            "The format is: `qualified.package.name::classOrMethodName` with `::` acting as separator " +
            "between the package name and the toplevel class or method name (defaults to `{module}::run`).")
    public void setRun(String run) {
        this.run = run;
    }

    @Description("Target assembly file (defaults to `{name}-{version}.jar`).")
    @OptionArgument(shortName = 'o', argumentName="file")
    public void setOut(File out) {
        this.out = out;
    }

    @OptionArgument(argumentName="moduleOrFile", shortName='x')
    @Description("Excludes modules from the resulting far jat. Can be a module name or " + 
            "a file containing module names. Can be specified multiple times. Note that "+
            "this excludes the module from the resulting assembly, but if your modules require that "+
            "module to be present at runtime it will still be required and may cause your "+
            "application to fail to start if it is not provided at runtime.")
    public void setExcludeModule(List<String> exclusions) {
        for (String each : exclusions) {
            File xFile = new File(each);
            if (xFile.exists() && xFile.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(xFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        this.excludedModules.add(line);
                    }
                } catch (IOException e) {
                    throw new ToolUsageError(CeylonAssembleMessages.msg("exclude.file.failure", each), 
                            e);
                }
            } else {
                this.excludedModules.add(each);
            }
        }
    }

    @Option(longName="force")
    @Description("Ignore errors about conflicting modules.")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Option
    @Description("Include the language module and its dependencies in the assembly."
            + " to ensures that the assembly can be run stand-alone using Java without"
            + " having Ceylon installed. This option does NOT support module isolation!")
    public void setIncludeLanguage(boolean includeLanguage) {
        this.includeLanguage = includeLanguage;
    }

    @Option
    @Description("Include enough of the Ceylon runtime in the assembly to ensure"
            + " that the assembly can be run stand-alone using Java without having"
            + " Ceylon installed. This option supports full module isolation.")
    public void setIncludeRuntime(boolean includeRuntime) {
        this.includeRuntime = includeRuntime;
    }

    @Option
    @Description("Include artifacts compiled for the JVM (`.car` and `.jar`) (default: `true`)")
    public void setJvm(boolean jvm) {
        this.jvm = jvm;
    }

    @Option
    @Description("Include artifacts compiled for JavaScript (`.js` and `-model.js`) (default: `false`)")
    public void setJs(boolean js) {
        this.js = js;
    }

    @Option
    @Description("Include artifacts compiled for Dart (`.dart` and `-dartmodel.json`) (default: `true`)")
    public void setDart(boolean dart) {
        this.dart = dart;
    }

    @Override
    protected String[] getLoaderSuffixes() {
        return loaderSuffixes;
    }
    
    @Override
    public void initialize(CeylonTool mainTool) throws Exception {
        // Determine the artifacts we'll include in the assembly
        boolean defaults = js == null 
                && jvm == null
                && dart == null;
        mqt = ModuleQuery.Type.JVM;
        if (BooleanUtil.isTrue(dart)) {
            mqt = ModuleQuery.Type.DART;
        }
        if (BooleanUtil.isTrue(js)) {
            mqt = ModuleQuery.Type.JS;
        }
        if (BooleanUtil.isTrue(jvm) || defaults) {
            mqt = ModuleQuery.Type.JVM;
        }
        loaderSuffixes = listSuffixes(true);
        assemblySuffixes = listSuffixes(false);
        
        super.initialize(mainTool);
    }

    private String[] listSuffixes(boolean codeOnly) {
        ArrayList<String> sfx = new ArrayList<String>();
        boolean defaults = js == null 
                && jvm == null
                && dart == null;
        if (BooleanUtil.isTrue(jvm) || defaults) {
            // put the CAR first since its presence will shortcut the others
            sfx.add(ArtifactContext.CAR);
            sfx.add(ArtifactContext.JAR);
            if (!codeOnly) {
                sfx.add(ArtifactContext.MODULE_PROPERTIES);
                sfx.add(ArtifactContext.MODULE_XML);
            }
        }
        if (BooleanUtil.isTrue(js) || defaults) {
            sfx.add(ArtifactContext.JS);
            sfx.add(ArtifactContext.JS_MODEL);
            if (!codeOnly) {
                sfx.add(ArtifactContext.RESOURCES);
            }
        }
        if (BooleanUtil.isTrue(dart) || defaults) {
            sfx.add(ArtifactContext.DART);
            sfx.add(ArtifactContext.DART_MODEL);
            if (!codeOnly) {
                sfx.add(ArtifactContext.RESOURCES);
            }
        }
        return sfx.toArray(new String[] {});
    }
    
    @Override
    public void run() throws Exception {
        if (includeRuntime) {
            // includeRuntime implies includeLanguage
            includeLanguage = true;
        }
        
        String firstModuleName = null, firstModuleVersion = null;
        for (ModuleSpec module : modules) {
            String moduleName = module.getName();
            String version = checkModuleVersionsOrShowSuggestions(
                    moduleName,
                    module.isVersioned() ? module.getVersion() : null,
                    mqt,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION,
                    Versions.JS_BINARY_MAJOR_VERSION,
                    Versions.JS_BINARY_MINOR_VERSION,
                    null);
            if(version == null)
                return;
            if(firstModuleName == null){
                firstModuleName = moduleName;
                firstModuleVersion = version;
            }
            loadModule(null, moduleName, version);
            if(!force)
                errorOnConflictingModule(moduleName, version);
        }
        if (includeRuntime) {
            loadModule(null, "ceylon.runtime", Versions.CEYLON_VERSION_NUMBER);
            loadModule(null, "com.redhat.ceylon.module-resolver-aether", Versions.CEYLON_VERSION_NUMBER);
        }
        loader.resolve();
        String versionSuffix = firstModuleVersion != null && !firstModuleVersion.isEmpty()
                ? "-"+firstModuleVersion
                : "";
        File outputCas = applyCwd(out != null ? out : new File(firstModuleName + versionSuffix + CEYLON_ASSEMBLY_SUFFIX));
        if(outputCas.getParentFile() != null && !outputCas.getParentFile().exists()){
            FileUtil.mkdirs(outputCas.getParentFile());
        }
        if(outputCas.exists()){
            FileUtil.delete(outputCas);
        }
        final Set<String> added = new HashSet<>();

        // Create a MANIFEST.MF and add it to the assembly
        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.putValue("Manifest-Version", "1.0");
        mainAttributes.putValue("Created-By", "Ceylon assemble for module "+firstModuleName+"/"+firstModuleVersion);
        mainAttributes.putValue(Constants.ATTR_ASSEMBLY_MAIN_MODULE, ModuleUtil.makeModuleName(firstModuleName, firstModuleVersion));
        if (run != null) {
            mainAttributes.putValue(Constants.ATTR_ASSEMBLY_RUN, run);
        }
        mainAttributes.putValue(Constants.ATTR_ASSEMBLY_REPOSITORY, "modules");
        File ovrFile = getOverridesFile();
        if (ovrFile != null) {
            mainAttributes.putValue(Constants.ATTR_ASSEMBLY_OVERRIDES, ovrFile.getName());
        }
        if (includeLanguage) {
            String className = JVMModuleUtil.javaClassNameFromCeylon(firstModuleName, run != null ? run : (firstModuleName + "::run"));
            mainAttributes.putValue("Main-Class", "com.redhat.ceylon.tools.assemble.CeylonAssemblyRunner");
            mainAttributes.putValue(Constants.ATTR_ASSEMBLY_MAIN_CLASS, className);
        }
        added.add("META-INF/");
        added.add("META-INF/MANIFEST.MF");

        try(ZipOutputStream zipFile = new JarOutputStream(new FileOutputStream(outputCas), manifest)){
            if (ovrFile != null) {
                // Copy the overrides.xml file to the output CAS
                try (InputStream is = new FileInputStream(ovrFile)) {
                    zipFile.putNextEntry(new ZipEntry(ovrFile.getName()));
                    IOUtils.copyStream(is, zipFile, true, false);
                }
            }
            
            if (includeLanguage) {
                // Copy the CeylonAssemblyRunner class and dependencies to the output CAS
                String prefix = CeylonAssemblyRunner.class.getName().replace('.', '/');
                String[] postfixes = {
                        "",
                        "$CeylonAssemblyClassLoader",
                        "$CeylonAssemblyClassLoader$1"
                };
                for (String postfix : postfixes) {
                    String clsName = prefix + postfix + ".class";
                    try (InputStream is = CeylonAssemblyRunner.class.getResourceAsStream("/" + clsName)) {
                        zipFile.putNextEntry(new ZipEntry(clsName));
                        IOUtils.copyStream(is, zipFile, true, false);
                    }
                }
            }
            
            // Visit the module and all its dependencies
            loader.visitModules(new ModuleGraph.Visitor() {
                @Override
                public void visit(ModuleGraph.Module module) {
                    if(module.artifact != null){
                        File file = module.artifact.artifact();
                        try{
                            if(file != null){
                                if(isVerbose()){
                                    append(file.getAbsolutePath());
                                    newline();
                                }
                                if (module.artifact.namespace() == null) {
                                    // Copy all the "interesting" artifacts to the assembly, not just the JVM one
                                    ArtifactContext ac = new ArtifactContext(module.artifact.namespace(), module.artifact.name(), module.artifact.version(), assemblySuffixes);
                                    List<ArtifactResult> artifacts = getRepositoryManager().getArtifactResults(ac);
                                    for (ArtifactResult ar : artifacts) {
                                        String name = "modules/" + moduleToPath(module.name) + "/" + module.version + "/" + ar.artifact().getName();
                                        addEntry(zipFile, ar.artifact(), name);
                                    }
                                } else if (module.artifact.namespace().equals(MavenRepository.NAMESPACE)) {
                                    String name = "maven/" + moduleToPath(module.name) + "/" + module.version + "/" + file.getName();
                                    addEntry(zipFile, file, name);
                                    // Copy the Maven artifact's pom file as well
                                    String pomName = module.artifact.artifactId() + "-" + module.version + ".pom";
                                    File mfile = new File(file.getParentFile(), pomName);
                                    if (mfile.isFile()) {
                                        name = "maven/" + moduleToPath(module.name) + "/" + module.version + "/" + mfile.getName();
                                        addEntry(zipFile, mfile, name);
                                    }
                                } else if (module.artifact.namespace().equals("npm")) {
                                    File parent = module.artifact.artifact().getParentFile().getCanonicalFile();
                                    while (parent != null && !(new File(parent, "package.json")).exists()) {
                                        parent = parent.getParentFile();
                                    }
                                    String name = "node_modules/" + parent.getName();
                                    addEntry(zipFile, parent, name);
                                }
                            }
                        }catch(IOException x){
                            // lame
                            throw new RuntimeException(x);
                        }
                    }
                }

                private String moduleToPath(String name) {
                    return ModuleUtil.moduleToPath(name).getPath().replace(':', File.separatorChar);
                }

                private void addEntry(final ZipOutputStream zipFile, final File file, final String name) throws IOException {
                    if (file.isFile()) {
                        addFileEntry(zipFile, file, name);
                    } else if (file.isDirectory()) {
                        Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                            @Override
                            public FileVisitResult visitFile(Path path,
                                    BasicFileAttributes attrs)
                                    throws IOException {
                                if (path.toFile().isFile()) {
                                    Path relPath = file.toPath().relativize(path);
                                    String newName = name + "/" + relPath;
                                    addFileEntry(zipFile, path.toFile(), newName);
                                }
                                return super.visitFile(path, attrs);
                            }
                        });
                    }
                }

                private void addFileEntry(ZipOutputStream zipFile, File file, String name) throws IOException {
                    try (InputStream is = new FileInputStream(file)) {
                        zipFile.putNextEntry(new ZipEntry(name));
                        IOUtils.copyStream(is, zipFile, true, false);
                    }
                }
            });
            zipFile.flush();
        }
        flush();
    }

    @Override
    protected boolean shouldExclude(String moduleName, String version) {
        return super.shouldExclude(moduleName, version) 
            || this.excludedModules.contains(moduleName) 
            || !includeLanguage && "ceylon.language".equals(moduleName);
    }
    
    private File getOverridesFile() {
        String path = overrides != null ? overrides : 
            DefaultToolOptions.getDefaultOverrides();
        if (path != null) {
            return applyCwd(new File(path));
        }
        return null;
    }
}
