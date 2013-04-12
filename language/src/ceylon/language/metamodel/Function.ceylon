shared interface Function<out Type, in Arguments> 
        satisfies Callable<Type,Arguments> & Declaration 
        given Arguments satisfies Anything[] {}