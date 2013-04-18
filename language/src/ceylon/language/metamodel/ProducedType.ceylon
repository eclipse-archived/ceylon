
shared interface ProducedType of ClassOrInterfaceType<Anything>
                                 | TypeParameterType
                                 | UnionType
                                 | IntersectionType
                                 | nothingType {}

shared interface TypeParameterType satisfies ProducedType {
    
    shared formal TypeParameter declaration;
}

shared interface ClassOrInterfaceType<out Type> 
    of ClassType<Type, Nothing[]> | InterfaceType<Type>
    satisfies ProducedType {
    
    shared formal ClassOrInterface<Type> declaration;
    
    shared formal Map<TypeParameter, ProducedType> typeArguments;
    
    shared formal ClassType<Anything,Nothing[]>? superclass;
    
    shared formal InterfaceType<Anything>[] interfaces;
}

shared interface ClassType<out Type, in Arguments>
    satisfies ClassOrInterfaceType<Type>
    given Arguments satisfies Anything[] {
    
    shared formal actual Class<Type, Arguments> declaration;
}

shared interface InterfaceType<out Type>
    satisfies ClassOrInterfaceType<Type> {
    
    shared formal actual Interface<Type> declaration;
}

shared interface UnionType satisfies ProducedType {
    
    shared formal List<ProducedType> caseTypes;
}

shared interface IntersectionType satisfies ProducedType {
    
    shared formal List<ProducedType> satisfiedTypes;
}

shared object nothingType satisfies ProducedType {}
