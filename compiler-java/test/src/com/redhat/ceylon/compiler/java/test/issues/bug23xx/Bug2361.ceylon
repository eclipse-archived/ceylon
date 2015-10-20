@nomodel
shared interface Bug2361A<Other> 
        given Other satisfies Bug2361A<Other> {
    shared formal void bar(Other other);
    shared formal void gee<Indirect>(Indirect other)
        given Indirect satisfies Other;
}
@nomodel
interface Bug2361 {}
@nomodel
shared interface Bug2361B<Other> 
        given Other satisfies Bug2361&Bug2361B<Other> {
    shared formal void bar(Other other);
}
@nomodel
shared void bug2361(){
    variable Anything r;
    
    r = Bug2361A<Nothing>.bar;
    r = Bug2361A<Nothing>.gee<Nothing>;
    Bug2361A<Nothing> foo = nothing;
    r = foo.bar;
    r = foo.gee<Nothing>;
    
    r = Bug2361A<Nothing[2]>.bar;
    Bug2361A<Nothing[2]> bar = nothing;
    r = bar.bar;
    
    //r = Bug2361A<String|Integer>.bar;
    Bug2361A<String|Integer> baz = nothing;
    r = baz.bar;
    
    // Check when 2nd constrain
    r = Bug2361B<Nothing>.bar;
    Bug2361B<Nothing> foo2 = nothing;
    r = foo2.bar;
    
    r = Bug2361B<Nothing[2]>.bar;
    Bug2361B<Nothing[2]> bar2 = nothing;
    r = bar2.bar;
    
    //r = Bug2361B<String|Integer>.bar;
    Bug2361B<String|Integer> baz2 = nothing;
    r = baz2.bar; // error
    
    r = plus<Integer>;
}
@nomodel
shared interface Completion2361<out Element, out T>
        given T satisfies Element[] {
    
    shared formal Completion2361<Result,[Result]> map<Result>(
        "A function that is called when fulfilled."
        Result(*T) onFulfilled);
}
@nomodel
class Conjunction2361<out Element, out First, Rest>(first, rest)
        satisfies Completion2361<Element,Tuple<First|Element,First,Rest>>
        given First satisfies Element
        given Rest satisfies Sequential<Element> {
    
    "The first promise."
    Completion2361<Rest,[Rest]> rest;
    
    "The second promise."
    Completion2361<First,[First]> first;
    
    void onRestFulfilled(Rest val) {
    }
    rest.map(onRestFulfilled);
    
    void onFirstFulfilled(First val) {
    }
    first.map(onFirstFulfilled);
    
    shared actual Completion2361<Result,[Result]> map<Result>(
        "A function that is called when fulfilled."
        Result(*Tuple<First|Element,First,Rest>) onFulfilled) => nothing;
}