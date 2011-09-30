@nomodel
class KlassWithObjectInInitialiser(String x) {
    shared object y {
        String m() {
            return x;
        }
    }
}