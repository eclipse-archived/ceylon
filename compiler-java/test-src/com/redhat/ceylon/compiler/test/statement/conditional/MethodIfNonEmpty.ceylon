@nomodel
class MethodIfNonEmpty() {
    shared Natural m(Natural[] x) {
        if (nonempty x) {
            Sequence<Natural> s = x;
        }
        return 0;
    }
}