@nomodel
class InitializerObjectInStatement() {
    for (i in 1..10) {
        @nomodel
        object foo { shared actual String string = i.string; }
        String s = foo.string;
    }
}