public class Multi() {
    void test(Process p) {
        p.writeLine("I am the parent");
    }
}
public class Multi(Integer count) extends Multi() {
    void test(Process p) {
        p.writeLine("I am the child");
    }
}
