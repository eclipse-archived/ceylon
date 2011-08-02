@nomodel
class StringBoxing(){
    void m() {
        String s1 = "TEST";
        String s2 = s1.lowercase();
        String s3 = upper(s2);
    }
    String upper(String s) {
        return s.uppercase();
    }
}