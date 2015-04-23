@noanno
class CtorAbstractCtorSuper {
    shared new Foo(Boolean b) {
        checker.note("super");
    }
}

object checker {
    variable String[] s = [];
    shared void reset() {
        s=[];
    }
    shared void note(String str) {
        s = [str, *s];
    }
    shared void check(String expect) {
        value r = s.reversed.string;
        print(r);
        if (expect != r) {
            print("X");
            //throw AssertionError("Expected ``expect`` but got ``r``");
        }
    }
}

@noanno
class CtorAbstractCtorPartial extends CtorAbstractCtorSuper {
    Integer x;
    Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        checker.note("Rest");
        this.y = y;
        print(this.x);
    }
    print(this.x);
    checker.note("c");
}
@noanno
class CtorAbstractCtorPartialShared extends CtorAbstractCtorSuper {
    shared Integer x;
    shared Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        checker.note("Rest");
        this.y = y;
    }
    checker.note("c");
}
@noanno
class CtorAbstractCtorPartialCaptured extends CtorAbstractCtorSuper {
    Integer x;
    Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        checker.note("Rest");
        this.y = y;
    }
    checker.note("c");
    shared Integer capture() {
        return x+y;
    }
}


@noanno
class CtorAbstractCtorPartialVariable extends CtorAbstractCtorSuper {
    variable Integer x;
    variable Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(1);
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(4);
        checker.note("Rest");
        this.y = y;
        object yCapturer {
            shared Integer capture(Integer incr) {
                return outer.y+=incr;
            }
        }
        yCapturer.capture(1);
    }
    object xCapturer3 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    xCapturer3.capture(8);
    object yCapturer2 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    yCapturer2.capture(2);
    checker.note("c");
    print(x);
    print(y);
}
@noanno
class CtorAbstractCtorPartialSharedVariable extends CtorAbstractCtorSuper {
    shared variable Integer x;
    shared variable Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(1);
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(4);
        checker.note("Rest");
        this.y = y;
        object yCapturer {
            shared Integer capture(Integer incr) {
                return outer.y+=incr;
            }
        }
        yCapturer.capture(1);
    }
    object xCapturer3 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    xCapturer3.capture(8);
    object yCapturer2 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    yCapturer2.capture(2);
    checker.note("c");
    print(x);
    print(y);
}
@noanno
class CtorAbstractCtorPartialCapturedVariable extends CtorAbstractCtorSuper {
    variable Integer x;
    variable Integer y;
    checker.note("a");
    abstract new Partial(Integer x) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(1);
    }
    checker.note("b");
    shared new Rest(Integer y) extends Partial(1) {
        object xCapturer {
            shared Integer capture(Integer incr) {
                return outer.x+=incr;
            }
        }
        xCapturer.capture(4);
        checker.note("Rest");
        this.y = y;
        object yCapturer {
            shared Integer capture(Integer incr) {
                return outer.y+=incr;
            }
        }
        yCapturer.capture(1);
    }
    object xCapturer3 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    xCapturer3.capture(8);
    object yCapturer2 {
        shared Integer capture(Integer incr) {
            return outer.x+=incr;
        }
    }
    yCapturer2.capture(2);
    checker.note("c");
    print(x);
    print(y);
    shared Integer capture() {
        return x+y;
    }
}

@noanno
class CtorAbstractCtorGeneric<T> extends CtorAbstractCtorSuper {
    T? x;
    T? y;
    checker.note("a");
    abstract new Partial(T? x=null) extends Foo(true) {
        checker.note("Partial");
        this.x = x;
    }
    checker.note("b");
    shared new Rest(T? y=null) extends Partial() {
        checker.note("Rest");
        this.y = y;
        print(this.x);
    }
    print(this.x);
    checker.note("c");
}

shared void runCtorAbstractCtor() {
    checker.reset();
    CtorAbstractCtorPartial.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    checker.reset();
    CtorAbstractCtorPartialShared.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    checker.reset();
    CtorAbstractCtorPartialCaptured.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    
    checker.reset();
    CtorAbstractCtorPartialVariable.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    checker.reset();
    CtorAbstractCtorPartialSharedVariable.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    checker.reset();
    CtorAbstractCtorPartialCapturedVariable.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
    checker.reset();
    CtorAbstractCtorGeneric.Rest(1);
    checker.check("[super, a, partial, b, Rest, c]");
}