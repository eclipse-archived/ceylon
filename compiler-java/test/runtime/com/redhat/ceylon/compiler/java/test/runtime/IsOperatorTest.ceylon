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
//import java.lang{JThrowable = Throwable, NullPointerException }
shared class IsOperatorTest() {
    object o1 extends Object() {
        shared actual Boolean equals(Object o) {
            return false;
        }
        shared actual Integer hash = 0;
    }
    Anything o = o1;
    object i1 extends Object() satisfies Identifiable {
        shared actual Boolean equals(Object o) {
            return false;
        }
        shared actual Integer hash = 0;
    }
    Anything i = i1;
    object io1 extends Basic() {}
    Anything io = io1;
    Anything nowt = null;
    
    Throwable error = Error();
    Throwable exception = Exception();
    Throwable myError {
        object e extends Error(){}
        return e;
    }
    Throwable myException {
        object e extends Exception() {}
        return e;
    }
    //Throwable npe = NullPointerException();
    //Throwable jthrowable = JThrowable();
    
    @test
    shared void ifIs() {
        if (is Null nowt) {
           
        } else {
            throw;
        }
        if (is Object nowt) {
            throw;
        }
        if (is Identifiable nowt) {
            throw;
        }
        if (is Basic nowt) {
            throw;
        }
        
        if (is Null o) {
            throw;
        }
        if (is Object o) {
        
        } else {
            throw;
        }
        if (is Identifiable o) {
            throw;
        }
        if (is Basic o) {
            throw;
        }
        
        if (is Null i) {
            throw;
        }
        if (is Object i) {
        
        } else {
            throw;
        }
        if (is Identifiable i) {
        
        } else {
            throw;
        }
        if (is Basic i) {
            throw;
        }
        
        if (is Null io) {
            throw;
        }
        if (is Object io) {
        
        } else {
            throw;
        }
        if (is Identifiable io) {
        
        } else {
            throw;
        }
        if (is Basic io) {
            
        } else {
            throw;
        }
        
        if (is Error error) {
            
        } else {
            throw;
        }
        if (is Error x=myError) {
            
        } else {
            throw;
        }
        if (is Exception exception) {
            
        } else {
            throw;
        }
        if (is Exception x=myException) {
            
        } else {
            throw;
        }
        
        if (is Error exception) {
            throw;
        }
        if (is Error x=myException) {
            throw;
        }
        
        if (is Exception error) {
            throw;
        }
        if (is Exception x=myError) {
            throw;
        }
    }
    
    @test
    shared void operatorIs() {
        variable Boolean b = false;
        b = nowt is Null;
        if (b) {
           
        } else {
            throw;
        }
        b= nowt is Object;
        if (b) {
            throw;
        }
        b = nowt is Identifiable;
        if (b) {
            throw;
        }
        b = nowt is Basic;
        if (b) {
            throw;
        }
        
        b= o is Null;
        if (b) {
            throw;
        }
        b = o is Object;
        if (b) {
        
        } else {
            throw;
        }
        b = o is Identifiable;
        if (b) {
            throw;
        }
        b = o is Basic;
        if (b) {
            throw;
        }
        
        b = i is Null;
        if (b) {
            throw;
        }
        b = i is Object;
        if (b) {
        
        } else {
            throw;
        }
        b = i is Identifiable;
        if (b) {
        
        } else {
            throw;
        }
        b = i is Basic;
        if (b) {
            throw;
        }
        
        b = io is Null;
        if (b) {
            throw;
        }
        b = io is Object;
        if (b) {
        
        } else {
            throw;
        }
        b = io is Identifiable;
        if (b) {
        
        } else {
            throw;
        }
        b = io is Basic;
        if (b) {
            
        } else {
            throw;
        }
        
        b =  error is Error;
        if (b) {
            
        } else {
            throw;
        }
        b = myError is Error;
        if (b) {
            
        } else {
            throw;
        }
        b = exception is Exception;
        if (b) {
            
        } else {
            throw;
        }
        b = myException is Exception;
        if (b) {
            
        } else {
            throw;
        }
        
        b = exception is Error;
        if (b) {
            throw;
        }
        b = myException is Error;
        if (b) {
            throw;
        }
        
        b = error is Exception;
        if (b) {
            throw;
        }
        b = myError is Exception;
        if (b) {
            throw;
        }
    }
 
 
    
}