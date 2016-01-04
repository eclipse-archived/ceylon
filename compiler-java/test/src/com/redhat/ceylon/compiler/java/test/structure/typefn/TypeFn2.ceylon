

@noanno
void genericMethodReference() {
    // XXX AFAICS these are not supported by the tc
    //value xxx = Iterable.map;
    //<Mapped> 
    //        => {Mapped*}({String*},Mapped(String)) mapRef = Iterable<String>.map;//generic class reference
}

@noanno
void useNonFunctional() {
    <Any> given Any satisfies Object => [Any, String] nonFunctional = nothing;
    // check we can apply a type constructor whose result is not a Callable type
    // note there ar eno instances of such types
    [String, String] xx = nonFunctional<String>;
    
    // TODO Tidy/Encapsulate stuff in the compiler
    // top levels, attributes of type constructor type
    // class, attribute and function parameters of type constructor type
    // type constructors from classes, methods
    // using a type constructor as a type argument!
    // a type parameter with a type constructor upper bound
    // model loader
    // support type constructor abbreviation in the type parser
    
    // how can one type constructor be considered a subtype of another type constructor
    // when there's no base type in the language module to represent type constructors?
    // 
}
