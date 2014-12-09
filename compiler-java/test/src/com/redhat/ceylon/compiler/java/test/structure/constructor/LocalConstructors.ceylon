@noanno
void localConstructors<X>(X x) {
    class C<Y> {
        shared new C(X x, Y? y=null) {}
        shared new NonDefault(Y y) {}
    }
    variable C<String> c = C<String>.C(x);
    c = C<String>.C(x, "");
    c = C<String>.NonDefault("");
    class Sub<Z> extends C<Z> {
        shared new Sub(Z z) extends super.C(x, z) {}
        shared new NonDefault(Z z) extends super.NonDefault(z) {}
    }
}