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
    Anything o2 = o1;
    Anything o3 = o1;
    Anything o4 = o1;
    object i1 extends Object() satisfies Identifiable {
        shared actual Boolean equals(Object o) {
            return false;
        }
        shared actual Integer hash = 0;
    }
    Anything i = i1;
    Anything i2 = i1;
    Anything i3 = i1;
    Anything i4 = i1;
    object io1 extends Basic() {}
    Anything io = io1;
    Anything io2 = io1;
    Anything io3 = io1;
    Anything io4 = io1;
    Anything nowt = null;
    Anything nowt2 = null;
    Anything nowt3 = null;
    Anything nowt4 = null;
    
    Throwable throwable = AssertionError("");
    Throwable exception = Exception();
    Throwable myThrowable {
        object e extends AssertionError(""){}
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
        if (is Object nowt2) {
            throw;
        }
        if (is Identifiable nowt3) {
            throw;
        }
        if (is Basic nowt4) {
            throw;
        }
        
        if (is Null o) {
            throw;
        }
        if (is Object o2) {
        
        } else {
            throw;
        }
        if (is Identifiable o3) {
            throw;
        }
        if (is Basic o4) {
            throw;
        }
        
        if (is Null i) {
            throw;
        }
        if (is Object i2) {
        
        } else {
            throw;
        }
        if (is Identifiable i3) {
        
        } else {
            throw;
        }
        if (is Basic i4) {
            throw;
        }
        
        if (is Null io) {
            throw;
        }
        if (is Object io2) {
        
        } else {
            throw;
        }
        if (is Identifiable io3) {
        
        } else {
            throw;
        }
        if (is Basic io4) {
            
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
        
        if (is Exception throwable) {
            throw;
        }
        if (is Exception x=myThrowable) {
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
        
        b = throwable is Exception;
        if (b) {
            throw;
        }
        b = myThrowable is Exception;
        if (b) {
            throw;
        }
    }
}