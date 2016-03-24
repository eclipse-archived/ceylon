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
class Bug1381_Superclass() {
    variable Integer a = 0;
    Integer m(Integer p) => p;
    class C(Integer p) {}
    shared void callsite(Bug1381_Subclass bar) {
        Integer a1 = bar.a;
        bar.a = a1;
        bar.a++;
        bar.a+=1;
        Integer(Bug1381_Subclass) statica = Bug1381_Subclass.a;
        
        Integer m1 = bar.m(1);
        Integer m2 = bar.m{
            p=1;
        };
        Integer(Integer) refm = bar.m;
        Integer(Integer)(Bug1381_Subclass) staticm = Bug1381_Subclass.m;
        
        C c1 = bar.C(1);
        C c2 = bar.C{
            p=1;
        };
        C(Integer) refC = bar.C;
        C(Integer)(Bug1381_Subclass) staticC = Bug1381_Subclass.C;
    }
}
@noanno
class Bug1381_Subclass() extends Bug1381_Superclass() {
}
@noanno
interface Bug1381_Superinterface {
    Integer a => 0;
    assign a {}
    Integer m(Integer p) => p;
    class C(Integer p) {}
    shared void callsite(Bug1381_Subinterface bar) {
        Integer a1 = bar.a;
        bar.a = a1;
        bar.a++;
        bar.a+=1;
        Integer(Bug1381_Subinterface) statica = Bug1381_Subinterface.a;
        
        Integer m1 = bar.m(1);
        Integer m2 = bar.m{
            p=1;
        };
        Integer(Integer) refm = bar.m;
        Integer(Integer)(Bug1381_Subinterface) staticm = Bug1381_Subinterface.m;
        
        C c1 = bar.C(1);
        C c2 = bar.C{
            p=1;
        };
        C(Integer) refC = bar.C;
        C(Integer)(Bug1381_Subinterface) staticC = Bug1381_Subinterface.C;
    }
}
@noanno
interface Bug1381_Subinterface satisfies Bug1381_Superinterface {
}