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
interface Intersect {}
class ExceptionIntersection() extends Exception() satisfies Intersect{}
class ExceptionSub() extends Exception() {}
class ExceptionSubIntersection() extends ExceptionSub() satisfies Intersect{}
class ExceptionGeneric<T>() extends Exception() {}
class ExceptionGenericSub() extends ExceptionGeneric<Integer>() {}
class TryCatchGenericIntersection() {
    shared void catchIntersection(Exception exception) {
        try {
            throw exception;
        } catch (Exception&Intersect e) {
            print("Caught Exception&Intersect");
        }
    }
    shared void catchExceptionSubIntersection(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionSub&Intersect e) {
            print("Caught ExceptionSub&Intersect");
        }
    }
    shared void catchExceptionGenericString(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionGeneric<String> e) {
            print("Caught ExceptionGeneric<String>");
        }
    }
    shared void catchExceptionGenericStringOrInteger(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionGeneric<String> e) {
            print("Caught ExceptionGeneric<String>");
        } catch (ExceptionGeneric<Integer> e) {
            print("Caught ExceptionGeneric<Integer>");
        }
    }
    shared void catchExceptionGenericStringOrInteger2(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionGeneric<String>|ExceptionGeneric<Integer> e) {
            print("Caught ExceptionGeneric<String>|ExceptionGeneric<Integer>");
        }
    }
    shared void catchExceptionGenericStringOrIntegerOrExceptionSub(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionGeneric<String> e) {
            print("Caught ExceptionGeneric<String>");
        } catch (ExceptionGeneric<Integer> e) {
            print("Caught ExceptionGeneric<Integer>");
        } catch (ExceptionSub e) {
            print("Caught ExceptionSub");
        }
    }
    shared void catchExceptionGenericStringOrIntegerOrExceptionSubIntersect(Exception exception) {
        try {
            throw exception;
        } catch (ExceptionGeneric<String> e) {
            print("Caught ExceptionGeneric<String>");
        } catch (ExceptionGeneric<Integer> e) {
            print("Caught ExceptionGeneric<Integer>");
        } catch (ExceptionSub&Intersect e) {
            print("Caught ExceptionSub&Intersect");
        }
    }
    
}
void tryCatchGenericIntersection() {
    value t = TryCatchGenericIntersection();
    // catchIntersection
    t.catchIntersection(ExceptionIntersection());
    try {
        t.catchIntersection(Exception());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { }

    // catchExceptionSubIntersection
    t.catchExceptionSubIntersection(ExceptionSubIntersection());
    try {
        t.catchExceptionSubIntersection(ExceptionSub());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionSub e); }
    try {
        t.catchExceptionSubIntersection(ExceptionIntersection());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionIntersection e); }
    
    // catchExceptionGenericString
    t.catchExceptionGenericString(ExceptionGeneric<String>());
    try {
        t.catchExceptionGenericString(ExceptionGeneric<Integer>());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionGeneric<Integer> e); }
    try {
        t.catchExceptionGenericString(Exception());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { }
    
    // catchExceptionGenericStringOrInteger
    t.catchExceptionGenericStringOrInteger(ExceptionGeneric<String>());
    t.catchExceptionGenericStringOrInteger(ExceptionGeneric<Integer>());
    try {
        t.catchExceptionGenericStringOrInteger(ExceptionGeneric<Boolean>());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionGeneric<Boolean> e); }
    
    // catchExceptionGenericStringOrInteger2
    t.catchExceptionGenericStringOrInteger2(ExceptionGeneric<String>());
    t.catchExceptionGenericStringOrInteger2(ExceptionGeneric<Integer>());
    try {
        t.catchExceptionGenericStringOrInteger2(ExceptionGeneric<Boolean>());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionGeneric<Boolean> e); }
    
    // catchExceptionGenericStringOrIntegerOrExceptionSub
    t.catchExceptionGenericStringOrIntegerOrExceptionSub(ExceptionGeneric<String>());
    t.catchExceptionGenericStringOrIntegerOrExceptionSub(ExceptionGeneric<Integer>());
    t.catchExceptionGenericStringOrIntegerOrExceptionSub(ExceptionSub());
    try {
        t.catchExceptionGenericStringOrInteger2(ExceptionGeneric<Boolean>());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionGeneric<Boolean> e); }
    try {
        t.catchExceptionGenericStringOrInteger2(Exception());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { }
    
    // catchExceptionGenericStringOrIntegerOrExceptionSubIntersect
    t.catchExceptionGenericStringOrIntegerOrExceptionSubIntersect(ExceptionGeneric<String>());
    t.catchExceptionGenericStringOrIntegerOrExceptionSubIntersect(ExceptionGeneric<Integer>());
    t.catchExceptionGenericStringOrIntegerOrExceptionSubIntersect(ExceptionSubIntersection());
    try {
        t.catchExceptionGenericStringOrInteger2(ExceptionGeneric<Boolean>());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionGeneric<Boolean> e); }
    try {
        t.catchExceptionGenericStringOrInteger2(ExceptionSub());
        assert(false);
    }
    catch (AssertionException e) { throw e;} 
    catch (Exception e) { assert(is ExceptionSub e); }
}