@nomodel
class SequenceInstantiation(){
    shared Sequence<Natural> m() {
        return { 1, 2, 3, n1, n2() };
    }
    Natural n1 {
        return 4;
    }
    Natural n2() {
        return 5;
    }
}