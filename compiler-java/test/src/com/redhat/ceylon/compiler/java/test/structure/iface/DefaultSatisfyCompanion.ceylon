@compileUsing:"companion"
interface DefaultSatisfyCompanion<T> {
    shared default void m() {}
}
interface DefaultSatisfyCompanion2<U> satisfies DefaultSatisfyCompanion<U> {
    shared actual void m() {
        super.m();
    }
}
class DefaultSatisfyCompanion3<X>() satisfies DefaultSatisfyCompanion2<X> {}