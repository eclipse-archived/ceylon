shared interface Class<out Type, in Arguments> 
        satisfies ClassOrInterface<Type> & 
                  Callable<Type,Arguments> & Declaration 
        given Arguments satisfies Anything[] {
    shared formal actual ClassType<Type, Anything[]> apply(ProducedType* types);
}