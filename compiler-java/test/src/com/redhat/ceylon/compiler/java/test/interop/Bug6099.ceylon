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
import com.redhat.ceylon.compiler.java.test.interop { 
    Bug6099Java { 
        InnerInterface,
        \IinnerInterface2,
        STATIC_CONSTANT_ALT,
        ...
    }
}

@noanno
see(`function process.writeLine`) // regression for Ceylon anonymous types
shared void bug6099() {
    process.writeLine("foo");
    value java = Bug6099Java();
    
    print(java.method());
    print(java.instanceField);
    print(java.staticConstant);
    print(java.staticConstantAlt);
    print(java.\iSTATIC_CONSTANT_ALT);
    print(\iSTATIC_CONSTANT_ALT);
    print(Bug6099Java.staticConstantAlt);
    print(Bug6099Java.\iSTATIC_CONSTANT_ALT);
    print(java.InnerClass());
    print(java.\IinnerClass());
    Bug6099Java.InnerClass xna = nothing;
    Bug6099Java.\IinnerClass xnb = nothing;
    InnerInterface xa = nothing;
    \IinnerInterface xb = nothing;
    InnerInterface2 x2a = nothing;
    \IinnerInterface2 x2b = nothing;
    InnerInterface3 x3a = nothing;
    \IinnerInterface3 x3b = nothing;
    Bug6099JavaTopLevelClass ya = Bug6099JavaTopLevelClass(); 
    \Ibug6099JavaTopLevelClass yb = \Ibug6099JavaTopLevelClass(); 
    Bug6099JavaTopLevelInterface za = nothing; 
    \Ibug6099JavaTopLevelInterface zb = nothing;
    package.Bug6099JavaTopLevelInterface za2 = nothing; 
    package.\Ibug6099JavaTopLevelInterface zb2 = nothing;
    
    // regression for ceylon constants TypeDescriptors
    value b = [true]; 
}

object foo {
    shared void bar(){
        foo.bar();
    }
}
