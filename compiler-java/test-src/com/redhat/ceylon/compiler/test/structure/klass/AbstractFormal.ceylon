abstract class AbstractFormal() {
    shared void m() {
        test();
        test2();
        test3();
        test4();
    }
    shared formal void test();
    shared default void test2() { return; }
    shared default void test3() { return; }
    shared default void test4() { return; }
}
class ActualKlass() extends AbstractFormal() {
    shared actual void test() { return; }
    shared actual void test3() { return; }
    shared actual default void test4() { return; }
}
class ActualSubKlass() extends ActualKlass() {
    shared actual void test4() { return; }
}