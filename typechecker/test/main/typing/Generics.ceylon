class Generics() {
    
    class Holder<X>(X x) {
        shared X held = x;
        shared X add(X x, X y) {
            @error return x + y;
        }
        shared Y noop<Y>(Y y) {
            return y;
        }
    }
    
    Holder<W> create<W>(W w) {
        return Holder<W>(w);
    }
    
    @type:"Generics.Holder<String>" Holder<String> hs;
    
    @type:"Generics.Holder<String>" Holder<String>("hello");
    @type:"String" value shh = Holder<String>("hello").held;
    
    @type:"Generics.Holder<String>" Holder("hello");
    
    Holder<String> h1 = Holder<String>("hello");
    String hello = h1.held;
    String hw = h1.add("hello", "world");
    @error Integer en = h1.add(1,2);
    
    Holder<Integer> h2 = Holder<Integer>{ x = 0; };
    Integer zero = h2.held;
    Integer n = h2.add(1,2);
    @error String ehw = h2.add("hello", "world");
    
    @type:"Float" h2.noop<Float>(1.0);
    String s1 = h2.noop<String>("Hi!");
    String s2 = h2.noop("Hi!");
    
    @type:"Generics.Holder<Integer>" create<Integer>(3);
    Integer nn = create<Integer>(5).held;
    
    X op<X>(X arg) {
        return arg;
    }
    String hi = op<String>("Hi");
    @error String hi2 = op<X>("Hi");
    
    class C<X>() {
        shared X cop(X arg) {
            if (false) {
                X nothing { return arg; }
                return nothing;
            }
            else {
                return arg;
            }
        }
    }
    String hiback = C<String>().cop("Hi back!");
    
    class Producer<out X>() {}
    class Consumer<in X>() {}
    
    Producer<Object> p = Producer<String>();
    Consumer<String> c = Consumer<Object>();
    Producer<Consumer<Producer<String>>> o = Producer<Consumer<Producer<Object>>>();
    
    class Variances<out X, in Y, Z>(X x, Y y, Z z) 
            given X satisfies Object
            given Y satisfies Object {
        shared X goodAtt = x;
        @error shared Y badAtt = y;
        shared X[] goodAtt2 { return [x]; }
        @error shared Y[] badAtt2 { return [y]; }
        shared X goodMethod() { return goodAtt; }
        @error shared Y badMethod() { return badAtt; }
        shared X[] goodMethod2() { return [goodAtt]; }
        @error shared Y[] badMethod2() { return [badAtt]; }
        shared Producer<X> goodMethod3() { return Producer<X>(); }
        @error shared Consumer<X> badMethod3() { return Consumer<X>(); }
        shared Consumer<Y> goodMethod4() { return Consumer<Y>(); }
        @error shared Producer<Y> badMethod4() { return Producer<Y>(); }
        shared void goodAnythingMethod(Y y) {}
        shared void badAnythingMethod(@error X x) {}
        shared void goodAnythingMethod2(Y[] y) {}
        shared void badAnythingMethod2(@error X[] x) {}
        shared void goodAnythingMethod3(Consumer<X> c) {}
        shared void badAnythingMethod3(@error Consumer<Y> p) {}
        shared void goodAnythingMethod4(Producer<Y> c) {}
        shared void badAnythingMethod4(@error Producer<X> p) {}
        shared class GoodClass(Y y) {}
        shared class BadClass(@error X x) {}
        shared class GoodClass2(Y[] y) {}
        shared class BadClass2(@error X[] x) {}
        shared class GoodClassInheritance() 
                extends Object() 
                satisfies Sequence<X> {
            //fake implementations
            shared actual Integer size = 1;
            shared actual Integer lastIndex = 0;
            shared actual X[] rest = {};
            shared actual GoodClassInheritance clone = GoodClassInheritance();
            shared actual X? get(Integer key) { return null; }
            shared actual Boolean contains(Object x) { return false; }
            shared actual X[] segment(Integer from, Integer length) { return this; }
            shared actual X first { throw; }
            shared actual X last { throw; }
            shared actual Sequence<X> reversed { return this; }
            shared actual X[] span(Integer from, Integer to) { return this; }
            shared actual X[] spanTo(Integer to) { return this; }
            shared actual X[] spanFrom(Integer from) { return this; }
            shared actual Iterator<X> iterator { throw; }
        }
        @error shared class BadClassInheritance() satisfies Sequence<Y> {}
        shared class GoodClassInheritance2() extends Producer<X>() {}
        @error shared class BadClassInheritance2() extends Producer<Y>() {}
        shared class GoodClassInheritance3() extends Consumer<Y>() {}
        @error shared class BadClassInheritance3() extends Consumer<X>() {}
        shared class BadParameterizedClassGiven1<T>(T t) @error given T satisfies X {}
        shared class GoodParameterizedClassGiven2<T>(T t) given T satisfies Y {}
        shared class GoodParameterizedClassGiven3<in T>(T t) given T satisfies Y {}
        shared class GoodParameterizedClassGiven4<out T>(T t) given T satisfies Y {}
        shared class OKParameterizedClassGiven1<out T>(T t) given T satisfies Y {
        	shared T get() { return t; }
        }
        shared class OKParameterizedClassGiven2<in T>(T t) given T satisfies Y {
        	shared void put(T t) {}
        }
        shared class GoodParameterizedClass<out T>(T t) {}
        shared class GoodParameterizedClass2<out T>(Producer<T> t) {}
        shared class GoodParameterizedClass3<out T>(Consumer<T> t) {}
        shared class GoodParameterizedClass4<out T>(void get(T t)) {}
        shared class GoodParameterizedClass5<out T>(void get(T[] t)) given T satisfies Object {}
        shared class GoodParameterizedClass6<out T>(void get(Producer<T> t)) {}
        shared class GoodParameterizedClass7<in T>(T t) {}
        shared class GoodParameterizedClass8<in T>(void get(Consumer<T> t)) {}
        shared class GoodParameterizedClass9<in T>(Producer<T> t) {}
        shared class GoodParameterizedClass10<in T>(Consumer<T> t) {}
        shared class BadParameterizedClass<out T>(void get(Consumer<T> t)) {}
        shared class BadParameterizedClass2<in T>(void get(T t)) {}
        shared class BadParameterizedClass3<in T>(void get(T[] t)) given T satisfies Object {}
        shared class BadParameterizedClass4<in T>(void get(Producer<T> t)) {}
        shared void goodHigherOrderMethod(void get(X x)) {}
        shared void badHigherOrderMethod(void get(@error Y x)) {}
        shared void goodHigherOrderMethod2(void get(X[] x)) {}
        shared void badHigherOrderMethod2(void get(@error Y[] x)) {}
        shared void goodHigherOrderMethod3(void get(Producer<X> x)) {}
        shared void badHigherOrderMethod3(void get(@error Consumer<X> x)) {}
        
        shared Z methodWithNonvariant(Z z) { return z; }
        shared Z attributeWithNonvariant;
        attributeWithNonvariant = nothing;
        shared variable Z variableAtt = z;
        @error shared variable X badVariableAtt = x;
        @error shared variable Y badVariableAtt2 = y;
        shared Z goodGetter { return z; }
        assign goodGetter { }
        @error shared X badGetter { return x; }
        assign badGetter { }
        @error shared Y badGetter2 { return y; }
        assign badGetter2 { }
        
        class NestedClass() {
            shared X x;
            @error shared Y y;
            shared void goodMethod(Y y) {}
            shared void badMethod(@error X x) {}
            shared Z method(Z z) { return z; }
            x = nothing;
        }
        
        shared void goodGenericMethod1<D>(D d) given D satisfies Y {}
        shared void goodGenericMethod3<D>(D d) given D satisfies Consumer<X> {}
        shared void goodGenericMethod4<D>(D d) given D satisfies Producer<Y> {}
        shared void badGenericMethod1<D>(D d) @error given D satisfies X {}
        shared void badGenericMethod5<D>(D d) @error given D satisfies Producer<X> {}
        shared void badGenericMethod6<D>(D d) @error given D satisfies Consumer<Y> {}
    }
    
    class Bar() {
        shared String hello = "Hello";
    }
    class Foo<X>(X x) given X satisfies Bar {
        @type:"String" value xh = x.hello;
    }
    
    class Outer<X>(X x) given X satisfies Object {
        //shared X x = x;
        shared class Inner<Y>(Y y) given Y satisfies Object {
            //shared Y y = y;
            shared Entry<X,Y> getIt() {
                return x->y;
            }
        }
        shared Inner<String> createInner(String string) {
            return Inner<String>(string);
        }
    }
        
    @type:"Generics.Outer<Integer>.Inner<String>" Outer<Integer>(1).Inner<String>("hello");
    @type:"Generics.Outer<Integer>.Inner<String>" Outer<Integer>(1).createInner("hello");
    @type:"Entry<Integer,String>" Outer<Integer>(1).Inner<String>("hello").getIt();
    @type:"Entry<Integer,String>" Outer<Integer>(1).createInner("hello").getIt();

    Outer<Integer>.Inner<String> aa = Outer<Integer>(1).Inner<String>("hello");
    @error Inner<String> bb = Outer<Integer>(1).Inner<String>("hello");
    @error Outer<Integer> dd = Outer<Integer>(1).Inner<String>("hello");
    @error Outer<Integer>.Inner<String> cc = Outer<Float>(1.3).Inner<String>("hello");
    
    @type:"Generics.Outer<Integer>.Inner<String>" value aaa = aa;
    @type:"Entry<Integer,String>" aa.getIt();
    
    class Num() satisfies Comparable<Num> {
        //fake implementation
        shared actual Comparison compare(Num other) {
            throw;
        }
    }
    
    abstract class SortedList<T>(T* elements) 
        extends Object()
        satisfies Sequence<T> 
        given T satisfies Comparable<T> {
        shared actual formal String string;
    }
    
    interface WithCovariant<out X> {}
    class Good1<T>() satisfies WithCovariant<T> {}
    class Good2<out T>() satisfies WithCovariant<Producer<T>> {}
    class Good3<in T>() satisfies WithCovariant<Consumer<T>> {}
    @error class Bad1<in T>() satisfies WithCovariant<T> {}
    @error class Bad2<out T>() satisfies WithCovariant<Consumer<T>> {}
    
    interface WithContravariant<in X> {}
    class Good4<T>() satisfies WithContravariant<T> {}
    class Good5<in T>() satisfies WithContravariant<Producer<T>> {}
    @error class Bad6<out T>() satisfies WithContravariant<Consumer<T>> {}
    @error class Bad3<out T>() satisfies WithContravariant<T> {}
    @error class Bad4<in T>() satisfies WithContravariant<Consumer<T>> {}
    
    interface SequenceSequence<out T, out X> 
            satisfies Sequence<T>
            given T satisfies Sequence<X> & Object
            given X satisfies Object {}
    
    interface BadSequenceSequence<out T> 
            satisfies Sequence<T>
            /*@error*/ given T/*<out X>*/ satisfies Sequence<X> & Object
            @error given X satisfies Object {}
    
    class Upper<X>(X x)
            given X satisfies Object {
        shared Entry<X,Y> method<Y>(X x, Y y) 
                given Y satisfies Object { 
            return x->y; 
        }
        shared class Inner<Y>(X x, Y y) {}
    }
    
    class Lower<W>(W w) extends Upper<W>(w)
            given W satisfies Object {}
     
    @type:"Entry<String,Integer>" Lower<String>("hello").method<Integer>("world",1);
    @type:"Generics.Upper<String>.Inner<Float>" Lower<String>("hello").Inner<Float>("world", 2.3);
    
    interface Some<out X> {}
    class Foo1() satisfies Some<Object> {}
    class Bar1() extends Foo1() satisfies Some<String> {}
    Some<String> bar1 = Bar1();
    class Foo2() satisfies Some<String> {}
    class Bar2() extends Foo2() satisfies Some<Object> {}
    Some<String> bar2 = Bar2();
    interface Foo3 satisfies Some<Object> {}
    interface Bar3 satisfies Some<String> {}
    class Baz3() satisfies Foo3 & Bar3 {}
    Some<String> baz3 = Baz3();
    class Foo4() satisfies Some<Object> {}
    class Baz4() extends Foo4() satisfies Some<String> {}
    Some<String> baz4 = Baz4();
    class Foo5() satisfies Some<String> {}
    class Baz5() extends Foo5() satisfies Some<Object> {}
    Some<String> baz5 = Baz5();
    
    interface Self<out T> of T {
        shared default T get {
            return this of T;
        }
    }
    
    class Super() 
            satisfies Self<Super> {
        /*shared actual default Super get {
            return this;
        }*/
    }
    
    class Sub() 
            extends Super() 
            satisfies Self<Sub> {
        /*shared actual Sub get {
            return this;
        }*/
    }
    
    class SubSub()
            extends Sub() 
            satisfies Self<SubSub> {
        shared actual SubSub get {
            return this;
        }
    }
    
    @error class Wrong() 
            extends Super() 
            satisfies Self<String> {
        /*@error shared actual String get {
            return "hello";
        }*/
    }
    
    Super self1 = Super().get;
    Sub self2 = Sub().get;
    SubSub self3 = SubSub().get;

    void method<X>() {}

    @error Producer<Holder>();
    @error Producer<Holder> hp = Producer<Holder>();
    @error Producer<Holder>? ohp = null;
    @error Generics.Producer<Holder>();
    @error Generics.Producer<Holder> ghp = Generics.Producer<Holder>();
    @error Generics.Producer<Holder>? gohp = null;
    
    @error object wc satisfies WithCovariant {}
    @error object wch satisfies WithCovariant<Holder> {}
    @error object gwc satisfies Generics.WithCovariant {}
    @error object gwch satisfies Generics.WithCovariant<Holder> {}
    
    @error method<Holder>();
    @error this.method<Holder>();
    
    @error Producer<Holder<String,String>>();
    @error Producer<Holder<String,String>> hpss = Producer<Holder<String,String>>();
    @error Producer<Holder<String,String>>? ohpss = null;
    @error Generics.Producer<Holder<String,String>>();
    @error Generics.Producer<Holder<String,String>> ghpss = Generics.Producer<Holder<String,String>>();
    @error Generics.Producer<Holder<String,String>>? gohpss = null;
    
    @error object wchss satisfies WithCovariant<Holder<String,String>> {}
    
    @error method<Holder<String,String>>();
    @error this.method<Holder<String,String>>();
    
    @error Producer<String<String,String>>();
    @error Producer<String<String,String>> spss = Producer<String<String,String>>();
    @error Producer<String<String,String>>? ospss = null;
    @error Generics.Producer<String<String,String>>();
    @error Generics.Producer<String<String,String>> gspss = Generics.Producer<String<String,String>>();
    @error Generics.Producer<String<String,String>>? gospss = null;
    
    @error object wcsss satisfies WithCovariant<String<String,String>> {}
    
    @error method<String<String,String>>();
    @error this.method<String<String,String>>();
    
    @error Producer<Holder<Holder>>();
    @error method<Holder<Holder>>();
    
    @error Holder<String>.X wrong;
    
    T choose<T>(Boolean b, T first, T second) {
    	if (b) {
    		return first;
    	}
    	else {
    		return second;
    	}
    }
    
    P getFirst<P>(Sequence<P> list) {
        return list.first;
    } 
    Number getFirstNumber(Sequence<Number> nums) {
        return getFirst(nums);
    } 
    Object getFirstNonEmpty(Sequence<String> strs, Sequence<Object> obs) {
        return getFirst(choose(true, strs, obs));
    	/*if (nonempty strs) {
            return getFirst(strs);
        }
        else {
            return getFirst(obs);
        }*/
    }
    Object getFirstNonEmpty2(Sequence<String> strs, Sequence<Integer> ints) {
        return getFirst(choose(false, strs, ints));
        /*if (nonempty strs) {
            return getFirst(strs);
        }
        else {
            return getFirst(ints);
        }*/
    }
    
    interface Addable<in T> {
    	shared formal void add(T t);
    }
    class Var() { 
    	Boolean bool = true; 
    	void addTo(Addable<Var> trues, Addable<Var> falses) {
    		choose(bool, trues, falses).add(this);
    		//if (mValue) { trues.add(this); } else { falses.add(this); }
    	}
    }
    
    interface Inter1<in T> {
    	shared void put(T t) { throw; }
    }
    interface Inter2<out T> {
    	shared T get() { throw; }
    }

    interface Super1 satisfies Inter1<Nothing> & Inter2<String> {}
    interface Super2 satisfies Inter1<Integer> & Inter2<Object> {}
    class Impl() satisfies Super1 & Super2 {}
    value impl = Impl();
    String implget1 = impl.get();
    @error Integer implget2 = impl.get();
    impl.put(1);
    @error impl.put("hello");
    Inter1<Integer> inter1 = impl;
    Inter2<String> inter2 = impl;
    Inter1<Nothing> inter1b = impl;
    Inter2<Object> inter2o = impl;

    interface Super3 satisfies Inter1<Object> & Inter2<String> {}
    interface Super4 satisfies Inter1<Integer> & Inter2<Nothing> {}
    class Conc() satisfies Super3 & Super4 {}
    value conc = Conc();
    String concget1 = conc.get();
    Integer concget2 = conc.get();
    conc.put(1);
    conc.put("hello");
    Inter1<Integer> inter3 = conc;
    Inter2<String> inter4 = conc;
    Inter1<Nothing> inter3b = conc;
    Inter2<Object> inter4o = conc;
    
    abstract class Co1<out T>(T t) {
        shared class Foo() {
            shared T get() { return t; } 
        } 
        shared void bar(@error Foo foo) {
            T t = foo.get();
        }
    }
    
    abstract class Co2<out T>(T t) {
        shared class Foo() {
            shared T get() { return t; } 
        } 
        shared void bar(@error Co2<T>.Foo foo) {
            T t = foo.get();
        }
    }
    
    interface Invar<T> {}
    class SuperInvar() satisfies Invar<Integer> {}
    interface MixinInvar satisfies Invar<Float> {}
    @error abstract class SubInvar() extends SuperInvar() satisfies MixinInvar {}
    
    interface Contravar<in T> {}
    class SuperContravar() satisfies Contravar<Integer> {}
    interface MixinContravar satisfies Contravar<Float> {}
    abstract class SubContravar() extends SuperContravar() satisfies MixinContravar {}
    
    interface ContravarWithMember<in T> {
        shared void consume(T t) {}
    }
    class SuperContravarWithMember() satisfies ContravarWithMember<Integer> {}
    interface MixinContravarWithMember satisfies ContravarWithMember<Float> {}
    abstract class SubContravarWithMember() extends SuperContravarWithMember() satisfies MixinContravarWithMember {}
    
    interface ContravarBroken<in T> {
        shared formal void consume(T t);
    }
    class SuperContravarBroken() satisfies ContravarBroken<Integer> { 
        shared actual void consume(Integer t) {}
    }
    interface MixinContravarBroken satisfies ContravarBroken<Float> { 
        shared actual void consume(Float t) {}
    }
    @error abstract class SubContravarBroken() extends SuperContravarBroken() satisfies MixinContravarBroken {}
    
    interface Enumerated<out U, in V, W> 
            of E0<U,V,W> | E1<U,V,W> | E2<U,V,W> | E3<U,V,W> | E4<V,W> | E5<U,W> | E6<U,V,W> {}
    @error interface E0<out U, in V, out W> satisfies Enumerated<U,V,W> {}
    @error interface E1<out U, in V, in W> satisfies Enumerated<U,V,W> {}
    @error interface E2<U, in V, W> satisfies Enumerated<U,V,W> {}
    @error interface E3<out U, V, W> satisfies Enumerated <U,V,W>{}
    interface E4<in V, W> satisfies Enumerated<Nothing,V,W> {}
    interface E5<out U, W> satisfies Enumerated<U,Anything,W> {}
    interface E6<out U, in V, W> satisfies Enumerated<U,V,W> {}

    abstract class Algebraic<out U, in V, W>() 
            of A0<U,V,W> | A1<U,V,W> | A2<U,V,W> | A3<U,V,W> | A4<V,W> | A5<U,W> | A6<U,V,W> {}
    @error class A0<out U, in V, out W>() extends Algebraic<U,V,W>() {}
    @error class A1<out U, in V, in W>() extends Algebraic<U,V,W>() {}
    @error class A2<U, in V, W>() extends Algebraic<U,V,W>() {}
    @error class A3<out U, V, W>() extends Algebraic<U,V,W>() {}
    class A4<in V, W>() extends Algebraic<Nothing,V,W>() {}
    class A5<out U, W>() extends Algebraic<U,Anything,W>() {}
    class A6<out U, in V, W>() extends Algebraic<U,V,W>() {}
    
    T genericMethod1<T>(T t) given T satisfies Numeric<T> {
        return t;
    }
    T genericMethod2<T>(T? t) given T satisfies Object {
        if (exists t) {
            return t;
        }
        else {
            throw;
        }
    }
    @error genericMethod1("hello");
    @type:"Integer" genericMethod1(1);
    @type:"String" genericMethod2("hello");
    @type:"String" genericMethod2(true then "hello");
    
    @type:"Iterable<String,Null>" coalesce{null, "hello"};
    @type:"Sequential<String>" join({}, {"hello", "world"}, {"goodbye"});
    
    class ParamOuter<T>() {
        class Inner<Y>(){
            class Innerest() {}
            @error ParamOuter<String>.Inner<Y>.Innerest();
            @error ParamOuter<T>.Inner<Integer>.Innerest();
            @error ParamOuter<T>.Inner<Y>.Innerest();
        }
        @error ParamOuter<String>.Inner<Integer>();
        @error ParamOuter<T>.Inner<Integer>();
    }
        
    void withParamOfMethod<T>(T t) { Anything x = t; } 
    class WithParamOfClass<T>(T t) { Anything x = t; } 

    class N()=>Null();
    class S()=>String();

    void unsatisfiable1<T>() @error given T satisfies Null&String {}
    void unsatisfiable2<T>() @error given T satisfies Null&Container<Anything> {}
    void unsatisfiable3<T>() @error given T satisfies Holder<String>&Holder<Integer> {}
    void unsatisfiable4<T>() @error given T satisfies Holder<Object>&Holder<T> {}
    void unsatisfiable5<T>() @error given T satisfies N&S {}
    void unsatisfiable6<T>() @error given T satisfies Holder<N>&Holder<S> {}
    
}

