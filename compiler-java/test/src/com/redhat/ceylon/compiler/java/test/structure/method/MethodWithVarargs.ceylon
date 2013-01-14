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
class MethodWithVarargs() {
    shared void f1(Integer* ns) {
        for (Integer n in ns) { }
    }
    shared void f2(Integer i, Integer* ns) {
        for (Integer n in ns) { }
    }
    shared void f3(Object* os) {
        for (Object o in os) { }
    }
    void m() {
        f1(1, 2, 3);
        f2(1, 2, 3);
        f1();
        f2(1);
        f1(*[1, 2, 3]);
        f2(1, *[2, 3]);
        f3(1, 2, 3);
        f3({1, 2, 3});
        f3(*[1, 2, 3]);
        f3(1, "foo", 5.0);
    }
}