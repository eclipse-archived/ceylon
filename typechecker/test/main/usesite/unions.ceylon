void unioninstantiations() {
    interface I {}
    interface J {}
    class Bar<T>() { 
        shared T bar(T t) => nothing; 
    }
    
    <I|J> & Bar<out String> test1 = nothing;
    String s1 = test1.bar(nothing);
    @error value x1 = test1.bar("");
    
    <I|J> & Bar<in String> test2 = nothing;
    Anything a2 = test2.bar("");
    @error String s2 = test2.bar("");
    
    I&Bar<out String> | Bar<String> test3 = nothing;
    String s3 = test3.bar(nothing);
    @error value x3 = test3.bar("");
    
    I&Bar<in String> | Bar<String> test4 = nothing;
    Anything a4 = test4.bar("");
    @error String s4 = test4.bar("");
    
    I&Bar<String> | J&Bar<String> test5 = nothing;
    String s5 = test5.bar("");
    
    I&Bar<String> | J&Bar<Integer> test6 = nothing;
    Anything a6 = test6.bar(nothing);
    
    I&Bar<in String> | J&Bar<out String> test7 = nothing;
    Anything a7 = test7.bar(nothing);
    @error String s7 = test7.bar(nothing);
    @error value x7 = test7.bar("");
    
    I&Bar<in String> | J&Bar<out Integer> test8 = nothing;
    Anything a8 = test8.bar(nothing);
    @error String s8 = test8.bar(nothing);
    @error value x8 = test8.bar("");
}