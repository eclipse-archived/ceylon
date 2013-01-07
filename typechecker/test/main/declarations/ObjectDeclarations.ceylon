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
    shared object innerValue {
        shared A val = a;
    }
}

void method2() {
    String s = TypeParameterResolving("hello").innerValue.val;
}


interface Something {
    shared formal Object something;
}

object se1 satisfies Something {
    shared actual String something = "something";
}

object se2 satisfies Something {
    shared actual String something { 
        return "something"; 
    }
}

void testSomething(Something s) {
    @type:"Object" value ss = s.something;
    @type:"String" value ss1 = se1.something;
    @type:"String" value ss2 = se2.something;
}