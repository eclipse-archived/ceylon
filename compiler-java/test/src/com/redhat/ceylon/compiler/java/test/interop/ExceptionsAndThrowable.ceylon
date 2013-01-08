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
import java.lang { Error, JException = Exception, RuntimeException }

@nomodel
shared class MyException() extends Exception(){}

@nomodel
shared void testExceptions(Exception param){
    Exception x = MyException();
    Object o = x;
    if(is Exception o){
    }
    try{
    }catch(Exception z){
    }
}

@nomodel
shared void testThrowable(JavaExceptionsAndThrowable j){
    Exception t = j.throwable();
    if(2 > 1){
        throw Exception("error", j.throwable());
    }
    if(is MyException t){
    }
    if(is Error t){
    }
    if(is RuntimeException t){
    }
    if(is JException t){
    }
}

@nomodel
shared class ExceptionsAndThrowableRefinement() extends JavaExceptionsAndThrowable() {
    shared actual Exception t(Exception? x){ if(exists x){ return x; } else { return nothing;} }
    shared actual Error e(Error? x){ if(exists x){ return x; } else { return nothing;} }
    shared actual JException x(JException? x){ if(exists x){ return x; } else { return nothing;} }
    shared actual RuntimeException rtx(RuntimeException? x){ if(exists x){ return x; } else { return nothing;} }
}