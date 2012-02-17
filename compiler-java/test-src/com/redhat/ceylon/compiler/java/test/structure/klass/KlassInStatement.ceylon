@nomodel
class KlassInStatement() {
    void m() {
        if (true) {
        Integer i = 1;
            @nomodel
            class KlassInStatement1() {
                shared actual String string {return i.string;}
            }
            String s = KlassInStatement1().string;
        }
    }
}