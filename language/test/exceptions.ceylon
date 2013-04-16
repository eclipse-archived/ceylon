class MyException() extends Exception("my exception", null) {}
class OtherException() extends Exception("other exception", null) {}

class ResourceException(String msg) extends Exception(msg, null) {}
class MyResource(Integer err) satisfies Closeable {
    shared variable Integer state = 0;
    
    if (err == 1) {
        throw ResourceException("init resource");
    }

    state += 1;
    
    shared actual void open() {
        state += 2;
        if (err == 2) {
            throw ResourceException("open resource");
        }
        state += 4;
    }

    shared actual void close(Exception? exception) {
        state += 8;
        if (err == 3) {
            throw ResourceException("close resource");
        }
        state += 16;
    }
}

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

    variable Integer pass = 0;
    variable MyResource res = MyResource(0);
    try (res) {
        pass++;
    } catch (Exception e) {
        fail("try-with-resource 1 unexpected exception");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-resource 1 pass check");
    check(res.state==31, "try-with-resource 1 resource state check");

    pass = 0;
    res = MyResource(0);
    try (res) {
        pass++;
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-resource 2 exception message");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-resource 2 final check");
    check(res.state==31, "try-with-resource 2 resource state check");
    
    pass = 0;
    try (MyResource(1)) {
        fail("try-with-resource 3 unexpected try-block execution");
    } catch (Exception e) {
        pass++;
        check(e.message=="init resource", "try-with-resource 3 exception message");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-resource 3 pass check");
    
    pass = 0;
    res = MyResource(2);
    try (res) {
        fail("try-with-resource 4 unexpected try-block execution");
    } catch (Exception e) {
        pass++;
        check(e.message=="open resource", "try-with-resource 4 exception message");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-resource 4 pass check");
    check(res.state==3, "try-with-resource 4 resource state check");
    
    pass = 0;
    res = MyResource(3);
    try (res) {
        pass++;
    } catch (Exception e) {
        pass++;
        check(e.message=="close resource", "try-with-resource 5 exception message");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-resource 5 pass check");
    check(res.state==15, "try-with-resource 5 resource state check");
    
    pass = 0;
    res = MyResource(3);
    try (res) {
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-resource 6 exception message");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-resource 6 pass check");
    check(res.state==15, "try-with-resource 6 resource state check");
}
