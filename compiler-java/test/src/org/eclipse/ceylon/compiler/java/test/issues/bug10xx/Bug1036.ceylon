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
interface Scalable<in T,out S> of S 
        given S satisfies Scalable<T,S> {
    shared formal S scaleUp(T t);
    shared formal S scaleDown(T t);
}

@noanno
interface Numeric<T> of T 
        satisfies Scalable<T,T>
        given T satisfies Numeric<T> {
    shared formal T times(T t);
    shared formal T divided(T t);
    shared  actual T scaleUp(T t) => t.times(this of T);
    shared  actual T scaleDown(T t) => divided(t);
}

@noanno
S scale<T,S>(T x, Scalable<T,S> y) 
        given S satisfies Scalable<T,S>
        => y.scaleUp(x);

@noanno
abstract class Vector() satisfies Scalable<Real,Vector> {}
@noanno
abstract class Real() satisfies Numeric<Real>&Scalable<Int|Real,Real> {}
@noanno
abstract class Int() satisfies Numeric<Int> {}

@noanno
void bug1036(Real real, Vector vector, Int int) {
    Vector vectorScaledUp = scale(real,vector);
    Real realProduct = scale(real,real);
    Real realScaledUp = scale(int,real);
}