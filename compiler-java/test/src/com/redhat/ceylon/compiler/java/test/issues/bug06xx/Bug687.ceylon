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
class Bug687()  {
    void method<T>(void foo(T t)) {
    }

    shared void sort<T>(T* elements) 
        given T satisfies Number<Integer> {
    }
    shared void sort3<T>(Iterable<T> elements) 
        given T satisfies Number<Integer> {
    }
    shared void sort2(Number<Integer>&Category* elements) { 
    }
    void foo(void p(Number<Integer>&Category* elements)){
        sort(*{});
        sort{elements = {};};
        sort2(*{});
        sort2{elements = {};};
        p(*{});
        p{elements = {};};
        
        value f = sort3<Nothing>;
        f({});
    }
}
