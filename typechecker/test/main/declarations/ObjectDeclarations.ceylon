class X<T>(T t) {
    shared T val = t;
    shared class Y() {}
}

object x extends X<String>("Hello") {
    Y y() {
        String s = val;
        String ss = this.val;
        String sss = super.val;
        this.Y();
        Y y = Y();
        return y;
    }
}

class Z() extends X<Integer>(1) {
    Y y() {
        Y y = Y();
        return y;
    }
}

String method() {
    return x.val;
}

shared class TypeParameterResolving<A>(A a) {
    shared object innerObject {
        shared A val = a;
    }
}

void method2() {
    String s = TypeParameterResolving("hello").innerObject.val;
}