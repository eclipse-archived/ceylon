@nomodel
class ObjectInMethod() {
    void m() {
        Integer i = 1;
        @nomodel
        object foo { shared actual String string = i.string; }
        String s = foo.string;
    }
}