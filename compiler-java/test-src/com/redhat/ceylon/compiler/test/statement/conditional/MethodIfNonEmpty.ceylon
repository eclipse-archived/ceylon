class MethodIfNonEmpty() {
    shared Integer m(Sequence<Integer> x) {
        if (nonempty x) {
            return 1;
        }
        return 0;
    }
}