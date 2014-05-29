package com.redhat.ceylon.tools.version;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenRewriteStream;

import com.redhat.ceylon.common.config.DefaultToolOptions;
import com.redhat.ceylon.common.tool.Argument;
import com.redhat.ceylon.common.tool.CeylonBaseTool;
import com.redhat.ceylon.common.tool.Description;
import com.redhat.ceylon.common.tool.Option;
import com.redhat.ceylon.common.tool.OptionArgument;
import com.redhat.ceylon.common.tool.ParsedBy;
import com.redhat.ceylon.common.tool.RemainingSections;
import com.redhat.ceylon.common.tool.StandardArgumentParsers;
import com.redhat.ceylon.common.tool.Summary;
import com.redhat.ceylon.common.tools.ModuleSpec;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;

@Summary("Shows and updates version numbers in module descriptors")
@Description(
          "If `--set` is present then update the module versions, "
        + "otherwise show the module versions."
        + "\n\n"
        + "If `--dependencies` is present then show the "
        + "versions of module imports of the given module(s)."
        + "\n\n"
        + "`<modules>` specifies the module names (excluding versions) of "
        + "the modules to show or whose versions should be updated. "
        + "If unspecified then all modules are shown/updated.\n\n"
        + "**Note:** Other modules may also be updated unless "
        + "the `--no-update-dependencies` option is used, even if they're not listed in `<modules>`\n\n"
        )
@RemainingSections(value=""
        + "## Examples"
        + "\n\n"
        + "Listing the versions of all the modules in the ceylon SDK:"
        + "\n\n"
        + "    ceylon version"
        + "\n\n"
        + "Listing the version of ceylon.collection, and modules that depend on it"
        + "\n\n"
        + "    ceylon version --dependencies ceylon.collection"
        + "\n\n"
        + "Updating the version of ceylon.collection, and the modules that depend on it"
        + "\n\n"
        + "    ceylon version --set 1.0.1 ceylon.collection")
public class CeylonVersionTool extends CeylonBaseTool {

    // TODO Allow --src to be a :-separated path
    
    private Appendable out = System.out;
    
    private String newVersion;
    
    private List<ModuleSpec> modules;

    private List<File> sourceFolders = DefaultToolOptions.getCompilerSourceDirs();
    
    private String encoding = System.getProperty("file.encoding");
    
    private boolean dependencies = false;

    private boolean noUpdateDependencies = false;

    private Confirm confirm = Confirm.all;
    
    public void setOut(Appendable out) {
        this.out = out;
    }
    
    @OptionArgument(longName="src", argumentName="dir")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("A directory containing Ceylon and/or Java source code (default: `./source`)")
    public void setSourceFolders(List<File> sourceFolders) {
        this.sourceFolders  = sourceFolders;
    }
    
    @OptionArgument(longName="source", argumentName="dirs")
    @ParsedBy(StandardArgumentParsers.PathArgumentParser.class)
    @Description("An alias for `--src`" +
            " (default: `./source`)")
    public void setSource(List<File> source) {
        setSourceFolders(source);
    }
    
    @OptionArgument
    @Description("The new version number to set."
            + "If unspecified then module versions are shown and not updated.")
    public void setSet(String newVersion) {
        this.newVersion = newVersion;
    }
    
    @OptionArgument(argumentName="charset")
    @Description("Used with `--set`, sets the encoding used for reading and writing the `module.ceylon` files " +
            "(default: platform-specific).")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    @Option
    @Description("Do not update of the version in module imports of the "
            + "target module(s) in other modules in the given `--src` directories. "
            + "For example:\n\n"
            + "    ceylon version --set 1.1 ceylon.collection\n\n"
            + "would update the version of ceylon.collection to 1.1 and update "
            + "the module import version of all dependent modules in the given `--src` directories "
            + "which depended on `ceylon.collection` __even if those "
            + "modules are not listed as `<modules>`__.\n\n"
            + "Whereas:\n\n"
            + "    ceylon version --set 1.1 --no-update-dependencies ceylon.collection\n\n"
            + "would just update the version of `ceylon.collection` to 1.1, "
            + "leaving dependent modules depending on the old version.")
    public void setNoUpdateDependencies(boolean noUpdateDependencies) {
        this.noUpdateDependencies = noUpdateDependencies;
    }

    @Option
    @Description("Display modules who depend on the given module. Only used when displaying modules, not when setting a new version.")
    public void setDependencies(boolean dependencies) {
        this.dependencies = dependencies;
    }

    enum Confirm {
        none,
        all,
        dependencies
    }
    
    @OptionArgument(argumentName="option")
    @Description("Used with `--set`, determines which updates require confirmation.\n\n"
            + "* `--confirm=all` requires confirmation "
            + "on the console for each update performed.\n"
            + "* `--confirm=dependencies` means that confirmation is only "
            + "required when updating versions appearing in module imports; "
            + "module versions are updated without confirmation.\n"
            + "* `--confirm=none` prevents any confirmation.\n\n"
            + "(default: `all`).")
    public void setConfirm(Confirm confirm) {
        this.confirm = confirm;
    }
    
