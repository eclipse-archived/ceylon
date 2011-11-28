package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Identifier;
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
    private Tree.CompilationUnit unit;

    public ModuleVisitor(ModuleBuilder moduleBuilder, Package pkg) {
        this.moduleBuilder = moduleBuilder;
        this.pkg = pkg;
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
                if (id.getText().equals("Module")) {
                    Tree.SpecifiedArgument nsa = getArgument(that, "name");
                    String moduleName = argumentToString(nsa);
                    if (moduleName==null) {
                        unit.addError("missing module name");
                    }
                    else {
                        //mainModule = pkg.getModule();
                        mainModule = moduleBuilder.getOrCreateModule(pkg.getName()); //in compiler the Package has a null Module
                        if (mainModule == null) {
                            unit.addError("A module cannot be defined at the top level of the hierarchy");
                        }
                        else {
                            if ( !mainModule.getNameAsString().equals(moduleName) ) {
                                nsa.addError("module name does not match descriptor location");
                            }
                            moduleBuilder.addErrorsToModule(mainModule, unit);
                            mainModule.setDoc(argumentToString(getArgument(that, "doc")));
                            mainModule.setLicense(argumentToString(getArgument(that, "license")));
                            Tree.SpecifiedArgument vsa = getArgument(that, "version");
                            String version = argumentToString(vsa);
                            if (version==null) {
                                unit.addError("missing module version");
                            }
                            else {
                                if (version.isEmpty()) {
                                    vsa.addError("empty version identifier");
                                }
                                else {
                                    mainModule.setVersion(version);
                                }
                            }
                            List<String> by = argumentToStrings(getArgument(that, "by"));
                            if (by!=null) {
                                mainModule.getAuthors().addAll(by);
                            }
                        }
                    }
                }
                if (id.getText().equals("Import")) {
                    Tree.SpecifiedArgument nsa = getArgument(that, "name");
                    String moduleName = argumentToString(nsa);
                    if (moduleName==null) {
                        unit.addError("missing imported module name");
                    }
                    else {
                        //TODO: do something with the specified version number!
                        Module importedModule = moduleBuilder.getOrCreateModule(splitModuleName(moduleName));
                        if (importedModule == null) {
                            nsa.addError("A module cannot be defined at the top level of the hierarchy");
                        }
                        else {
                            if (!mainModule.getDependencies().contains(importedModule)) {
                                mainModule.getDependencies().add(importedModule);
                            }
                            moduleBuilder.addModuleDependencyDefinition(importedModule, nsa);
                            Tree.SpecifiedArgument vsa = getArgument(that, "version");
                            String version = argumentToString(vsa);
                            if (version.isEmpty()) {
                                vsa.addError("empty version identifier");
                            }
                            //TODO: this is wrong and temporary:
                            if (importedModule.getVersion() == null) {
                                importedModule.setVersion(version);
                            }
                        }
                    }
                }
                if (id.getText().equals("Package")) {
                    Tree.SpecifiedArgument nsa = getArgument(that, "name");
                    String packageName = argumentToString(nsa);
                    if (packageName==null) {
                        unit.addError("missing package name");
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
        }
        super.visit(that);
    }

    private static List<String> splitModuleName(String moduleName) {
        return Arrays.asList(moduleName.split("[\\.]"));
    }

    private Tree.SpecifiedArgument getArgument(Tree.InvocationExpression that, String name) {
        for (Tree.NamedArgument arg: that.getNamedArgumentList().getNamedArguments()) {
            if (arg instanceof Tree.SpecifiedArgument) {
                Tree.Identifier aid = arg.getIdentifier();
                if (aid!=null && aid.getText().equals(name)) {
                    return (Tree.SpecifiedArgument) arg;
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
            if ( term instanceof Tree.SequenceEnumeration) {
                List<String> result = new ArrayList<String>();
                ExpressionList el = ((Tree.SequenceEnumeration) term).getExpressionList();
                if (el!=null) {
                    for (Tree.Expression exp: el.getExpressions()) {
                        result.add(termToString(exp.getTerm()));
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
        else if ( term instanceof Tree.BaseMemberExpression) {
            return ((Tree.BaseMemberExpression) term).getIdentifier().getText();
        }
        else {
            return null;
        }
    }

}
