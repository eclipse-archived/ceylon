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
@noanno
class Test1 {
    String name;
    shared new ()  {
        // not captured
        name = "a";
        print(name);
    }
}

@noanno
class Test2 {
    String name = "a";
    shared new ()  {
        // not captured
        print(name);
    }
}

@noanno
class Test3 {
    String name;
    name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    shared new b ()  {
        // not captured
        print(name);
    }
}

@noanno
class Test4 {
    String name;
    name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    shared new b ()  {
        // not captured
        print(name);
    }
    // not captured
    print(name);
}

/*
 All statements before a constructor (those after its delegate if any) are copied into it.
 If a constructor is used as a delegate, all statements after it are split into another constructor (and should capture)
 If not, all statements after it are copied into it.
 We should treat as captured only those which are captured in two different chained constructors
*/

@noanno
class TestA {
    String name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    // not captured
    print(name);
}

@noanno
class TestB1 {
    String name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    // captures it
    print(name);
    shared new b() extends a(){
    }
}

@noanno
class TestB2 {
    String name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    shared new b() extends a(){
        // captures it
        print(name);
    }
}

@noanno
class TestB3 {
    String name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    shared new b() extends a(){
    }
    // captures it
    print(name);
}

@noanno
class TestB4 {
    String name = "a";
    shared new a ()  {
        // not captured
        print(name);
    }
    shared new b() extends a(){
    }
    shared new c() extends b(){
    }
    // captures it
    print(name);
}

@noanno
class TestC {
    String name;
    shared new a ()  {
        // not captured here but its declaration is part of this delegate
    }
    shared new b() extends a(){
        // captures it
        name = "b";
        print(name);
    }
}

@noanno
class TestC2 {
    shared new a ()  {
        // does not capture it because it's after
    }
    String name;
    shared new b() extends a(){
        // remains a local
        name = "b";
        print(name);
    }
}

@noanno
class Super {
    // both captured
    String name;
    String name2;
    shared new ()  {
        // nothing initialised
    }
    shared new constr() {
        // partial initialisation
        name = "Gavin";
    }
    shared new default() 
            extends constr() {
        // partial init
        name2 = "";
        // capturing zone
        print(name);
        print(name2);
    }
}

@noanno
class Super2 {
    // captured
    String name;
    // local
    String name2;
    shared new ()  {
        // nothing initialised
    }
    shared new constr() {
        // partial initialisation
        name = "Gavin";
    }
    shared new default() 
            extends constr() {
        // partial init
        name2 = "";
        // capturing zone
        print(name);
    }
}

@noanno
class Super3 {
    // both locals
    String name;
    String name2;
    shared new ()  {
        // nothing initialised
    }
    shared new constr() {
        // partial initialisation
        name = "Gavin";
    }
    shared new default() 
            extends constr() {
    }
}

@noanno
class Super4 {
    // local
    String name;
    shared new ()  {
        name = "Foo";
    }
    shared new constr() {
        name = "Gavin";
    }
    shared new default() 
            extends constr() {
    }
}
