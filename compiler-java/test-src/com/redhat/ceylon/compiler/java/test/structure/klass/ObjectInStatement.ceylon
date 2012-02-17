@nomodel
class ObjectInStatement() {
    void m() {
        if (true) {
            Integer i = 1;
            @nomodel
            object foo { shared actual String string = i.string; }
            String s = foo.string;
        }
    }
}