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
	
    private static boolean isUndecidableSupertype(ProducedType st) {
        if (st==null) return false;
        TypeDeclaration std = st.getDeclaration();
        if (std instanceof TypeAlias) {
            if (isUndecidableSupertype(std.getExtendedType())) {
                return true;
            }
        }
        List<ProducedType> tal = st.getTypeArgumentList();
        List<TypeParameter> tpl = std.getTypeParameters();
        for (int i=0; i<tal.size() && i<tpl.size(); i++) {
            TypeParameter tp = tpl.get(i);
            ProducedType at = tal.get(i);
            if (!tp.isCovariant() && !tp.isContravariant()) {
                if (containsIntersection(at)) {
                    return true;
                }
            }
            if (isUndecidableSupertype(at)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIntersection(ProducedType at) {
        if (at==null) return false;
        TypeDeclaration d = at.getDeclaration();
        if (d instanceof TypeAlias) {
            return containsIntersection(d.getExtendedType());
        }
        if (d instanceof IntersectionType) {
            return true;
        }
        if (d instanceof UnionType) {
            for (ProducedType ct: d.getCaseTypes()) {
                if (containsIntersection(ct)) {
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
                if (isUndecidableSupertype(st.getTypeModel())) {
                    st.addError("satisfied type contains intersecton as argument to invariant type parameter");
                    d.getSatisfiedTypes().set(i, new UnknownType(that.getUnit()).getType());
                }
                i++;
            }
        }
    }
    
    private void checkForUndecidability(Tree.ExtendedType that, Class d) {
        if (that!=null) {
            Tree.StaticType et = that.getType();
            if (isUndecidableSupertype(et.getTypeModel())) {
                et.addError("extended type contains intersecton as argument to invariant type parameter");
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
