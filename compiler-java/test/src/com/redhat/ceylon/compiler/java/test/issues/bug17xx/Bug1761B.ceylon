/*void bug1761B(Anything b = () {
        class Bar() {}
        return Bar();
    }) {
}*/

Anything g = function(Integer i) {
    Integer j = i + 1;
    class Gee() {
        shared actual String string => j.string;
    }
    return Gee();
};
