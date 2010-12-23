public class Multi() {
    void test(Process p) {
        p.writeLine("I am the parent");
    }
    void check(Process p) {
        Test().test(p);
    }
}
public class Multi(Natural count) extends Multi() {
    void test(Process p) {
        p.writeLine("I am the child");
    }
}
class Test() {
    void test(Process p) {
        Multi().test(p);
        Multi(4).test(p);
    }
}
