class MethodIfExists() {
    shared Natural m(Natural? x) {
        if (exists x) {
            return x;
        }
        return 0;
    }
}