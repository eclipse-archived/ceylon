// FIXME THis is not correct yet
abstract class AbstractFormal() {
    shared void m() {
        test();
        test2();
        test3();
    }
    shared formal void test();
    shared default void test2() { return; }
    shared default void test3() { return; }
}
class ActualKlass() extends AbstractFormal() {
    shared actual void test() { return; }
    shared actual void test3() { return; }
}