package com.redhat.ceylon.compiler.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.model.Model;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private final CommonTree antlrTreeNode;
    private Model modelNode;
    private Scope scope;
    private Unit unit;
    private ProducedType typeModel;
    private List<AnalysisError> errors = new ArrayList<AnalysisError>();
    
    protected Node(CommonTree antlrTreeNode) {
        this.antlrTreeNode = antlrTreeNode; 
        text = antlrTreeNode.getText();
    }
    
    /**
     * The scope within which the node occurs. 
     */
    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
    
    /**
     * The type of this node. Note that many 
     * nodes do not have a type.
     */
    public ProducedType getTypeModel() {
        return typeModel;
    }
    
    public void setTypeModel(ProducedType type) {
        this.typeModel = type;
    }
    
    /**
     * The compilation unit in which the node
     * occurs.
     */
    public Unit getUnit() {
        return unit;
    }
    
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
     * The corresponding model object. Note that there may be no
     * corresponding model object, since the two data structures
     * are not isomorphic.
     */
    public Model getModelNode() {
        return modelNode;
    }

    public void setModelNode(Model modelNode) {
        this.modelNode = modelNode;
    }
    
    /**
     * The text of the corresponding ANTLR node.
     */
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * The text of the corresponding ANTLR tree node. Never null, 
     * since the two trees are isomorphic.
     */
    public CommonTree getAntlrTreeNode() {
        return antlrTreeNode;
    }
    
    
    /**
     * The compilation errors belonging to this node.
     */
    public List<AnalysisError> getErrors() {
        return errors;
    }
    
    public void addError(String message) {
        errors.add( new AnalysisError(this, message) );
    }
    
    public abstract void visitChildren(Visitor visitor);
    
    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        PrintVisitor pv = new PrintVisitor(w);
        pv.visitAny(this);
        return w.toString();
        //return getClass().getSimpleName() + "(" + text + ")"; 
    }
}
