
shared interface Class<out Type=Anything, in Arguments=Nothing>
    satisfies ClassModel<Type, Arguments> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
}
