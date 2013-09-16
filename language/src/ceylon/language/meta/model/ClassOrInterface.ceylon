import ceylon.language.meta.declaration {
    ClassOrInterfaceDeclaration,
    TypeParameter
}

import ceylon.language.meta.model {
    ClosedType = Type
}

shared interface ClassOrInterface<out Type=Anything> 
    of ClassModel<Type, Nothing> | InterfaceModel<Type>
    satisfies Model & Generic & ClosedType<Type> {
    
    shared formal actual ClassOrInterfaceDeclaration declaration;
    
    shared formal ClassModel<Anything, Nothing>? extendedType;
    
    shared formal InterfaceModel<Anything>[] satisfiedTypes;

    // FIXME: move all these to Type
    // FIXME: introduce MemberClassOrInterface?
    // if I do that I have to give up the enumerated type of ClassModel | InterfaceModel here, so let's not do that for now,
    // since I don't quite see what we would gain
    // FIXME: should we turn this into getClass and getInterface like we do for the rest?
    shared formal Member<SubType, Kind>? getClassOrInterface<SubType, Kind>(String name, ClosedType<Anything>* types)
        given Kind satisfies ClassOrInterface<Anything>;
    
    shared formal Method<SubType, Type, Arguments>? getMethod<SubType, Type, Arguments>(String name, ClosedType<Anything>* types)
        given Arguments satisfies Anything[];
    
    shared formal Attribute<SubType, Type>? getAttribute<SubType, Type>(String name);

    shared formal VariableAttribute<SubType, Type>? getVariableAttribute<SubType, Type>(String name);
}

