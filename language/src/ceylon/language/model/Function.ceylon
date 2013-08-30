
shared interface Function<out Type=Anything, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Callable<Type, Arguments>
        given Arguments satisfies Anything[] {
}
