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
    object o satisfies CB {}
    o.noop();
    CA ca = o;
}