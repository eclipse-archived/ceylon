package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasAnnotation;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportPath;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QuotedLiteral;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.util.ModuleManager;

/**
 * Detect and populate the list of imports for modules. In 
 * theory should only be called on module.ceylon and
 * package.ceylon files
 *
 * Put restrictions on how module.ceylon files are built 
 * today:
 * 
 *  - names and versions must be string literals or else 
 *    the visitor cannot extract them
 *  - imports must be "explicitly" defined, ie not imported 
 *    as List<Import> or else the module names cannot be 
 *    extracted
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
    private final ModuleSourceMapper moduleManagerUtil;
    private final Package pkg;
    private Tree.CompilationUnit unit;
    private Phase phase = Phase.SRC_MODULE;
    private boolean completeOnlyAST = false;
    private String moduleBackend = null;

    public void setCompleteOnlyAST(boolean completeOnlyAST) {
        this.completeOnlyAST = completeOnlyAST;
    }

    public boolean isCompleteOnlyAST() {
        return completeOnlyAST;
    }

    public ModuleVisitor(ModuleManager moduleManager, ModuleSourceMapper moduleManagerUtil, Package pkg) {
        this.moduleManager = moduleManager;
        this.moduleManagerUtil = moduleManagerUtil;
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
    
    private static String getVersionString(Tree.QuotedLiteral quoted) {
        if (quoted==null) {
            return null;
        }
        else {
            String versionString = quoted.getText();
            if (versionString.length()<2) {
                return "";
            }
            else {
                if (versionString.charAt(0)=='\'') {
                    quoted.addError("version should be double-quoted");
                }
                return versionString.substring(1, versionString.length()-1);
            }
        }
    }

    private static String getNameString(Tree.QuotedLiteral quoted) {
        return getNameString(quoted, true);
    }

    private static String getNameString(Tree.QuotedLiteral quoted, boolean addErrorOnInvalidQuotes) {
        String nameString = quoted.getText();
        if (nameString.length()<2) {
            return "";
        }
        else {
            if (addErrorOnInvalidQuotes && nameString.charAt(0)=='\'') {
                quoted.addError("module name should be double-quoted");
            }
            return nameString.substring(1, nameString.length()-1);
        }
    }

    @Override
    public void visit(Tree.ModuleDescriptor that) {
        moduleBackend = getNativeBackend(that.getAnnotationList(), that.getUnit());
        super.visit(that);
        if (phase==Phase.SRC_MODULE) {
            String version = getVersionString(that.getVersion());
            ImportPath importPath = that.getImportPath();
            List<String> name = getNameAsList(importPath);
            if (pkg.getNameAsString().isEmpty()) {
                that.addError("module descriptor encountered in root source directory");
            }
            else if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else {
                String initialName = name.get(0);
                if (initialName.equals(Module.DEFAULT_MODULE_NAME)) {
                    importPath.addError("reserved module name: 'default'");
                }
                else if (name.size()==1 && initialName.equals("ceylon")) {
                    importPath.addError("reserved module name: 'ceylon'");
                }
                else {
                    if (initialName.equals("ceylon")) {
                        importPath.addUsageWarning(Warning.ceylonNamespace,
                                "discouraged module name: this namespace is used by Ceylon platform modules");
                    }
                    else if (initialName.equals("java") || initialName.equals("javax")) {
                        importPath.addUnsupportedError("unsupported module name: this namespace is used by Java platform modules");
                    }
                    mainModule = moduleManager.getOrCreateModule(name, version);
                    importPath.setModel(mainModule);
                    if (!completeOnlyAST) {
                        mainModule.setUnit(unit.getUnit());
                        mainModule.setVersion(version);
                    }
                    String nameString = formatPath(importPath.getIdentifiers());
                	if ( !pkg.getNameAsString().equals(nameString) ) {
                        importPath
                            .addError("module name does not match descriptor location: '" + 
                            		nameString + "' should be '" + pkg.getNameAsString() + "'", 
                            		8000);
                    }
                    if (!completeOnlyAST) {
                        moduleManagerUtil.addLinkBetweenModuleAndNode(mainModule, that);
                        mainModule.setAvailable(true);
                        mainModule.getAnnotations().clear();
                        buildAnnotations(that.getAnnotationList(), mainModule.getAnnotations());
                        mainModule.setNativeBackend(moduleBackend);
                    }
                }
            }
            HashSet<String> set = new HashSet<String>();
            Tree.ImportModuleList iml = that.getImportModuleList();
            if (iml!=null) {
                for (Tree.ImportModule im: iml.getImportModules()) {
                    Tree.ImportPath ip = im.getImportPath();
                    if (ip!=null) {
                        String mp = formatPath(ip.getIdentifiers());
                        if (!set.add(mp)) {
                            ip.addError("duplicate module import: '" + mp + "'");
                        }
                    }
                    QuotedLiteral ql = im.getQuotedLiteral();
                    if(ql != null){
                        String mp = getNameString(ql, false);
                        if (!set.add(mp)) {
                            ql.addError("duplicate module import: '" + mp + "'");
                        }
                    }
                }
            }
        }
        moduleBackend = null;
    }
    
    @Override
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            Tree.ImportPath importPath = that.getImportPath();
            List<String> name = getNameAsList(importPath);
            if (pkg.getNameAsString().isEmpty()) {
                that.addError("package descriptor encountered in root source directory");
            }
            else if (name.isEmpty()) {
                that.addError("missing package name");
            }
            else if (name.get(0).equals(Module.DEFAULT_MODULE_NAME)) {
                importPath.addError("reserved module name: 'default'");
            }
            else if (name.size()==1 && name.get(0).equals("ceylon")) {
                importPath.addError("reserved module name: 'ceylon'");
            }
            else {
                if (name.get(0).equals("ceylon")) {
                    importPath.addUsageWarning(Warning.ceylonNamespace,
                            "discouraged package name: this namespace is used by Ceylon platform modules");
                }
                else if (name.get(0).equals("java")||name.get(0).equals("javax")) {
                    importPath.addUsageWarning(Warning.javaNamespace,
                            "discouraged package name: this namespace is used by Java platform modules");
                }
                importPath.setModel(pkg);
                if (!completeOnlyAST) {
                    pkg.setUnit(unit.getUnit());
                }
                String nameString = formatPath(importPath.getIdentifiers());
				if ( !pkg.getNameAsString().equals(nameString) ) {
                    importPath
                        .addError("package name does not match descriptor location: '" + 
                        		nameString + "' should be '" + pkg.getNameAsString() + "'", 
                                8000);
                }
                if (!completeOnlyAST) {
                    if (hasAnnotation(that.getAnnotationList(), "shared", unit.getUnit())) {
                        pkg.setShared(true);
                    }
                    else {
                        pkg.setShared(false);
                    }
                    pkg.getAnnotations().clear();
                    buildAnnotations(that.getAnnotationList(), pkg.getAnnotations());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            if (that.getVersion()==null) {
                that.addError("missing module version");
            }
            String version = getVersionString(that.getVersion());
            String nameString;
            List<String> name;
            Node node;
            if (that.getImportPath()!=null) {
                nameString = formatPath(that.getImportPath().getIdentifiers());
            	name = getNameAsList(that.getImportPath());
            	node = that.getImportPath();
            }
            else if (that.getQuotedLiteral()!=null) {
                nameString = getNameString(that.getQuotedLiteral());
                name = asList(nameString.split("\\."));
                node = that.getQuotedLiteral();
            }
            else {
                nameString = "";
            	name = Collections.emptyList();
            	node = null;
            }
            if (name.isEmpty()) {
                that.addError("missing module name");
            }
            else if (name.get(0).equals(Module.DEFAULT_MODULE_NAME)) {
            	if (that.getImportPath()!=null) {
            		node.addError("reserved module name: 'default'");
            	}
            }
            else if (name.size()==1 && name.get(0).equals("ceylon")) {
                if (that.getImportPath()!=null) {
                    node.addError("reserved module name: 'ceylon'");
                }
            }
            else if (name.size()>1 && name.get(0).equals("ceylon")
                    && name.get(1).equals("language")) {
                if (that.getImportPath()!=null) {
                    node.addError("the language module is imported implicitly");
                }
            }
            else {
                Tree.AnnotationList al = that.getAnnotationList();
                String be = getNativeBackend(al, unit.getUnit());
                if (be != null) {
                    Backend backend = Backend.fromAnnotation(be);
                    if (backend == null) {
                        node.addError("illegal native backend name: '\"" + 
                                be + "\"' (must be either '\"jvm\"' or '\"js\"')");
                    } else if (moduleBackend != null && !moduleBackend.equals(be)) {
                        node.addError("native backend name on import conflicts with module descriptor: '\"" + 
                                be + "\"' is not '\"" + moduleBackend + "\"'");
                    }
                }
                Module importedModule = moduleManager.getOrCreateModule(name, version);
                if (that.getImportPath()!=null) {
                	that.getImportPath().setModel(importedModule);
                }
                if (!completeOnlyAST) {
                    if (mainModule != null) {
                        if (importedModule.getVersion() == null) {
                            importedModule.setVersion(version);
                        }
                        ModuleImport moduleImport = moduleManager.findImport(mainModule, importedModule);
                        if (moduleImport == null) {
                            boolean optional = hasAnnotation(al, "optional", unit.getUnit());
                            boolean export = hasAnnotation(al, "shared", unit.getUnit());
                            moduleImport = new ModuleImport(importedModule, optional, export, be);
                            moduleImport.getAnnotations().clear();
                            buildAnnotations(al, moduleImport.getAnnotations());
                            mainModule.addImport(moduleImport);
                        }
                        moduleManagerUtil.addModuleDependencyDefinition(moduleImport, that);
                    }
                }
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
    
    @Override
    public void visit(Tree.Import that) {
        super.visit(that);
        Tree.ImportPath path = that.getImportPath();
        if (path!=null && 
                formatPath(path.getIdentifiers()).equals(Module.LANGUAGE_MODULE_NAME)) {
            Tree.ImportMemberOrTypeList imtl = that.getImportMemberOrTypeList();
            if (imtl!=null) {
                for (Tree.ImportMemberOrType imt: imtl.getImportMemberOrTypes()) {
                    if (imt.getAlias()!=null && imt.getIdentifier()!=null) {
                        String name = name(imt.getIdentifier());
                        String alias = name(imt.getAlias().getIdentifier());
                        Map<String, String> mods = unit.getUnit().getModifiers();
                        if (mods.containsKey(name)) {
                            String curr = mods.get(alias);
                            if (curr!=null && curr.equals(alias)) {
                                mods.remove(alias);
                            }
                            mods.put(name, alias);
                        }
                    }
                }
            }
        }
    }
    
}
