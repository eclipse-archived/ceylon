class MethodIfIs() {
    shared String m() {
        Object x = Exception();
        if (is Exception x) {
            return x.message;
        }
        return "";
    }
}