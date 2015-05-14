shared interface Functor<out Element,out Container> 
        given Container<Element> {
    shared formal 
    Functor<Result,Container> fmap<Result>
            (Result(Element) fun)
            given Result satisfies Object;
    shared formal
    Container<Element> get;
}

Container<String> toString<Element,Container>
        (Functor<Element,Container> functor)
        given Container<Element>
        => functor.fmap((e)=> e?.string else "<null>").get;

shared alias Listish<out Item> 
        => Map<Integer,Item>|List<Item>;

shared class ListishFunctor<out Item>(map) 
        satisfies Functor<Item, Listish> {
    Listish<Item> map;
    shared actual 
    ListishFunctor<Result> fmap<Result>
            (Result(Item) fun) 
            given Result satisfies Object
            => ListishFunctor(if (is List<Item> map) 
        then map.map(fun).sequence()
    else map.mapItems((k,i)=>fun(i)));
    get => map;
}

shared alias Id<Val> => Val;


shared class IdFunctor<out Val>(Val val) 
        satisfies Functor<Val, Id> {
    shared actual 
    IdFunctor<Result> fmap<Result>
            (Result(Val) fun) 
            given Result satisfies Object
            => IdFunctor(fun(val));
    get => val;
}

shared alias Maybe<Val> => Val?;

shared class MaybeFunctor<out Val>(Val? val) 
        satisfies Functor<Val, Maybe> {
    shared actual 
    MaybeFunctor<Result> fmap<Result>
            (Result(Val) fun) 
            given Result satisfies Object
            => MaybeFunctor(if (exists val) 
        then fun(val) 
    else null);
    get => val;
}

void tryit(Map<Integer,Integer> map, Float float, Float? x) {
    String res = toString(IdFunctor(float));
    String? maybe = toString(MaybeFunctor(x));
    
    Listish<String> res1 
            = toString(ListishFunctor(map));
    Map<Integer,String>|List<String> res2 
            = toString(ListishFunctor(map));
}