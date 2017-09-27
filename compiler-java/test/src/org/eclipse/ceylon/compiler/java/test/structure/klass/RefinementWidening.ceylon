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
class RWInvParam<T>(){}
@noanno
class RWInvParamExt<T>() extends RWInvParam<T>(){}

@noanno
class RWCovParam<out T>(){}
@noanno
class RWCovParamExt<out T>() extends RWCovParam<T>(){}

@noanno
class RWContrParam<in T>(){}
@noanno
class RWContrParamExt<in T>() extends RWContrParam<T>(){}

@noanno
interface RWTopI{}
@noanno
interface RWLeft satisfies RWTopI{}
@noanno
interface RWRight satisfies RWTopI{}

@noanno
interface RWNumber {}
@noanno
interface RWInteger satisfies RWNumber {}
@noanno
interface RWFloat satisfies RWNumber {}

@noanno
abstract class RWTop<X, Y>() {
    shared formal RWInvParam<RWNumber> m();
    shared formal RWCovParam<RWNumber> m2();
    shared formal RWCovParam<RWNumber> m2_erasure();
    shared formal RWContrParam<RWInteger> m3();
    shared formal RWContrParam<RWInteger> m3_erasure();
    shared formal T tParam<T>();
    shared formal T tParamErasure<T>();
    // disabled because although we can compile them, we can't invoke them: https://github.com/ceylon/ceylon-compiler/issues/1664
    //shared formal T tBoundParam<T>() given T satisfies RWNumber;
    shared formal T tCovBoundParam<out T>() given T satisfies RWInteger;
    shared formal X typeBound();
    shared formal X typeCovBound();
    shared formal Y classBound();
    shared formal Y classBound_erasure();
}

@noanno
abstract class RWBottom<Z>() extends RWTop<RWNumber, Z>() {
    shared actual formal RWInvParamExt<RWNumber> m();
    shared actual formal RWCovParamExt<RWInteger> m2();
    shared actual formal RWCovParamExt<RWInteger|RWFloat> m2_erasure();
    shared actual formal RWContrParamExt<RWNumber> m3();
    shared actual formal RWContrParamExt<RWNumber|Category> m3_erasure();
    shared actual formal S tParam<S>();
    shared actual formal S&RWNumber tParamErasure<S>();
    //shared actual formal S tBoundParam<S>();
    shared actual formal S tCovBoundParam<out S>() given S satisfies RWNumber;
    shared actual formal RWNumber typeBound();
    shared actual formal RWInteger typeCovBound();
    shared actual formal Z classBound();
    shared actual formal Z&RWNumber classBound_erasure();
}