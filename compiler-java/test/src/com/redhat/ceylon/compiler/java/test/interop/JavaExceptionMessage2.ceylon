import java.io{ IOException }

void javaExceptionMessage2() {
    /* we're basically trying to show that for every possible primary of .message
       we're obtaining the message via Util.throwableMessage to get a non-null result.
     outer.message
     this.message
     super.message
     e.message DONE
     e*.message DONE
     e?.message DONE
     E.message DONE
     E().message DONE
     f().message DONE
     l[i].message DONE
     ().message
     ((Throwable t) => t).message
    */
    value factory = JavaExceptionsAndThrowable();
    try {
        factory.throwRuntimeException(null);
    } catch (Throwable t) {
        assert(t.message == "");
    }
    
    value ref = Exception.message;
    assert("" == ref(factory.newException(null)));
    
    assert("" == [factory.newException(null)]*.message[0]);
    
    Throwable? t = factory.newException(null);
    assert("" == (t?.message else "X"));
    
    assert("" == factory.newException(null).message);
    
    assert("" == [factory.newException(null)][0].message);
    
    assert("" == (() => factory.newException(null))().message);
}