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
doc "This is `StubClass`"
see(StubInterface)
tagged("stubTag1", "stubTag2")
shared class StubClass() satisfies StubInterface {

    doc "The stub attribute with `throws`."
    throws (OverflowException, "if the number is too large to be represented as an integer")
    shared Integer attributeWithThrows = 0;
    
    doc "The stub attribute with `see`."
    see (methodWithSee, StubException)
    shared Integer attributeWithSee = 0;
    
    doc "The stub attribute with `tagged`."
    tagged("stubTag1")
    shared Integer attributeWithTagged = 0;
    
    doc "The stub method with `throws`."
    throws (StubException, "`when` with __WIKI__ syntax")
    shared void methodWithThrows() {}
    
    doc "The stub method with `see`."
    see (attributeWithSee, StubException)
    shared void methodWithSee() {}
    
    doc "The stub method with `tagged`."
    tagged("stubTag2")
    shared void methodWithTagged() {}
    
    shared actual void formalMethod() {}
    
    shared actual void defaultMethod() {}
    
    shared interface StubInnerInterface {

        shared formal void innerMethod();
       
    }

    shared class StubInnerClass() satisfies StubInnerInterface {

        shared actual void innerMethod() {}

    }
    
}