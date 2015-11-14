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
class OuterReferenceSuper() {
    shared Boolean superTest() {
        return true;
    }
}
@noanno
class OuterReference() extends OuterReferenceSuper() {
    class InnerClass(Boolean b = outer.superTest()) {
        Boolean m(Boolean b = outer.superTest()) {
            outer.superTest();
            return outer.test();
        }
    }
    interface InnerIface {
        Boolean m(Boolean b = outer.superTest()) {
            outer.superTest();
            return outer.test();
        }
    }
    void m() {
        class LocalClass(Boolean b = outer.superTest()) {
            outer.superTest();
            outer.test();
        }
        interface LocalIface {
            void m(Boolean b = outer.superTest()) {
                outer.superTest();
                outer.test();
            }
        }
    }
    shared Boolean test() {
        return true;
    }
}