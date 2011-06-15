class MethodIfExists() {
    shared Integer m() {
        variable Integer? x = 5;
        if (exists x) {
            return x;
        }
        return 0;
    }
}