/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.test.statement.trycatch;

import java.io.IOException;

public class JavaThrower {

    JavaThrower() {
        
    }
    
    public boolean throwException() throws Exception {
        throw new Exception();
    }
    
    public boolean throwThrowable() throws Throwable {
        throw new Throwable();
    }
    
    public boolean throwRuntimeException() throws RuntimeException {
        throw new RuntimeException();
    }
    
    public boolean throwError() throws Error {
        throw new Error();
    }
    
    public boolean throwsMultiple() throws ClassNotFoundException, IOException {
        return false;
    }
    
}
