/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
// extends
class ExceptionTypesException(String? description=null, Throwable? cause=null) 
        extends Exception(description, cause) {
    
}

class ExceptionTypesError(String? description=null, Throwable? cause=null) 
        extends Error(description, cause) {
    
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
void exceptionTypeAssignment() {
    JavaThrower jt = JavaThrower();
    variable Throwable t;
    t = jt.giveThrowable;
    t = jt.giveError;
    t = jt.giveOome;
    t = jt.giveException;
    t = jt.giveIoException;
    t = jt.giveRuntimeException;
    t = jt.giveNpe;
    
    variable Error er;
    er = jt.giveError;
    er = jt.giveOome;
    
    variable Exception ex;
    ex = jt.giveException;
    ex = jt.giveIoException;
    ex = jt.giveRuntimeException;
    ex = jt.giveNpe;
    
    jt.takeThrowable(Error());
    jt.takeThrowable(ExceptionTypesError());
    jt.takeThrowable(Exception());
    jt.takeThrowable(ExceptionTypesException());
    
    jt.takeError(Error());
    jt.takeError(ExceptionTypesError());
    
    jt.takeException(Exception());
    jt.takeException(ExceptionTypesException());
}