package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.Arrays;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detect and populate the list of imports for modules.
 * In theory should only be called on module.ceylon files
 *
 * Put retrictions on how module.ceylon files are built today:
 *  - names must be string literals or else the visitor cannot extract them
 *  - imports must be "explicitly defined, ie not imported as List<Import> or else the module names cannot be extracted
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImportVisitor extends Visitor {
    private final Context context;
    /**
     * are we truly in a module?
     */
    private boolean isModule = false;
    /**
     * Instance of the visited module which will receive
     * the dependencies declaration
     */
    private Module visitedModule;
    private final ModuleBuilder moduleBuilder;


    public ModuleImportVisitor(ModuleBuilder moduleBuilder, Context context) {
        this.moduleBuilder = moduleBuilder;
        this.context = context;
    }

    @Override
    public void visit(Tree.SimpleType that) {
        final Tree.Identifier identifier = that.getIdentifier();
        if ( identifier != null && "Module".equals( identifier.getText() ) ) {
            isModule = true;
        }
        super.visit(that); //is that right to call super after? I have no clue
    }

    @Override
    public void visit(Tree.SpecifiedArgument that) {
        //safety, if the object is not of type Module, ignore
        if (isModule) {
            final Tree.Identifier identifier = that.getIdentifier();
            if ( identifier != null && "name".equals( identifier.getText() ) ) {
                final Tree.SpecifierExpression specifierExpression = that.getSpecifierExpression();
                final Tree.Expression expression =
                        specifierExpression != null ? specifierExpression.getExpression() : null;
                final Tree.Term term = expression != null ? expression.getTerm() : null;
                String currentModuleName = term != null ? term.getText() : null;
                if ( currentModuleName == null ) {
                    that.getErrors().add( new AnalysisError(that, "Malformed module.ceylon: cannot extract module name") );
                    super.visit(that); //is that right to call super after? I have no clue
                    return;
                }

                if ( !currentModuleName.startsWith("'") || !currentModuleName.endsWith("'") ) {
                    that.getErrors().add( new AnalysisError(that, "Malformed module.ceylon: module names must be quoted identifiers") );
                    super.visit(that); //is that right to call super after? I have no clue
                    return;
                }
                final String[] splitModuleName = currentModuleName.substring(1,currentModuleName.length()-1).split("[\\.']");
                Module currentModule = moduleBuilder.getOrCreateModule(Arrays.asList(splitModuleName));
                //main module definition
                if (visitedModule == null) {
                    visitedModule = currentModule;
                }
                else {
                    visitedModule.getDependencies().add(currentModule);
                }
            }
        }
        super.visit(that); //is that right to call super after? I have no clue
    }
}

