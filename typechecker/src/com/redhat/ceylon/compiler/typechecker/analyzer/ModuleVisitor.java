package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasAnnotation;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detect and populate the list of imports for modules.
 * In theory should only be called on module.ceylon and
 * package.ceylon files
 *
 * Put restrictions on how module.ceylon files are built today:
 *  - names and versions must be string literals or else the 
 *    visitor cannot extract them
 *  - imports must be "explicitly" defined, ie not imported as 
 *    List<Import> or else the module names cannot be extracted
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleVisitor extends Visitor {
    
    /**
     * Instance of the visited module which will receive
     * the dependencies declaration
     */
    private Module mainModule;
    private final ModuleManager moduleManager;
    private final Package pkg;
    private Tree.CompilationUnit unit;
    private Phase phase = Phase.SRC_MODULE;

    public ModuleVisitor(ModuleManager moduleManager, Package pkg) {
        this.moduleManager = moduleManager;
        this.pkg = pkg;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    
    @Override
    public void visit(Tree.CompilationUnit that) {
        unit = that;
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ModuleDescriptor that) {
        super.visit(that);
        if (phase==Phase.SRC_MODULE) {
            String version = that.getVersion().getText();
            List<String> name = getNameAsList(that.getImportPath());
            if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else if (name.get(0).equals(Module.DEFAULT_MODULE_NAME)) {
                that.getImportPath().addError("default is a reserved module name");
            }
            else {
                mainModule = moduleManager.getOrCreateModule(name, version);
                mainModule.setVersion(version);
                if ( !mainModule.getNameAsString().equals(formatPath(that.getImportPath().getIdentifiers())) ) {
                    that.getImportPath()
                        .addError("module name does not match descriptor location");
                }
                moduleManager.addLinkBetweenModuleAndNode(mainModule, unit);
                mainModule.setAvailable(true);
            }
        }
    }
    
    @Override
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            List<String> name = getNameAsList(that.getImportPath());
            if (name.isEmpty()) {
                that.addError("missing packge name");
            }
            else if (name.get(0).equals(Module.DEFAULT_MODULE_NAME)) {
                that.getImportPath().addError("default is a reserved module name");
            }
            else {
                if ( !pkg.getNameAsString().equals(formatPath(that.getImportPath().getIdentifiers())) ) {
                    that.getImportPath()
                        .addError("package name does not match descriptor location");
                }
                if (hasAnnotation(that.getAnnotationList(), "shared")) {
                    pkg.setShared(true);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            String version = that.getVersion().getText();
            List<String> name = getNameAsList(that.getImportPath());
            if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else if (name.get(0).equals(Module.DEFAULT_MODULE_NAME)) {
                that.getImportPath().addError("default is a reserved module name");
            }
            else {
                Module importedModule = moduleManager.getOrCreateModule(name,version);
                if (mainModule != null) {
                    if (importedModule.getVersion() == null) {
                        importedModule.setVersion(version);
                    }
                    //String optionalString = argumentToString(getArgument(that, "optional"));
                    //String exportString = argumentToString(getArgument(that, "export"));
                    ModuleImport moduleImport = moduleManager.findImport(mainModule, importedModule);
                    if (moduleImport == null) {
                        //boolean optional = optionalString!=null && optionalString.equals("true");
                        //boolean export = exportString!=null && exportString.equals("true");
                        moduleImport = new ModuleImport(importedModule, false, false);//TODO: optional, export
                        mainModule.getImports().add(moduleImport);
                    }
                    moduleManager.addModuleDependencyDefinition(moduleImport, that);
                }
                //else we leave it behind unprocessed
            }
        }
    }

    private List<String> getNameAsList(Tree.ImportPath that) {
        List<String> name = new ArrayList<String>();
        for (Tree.Identifier i: that.getIdentifiers()) {
           name.add(i.getText()); 
        }
        return name;
    }
    
    public enum Phase {
        SRC_MODULE,
        REMAINING
    }
    
    public Module getMainModule() {
        return mainModule;
    }
    
}
