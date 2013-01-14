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
class InnerMethodWithDefaultedParams(Integer init) {
    shared Integer prop = 42;
    Integer privProp = 24;
    Integer init = init;
    shared void test(Integer param) {
        @error
        void f1(Integer n = 5) {}
        @error
        void f2(Integer n, String s = "test") {}
        @error
        void f3(Integer n = 5, Integer m = n) {}
        @error
        void f4(Integer n = 5, Integer m = n + 1) {}
        @error
        void f5(Integer n = prop) {}
        @error
        void f6(Integer n = privProp) {}
        @error
        void f7(Integer n = this.prop) {}
        @error
        void f8(Integer n = init) {}
        @error
        void f9(Integer n = prop.successor) {}
        @error
        void fa(Integer n = 5, Integer* seq) {}
        
        
    }
}