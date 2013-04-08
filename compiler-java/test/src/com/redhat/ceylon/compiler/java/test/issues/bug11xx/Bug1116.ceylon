@nomodel
class Bug1116() {
    void m([String] s) {
        [Object] objects = s;
    }
    void m2([String, String*] s) {
        [Object, String*] objects = s;
    }
    void m3([Object, String*] s) {
        [Object, Object*] objects = s;
    }
    void m4([String, Object*] s) {
        [Object, Object*] objects = s;
    }
}