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
class Bug2234Sample {
    shared new foo {}
}

@noanno
class Bug2234() {
    shared class Inner {
        shared String name;
        shared new foo {
            name = "foo";
        }
        shared new() {
            name = "Inner";
        }
    }
    shared Inner member = Inner();
    shared void test() {
        bug2234_check(Inner.foo.name=="foo", "#1129.4");
    }
}

@noanno
void bug2234_check(Boolean b, String s){}

@noanno
void bug2234(){
    value o = Bug2234();
    bug2234_check(o.Inner.foo.name=="foo", "spec#1129.1");
    bug2234_check(Bug2234().Inner.foo.name=="foo", "spec#1129.2");
    value oi=o.Inner();
    bug2234_check(oi.foo.name=="foo", "spec#1129.3");
    o.test();
    value ref1=Bug2234.Inner.foo;
    value ref2=Bug2234.member;
    bug2234_check(ref1(o).name=="foo", "spec#1129.5");
    //check(Simple1129.foo.name=="Foo!", "spec#1129.6");
    //check(Simple1129().foo.name=="Foo!", "spec#1129.7");
    //check(Delegating1129.foo.name=="foo", "spec#1129.8");
}