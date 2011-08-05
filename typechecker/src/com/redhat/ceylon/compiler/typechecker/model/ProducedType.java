package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.ArrayList;
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
        if (getDeclaration().isMemberType()) {
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
        if (getDeclaration().isMemberType()) {
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
            else {
                if (cases.size()==1) {
                    ProducedType st = cases.get(0);
                    return st.isExactly(type);
                }
                else {
                    return false;
                }
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
        else {
            if (type.getDeclaration()!=getDeclaration()) {
                return false;
            }
            else {
                for (TypeParameter p: getDeclaration().getTypeParameters()) {
                    ProducedType arg = getTypeArguments().get(p);
                    ProducedType otherArg = type.getTypeArguments().get(p);
                    if (arg==null || otherArg==null) {
                        throw new RuntimeException(
                                "Missing type argument for: " +
                                        p.getName() + " of " +
                                        getDeclaration().getName()
                        );
                    }
                    if (!arg.isExactly(otherArg)) {
                        return false;
                    }
                }
                return true;
            }
        }
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
            ProducedType st = getSupertype(type.getDeclaration(), selfTypeToIgnore);
            if (st==null) {
                return false;
            }
            else {
                for (TypeParameter p : type.getDeclaration().getTypeParameters()) {
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
                        if (!arg.isExactly(otherArg)) {
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
            return replaceDeclaration(new BottomType());
        }
        else if (getDeclaration() instanceof UnionType) {
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getCaseTypes()) {
                if (ct.getSupertype(ci)==null) {
                    addToUnion(types, ct.minus(ci));
                }
            }
            //if we would have a union of just one type, 
            //just return the type itself!
            if (types.size()==1) {
                return types.get(0);
            }
            else {
                UnionType ut = new UnionType();
                ut.setCaseTypes(types);
                return replaceDeclaration(ut);
            }
        }
        else {
            return this;
        }
    }

    public ProducedType substitute(Map<TypeParameter, ProducedType> substitutions) {

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
                if (sub!=null) {
                    return sub;
                }
            }
            d = getDeclaration();
        }

        return replaceDeclaration(d, substitutions);

    }

    //TODO: ugh, horrible code duplication from above!
    ProducedType substituteInternal(Map<TypeParameter, ProducedType> substitutions) {

        Declaration d;
        if (getDeclaration() instanceof UnionType) {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                if (ct!=null) {
                    types.add(ct.substituteInternal(substitutions));
                }
            }
            ut.setCaseTypes(types);
            d = ut;
        }
        else {
            if (getDeclaration() instanceof TypeParameter) {
                ProducedType sub = substitutions.get(getDeclaration());
                if (sub!=null) {
                    return sub;
                }
            }
            d = getDeclaration();
        }

        return replaceDeclarationInternal(d, substitutions);

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
    
    //TODO: ugh, horrible code duplication from above!
    private ProducedType replaceDeclarationInternal(Declaration d,
            Map<TypeParameter, ProducedType> substitutions) {
        ProducedType t = new ProducedType();
        t.setDeclaration(d);
        if (getDeclaringType()!=null) {
            t.setDeclaringType(getDeclaringType().substituteInternal(substitutions));
        }
        t.setTypeArguments(subInternal(substitutions));
        return t;
    }
    
    public ProducedReference getTypedReference(Declaration member, List<ProducedType> typeArguments) {
        if (member instanceof TypeDeclaration) {
            return getTypeMember( (TypeDeclaration) member, typeArguments );
        }
        else {
            return getTypedMember( (TypedDeclaration) member, typeArguments);
        }
    }

    public ProducedTypedReference getTypedMember(TypedDeclaration member, List<ProducedType> typeArguments) {
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

    public ProducedType getTypeMember(TypeDeclaration member, List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype((TypeDeclaration) member.getContainer());
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
        ProducedType rst = (receiver==null) ? null : receiver.getSupertype((TypeDeclaration) member.getContainer());
        return substitute(arguments(member, rst, typeArguments));
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
    
    public ProducedType getSupertype(final TypeDeclaration dec, TypeDeclaration selfTypeToIgnore) {
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
    
    ProducedType getSupertype(final Criteria c, List<ProducedType> list, final TypeDeclaration ignoringSelfType) {
        if (c.satisfies(getDeclaration())) {
            return this;
        }
        if ( isWellDefined() && Util.addToSupertypes(list, this) ) {
            //search for the most-specific supertype 
            //for the given declaration
            ProducedType result = null;
            ProducedType extendedType = getInternalExtendedType();
            if (extendedType!=null) {
                ProducedType possibleResult = extendedType.getSupertype(c, list, ignoringSelfType);
                if (possibleResult!=null) {
                    result = possibleResult;
                }
            }
            for (ProducedType dst: getInternalSatisfiedTypes()) {
                ProducedType possibleResult = dst.getSupertype(c, list, ignoringSelfType);
                if (possibleResult!=null) {
                    if (result==null || possibleResult.isSubtypeOf(result, ignoringSelfType)) {
                        result = possibleResult;
                    }
                }
            }
            if (getDeclaration()!=ignoringSelfType) {
                ProducedType selfType = getInternalSelfType();
                if (selfType!=null) {
                    ProducedType possibleResult = selfType.getSupertype(c, list, ignoringSelfType);
                    if (possibleResult!=null) {
                        if (result==null || possibleResult.isSubtypeOf(result, ignoringSelfType)) {
                            result = possibleResult;
                        }
                    }
                }
            }
            final List<ProducedType> caseTypes = getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                Criteria c2 = new Criteria() {
                    @Override
                    public boolean satisfies(TypeDeclaration type) {
                        if ( c.satisfies(type) ) {
                            for (ProducedType ct: caseTypes) {
                                ProducedType pt = ct.getSupertype(type, ignoringSelfType);
                                if (pt==null) {
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
                //first find a common supertype declaration 
                //that satisfies the criteria
                ProducedType stc = caseTypes.get(0).getSupertype(c2, list, ignoringSelfType);
                if (stc!=null) {
                    ProducedType candidateResult = getCommonSupertype(caseTypes, stc.getDeclaration(), ignoringSelfType);
                    if (candidateResult!=null) {
                        if (result==null || candidateResult.isSubtypeOf(result, ignoringSelfType)) {
                            result = candidateResult;
                        }
                    }
                }
            }
            return result;
        }
        else {
            return null;
        }
    }

    private ProducedType getCommonSupertype(final List<ProducedType> caseTypes,
            TypeDeclaration dec, final TypeDeclaration selfTypeToIgnore) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            //TODO: construct an intersection!
            //if (tp.isContravariant()) { ..... }
            List<ProducedType> list2 = new ArrayList<ProducedType>();
            for (ProducedType pt: caseTypes) {
                ProducedType st = pt.getSupertype(dec, selfTypeToIgnore);
                if (st==null) {
                    return null;
                }
                Util.addToUnion(list2, st.getTypeArguments().get(tp));
            }
            UnionType ut = new UnionType();
            ut.setCaseTypes(list2);
            args.add(ut.getType());
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
        if (dec.isMemberType()) {
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

    public boolean checkVariance(boolean covariant, boolean contravariant, Declaration declaration) {
        if (getDeclaration() instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) getDeclaration();
            return tp.getDeclaration()==declaration ||
                    ((covariant || !tp.isCovariant()) && (contravariant || !tp.isContravariant()));
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct : getDeclaration().getCaseTypes()) {
                if (!ct.checkVariance(covariant, contravariant, declaration)) {
                    return false;
                }
            }
            return true;
        }
        else {
            for (TypeParameter tp : getDeclaration().getTypeParameters()) {
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

}
