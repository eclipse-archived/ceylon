class Lambdas() {
    
    void repeat(Integer times, void perform(Integer it)) {
        for (Integer it in 0..times-1) {
            perform(it);
        }
    }
    
    repeat(10, (Integer n) => print("Hello"));
    repeat(10, (Integer n) => print("Hello"));
    repeat(10, (Integer n) => print("Hello"));

    @error repeat(10, (Float n) => print("Hello"));
    
    repeat(10, void (Integer i) {});
    repeat(10, void (Integer i) { print(i.string); });
    repeat(10, (Integer i) { return print(i.string); });
    repeat(10, void (Integer i) { @error return print(i.string); });
    
    Integer amount = 0;
    repeat(2, void (Integer i) { Float amount = i.float; Float result = amount/2.0; });
    Integer result = amount+1;
    
    function acceptLazyFloat(Float lazyFloat()) => lazyFloat();
    
    acceptLazyFloat(() { return 0.0; });
    acceptLazyFloat(() { if (1==1) { return 0.0; } else { return 1.0; } });
    @error acceptLazyFloat(void () {});
    @error acceptLazyFloat(() { return 0; });
    @error acceptLazyFloat((String s) { return 0.0; });
    @error acceptLazyFloat(() {});
    acceptLazyFloat(() { @error return; });
    @error acceptLazyFloat(() { if (1==1) { return 0.0; } });

    function foo(Float bar(Float x)(Integer y)) => bar(1.0)(0);
    
    @type:"Float" foo((Float x)(Integer y) => x+y);
    @type:"Float" foo((Float x)(Integer y) => x+y);
    
    @error foo((Float x)(Integer y) => print((x+y).string));
    @error foo((Float x)(Float y) => x+y);
    
    function baz(Float bar(Float qux(Float x))) {
        return bar((Float x) => x);
    }
    
    @type:"Float" baz((Float qux(Float x)) => qux(1.0));
    @type:"Float" baz((Float qux(Float x)) => qux(1.0));

    @error baz((Float qux(Float x)) => qux(1.0).integer);
    @error baz((Float qux(Integer x)) => qux(1));
    @error baz((Integer qux(Float x)) => qux(1.0).float);
    
    function apply<X>(X x)(X f(X x)) => f(x);
    
    function sqrt(Float x) => x^0.5;
    
    @type:"Float" apply(2.0)(sqrt);
    @type:"Float" apply<Float>(2.0)((Float x) => x^3);
    
    @type:"Float" function applyToTwo(Float f(Float x)) => apply(2.0)(f);
    applyToTwo(sqrt);
    applyToTwo((Float x) => x^3);
    
    @type:"Callable<Float,Tuple<Callable<Float,Tuple<Float,Float,Empty>>,Callable<Float,Tuple<Float,Float,Empty>>,Empty>>" value applyToOne = apply(1.0);
    @type:"Float" applyToOne(sqrt);
    @type:"Float" applyToOne((Float x) => x^3);
    
    void exec(Callable<Anything,[]> run) {
        run();
    }
    
    Callable<String,[]> lazy(String s) {
        function result() {
            return s;
        }
        return result;
    }
    @type:"String" lazy("hello")();
    
    function name(String first)(String middle)(String last) {
        return "``first`` ``middle`` ``last``";
    }
    String n1(String middle)(String last) => name("Gavin")(middle)(last);
    String n2(String last) => name("Gavin")("A")(last);
    String n3 = name("Gavin")("A")("King");
    String(String) n4 = name("Gavin")("A");
    String(String)(String) n5 = name("Gavin");
    
    @type:"String" function nm2(String middle)(String last) => name("Gavin")(middle)(last);
    @type:"String" function nm1(String last) => name("Gavin")("A")(last);
    
    @type:"Callable<Callable<String,Tuple<String,String,Empty>>,Tuple<String,String,Empty>>" value nmv2 = name("Gavin");
    @type:"Callable<String,Tuple<String,String,Empty>>" value nmv1 = name("Gavin")("A");
    
    void noop(String x)(Integer y)(Float z) {}
    noop("")(1)(1.0);
    @error noop("")(1.0)(1.0);
    @error noop("")(1)(1);
    
    @type:"Callable<String,Tuple<String,String,Empty>>" 
    function higher() => (String s) => s.uppercased;

    @type:"Callable<Callable<Float,Tuple<Float,Float,Empty>>,Tuple<Float,Float,Empty>>" 
    function evenHigher(Float z) => (Float x)(Float y) => x+y+z;
    
    @type:"String" function l1(String s) => ((String s) => s.uppercased)(s);
    @type:"Callable<String,Tuple<String,String,Empty>>" value l2 = (String s) => s.lowercased;
    @error String l3(String s) => (String s) => s.size;
    @error Integer l4(String s, Integer i) => (String s) => s.size;
    
    String()(String) lambdalambda0 = (String s) () => s;
    String()(String) lambdalambda1 = function (String s) => function () => s;  
    String()(String) lambdalambda2 = function (String s)() => s;
    
    String() lambdafun0(String s) => () => s;
    String() lambdafun1(String s) => function () => s;  
    String() lambdafun2(String s) { return () => s; }
    String() lambdafun3(String s) { return function () => s; }
    
    //no longer supported
    /*void hi() = () print("Hi!");
    void hello(String name) = (String name) print("Hello, " name "!");
    
    hi();
    hello("Gavin");
    
    Float square(Float x) = (Float y) y**2;
    Float cube(Float x) = (Float y) return y**3;*/
    
}

class SuperWithFun(String fun()) {}
@error class BadSubWithFun(variable String s) extends SuperWithFun(()=>s) {}
class GoodSubWithFun(String s) extends SuperWithFun(()=>s) {}
