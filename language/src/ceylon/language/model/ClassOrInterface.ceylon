import ceylon.language.model.declaration {
    ClassOrInterfaceDeclaration,
    TypeParameter
}

import ceylon.language.model {
    ClosedType = Type
}

shared interface ClassOrInterface<out Type=Anything> 
    of ClassModel<Type, Nothing> | InterfaceModel<Type>
    satisfies Model & Generic & ClosedType<Type> {
    
    shared formal actual ClassOrInterfaceDeclaration declaration;
    
    // FIXME: ClassModel
    shared formal Class<Anything, Nothing>? extendedType;
    
    // FIXME: InterfaceModel
    shared formal Interface<Anything>[] satisfiedTypes;

    // FIXME: move all these to Type
    // FIXME: introduce MemberClassOrInterface?
    shared formal Member<SubType, Kind>? getClassOrInterface<SubType, Kind>(String name, ClosedType<Anything>* types)
        given Kind satisfies ClassOrInterface<Anything>;
    
    shared formal Method<SubType, Type, Arguments>? getMethod<SubType, Type, Arguments>(String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];
    
    shared formal Attribute<SubType, Type>? getAttribute<SubType, Type>(String name);

    shared formal VariableAttribute<SubType, Type>? getVariableAttribute<SubType, Type>(String name);
}

