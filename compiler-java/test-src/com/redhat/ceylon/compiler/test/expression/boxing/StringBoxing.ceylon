@nomodel
class StringBoxing(){
    void m() {
        String s1 = "TEST";
        String s2 = s1.lowercase();
        String s3 = upper(s2);
        String? s4 = upper2(s3);
        String s5 = upper3(s4);
        String s6 = upper3(s4).uppercase();
        s6.compare(s5);
    }
    String upper(String s) {
        return s.uppercase();
    }
    String? upper2(String? s) {
        if (exists s) {
            return s.uppercase();
        }
        return null;
    }
    String upper3(String? s) {
        if (exists s) {
            return s.uppercase();
        } else {
            return "";
        }
    }
}