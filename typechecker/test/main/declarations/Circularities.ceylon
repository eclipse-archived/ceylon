@error class CX() extends CY() {
    shared String hello = "hello";
}
@error class CY() extends CX() {}

@error interface CA satisfies CB {
    shared void noop() {}
}
@error interface CB satisfies CA {}

class WithCircularTypeParams<S,T>()
    @error given S satisfies T
    @error given T satisfies S {}


class WithCircularTypeParams2<S,T>()
    @error given S satisfies T & WithCircularTypeParams2<S,T>
    @error given T satisfies S & WithCircularTypeParams2<S,T> {}


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