package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Detects recursive type aliases and potentially 
 * undecidable supertypes (supertypes containing
 * intersections in arguments to invariant type
 * parameters).
 * 
 * @author Gavin King
 *
 */
public class AliasVisitor extends Visitor {
	
	private void check(Node that, ProducedType t, TypeDeclaration d) {
		if (t!=null && t.isRecursiveTypeAliasDefinition(d)) {
			that.addError("circular definition of type alias: " + d.getName());
			//to avoid stack overflows, throw 
			//away the recursive definition:
			d.setExtendedType(new UnknownType(that.getUnit()).getType());
		}
	}
	
    private static boolean isUndecidableSupertype(ProducedType st,
            Node node) {
        if (st==null) return false;
        TypeDeclaration std = st.getDeclaration();
        if (std instanceof TypeAlias) {
            ProducedType et = std.getExtendedType();
            if (et.isRecursiveTypeAliasDefinition(std)) {
                node.addError("supertype contains reference to circularly defined type alias");
                return true;
            }
            else if (isUndecidableSupertype(et, node)) {
                return true;
            }
        }
        List<ProducedType> tal = st.getTypeArgumentList();
        List<TypeParameter> tpl = std.getTypeParameters();
        for (int i=0; i<tal.size() && i<tpl.size(); i++) {
            TypeParameter tp = tpl.get(i);
            ProducedType at = tal.get(i);
            if (!tp.isCovariant() && !tp.isContravariant()) {
                if (containsIntersection(at, node)) {
                    return true;
                }
            }
            if (isUndecidableSupertype(at, node)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIntersection(ProducedType at,
            Node node) {
        if (at==null) return false;
        TypeDeclaration atd = at.getDeclaration();
        if (atd instanceof TypeAlias) {
            ProducedType et = atd.getExtendedType();
            if (et.isRecursiveTypeAliasDefinition(atd)) {
                node.addError("supertype contains reference to circularly defined type alias");
                return true;
            }
            else if (containsIntersection(et, node)) {
                return true;
            }
        }
        if (atd instanceof IntersectionType) {
            node.addError("supertype contains intersection as argument to invariant type parameter: " +
                    at.getProducedTypeName(node.getUnit()));
            return true;
        }
        if (atd instanceof UnionType) {
            for (ProducedType ct: atd.getCaseTypes()) {
                if (containsIntersection(ct, node)) {
                    return true;
                }
            }
        }
        return false;
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
	
    private void checkForUndecidability(Tree.SatisfiedTypes that, TypeDeclaration d) {
        if (that!=null) {
            int i=0;
            for (Tree.StaticType st: that.getTypes()) {
                if (isUndecidableSupertype(st.getTypeModel(), st)) {
                    d.getSatisfiedTypes().set(i, new UnknownType(that.getUnit()).getType());
                }
                i++;
            }
        }
    }
    
    private void checkForUndecidability(Tree.ExtendedType that, Class d) {
        if (that!=null) {
            Tree.StaticType et = that.getType();
            if (isUndecidableSupertype(et.getTypeModel(), et)) {
                d.setExtendedType(new UnknownType(that.getUnit()).getType());
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
