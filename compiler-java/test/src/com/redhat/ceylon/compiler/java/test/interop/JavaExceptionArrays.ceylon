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
import java.io{ IOException }
import java.lang{
    //JException=Exception,
    //JError = Error,
    JAssertionError = AssertionError
}

void javaExceptionArrays() {
    value factory = JavaExceptionsAndThrowable();
    value cex = Exception("");
    value jex = factory.newException("");
    value ioex = IOException("");
    value cerr = Error("");
    value jerr = factory.newError("");
    value aerr = JAssertionError("");
    
    
    Array<Throwable> throwables = arrayOfSize<Throwable>(1, cex);
    throwables.set(0, jex);
    assert(exists t1=throwables[0], t1 == jex);
    throwables.set(0, ioex);
    assert(exists t2=throwables[0], t2 == ioex);
    throwables.set(0, cerr);
    assert(exists t3=throwables[0], t3 == cerr);
    throwables.set(0, jerr);
    assert(exists t4=throwables[0], t4 == jerr);
    throwables.set(0, aerr);
    assert(exists t5=throwables[0], t5 == aerr);
    
    Array<Exception> cexceptions = arrayOfSize<Exception>(1, cex);
    cexceptions.set(0, jex);
    assert(exists t11=cexceptions[0], t11 == jex);
    cexceptions.set(0, ioex);
    assert(exists t12=cexceptions[0], t12 == ioex);
    
    Array<Exception> jexceptions = arrayOfSize<Exception>(1, jex);
    
    Array<Error> errors = arrayOfSize<Error>(1, cerr);
    errors.set(0, cerr);
    assert(exists t23=errors[0], t23 == cerr);
    errors.set(0, jerr);
    assert(exists t24=errors[0], t24 == jerr);
    errors.set(0, aerr);
    assert(exists t25=errors[0], t25 == aerr);
    
    Array<Error> jerrors = arrayOfSize<Error>(1, jerr);
    
}