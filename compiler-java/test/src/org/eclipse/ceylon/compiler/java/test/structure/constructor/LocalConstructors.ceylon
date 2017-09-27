@noanno
void localConstructors<X>(X x) {
    class C<Y> {
        shared new (X x, Y? y=null) {}
        shared new nonDefault(Y y) {}
    }
    variable C<String> c = C<String>(x);
    c = C<String>(x, "");
    c = C<String>.nonDefault("");
    class Sub<Z> extends C<Z> {
        shared new (Z z) extends C<Z>(x, z) {}
        shared new nonDefault(Z z) extends super.nonDefault(z) {}
    }
}