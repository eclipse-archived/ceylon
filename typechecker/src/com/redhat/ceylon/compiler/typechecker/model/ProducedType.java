package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A produced type with actual type arguments.
 * This represents something that is actually
 * considered a "type" in the language
 * specification.
 *
 * @author Gavin King
 */
public class ProducedType extends ProducedReference {

    ProducedType() {}

    @Override
    public TypeDeclaration getDeclaration() {
        return (TypeDeclaration) super.getDeclaration();
    }

    public boolean isExactly(ProducedType type) {
        if (getDeclaration() instanceof BottomType) {
            return type.getDeclaration() instanceof BottomType;
        }
        else if (getDeclaration() instanceof UnionType) {
            List<ProducedType> cases = getCaseTypes();
            if (type.getDeclaration() instanceof UnionType) {
                List<ProducedType> otherCases = type.getCaseTypes();
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
                        if (!found) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            else if (cases.size()==1) {
                ProducedType st = cases.get(0);
                return st.isExactly(type);
            }
            else {
                return false;
            }
        }
        else if (getDeclaration() instanceof IntersectionType) {
            List<ProducedType> types = getSatisfiedTypes();
            if (type.getDeclaration() instanceof IntersectionType) {
                List<ProducedType> otherTypes = type.getSatisfiedTypes();
                if (types.size()!=otherTypes.size()) {
                    return false;
                }
                else {
                    for (ProducedType c: types) {
                        boolean found = false;
                        for (ProducedType oc: otherTypes) {
                            if (c.isExactly(oc)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            else if (types.size()==1) {
                ProducedType st = types.get(0);
                return st.isExactly(type);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof UnionType) {
            List<ProducedType> otherCases = type.getCaseTypes();
            if (otherCases.size()==1) {
                ProducedType st = otherCases.get(0);
                return this.isExactly(st);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            List<ProducedType> otherTypes = type.getSatisfiedTypes();
            if (otherTypes.size()==1) {
                ProducedType st = otherTypes.get(0);
                return this.isExactly(st);
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
                        return false;
                        /*throw new RuntimeException(
                                "Missing type argument for: " +
                                        p.getName() + " of " +
                                        getDeclaration().getName());*/
                    }
                    else if (!arg.isExactly(otherArg)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public boolean isSupertypeOf(ProducedType type) {
        return type.isSubtypeOf(this);
    }

    public boolean isSubtypeOf(ProducedType type) {
        return isSubtypeOf(type, null);
    }
    
    public boolean isSubtypeOf(ProducedType type, TypeDeclaration selfTypeToIgnore) {
        if (getDeclaration() instanceof BottomType) {
            return true;
        }
        else if (type.getDeclaration() instanceof BottomType) {
            return false;
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getInternalCaseTypes()) {
                if (ct==null || !ct.isSubtypeOf(type, selfTypeToIgnore)) {
                    return false;
                }
            }
            return true;
        }
        else if (type.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: type.getInternalCaseTypes()) {
                if (ct!=null && isSubtypeOf(ct, selfTypeToIgnore)) {
                    return true;
                }
            }
            return false;
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: type.getInternalSatisfiedTypes()) {
                if (ct!=null && !isSubtypeOf(ct, selfTypeToIgnore)) {
                    return false;
                }
            }
            return true;
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getInternalSatisfiedTypes()) {
                if (ct==null || ct.isSubtypeOf(type, selfTypeToIgnore)) {
                    return true;
                }
            }
            return false;
        }
        else {
            boolean isInner = getDeclaration().isMember();
            if (isInner) {
                boolean isOtherInner = type.getDeclaration().isMember();
                if (isOtherInner) {
                    if (!getDeclaringType().isSubtypeOf(type.getDeclaringType())) {
                        return false;
                    }
                }
            }
            ProducedType st = getSupertype(type.getDeclaration(), selfTypeToIgnore);
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
                    else if (p.isCovariant()) {
                        if (!arg.isSubtypeOf(otherArg)) {
                            return false;
                        }
                    }
                    else if (p.isContravariant()) {
                        if (!otherArg.isSubtypeOf(arg)) {
                            return false;
                        }
                    }
                    else {
                        if (!arg.isExactly(otherArg)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    public ProducedType minus(ClassOrInterface ci) {
        if (getDeclaration()==ci) {
            return new BottomType().getType();
        }
        else if (getDeclaration() instanceof UnionType) {
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getCaseTypes()) {
                if (ct.getSupertype(ci)==null) {
                    addToUnion(types, ct.minus(ci));
                }
            }
            UnionType ut = new UnionType();
            ut.setCaseTypes(types);
            return ut.getType();
        }
        else {
            return this;
        }
    }

    public ProducedType substitute(Map<TypeParameter, ProducedType> substitutions) {
        return new Substitution().substitute(this, substitutions);
    }

    public ProducedType substituteInternal(Map<TypeParameter, ProducedType> substitutions) {
        return new InternalSubstitution().substitute(this, substitutions);
    }

    public ProducedReference getTypedReference(Declaration member, 
            List<ProducedType> typeArguments) {
        if (member instanceof TypeDeclaration) {
            return getTypeMember( (TypeDeclaration) member, typeArguments );
        }
        else {
            return getTypedMember( (TypedDeclaration) member, typeArguments);
        }
    }

    public ProducedTypedReference getTypedMember(TypedDeclaration member, 
            List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype((TypeDeclaration) member.getContainer());
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

    public ProducedType getTypeMember(TypeDeclaration member, 
            List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype((TypeDeclaration) member.getContainer());
        ProducedType pt = new ProducedType();
        pt.setDeclaration(member);
        pt.setDeclaringType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;
    }

    public ProducedType getProducedType(ProducedType receiver, Declaration member, 
            List<ProducedType> typeArguments) {
        ProducedType rst = (receiver==null) ? null : 
                receiver.getSupertype((TypeDeclaration) member.getContainer());
        return new Substitution().substitute(this, arguments(member, rst, typeArguments));
    }

    public ProducedType getType() {
        return this;
    }
    public List<ProducedType> getSupertypes() {
        return getSupertypes(new ArrayList<ProducedType>());
    }
    
    List<ProducedType> getSupertypes(List<ProducedType> list) {
        if ( isWellDefined() && Util.addToSupertypes(list, this) ) {
            ProducedType extendedType = getExtendedType();
            if (extendedType!=null) {
                extendedType.getSupertypes(list);
            }
            for (ProducedType dst : getSatisfiedTypes()) {
                dst.getSupertypes(list);
            }
            ProducedType selfType = getSelfType();
            if (selfType!=null) {
                selfType.getSupertypes(list);
            }
            List<ProducedType> caseTypes = getCaseTypes();
            if (caseTypes!=null /*&& !getDeclaration().getCaseTypes().isEmpty()*/) {
                for (ProducedType t: caseTypes) {
                    List<ProducedType> candidates = t.getSupertypes();
                    for (ProducedType st: candidates) {
                        boolean include = true;
                        for (ProducedType ct : getDeclaration().getCaseTypes()) {
                            if (!ct.isSubtypeOf(st)) {
                                include = false;
                                break;
                            }
                        }
                        if (include) {
                            Util.addToSupertypes(list, st);
                        }
                    }
                }
            }
        }
        return list;
    }
    
    public ProducedType getSupertype(TypeDeclaration dec) {
        return getSupertype(dec, null);
    }
    
    private ProducedType getSupertype(final TypeDeclaration dec, 
            TypeDeclaration selfTypeToIgnore) {
        Criteria c = new Criteria() {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                return type==dec;
            }
        };
        return getSupertype(c, new ArrayList<ProducedType>(), selfTypeToIgnore);
    }
    
    ProducedType getSupertype(Criteria c) {
        return getSupertype(c, new ArrayList<ProducedType>(), null);
    }
    
    static interface Criteria {
        boolean satisfies(TypeDeclaration type);
    }
    
    ProducedType getSupertype(final Criteria c, List<ProducedType> list, 
            final TypeDeclaration ignoringSelfType) {
        if (c.satisfies(getDeclaration())) {
            return this;
        }
        if ( isWellDefined() && Util.addToSupertypes(list, this) ) {
            //search for the most-specific supertype 
            //for the given declaration
            ProducedType result = null;
            ProducedType extendedType = getInternalExtendedType();
            if (extendedType!=null) {
                ProducedType possibleResult = extendedType.getSupertype(c, list, 
                                ignoringSelfType);
                if (possibleResult!=null) {
                    result = possibleResult;
                }
            }
            for (ProducedType dst: getInternalSatisfiedTypes()) {
                ProducedType possibleResult = dst.getSupertype(c, list, 
                                ignoringSelfType);
                if (possibleResult!=null && (result==null || 
                        possibleResult.isSubtypeOf(result, ignoringSelfType))) {
                    result = possibleResult;
                }
            }
            if (getDeclaration()!=ignoringSelfType) {
                ProducedType selfType = getInternalSelfType();
                if (selfType!=null) {
                    ProducedType possibleResult = selfType.getSupertype(c, list, 
                                ignoringSelfType);
                    if (possibleResult!=null && (result==null || 
                            possibleResult.isSubtypeOf(result, ignoringSelfType))) {
                        result = possibleResult;
                    }
                }
            }
            final List<ProducedType> caseTypes = getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                //first find a common superclass or superinterface 
                //declaration that satisfies the criteria, ignoring
                //type arguments for now
                Criteria c2 = new Criteria() {
                    @Override
                    public boolean satisfies(TypeDeclaration type) {
                        if ( c.satisfies(type) ) {
                            for (ProducedType ct: caseTypes) {
                                if (ct.getSupertype(type, ignoringSelfType)==null) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                };
                ProducedType stc = caseTypes.get(0).getSupertype(c2, list, 
                                ignoringSelfType);
                if (stc!=null) {
                    //we found the declaration, now try to construct a 
                    //produced type that is a true common supertype
                    ProducedType candidateResult = getCommonSupertype(caseTypes, 
                            stc.getDeclaration(), ignoringSelfType);
                    if (candidateResult!=null && (result==null || 
                            candidateResult.isSubtypeOf(result, ignoringSelfType))) {
                        result = candidateResult;
                    }
                }
            }
            return result;
        }
        else {
            return null;
        }
    }

    private static ProducedType getCommonSupertype(final List<ProducedType> caseTypes,
            TypeDeclaration dec, final TypeDeclaration selfTypeToIgnore) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            List<ProducedType> list2 = new ArrayList<ProducedType>();
            ProducedType result;
            if (tp.isContravariant()) { 
                for (ProducedType pt: caseTypes) {
                    ProducedType st = pt.getSupertype(dec, selfTypeToIgnore);
                    if (st==null) {
                        return null;
                    }
                    addToIntersection(list2, st.getTypeArguments().get(tp));
                }
                IntersectionType it = new IntersectionType();
                it.setSatisfiedTypes(list2);
                result = it.canonicalize().getType();
            }
            else {
                for (ProducedType pt: caseTypes) {
                    ProducedType st = pt.getSupertype(dec, selfTypeToIgnore);
                    if (st==null) {
                        return null;
                    }
                    addToUnion(list2, st.getTypeArguments().get(tp));
                }
                UnionType ut = new UnionType();
                ut.setCaseTypes(list2);
                result = ut.getType();
            }
            args.add(result);
        }
        //check that the unioned type args
        //satisfy the type constraints
        for (int i=0; i<args.size(); i++) {
            TypeParameter tp = dec.getTypeParameters().get(i);
            for (ProducedType ub: tp.getSatisfiedTypes()) {
                if (!args.get(i).isSubtypeOf(ub)) {
                    return null;
                }
            }
        }
        //recurse to the qualifying type
        ProducedType outerType;
        if (dec.isMember()) {
            TypeDeclaration outer = (TypeDeclaration) dec.getContainer();
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType pt: caseTypes) {
                ProducedType st = pt.getDeclaringType().getSupertype(outer, null);
                list.add(st);
            }
            outerType = getCommonSupertype(list, outer, null);
        }
        else {
            outerType = null;
        }
        //make the resulting type
        ProducedType candidateResult = dec.getProducedType(outerType, args);
        //check the the resulting type is *really*
        //a subtype (take variance into account)
        for (ProducedType pt: caseTypes) {
            if (!pt.isSubtypeOf(candidateResult)) {
                return null;
            }
        }
        return candidateResult;
    }
    
    public List<ProducedType> getTypeArgumentList() {
        List<ProducedType> lpt = new ArrayList<ProducedType>();
        for (TypeParameter tp : getDeclaration().getTypeParameters()) {
            lpt.add(getTypeArguments().get(tp));
        }
        return lpt;
    }

    public boolean checkVariance(boolean covariant, boolean contravariant, 
            Declaration declaration) {
        if (getDeclaration() instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) getDeclaration();
            return tp.getDeclaration()==declaration ||
                    ((covariant || !tp.isCovariant()) && 
                            (contravariant || !tp.isContravariant()));
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                if (!ct.checkVariance(covariant, contravariant, declaration)) {
                    return false;
                }
            }
            return true;
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getSatisfiedTypes()) {
                if (!ct.checkVariance(covariant, contravariant, declaration)) {
                    return false;
                }
            }
            return true;
        }
        else {
            for (TypeParameter tp: getDeclaration().getTypeParameters()) {
                ProducedType pt = getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        if (!pt.checkVariance(covariant, contravariant, declaration)) {
                            return false;
                        }
                    }
                    else if (tp.isContravariant()) {
                        if (!pt.checkVariance(!covariant, !contravariant, declaration)) {
                            return false;
                        }
                    }
                    else {
                        if (!pt.checkVariance(false, false, declaration)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    public boolean isWellDefined() {
        for (ProducedType at: getTypeArgumentList()) {
            if (at==null || !at.isWellDefined() ) {
                return false;
            }
        }
        return true;
    }

    private ProducedType getInternalSelfType() {
        ProducedType selfType = getDeclaration().getSelfType();
        return selfType==null?null:selfType.substituteInternal(getTypeArguments());
    }

    private List<ProducedType> getInternalSatisfiedTypes() {
        List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
        for (ProducedType st: getDeclaration().getSatisfiedTypes()) {
            satisfiedTypes.add(st.substituteInternal(getTypeArguments()));
        }
        return satisfiedTypes;
    }

    private ProducedType getInternalExtendedType() {
        ProducedType extendedType = getDeclaration().getExtendedType();
        return extendedType==null?null:extendedType.substituteInternal(getTypeArguments());
    }

    private List<ProducedType> getInternalCaseTypes() {
        if (getDeclaration().getCaseTypes()==null) {
            return null;
        }
        else {
            List<ProducedType> caseTypes = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                caseTypes.add(ct.substituteInternal(getTypeArguments()));
            }
            return caseTypes;
        }
    }

    private ProducedType getSelfType() {
        ProducedType selfType = getDeclaration().getSelfType();
        return selfType==null?null:selfType.substitute(getTypeArguments());
    }

    private List<ProducedType> getSatisfiedTypes() {
        List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
        for (ProducedType st: getDeclaration().getSatisfiedTypes()) {
            satisfiedTypes.add(st.substitute(getTypeArguments()));
        }
        return satisfiedTypes;
    }

    private ProducedType getExtendedType() {
        ProducedType extendedType = getDeclaration().getExtendedType();
        return extendedType==null?null:extendedType.substitute(getTypeArguments());
    }

    private List<ProducedType> getCaseTypes() {
        if (getDeclaration().getCaseTypes()==null) {
            return null;
        }
        else {
            List<ProducedType> caseTypes = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                caseTypes.add(ct.substitute(getTypeArguments()));
            }
            return caseTypes;
        }
    }

    /**
     * Substitutes type arguments for type parameters.
     * This default strategy eliminates duplicate types
     * from unions after substituting arguments.
     * @author Gavin King
     */
    static class Substitution {
        
        ProducedType substitute(ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Declaration dec;
            if (pt.getDeclaration() instanceof UnionType) {
                UnionType ut = new UnionType();
                List<ProducedType> types = new ArrayList<ProducedType>();
                for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                    addTypeToUnion(ct, substitutions, types);
                }
                ut.setCaseTypes(types);
                dec = ut;
            }
            else if (pt.getDeclaration() instanceof IntersectionType) {
                IntersectionType it = new IntersectionType();
                List<ProducedType> types = new ArrayList<ProducedType>();
                for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                    addTypeToIntersection(ct, substitutions, types);
                }
                it.setSatisfiedTypes(types);
                dec = it.canonicalize();
            }
            else {
                if (pt.getDeclaration() instanceof TypeParameter) {
                    ProducedType sub = substitutions.get(pt.getDeclaration());
                    if (sub!=null) {
                        return sub;
                    }
                }
                dec = pt.getDeclaration();
            }
            return replaceDeclaration(pt, dec, substitutions);
        }

        void addTypeToUnion(ProducedType ct, Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToUnion(types, substitute(ct, substitutions));
            }
        }

        void addTypeToIntersection(ProducedType ct, Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToIntersection(types, substitute(ct, substitutions));
            }
        }

        private Map<TypeParameter, ProducedType> substituted(ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
            for (Map.Entry<TypeParameter, ProducedType> e : pt.getTypeArguments().entrySet()) {
                if (e.getValue()!=null) {
                    map.put(e.getKey(), substitute(e.getValue(), substitutions));
                }
            }
            if (pt.getDeclaringType()!=null) {
                map.putAll(substituted(pt.getDeclaringType(), substitutions));
            }
            return map;
        }

        private ProducedType replaceDeclaration(ProducedType pt, Declaration dec,
                Map<TypeParameter, ProducedType> substitutions) {
            ProducedType type = new ProducedType();
            type.setDeclaration(dec);
            if (pt.getDeclaringType()!=null) {
                type.setDeclaringType(substitute(pt.getDeclaringType(), substitutions));
            }
            type.setTypeArguments(substituted(pt, substitutions));
            return type;
        }
            
    }
    
    /**
     * This special strategy for internal use by the 
     * containing class does not eliminate duplicate 
     * types from unions after substituting arguments.
     * This is to avoid a stack overflow that otherwise
     * results! (Determining if a union contains 
     * duplicates requires recursion to the argument
     * substitution code via some very difficult-to-
     * understand flow.)
     * @author Gavin King
     */
    static class InternalSubstitution extends Substitution {
    
        private void addType(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct!=null) {
                types.add(substitute(ct, substitutions));
            }
        }
        
        @Override void addTypeToUnion(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }

        @Override void addTypeToIntersection(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }
        
    }

    @Override
    public String toString() {
        return "Type[" + getProducedTypeName() + "]";
    }

    public String getProducedTypeName() {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        String producedTypeName = "";
        if (getDeclaration().isMember()) {
            producedTypeName += getDeclaringType().getProducedTypeName();
            producedTypeName += ".";
        }
        producedTypeName += getDeclaration().getName();
        if (!getTypeArgumentList().isEmpty()) {
            producedTypeName += "<";
            for (ProducedType t : getTypeArgumentList()) {
                if (t==null) {
                    producedTypeName += "unknown,";
                }
                else {
                    producedTypeName += t.getProducedTypeName() + ",";
                }
            }
            producedTypeName += ">";
            producedTypeName = producedTypeName.replace(",>", ">");
        }
        return producedTypeName;
    }

    public String getProducedTypeQualifiedName() {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        String producedTypeName = "";
        if (getDeclaration().isMember()) {
            producedTypeName += getDeclaringType().getProducedTypeQualifiedName();
            producedTypeName += ".";
        }
        producedTypeName += getDeclaration().getQualifiedNameString();
        if (!getTypeArgumentList().isEmpty()) {
            producedTypeName += "<";
            for (ProducedType t : getTypeArgumentList()) {
                if (t==null) {
                    producedTypeName += "?,";
                }
                else {
                    producedTypeName += t.getProducedTypeQualifiedName() + ",";
                }
            }
            producedTypeName += ">";
            producedTypeName = producedTypeName.replace(",>", ">");
        }
        return producedTypeName;
    }

}
