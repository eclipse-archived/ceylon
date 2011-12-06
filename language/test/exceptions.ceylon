class MyException() extends Exception("my exception", null) {}
class OtherException() extends Exception("other exception", null) {}

void exceptions() {
    variable Boolean caught:=false;
    try {
        throw MyException();
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (MyException me) {
        caught:=true;
        assert(me.message=="my exception", "exception message");
        assert(!exists me.cause, "exception cause");
    }
    assert(caught, "caught");

    caught:=false;
    try {
        throw MyException();
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (Exception me) {
        caught:=true;
        assert(me.message=="my exception", "exception message");
        assert(!exists me.cause, "exception cause");
    }
    assert(caught, "caught");
    
    caught:=false;
    try {
        throw MyException();
    }
    catch (OtherException|MyException e) {
        caught:=true;
        assert(e.message=="my exception", "exception message");
        assert(!exists e.cause, "exception cause");
    }
    catch (Exception me) {
        fail("any exception");
    }
    assert(caught, "caught");
    
    caught:=false;
    try {
        throw Exception("hello", MyException());
    }
    catch (Exception e) {
        caught:=true;
        assert(e.message=="hello", "exception message");
        assert(exists e.cause, "exception cause");
        assert(is MyException e.cause, "exception cause");
    }
    assert(caught, "caught");
    
    caught:=false;
    try {
        try {
            throw Exception(null, null);
        }
        catch (MyException me) {
            caught:=true;
        }
    }
    catch (Exception e) {}
    assert(!caught, "caught");
    
}