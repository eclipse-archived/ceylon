<T> => F<G<T>>(T) compose<F,G>
        (<T> => F<T>(T) f, <T> => G<T>(T) g)
        given F<T> given G<T>
        => <T>(T t) => f(g(t));

shared void testCompose() {
    T() lazy<T>(T t) => () => t;
    [T] singleton<T>(T t) => [t];
    
    alias Lazy<T> => T();
    alias Sing<T> => [T];
    
    value lazySingleton1 
            = compose<<T> => T(),<T> => [T]>
                (lazy, singleton);
    
    $type:"[Integer]()" value int1 = lazySingleton1(1);
    $type:"[String]()" value str1 = lazySingleton1("hello");
    
    $type:"Integer" value i1 = int1()[0];
    $type:"String" value s1 = str1()[0];
    
    value lazySingleton2 
            = compose<Lazy,Sing>
                (lazy, singleton);

    $type:"Lazy<Sing<Integer>>" value int2 = lazySingleton2(1);
    $type:"Lazy<Sing<String>>" value str2 = lazySingleton2("hello");
    
    $type:"Integer" value i2 = int2()[0];
    $type:"String" value s2 = str2()[0];
    

}