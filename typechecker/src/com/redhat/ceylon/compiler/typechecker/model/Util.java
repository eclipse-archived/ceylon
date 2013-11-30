package com.redhat.ceylon.compiler.typechecker.model;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Util {
    
    /**
     * Is the second scope contained by the first scope?
     */
    public static boolean contains(Scope outer, Scope inner) {
        if (outer != null) {
            while (inner!=null) {
                if (inner.equals(outer)) {
                    return true;
                }
                inner = inner.getScope();
            }
        }
        return false;
    }
    
    /**
     * Get the nearest containing scope that is not a
     * ConditionScope. 
     */
    public static Scope getRealScope(Scope scope) {
        while (!(scope instanceof Package)) {
            if (!(scope instanceof ConditionScope)) {
                return scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the class or interface that "this" and "super" 
     * refer to. 
     */
    public static ClassOrInterface getContainingClassOrInterface(Scope scope) {
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                return (ClassOrInterface) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the class or interface that "outer" refers to. 
     */
    public static ProducedType getOuterClassOrInterface(Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    return ((ClassOrInterface) scope).getType();
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Convenience method to bind a single type argument 
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, 
            ProducedType typeArgument) {
        if (declaration==null) return null;
        return declaration.getProducedType(null, singletonList(typeArgument));
    }

    /**
     * Convenience method to bind a list of type arguments
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, 
            ProducedType... typeArguments) {
        if (declaration==null) return null;
        return declaration.getProducedType(null, asList(typeArguments));
    }

    public static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !(declaration instanceof Setter) && //return getters, not setters
            !declaration.isAnonymous(); //don't return the type associated with an object dec 
    }
    
    public static boolean isAbstraction(Declaration d) {
        return d instanceof Functional && 
                ((Functional) d).isAbstraction();
    }

    public static boolean notOverloaded(Declaration d) {
        return !(d instanceof Functional) || 
                !((Functional) d).isOverloaded() ||
                ((Functional) d).isAbstraction();
    }
    
    public static boolean isOverloadedVersion(Declaration decl) {
        return (decl instanceof Functional) &&
                ((Functional)decl).isOverloaded();
    }

    static boolean hasMatchingSignature(List<ProducedType> signature, 
            boolean ellipsis, Declaration d) {
        return hasMatchingSignature(signature, ellipsis, d, true);
    }
    
    static boolean hasMatchingSignature(List<ProducedType> signature, 
            boolean spread, Declaration d, boolean excludeAbstractClasses) {
        if (excludeAbstractClasses && 
                d instanceof Class && ((Class) d).isAbstract()) {
            return false;
        }
        if (d instanceof Functional) {
            Functional f = (Functional) d;
            if (f.isAbstraction()) {
                return false;
            }
            else {
                Unit unit = d.getUnit();
                List<ParameterList> pls = f.getParameterLists();
                if (pls!=null && !pls.isEmpty()) {
                    List<Parameter> params = pls.get(0).getParameters();
                    int size = params.size();
                    boolean hasSeqParam = pls.get(0).hasSequencedParameter();
                    int sigSize = signature.size();
                    if (hasSeqParam) {
                        size--;
                        if (sigSize<size) {
                            return false;
                        }
                    }
                    else if (sigSize!=size) {
                        return false;
                    }
                    for (int i=0; i<size; i++) {
                        MethodOrValue pm = params.get(i).getModel();
                        if (pm==null) return false;
                        ProducedType pdt = pm.getType();
                        if (pdt==null) return false;
                        ProducedType sdt = signature.get(i);
                        if (!matches(pdt, sdt, d)) return false;
                    }
                    if (hasSeqParam) {
                        ProducedType pdt = params.get(size).getModel().getType();
                        if (pdt==null || pdt.getTypeArgumentList().isEmpty()) return false;
                        //Note: don't use Unit.getIterableType() because this
                        //      gets called from model loader out-of-phase
                        ProducedType ipdt = pdt.getTypeArgumentList().get(0);  
                        for (int i=size; i<sigSize; i++) {
                            if (spread && i==sigSize-1) {
                                ProducedType sdt = signature.get(i);
                                ProducedType isdt = unit.getIteratedType(sdt);
                                if (!matches(ipdt, isdt, d)) return false;
                            }
                            else {
                                ProducedType sdt = signature.get(i);
                                if (!matches(ipdt, sdt, d)) return false;
                            }
                        }
                    }
                    else if (spread) {
                        // if the method doesn't take sequenced params
                        // and we have an ellipsis let's not use it 
                        // since we expect a variadic method
                        // TODO: this is basically wrong now that we
                        //       can spread tuples
                        return false;
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }
    
    private static boolean matches(ProducedType pdt, ProducedType sdt, Declaration d) {
        if (pdt==null || sdt==null) return false;
        //Ignore optionality for resolving overloads, since
        //all Java parameters are treated as optional
        //Except in the case of primitive parameters
        Unit unit = d.getUnit();
        ProducedType nt = unit.getNullValueDeclaration().getType();
        if (nt.isSubtypeOf(sdt) && !nt.isSubtypeOf(pdt)) return false; //only for primitives
        ProducedType paramType = unit.getDefiniteType(pdt);
        ProducedType sigType = unit.getDefiniteType(sdt);
        ProducedType ast = sigType.getSupertype(unit.getArrayDeclaration());
        if (ast!=null) sigType = ast;
        if (sigType.isSubtypeOf(unit.getNullDeclaration().getType())) {
            return true;
        }
        if (isTypeUnknown(sigType) || isTypeUnknown(paramType)) return false;
        if (!erase(sigType.getDeclaration())
                .inherits(erase(paramType.getDeclaration())) &&
                underlyingTypesUnequal(paramType, sigType)) {
            return false;
        }
        return true;
    }

    private static boolean underlyingTypesUnequal(ProducedType paramType,
            ProducedType sigType) {
        String sut = sigType.getUnderlyingType();
        String put = paramType.getUnderlyingType();
        return sut==null || put==null || !sut.equals(put);
    }
    
    static boolean betterMatch(Declaration d, Declaration r) {
        if (d instanceof Functional && r instanceof Functional) {
            List<ParameterList> dpls = ((Functional) d).getParameterLists();
            List<ParameterList> rpls = ((Functional) r).getParameterLists();
            if (dpls!=null&&!dpls.isEmpty() && rpls!=null&&!rpls.isEmpty()) {
                List<Parameter> dpl = dpls.get(0).getParameters();
                List<Parameter> rpl = rpls.get(0).getParameters();
                int dplSize = dpl.size();
                int rplSize = rpl.size();
                //ignore sequenced parameters
                boolean dhsp = dpls.get(0).hasSequencedParameter();
                boolean rhsp = rpls.get(0).hasSequencedParameter();
                //always prefer a signature without varargs 
                //over one with a varargs parameter
                if (!dhsp && rhsp) {
                    return true;
                }
                if (dhsp && !rhsp) {
                    return false;
                }
                //ignore sequenced parameters
                if (dhsp) { dplSize--; }
                if (rhsp) { rplSize--; }
                if (dplSize==rplSize) {
                    //if all parameters are of more specific
                    //or equal type, prefer it
                    Unit unit = d.getUnit();
                    for (int i=0; i<dplSize; i++) {
                        ProducedType paramType = unit.getDefiniteType(dpl.get(i).getModel().getType());
                        ProducedType otherType = unit.getDefiniteType(rpl.get(i).getModel().getType());
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        if (!erase(paramType.getDeclaration())
                                    .inherits(erase(otherType.getDeclaration())) &&
                                underlyingTypesUnequal(paramType, otherType)) {
                            return false;
                        }
                    }
                    // check sequenced parameters last
                    if (dhsp && rhsp){
                        ProducedType paramType = unit.getDefiniteType(dpl.get(dplSize).getModel().getType());
                        ProducedType otherType = unit.getDefiniteType(rpl.get(dplSize).getModel().getType());
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        paramType = unit.getIteratedType(paramType);
                        otherType = unit.getIteratedType(otherType);
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        if (!erase(paramType.getDeclaration())
                                .inherits(erase(otherType.getDeclaration())) &&
                            underlyingTypesUnequal(paramType, otherType)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNamed(String name, Declaration d) {
        String dname = d.getName();
        return dname!=null && dname.equals(name);
    }
    
    private static TypeDeclaration erase(TypeDeclaration paramType) {
        if (paramType instanceof TypeParameter) {
            if ( paramType.getSatisfiedTypes().isEmpty() ) {
                return paramType.getExtendedTypeDeclaration();
            }
            else {
                //TODO: is this actually correct? What is Java's
                //      rule here?
                return paramType.getSatisfiedTypeDeclarations().get(0);
            }
        }
        else if (paramType instanceof UnionType || 
                paramType instanceof IntersectionType) {
            //TODO: this is pretty sucky, cos in theory a
            //      union or intersection might be assignable
            //      to the parameter type with a typecast
            return paramType.getUnit().getObjectDeclaration();
        }
        else {
            return paramType;
        }
    }
    
    static boolean isNameMatching(String startingWith, Declaration d) {
        return d.getName()!=null && 
            d.getName().toLowerCase().startsWith(startingWith.toLowerCase());
    }
    
    /**
     * Collect together type arguments given a list of 
     * type arguments to a declaration and the receiving 
     * type.
     * 
     * @return a map of type parameter to type argument
     *  
     * @param declaration a declaration
     * @param receivingType the receiving produced type 
     *        of which the declaration is a member
     * @param typeArguments explicit or inferred type 
     *        arguments of the declaration
     */
    static Map<TypeParameter,ProducedType> arguments(Declaration declaration, 
            ProducedType receivingType, List<ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> map = getArgumentsOfOuterType(receivingType);
        //now turn the type argument tuple into a
        //map from type parameter to argument
        if (declaration instanceof Generic) {
            Generic g = (Generic) declaration;
            List<TypeParameter> params = g.getTypeParameters();
            for (int i=0; i<params.size(); i++) {
                if (typeArguments.size()>i) {
                    map.put(params.get(i), typeArguments.get(i));
                }
            }
        }
        return map;
    }

    public static Map<TypeParameter, ProducedType> getArgumentsOfOuterType(
            ProducedType receivingType) {
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        //make sure we collect all type arguments
        //from the whole qualified type!
        ProducedType dt = receivingType;
        while (dt!=null) {
            map.putAll(dt.getTypeArguments());
            dt = dt.getQualifyingType();
        }
        return map;
    }
    
    static <T> List<T> list(List<T> list, T element) {
        List<T> result = new ArrayList<T>(list.size()+1);
        result.addAll(list);
        result.add(element);
        return result;
    }

    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form a union type, taking into
     * account that a subtype is a "duplicate" of its
     * supertype.
     */
    public static void addToUnion(List<ProducedType> list, ProducedType pt) {
        if (pt==null) {
            return;
        }
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType t: pt.getDeclaration().getCaseTypes() ) {
                addToUnion( list, t.substitute(pt.getTypeArguments()) );
            }
        }
        else if (pt.isWellDefined()) {
            boolean add=true;
            for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                if (pt.isSubtypeOf(t)) {
                    add=false;
                    break;
                }
                else if (pt.isSupertypeOf(t)) {
                    iter.remove();
                }
            }
            if (add) {
                list.add(pt);
            }
        }
    }
    
    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form an intersection type, taking 
     * into account that a supertype is a "duplicate" of its
     * subtype.
     */
    public static void addToIntersection(List<ProducedType> list, ProducedType pt, 
            Unit unit) {
        if (pt==null) {
            return;
        }
        if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType t: pt.getDeclaration().getSatisfiedTypes() ) {
                addToIntersection(list, t.substitute(pt.getTypeArguments()), unit);
            }
        }
        else {
            //implement the rule that Foo&Bar==Nothing if 
            //there exists some enumerated type Baz with
            //    Baz of Foo | Bar 
            //(the intersection of disjoint types is empty)
            for (ProducedType supertype: pt.getSupertypes()) {
                List<TypeDeclaration> ctds = supertype.getDeclaration()
                        .getCaseTypeDeclarations();
                if (ctds!=null) {
                    TypeDeclaration ctd=null;
                    for (TypeDeclaration ct: ctds) {
                        if (pt.getSupertype(ct)!=null) {
                            ctd = ct;
                            break;
                        }
                    }
                    if (ctd!=null) {
                        for (TypeDeclaration ct: ctds) {
                            if (ct!=ctd) {
                                for (ProducedType t: list) {
                                    if (t.getSupertype(ct)!=null) {
                                        list.clear();
                                        list.add(new NothingType(unit).getType());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Boolean add = pt.isWellDefined();
            if (add) {
                for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                    ProducedType t = iter.next();
                    if (pt.isSupertypeOf(t)) {
                        add = false;
                        break;
                    }
                    else if (pt.isSubtypeOf(t)) {
                        iter.remove();
                    }
                    else if (haveUninhabitableIntersection(pt,t, unit)) {
                        list.clear();
                        list.add(unit.getNothingDeclaration().getType());
                        return;
                    }
                    else if (pt.getDeclaration() instanceof ClassOrInterface && 
                            t.getDeclaration() instanceof ClassOrInterface && 
                            pt.getDeclaration().equals(t.getDeclaration()) &&
                            !pt.containsUnknowns() &&
                            !t.containsUnknowns()) {
                        //canonicalize T<InX,OutX>&T<InY,OutY> to T<InX|InY,OutX&OutY>
                        ProducedType pi = principalInstantiation(pt.getDeclaration(), pt, t, unit);
                        if (!pi.containsUnknowns()) {
                            iter.remove();
                            list.add(pi);
                            return;
                        }
                    }
                }
            }
            if (add && list.size()>1) {
                //it is possible to have a type that is a
                //supertype of the intersection, even though
                //it is not a supertype of any of the 
                //intersected types!
                IntersectionType it = new IntersectionType(unit);
                it.setSatisfiedTypes(list);
                ProducedType type = it.canonicalize().getType();
                if (pt.isSupertypeOf(type)) {
                    add = false;
                }
            }
            if (add) {
                list.add(pt);
            }
        }
    }

    /**
     * The meet of two classes unrelated by inheritance,
     * or of Null with an interface type is empty. The meet
     * of an anonymous class with a type to which it is not
     * assignable is empty.
     */
    private static boolean emptyMeet(ProducedType p, ProducedType q, Unit unit) {
        TypeDeclaration nd = unit.getNullDeclaration(); //TODO what about the anonymous type of null?
        TypeDeclaration pd = p.getDeclaration();
        TypeDeclaration qd = q.getDeclaration();
        if (pd instanceof TypeParameter) {
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(pd.getSatisfiedTypes());
            p = it.canonicalize().getType();
            pd = p.getDeclaration();
        }
        if (qd instanceof TypeParameter) {
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(qd.getSatisfiedTypes());
            q = it.canonicalize().getType();
            qd = q.getDeclaration();
        }
        if (qd instanceof IntersectionType) {
            for (ProducedType t: qd.getSatisfiedTypes()) {
                if (emptyMeet(p,t, unit)) {
                    return true;
                }
            }
            return false;
        }
        if (pd instanceof IntersectionType) {
            for (ProducedType t: pd.getSatisfiedTypes()) {
                if (emptyMeet(q,t, unit)) {
                    return true;
                }
            }
            return false;
        }
        if (qd instanceof UnionType) {
            for (ProducedType t: qd.getCaseTypes()) {
                if (!emptyMeet(p,t, unit)) {
                    return false;
                }
            }
            return true;
        }
        if (pd instanceof UnionType) {
            for (ProducedType t: pd.getCaseTypes()) {
                if (!emptyMeet(q,t, unit)) {
                    return false;
                }
            }
            return true;
        }
        if (pd instanceof Class && qd instanceof Class ||
            pd instanceof Interface && qd instanceof Class &&
                    qd.equals(nd) ||
            qd instanceof Interface && pd instanceof Class &&
                    pd.equals(nd)) {
            if (q.getSupertype(pd)==null &&
                p.getSupertype(qd)==null) {
                return true;
            }
        }
        if (pd.isFinal()) {
            if (pd.getTypeParameters().isEmpty() &&
                    !q.containsTypeParameters() &&
                    !p.isSubtypeOf(q) &&
                    !(qd instanceof UnknownType)) {
                return true;
            }
            if (qd instanceof ClassOrInterface &&
                    p.getSupertype(qd)==null) {
                return true;
            }
        }
        if (qd.isFinal()) { 
            if (qd.getTypeParameters().isEmpty() &&
                    !p.containsTypeParameters() &&
                    !q.isSubtypeOf(p) &&
                    !(pd instanceof UnknownType)) {
                return true;
            }
            if (pd instanceof ClassOrInterface &&
                    q.getSupertype(pd)==null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given two instantiations of a qualified type constructor, 
     * determine the qualifying type of the principal 
     * instantiation of that type constructor for the 
     * intersection of the two types.
     */
    static ProducedType principalQualifyingType(ProducedType pt,
            ProducedType t, TypeDeclaration td, Unit unit) {
        ProducedType ptqt = pt.getQualifyingType();
        ProducedType tqt = t.getQualifyingType();
        if (ptqt!=null && tqt!=null && 
                td.getContainer() instanceof TypeDeclaration) {
            TypeDeclaration qtd = (TypeDeclaration) td.getContainer();
            ProducedType pst = ptqt.getSupertype(qtd);
            ProducedType st = tqt.getSupertype(qtd);
            if (pst!=null && st!=null) {
                return principalInstantiation(qtd, pst, st, unit);
            }
        }
        return null;
    }
    
    /**
     * Determine if a type of form X<P>&X<Q> is equivalent to
     * Nothing where X<T> is invariant in T.
     * @param p the argument type P
     * @param q the argument type Q
     */
    private static boolean haveUninhabitableIntersection
            (ProducedType p, ProducedType q, Unit unit) {
        return emptyMeet(p, q, unit) ||
                hasEmptyIntersectionOfInvariantInstantiations(p, q);
    }

    private static boolean hasEmptyIntersectionOfInvariantInstantiations(
            ProducedType p, ProducedType q) {
//        if (!p.containsTypeParameters() && !q.containsTypeParameters()) {
            List<TypeDeclaration> stds = p.getDeclaration().getSuperTypeDeclarations();
            stds.retainAll(q.getDeclaration().getSuperTypeDeclarations());
            for (TypeDeclaration std: stds) {
                ProducedType pst = null;
                ProducedType qst = null;
                for (TypeParameter tp: std.getTypeParameters()) {
                    if (tp.isInvariant()) {
                        if (pst==null) pst = p.getSupertype(std);
                        if (qst==null) qst = q.getSupertype(std);
                        ProducedType psta = pst.getTypeArguments().get(tp);
                        ProducedType qsta = qst.getTypeArguments().get(tp);
                        if (psta!=null && psta.isWellDefined() &&
                                qsta!=null && psta.isWellDefined() &&
                                //what about types with UnknownType as an arg?
                                !pst.containsTypeParameters() && 
                                !qst.containsTypeParameters() &&
                                !pst.isExactly(qst)) {
                            return true;
                        }
                    }
                }
            }
//        }
        return false;
    }
    
    public static String formatPath(List<String> path, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<path.size(); i++) {
            String pathPart = path.get(i);
            if (! pathPart.isEmpty()) {
                sb.append(pathPart);
                if (i<path.size()-1) sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String formatPath(List<String> path) {
        return formatPath(path, '.');
    }
    
    static boolean addToSupertypes(List<ProducedType> list, ProducedType st) {
        for (ProducedType et: list) {
            if (st.getDeclaration().equals(et.getDeclaration()) && //return both a type and its self type
                    st.isExactlyInternal(et)) {
                return false;
            }
        }
        list.add(st);
        return true;
    }

    public static ProducedType unionType(ProducedType lhst, ProducedType rhst, 
            Unit unit) {
        List<ProducedType> list = new ArrayList<ProducedType>(2);
        addToUnion(list, rhst);
        addToUnion(list, lhst);
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        return ut.getType();
    }

    public static ProducedType intersectionType(ProducedType lhst, ProducedType rhst, 
            Unit unit) {
        List<ProducedType> list = new ArrayList<ProducedType>(2);
        addToIntersection(list, rhst, unit);
        addToIntersection(list, lhst, unit);
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        return it.canonicalize().getType();
    }

    public static boolean isElementOfUnion(UnionType ut, ClassOrInterface ci) {
        for (TypeDeclaration ct: ut.getCaseTypeDeclarations()) {
            if (ct instanceof ClassOrInterface && ct.equals(ci)) {
                return true;
            }
        }
        return false;
    }
    
    public static Declaration lookupMember(List<Declaration> members, String name,
            List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = new ArrayList<Declaration>(1);
        Declaration inexactMatch = null;
        for (Declaration d: members) {
            if (isResolvable(d) && isNamed(name, d)) {
                if (signature==null) {
                    //no argument types: either a type 
                    //declaration, an attribute, or a method 
                    //reference - don't return overloaded
                    //forms of the declaration (instead
                    //return the "abstraction" of them)
                    if (notOverloaded(d)) {
                        return d;
                    }
                }
                else {
                    if (notOverloaded(d)) {
                        //we have found either a non-overloaded
                        //declaration, or the "abstraction" 
                        //which of all the overloaded forms 
                        //of the declaration
                        //Note: I could not do this optimization
                        //      because then it could not distinguish
                        //      between Java open() and isOpen()
                        /*if (!isAbstraction(d)) {
                            return d;
                        }*/
                        inexactMatch = d;
                    }
                    if (hasMatchingSignature(signature, ellipsis, d)) {
                        //we have found an exactly matching 
                        //overloaded declaration
                        addIfBetterMatch(results, d);
                    }
                }
            }
        }
        switch (results.size()) {
        case 0:
            //no exact match, so return the non-overloaded
            //declaration or the "abstraction" of the 
            //overloaded declaration
            return inexactMatch;
        case 1:
            //exactly one exact match, so return it
            return results.get(0);
        default:
            //more than one matching overloaded declaration,
            //so return the "abstraction" of the overloaded
            //declaration
            return inexactMatch;
        }
    }

    private static void addIfBetterMatch(List<Declaration> results, Declaration d) {
        boolean add=true;
        for (Iterator<Declaration> i = results.iterator(); i.hasNext();) {
            Declaration o = i.next();
            if (betterMatch(d, o)) {
                i.remove();
            }
            else if (betterMatch(o, d)) { //TODO: note asymmetry here resulting in nondeterminate behavior!
                add=false;
            }
        }
        if (add) results.add(d);
    }
    
    public static Declaration findMatchingOverloadedClass(Class abstractionClass, 
            List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = new ArrayList<Declaration>(1);
        if (!abstractionClass.isAbstraction()) {
            return abstractionClass;
        }
        for (Declaration overloaded: abstractionClass.getOverloads()) {
            if (hasMatchingSignature(signature, ellipsis, overloaded, false)) {
                addIfBetterMatch(results, overloaded);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        }
        return abstractionClass;
    }

    public static boolean isTypeUnknown(ProducedType type) {
        return type==null || type.getDeclaration()==null ||
                type.containsUnknowns();
    }

    public static List<ProducedType> getSignature(Declaration dec) {
        if(dec instanceof Functional == false)
            return null;
        List<ParameterList> parameterLists = ((Functional)dec).getParameterLists();
        if(parameterLists == null || parameterLists.isEmpty())
            return null;
        ParameterList parameterList = parameterLists.get(0);
        if(parameterList == null || parameterList.getParameters() == null)
            return null;
        int size = parameterList.getParameters().size();
        List<ProducedType> signature = new ArrayList<ProducedType>(size);
        for(Parameter param : parameterList.getParameters()){
            signature.add(param.getModel()==null ? 
                    new UnknownType(dec.getUnit()).getType() : 
                    param.getModel().getType());
        }
        return signature;
    }
    
    public static boolean isCompletelyVisible(Declaration member, ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisible(member, pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if ( at!=null && !isCompletelyVisible(member, at) ) {
                    return false;
                }
            }
            return true;
        }
    }

    static boolean isVisible(Declaration member, TypeDeclaration type) {
        return type instanceof TypeParameter || 
                type.isVisible(member.getVisibleScope());
    }

    /**
     * Given two instantiations of the same type constructor,
     * construct a principal instantiation that is a supertype
     * of both. This is impossible in the following special
     * cases:
     * 
     * - an abstract class which does not obey the principal
     *   instantiation inheritance rule
     * - an intersection between two instantiations of the
     *   same type where one argument is a type parameter
     * 
     * Nevertheless, we give it our best shot!
     */
    public static ProducedType principalInstantiation(
            TypeDeclaration dec, ProducedType first, ProducedType second, 
            Unit unit) {
        List<TypeParameter> tps = dec.getTypeParameters();
        List<ProducedType> args = new ArrayList<ProducedType>(tps.size());
        for (TypeParameter tp: tps) {
            ProducedType arg;
            ProducedType rta = first.getTypeArguments().get(tp);
            ProducedType prta = second.getTypeArguments().get(tp);
            if (tp.isContravariant()) {
                arg = unionType(rta, prta, unit);
            }
            else if (tp.isCovariant()) {
                arg = intersectionType(rta, prta, unit);
            }
            else {
                //invariant type
                if (rta.isExactly(prta)) {
                    arg = rta;
                }
                else if (rta.containsTypeParameters() ||
                         prta.containsTypeParameters()) {
                    //type parameters that might represent 
                    //equivalent types at runtime. This is
                    //a hole in our type system!
                    arg = new UnknownType(unit).getType();
                }
                else {
                    //the type arguments are distinct, and the
                    //intersection is Nothing, so there is
                    //no reasonable principal instantiation
                    return unit.getNothingDeclaration().getType();
                }
            }
            args.add(arg);
        }
        return dec.getProducedType(principalQualifyingType(first, second, dec, unit), args);
    }
    
    public static boolean areConsistentSupertypes(ProducedType st1, 
            ProducedType st2, Unit unit) {
        //can't inherit two instantiations of an invariant type
        //Note: I don't think we need to check type parameters of 
        //      the qualifying type, since you're not allowed to
        //      subtype an arbitrary instantiation of a nested
        //      type - only supertypes of the outer type
        for (TypeParameter tp: st1.getDeclaration().getTypeParameters()) {
            if (!tp.isCovariant() && !tp.isContravariant()) {
                ProducedType ta1 = st1.getTypeArguments().get(tp);
                ProducedType ta2 = st2.getTypeArguments().get(tp);
                if (!ta1.isExactly(ta2)) {
                    return false;
                }
            }
        }
        return !intersectionType(st1, st2, unit).isNothing();
    }

    public static ProducedType intersectionOfSupertypes(ClassOrInterface ci) {
        List<ProducedType> list = new ArrayList<ProducedType>(ci.getSatisfiedTypes().size()+1);
        list.add(ci.getExtendedType());
        list.addAll(ci.getSatisfiedTypes());
        IntersectionType it = new IntersectionType(ci.getUnit());
        it.setSatisfiedTypes(list);
        ProducedType type = it.getType();
        return type;
    }

}
