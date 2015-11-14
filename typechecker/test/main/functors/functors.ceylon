shared interface Functor<out Element,Container> 
        given Container<Element> {
    shared formal 
    Container<Result> fmap<Result>
            (Result(Element) fun);
}

shared class ListFunctor<Element>(List<Element> list)
        satisfies Functor<Element,List> {
    shared actual 
    List<Result> fmap<Result>
            (Result(Element) fun)
            => list.collect(fun);
}

class IterableFunctor({Integer*} ints) 
        satisfies Functor<Integer, Iterable> {
    shared actual 
    {Result*} fmap<Result>
            (Result(Integer) fun)
            => ints.map(fun);
}

Container<String> toString<Element,Container>
        (Functor<Element,Container> functor)
        given Container<Element>
        => functor.fmap((e)=> e?.string else "<null>");

void test() {
    List<String> stringList = 
            toString(ListFunctor([1, 2, 3]));
    {String*} stringStream = 
            toString(IterableFunctor {1, 2, 3 });
    
    @type:"List<String>"
    value strings = 
            toString(ListFunctor([1, 2, 3]));
    
    @type:"List<String>"
    value strings0 = 
            toString<Integer,List>(ListFunctor([1, 2, 3]));
    
    @error
    value strings1 = 
            toString<Integer,Set>(ListFunctor([1, 2, 3]));
    @error
    value strings2 = 
            toString<Integer,List>(ListFunctor([1.0, 2.0, 3.0]));
}
