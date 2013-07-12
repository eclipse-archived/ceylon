
shared interface Class<out Type, in Arguments>
    satisfies ClassType<Type, Arguments> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
}
