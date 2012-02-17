@nomodel
class ObjectInSetter() {
    String foo {
        return "";
    }
    assign foo {
        if (true) {
            Integer i = 1;
            @nomodel
            object bar {
                shared actual String string { return i.string; }
            }
            String b = bar.string;
        }
    }
}