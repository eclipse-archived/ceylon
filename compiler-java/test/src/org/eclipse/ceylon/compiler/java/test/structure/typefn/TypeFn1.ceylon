@noanno
Any pipe<Any>(Any anything) => anything;
@noanno
void genericFunctionReference() {
    <Any> => Any(Any) pipeRef = pipe;//generic reference
    String(String) stringPipeRef = pipeRef<String>;//apply
    assert("" == stringPipeRef(""));//call
    assert("hello" == pipeRef("hello"));//apply and call in one
}

@noanno
void genericLocalFunctionReference() {
    @noanno
    Number add<Number>(Number x, Number y)
            given Number satisfies Summable<Number>
            => x+y;
    <Number> given Number satisfies Summable<Number> 
            => Number(Number,Number) addRef = add;//generic reference
    Float(Float,Float) floatAddRef = addRef<Float>;//apply
    assert(2.0 == floatAddRef(1.0, 1.0));//call
    String(String,String) stringAddRef = addRef<String>;//apply
    assert("hello world" == stringAddRef("hello", " world"));//call
    assert("hello, world" == addRef("hello,", " world"));//apply and call
}

@noanno
void genericClassReference() {
    <First, Second> given First satisfies Object
            => Entry<First,Second>(First,Second) pairRef = Entry;//generic class reference
    <First, Second> 
            given First satisfies Object
            given Second  satisfies Object
            => Entry<First,Second>(First,Second) pairRef2 = pairRef;//generic class reference
    Entry<String,String>(String,String) stringPair = pairRef<String,String>;//apply
    assert("hello->world" == stringPair("hello", "world").string);//call
    assert("hello->world" == pairRef("hello", "world").string);//apply and call
}

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
