@nomodel
class MethodWithLocalObject() {
    void m() {
        object y {
            shared String m() {
                return "foo";
            }
        }
        String s = y.m();
    }
}