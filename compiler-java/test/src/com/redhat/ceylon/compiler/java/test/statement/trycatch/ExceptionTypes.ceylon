// extends
class ExceptionTypesException(String? description=null, Throwable? cause=null) extends Exception(description, cause) {
    
}

class ExceptionTypesError(String? description=null, Throwable? cause=null) extends Error(description, cause) {
    
}
void exceptionTypesUsage(Integer i, Throwable f()) {
    // instantiation/throw
    switch(i)
    case (0) {
        throw f();
    }
    case (1) {
        throw Exception();
    }
    case (2) {
        throw Error();
    }
    case (3) {
        throw ExceptionTypesException();
    }
    case (4) {
        throw ExceptionTypesError();
    }
    else {}
    
    // catch
    switch(i)
    case (10) {
        try {
            f();
        } catch (Throwable t) {
            
        }
    }
    case (11) {
        try {
            f();
        } catch (Exception t) {
            
        }
    }
    case (12) {
        try {
            f();
        } catch (Error t) {
            
        }
    }
    case (13) {
        try {
            f();
        } catch (ExceptionTypesException t) {
            
        }
    }
    case (14) {
        try {
            f();
        } catch (ExceptionTypesError t) {
            
        }
    }
    else {
    }
    // Because Error is erased to j.l.Error we need to check we're accessing its type descriptor OK
    value errors = [Error(), Error()];
    value exceptions = [Exception(), Exception()];
    value errorsAndExceptions = [Error(), Exception()];
    value subclasses = [ExceptionTypesException(), ExceptionTypesError()];
}