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
import java.util.Set;

import com.redhat.ceylon.cmr.api.ModuleDependencyInfo;
import com.redhat.ceylon.cmr.api.ModuleSearchResult.ModuleDetails;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.DependencyErrors;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.DependencyResults;
import com.redhat.ceylon.cmr.ceylon.LegacyImporter.ImporterFeedback;
import com.redhat.ceylon.cmr.ceylon.OutputRepoUsingTool;
import com.redhat.ceylon.cmr.impl.CMRException;
import com.redhat.ceylon.common.Messages;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tool.ToolUsageError;
import com.redhat.ceylon.common.tools.CeylonTool;
import com.redhat.ceylon.common.tools.ModuleSpec;

@Summary("Imports a jar file into a Ceylon module repository")
@Description("Imports the given `<jar-file>` using the module name and version " +
        "given by `<module>` into the repository named by the " +
        "`--out` option.\n" +
        "\n" +
        "`<module>` is a module name and version separated with a slash, for example " +
        "`com.example.foobar/1.2.0`.\n" +
        "\n" +
        "`<jar-file>` is the name of the Jar file to import.")
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
        OutputRepoUsingTool.DOCSECTION_REPOSITORIES)
public class CeylonImportJarTool extends OutputRepoUsingTool {

    private ModuleSpec module;
    private File jarFile;
    private File descriptor;
    private boolean updateDescriptor;
    private boolean force;
    private boolean dryRun;
    private boolean showClasses;
    private boolean showSuggestions;
    
    public CeylonImportJarTool() {
        super(ImportJarMessages.RESOURCE_BUNDLE);
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
        File f = applyCwd(jarFile);
        checkReadableFile(f, "error.jarFile", true);
        if(!f.getName().toLowerCase().endsWith(".jar"))
            throw new ImportJarException("error.jarFile.notJar", new Object[]{f.toString()}, null);
        
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
        LegacyImporter importer = new LegacyImporter(module.getName(), module.getVersion(), applyCwd(jarFile), getOutputRepositoryManager(), getRepositoryManager(), log, new ImporterFeedback() {
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
                    append("    ").append(dep.getName()).newline();
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
            public void packageName(String pkg) throws IOException {
                append("    ").append(pkg).newline();
            }

            @Override
            public void afterPackages() {
            }

            @Override
            public void beforeClasses() throws IOException {
                msg("info.declare.class.imports").newline();
            }

            @Override
            public void className(String cls) throws IOException {
                append("    ").append(cls).newline();
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
        LegacyImporter importer = createImporter().moduleDescriptor(applyCwd(descriptor));
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
                } catch(CMRException x) {
                    throw new ImportJarException("error.failedWriteArtifact", new Object[]{module, x.getLocalizedMessage()}, x);
                } catch(Exception x) {
                    // FIXME: remove when the whole CMR is using CMRException
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