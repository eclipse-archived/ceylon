@nomodel
class MethodErasure() {
    Integer m1() {
        return hash;
    }
    String m2() {
        return string;
    }
    void m3() {
        hashCode();
        toString();
    }
    String hashCode() {
        return "bar";
    }
    Natural toString() {
        return 0;
    }
}