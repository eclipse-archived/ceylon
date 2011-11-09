@nomodel
class MethodWithLocalObject() {
    void m() {
        value ss = "foo";
        object y {
            shared String m() {
                return ss;
            }
        }
        String s = y.m();
    }
}