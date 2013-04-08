@nomodel
class Bug1116() {
    void m([String] s) {
        [Object] objects = s;//cast
    }
    
    void m5([String, String] s) {
        [Object, String] objects = s;//cast
    }
    void m2([String, String*] s) {
        [Object, String*] objects = s;//cast
    }
    
    void m6([Object, String] s) {
        [Object, Object] objects = s;//cast
    }
    void m3([Object, String*] s) {
        [Object, Object*] objects = s;
    }
    
    void m4([String, Object*] s) {
        [Object, Object*] objects = s;
    }
    void m7([String, Object] s) {
        [Object, Object] objects = s;
    }
}