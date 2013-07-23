package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detects and eliminates potentially undecidable 
 * supertypes, including: 
 * - supertypes containing intersections in type 
 *   arguments, and
 * - supertypes with incorrect variance.
 * 
 * @author Gavin King
 *
 */
public class SupertypeVisitor extends Visitor {

    private static boolean isUndecidableSupertype(ProducedType at,
            TypeDeclaration td, Node node) {
        if (at==null) return false;
        TypeDeclaration atd = at.getDeclaration();
        if (atd instanceof TypeAlias) {
            ProducedType et = atd.getExtendedType();
            if (et.isRecursiveTypeAliasDefinition(atd)) {
                //TODO: we could get rid of this check if
                //      we introduced and additional
                //      compilation phase to separate
                //      SupertypeVisitor/AliasVisitor
                node.addError("supertype contains reference to circularly defined type alias");
                return true;
            }
            else if (isUndecidableSupertype(et, td, node)) {
                return true;
            }
        }
        else if (atd instanceof IntersectionType) {
            for (ProducedType st: atd.getSatisfiedTypes()) {
                if (st.isRecursiveTypeDefinition(td)) {
                    node.addError("supertype contains recursion within intersection: " +
                            st.getProducedTypeName(node.getUnit()));
                    return true;
                }
                if (isUndecidableSupertype(st, td, node)) {
                    return true;
                }
            }
        }
        else if (atd instanceof UnionType) {
            for (ProducedType ct: atd.getCaseTypes()) {
                if (ct.isRecursiveTypeDefinition(td)) {
                    node.addError("supertype contains recursion within union: " +
                            ct.getProducedTypeName(node.getUnit()));
                    return true;
                }
                if (isUndecidableSupertype(ct, td, node)) {
                    return true;
                }
            }
        }
        else {
            for (ProducedType t: at.getTypeArgumentList()) {
                if (isUndecidableSupertype(t, td, node)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkSupertypeVariance(Tree.SatisfiedTypes that, TypeDeclaration d) {
        if (that!=null) {
            int i=0;
            for (Tree.StaticType st: that.getTypes()) {
                ProducedType type = st.getTypeModel();
                if (type.isRecursiveTypeAliasDefinition(d)) {
                    //TODO: we could get rid of this check if
                    //      we introduced and additional
                    //      compilation phase to separate
                    //      SupertypeVisitor/AliasVisitor
                    st.addError("supertype contains reference to circularly defined type alias");
                }
                else {
                    List<TypeDeclaration> errors = type.resolveAliases().checkDecidability();
                    for (TypeDeclaration td: errors) {
                        that.addError("type with contravariant type parameter " + td.getName() + 
                                " appears in contravariant location in satisfied type: " + 
                                type.getProducedTypeName(that.getUnit()));
                    }
                    if (!errors.isEmpty()) {
                        d.getSatisfiedTypes().set(i, new UnknownType(that.getUnit()).getType());
                    }
                    i++;
                }
            }
        }
    }

    private void checkSupertypeVariance(Tree.ExtendedType that, Class d) {
        if (that!=null) {
            Tree.StaticType et = that.getType();
            ProducedType type = et.getTypeModel();
            if (type.isRecursiveTypeAliasDefinition(d)) {
                //TODO: we could get rid of this check if
                //      we introduced and additional
                //      compilation phase to separate
                //      SupertypeVisitor/AliasVisitor
                et.addError("supertype contains reference to circularly defined type alias");
            }
            else {
                List<TypeDeclaration> errors = type.resolveAliases().checkDecidability();
                for (TypeDeclaration td: errors) {
                    that.addError("type with contravariant type parameter " + td.getName() + 
                            " appears in contravariant location in extended type: " + 
                            type.getProducedTypeName(that.getUnit()));
                }
                if (!errors.isEmpty()) {
                    d.setExtendedType(new UnknownType(that.getUnit()).getType());
                }
            }
        }
    }

    private void checkForUndecidability(Tree.SatisfiedTypes that, TypeDeclaration d) {
        if (that!=null) {
            int i=0;
            for (Tree.StaticType st: that.getTypes()) {
                ProducedType t = st.getTypeModel();
                if (t!=null && t.isRecursiveRawTypeDefinition(d)) {
//                    st.addError("supertype is circularly defined");
                    d.getSatisfiedTypes().set(i, new UnknownType(that.getUnit()).getType());
                    d.getBrokenSupertypes().add(t.getDeclaration());
                }
                if (isUndecidableSupertype(t, d, st)) {
                    d.getSatisfiedTypes().set(i, new UnknownType(that.getUnit()).getType());
                }
                i++;
            }
        }
    }

    private void checkForUndecidability(Tree.ExtendedType that, Class d) {
        if (that!=null) {
            Tree.StaticType et = that.getType();
            ProducedType t = et.getTypeModel();
            if (t!=null && t.isRecursiveRawTypeDefinition(d)) {
//                et.addError("supertype is circularly defined");
                d.setExtendedType(new UnknownType(that.getUnit()).getType());
                d.getBrokenSupertypes().add(t.getDeclaration());
            }
            if (isUndecidableSupertype(t, d, et)) {
                d.setExtendedType(new UnknownType(that.getUnit()).getType());
            }
        }
    }
    
    @Override 
    public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
        checkForUndecidability(that.getExtendedType(), that.getDeclarationModel());
        checkSupertypeVariance(that.getExtendedType(), that.getDeclarationModel());
        checkSupertypeVariance(that.getSatisfiedTypes(), that.getDeclarationModel());
    }

    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
        checkSupertypeVariance(that.getSatisfiedTypes(), that.getDeclarationModel());
    }

    @Override 
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
        checkSupertypeVariance(that.getSatisfiedTypes(), that.getDeclarationModel());
    }

}
