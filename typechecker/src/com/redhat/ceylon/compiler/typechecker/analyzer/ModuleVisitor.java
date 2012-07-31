package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.buildAnnotations;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.formatPath;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasAnnotation;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgumentList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SequencedArgument;
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
    
    private String getVersionString(Tree.QuotedLiteral that) {
        return that==null ? null : that.getText()
                .substring(1, that.getText().length() - 1);
    }
    
    @Override
    public void visit(Tree.ModuleDescriptor that) {
        super.visit(that);
        if (phase==Phase.SRC_MODULE) {
            String version = getVersionString(that.getVersion());
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
                buildAnnotations(that.getAnnotationList(), mainModule.getAnnotations());
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
                buildAnnotations(that.getAnnotationList(), pkg.getAnnotations());
            }
        }
    }
    
    @Override
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        if (phase==Phase.REMAINING) {
            String version = getVersionString(that.getVersion());
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
                    ModuleImport moduleImport = moduleManager.findImport(mainModule, importedModule);
                    if (moduleImport == null) {
                        boolean optional = hasAnnotation(that.getAnnotationList(), "optional");
                        boolean export = hasAnnotation(that.getAnnotationList(), "export");
                        moduleImport = new ModuleImport(importedModule, optional, export);
                        buildAnnotations(that.getAnnotationList(), moduleImport.getAnnotations());
                        mainModule.getImports().add(moduleImport);
                    }
                    moduleManager.addModuleDependencyDefinition(moduleImport, that);
                }
            }
        }
        //this is from master, who knows what it does?
        /*if (id.getText().equals("Package")) {
            Tree.SpecifiedArgument nsa = getArgument(that, "name");
            String packageName = argumentToString(nsa);
            if (packageName==null) {
                that.addError("missing package name");
            }
            if (packageName.isEmpty()) {
                nsa.addError("empty package name");
            }
            else if (packageName.startsWith(Module.DEFAULT_MODULE_NAME)) {
                nsa.addError("default is a reserved package name");
            }
            else if (pkg.getName().isEmpty()) {
                that.addError("package descriptor may not be defined in the root source directory");
            }
            else {
                if ( !pkg.getNameAsString().equals(packageName) ) {
                    nsa.addError("package name does not match descriptor location");
                }
                pkg.setDoc(argumentToString(getArgument(that, "doc")));
                String shared = argumentToString(getArgument(that, "shared"));
                if (shared!=null && shared.equals("true")) {
                    pkg.setShared(true);
                }
                List<String> by = argumentToStrings(getArgument(that, "by"));
                if (by!=null) {
                    pkg.getAuthors().addAll(by);
                }
            }
        }*/
    }

    private List<String> getNameAsList(Tree.ImportPath that) {
        List<String> name = new ArrayList<String>();
        for (Tree.Identifier i: that.getIdentifiers()) {
           name.add(i.getText()); 
        }
        return name;
    }
    
    private List<String> argumentToStrings(Tree.SpecifiedArgument sa) {
        if (sa==null) return null;
        Tree.SpecifierExpression se = sa.getSpecifierExpression();
        if (se!=null && se.getExpression()!=null) {
            Tree.Term term = se.getExpression().getTerm();
            if (term instanceof Tree.SequenceEnumeration) {
                List<String> result = new ArrayList<String>();
                SequencedArgument sqa = ((Tree.SequenceEnumeration) term).getSequencedArgument();
                if (sqa!=null) {
                    ExpressionList el = sqa.getExpressionList();
                    if (el!=null) {
                        for (Tree.Expression exp: el.getExpressions()) {
                            result.add(termToString(exp.getTerm()));
                        }
                    }
                }
                return result;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    private String argumentToString(Tree.SpecifiedArgument sa) {
        if (sa==null) return null;
        Tree.SpecifierExpression se = sa.getSpecifierExpression();
        if (se!=null && se.getExpression()!=null) {
            Tree.Term term = se.getExpression().getTerm();
            return termToString(term);
        }
        else {
            return null;
        }
    }

    private String termToString(Tree.Term term) {
        if (term instanceof Tree.Literal) {
            String text = term.getText();
            if (text.length()>=2 &&
                    (text.startsWith("'") && text.endsWith("'") || 
                    text.startsWith("\"") && text.endsWith("\"")) ) {
                return text.substring(1, text.length()-1);
            }
            else {
                return text;
            }
        }
        else if (term instanceof Tree.BaseMemberExpression) {
            return ((Tree.BaseMemberExpression) term).getIdentifier().getText();
        }
        else {
        	term.addError("module and package descriptors must be defined using literal value expressions");
            return null;
        }
    }
    public enum Phase {
        SRC_MODULE,
        REMAINING
    }
    
    public Module getMainModule() {
        return mainModule;
    }
    
}
