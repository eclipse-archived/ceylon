import ceylon.language.meta.declaration {
    ClassOrInterfaceDeclaration
}

import ceylon.language.meta.model {
    ClosedType = Type
}

"Model of a class or interface that you can inspect.
 
 The models of classes and interfaces are also closed types."
shared sealed interface ClassOrInterface<out Type=Anything> 
    of ClassModel<Type, Nothing> | InterfaceModel<Type>
    satisfies Model & Generic & ClosedType<Type> {
    
    "The declaration for this class or interface."
    shared formal actual ClassOrInterfaceDeclaration declaration;

    "The extended closed type for this class or interface. 
     Note that the [[Anything|ceylon.language::Anything]] type
     has no extended type since it is the top of the type hierarchy."
    shared formal ClassModel<>? extendedType;
    
    "The list of closed types that this class or interface satisfies."
    shared formal InterfaceModel<>[] satisfiedTypes;

    "The list of case values for this type. This omits any case type to 
     only contain case values."
    shared formal Type[] caseValues;

    // FIXME: move all these to Type
    // FIXME: introduce MemberClassOrInterface?
    // if I do that I have to give up the enumerated type of ClassModel | InterfaceModel here, so let's not do that for now,
    // since I don't quite see what we would gain

    "Looks up a shared member class or interface by name, 
     Returns `null` if no such member matches. 
     This includes inherited member classes or interface but not unshared 
     classes or interfaces."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container` or `Kind` type arguments are 
         not compatible with the actual result.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal Member<Container, Kind>? getClassOrInterface
            <Container=Nothing, Kind=ClassOrInterface<>>
            (String name, ClosedType<Anything>* types)
                given Kind satisfies ClassOrInterface<Anything>;

    "Looks up a member class or interface directly declared on this class or interface, by name. 
     Returns `null` if no such class or interface exists 
     This includes unshared member classes or interfaces but not inherited classes or interfaces."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container` or `Kind` type arguments are not 
         compatible with the actual result.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal Member<Container, Kind>? getDeclaredClassOrInterface
            <Container=Nothing, Kind=ClassOrInterface<>>
            (String name, ClosedType<Anything>* types)
                given Kind satisfies ClassOrInterface<Anything>;

    "Looks up a shared member class by name, 
     Returns `null` if no such member matches. 
     This includes inherited member classes but not unshared 
     classes."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Type` or `Arguments` type arguments 
         are not compatible with the actual result, 
         or if the corresponding member is not a `MemberClass`.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal MemberClass<Container, Type, Arguments>? getClass
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
                given Arguments satisfies Anything[];

    "Looks up a member class directly declared on this class or interface, by name. 
     Returns `null` if no such class exists 
     This includes unshared classes but not inherited classes."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Type` or `Arguments` type arguments 
         are not compatible with the actual result, 
         or if the corresponding member is not a `MemberClass`.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal MemberClass<Container, Type, Arguments>? getDeclaredClass
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
                given Arguments satisfies Anything[];

    "Looks up a shared member interface by name, 
     Returns `null` if no such member matches. 
     This includes inherited member interfaces but not unshared 
     interfaces."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container` or `Type` type arguments are not 
         compatible with the actual result, 
         or if the corresponding member is not a `MemberInterface`.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal MemberInterface<Container, Type>? getInterface
            <Container=Nothing, Type=Anything>
            (String name, ClosedType<Anything>* types);
    
    "Looks up a member interface directly declared on this class or interface, by name. 
     Returns `null` if no such interface exists 
     This includes unshared interface but not inherited interface."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container` or `Type` type arguments are not 
         compatible with the actual result, 
         or if the corresponding member is not a `MemberInterface`.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal MemberInterface<Container, Type>? getDeclaredInterface
            <Container=Nothing, Type=Anything>
            (String name, ClosedType<Anything>* types);
    
    "Looks up a shared method by name, 
     Returns `null` if no such method matches. 
     This includes inherited methods but not unshared 
     methods."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Type` or `Arguments` type arguments 
         are not compatible with the actual result.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible
         with the actual result's type parameters.")
    shared formal Method<Container, Type, Arguments>? getMethod
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
                given Arguments satisfies Anything[];

    "Looks up a method directly declared on this class or interface, by name. 
     Returns `null` if no such method exists 
     This includes unshared methods but not inherited methods."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Type` or `Arguments` type arguments 
         are not compatible with the actual result.")
    throws(`class TypeApplicationException`, 
        "If the specified closed type argument values are not compatible 
         with the actual result's type parameters.")
    shared formal Method<Container, Type, Arguments>? getDeclaredMethod
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (String name, ClosedType<Anything>* types)
                given Arguments satisfies Anything[];
    
    "Looks up an attribute by name, 
     Returns `null` if no such attribute matches. 
     This includes inherited attributes but not unshared 
     attributes."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Get` or `Set` type arguments are not 
         compatible with the actual result.")
    shared formal Attribute<Container, Get, Set>? getAttribute
            <Container=Nothing, Get=Anything, Set=Nothing>
            (String name);

    "Looks up an attribute directly declared on this class or interface, by name. 
     Returns `null` if no such attribute exists 
     This includes unshared attributes but not inherited attributes."
    throws(`class IncompatibleTypeException`, 
        "If the specified `Container`, `Get` or `Set` type arguments are
         not compatible with the actual result.")
    shared formal Attribute<Container, Get, Set>? getDeclaredAttribute
            <Container=Nothing, Get=Anything, Set=Nothing>
            (String name);

    "Returns the list of attributes directly declared on this class or interface 
     and annotated with all the specified annotations.
     This includes unshared attributes but not inherited attributes."
    shared formal Attribute<Container, Get, Set>[] getDeclaredAttributes
            <Container=Nothing, Get=Anything, Set=Nothing>
            (ClosedType<Annotation>* annotationTypes);

    "Returns the list of shared attributes on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited attributes but not unshared attributes."
    shared formal Attribute<Container, Get, Set>[] getAttributes
            <Container=Nothing, Get=Anything, Set=Nothing>
            (ClosedType<Annotation>* annotationTypes);

    "Returns the list of methods directly declared on this class or interface 
     and annotated with all the specified annotations.
     This includes unshared methods but not inherited methods."
    shared formal Method<Container, Type, Arguments>[] getDeclaredMethods
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];

    "Returns the list of shared methods on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited methods but not unshared methods."
    shared formal Method<Container, Type, Arguments>[] getMethods
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];

    "Returns the list of member classes directly declared on this class or interface 
     and annotated with all the specified annotations.
     This includes unshared member classes but not inherited member classes."
    shared formal MemberClass<Container, Type, Arguments>[] getDeclaredClasses
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];
    
    "Returns the list of shared member classes on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited member classes but not unshared member classes."
    shared formal MemberClass<Container, Type, Arguments>[] getClasses
            <Container=Nothing, Type=Anything, Arguments=Nothing>
            (ClosedType<Annotation>* annotationTypes)
                given Arguments satisfies Anything[];

    "Returns the list of member interfaces directly declared on this class or interface 
     and annotated with all the specified annotations.
     This includes unshared member interfaces but not inherited member interfaces."
    shared formal MemberInterface<Container, Type>[] getDeclaredInterfaces
            <Container=Nothing, Type=Anything>
            (ClosedType<Annotation>* annotationTypes);
    
    "Returns the list of shared member interfaces on this class or interface
     and annotated with all the specified annotations. 
     This includes inherited member interfaces but not unshared member interfaces."
    shared formal MemberInterface<Container, Type>[] getInterfaces
            <Container=Nothing, Type=Anything>
            (ClosedType<Annotation>* annotationTypes);
}

