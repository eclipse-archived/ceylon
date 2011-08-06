@nomodel
class MethodErasure() {
    shared actual String string {
        return "foo";
    }
    shared actual Integer hash {
        return +0;
    }
    String hashCode() {
        return "bar";
    }
    Natural toString() {
        return 0;
    }
}