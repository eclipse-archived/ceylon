@nomodel
class KlassInGetter() {
    String foo {
        if (true) {
            Integer i = 1;
            @nomodel
            class Bar() {
                shared String bar() {
                    return i.string;
                }
            }
            return Bar().bar();
        }
        return "";
    }
}