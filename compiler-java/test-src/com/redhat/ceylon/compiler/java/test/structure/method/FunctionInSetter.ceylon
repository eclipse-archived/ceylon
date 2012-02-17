@nomodel
class FunctionInSetter() {
    String foo {
        return "";
    }
    assign foo {
        if (true) {
            Integer i = 1;
            String bar() {
                return i.string;
            }
            bar();
        }
    }
}