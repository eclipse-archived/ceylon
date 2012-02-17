@nomodel
class FunctionInGetter() {
    String foo {
        if (true) {
            Integer i = 1;
            String bar() {
                return i.string;
            }
            return bar();
        }
        return "";
    }
}