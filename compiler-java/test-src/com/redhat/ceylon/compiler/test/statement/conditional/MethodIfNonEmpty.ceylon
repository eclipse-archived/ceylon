@nomodel
class MethodIfNonEmpty() {
    shared Natural m(Natural[] x) {
        if (nonempty x) {
            return 1;
        }
        return 0;
    }
}