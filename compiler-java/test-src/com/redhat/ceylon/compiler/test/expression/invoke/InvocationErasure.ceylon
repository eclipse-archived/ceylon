@nomodel
class MethodErasure() {
    Integer m1() {
        return hash;
    }
    Integer m2() {
        return this.hash;
    }
    String m3() {
        return string;
    }
    String m4() {
        return this.string;
    }
    void m5() {
        hashCode();
        this.hashCode();
        toString();
        this.toString();
    }
    String hashCode() {
        return "bar";
    }
    Natural toString() {
        return 0;
    }
}