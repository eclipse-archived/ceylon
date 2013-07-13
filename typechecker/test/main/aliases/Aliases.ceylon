class Aliases() {
    
    class C(String s) => Class<String>(s);
    @error class C1(String s) => Class(s);
    @error class C2(String s) => Class<String>;
    @type:"Aliases.C" C("hello");
    @type:"Aliases.C" C{s="hello";};
    Class<String> x = C("hello");
    Class<String> y = C{s="hello";};
    C("hello").hello("gavin");
    C{s="hello";}.hello{name="gavin";};
    Class<String>{message="hello";}.hello{name="gavin";};
    C c = C("hello");
    c.hello("gavin");
    Class<String> csp = c;
    Class<String> cs = Class<String>("gday");
    @type:"Aliases.C" C cp = cs;
    @type:"Aliases.C" value l = C("hi");
    
    class Cok1(String s) => Class<String>("");
    class Cok2() => Class<String>("");
    class Cok3(Integer i) => Class<String>(i.string);
    class Cok4(String x, String y) => Class<String>(x+y);
    @error class Cbroken1(String s) => Class<String>;
    @error class Cbroken2(Integer i) => Class<String>(i);
    @error class Cbroken4(String s) => Class<String>(0);
    
    class Def(String s="") => Class<String>(s);
    Def def0 = Def();
    Def def1 = Def("hello");
    
    class D() extends C("greetings") {}
    
    C cd = D();
    Class<String> csd = D();

    interface I<X> => Interface<X>;
    class B<X>(X n) satisfies I<X> {
        shared actual X name = n;
    }
    B<String> b = B("gavin");
    String n = b.name;
    @type:"Aliases.I<String>" I<String> i = b;
    Interface<String> ins = i;
    function m() => i;
    @type:"Aliases.I<String>" m();
    Interface<String> z = m();
    
    interface IS => Interface<String>;
    @type:"Aliases.IS" IS isa = i;
    Interface<String> insa = isa;
    
    @error class BadC1() => Class<String>();
    @error class BadC2(Integer n) => Class<String>(n);
    @error class BadC3(String s1, String s2) => Class<String>(s1,s2);
    
    //@error class X() => String|Integer;
    @error interface Y => Container&Identifiable;
    
    interface Seq<T> => T[];
    interface It<T> => {T*};
    interface Fun<T> => T(Object);
}

interface Li0<U,V> => List<U>;
@error interface Li1<in E> => List<E>;
interface Li2<E> => List<E>;
interface Li3<out E> => List<E>;

interface LL1<out E> => List<List<E>>;
@error interface LL2<out E> => List<SequenceBuilder<E>>;
@error interface LL3<out E> => SequenceBuilder<List<E>>;

class Si1<T>(T t) given T satisfies Object => Singleton<T>(t); 
@error class Si2<in T>(T t) given T satisfies Object => Singleton<T>(t); 
class Si3<out T>(T t) given T satisfies Object => Singleton<T>(t);

class E1<out T>(T x, T y) given T satisfies Object => Entry<T,T>(x,y);
@error class E2<out T>(T x, T y) => Entry<T,T>(x,y);
@error class E3<in T>(T x, T y) given T satisfies Object => Entry<T,T>(x,y);

class MemberClassAliasTricks_Foo(Integer a = 1, Integer b = 2){
    
    String x=>y;
    String y=>x;
    
    class MemberClassAliasToToplevel(Integer a, Integer b) 
            => MemberClassAliasTricks_Foo(a,b);
    class MemberClassAliasToToplevel2(Integer a, Integer b) 
            => MemberClassAliasToToplevel(a,b);

    void test(){
        MemberClassAliasToToplevel m1 = MemberClassAliasToToplevel(1,2);
        MemberClassAliasToToplevel2 m2 = MemberClassAliasToToplevel2(1,2);
    }
}

@error alias Rec<T> => Tuple<T,T,Rec<T>>;
@error alias RX => String|List<RY>;
alias RY => Object&Iterable<RX>;

alias Id<T> => T;
alias Or<X,Y> => X|Y;

T unwrap<T>(Id<T> idt) => idt;

void testOpAliases() {
    Id<String> idstr = "hello";
    Id<Object> idobj = idstr;
    String str = idstr;
    @type:"String" value string = unwrap(idstr);
    Integer idstrlen = idstr.size;
    
    Or<Integer,Float> ornum = 1;
    Integer|Float num = ornum;
    Float ornumasfloat = ornum.float;
    Or<Float,Integer> ornum2 = ornum;
}

abstract class S(String s) => String(s);
@error abstract class StringSubclass(String s) extends S(s) {}
@error abstract class IntSubclass(Integer i) extends I(i) {}
abstract class I(Integer i) => Integer(i);

void inheritsAlias() {
    interface I<T> {}
    interface J satisfies I<J> {}
    interface K => J;
    interface L satisfies I<String> {}
    interface M => L;
    class X() satisfies I<K> {}
    class Y() satisfies I<M> {}
    class Z() satisfies K {}
    class W() satisfies M {}
    alias N=>M;
    @error class V() satisfies I<I<N>> {}
    I<J> x = X();
    I<L> y = Y();
    J z = Z();
    M w = W();
    @error I<I<L>> v = V();
}

abstract class AbstractClass() {}
class ConcreteClass() extends AbstractClass() {}

abstract class AbstractOuterClass() {
    shared formal class InnerAlias() => AbstractClass();
}

abstract class ConcreteOuterClass() extends AbstractOuterClass() {
    shared actual class InnerAlias() => ConcreteClass();
}

class OkOuterClass() {
    shared abstract class InnerAlias() => AbstractClass();
    shared abstract class OtherInnerAlias() => ConcreteClass();
}

abstract class BrokenOuterClass() {
    @error shared class InnerAlias() => AbstractClass();
}

abstract class GoodOuterClass() {
    shared formal class FormalClass() {}
    shared class YetAnotherInnerAlias() => FormalClass();
}

abstract class ToplevelAlias() => AbstractClass();
@error class BrokenToplevelAlias1() => AbstractClass();
@error formal class BrokenToplevelAlias2() => AbstractClass();


class Qux1<T>(T t) {
    shared alias Q=>[T+];
    shared Q q() => [t];
}

class Qux2<T>(T t) {
    shared interface Q=>[T+];
    shared Q q() => [t];
}

void testQux() {
    @type:"Qux1<String>.Q" value q1 = Qux1("").q();
    @type:"Qux2<String>.Q" value q2 = Qux2("").q();
}