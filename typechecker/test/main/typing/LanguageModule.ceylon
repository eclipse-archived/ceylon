class LanguageModule() {
    
    $type:"Iterable<Entry<Integer,String>,Nothing>" value e1 = {"hello", "world"}.indexed;
    $type:"Iterable<Entry<Integer,String>,Nothing>" value e2 = ["hello", "world"].indexed;
    for (Integer i->String s in {"hello", "world", "!"}.indexed) {}
    
    //print(append({"one", "two" , "three"}, "four").size==4);
    print(["one", "two" , "three"].withTrailing("four").size==4);
    
    if ("hello".sequence() nonempty) {}
    List<Character> chars = "hello";
    
    [Integer*] ints2 = [];
    {Integer*} ints1 = {};
    
    Integer m1 = min { 1, 2 };
    Null n1 = min {};
    $type:"Null|Integer" min {*ints1};
    $type:"Null|Integer" min {*ints2};
    
    Integer m2 = min([1, 2]);
    Null n2 = min([]);
    Integer m3 = min({1, 2});
    Null n3 = min({});
    $type:"Null|Integer" min(ints1);
    $type:"Null|Integer" min(ints2);
    
    $type:"[]" emptyOrSingleton(null);
    $type:"[]|[Integer]" emptyOrSingleton(1);
    $type:"[]|[Integer]" emptyOrSingleton(1==1 then 1);
    
    Nothing nt1 = [nothing];
    Nothing nt2 = [nothing, nothing];
    Nothing nt3 = [ for (i in 0..10) nothing ];
    
    Integer potencia(Integer base, Integer expo) => base^expo;
    Integer veces(Integer x, Integer y) => x*y;
    $type:"Integer(Integer)" value p2 = curry(potencia)(2);
    $type:"Integer(Integer)" value triple = compose(curry(veces)(3), p2);
    function f1(Integer i) => i;
    $type:"Integer()(Integer)" value g1 = curry(f1);
    $type:"Integer(Integer)" Integer(Integer) h1 = uncurry(g1);
    function f2(Integer i, Float f) => i*f;
    $type:"Float(Float)(Integer)" value g2 = curry(f2);
    $type:"Float(Integer)(Float)" value h2 = shuffle(g2);
    $type:"Float(Float,Integer)" Float(Float,Integer) i1 = uncurry(h2);
    
    [String+] strings = ["Hello", "World"];
    [Integer+] integers = [1];
    $type:"{[Integer, String]*}"
    value stuff = zipPairs(integers, strings.rest);
    $type:"[String|Integer+]"
    value objects = concatenate([strings.first], *stuff);
    
}

$error class Dumbthing() extends Anything() {}
$error class Numbthing() extends Null() {}
