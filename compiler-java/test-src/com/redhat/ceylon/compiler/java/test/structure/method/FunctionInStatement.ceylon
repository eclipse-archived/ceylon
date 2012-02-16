@nomodel
class FunctionInStatement() {
    void m() {
        for (i in 1..10) {
            String foo() { return i.string; }
            String s = foo();
        }
    }
}