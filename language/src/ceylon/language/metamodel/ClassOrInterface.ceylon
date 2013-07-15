import ceylon.language.metamodel.declaration {
    ClassOrInterfaceDeclaration,
    TypeParameter
}

import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface ClassOrInterface<out Type> 
    of ClassModel<Type, Nothing> | InterfaceModel<Type>
    satisfies Model & ClosedType {
    
    shared formal actual ClassOrInterfaceDeclaration declaration;
    
    // FIXME: turn that into an interface and add to Function too
    shared formal Map<TypeParameter, ClosedType> typeArguments;
    
    // FIXME: ClassModel probably?
    shared formal Class<Anything, Nothing>? superclass;
    
    // FIXME: InterfaceModel probably?
    shared formal Interface<Anything>[] interfaces;

    // FIXME: introduce MemberClassOrInterface?
    shared formal Member<SubType, Kind>? getClassOrInterface<SubType, Kind>(String name, ClosedType* types)
        given Kind satisfies ClassOrInterface<Anything>;
    
    shared formal Method<SubType, Type, Arguments>? getMethod<SubType, Type, Arguments>(String name, ClosedType* types)
        given Arguments satisfies Anything[];
    
    shared formal Attribute<SubType, Type>? getAttribute<SubType, Type>(String name);

    shared formal VariableAttribute<SubType, Type>? getVariableAttribute<SubType, Type>(String name);
}

