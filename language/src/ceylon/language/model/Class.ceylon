
shared interface Class<out Type, in Arguments>
    satisfies ClassModel<Type, Arguments> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
}
