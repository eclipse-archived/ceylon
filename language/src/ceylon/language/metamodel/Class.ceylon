import ceylon.language.metamodel.declaration {
    ClassDeclaration
}

shared interface ClassType<out Type, in Arguments>
    satisfies ClassOrInterface<Type>
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
}

shared interface Class<out Type, in Arguments>
    satisfies ClassType<Type, Arguments> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
}

shared interface MemberClass<in Container, out Type, in Arguments>
        satisfies ClassType<Type, Arguments> & Member<Container, Class<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
