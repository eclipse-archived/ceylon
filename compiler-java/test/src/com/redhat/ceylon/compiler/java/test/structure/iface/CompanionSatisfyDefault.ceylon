interface CompanionSatisfyDefault<T> {
    shared default void m() {}
    shared formal void m2();
}
@compileUsing:"companion"
interface CompanionSatisfyDefault2<U> satisfies CompanionSatisfyDefault<U> {
    shared actual void m() {
        super.m();
    }
    m2() => m();
}
class CompanionSatisfyDefault3<X>() satisfies CompanionSatisfyDefault2<X> {}