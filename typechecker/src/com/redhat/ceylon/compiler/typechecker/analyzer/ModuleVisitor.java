package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifiedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detect and populate the list of imports for modules.
 * In theory should only be called on module.ceylon and
 * package.ceylon files
 *
 * Put retrictions on how module.ceylon files are built today:
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
    private final ModuleBuilder moduleBuilder;
    private final Package pkg;

    public ModuleVisitor(ModuleBuilder moduleBuilder, Package pkg) {
        this.moduleBuilder = moduleBuilder;
        this.pkg = pkg;
    }
    
    @Override
    public void visit(Tree.InvocationExpression that) {
        Tree.Primary p = that.getPrimary();
        if (p instanceof Tree.BaseTypeExpression) {
            Identifier id = ((BaseTypeExpression) p).getIdentifier();
            if (id!=null) {
                if (id.getText().equals("Module")) {
                    String moduleName = parseArgument(that, "name");
                    if (moduleName==null) {
                    	that.addError("missing module name");
                    }
                    else {
                        mainModule = pkg.getModule();
                        if ( !mainModule.getNameAsString().equals(moduleName) ) {
                        	that.addError("module name does not match descriptor location");
                        }
                        mainModule.setDoc(parseArgument(that, "doc"));
                        mainModule.setLicense(parseArgument(that, "license"));
                        mainModule.setVersion(parseArgument(that, "version"));
                    }
                }
                if (id.getText().equals("Import")) {
                    String moduleName = parseArgument(that, "name");
                    if (moduleName==null) {
                    	that.addError("missing imported module name");
                    }
                    else {
                        //TODO: do something with the specified version number!
                        Module importedModule = moduleBuilder.getOrCreateModule(splitModuleName(moduleName));
                        mainModule.getDependencies().add(importedModule);
                    }
                }
                if (id.getText().equals("Package")) {
                    String packageName = parseArgument(that, "name");
                    if (packageName==null) {
                    	that.addError("missing package name");
                    }
                    else {
                        if ( !pkg.getNameAsString().equals(packageName) ) {
                        	that.addError("package name does not match descriptor location");
                        }
                        pkg.setDoc(parseArgument(that, "doc"));
                    }
                }
            }
        }
        super.visit(that);
    }

	private static List<String> splitModuleName(String moduleName) {
		return Arrays.asList(moduleName.split("[\\.]"));
	}

    private String parseArgument(Tree.InvocationExpression that, String name) {
        for (Tree.NamedArgument arg: that.getNamedArgumentList().getNamedArguments()) {
            if (arg instanceof Tree.SpecifiedArgument) {
                Tree.Identifier aid = arg.getIdentifier();
                if (aid!=null && aid.getText().equals(name)) {
                    SpecifiedArgument sa = (Tree.SpecifiedArgument) arg;
                    Tree.SpecifierExpression se = sa.getSpecifierExpression();
                    if (se!=null && se.getExpression()!=null && 
                                se.getExpression().getTerm() instanceof Tree.Literal) {
                        String text = se.getExpression().getTerm().getText();
                        if (text.length()>=2 &&
                                (text.startsWith("'") && text.endsWith("'") || 
                                text.startsWith("\"") && text.endsWith("\"")) ) {
                            return text.substring(1, text.length()-1);
                        }
                    }
                }
            }
        }
        return null;
    }

}
