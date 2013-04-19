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
T indirectTypeParam_topLevel<T>(T t) {return t;}

@noanno
void indirectTypeParam<X>() {
    value f1 = indirectTypeParam_topLevel<Integer>;
    variable Integer i = f1(1);

    T local<T>(T t) {return t;}
    
    value f2 = local<Integer>;
    i = f2(1);

    void mpl<T>(T t)() {
        throw;
    }
    
    function f3(Integer i)() => mpl<Integer>(i);
    f3(1)();
    
    value j = join<Object>;
    value j2 = join<X>;
}