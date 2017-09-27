package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static java.util.Collections.singleton;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AliasVisitor.typeList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.DecidabilityException;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.UnknownType;

/**
 * Detects and eliminates potentially undecidable 
 * supertypes, including:
 *  
 * - supertypes containing intersections in type arguments, 
 *   and
 * - supertypes with incorrect variance.
 * 
 * @author Gavin King
 *
 */
public class SupertypeVisitor extends Visitor {
    
    private boolean displayErrors;
    
    public SupertypeVisitor(boolean displayErrors) {
        this.displayErrors = displayErrors;
    }
    
    private boolean checkSupertypeVariance(Type type, 
            TypeDeclaration d, Node node) {
        List<TypeDeclaration> errors = 
                type.resolveAliases()
                    .checkDecidability();
        if (displayErrors) {
            for (TypeDeclaration td: errors) {
                Unit unit = node.getUnit();
                node.addError("type with contravariant type parameter '" + 
                        td.getName() + 
                        "' appears in contravariant or invariant location in supertype: '" +
                        type.asString(unit) + 
                        "'");
            }
        }
        return !errors.isEmpty();
    }

    private void checkForUndecidability(
            Tree.ExtendedType etn, 
            Tree.SatisfiedTypes stn, 
            TypeDeclaration type, 
            Tree.TypeDeclaration that) {
        boolean errors = false;
        
        if (stn!=null) {
            for (Tree.StaticType st: stn.getTypes()) {
                Type t = st.getTypeModel();
                if (t!=null) {
                    TypeDeclaration td = t.getDeclaration();
                    if (!(td instanceof UnknownType) &&
                        !(td instanceof TypeAlias)) {
                        if (td == type) {
                            brokenSatisfiedType(type, st, null);
                            errors = true;
                        }
                        else {
                            List<TypeDeclaration> list = 
                                    t.isRecursiveRawTypeDefinition(
                                            singleton(type));
                            if (!list.isEmpty()) {
                                brokenSatisfiedType(type, st, list);
                                errors = true;
                            }
                        }
                    }
                }
            }
        }
        if (etn!=null) {
            Tree.StaticType et = etn.getType();
            if (et!=null) {
                Type t = et.getTypeModel();
                if (t!=null) {
                    TypeDeclaration td = t.getDeclaration();
                    if (!(td instanceof UnknownType) &&
                        !(td instanceof TypeAlias)) {
                        if (td == type) {
                            brokenExtendedType(type, et, null);
                            errors = true;
                        }
                        else {
                            List<TypeDeclaration> list = 
                                    t.isRecursiveRawTypeDefinition(
                                            singleton(type));
                            if (!list.isEmpty()) {
                                brokenExtendedType(type, et, list);
                                errors = true;
                            }
                        }
                    }
                }
            }
        }
        
        if (!errors) {
            Unit unit = type.getUnit();
            List<Type> list = new ArrayList<Type>();
            try {
                List<Type> supertypes = 
                        type.getType()
                            .getSupertypes();
                for (Type st: supertypes) {
                    addToIntersection(list, st, unit);
                }
                //probably unnecessary - if it were 
                //going to blow up, it would have 
                //already blown up in addToIntersection()
                canonicalIntersection(list, unit);
            }
            catch (DecidabilityException re) {
                brokenHierarchy(type, that, unit);
                return;
            }
            
            try {
                type.getType().getUnionOfCases();
            }
            catch (DecidabilityException re) {
                brokenSelfType(type, that);
            }
            
            if (stn!=null) {
                for (Tree.StaticType st: stn.getTypes()) {
                    Type t = st.getTypeModel();
                    if (t!=null) {
                        if (checkSupertypeVariance(t, type, st)) {
                            type.getSatisfiedTypes().remove(t);
                            type.clearProducedTypeCache();
                        }
                    }
                }
            }
            if (etn!=null) {
                Tree.StaticType et = etn.getType();
                if (et!=null) {
                    Type t = et.getTypeModel();
                    if (t!=null) {
                        if (checkSupertypeVariance(t, type, et)) {
                            type.setExtendedType(unit.getBasicType());
                            type.clearProducedTypeCache();
                        }
                    }
                }
            }
        }
    }

    private void brokenSelfType(TypeDeclaration type, Tree.TypeDeclaration that) {
        if (displayErrors) {
            that.addError("inheritance hierarchy is undecidable: " + 
                    "coverage analysis did not terminate for '" +
                    type.getName() + "'");
        }
        type.getCaseTypes().clear();
        type.clearProducedTypeCache();
    }

    private void brokenHierarchy(TypeDeclaration type, 
            Tree.TypeDeclaration that, Unit unit) {
        if (displayErrors) {
            that.addError("inheritance hierarchy is undecidable: " + 
                    "could not canonicalize the intersection of all supertypes of '" +
                    type.getName() + "'");
        }
        type.getSatisfiedTypes().clear();
        type.setExtendedType(unit.getBasicType());
        type.clearProducedTypeCache();
    }

    private void brokenExtendedType(TypeDeclaration d, 
            Tree.StaticType et, List<TypeDeclaration> list) {
        if (displayErrors) {
            et.addError(message(d, list));
        }
        Type pt = et.getTypeModel();
        et.setTypeModel(null);
        d.setExtendedType(et.getUnit().getBasicType());
        d.addBrokenSupertype(pt);
        d.clearProducedTypeCache();
    }

    private void brokenSatisfiedType(TypeDeclaration d, 
            Tree.StaticType st, List<TypeDeclaration> list) {
        if (displayErrors) {
            st.addError(message(d, list));
        }
        Type pt = st.getTypeModel();
        st.setTypeModel(null);
        d.getSatisfiedTypes().remove(pt);
        d.addBrokenSupertype(pt);
        d.clearProducedTypeCache();
    }

    private String message(TypeDeclaration d, 
            List<TypeDeclaration> list) {
        return list==null ?
            "inheritance is circular: '" + 
                d.getName() + 
                "' inherits itself" :
            "inheritance is circular: definition of '" + 
                d.getName() + 
                "' is recursive, involving " + 
                typeList(list);
    }
    
    @Override 
    public void visit(Tree.ClassDefinition that) {
        super.visit(that);
        checkForUndecidability(that.getExtendedType(), 
                that.getSatisfiedTypes(), 
                that.getDeclarationModel(), 
                that);
    }

    @Override 
    public void visit(Tree.InterfaceDefinition that) {
        super.visit(that);
        checkForUndecidability(null, 
                that.getSatisfiedTypes(), 
                that.getDeclarationModel(), 
                that);
    }

    @Override 
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        checkForUndecidability(null, 
                that.getSatisfiedTypes(), 
                that.getDeclarationModel(), 
                that);
    }

}
