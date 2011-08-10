@nomodel
class MethodIfExists() {
    shared Natural m(Natural? x) {
        if (exists x) {
            return x;
        }
        return 0;
    }
    shared Natural m2(Natural? x) {
        if (exists y = x) {
            return y;
        }
        return 0;
    }
}