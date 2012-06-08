package com.redhat.ceylon.compiler.typechecker.analyzer;

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
    
    //TODO: we need to add *much* more validation of the
    //      format of the descriptor
    
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
    public void visit(Tree.InvocationExpression that) {
        Tree.Primary p = that.getPrimary();
        if (p instanceof Tree.BaseTypeExpression) {
            Identifier id = ((BaseTypeExpression) p).getIdentifier();
            if (id!=null) {
                switch (phase) {
                    case SRC_MODULE:
                        visitForSrcModulePhase(that,id);
                        break;
                    case REMAINING:
                        visitForRemainingPhase(that,id);
                        break;
                }
            }
        }
        super.visit(that);
    }

    private void visitForSrcModulePhase(Tree.InvocationExpression that, Identifier id) {
        if (id.getText().equals("Module")) {
            Tree.SpecifiedArgument nsa = getArgument(that, "name");
            String moduleName = argumentToString(nsa);
            if (moduleName==null) {
                that.addError("missing module name");
            }
            else if (moduleName.startsWith(Module.DEFAULT_MODULE_NAME)) {
                that.addError("default is a reserved module name");
            }
            else {
                Tree.SpecifiedArgument vsa = getArgument(that, "version");
                if (vsa!=null) {
                    String version = argumentToString(vsa);
                    if (version==null) {
                        that.addError("missing module version");
                    }
                    else {
                        if (version.isEmpty()) {
                            vsa.addError("empty version identifier");
                        }
                    }
                    //mainModule = pkg.getModule();
                    mainModule = moduleManager.getOrCreateModule(pkg.getName(),version); //in compiler the Package has a null Module
                    if (mainModule == null) {
                        nsa.addError("module must have a nonempty name");
                    }
                    else {
                        mainModule.setVersion(version);
                        if ( !mainModule.getNameAsString().equals(moduleName) ) {
                            nsa.addError("module name does not match descriptor location");
                        }
                        moduleManager.addLinkBetweenModuleAndNode(mainModule, unit);
                        mainModule.setDoc(argumentToString(getArgument(that, "doc")));
                        mainModule.setLicense(argumentToString(getArgument(that, "license")));
                        List<String> by = argumentToStrings(getArgument(that, "by"));
                        if (by!=null) {
                            mainModule.getAuthors().addAll(by);
                        }
                        mainModule.setAvailable(true);
                    }
                }
            }
        }
    }

    private void visitForRemainingPhase(Tree.InvocationExpression that, Identifier id) {
        if (id.getText().equals("Import")) {
            Tree.SpecifiedArgument nsa = getArgument(that, "name");
            String moduleName = argumentToString(nsa);
            if (moduleName==null) {
                that.addError("missing imported module name");
            }
            else {
                Tree.SpecifiedArgument vsa = getArgument(that, "version");
                if (vsa!=null) {
                    String version = argumentToString(vsa);
                    if (version.isEmpty()) {
                        vsa.addError("empty version identifier");
                    }
                    Module importedModule = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(moduleName),version);
                    if (importedModule == null) {
                        nsa.addError("module must have a nonempty name");
                    }
                    else if (mainModule != null) {
                        if (importedModule.getVersion() == null) {
                            importedModule.setVersion(version);
                        }
                        String optionalString = argumentToString(getArgument(that, "optional"));
                        String exportString = argumentToString(getArgument(that, "export"));
                        ModuleImport moduleImport = moduleManager.findImport(mainModule, importedModule);
                        if (moduleImport == null) {
                            boolean optional = optionalString!=null && optionalString.equals("true");
                            boolean export = exportString!=null && exportString.equals("true");
                            moduleImport = new ModuleImport(importedModule, optional, export);
                            mainModule.getImports().add(moduleImport);
                        }
                        moduleManager.addModuleDependencyDefinition(moduleImport, nsa);
                    }
                    //else we leave it behind unprocessed
                }
            }
        }
        if (id.getText().equals("Package")) {
            Tree.SpecifiedArgument nsa = getArgument(that, "name");
            String packageName = argumentToString(nsa);
            if (packageName==null) {
                that.addError("missing package name");
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
        }
    }

    private Tree.SpecifiedArgument getArgument(Tree.InvocationExpression that, String name) {
        NamedArgumentList nal = that.getNamedArgumentList();
        if (nal==null) {
        	that.addError("module and package descriptors must be defined using named argument lists");
        }
        else {
			for (Tree.NamedArgument arg: nal.getNamedArguments()) {
	            if (arg instanceof Tree.SpecifiedArgument) {
	                Tree.Identifier aid = arg.getIdentifier();
	                if (aid!=null && aid.getText().equals(name)) {
	                    return (Tree.SpecifiedArgument) arg;
	                }
	            }
	        }
        }
        return null;
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
