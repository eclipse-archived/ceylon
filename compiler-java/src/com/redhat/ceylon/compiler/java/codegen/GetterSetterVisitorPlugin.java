package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeGetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TypedDeclaration;

public class GetterSetterVisitorPlugin extends VisitorPlugin {

    @Override
    public void visit(AttributeGetterDefinition that) {
        context.getScope().userData.put(keyName(that), that);
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        AttributeGetterDefinition getter = (AttributeGetterDefinition) context.getScope().userData.get(keyName(that));
        if (getter != null) {
            getter.setSetter(that);
        }
    }
    
    private String keyName(TypedDeclaration decl) {
        return "getter_" + decl.getIdentifier().getText();
    }
}
