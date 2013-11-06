package com.redhat.ceylon.compiler.typechecker.context;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;

import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonLexer;
import com.redhat.ceylon.compiler.typechecker.parser.CeylonParser;
import com.redhat.ceylon.compiler.typechecker.parser.LexError;
import com.redhat.ceylon.compiler.typechecker.parser.ParseError;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.util.ModuleManagerFactory;

/**
 * Contains phased units
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PhasedUnits extends PhasedUnitMap<PhasedUnit, PhasedUnit> {
    private final Context context;
    private final ModuleManager moduleManager;
    private List<String> moduleFilters;
    private String encoding;

    public PhasedUnits(Context context) {
        this.context = context;
        this.moduleManager = new ModuleManager(context);
        this.moduleManager.initCoreModules();
    }

    public PhasedUnits(Context context, ModuleManagerFactory moduleManagerFactory) {
        this.context = context;
        if(moduleManagerFactory != null){
            this.moduleManager = moduleManagerFactory.createModuleManager(context);
        }else{
            this.moduleManager = new ModuleManager(context);
        }
        this.moduleManager.initCoreModules();
    }
    
    public void setModuleFilters(List<String> moduleFilters){
        this.moduleFilters = moduleFilters;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void parseUnits(List<VirtualFile> srcDirectories) {
        for (VirtualFile file : srcDirectories) {
            parseUnit(file, file);
        }
    }

    public void parseUnit(VirtualFile srcDir) {
        parseUnit(srcDir, srcDir);
    }

    public void parseUnit(VirtualFile file, VirtualFile srcDir) {
        try {
            if (file.isFolder()) {
                //root directory is the src dir => start from here
                for (VirtualFile subfile : file.getChildren()) {
                    parseFileOrDirectory(subfile, srcDir);
                }
            }
            else {
                //simple file compilation
                //TODO is that really valid?
                parseFileOrDirectory(file, srcDir);
            }
        }
        catch (RuntimeException e) {
            //let it go
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("Error while parsing the source directory: " + file.toString(), e);
        }
    }

    protected void parseFile(VirtualFile file, VirtualFile srcDir) throws Exception {
        if (file.getName().endsWith(".ceylon")) {

            //System.out.println("Parsing " + file.getName());
            CeylonLexer lexer = new CeylonLexer(new ANTLRInputStream(file.getInputStream(), getEncoding()));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            CeylonParser parser = new CeylonParser(tokenStream);
            Tree.CompilationUnit cu = parser.compilationUnit();
            List<CommonToken> tokens = new ArrayList<CommonToken>(tokenStream.getTokens().size()); 
            tokens.addAll(tokenStream.getTokens());
            PhasedUnit phasedUnit = new PhasedUnit(file, srcDir, cu, 
                    moduleManager.getCurrentPackage(), moduleManager,
                    context, tokens);
            addPhasedUnit(file, phasedUnit);

            List<LexError> lexerErrors = lexer.getErrors();
            for (LexError le : lexerErrors) {
                //System.out.println("Lexer error in " + file.getName() + ": " + le.getMessage());
                cu.addLexError(le);
            }
            lexerErrors.clear();

            List<ParseError> parserErrors = parser.getErrors();
            for (ParseError pe : parserErrors) {
                //System.out.println("Parser error in " + file.getName() + ": " + pe.getMessage());
                cu.addParseError(pe);
            }
            parserErrors.clear();

        }
    }

    protected String getEncoding() {
		return encoding != null ? encoding : System.getProperty("file.encoding");
	}

    private void parseFileOrDirectory(VirtualFile file, VirtualFile srcDir) throws Exception {
        if (file.isFolder()) {
            processDirectory(file, srcDir);
        }
        else {
            parseFile(file, srcDir);
        }
    }

    private void processDirectory(VirtualFile dir, VirtualFile srcDir) throws Exception {
        moduleManager.push(dir.getName());
        
        // See if we're defining a new module
        final List<VirtualFile> files = dir.getChildren();
        boolean definesModule = false;
        for (VirtualFile file : files) {
            if (ModuleManager.MODULE_FILE.equals(file.getName())) {
                definesModule = true;
                break;
            }
        }

        if(checkModuleFilters(definesModule)){
            if(definesModule)
                moduleManager.visitModuleFile();
            for (VirtualFile file : files) {
                parseFileOrDirectory(file, srcDir);
            }
        }
        moduleManager.pop();
    }
    
    private boolean checkModuleFilters(boolean definesModule) {
        if(moduleFilters == null || moduleFilters.isEmpty())
            return true;
        Package pkg = moduleManager.getCurrentPackage();
        String pkgName = pkg.getNameAsString();
        /*
            ; filter example syntax:
            module-list+ (wanted-modules+) -> allowed-paths*
        
            ; Filter unwanted modules
            a, a.b, aa, b (a) -> a, a.b
        
            ; Filter unwanted longer-named modules 
            a.b.c, a.b.d (a.b) -> 
        
            ; Allow going to the module we're looking for
            a, a.b, a.b.c (a.b.c) -> a, a.b, a.b.c
        
            ; Allow any package in the default module
            a, b, c (default) -> a, b, c
        */
        for(String module : moduleFilters){
            // Are we looking for the default module?
            if(module.equals(Module.DEFAULT_MODULE_NAME)){
                // Allow anything for the default module if it's not owned by another module
                // and we're not defining a new module.
                if(pkg.getModule().isDefault() && !definesModule){
                    return true;
                }
                // None of the other rules apply to the default module.
                continue;
            }
            // Allow module folder.
            // We don't check for the presence of a module declaration here, we asked for this module so
            // if we're not defining it we will just have to deal with the error elsewhere (its absence).
            if(pkgName.equals(module)){
                return true;
            }
            // Allow sub-packages, but only if they are actually contained by the module itself
            // and not accidental modules with longer names.
            // We don't check for the presence of a module declaration here since that will generate
            // an error later because a module can't contain another module
            if(pkgName.startsWith(module + ".") && pkg.getModule().getNameAsString().equals(module)){
                return true;
            }
            // Allow the path that leads to the modules we're looking for, as long as they are in the default module
            // and we're not defining a new module
            if(module.startsWith(pkgName + ".") && pkg.getModule().isDefault() && !definesModule){
                return true;
            }
        }
        return false;
    }

    public void visitModules() {
        List<PhasedUnit> listOfUnits = getPhasedUnits();
        for (PhasedUnit pu : listOfUnits) {
            pu.visitSrcModulePhase();
        }
        for (PhasedUnit pu : listOfUnits) {
            pu.visitRemainingModulePhase();
        }
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    protected PhasedUnit toStoredType(PhasedUnit phasedUnit) {
        return phasedUnit;
    }

    @Override
    protected PhasedUnit fromStoredType(PhasedUnit storedValue, String path) {
        return storedValue;
    }
}