    @Argument(argumentName="modules", multiplicity="*")
    public void setModules(List<String> modules) {
        setModuleSpecs(ModuleSpec.parseEachList(modules, ModuleSpec.Option.VERSION_PROHIBITED));
    }
    
    public void setModuleSpecs(List<ModuleSpec> modules) {
        this.modules = modules;
    }

    @Override
    public void initialize() {
    }
    
    @Override
    public void run() throws IOException, RecognitionException {
        setSystemProperties();
        // TODO if version is empty? Prompt? Or should --set have an optional argument? 
        TypeCheckerBuilder tcb = new TypeCheckerBuilder();
        for (File path: this.sourceFolders) {
            tcb.addSrcDirectory(applyCwd(path));
        }
        TypeChecker tc = tcb.getTypeChecker();
        PhasedUnits pus = tc.getPhasedUnits();
        pus.visitModules();
        
        ArrayList<Module> moduleList = new ArrayList<Module>(pus.getModuleManager().getCompiledModules());
        Collections.sort(moduleList, new Comparator<Module>() {
            @Override
            public int compare(Module m1, Module m2) {
                if (match(m1) && !match(m2)) {
                    return -1;
                } else if (!match(m1) && match(m2)) {
                    return +1;
                }
                int cmp = m1.getNameAsString().compareToIgnoreCase(m2.getNameAsString());
                if (cmp == 0) {
                    cmp = m1.getVersion().compareTo(m2.getVersion());
                }
                return cmp;
            }
        });
        // first update all module versions and remember which version we assigned to which module
        // because the user can update every individual version
        Map<String,String> updatedModuleVersions = new HashMap<String,String>();
        for (Module module : moduleList) {
            boolean isMatch = match(module);
            if (newVersion == null) {
                output(module, isMatch);
            } else if (isMatch) {
                if (!updateModuleVersion(module, updatedModuleVersions)) {
                    return;
                }
            }
        }
        // now do dependencies because we know which modules we did update
        if (newVersion != null && !noUpdateDependencies) {
            for (Module module : moduleList) {
                if (!updateModuleImports(module, updatedModuleVersions)) {
                    return;
                }
            }
        }
    }

    private boolean updateModuleVersion(Module module, Map<String,String> updatedModuleVersions) throws IOException,
            RecognitionException {
        String moduleDescriptorPath = module.getUnit().getFullPath();
        CeylonLexer lexer = new CeylonLexer(new ANTLRFileStream(moduleDescriptorPath, encoding));
        TokenRewriteStream tokenStream = new TokenRewriteStream(lexer);
        CeylonParser parser = new CeylonParser(tokenStream);
        Tree.CompilationUnit cu = parser.compilationUnit();
        String v = this.confirm == Confirm.dependencies ? this.newVersion : confirm("update.module.version", module.getNameAsString(), module.getVersion(), this.newVersion);
        if (v == null) {
            return false;
        } else if (!v.isEmpty()) {
            // record the new version for this module
            updatedModuleVersions.put(module.getNameAsString(), v);
            updateModuleVersion(moduleDescriptorPath, tokenStream, cu, v);
        }
        return true;
    }

    private boolean updateModuleImports(Module module, Map<String,String> updatedModuleVersions) throws IOException,
            RecognitionException {
        String moduleDescriptorPath = module.getUnit().getFullPath();
        CeylonLexer lexer = new CeylonLexer(new ANTLRFileStream(moduleDescriptorPath, encoding));
        TokenRewriteStream tokenStream = new TokenRewriteStream(lexer);
        CeylonParser parser = new CeylonParser(tokenStream);
        Tree.CompilationUnit cu = parser.compilationUnit();
        List<Tree.ImportModule> moduleImports = findUpdatedImport(cu, updatedModuleVersions);
        for (Tree.ImportModule moduleImport : moduleImports) {
            String importedModuleName = getModuleName(moduleImport);
            String newVersion = updatedModuleVersions.get(importedModuleName);
            if(newVersion == null)
                newVersion = this.newVersion;
            String v = confirm("update.dependency.version", importedModuleName, module.getNameAsString(), module.getVersion(), newVersion);
            if (v == null) {
                return false;
            } else if (!v.isEmpty()) {
                updateImportVersion(moduleDescriptorPath, tokenStream, moduleImport, v);
            }
        }
        return true;
    }

    private void output(Module module, boolean isMatch) throws IOException,
            RecognitionException {
        if (isMatch) {
            outputVersion(module);
        } else if (dependencies) {
            String moduleDescriptorPath = module.getUnit().getFullPath();
            CeylonLexer lexer = new CeylonLexer(new ANTLRFileStream(moduleDescriptorPath, encoding));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            CeylonParser parser = new CeylonParser(tokenStream);
            Tree.CompilationUnit cu = parser.compilationUnit();
            List<Tree.ImportModule> moduleImports = findImport(cu);
            for (Tree.ImportModule moduleImport : moduleImports) {
                outputDependency(module, moduleImport);
            }
        }
    }
    private void outputVersion(Module module) throws IOException {
        out.append(CeylonVersionMessages.msg("output.module", 
                module.getNameAsString(), module.getVersion()))
            .append(System.lineSeparator());
    }
    private void outputDependency(Module module, ImportModule moduleImport) throws IOException {
        String version = moduleImport.getVersion().getText();
        version = version.substring(1, version.length()-1);
        out.append(CeylonVersionMessages.msg("output.dependency", 
                module.getNameAsString(), module.getVersion(),
                getModuleName(moduleImport), version))
            .append(System.lineSeparator());
        
    }
    
