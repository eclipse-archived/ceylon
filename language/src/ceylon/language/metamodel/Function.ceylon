shared interface Function<out Type, in Arguments> 
        satisfies Callable<Type,Arguments> & Declaration & Parameterised 
        given Arguments satisfies Anything[] {

    shared formal ProducedType type;
}