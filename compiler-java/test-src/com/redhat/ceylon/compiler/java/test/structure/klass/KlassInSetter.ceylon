@nomodel
class KlassInSetter() {
    String foo {
        return "";
    }
    assign foo {
        if (true) {
            Integer i = 1;
            @nomodel
            class Bar() {
                shared String bar() {
                    return i.string;
                }
            }
            Bar().bar();
        }
    }
}