class X<T>(T t) {
    shared T val = t;
    shared class Y() {}
}

object x extends X<String>("Hello") {
    String s = val;
    String ss = this.val;
    String sss = super.val;
    this.Y();
    Y y = Y();
}

class Z() extends X<Natural>(1) {
    Y y = Y();
}

String method() {
    return x.val;
}