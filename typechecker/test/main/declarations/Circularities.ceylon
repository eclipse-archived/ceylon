@error class CX() extends CY() {
    shared String hello = "hello";
}
@error class CY() extends CX() {}

@error interface CA satisfies CB {
    shared void noop() {}
}
@error interface CB satisfies CA {}

class WithCircularTypeParam<T>() 
    @error given T satisfies T {}

class WithCircularTypeParams<S,T>()
    @error given S satisfies T
    @error given T satisfies S {}


class WithCircularTypeParams2<S,T>()
    @error given S satisfies T & WithCircularTypeParams2<S,T>
    @error given T satisfies S & WithCircularTypeParams2<S,T> {}

class Crazy1<T>() 
    @error given T satisfies X {
    alias X => T;
}
interface Silly<T> {}
class Crazy2<T>() 
        given T satisfies Silly<X> {
    alias X => T;
}

void testMemberResolutionOnCircular() {
    @error String hi = CY().hello;
    @error CX cx = CY();
    object o satisfies CB {}
    @error o.noop();
    @error CA ca = o;
}

interface CircularConstraints<P,Q,R>
        @error given P satisfies Q
        @error given Q satisfies R
        @error given R satisfies P {}

class Good1WithCircularConstraints() satisfies CircularConstraints<String,String,String> {}
class Good2WithCircularConstraints() satisfies CircularConstraints<Object,Object,Object> {}
class Bad1WithCircularConstraints() satisfies CircularConstraints<Object,Object,String> {}
class Bad2WithCircularConstraints() satisfies CircularConstraints<String,String,Object> {}

@error class Circ satisfies Circ {
    shared new circ() {}  
}

alias Stt<T> => Sett<T>;

@error interface Sett<T> satisfies List&Stt&Sett {}

void withSet(Sett<Object> set) {
    List list = set;
}