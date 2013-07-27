import ceylon.language { S=String }

class Intersection() {
    
    class String(S s) {}
    class Float() {}
    
    interface X {
        shared String hello {
            return String("hello");
        }
    }
    
    interface Y {
        shared String goodbye {
            return String("goodbye");
        }
    }
    
    class XY() satisfies X & Y {}
    
    X&Y xy = XY();
    @type:"Intersection.String" value hi = xy.hello;
    @type:"Intersection.String" value bye = xy.goodbye;
    X x = xy;
    Y y = xy;
    
    object xOnly satisfies X {}
    
    @error X&Y xyError = xOnly;
    
    @type:"Intersection.X&Object&Intersection.Y" X&Object&Y xo = XY();
    @type:"Intersection.X&Object&Intersection.Y" X&Object&Y xo1 = xo;
    @type:"Intersection.X&Nothing&Intersection.Y" X&Nothing&Y xb = nothing;
    @type:"Nothing" value xb1 = xb;
    @type:"Nothing" Nothing xb2 = xb;
    
    @type:"Intersection.X&Intersection.Y" function f(X&Y xy) {
        return xy;
    }
    
    f(xy);
    @error f(xOnly);
    
    class Super() {
        shared default X&Y get(X&Y&Object xy) {
            return xy;
        }
    }
    
    class Good1() extends Super() {
        shared actual X&Y get(X&Y&Object xy) {
            print(xy.hello);
            return xy;
        }
    }
    
    class Good2() extends Super() {
        shared actual X&Y&Object get(X&Y xy) {
            print(xy.goodbye);
            return xy;
        }
    }
    
    class Bad() extends Super() {
        @error shared actual X get(@error X&Object xy) {
            return xy;
        }
    }
    
    class Consumer<in T>() {
        shared void consume(T t) {}
    }
    
    Consumer<X>|Consumer<Y> consumer = Consumer<X>();
    Consumer<X&Y> c = consumer;
    consumer.consume(xy);
    @error consumer.consume(xOnly);
    @error Consumer<X>&Consumer<Y> errc = c;
    
    Sequence<X&Y> seqxy = [ XY(), XY() ];
    Sequence<X>&Sequence<Y> useq = seqxy;
    
    Consumer<X|Y> consxy = Consumer<X|Y>();
    Consumer<X>&Consumer<Y> ucons = consxy;
    
    interface WithParam<out T> {
    	shared T get() { throw; }
    }
    class WithUnionArg<U,V>() satisfies WithParam<U|V> {}
    class WithIntersectionArg<U,V>() satisfies WithParam<U&V> {}
    class WithMoreIntersectionArg<U,V,W>() satisfies WithParam<U&V&W> {}
    WithParam<String&X|String&Y> wp1 = WithIntersectionArg<String,X|Y>();
    WithParam<String&X|String&Y> wp2 = WithUnionArg<String&X,String&Y>();
    WithParam<String&X|String&Y|Float&X|Float&Y> wp3 = WithIntersectionArg<String|Float,X|Y>();
    WithParam<String&X&Float|String&Y&Float|String&X&Integer|String&Y&Integer> wp4 = WithMoreIntersectionArg<String,X|Y,Float|Integer>();
    
    @type:"Intersection.String&Intersection.X|Intersection.String&Intersection.Y" WithIntersectionArg<String,X|Y>().get();
    @type:"Intersection.String&Intersection.X|Intersection.String&Intersection.Y|Intersection.Float&Intersection.X|Intersection.Float&Intersection.Y" WithIntersectionArg<String|Float,X|Y>().get();
    
    interface One {}
    interface Two {}
    object one satisfies One {}
    object onetwo satisfies One&Two {}
    
    @type:"Sequence<Intersection.One>&Sequence<Intersection.Two>" 
    Sequence<One>&Sequence<Two> seq = [ onetwo ];
    @type:"Sequence<Intersection.One&Intersection.Two>" value seq1 = seq;
    @type:"Sequence<Intersection.One&Intersection.Two>" Sequence<One&Two> seq2 = seq;
    @type:"Intersection.One&Intersection.Two" value item = seq[0];
    @type:"Intersection.One&Intersection.Two" value fst = seq.first;
    @type:"Iterator<Intersection.One&Intersection.Two>" value itr = seq.iterator();
    
    @type:"Intersection.Consumer<Intersection.One>&Intersection.Consumer<Intersection.Two>" 
    Consumer<One>&Consumer<Two> cons = Consumer<One|Two>();
    @type:"Intersection.Consumer<Intersection.One|Intersection.Two>" value cons1 = cons;
    @type:"Intersection.Consumer<Intersection.One|Intersection.Two>" Consumer<One|Two> cons2 = cons;
    cons.consume(onetwo);
    cons.consume(one);
    One|Two unk = one;
    cons.consume(unk);
    if (is One&Two unk) {
        //todo
        @type:"Intersection.One&Intersection.Two" value unkv = unk;
        Two two = unk;
    }
    
    class I<T>(T t) {} 
    
    A&B intersect<A,B>(A a, B b) { throw; }
    @type:"Nothing" intersect(1, "hello");
    @type:"Nothing" intersect(null, {"hello"});
    @type:"Integer" intersect(1, 3);
    String[] onestring = [String("hello")];
    @type:"Intersection.Float&Sequential<Intersection.String>" intersect(Float(), onestring);
    S[] ones = ["hello"];
    @type:"Intersection.Float&Sequential<String>" intersect(Float(), ones);
    @type:"Nothing" intersect(Float(), ["hello"]);
    @type:"Nothing" intersect(I({"hello"}), I({}));
    
    interface I1 {} 
    interface I2 {}

    void meth(I1[]&I2[] seq) {
        @type:"Null|Intersection.I1&Intersection.I2" value item = seq[4];
    }
    
    Integer m1 = max([1, 2, 3]);
    Null m2 = max([]);
    Integer? m3 = max(join([],[1, 2, 3]));
    Integer? m4 = max({1, 2, 3}.filter((Integer i) => i>0));
    @type:"Integer" max([1, 2, 3]);
    @type:"Null" max([]);
    @type:"Null|Integer" max(join([],[1, 2, 3]));
    @type:"Null|Integer" max({1, 2, 3}.filter((Integer i) => i>0));
    
    interface Multi<out X, out Y> {}
    Multi<Nothing,Integer>&Multi<Integer,Nothing> multiinter = nothing;
    Multi<Nothing,Integer>&Multi<Integer,Nothing> multiunion = nothing;
    Multi<Nothing,Nothing> check = multiinter;
    @error Multi<Nothing,Nothing> check = multiunion;
    
}

interface IntersectionCanonicalization {

    class Inv<T>() {}
    
    interface Co<out T> of T {}
    interface A satisfies Co<A> {}
    interface B satisfies Co<B> {}
    Inv<A&B&Co<A&B>> foo1(Inv<A&B> inv) => inv;
    
    interface Contra<in T> of T {}
    interface C satisfies Contra<C> {}
    interface D satisfies Contra<D> {}
    Inv<C&D&Contra<C|D>> foo2(Inv<C&D> inv) => inv;

}