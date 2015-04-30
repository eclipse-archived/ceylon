package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AliasVisitor.typeList;
import static com.redhat.ceylon.model.typechecker.model.Util.addToIntersection;
import static java.util.Collections.singleton;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.IntersectionType;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Unit;

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
    
    private boolean displayErrors;
    
    public SupertypeVisitor(boolean displayErrors) {
        this.displayErrors = displayErrors;
    }
    
    private boolean checkSupertypeVariance(ProducedType type, 
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
                        type.getProducedTypeName(unit) + 
                        "'");
            }
        }
        return !errors.isEmpty();
    }

    private void checkForUndecidability(Tree.ExtendedType etn, 
            Tree.SatisfiedTypes stn, TypeDeclaration d, 
            Tree.TypeDeclaration that) {
        boolean errors = false;
        if (stn!=null) {
            for (Tree.StaticType st: stn.getTypes()) {
                ProducedType t = st.getTypeModel();
                if (t!=null) {
                    List<TypeDeclaration> list = 
                            t.isRecursiveRawTypeDefinition(
                                    singleton(d));
                    if (!list.isEmpty()) {
                        if (displayErrors) {
                            stn.addError("inheritance is circular: definition of '" + 
                                    d.getName() + 
                                    "' is recursive, involving " + 
                                    typeList(list));
                        }
                        d.getSatisfiedTypes().remove(t);
                        d.addBrokenSupertype(t);
                        d.clearProducedTypeCache();
                        errors = true;
                    }
                }
            }
        }
        Unit unit = d.getUnit();
        Class bd = unit.getBasicDeclaration();
        if (etn!=null) {
            Tree.StaticType et = etn.getType();
            if (et!=null) {
            	ProducedType t = et.getTypeModel();
            	if (t!=null) {
            		List<TypeDeclaration> list = 
            		        t.isRecursiveRawTypeDefinition(
            		                singleton(d));
            		if (!list.isEmpty()) {
            	        if (displayErrors) {
                			etn.addError("inheritance is circular: definition of '" + 
                					d.getName() + 
                					"' is recursive, involving " + 
                					typeList(list));
            	        }
            			d.setExtendedType(unit.getType(bd));
            			d.addBrokenSupertype(t);
                        d.clearProducedTypeCache();
            			errors = true;
            		}
            	}
            }
        }
        if (!errors) {
            List<ProducedType> list = 
                    new ArrayList<ProducedType>();
            try {
                for (ProducedType st: 
                        d.getType().getSupertypes()) {
                    addToIntersection(list, st, unit);
                }
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(list);
				it.canonicalize().getType();
            }
            catch (RuntimeException re) {
                if (displayErrors) {
                    that.addError("inheritance hierarchy is undecidable: " + 
                            "could not canonicalize the intersection of all supertypes of '" +
                            d.getName() + 
                            "'");
                }
                d.getSatisfiedTypes().clear();
                d.setExtendedType(unit.getType(bd));
                d.clearProducedTypeCache();
                return;
            }
            if (stn!=null) {
                for (Tree.StaticType st: stn.getTypes()) {
                    ProducedType t = st.getTypeModel();
                    if (t!=null) {
                        if (checkSupertypeVariance(t, d, st)) {
                            d.getSatisfiedTypes().remove(t);
                            d.clearProducedTypeCache();
                        }
                    }
                }
            }
            if (etn!=null) {
                Tree.StaticType et = etn.getType();
                if (et!=null) {
                	ProducedType t = et.getTypeModel();
                	if (t!=null) {
                		if (checkSupertypeVariance(t, d, et)) {
                			d.getSatisfiedTypes().remove(t);
                            d.clearProducedTypeCache();
                		}
                	}
                }
            }
        }
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
