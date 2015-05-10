shared interface Functor<Element,@Container> 
        given Container<Element> {
    shared formal 
    Container<Result> fmap<Result>
            (Result(Element) fun);
}

shared class ListFunctor<Element>(List<Element> list)
        satisfies Functor<Element,@List> {
    shared actual 
    List<Result> fmap<Result>
            (Result(Element) fun)
            => list.collect(fun);
}

Container<String> toString<Element,@Container>
        (Functor<Element,@Container> functor)
        given Container<Element>
        => functor.fmap((e)=> e?.string else "<null>");

void test() {
    List<String> strings = 
            toString(ListFunctor([1, 2, 3]));
    
    @type:"List<String>"
    value istrings = 
            toString(ListFunctor([1, 2, 3]));
    
    @type:"List<String>"
    value istrings0 = 
            toString<Integer,@List>(ListFunctor([1, 2, 3]));
    
    @error
    value istrings1 = 
            toString<Integer,@Set>(ListFunctor([1, 2, 3]));
    @error
    value istrings2 = 
            toString<Integer,@List>(ListFunctor([1.0, 2.0, 3.0]));
}
