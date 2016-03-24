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
package com.redhat.ceylon.compiler.java.test.statement.trycatch;

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
    
    public java.lang.Throwable getGiveThrowable() {
        return new java.lang.Exception();
    }
    
    public void takeThrowable(java.lang.Throwable e) {
    }
    
    public java.lang.Error getGiveError() {
        return new java.lang.Error();
    }
    
    public void takeError(java.lang.Error e) {
    }
    
    public java.lang.OutOfMemoryError getGiveOome() {
        return new java.lang.OutOfMemoryError();
    }
    
    public void takeOome(java.lang.OutOfMemoryError e) {
    }
    
    public java.lang.Exception getGiveException() {
        return new java.lang.Exception();
    }
    
    public void takeException(java.lang.Exception e) {
    }
    
    public java.io.IOException getGiveIoException() {
        return new java.io.IOException();
    }
    
    public void takeIoException(java.io.IOException e) {
    }
    
    public java.lang.RuntimeException getGiveRuntimeException() {
        return new java.lang.RuntimeException();
    }
    
    public void takeRuntimeException(java.lang.RuntimeException e) {
    }
    
    public java.lang.NullPointerException getGiveNpe() {
        return new java.lang.NullPointerException();
    }
    
    public void takeNpe(java.lang.NullPointerException e) {
    }
    
    
    
}
