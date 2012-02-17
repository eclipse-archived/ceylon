@nomodel
class ObjectInGetter() {
    String foo {
        if (true) {
            Integer i = 1;
            @nomodel
            object bar {
                shared actual String string { return i.string; }
            }
            return bar.string;
        }
        return "";
    }
}