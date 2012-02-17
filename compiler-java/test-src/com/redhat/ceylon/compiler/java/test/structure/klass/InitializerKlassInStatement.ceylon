@nomodel
class InitializerKlassInStatement() {
    for (i in 1..10) {
        @nomodel
        class KlassInStatement1() {
            shared actual String string {return i.string;}
        }
        String s = KlassInStatement1().string;
    }
}