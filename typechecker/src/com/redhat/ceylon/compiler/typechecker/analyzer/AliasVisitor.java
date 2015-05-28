package com.redhat.ceylon.compiler.typechecker.analyzer;

import static java.util.Collections.singleton;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TupleType;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;

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
            List<TypeDeclaration> list = 
                    t.isRecursiveTypeAliasDefinition(singleton(d));
            if (!list.isEmpty()) {
                that.addError("type alias is circular: definition of '" + 
                        d.getName() + 
                        "' is recursive, involving " + 
                        typeList(list));
                //to avoid stack overflows, throw 
                //away the recursive definition:
                d.setExtendedType(that.getUnit().getUnknownType());
                d.addBrokenSupertype(t);
            }
        }
    }

    public static String typeList(List<TypeDeclaration> list) {
        StringBuffer sb = new StringBuffer();
        for (TypeDeclaration td: list) {
            sb.append("'").append(td.getName()).append("', ");
        }
        sb.setLength(sb.length()-2);
        return sb.toString();
    }

    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.ClassDeclaration that) {
        Tree.ClassSpecifier ts = that.getClassSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
        super.visit(that);
    }

    @Override
    public void visit(Tree.InterfaceDeclaration that) {
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            Tree.StaticType st = ts.getType();
            if (st!=null) {
                check(ts, st.getTypeModel(), 
                        that.getDeclarationModel());
            }
        }
        super.visit(that);
    }
    
    // Necessary in order to resolve a nasty bug #867 
    // resulting from caching of type aliases:
    
    @Override
    public void visit(TupleType that) {
        super.visit(that);
        that.setTypeModel(that.getTypeModel().resolveAliases());
    }
    
    @Override
    public void visit(Tree.FunctionType that) {
        super.visit(that);
        ProducedType rt = that.getTypeModel();
        if (rt!=null) {
            that.setTypeModel(rt.resolveAliases());
        }
    }
    
}
