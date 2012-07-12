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
see(StubInterface, stubTopLevelAttribute, stubTopLevelMethod)
tagged("stubTag1", "stubTag2")
shared class StubClass(
  doc "Constructor parameter `a`" Integer a,
  doc "Constructor parameter `b`" Integer b) 
    satisfies StubInterface {

    doc "The stub attribute with `throws`."
    throws (OverflowException, "if the number is too large to be represented as an integer")
    shared Integer attributeWithThrows = 0;
    
    doc "The stub attribute with `see`."
    see (methodWithSee, StubException)
    shared Integer attributeWithSee = 0;
    
    doc "The stub attribute with `tagged`."
    tagged("stubTag1")
    shared Integer attributeWithTagged = 0;
    
    doc "The stub method with parameters documentation."
    shared void methodWithParametersDocumentation(
        doc "Method parameter `a`" Integer a, 
        doc "Method parameter `b`" Integer b) {}
    
    doc "The stub method with `throws`."
    throws (StubException, "`when` with __WIKI__ syntax")
    shared void methodWithThrows() {}
    
    doc "The stub method with `see`."
    see (attributeWithSee, StubException)
    shared void methodWithSee() {}
    
    doc "The stub method with `tagged` and long description <i>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</i>"
    tagged("stubTag2")
    shared void methodWithTagged() {}
    
    shared actual void formalMethodFromStubInterface() {}
    
    shared actual void defaultDeprecatedMethodFromStubInterface() {}
    
    doc "This is `StubInnerInterface`"
    tagged("stubInnerTag1")
    shared interface StubInnerInterface {

        tagged("stubInnerMethodTag1")
        shared formal void innerMethod();
       
    }

    shared class StubInnerClass() satisfies StubInnerInterface {

        shared actual void innerMethod() {}

    }
    
    shared class StubInnerException() extends StubException() {

    }
    
}