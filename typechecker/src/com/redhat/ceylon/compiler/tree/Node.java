package com.redhat.ceylon.compiler.tree;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.tree.CommonTree;

import com.redhat.ceylon.compiler.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.model.Declaration;
import com.redhat.ceylon.compiler.model.ProducedReference;
import com.redhat.ceylon.compiler.model.ProducedType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.util.PrintVisitor;

public abstract class Node {
    
    private String text;
    private final CommonTree antlrTreeNode;
    private Declaration declarationModel;
    private Scope scope;
    private Unit unit;
    private ProducedType typeModel;
    private ProducedReference memberReference;
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
     * The model object representing the type of this 
     * node. Note that many nodes do not have a type.
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
     * The model object representing the declaration
     * introduced by this node. Note that most nodes
     * are not declarations.
     */
    public Declaration getDeclarationModel() {
        return declarationModel;
    }

    public void setDeclarationModel(Declaration modelNode) {
        this.declarationModel = modelNode;
    }
    
    /**
     * The member reference of a primary expression. This 
     * will be null for nodes which do not refer to a member 
     * or type.
     */
    public ProducedReference getMemberReference() {
        return memberReference;
    }
    
    public void setMemberReference(ProducedReference memberReference) {
        this.memberReference = memberReference;
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
