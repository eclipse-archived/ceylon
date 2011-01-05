public class Multi(Integer count) {
    void test(Process p) {
        p.writeLine("I am the parent");
        test2(p);
    }

    void test2(Process p) {
        p.writeLine("my count is");
        p.writeLine(count);        
    }
}
public class Multi() extends Multi(5) {
    void test(Process p) {
        p.writeLine("I am the child");
        test2(p);
    }
}
