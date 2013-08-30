
shared interface Method<in Container, out Type=Anything, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Member<Container, Function<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
