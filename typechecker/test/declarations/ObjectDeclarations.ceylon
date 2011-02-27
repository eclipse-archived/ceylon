class X<T>(T t) {
    shared T val = t;
}

object x extends X<String>("Hello") {
    String s = val;
    String ss = this.val;
    String sss = super.val;
}

String method() {
    return x.val;
}