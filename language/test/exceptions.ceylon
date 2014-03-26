class MyException() extends Exception("my exception", null) {}
class OtherException() extends Exception("other exception", null) {}
class MyError() extends Error("my error", null) {}
class OtherError() extends Error("other error", null) {}

variable Integer sharedState = -1;

class ResourceException(String msg) extends Exception(msg, null) {}

Integer stateUninit = 0;
Integer statePostInit = 1;
Integer statePreObtain = 2;
Integer statePostObtain = 4;
Integer statePreRelease = 8;
Integer statePostRelease = 16;
Integer statePreDestroy = 8;
Integer statePostDestroy = 16;

Integer errNone = 0;
Integer errInInit = 1;
Integer errInObtain = 2;
Integer errInRelease = 3;
Integer errInDestroy = 3;

class MyDestroyableResource(Integer err) satisfies Destroyable {
    shared variable Integer state = stateUninit;

    if (err == errInInit) {
        sharedState=0;
        throw ResourceException("init resource");
    }

    state += statePostInit;
    
    shared actual void destroy(Throwable? exception) {
        state += statePreDestroy;
        if (err == errInDestroy) {
            sharedState = state;
            throw ResourceException("destroy resource");
        }
        state += statePostDestroy;
        sharedState = state;
    }
}

class MyObtainableResource(Integer err) satisfies Obtainable {
    shared variable Integer state = stateUninit;
    
    if (err == errInInit) {
        sharedState=0;
        throw ResourceException("init resource");
    }
    
    state += statePostInit;
    
    shared actual void obtain() {
        state += statePreObtain;
        if (err == errInObtain) {
            sharedState = state;
            throw ResourceException("obtain resource");
        }
        state += statePostObtain;
        sharedState = state;
    }
    
    shared actual void release(Throwable? exception) {
        state += statePreRelease;
        if (err == errInRelease) {
            sharedState = state;
            throw ResourceException("release resource");
        }
        state += statePostRelease;
        sharedState = state;
    }
}

