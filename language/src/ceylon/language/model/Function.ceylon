
shared interface Function<out Type, in Arguments>
        satisfies FunctionModel<Type, Arguments> & Callable<Type, Arguments>
        given Arguments satisfies Anything[] {
}
