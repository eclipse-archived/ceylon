class LanguageModule() {
    
    shared N plus<X,Y,N>(X x, Y y)
            given N of X|Y satisfies Numeric<N>
            given X satisfies Castable<N> & Numeric<X>
            given Y satisfies Castable<N> & Numeric<Y> {
        return x.castTo<N>().plus(y.castTo<N>());
    }

    @type:"Integer" plus<Integer, Integer, Integer>(1, 2);
    @type:"Integer" plus<Integer, Integer, Integer>(1, -2);
    @type:"Float" plus<Integer, Float, Float>(1, 2.0);
    @error plus<Integer, Float, Integer>(1.0, -2);
    
    //Put these back in once we have enumerated type bounds
    //@error plus<Integer, Integer, Float>(1, -2);
    //@error plus<Integer, Integer, Integer>(1, 2);

    @type:"Iterable<Entry<Integer,String>,Null>" entries("hello", "world");
    @type:"Iterable<Entry<Integer,String>,Null>" entries(["hello", "world"]*);
    for (Integer i->String s in entries("hello", "world", "!")) {}
    
    //print(append({"one", "two" , "three"}, "four").size==4);
    print(["one", "two" , "three"].withTrailing("four").size==4);
    
    if ("hello" nonempty) {}
    Character[] chars = "hello";
    
    //TODO: make this work!!
    @error Integer m1 = min { @error 1, 2 };
    Integer m2 = min([1, 2]);
    Integer? m3 = min({1, 2});

}