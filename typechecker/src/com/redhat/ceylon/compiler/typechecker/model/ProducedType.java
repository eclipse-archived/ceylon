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
            if (type.getDeclaration() instanceof UnionType) {
                List<ProducedType> cases = getDeclaration().getCaseTypes();
                List<ProducedType> otherCases = type.getDeclaration().getCaseTypes();
                if (cases.size()!=otherCases.size()) {
                    return false;
                }
                else {
                    for (ProducedType c : cases) {
                        boolean found = false;
                        for (ProducedType oc : otherCases) {
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
                if (getDeclaration().getCaseTypes().size()==1) {
                    ProducedType st = getDeclaration().getCaseTypes().get(0).substitute(getTypeArguments());
                    return st.isExactly(type);
                }
                else {
                    return false;
                }
            }
        }
        else if (type.getDeclaration() instanceof UnionType) {
            if (type.getDeclaration().getCaseTypes().size()==1) {
                ProducedType st = type.getDeclaration().getCaseTypes().get(0).substitute(type.getTypeArguments());
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
                for (TypeParameter p : getDeclaration().getTypeParameters()) {
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
        if (getDeclaration() instanceof BottomType) {
            return true;
        }
        else if (type.getDeclaration() instanceof BottomType) {
            return false;
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct : getDeclaration().getCaseTypes()) {
                if (ct==null || !ct.isSubtypeOf(type)) {
                    return false;
                }
            }
            return true;
        }
        else if (type.getDeclaration() instanceof UnionType) {
            for (ProducedType ct : type.getDeclaration().getCaseTypes()) {
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
            ProducedType st = getSupertype(type.getDeclaration());
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
            for (ProducedType ct : getDeclaration().getCaseTypes()) {
                if (ct.getSupertype(ci)==null) {
                    addToUnion(types, ct.minus(ci));
                }
            }
            //if we would have a union of just one type, 
            //just return the type itself!
            if (types.size()==1) {
                //TODO: I *think* that it is correct to call
                //      substitute here, but apparently the
                //      rest of the system never creates a
                //      produced type with type args from a
                //      union type, so no way to test this!
                return types.get(0).substitute(getTypeArguments());
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
            for (ProducedType ct : getDeclaration().getCaseTypes()) {
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
        if ( Util.addToSupertypes(list, this) ) {
            if (getDeclaration().getExtendedType()!=null) {
                getDeclaration().getExtendedType().substitute(getTypeArguments()).getSupertypes(list);
            }
            for (ProducedType dst : getDeclaration().getSatisfiedTypes()) {
                dst.substitute(getTypeArguments()).getSupertypes(list);
            }
            if (getDeclaration().getSelfType()!=null) {
                getDeclaration().getSelfType().substitute(getTypeArguments()).getSupertypes(list);
            }
            if (getDeclaration().getCaseTypes()!=null /*&& !getDeclaration().getCaseTypes().isEmpty()*/) {
                for (ProducedType t : getDeclaration().getCaseTypes()) {
                    List<ProducedType> candidates = t.substitute(getTypeArguments()).getSupertypes();
                    for (ProducedType st : candidates) {
                        boolean include = true;
                        for (ProducedType ct : getDeclaration().getCaseTypes()) {
                            if (!ct.substitute(getTypeArguments()).isSubtypeOf(st)) {
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

    public ProducedType getSupertype(final TypeDeclaration dec) {
        Criteria c = new Criteria() {
            @Override
            public boolean satisfies(TypeDeclaration td) {
                return td==dec;
            }
        };
        return getSupertype(c);
    }
    
    ProducedType getSupertype(Criteria c) {
        return getSupertype(c, new ArrayList<ProducedType>());
    }
    
    static interface Criteria {
        boolean satisfies(TypeDeclaration td);
    }
    
    ProducedType getSupertype(Criteria c, List<ProducedType> list) {
        if (c.satisfies(getDeclaration())) {
            return this;
        }
        if ( Util.addToSupertypes(list, this) ) {
            //search for the most-specific supertype 
            //for the given declaration
            ProducedType result = null;
            if (getDeclaration().getExtendedType()!=null) {
                //TODO: I would prefer to substitute type args before 
                //      recursing, but I got some stack overflows
                ProducedType possibleResult = getDeclaration().getExtendedType().getSupertype(c, list);
                if (possibleResult!=null) {
                    possibleResult = possibleResult.substitute(getTypeArguments());
                    result = possibleResult;
                }
            }
            for (ProducedType dst : getDeclaration().getSatisfiedTypes()) {
                //TODO: I would prefer to substitute type args before 
                //      recursing, but I got some stack overflows
                ProducedType possibleResult = dst.getSupertype(c, list);
                if (possibleResult!=null) {
                    possibleResult = possibleResult.substitute(getTypeArguments());
                    if (result==null || possibleResult.isSubtypeOf(result)) {
                        result = possibleResult;
                    }
                }
            }
            if (getDeclaration().getSelfType()!=null) {
                ProducedType possibleResult;
                //TODO: the following is super-ugly
                //      it would be much better if
                //      we substituted type args 
                //      first before recursing
                ProducedType actualSelfType = getTypeArguments().get(getDeclaration().getSelfType().getDeclaration());
                if (actualSelfType!=null && c.satisfies(actualSelfType.getDeclaration())) {
                    possibleResult = getDeclaration().getSelfType();
                }
                else {
                    possibleResult = getDeclaration().getSelfType().getSupertype(c, list);
                }
                //end ugly
                if (possibleResult!=null) {
                    possibleResult = possibleResult.substitute(getTypeArguments());
                    if (result==null || possibleResult.isSubtypeOf(result)) {
                        result = possibleResult;
                    }
                }
            }
            //consider types which are supertypes of 
            //all cases (do we really absolutely need 
            //to iterate *all* supertypes of every 
            //case type or is there a smarter way?)
            if (getDeclaration().getCaseTypes()!=null && !getDeclaration().getCaseTypes().isEmpty()) {
                for (ProducedType t: getDeclaration().getCaseTypes()) {
                    for (ProducedType st: t.substitute(getTypeArguments()).getSupertypes()) {
                        ProducedType candidateResult = st.getSupertype(c, list);
                        if (candidateResult!=null) {
                            //candidateResult = candidateResult;
                            boolean include = true;
                            for (ProducedType ct : getDeclaration().getCaseTypes()) {
                                if (!ct.substitute(getTypeArguments()).isSubtypeOf(candidateResult)) {
                                    include = false;
                                    break;
                                }
                            }
                            if (include) {
                                if (result==null || candidateResult.isSubtypeOf(result)) {
                                    result = candidateResult;
                                }
                            }
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

}