class Invariance() {

    interface O<T> {}
    interface I satisfies O<I> {}
    void f1<E>() @error given E satisfies O<E> & O<I> {}
    void f2<E>() given E satisfies O<E&I> {}
    void f3<E>() @error given E satisfies O<E> & I {}
    @error interface F1<E> satisfies O<E> & O<I> {}
    interface F2<E> satisfies O<E&I> {}
    @error interface F3<E> satisfies O<E> & I {}
    
    void m<E>(O<E> & O<I> val) 
            given E satisfies O<E> {
        //these fail because of the famous problem with
        //not being able to write down the pricipal
        //instantiation of O<E> & O<I>
        @error O<E> val1 = val;
        @error O<I> val2 = val;
    }
    
    void n<E>(F1<E> f1, F2<E> f2, F3<E> f3) 
            given E satisfies O<E> {
        O<E> & O<I> g1 = f1; 
        O<E&I> g2 = f2; 
        O<E> & O<I> g3 = f3; 
    }
    
}

class CoVariance() {

    interface O<out T> {}
    interface I satisfies O<I> {}
    //nothing really wrong with this, 
    //but the spec says it is an error
    void f1<E>() @error given E satisfies O<E> & O<I> {}
    void f2<E>() given E satisfies O<E&I> {}
    void f3<E>() given E satisfies O<E> & I {}
    //nothing really wrong with this, 
    //but the spec says it is an error
    @error interface F1<E> satisfies O<E> & O<I> {}
    interface F2<E> satisfies O<E&I> {}
    interface F3<E> satisfies O<E> & I {}
    
    void m<E>(O<E> & O<I> val) 
            given E satisfies O<E> {
        O<E> val1 = val;
        O<I> val2 = val;
    }
    
    void n<E>(F1<E> f1, F2<E> f2, F3<E> f3) 
            given E satisfies O<E> {
        O<E> & O<I> g1 = f1; 
        O<E> & O<I> g2 = f2; 
        O<E> & O<I> g3 = f3; 
    }
    
}

interface Bound1 {}
interface Bound2 {}
abstract class WithConstraint<in T>() 
        of WithIntersectionArg|WithBrokenArg1|WithBrokenArg2
        given T satisfies Bound1&Bound2 {}
class WithIntersectionArg() extends WithConstraint<Bound1&Bound2>() {}
@error class WithBrokenArg1() extends WithConstraint<Bound1>() {}
@error class WithBrokenArg2() extends WithConstraint<Anything>() {}
abstract class WithoutConstraint<in T>() 
        of WithAnythingArg {}
class WithAnythingArg() extends WithoutConstraint<Anything>() {}

