package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A type with actual type arguments.
 * 
 * @author Gavin King
 *
 */
public class ProducedType extends ProducedReference {
	
    ProducedType() {}
    
	@Override
    public TypeDeclaration getDeclaration() {
		return (TypeDeclaration) super.getDeclaration();
	}
		
	@Override
	public String toString() {
		return "Type[" + getProducedTypeName() + "]";
	}

	public String getProducedTypeName() {
        if (getDeclaration() == null) {
            //unknown type
            return null;
        }
		String producedTypeName = "";
		if (getDeclaration().isMemberType()) {
		    producedTypeName += getDeclaringType().getProducedTypeName();
		    producedTypeName += ".";
		}
		producedTypeName += getDeclaration().getName();
		if (!getTypeArgumentList().isEmpty()) {
			producedTypeName+="<";
			for (ProducedType t: getTypeArgumentList()) {
			    if (t==null) {
			        producedTypeName+="unknown,";
			    }
			    else {
			        producedTypeName+=t.getProducedTypeName() + ",";
			    }
			}
            producedTypeName+=">";
			producedTypeName = producedTypeName.replace(",>", ">");
		}
		return producedTypeName;
	}
	
	public boolean isExactly(ProducedType type) {
	    if (getDeclaration() instanceof BottomType) {
	        return type.getDeclaration() instanceof BottomType;
	    }
	    else if (getDeclaration() instanceof UnionType) {
	        if (type.getDeclaration() instanceof UnionType) {
	            List<ProducedType> cases = getDeclaration().getCaseTypes();
	            List<ProducedType> otherCases = type.getDeclaration().getCaseTypes();
	            if (cases.size()!=otherCases.size()) {
	                return false;
	            }
	            else {
	                for (ProducedType c: cases) {
	                    boolean found = false;
	                    for (ProducedType oc: otherCases) {
	                        if (c.isExactly(oc)) {
	                            found = true;
	                            break;
	                        }
	                    }
	                    if (!found) return false;
	                }
	                return true;
	            }
	        }
	        else {
	            return false;
	        }
	    }
	    else {
    	    if (type.getDeclaration()!=getDeclaration()) {
    	        return false;
    	    }
    	    else {
                for (TypeParameter p: getDeclaration().getTypeParameters()) {
                    ProducedType arg = getTypeArguments().get(p);
                    ProducedType otherArg = type.getTypeArguments().get(p);
                    if (arg==null || otherArg==null) {
                        throw new RuntimeException("Missing type argument for: " +
                                p.getName() + " of " + 
                                getDeclaration().getName());
                    }
        	        if ( !arg.isExactly(otherArg) ) {
        	            return false;
        	        }
        	    }
        	    return true;
    	    }
	    }
	}
	
