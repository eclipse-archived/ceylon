class LanguageModule() {
    
    shared N plus<X,Y,N>(X x, Y y)
            given N of X|Y satisfies Numeric<N>
            given X satisfies Castable<N> & Numeric<X>
            given Y satisfies Castable<N> & Numeric<Y> {
        return x.as<N>().plus(y.as<N>());
    }

    @type["Natural"] plus<Natural, Natural, Natural>(1, 2);
    @type["Integer"] plus<Natural, Integer, Integer>(1, -2);
    @type["Float"] plus<Natural, Float, Float>(1, 2.0);
    @error plus<Natural, Integer, Natural>(1, -2);
    
    //Put these back in once we have enumerated type bounds
    //@error plus<Natural, Integer, Float>(1, -2);
    //@error plus<Natural, Natural, Integer>(1, 2);

    entries<String>("hello", "world");
    entries<String>({"hello", "world"});
    for (Natural i->String s in entries<String>("hello", "world", "!")) {}

}