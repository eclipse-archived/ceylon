package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AliasVisitor.typeList;
import static java.util.Collections.singleton;

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
        boolean result = false;
        TypeDeclaration atd = at.getDeclaration();
        if (atd instanceof TypeAlias) {
            ProducedType et = atd.getExtendedType();
            if (!et.isRecursiveTypeAliasDefinition(singleton(atd)).isEmpty()) {
                //TODO: we could get rid of this check if
                //      we introduced and additional
                //      compilation phase to separate
                //      SupertypeVisitor/AliasVisitor
                node.addError("supertype contains reference to circularly defined type alias: " +
                    atd.getName() + " is circular");
                result = true;
            }
            else if (isUndecidableSupertype(et, td, node)) {
                result = true;
            }
        }
        else if (atd instanceof IntersectionType) {
            for (ProducedType st: atd.getSatisfiedTypes()) {
                List<TypeDeclaration> l = st.isRecursiveTypeDefinition(singleton(td));
                if (!l.isEmpty()) {
                    node.addError("supertype contains recursion within intersection: type " +
                            st.getProducedTypeName(node.getUnit()) + " in intersection " + 
                            at.getProducedTypeName(node.getUnit()) + " involves " +
                            typeList(l));
                    result = true;
                }
                else if (isUndecidableSupertype(st, td, node)) {
                    result = true;
                }
            }
        }
        else if (atd instanceof UnionType) {
            for (ProducedType ct: atd.getCaseTypes()) {
                List<TypeDeclaration> l = ct.isRecursiveTypeDefinition(singleton(td));
                if (!l.isEmpty()) {
                    node.addError("supertype contains recursion within union: type " +
                            ct.getProducedTypeName(node.getUnit()) + " in union " + 
                            at.getProducedTypeName(node.getUnit()) + " involves " +
                            typeList(l));
                    result = true;
                }
                else if (isUndecidableSupertype(ct, td, node)) {
                    result = true;
                }
            }
        }
        else {
            for (ProducedType t: at.getTypeArgumentList()) {
                if (isUndecidableSupertype(t, td, node)) {
                    result = true;
                    break;
                }
            }
            ProducedType qt = at.getQualifyingType();
            if (qt!=null && isUndecidableSupertype(qt, td, node)) {
                result = true;
            }
        }
        return result;
    }

    private boolean checkSupertypeVariance(ProducedType type, TypeDeclaration d, Node node) {
        List<TypeDeclaration> errors = type.resolveAliases().checkDecidability();
        for (TypeDeclaration td: errors) {
            node.addError("type with contravariant type parameter " + td.getName() + 
                    " appears in contravariant location in supertype: " + 
                    type.getProducedTypeName(node.getUnit()));
        }
        return !errors.isEmpty();
    }

    private void checkForUndecidability(Tree.SatisfiedTypes that, TypeDeclaration d) {
        if (that!=null) {
            for (Tree.StaticType st: that.getTypes()) {
                ProducedType t = st.getTypeModel();
                if (t!=null) {
                    List<TypeDeclaration> l = t.isRecursiveRawTypeDefinition(singleton(d));
                    if (!l.isEmpty()) {
                        that.addError("inheritance is circular: definition of " + 
                                d.getName() + " is recursive, involving " + typeList(l));
                        d.getSatisfiedTypes().remove(t);
                        d.getBrokenSupertypes().add(t);
                    }
                    else if (isUndecidableSupertype(t, d, st)) {
                        d.getSatisfiedTypes().remove(t);
                    }
                    else if (checkSupertypeVariance(t, d, st)) {
                        d.getSatisfiedTypes().remove(t);
                    }
                }
            }
        }
    }

    private void checkForUndecidability(Tree.ExtendedType that, Class d) {
        if (that!=null) {
            Tree.StaticType et = that.getType();
            ProducedType t = et.getTypeModel();
            if (t!=null) {
                List<TypeDeclaration> l = t.isRecursiveRawTypeDefinition(singleton((TypeDeclaration)d));
                if (!l.isEmpty()) {
                    that.addError("inheritance is circular: definition of " + 
                            d.getName() + " is recursive, involving " + typeList(l));
                    d.setExtendedType(new UnknownType(that.getUnit()).getType());
                    d.getBrokenSupertypes().add(t);
                }
                else if (isUndecidableSupertype(t, d, et)) {
                    d.setExtendedType(new UnknownType(that.getUnit()).getType());
                }
                else if (checkSupertypeVariance(t, d, et)) {
                    d.getSatisfiedTypes().remove(t);
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
        checkForUndecidability(that.getExtendedType(), that.getDeclarationModel());
    }

    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
    }

    @Override 
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        checkForUndecidability(that.getSatisfiedTypes(), that.getDeclarationModel());
    }

}
