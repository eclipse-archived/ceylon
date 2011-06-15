class MethodIfExists() {
    shared Integer m(Integer? x) {
        if (exists x) {
            return x;
        }
        return 0;
    }
}