@test
shared void exceptions() {
    variable Boolean caught=false;
    try {
        throw MyException();
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (OtherError oe) {
        fail("other error");
    }
    catch (MyError oe) {
        fail("my error");
    }
    catch (MyException me) {
        caught=true;
        check(me.message=="my exception", "exception message");
        check(!me.cause exists, "exception cause");
    }
    check(caught, "caught");
    
    caught=false;
    try {
        throw MyError();
    }
    catch (MyException oe) {
        fail("my exception");
    }
    catch (OtherException oe) {
        fail("other exception");
    }
    catch (OtherError oe) {
        fail("other error");
    }
    catch (MyError me) {
        caught=true;
        check(me.message=="my error", "exception message");
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
    catch (Error oe) {
        fail("error");
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
    catch (OtherError|MyError e) {
        fail("error");
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
    catch (OtherError|MyError e) {
        fail("any error");
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
    // destroyable resources
    try (r=MyDestroyableResource(errNone)) {
        pass++;
    } catch (Exception e) {
        fail("try-with-destroyable-resource 1 unexpected exception");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-destroyable-resource 1 pass check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy + statePostDestroy, 
        "try-with-destroyable-resource 1 resource state check");

    pass = 0;
    try (r=MyDestroyableResource(errNone)) {
        pass++;
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-destroyable-resource 2 exception message");
        check(suppressedExceptions(e).empty, "try-with-destroyable-resource 2 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-destroyable-resource 2 final check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy + statePostDestroy, 
        "try-with-destroyable-resource 2 resource state check");
    
    pass = 0;
    try (r=MyDestroyableResource(errInInit)) {
        fail("try-with-destroyable-resource 3 unexpected try-block execution");
    } catch (Exception e) {
        pass++;
        check(e.message=="init resource", "try-with-destroyable-resource 3 exception message");
        check(suppressedExceptions(e).empty, "try-with-destroyable-resource 3 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-destroyable-resource 3 pass check");
    check(sharedState==stateUninit, 
        "try-with-destroyable-resource 3 resource state check");
    
    pass = 0;
    try (r=MyDestroyableResource(errInDestroy)) {
        pass++;
    } catch (Exception e) {
        pass++;
        check(e.message=="destroy resource", "try-with-destroyable-resource 5 exception message");
        check(suppressedExceptions(e).empty, "try-with-destroyable-resource 5 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-destroyable-resource 5 pass check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy,
        "try-with-destroyable-resource 5 resource state check");
    
    pass = 0;
    try (r=MyDestroyableResource(errInDestroy)) {
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-destroyable-resource 6 exception message");
        value sups = suppressedExceptions(e);
        if (nonempty sups) {
            check(sups.size==1, "try-with-destroyable-resource 6 wrong suppressed exceptions count");
            check(sups.first.message=="destroy resource", "try-with-destroyable-resource 6 wrong suppressed exception message");
        } else {
            fail("try-with-destroyable-resource 6 missing suppressed exceptions");
        }
    } finally {
        pass++;
    }
    check(pass==2, "try-with-destroyable-resource 6 pass check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy,
        "try-with-destroyable-resource 6 resource state check");

    pass = 0;
    try (r1=MyDestroyableResource(errNone), r2=MyDestroyableResource(errInDestroy)) {
        pass++;//fail("try-with-destroyable-resources 7 unexpected try-block execution");
    } catch (ResourceException e) {
        pass++;
        check(e.message=="destroy resource", "try-with-destroyable-resource 7 exception message");
        check(suppressedExceptions(e).empty, "try-with-destroyable-resource 7 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-destroyable-resource 7 pass check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy + statePostDestroy, 
        "try-with-destroyable-resource 7 resource state check");
    
    pass = 0;
    try (r1=MyDestroyableResource(errInDestroy), r2=MyDestroyableResource(errInDestroy)) {
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-destroyable-resource 8 exception message");
        value sups = suppressedExceptions(e);
        if (nonempty sups) {
            check(sups.size==2, "try-with-destroyable-resource 8 wrong suppressed exceptions count");
            check(sups.first.message=="destroy resource", "try-with-destroyable-resource 8 wrong suppressed exception message");
            if (nonempty r=sups.rest) {
                check(r.first.message=="destroy resource", "try-with-destroyable-resource 8 wrong suppressed exception message");
            } else {
                fail("try-with-destroyable-resource 8 this should never happen");
            }
        } else {
            fail("try-with-destroyable-resource 8 missing suppressed exceptions");
        }
    } finally {
        pass++;
    }
    check(pass==2, "try-with-destroyable-resource 8 pass check");
    check(sharedState==stateUninit + statePostInit + statePreDestroy, 
        "try-with-destroyable-resource 8 resource state check = ``sharedState``");
    
    // obtainable resources
    MyObtainableResource obtainable(Integer i) => MyObtainableResource(i);
    pass = 0;
    try (r=obtainable(errNone)) {
        pass++;
    } catch (Exception e) {
        fail("try-with-obtainable-resource 1 unexpected exception");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-obtainable-resource 1 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease + statePostRelease,
        "try-with-obtainable-resource 1 resource state check");
    
    pass = 0;
    try (r=obtainable(errNone)) {
        pass++;
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-obtainable-resource 2 exception message");
        check(suppressedExceptions(e).empty, "try-with-obtainable-resource 2 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-obtainable-resource 2 final check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease + statePostRelease,
        "try-with-obtainable-resource 2 resource state check");
    
    pass = 0;
    try (r=obtainable(errInInit)) {
        fail("try-with-obtainable-resource 3 unexpected try-block execution");
    } catch (Exception e) {
        pass++;
        check(e.message=="init resource", "try-with-obtainable-resource 3 exception message");
        check(suppressedExceptions(e).empty, "try-with-obtainable-resource 3 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-obtainable-resource 3 pass check");
    
    pass = 0;
    try (r=obtainable(errInObtain)) {
        fail("try-with-obtainable-resource 4 unexpected try-block execution");
    } catch (Exception e) {
        pass++;
        check(e.message=="obtain resource", "try-with-obtainable-resource 4 exception message");
        check(suppressedExceptions(e).empty, "try-with-obtainable-resource 4 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-obtainable-resource 4 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain,
         "try-with-obtainable-resource 4 resource state check");
    
    pass = 0;
    try (r=obtainable(errInRelease)) {
        pass++;
    } catch (Exception e) {
        pass++;
        check(e.message=="release resource", "try-with-obtainable-resource 5 exception message");
        check(suppressedExceptions(e).empty, "try-with-obtainable-resource 5 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==3, "try-with-obtainable-resource 5 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease, 
        "try-with-obtainable-resource 5 resource state check");
    
    pass = 0;
    try (r=obtainable(errInRelease)) {
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-obtainable-resource 6 exception message");
        value sups = suppressedExceptions(e);
        if (nonempty sups) {
            check(sups.size==1, "try-with-obtainable-resource 6 wrong suppressed exceptions count");
            check(sups.first.message=="release resource", "try-with-obtainable-resource 6 wrong suppressed exception message");
        } else {
            fail("try-with-obtainable-resource 6 missing suppressed exceptions");
        }
    } finally {
        pass++;
    }
    check(pass==2, "try-with-obtainable-resource 6 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease,
         "try-with-obtainable-resource 6 resource state check");
    
    pass = 0;
    try (r1=obtainable(errNone), r2=obtainable(errInObtain)) {
        fail("try-with-obtainable-resources 7 unexpected try-block execution");
    } catch (ResourceException e) {
        pass++;
        check(e.message=="obtain resource", "try-with-obtainable-resource 7 exception message");
        check(suppressedExceptions(e).empty, "try-with-obtainable-resource 7 unexpected suppressed exceptions");
    } finally {
        pass++;
    }
    check(pass==2, "try-with-obtainable-resource 7 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease + statePostRelease,
        "try-with-obtainable-resource 7 resource state check");
    
    pass = 0;
    try (r1=obtainable(errInRelease), r2=obtainable(errInRelease)) {
        throw MyException();
    } catch (Exception e) {
        pass++;
        check(e.message=="my exception", "try-with-obtainable-resource 8 exception message");
        value sups = suppressedExceptions(e);
        if (nonempty sups) {
            check(sups.size==2, "try-with-obtainable-resource 8 wrong suppressed exceptions count");
            check(sups.first.message=="release resource", "try-with-obtainable-resource 8 wrong suppressed exception message");
            if (nonempty r=sups.rest) {
                check(r.first.message=="release resource", "try-with-obtainable-resource 8 wrong suppressed exception message");
            } else {
                fail("try-with-obtainable-resource 8 this should never happen");
            }
        } else {
            fail("try-with-obtainable-resource 8 missing suppressed exceptions");
        }
    } finally {
        pass++;
    }
    check(pass==2, "try-with-resource 8 pass check");
    check(sharedState==stateUninit + statePostInit + statePreObtain + statePostObtain + statePreRelease,
         "try-with-resource 8 resource state check = ``sharedState``");
}
