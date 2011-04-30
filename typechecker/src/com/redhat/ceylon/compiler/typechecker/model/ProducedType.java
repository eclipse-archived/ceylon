package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.ArrayList;
import java.util.Iterator;
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
	            if (ct==null || !ct.isSubtypeOf(type)) {
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
    
    public ProducedType minus(ClassOrInterface ci) {
        if (getDeclaration()==ci) {
            return replaceDeclaration( new BottomType() );
        }
        else if (getDeclaration() instanceof UnionType) {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                if (ct.getSupertype(ci)==null) {
                    addToUnion(types, ct.minus(ci));
                }
            }
            ut.setCaseTypes(types);
            return replaceDeclaration(ut);
        }
        else {
            return this;
        }
    }
    
    public ProducedType substitute(Map<TypeParameter,ProducedType> substitutions) {
        
        Declaration d;
        if (getDeclaration() instanceof UnionType) {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                if (ct==null) {
                    types.add(null);
                }
                else {
                    addToUnion(types, ct.substitute(substitutions));
                }
            }
            ut.setCaseTypes(types);
            d = ut;            
        }
        else {
            if (getDeclaration() instanceof TypeParameter) {
                ProducedType sub = substitutions.get(getDeclaration());
                if (sub!=null) return sub;
            }
            d = getDeclaration();
        }
        
        return replaceDeclaration(d, substitutions);
        
    }

    private ProducedType replaceDeclaration(Declaration d) {
        ProducedType t = new ProducedType();
        t.setDeclaration(d);
        if (getDeclaringType()!=null) {
            t.setDeclaringType(getDeclaringType());
        }
        t.setTypeArguments(getTypeArguments());
        return t;
    }
        
    private ProducedType replaceDeclaration(Declaration d, 
            Map<TypeParameter, ProducedType> substitutions) {
        ProducedType t = new ProducedType();
        t.setDeclaration(d);
        if (getDeclaringType()!=null) {
            t.setDeclaringType(getDeclaringType().substitute(substitutions));
        }
        t.setTypeArguments(sub(substitutions));
        return t;
    }
        
    public ProducedTypedReference getTypedMember(TypedDeclaration member, List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype( (TypeDeclaration) member.getContainer() );
        /*if (declaringType==null) {
            return null;
        }
        else {*/
            ProducedTypedReference ptr = new ProducedTypedReference();
            ptr.setDeclaration(member);
            ptr.setDeclaringType(declaringType);
            Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
            //map.putAll(sub(map));
            ptr.setTypeArguments(map);
            return ptr;
        //}
    }
         
    public ProducedType getTypeMember(TypeDeclaration member, List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype( (TypeDeclaration) member.getContainer() );
        ProducedType pt = new ProducedType();
        pt.setDeclaration(member);
        pt.setDeclaringType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;
    }
    
    public ProducedType getProducedType(ProducedType receiver,
            Declaration member, List<ProducedType> typeArguments) {
        ProducedType rst =  (receiver==null) ? null : receiver.getSupertype( (TypeDeclaration) member.getContainer() );
        return substitute(com.redhat.ceylon.compiler.typechecker.model.Util.arguments(member, rst, typeArguments));
    }

    public ProducedType getType() {
        return this;
    }
    
    public List<ProducedType> getSupertypes() {
        List<ProducedType> list = new ArrayList<ProducedType>();
        list.add(this);
        if (getDeclaration().getExtendedType()!=null) {
            for (ProducedType et: getDeclaration().getExtendedType().getSupertypes()) {
                addToSupertypes( list, et.substitute(getTypeArguments()) );
            }
        }
        for (ProducedType dst: getDeclaration().getSatisfiedTypes()) {
            for (ProducedType st: dst.getSupertypes()) {
                addToSupertypes( list, st.substitute(getTypeArguments()) );
            }
        }
        if (getDeclaration().getCaseTypes()!=null && !getDeclaration().getCaseTypes().isEmpty()) {
            for (ProducedType t: getDeclaration().getCaseTypes()) {
                List<ProducedType> candidates = t.getSupertypes();
                for (ProducedType st: candidates) {
                    st = st.substitute(getTypeArguments());
                    boolean include = true;
                    for (ProducedType ct: getDeclaration().getCaseTypes()) {
                        ct = ct.substitute(getTypeArguments());
                        if (!ct.isSubtypeOf(st)) {
                            include = false;
                            break;
                        }
                    }
                    if (include) {
                        addToSupertypes(list, st);
                    }
                }
            }
        }
        return list;
    }

    public ProducedType getSupertype(TypeDeclaration dec) {
        if (this.getDeclaration()==dec) {
            return this;
        }
        else if (getDeclaration().getExtendedType()!=null) {
            ProducedType et = getDeclaration().getExtendedType().getSupertype(dec);
            if (et!=null) {
                return et.substitute(getTypeArguments());
            }
        }
        for (ProducedType dst: getDeclaration().getSatisfiedTypes()) {
            ProducedType st = dst.getSupertype(dec);
            if (st!=null) {
                return st.substitute(getTypeArguments());
            }
        }
        if (getDeclaration().getCaseTypes()!=null && !getDeclaration().getCaseTypes().isEmpty()) {
            for (ProducedType t: getDeclaration().getCaseTypes()) {
                ProducedType candidate = t.getSupertype(dec);
                if (candidate!=null) {
                    ProducedType st = candidate.substitute(getTypeArguments());
                    boolean include = true;
                    for (ProducedType ct: getDeclaration().getCaseTypes()) {
                        ct = ct.substitute(getTypeArguments());
                        if (!ct.isSubtypeOf(st)) {
                            include = false;
                            break;
                        }
                    }
                    if (include) {
                        return st;
                    }
                }
            }
        }
        return null;
    }

    private static void addToSupertypes(List<ProducedType> list, ProducedType st) {
        boolean include = true;
        for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
            ProducedType et = iter.next();
            if (st.isExactly(et)) {
                include = false;
                break;
            }
        }
        if (include) {
            list.add(st);
        }
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
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                if ( !ct.checkVariance(covariant, contravariant, declaration) ) return false;
            }
            return true;
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
                        if ( !pt.checkVariance(false, false, declaration) ) return false;
                    }
                }
            }
            return true;
        }
    }
    
}
