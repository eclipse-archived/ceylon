@nomodel
class FunctionInStatement() {
    void m() {
        if (true) {
            Integer i = 1;
            String foo() { return i.string; }
            String s = foo();
        }
    }
}