package com.redhat.ceylon.compiler.typechecker.analyzer;

import static java.util.Collections.singleton;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TupleType;

/**
 * Detects recursive type aliases and eliminates the
 * circularities.
 * 
 * @author Gavin King
 *
 */
public class AliasVisitor extends Visitor {

    private void check(Node that, ProducedType t, TypeDeclaration d) {
        if (t!=null) {
            List<TypeDeclaration> l = t.isRecursiveTypeAliasDefinition(singleton(d));
            if (!l.isEmpty()) {
                that.addError("type alias is circular: definition of " + 
                        d.getName() + " is recursive, involving " + typeList(l));
                //to avoid stack overflows, throw 
                //away the recursive definition:
                d.setExtendedType(new UnknownType(that.getUnit()).getType());
                d.getBrokenSupertypes().add(t);
            }
        }
    }

    public static String typeList(List<TypeDeclaration> l) {
        StringBuffer sb = new StringBuffer();
        for (TypeDeclaration td: l) {
            sb.append(td.getName()).append(", ");
        }
        sb.setLength(sb.length()-2);
        return sb.toString();
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        super.visit(that);
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
    }

    @Override
    public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Tree.ClassSpecifier ts = that.getClassSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
    }

    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        super.visit(that);
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
    }
    
    @Override
    public void visit(TupleType that) {
        super.visit(that);
        that.setTypeModel(that.getTypeModel().resolveAliases());
    }
    
    @Override
    public void visit(Tree.FunctionType that) {
        super.visit(that);
        that.setTypeModel(that.getTypeModel().resolveAliases());
    }
    
}
