class MyException() extends Exception("my exception", null) {}
class OtherException() extends Exception("other exception", null) {}

void exceptions() {
    variable Boolean caught=false;
    try {
        throw MyException();
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (MyException me) {
        caught=true;
        check(me.message=="my exception", "exception message");
        check(!me.cause exists, "exception cause");
    }
    check(caught, "caught");

    caught=false;
    try {
        throw MyException();
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (Exception me) {
        caught=true;
        check(me.message=="my exception", "exception message");
        check(!me.cause exists, "exception cause");
    }
    check(caught, "caught");
    
    caught=false;
    try {
        throw MyException();
    }
    catch (OtherException|MyException e) {
        caught=true;
        check(e.message=="my exception", "exception message");
        check(!e.cause exists, "exception cause");
    }
    catch (Exception me) {
        fail("any exception");
    }
    check(caught, "caught");
    
    caught=false;
    try {
        throw Exception("hello", null);
    }
    catch (OtherException|MyException e) {
        fail("any exception");
    }
    catch (Exception me) {
        caught=true;
    }
    check(caught, "caught");
    
    caught=false;
    try {
        throw Exception("hello", MyException());
    }
    catch (Exception e) {
        caught=true;
        check(e.message=="hello", "exception message");
        check(e.cause exists, "exception cause");
        check(e.cause is MyException, "exception cause");
    }
    check(caught, "caught");
    
    caught=false;
    try {
        try {
            throw Exception(null, null);
        }
        catch (MyException me) {
            caught=true;
        }
    }
    catch (Exception e) {}
    check(!caught, "caught");
    
}
