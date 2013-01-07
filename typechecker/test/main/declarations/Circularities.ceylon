@error class CX() extends CY() {
    @error shared String hello = "hello";
}
@error class CY() extends CX() {}

@error interface CA satisfies CB {
    @error shared void noop() {}
}
@error interface CB satisfies CA {}

class WithCircularTypeParams<S,T>()
    given S satisfies T
    given T satisfies S {}


class WithCircularTypeParams2<S,T>()
    given S satisfies T & WithCircularTypeParams2<S,T>
    given T satisfies S & WithCircularTypeParams2<S,T> {}


void testMemberResolutionOnCircular() {
    String hi = CY().hello;
    CX cx = CY();
    @error object o satisfies CB {}
    o.noop();
    CA ca = o;
}

interface CircularConstraints<P,Q,R>
        given P satisfies Q
        given Q satisfies R
        given R satisfies P {}

class Good1WithCircularConstraints() satisfies CircularConstraints<String,String,String> {}
class Good2WithCircularConstraints() satisfies CircularConstraints<Value,Value,Value> {}
@error class Bad1WithCircularConstraints() satisfies CircularConstraints<Value,Value,String> {}
@error class Bad2WithCircularConstraints() satisfies CircularConstraints<String,String,Value> {}