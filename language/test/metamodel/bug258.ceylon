
class Bug258(String name){
    shared actual Boolean equals(Object other){
        if(is Bug258 other, other.name == name){
            return true; 
        }
        return false;
    }
}

void bug258(){
    value objectEquals = `Object.equals`;
    assert (objectEquals("foo")("foo"));
    assert (!objectEquals("foo")("bar"));

    // depends on https://github.com/ceylon/ceylon.language/issues/261
    //value basicEquals = `Basic.equals`;
    //assert (basicEquals(Bug258("foo"))(Bug258("foo")));
    //assert (!basicEquals(Bug258("foo"))(Bug258("bar")));

    value objectString = `Object.string`;
    assert (objectString("foo").get() == "foo");

    value objectHash = `Object.hash`;
    assert (objectHash("foo").get() == "foo".hash);
    
    value xCause = Exception();
    value x = Exception("foo", xCause);

    value exception = `Exception`;
    value x2 = exception("foo", xCause);
    assert (exists iNeedEqDamnit = x2.cause, iNeedEqDamnit == xCause);
    assert (x2.message == "foo");
    
    value exceptionCause = `Exception.cause`;
    assert (exists grr = exceptionCause(x).get(), grr == xCause);

    value exceptionMessage = `Exception.message`;
    assert (exceptionMessage(x).get() == x.message);

    value exceptionPrintStackTrace = `Exception.printStackTrace`;
    print("Don't panic");
    exceptionPrintStackTrace(x)();
    print("Don't panic: that last stack trace was part of the test");
}