    private boolean match(Module module) {
        return match(module.getNameAsString());
    }
    
    private boolean match(String moduleName) {
        if(this.modules == null
                || this.modules.isEmpty())
            return true;
        for(ModuleSpec spec : this.modules){
            if(spec.getName().equals(moduleName))
                return true;
        }
        return false;
    }
    private void updateImportVersion(String moduleDescriptorPath, TokenRewriteStream tokenStream,
            Tree.ImportModule moduleImport, String version) 
                throws IOException {
        tokenStream.replace(moduleImport.getVersion().getToken(), "\"" + version + "\"");
        write(moduleDescriptorPath, tokenStream);
    }
    private void updateModuleVersion(String moduleDescriptorPath, TokenRewriteStream tokenStream,
            Tree.CompilationUnit cu, String version) 
                throws IOException {
        tokenStream.replace(cu.getModuleDescriptors().get(0).getVersion().getToken(), "\"" + version + "\"");
        write(moduleDescriptorPath, tokenStream);
    }

    private void write(String moduleDescriptorPath, TokenRewriteStream tokenStream) throws IOException,
            UnsupportedEncodingException, FileNotFoundException {
        // Write to a temp file in the same directory, and then atomically rename
        // so if anything goes wrong we've not destroyed the original file
        File target = new File(moduleDescriptorPath);
        File temp = File.createTempFile("module", ".ceylon_tmp", target.getParentFile());
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(temp), encoding)) {
            writer.append(tokenStream.toString());
        } catch (IOException e) {
            temp.delete();
        }
        Files.move(temp.toPath(), target.toPath(), 
                StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    }
    
    private List<Tree.ImportModule> findImport(Tree.CompilationUnit cu) {
        List<Tree.ImportModule> dependsOnTarget = new LinkedList<Tree.ImportModule>();
        for (Tree.ImportModule importModule : cu.getModuleDescriptors().get(0).getImportModuleList().getImportModules()) {
            String name = getModuleName(importModule);
            if (match(name)) {
                dependsOnTarget.add(importModule);
            }
        }
        return dependsOnTarget;
    }

    private List<Tree.ImportModule> findUpdatedImport(Tree.CompilationUnit cu, Map<String,String> updatedModules) {
        List<Tree.ImportModule> dependsOnTarget = new LinkedList<Tree.ImportModule>();
        for (Tree.ImportModule importModule : cu.getModuleDescriptors().get(0).getImportModuleList().getImportModules()) {
            String name = getModuleName(importModule);
            if (updatedModules.containsKey(name)) {
                dependsOnTarget.add(importModule);
            }
        }
        return dependsOnTarget;
    }

    private String getModuleName(Tree.ImportModule importModule) {
        String name;
        if (importModule.getQuotedLiteral() != null) {
            name = importModule.getQuotedLiteral().getText();
            name = name.substring(1, name.length()-1);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Tree.Identifier namePart : importModule.getImportPath().getIdentifiers()) {
                sb.append(namePart.getText()).append('.');
            }
            if (sb.length() > 0) {
                sb.setLength(sb.length()-1);
            }
            name = sb.toString();
        }
        return name;
    }

    /**
     * Issues a confirmation
     * @param prompt
     * @return null means quit, empty string means no, anything else is the version string to use.
     * @throws IOException 
     */
    private String confirm(String msgKey, Object... args) throws IOException {
        if (confirm == Confirm.none) {
            return this.newVersion;
        }
        String version = this.newVersion;
        Console console = System.console();
        while (true) {
            prompt: while (true) {
                // XXX Ugly: Need to replace the new version within the args array 
                args[args.length-1] = version;
                console.printf("%s", CeylonVersionMessages.msg(msgKey, args));
                String ch = console.readLine();
                if (ch.equals(CeylonVersionMessages.msg("mnemonic.yes"))) {
                    return version;
                } else if (ch.equals(CeylonVersionMessages.msg("mnemonic.help"))) {
                    out.append(CeylonVersionMessages.msg("help")).append(System.lineSeparator());
                    continue prompt;
                } else if (ch.equals(CeylonVersionMessages.msg("mnemonic.quit"))) {
                    return null;
                } else if (ch.equals(CeylonVersionMessages.msg("mnemonic.all"))) {
                    this.confirm = Confirm.none;
                    return version;
                } else if (ch.equals(CeylonVersionMessages.msg("mnemonic.no"))) {
                    return "";
                } else if (ch.equals(CeylonVersionMessages.msg("mnemonic.edit"))) {
                    break prompt;
                } else {
                    continue prompt;
                }
            }
            console.printf(CeylonVersionMessages.msg("prompt.version"));
            version = console.readLine();
        }
    }

    

}
