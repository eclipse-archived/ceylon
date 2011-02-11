package com.redhat.ceylon.compiler.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.model.Class;
import com.redhat.ceylon.compiler.model.Import;
import com.redhat.ceylon.compiler.model.Module;
import com.redhat.ceylon.compiler.model.Package;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.model.Unit;
import com.redhat.ceylon.compiler.tree.Node;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.Alias;
import com.redhat.ceylon.compiler.tree.Tree.Identifier;
import com.redhat.ceylon.compiler.tree.Visitor;
import com.redhat.ceylon.compiler.util.PrintUtil;

public class TypeVisitor extends Visitor {
    
    Unit unit;
    
    Type outerType;
    
    Package importPackage;
    
    public TypeVisitor(Unit cu) {
        unit = cu;
    }
    
    @Override
    public void visit(Tree.ImportPath that) {
        importPackage = getPackage(that.getIdentifiers());
    }
    
    Package getPackage(List<Identifier> importPath) {
        Module m = unit.getPackage().getModule();
        for (Package mp: m.getAllPackages()) {
            if ( hasName(importPath, mp) ) {
                return mp;
            }
        }
        throw new RuntimeException( "Package not found: " + 
                PrintUtil.importNodeToString(importPath) );
    }

    private boolean hasName(List<Identifier> importPath, Package mp) {
        if (mp.getName().size()==importPath.size()) {
            for (int i=0; i<mp.getName().size(); i++) {
                if (!mp.getName().get(i).equals(importPath.get(i).getText())) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void visit(Tree.ImportMemberOrType that) {
        Import i = new Import();
        Alias alias = that.getAlias();
        if (alias==null) {
            i.setAlias(that.getIdentifier().getText());
        }
        else {
            i.setAlias(alias.getIdentifier().getText());
        }
        i.setDeclaration( Util.getDeclaration(importPackage, 
                    that.getIdentifier()) );
        unit.getImports().add(i);
    }
        
    @Override
    public void visit(Tree.AnyAttributeDeclaration that) {
        super.visit(that);
        setType(that, that.getTypeOrSubtype());
    }

    @Override
    public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        setType(that, that.getTypeOrSubtype());
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        setType(that, that.getTypeOrSubtype());
    }
    
    private void setType(Node that, Tree.TypeOrSubtype type) {
        if (!(type instanceof Tree.LocalModifier)) { //if the type declaration is missing, we do type inference later
            Type t = (Type) type.getModelNode();
            ( (Typed) that.getModelNode() ).setType(t);
        }
    }
    
    @Override 
    public void visit(Tree.Type that) {
        Type type = new Type();
        that.setModelNode(type);
        type.setTreeNode(that);
        type.setGenericType( Util.getDeclaration(that) );
        //TODO: handle type arguments by substitution
        that.setTypeModel(type);
        if (outerType!=null) {
            outerType.getTypeArguments().add(type);
        }
        Type o = outerType;
        outerType = type;
        super.visit(that);
        outerType = o;
    }
    
    @Override 
    public void visit(Tree.VoidModifier that) {
        Type type = new Type();
        that.setModelNode(type);
        type.setTreeNode(that);
        //TODO: use the Void from the language package!
        Class c = new Class();
        c.setName("Void");
        type.setGenericType(c);
        that.setTypeModel(type);
    }

    /**
     * Suppress resolution of types that appear after the
     * member selection operator "."
     */
    @Override
    public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);            
    }
    
}
