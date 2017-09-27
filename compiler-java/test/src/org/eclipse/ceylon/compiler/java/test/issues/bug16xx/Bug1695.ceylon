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
class Bug1695T() {
    shared default Bug1695T[] c = [];
}

@noanno
class Bug1695A() extends Bug1695T() {}

@noanno
class Bug1695B(shared actual [<Bug1695A|Bug1695B>+] c) extends Bug1695T() {
    void f([<Bug1695A|Bug1695B>+] c1 = c) {
        [<Bug1695A|Bug1695B>+] c2 = c;
        [<Bug1695A|Bug1695B>+] c3 = this.c;
    }
}

@noanno
void bug1695(){
    Bug1695B b1 = Bug1695B { c = [Bug1695A()]; };
    Bug1695B b2 = Bug1695B ( [Bug1695A()] );
}

@noanno
interface Bug1695It<Element,Absent> 
        given Absent satisfies Null {
    
    shared default Callable<Iterable<Result,Absent>,Args> 
        spread<Result,Args>(Callable<Result,Args> method(Element element))
        given Args satisfies Anything[]
        => nothing;
}

@noanno
class Bug1695Emp() satisfies Bug1695It<Nothing,Null>{
    shared actual Callable<[],Args> spread<Result,Args>(
        Callable<Result,Args> method(Nothing element))
            given Args satisfies Anything[] 
            => flatten((Args args) => []);
}