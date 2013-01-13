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
    @type:"Iterable<Entry<Integer,String>,Null>" entries(*["hello", "world"]);
    for (Integer i->String s in entries("hello", "world", "!")) {}
    
    //print(append({"one", "two" , "three"}, "four").size==4);
    print(["one", "two" , "three"].withTrailing("four").size==4);
    
    if ("hello" nonempty) {}
    Character[] chars = "hello";
    
    [Integer*] ints2 = {};
    {Integer*} ints1 = {};
    
    Integer m1 = min { 1, 2 };
    Null n1 = min {};
    @type:"Null|Integer" min {*ints1};
    @type:"Null|Integer" min {*ints2};
    
    Integer m2 = min([1, 2]);
    Null n2 = min([]);
    Integer m3 = min({1, 2});
    Null n3 = min({});
    @type:"Null|Integer" min(ints1);
    @type:"Null|Integer" min(ints2);

}