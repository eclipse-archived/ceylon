@nomodel
class KlassWithObjectMember(String x) {
    shared object y {
        String m() {
            return x;
        }
    }
}