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
@nomodel
interface Common {
    shared formal Common common();
}

@nomodel 
interface FooInterface {
    shared formal FooInterface foo();
}

@nomodel
class Foo() satisfies Common & FooInterface {
    shared actual Common common() {return bottom;}
    shared actual Foo foo() {return bottom;}
}
@nomodel
class FooSub() extends Foo() {
    shared FooSub foo2() {return bottom;}
}

@nomodel
interface BarInterface {
    shared formal BarInterface bar();
}
@nomodel
class Bar() satisfies Common & BarInterface {
    shared actual Common common() {return bottom;}
    shared actual Bar bar() {return bottom;}
}
@nomodel
void isCond(Common?[] seq) {
    variable Foo[] foos = [ for (x in seq) if (is Foo x) x.foo() ];
    foos = [ for (x in seq) if (is FooSub x) x.foo() ];
    variable Common[] commons = [ for (x in seq) if (is Foo|Bar x) x.common() ];
    commons = [ for (x in seq) if (is Foo&BarInterface x) x.foo().common() ];
    variable BarInterface[] barIs = [ for (x in seq) if (is Foo&BarInterface x) x.bar() ];
    Nothing[] nowt = [ for (x in seq) if (is Nothing x) x ];
}
