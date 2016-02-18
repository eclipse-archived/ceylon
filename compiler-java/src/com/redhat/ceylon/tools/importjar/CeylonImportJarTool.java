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
package com.redhat.ceylon.tools.importjar;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.DependencyErrors;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.DependencyResults;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.ImporterFeedback;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Hidden;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.model.cmr.RepositoryException;

@Summary("Imports a jar file into a Ceylon module repository")
@Description("Imports the given `<jar-file>` using the module name and version " +
        "given by `<module>` into the repository named by the " +
        "`--out` option.\n" +
        "\n" +
        "`<module>` is a module name and version separated with a slash, for example " +
        "`com.example.foobar/1.2.0`.\n" +
        "\n" +
        "`<jar-file>` is the name of the Jar file to import.\n" +
        "\n" +
        "`<source-jar-file>` is an optional name of a Jar file containing the sources " +
        "for the Jar file to import.\n")
@RemainingSections("## Descriptors" +
        "\n\n" +
        "When the import-jar tool analyses the <jar-file> and complains about missing " +
        "dependencies a module descriptor file should be provided. The name of this " +
        "file can be provided using the `--descriptor` option or, when left out, the tool " +
        "will look for `<jar-file>.module.properties` or `<jar-file>.module.xml`. " +
        "\n\n" +
        "The format of these `.properties` or `.xml` files is documented online: " +
        "http://www.ceylon-lang.org/documentation/1.1/reference/structure/module/#legacy_modules " +
        "\n\n" +
        "If the option `--update-descriptor` is given the tool will try to update the " +
        "given descriptor file with the available information (for now this only works " +
        "for the `.properties` files). If the file didn't exist yet it will be created." +
        "\n\n" +
        "If the option `--source-jar-file` is given the tool will try to include the " +
        "source jar into the module as well, including a SHA1 of that source jar." +
        "\n\n" +
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonImportJarTool extends OutputRepoUsingTool {

    private ModuleSpec module;
    private File jarFile;
    private File sourceJarFile;
    private File descriptor;
    private boolean updateDescriptor;
    private boolean force;
    private boolean dryRun;
    private boolean showClasses;
    private boolean showSuggestions;
    private boolean allowCars;
    private boolean ignoreAnnotations;
	private List<String> missingDependenciesPackages;
	private Map<String, List<String>> parsedMissingDependenciesPackages;
    
    public CeylonImportJarTool() {
        super(ImportJarMessages.RESOURCE_BUNDLE);
    }
    
    @OptionArgument(longName="missing-dependency-packages")
    @Description("Specifies which packages a missing dependency contains. Can be specified multiple times. " +
            "Format: `module-name/module-version=package-wildcard(,package-wildcard)*`, where "+
    		"`package-wildcard` supports `*`, `**` and `?` wildcards.")
    public void setMissingDependencyPackages(List<String> missingDependencyPackages) {
        this.missingDependenciesPackages = missingDependencyPackages;
    }
    
    @OptionArgument(argumentName="file")
    @Description("Specify a module.xml or module.properties file to be used "
            + "as the module descriptor")
    public void setDescriptor(File descriptor) {
        this.descriptor = descriptor;
    }
    
    @Argument(argumentName="module", multiplicity="1", order=0)
    public void setModuleSpec(String module) {
        setModuleSpec(ModuleSpec.parse(module, 
                ModuleSpec.Option.VERSION_REQUIRED, 
                ModuleSpec.Option.DEFAULT_MODULE_PROHIBITED));
    }
    
    public void setModuleSpec(ModuleSpec module) {
        this.module = module;
    }

    @Argument(argumentName="jar-file", multiplicity="1", order=1)
    public void setFile(File jarFile) {
        this.jarFile = jarFile;
    }

    @Argument(argumentName="source-jar-file", multiplicity="?", order=2)
    public void setSourceJarFile(File sourceJarFile) {
        this.sourceJarFile = sourceJarFile;
    }
    
    @Option(longName="update-descriptor")
    @Description("Whenever possible will create or adjust the descriptor file with the necessary definitions.")
    public void setUpdateDescriptor(boolean updateDescriptor) {
        this.updateDescriptor = updateDescriptor;
    }
    
    @Option(longName="force")
    @Description("Skips sanity checks and forces publication of the JAR.")
    public void setForce(boolean force) {
        this.force = force;
    }

    @Option(longName="ignore-annotations")
    @Description("Do not check annotations for imports (default: `false`).")
    public void setIgnoreAnnotations(boolean ignoreAnnotations) {
        this.ignoreAnnotations = ignoreAnnotations;
    }

    @Hidden
    @Option(longName="allow-cars")
    @Description("Allows importing car files [for tests only].")
    public void setAllowCars(boolean allowCars) {
        this.allowCars = allowCars;
    }

    @Option(longName="dry-run")
    @Description("Performs all the sanity checks but does not publish the JAR.")
    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }
    
    @Option(longName="show-classes")
    @Description("Shows all external classes that are not declared as imports instead of their packages only.")
    public void setShowClasses(boolean showClasses) {
        this.showClasses = showClasses;
    }
    
    @Option(longName="show-suggestions")
    @Description("Shows suggestions for modules based on missing package names (this can take a long time).")
    public void setShowSuggestions(boolean showSuggestions) {
        this.showSuggestions = showSuggestions;
    }
    
    @Override
    protected boolean needsSystemRepo() {
        return false;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
        File f = assertJar(jarFile, "error.jarFile", "error.jarFile.notJar");
        
        if (sourceJarFile != null) {
             assertJar(sourceJarFile, "error.sourceJarFile", "error.sourceJarFile.notJar");
        }
        
        if (descriptor == null) {
            String baseName = f.getName().substring(0, f.getName().length() - 4);
            File desc = new File(f.getParentFile(), baseName + ".module.xml");
            if (!desc.isFile()) {
                desc = new File(f.getParentFile(), baseName + ".module.properties");
                if (desc.isFile() || updateDescriptor) {
                    descriptor = desc;
                }
            } else {
                descriptor = desc;
            }
        }
        if (descriptor != null) {
            checkReadableFile(applyCwd(descriptor), "error.descriptorFile", !updateDescriptor);
            if(!(descriptor.toString().toLowerCase().endsWith(".xml") ||
                    descriptor.toString().toLowerCase().endsWith(".properties")))
                throw new ImportJarException("error.descriptorFile.badSuffix", new Object[]{descriptor}, null);
        }
        if(missingDependenciesPackages != null){
        	parsedMissingDependenciesPackages = new HashMap<>();
        	for(String spec : missingDependenciesPackages){
        		int moduleSeparator = spec.indexOf('=');
        		if(moduleSeparator == -1)
        			throw new ImportJarException("error.missingDependenciesPackages.badSyntax", new Object[]{spec}, null);
        		String moduleSpec = spec.substring(0, moduleSeparator);
        		String packagesSpec = spec.substring(moduleSeparator+1);
        		if(moduleSpec.isEmpty() || packagesSpec.isEmpty())
        			throw new ImportJarException("error.missingDependenciesPackages.badSyntax", new Object[]{spec}, null);
        		List<String> packages = new LinkedList<>();
        		for(String pkg : packagesSpec.split(",")){
            		if(pkg.isEmpty())
            			throw new ImportJarException("error.missingDependenciesPackages.badSyntax", new Object[]{spec}, null);
            		packages.add(pkg);
        		}
        		if(packages.isEmpty())
        			throw new ImportJarException("error.missingDependenciesPackages.badSyntax", new Object[]{spec}, null);
        		parsedMissingDependenciesPackages.put(moduleSpec, packages);
        	}
        }
    }
    
    private File assertJar(File aJarFile, String errorResource1, String errorResource2) {
        File aJarFileWithCwd = applyCwd(aJarFile);
        checkReadableFile(aJarFileWithCwd, errorResource1, true);

        String fileName = aJarFileWithCwd.getName().toLowerCase();
        // allow: jar OK, car OK, other OK
        // !allow: jar OK, car, other
        if (! fileName.endsWith(".jar")
        		 && !(allowCars && fileName.endsWith(".car"))) {
            throw new ImportJarException(errorResource2, 
                                         new Object[] { aJarFile.toString() }, 
                                         null);
        }
        
        return aJarFileWithCwd;
    }

    private void checkReadableFile(File f, String keyPrefix, boolean required) {
        if (f.exists()) {
            if(f.isDirectory())
                throw new ImportJarException(keyPrefix + ".isDirectory", new Object[]{f.toString()}, null);
            if(!f.canRead())
                throw new ImportJarException(keyPrefix + ".notReadable", new Object[]{f.toString()}, null);
        } else if (required) {
            throw new ImportJarException(keyPrefix + ".doesNotExist", new Object[]{f.toString()}, null);
        }
    }
    
    private LegacyImporter createImporter() {
        LegacyImporter importer = new LegacyImporter(module.getName(), module.getVersion(), applyCwd(jarFile), applyCwd(sourceJarFile), getOutputRepositoryManager(), getRepositoryManager(), log, new ImporterFeedback() {
            @Override
            public void beforeDependencies() throws IOException {
                msg("info.checkingDependencies").newline();
            }
            
            @Override
            public void beforeDependency(ModuleDependencyInfo dep) throws IOException {
                append("    ").append(dep).append(" ... [");
            }
            
            @Override
            public void dependency(DependencyResults result, ModuleDependencyInfo dep) throws IOException {
                switch (result) {
                case DEP_OK:
                    msg("info.ok");
                    break;
                case DEP_OK_UNUSED:
                    msg("info.okButUnused");
                    break;
                case DEP_MARK_SHARED:
                    msg("error.markShared");
                    break;
                case DEP_NOT_FOUND:
                    msg("info.notFound");
                    break;
                case DEP_CHECK_FAILED:
                    msg("error.checkFailed");
                    break;
                case DEP_JDK:
                    append("    ").append(dep.getName());
                    if(dep.isExport())
                    	append(" ... [shared]");
                    newline();
                    break;
                }
            }
            
            @Override
            public void dependencyError(DependencyErrors error, ModuleDependencyInfo dep) {
                switch (error) {
                case DEPERR_INVALID_MODULE_DEFAULT:
                    throw new ImportJarException("error.descriptorFile.invalid.module.default");
                case DEPERR_INVALID_MODULE_NAME:
                    throw new ImportJarException("error.descriptorFile.invalid.module", new Object[]{dep.getName()}, null);
                case DEPERR_INVALID_MODULE_VERSION:
                    throw new ImportJarException("error.descriptorFile.invalid.module.version", new Object[]{dep.getVersion()}, null);
                }
            }
            
            @Override
            public void afterDependency(ModuleDependencyInfo dep) throws IOException {
                append("]").newline();
            }
            
            @Override
            public void afterDependencies() {
            }

            @Override
            public void beforeJdkModules() throws IOException {
                msg("info.declare.jdk.imports").newline();
            }

            @Override
            public void afterJdkModules() {
            }

            @Override
            public void beforePackages() throws IOException {
                msg("info.declare.module.imports").newline();
                if (!showSuggestions) {
                    msg("info.try.suggestions").newline();
                }
            }

            @Override
            public void packageName(String pkg, boolean shared) throws IOException {
                append("    ").append(pkg);
                if(shared)
                	append(" ... [shared]");
                newline();
            }

            @Override
            public void afterPackages() {
            }

            @Override
            public void beforeClasses() throws IOException {
                msg("info.declare.class.imports").newline();
            }

            @Override
            public void className(String cls, boolean shared) throws IOException {
                append("    ").append(cls);
                if(shared)
                	append(" ... [shared]");
                newline();
            }

            @Override
            public void afterClasses() {
            }

            @Override
            public ModuleDependencyInfo suggestions(String pkg, Set<ModuleDetails> suggestions) throws IOException {
                ModuleDependencyInfo dep = null;
                if (!suggestions.isEmpty()) {
                    append(", ");
                    if (suggestions.size() > 1) {
                        msg("info.try.importing.multiple");
                        for (ModuleDetails md : suggestions) {
                            newline();
                            String modver = md.getName() + "/" + md.getLastVersion().getVersion();
                            append("        ").append(modver);
                            dep = new ModuleDependencyInfo(md.getName(), md.getLastVersion().getVersion(), false, true);
                        }
                    } else {
                        ModuleDetails md = suggestions.iterator().next();
                        String modver = md.getName() + "/" + md.getLastVersion().getVersion();
                        msg("info.try.importing", modver);
                        dep = new ModuleDependencyInfo(md.getName(), md.getLastVersion().getVersion(), false, true);
                    }
                }
                return dep;
            }
        });
        return importer;
    }
    
    @Override
    public void run() throws Exception {
        LegacyImporter importer = createImporter()
        		.moduleDescriptor(applyCwd(descriptor))
        		.missingDependenciesPackages(parsedMissingDependenciesPackages);
        importer.setIgnoreAnnotations(ignoreAnnotations);
        if (!force || updateDescriptor) {
            try {
                importer.loadModuleDescriptor();
            } catch (ImportJarException x) {
                throw x;
            } catch (Exception x) {
                String key = "error.descriptorFile.invalid.";
                if (descriptor.getName().endsWith(".xml")) {
                    key += "xml";
                } else {
                    key += "properties";
                }
                throw new ImportJarException(key, new Object[]{descriptor.getPath(), x.getMessage()}, x);
            }
            
            if (!showClasses) {
                importer.listPackages(showSuggestions);
            } else {
                importer.listClasses();
            }
        }
        boolean hasErrors = importer.hasErrors();
        if (importer.hasProblems()) {
            if (updateDescriptor && descriptor != null) {
                if (!dryRun) {
                    importer.updateModuleDescriptor();
                }
            } else {
                hasErrors = true;
            }
        }
        if (!hasErrors || force) {
            if (!hasErrors) {
                if (force && !updateDescriptor) {
                    msg("info.forcedUpdate");
                } else {
                    msg("info.noProblems");
                }
            } else {
                msg("error.problemsFoundForced");
            }
            if (!dryRun) {
                msg("info.noProblems.publishing").newline();
                try {
                    importer.publish();
                } catch(RepositoryException x) {
                    throw new ImportJarException("error.failedWriteArtifact", new Object[]{module, x.getLocalizedMessage()}, x);
                } catch(Exception x) {
                    // FIXME: remove when the whole CMR is using RepositoryException
                    throw new ImportJarException("error.failedWriteArtifact", new Object[]{module, x.getLocalizedMessage()}, x);
                }
                String repoString = this.getOutputRepositoryManager().getRepositoriesDisplayString().toString();
                
                msg("info.published", this.module.toString(), repoString.substring(1, repoString.length()-1));
            }
            append(".").newline();
        } else {
            String msgKey;
            if (!updateDescriptor && descriptor == null) {
                msgKey = "error.problemsFoundSuggest";
            } else {
                msgKey = "error.problemsFound";
            }
            throw new ToolUsageError(Messages.msg(ImportJarMessages.RESOURCE_BUNDLE, msgKey));
        }
    }
}