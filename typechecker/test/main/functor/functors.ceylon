"A container that supports applying an [[fmap]] operation
 to the elements of type [[Element]] belonging to some data 
 structure represented by the type constructor [[Container]]."
shared interface Functor<out Element,out Container> 
        given Container<Element> {
    "Get the boxed values."
    shared formal
    Container<Element> get;
    "Apply a function to the boxed values."
    shared formal 
    Functor<Result,Container> fmap<Result>
            (Result(Element) fun)
            given Result satisfies Object;
}


//A utility function defined in terms of functors:

"Convert the boxed elements of the given functor to strings, 
 and return them."
Container<String> toString<Element,Container>
        (Functor<Element,Container> functor)
        given Container<Element>
        => functor.fmap((e)=> e?.string else "<null>")
                  .get;

//A functor for things sorta like lists

shared alias Listish<out Item> 
        => Map<Integer,Item>|List<Item>;

shared class ListishFunctor<out Item>(items)
        satisfies Functor<Item, Listish> {
    Listish<Item> items;
    get => items;
    shared actual 
    ListishFunctor<Result> fmap<Result>
            (Result(Item) fun) 
            given Result satisfies Object
            => ListishFunctor(
                    if (is List<Item> items) 
                    then items.map(fun).sequence()
                    else items.mapItems((k,i)=>fun(i)));
}

//A functor for regular values

shared alias Id<Val> => Val;

shared class IdFunctor<out Val>(Val val) 
        satisfies Functor<Val, Id> {
    get => val;
    shared actual 
    IdFunctor<Result> fmap<Result>
            (Result(Val) fun) 
            given Result satisfies Object
            => IdFunctor(fun(val));
}

//A functor for optional values

shared alias Maybe<Val> => Val?;

shared class MaybeFunctor<out Val>(Val? val) 
        satisfies Functor<Val, Maybe> {
    get => val;
    shared actual 
    MaybeFunctor<Result> fmap<Result>
            (Result(Val) fun) 
            given Result satisfies Object
            => MaybeFunctor(
                    if (exists val) 
                    then fun(val) 
                    else null);
}

//prove that it all works

void tryit(Map<Integer,Integer> map, Float float, Float? x,
    Functor<Object,Maybe>|Functor<Object,List> functor) {
    
    String string = toString(IdFunctor(float));
    
    String? maybe = toString(MaybeFunctor(x));
    
    @type:"Listish<String>"
    value listish = toString(ListishFunctor(map));
    
    Map<Integer,String>|List<String> mapOrList 
            = toString(ListishFunctor(map));
    
    String|List<String>? result 
            = toString<Object,Maybe|List>(functor);
}