	public boolean isSubtypeOf(ProducedType type) {
	    if (getDeclaration() instanceof BottomType) {
	        return true;
	    }
	    else if (type.getDeclaration() instanceof BottomType) {
	        return false;
	    }
	    else if (getDeclaration() instanceof UnionType) {
	        for (ProducedType ct: getDeclaration().getCaseTypes() ) {
	            if (!ct.isSubtypeOf(type)) {
	                return false;
	            }
	        }
            return true;
	    }
	    else if (type.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: type.getDeclaration().getCaseTypes() ) {
                if (ct!=null && this.isSubtypeOf(ct)) {
                    return true;
                }
            }
            return false;	    
	    }
    	else {
    	    boolean isInner = getDeclaration().isMemberType();
            if (isInner) {
                boolean isOtherInner = type.getDeclaration().isMemberType();
                if (isOtherInner) {
    	            if (!getDeclaringType().isSubtypeOf(type.getDeclaringType())) {
    	                return false;
    	            }
                }
    	    }
    	    ProducedType st = getSupertype( type.getDeclaration() );
    	    if (st==null) {
    	        return false;
    	    }
    	    else {
    	        for (TypeParameter p: type.getDeclaration().getTypeParameters()) {
    	            ProducedType arg = st.getTypeArguments().get(p);
    	            ProducedType otherArg = type.getTypeArguments().get(p);
    	            if (arg==null || otherArg==null) {
    	                /*throw new RuntimeException("Missing type argument for type parameter: " +
    	                        p.getName() + " of " + 
    	                        type.getDeclaration().getName());*/
    	                return false;
    	            }
    	            if (p.isCovariant()) {
    	                if (!arg.isSubtypeOf(otherArg)) {
    	                    return false;
    	                }
    	            }
    	            else if (p.isContravariant()) {
    	                if (!arg.isSupertypeOf(otherArg)) {
    	                    return false;
    	                }
    	            }
    	            else {
    	                if ( !arg.isExactly(otherArg) ) {
    	                    return false;
    	                }
    	            }
    	        }
    	        return true;
    	    }
	    }
	}
	
    public boolean isSupertypeOf(ProducedType type) {
        return type.isSubtypeOf(this);
    }
    
    public ProducedType substitute(Map<TypeParameter,ProducedType> substitutions) {
        if (getDeclaration() instanceof TypeParameter) {
            ProducedType sub = substitutions.get(getDeclaration());
            if (sub!=null) return sub;
        }
        ProducedType t = new ProducedType();
        t.setDeclaration(getDeclaration());
        if (getDeclaringType()!=null) {
            t.setDeclaringType(getDeclaringType().substitute(substitutions));
        }
        t.setTypeArguments(sub(substitutions));
        return t;
    }
        
    public ProducedTypedReference getTypedMember(TypedDeclaration td, List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype( (TypeDeclaration) td.getContainer() );
        if (declaringType==null) {
            return null;
        }
        else {
            return declaringType.getDeclaredTypedMember(td, declaringType, typeArguments);
        }
    }
         
    ProducedTypedReference getDeclaredTypedMember(TypedDeclaration td, ProducedType declaringType, List<ProducedType> typeArguments) {
        /*if (!acceptsArguments(td, typeArguments)) {
            return null;
        }*/
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(td);
        ptr.setDeclaringType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(td, declaringType, typeArguments);
        map.putAll(sub(map));
        ptr.setTypeArguments(map);
        return ptr;
    }
         
    public ProducedType getTypeMember(TypeDeclaration td, List<ProducedType> typeArguments) {
        //TODO: inherited type members, following pattern above!
        /*if (!acceptsArguments(td, typeArguments)) {
            return null;
        }*/
        ProducedType pt = new ProducedType();
        pt.setDeclaration(td);
        ProducedType declaringType = getSupertype( (TypeDeclaration) td.getContainer() );
        pt.setDeclaringType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(td, declaringType, typeArguments);
        map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;
    }
    
    public ProducedType getType() {
        return this;
    }
    
    public List<ProducedType> getSupertypes() {
        List<ProducedType> list = new ArrayList<ProducedType>();
        list.add(this);
        if (getDeclaration().getExtendedType()!=null) {
            for (ProducedType et: getDeclaration().getExtendedType().getSupertypes()) {
                list.add( et.substitute(getTypeArguments()) );
            }
        }
        for (ProducedType dst: getDeclaration().getSatisfiedTypes()) {
            for (ProducedType st: dst.getSupertypes()) {
                list.add( st.substitute(getTypeArguments()) );
            }
        }
        if (getDeclaration().getCaseTypes()!=null && !getDeclaration().getCaseTypes().isEmpty()) {
            List<ProducedType> candidates = getDeclaration().getCaseTypes().get(0).getSupertypes();
            for (ProducedType st: candidates) {
                boolean include = true;
                for (ProducedType ct: getDeclaration().getCaseTypes()) {
                    if (!ct.isSubtypeOf(st)) {
                        include = false;
                        break;
                    }
                }
                if (include) {
                    list.add(st);
                }
            }
        }
        return list;
    }
    
    public ProducedType getSupertype(TypeDeclaration genericType) {
        for (ProducedType st: getSupertypes()) {
            if (st.getDeclaration()==genericType) {
                return st;
            }
        }
        return null;
    }
    
    public List<ProducedType> getTypeArgumentList() {
        List<ProducedType> lpt = new ArrayList<ProducedType>();
        for (TypeParameter tp: getDeclaration().getTypeParameters()) {
            lpt.add( getTypeArguments().get(tp) );
        }
        return lpt;
    }
    
    public boolean checkVariance(boolean covariant, boolean contravariant, Declaration declaration) {
        if (getDeclaration() instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) getDeclaration();
            return tp.getDeclaration()==declaration ||
                ((covariant || !tp.isCovariant()) && (contravariant || !tp.isContravariant()));
        }
        else {
            for (TypeParameter tp: getDeclaration().getTypeParameters()) {
                ProducedType pt = getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        if (!pt.checkVariance(covariant, contravariant, declaration)) return false;
                    }
                    else if (tp.isContravariant()) {
                        if (!pt.checkVariance(!covariant, !contravariant, declaration)) return false;
                    }
                    else {
                        pt.checkVariance(false, false, declaration);
                    }
                }
            }
            return true;
        }
    }
    
}
