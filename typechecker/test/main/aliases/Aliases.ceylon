class Aliases() {
    
    class C(String s) = Class<String>;
    @type["Aliases.C"] C("hello");
    @type["Aliases.C"] C{s="hello";};
    Class<String> x = C("hello");
    Class<String> y = C{s="hello";};
    C("hello").hello("gavin");
    C{s="hello";}.hello{name="gavin";};
    Class<String>{message="hello";}.hello{name="gavin";};
    C c = C("hello");
    c.hello("gavin");
    Class<String> csp = c;
    Class<String> cs = Class<String>("gday");
    @type["Aliases.C"] C cp = cs;
    @type["Aliases.C"] value l = C("hi");
    
    class D() extends C("greetings") {}
    
    C cd = D();
    Class<String> csd = D();

    interface I<X> = Interface<X>;
    class B<X>(X n) satisfies I<X> {
        shared actual X name = n;
    }
    B<String> b = B("gavin");
    String n = b.name;
    @type["Aliases.I<String>"] I<String> i = b;
    Interface<String> ins = i;
    function m() {
        return i;
    }
    @type["Aliases.I<String>"] m();
    Interface<String> z = m();
    
    interface IS = Interface<String>;
    @type["Aliases.IS"] IS isa = i;
    Interface<String> insa = isa;
    
    @error class BadC1() = Class<String>;
    @error class BadC2(Integer n) = Class<String>;
    @error class BadC3(String s1, String s2) = Class<String>;
    
    @error class X() = String|Integer;
    @error interface Y = Container&Identifiable;
}

interface Li0<U,V> = List<U>;
@error interface Li1<in E> = List<E>;
interface Li2<E> = List<E>;
interface Li3<out E> = List<E>;

interface LL1<out E> = List<List<E>>;
@error interface LL2<out E> = List<SequenceBuilder<E>>;
@error interface LL3<out E> = SequenceBuilder<List<E>>;

class Si1<T>(T t) given T satisfies Object = Singleton<T>; 
@error class Si2<in T>(T t) given T satisfies Object = Singleton<T>; 
class Si3<out T>(T t) given T satisfies Object = Singleton<T>;

class E1<out T>(T x, T y) given T satisfies Object = Entry<T,T>;
@error class E2<out T>(T x, T y) = Entry<T,T>;
@error class E3<in T>(T x, T y) given T satisfies Object = Entry<T,T>;
