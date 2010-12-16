public class Multi() {
    void test(Process p) {
        p.writeLine("I am the parent");
    }
}
public class Multi(Natural count) extends Multi() {
    void test(Process p) {
        p.writeLine("I am the child");
    }
}
