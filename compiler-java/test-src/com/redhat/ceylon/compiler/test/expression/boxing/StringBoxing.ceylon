@nomodel
class StringBoxing(){
    void m() {
        String s1 = "TEST";
        String s2 = s1.lowercased;
        String s3 = upper(s2);
        String? s4 = upper2(s3);
        String s5 = upper3(s4);
        String s6 = upper3(s4).uppercased;
        s6.compare(s5);
    }
    String upper(String s) {
        return s.uppercased;
    }
    String? upper2(String? s) {
        if (exists s) {
            return s.uppercased;
        }
        return null;
    }
    String upper3(String? s) {
        if (exists s) {
            return s.uppercased;
        } else {
            return "";
        }
    }
}