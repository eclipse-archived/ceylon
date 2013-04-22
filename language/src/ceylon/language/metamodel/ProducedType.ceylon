
shared interface ProducedType of ClassOrInterfaceType
                                 | TypeParameterType
                                 | UnionType
                                 | IntersectionType
                                 | nothingType {}

shared interface AppliedProducedType of AppliedClassOrInterfaceType<Anything>
                                      | AppliedUnionType
                                      | AppliedIntersectionType
                                      | appliedNothingType {}

shared interface TypeParameterType satisfies ProducedType {
    
    shared formal TypeParameter declaration;
}

shared interface ClassOrInterfaceType 
    of ClassType | InterfaceType
    satisfies ProducedType {
    
    shared formal ClassOrInterface declaration;
    
    shared formal Map<TypeParameter, ProducedType> typeArguments;
    
    shared formal ClassType? superclass;
    
    shared formal InterfaceType[] interfaces;
}

shared interface ClassType
    satisfies ClassOrInterfaceType {
    
    shared formal actual Class declaration;
}

shared interface InterfaceType
    satisfies ClassOrInterfaceType {
    
    shared formal actual Interface declaration;
}

shared interface UnionType satisfies ProducedType {
    
    shared formal List<ProducedType> caseTypes;
}

shared interface IntersectionType satisfies ProducedType {
    
    shared formal List<ProducedType> satisfiedTypes;
}

shared object nothingType satisfies ProducedType {}

shared interface AppliedClassOrInterfaceType<out Type> 
    of AppliedClassType<Type, Nothing[]> | AppliedInterfaceType<Type>
    satisfies AppliedProducedType {
    
    shared formal ClassOrInterface declaration;
    
    shared formal Map<TypeParameter, AppliedProducedType> typeArguments;
    
    shared formal AppliedClassType<Anything,Nothing[]>? superclass;
    
    shared formal AppliedInterfaceType<Anything>[] interfaces;
}

shared interface AppliedClassType<out Type, in Arguments>
    satisfies AppliedClassOrInterfaceType<Type> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
    
    shared formal actual Class declaration;
}

shared interface AppliedInterfaceType<out Type>
    satisfies AppliedClassOrInterfaceType<Type> {
    
    shared formal actual Interface declaration;
}

shared interface AppliedUnionType satisfies AppliedProducedType {
    
    shared formal List<AppliedProducedType> caseTypes;
}

shared interface AppliedIntersectionType satisfies AppliedProducedType {
    
    shared formal List<AppliedProducedType> satisfiedTypes;
}

shared object appliedNothingType satisfies AppliedProducedType {}
