
shared interface ProducedType{}

shared interface ClassOrInterfaceType<out Type> satisfies ProducedType{
    
    shared formal ClassOrInterface<Type> declaration;
    
    shared formal Map<TypeParameter, ProducedType> typeArguments;
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
