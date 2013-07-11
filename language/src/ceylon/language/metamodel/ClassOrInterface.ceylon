import ceylon.language.metamodel.declaration {
    ClassOrInterfaceDeclaration,
    TypeParameter
}

import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface ClassOrInterface<out Type> 
    of Class<Type, Nothing> | Interface<Type>
    satisfies DeclarationType & ClosedType {
    
    shared formal actual ClassOrInterfaceDeclaration declaration;
    
    // FIXME: turn that into an interface and add to Function too
    shared formal Map<TypeParameter, ClosedType> typeArguments;
    
    shared formal Class<Anything, Nothing>? superclass;
    
    shared formal Interface<Anything>[] interfaces;

    shared formal Member<SubType, Kind>? getClassOrInterface<SubType, Kind>(String name, ClosedType* types)
        given Kind satisfies ClassOrInterface<Anything>;
    
    shared formal Member<SubType, Kind>? getFunction<SubType, Kind>(String name, ClosedType* types)
        given Kind satisfies Function<Anything, Nothing>;
    
    shared formal Member<SubType, Kind>? getAttribute<SubType, Kind>(String name)
        given Kind satisfies Value<Anything>;
}

