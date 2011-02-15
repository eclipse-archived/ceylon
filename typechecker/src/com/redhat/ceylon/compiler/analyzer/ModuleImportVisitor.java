package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.context.Context;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Visitor;

import java.util.Arrays;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class ModuleImportVisitor extends Visitor {
    private Context context;
    private boolean isModule = false;
    private Module visitedModule;


    public ModuleImportVisitor(Context context) {
        this.context = context;
    }

    @Override
    public void visit(Tree.Type that) {
        if ( "Module".equals(that.getIdentifier().getText()) ) {
            isModule = true;
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.SpecifiedArgument that) {
        //safety, if the object is not of type Module, ignore
        if (isModule) {
            if ( "name".equals( that.getIdentifier().getText() ) ) {
                String currentModuleName = that.getSpecifierExpression().getExpression().getTerm().getText();
                if ( !currentModuleName.startsWith("'") || !currentModuleName.endsWith("'") ) {
                    throw new RuntimeException("Module names in module.ceylon files must be quoted identifiers");
                }
                final String[] splitModuleName = currentModuleName.substring(1,currentModuleName.length()-1).split("[\\.']");
                Module currentModule = context.getOrCreateModule(Arrays.asList(splitModuleName));
                //main module definition
                if (visitedModule == null) {
                    visitedModule = currentModule;
                }
                else {
                    visitedModule.getDependencies().add(currentModule);
                }
            }
        }
        super.visit(that);
    }
}

