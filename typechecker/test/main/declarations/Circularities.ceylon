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
    String hi = CY().hello;
    CX cx = CY();
    @error object o satisfies CB {}
    o.noop();
    CA ca = o;
}

interface CircularConstraints<P,Q,R>
        @error given P satisfies Q
        @error given Q satisfies R
        @error given R satisfies P {}

@error class Good1WithCircularConstraints() satisfies CircularConstraints<String,String,String> {}
@error class Good2WithCircularConstraints() satisfies CircularConstraints<Object,Object,Object> {}
@error class Bad1WithCircularConstraints() satisfies CircularConstraints<Object,Object,String> {}
@error class Bad2WithCircularConstraints() satisfies CircularConstraints<String,String,Object> {}