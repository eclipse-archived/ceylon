package com.redhat.ceylon.compiler.codegen;

import java.util.ArrayList;
import java.util.HashMap;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

public class ToplevelAttributesDefinitionBuilder {
    private CeylonTransformer gen;
    private ArrayList<Tree.AttributeGetterDefinition> getters;
    private HashMap<String, Tree.AttributeSetterDefinition> setters;
    
    
    public ToplevelAttributesDefinitionBuilder(CeylonTransformer gen) {
        this.gen = gen;
        getters = new ArrayList<Tree.AttributeGetterDefinition>();
        setters = new HashMap<String, Tree.AttributeSetterDefinition>();
    }

    public void add(Tree.AttributeGetterDefinition decl) {
        getters.add(decl);
    }
    
    public void add(Tree.AttributeSetterDefinition decl) {
        String attrName = decl.getIdentifier().getText();
        setters.put(attrName, decl);
    }
    
    public ListBuffer<JCTree> build() {
        ListBuffer<JCTree> result = ListBuffer.lb();
        for (Tree.AttributeGetterDefinition getter : getters) {
            boolean annots = gen.checkCompilerAnnotations(getter);
            String attrName = getter.getIdentifier().getText();
            Tree.AttributeSetterDefinition setter = setters.get(attrName);
            result.appendList(gen.transformAttribute(getter, setter));
            gen.resetCompilerAnnotations(annots);
        }
        return result;
    }
}
