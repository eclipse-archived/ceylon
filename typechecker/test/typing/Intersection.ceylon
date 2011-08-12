class Intersection() {
    
    interface X {
        shared String hello {
            return "hello";
        }
    }
    
    interface Y {
        shared String goodbye {
            return "goodbye";
        }
    }
    
    class XY() satisfies X & Y {}
    
    X&Y xy = XY();
    @type["String"] value hi = xy.hello;
    @type["String"] value bye = xy.goodbye;
    X x = xy;
    Y y = xy;
    
    object xOnly satisfies X {}
    
    @error X&Y xyError = xOnly;
    
    @type["Intersection.X&Intersection.Y"] X&Object&Y xo = XY();
    @type["Bottom"] X&Bottom&Y xb;
    
    @type["Intersection.X&Intersection.Y"] function f(X&Y xy) {
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
            writeLine(xy.hello);
            return xy;
        }
    }
    
    class Good2() extends Super() {
        shared actual X&Y&Object get(X&Y xy) {
            writeLine(xy.goodbye);
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
    
    Sequence<X&Y> seqxy = { XY(), XY() };
    Sequence<X>&Sequence<Y> useq = seqxy;
    
    Consumer<X|Y> consxy = Consumer<X|Y>();
    Consumer<X>&Consumer<Y> ucons = consxy;
    
    interface WithParam<T> {
    	shared T get() { throw; }
    }
    class WithUnionArg<U,V>() satisfies WithParam<U|V> {}
    class WithIntersectionArg<U,V>() satisfies WithParam<U&V> {}
    class WithMoreIntersectionArg<U,V,W>() satisfies WithParam<U&V&W> {}
    WithParam<String&X|String&Y> wp1 = WithIntersectionArg<String,X|Y>();
    WithParam<String&X|String&Y> wp2 = WithUnionArg<String&X,String&Y>();
    WithParam<String&X|String&Y|Float&X|Float&Y> wp3 = WithIntersectionArg<String|Float,X|Y>();
    WithParam<String&X&Float|String&Y&Float|String&X&Natural|String&Y&Natural> wp4 = WithMoreIntersectionArg<String,X|Y,Float|Natural>();
    
    @type["String&Intersection.X|String&Intersection.Y"] WithIntersectionArg<String,X|Y>().get();
    @type["String&Intersection.X|String&Intersection.Y|Float&Intersection.X|Float&Intersection.Y"] WithIntersectionArg<String|Float,X|Y>().get();
    
}