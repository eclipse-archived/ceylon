class MethodIfIs() {
    shared String m(Object x) {
        if (is Exception x) {
            return x.message;
        }
        return "";
    }
}