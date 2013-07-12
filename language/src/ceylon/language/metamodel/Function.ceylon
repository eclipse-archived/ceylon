
shared interface Function<out Type, in Arguments>
        satisfies FunctionType<Type, Arguments> & Callable<Type, Arguments>
        given Arguments satisfies Anything[] {
}
