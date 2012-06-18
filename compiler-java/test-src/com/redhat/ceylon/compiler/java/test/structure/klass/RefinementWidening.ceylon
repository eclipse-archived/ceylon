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
class RWInvParam<T>(){}
@nomodel
class RWInvParamExt<T>() extends RWInvParam<T>(){}

@nomodel
class RWCovParam<out T>(){}
@nomodel
class RWCovParamExt<out T>() extends RWCovParam<T>(){}

@nomodel
class RWContrParam<in T>(){}
@nomodel
class RWContrParamExt<in T>() extends RWContrParam<T>(){}

@nomodel
interface RWTopI{}
@nomodel
interface RWLeft satisfies RWTopI{}
@nomodel
interface RWRight satisfies RWTopI{}

@nomodel
abstract class RWTop<X, Y>() {
    shared formal RWInvParam<Number> m();
    shared formal RWCovParam<Number> m2();
    shared formal RWCovParam<Number> m2_erasure();
    shared formal RWContrParam<Integer> m3();
    shared formal RWContrParam<Integer> m3_erasure();
    shared formal T tParam<T>();
    shared formal T tBoundParam<T>() given T satisfies Number;
    shared formal T tCovBoundParam<out T>() given T satisfies Integer;
    shared formal X typeBound();
    shared formal X typeCovBound();
    shared formal Y classBound();
    shared formal Y classBound_erasure();
}

@nomodel
abstract class RWBottom<Z>() extends RWTop<Number, Z>() {
    shared actual formal RWInvParamExt<Number> m();
    shared actual formal RWCovParamExt<Integer> m2();
    shared actual formal RWCovParamExt<Integer|Float> m2_erasure();
    shared actual formal RWContrParamExt<Number> m3();
    shared actual formal RWContrParamExt<Number|Category> m3_erasure();
    shared actual formal S tParam<S>();
    shared actual formal S tBoundParam<S>();
    shared actual formal S tCovBoundParam<out S>() given S satisfies Number;
    shared actual formal Number typeBound();
    shared actual formal Integer typeCovBound();
    shared actual formal Z classBound();
    shared actual formal Z&Number classBound_erasure();
}