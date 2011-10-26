@nomodel
class LocalMethod() {
    void m() {
        object y {
            shared String m() {
                return "foo";
            }
        }
        String s = y.m();
    }
}