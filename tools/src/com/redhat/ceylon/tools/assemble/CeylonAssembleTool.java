package com.redhat.ceylon.tools.assemble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.redhat.ceylon.cmr.api.ModuleQuery;
import com.redhat.ceylon.cmr.ceylon.loader.ModuleGraph;
import com.redhat.ceylon.cmr.impl.IOUtils;
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
import com.redhat.ceylon.tools.moduleloading.ModuleLoadingTool;

/**
 * @author Tako Schotanus (tako@ceylon-lang.org)
 */
@Summary("Generate a Ceylon assemble for a given module")
@Description("Generate an executable _assemble_ which contains the given module and all its run-time"
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

    @Description("Target assemble file (defaults to `{name}-{version}.jar`).")
    @OptionArgument(shortName = 'o', argumentName="file")
    public void setOut(File out) {
        this.out = out;
    }

    @OptionArgument(argumentName="moduleOrFile", shortName='x')
    @Description("Excludes modules from the resulting far jat. Can be a module name or " + 
            "a file containing module names. Can be specified multiple times. Note that "+
            "this excludes the module from the resulting assemble, but if your modules require that "+
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
                    ModuleQuery.Type.JVM,
                    Versions.JVM_BINARY_MAJOR_VERSION,
                    Versions.JVM_BINARY_MINOR_VERSION,
                    null, null, // JS binary but don't care since JVM
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
                                    String name = "modules/" + moduleToPath(module.name) + "/" + module.version + "/" + file.getName();
                                    addEntry(zipFile, file, name);
                                    // See if there are any module files to copy as well
                                    File mfile = new File(file.getParentFile(), "module.xml");
                                    if (mfile.isFile()) {
                                        name = "modules/" + moduleToPath(module.name) + "/" + module.version + "/" + mfile.getName();
                                        addEntry(zipFile, mfile, name);
                                    }
                                    mfile = new File(file.getParentFile(), "module.properties");
                                    if (mfile.isFile()) {
                                        name = "modules/" + moduleToPath(module.name) + "/" + module.version + "/" + mfile.getName();
                                        addEntry(zipFile, mfile, name);
                                    }
                                } else if (module.artifact.namespace().equals("maven")) {
                                    String name = "maven/" + moduleToPath(module.name) + "/" + module.version + "/" + file.getName();
                                    addEntry(zipFile, file, name);
                                    // Copy the Maven artifact's pom file as well
                                    String pomName = module.artifact.artifactId() + "-" + module.version + ".pom";
                                    File mfile = new File(file.getParentFile(), pomName);
                                    if (mfile.isFile()) {
                                        name = "maven/" + moduleToPath(module.name) + "/" + module.version + "/" + mfile.getName();
                                        addEntry(zipFile, mfile, name);
                                    }
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

                private void addEntry(ZipOutputStream zipFile, File file, String name) throws IOException {
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
        return super.shouldExclude(moduleName, version) ||
                this.excludedModules.contains(moduleName) ||
                (!includeLanguage && "ceylon.language".equals(moduleName));
    }
    
    private File getOverridesFile() {
        String path = (overrides != null) ? overrides : DefaultToolOptions.getDefaultOverrides();
        if (path != null) {
            return applyCwd(new File(path));
        }
        return null;
    }
}
