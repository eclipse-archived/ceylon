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
void callableArgumentParameterTypeParamMixed_f<S,T>(S foo(S s, T t)) {
}
@noanno
U callableArgumentParameterTypeParamMixed_bar<U,V>(U u, V v) {
    return u;
}
@noanno
Integer callableArgumentParameterTypeParamMixed_baz(Integer i, Boolean b) {
    return i;
}
@noanno
class CallableArgumentParameterTypeParamMixed<X>() {
    void m<Y>() {
        callableArgumentParameterTypeParamMixed_f<Integer,Boolean>(callableArgumentParameterTypeParamMixed_baz);
        callableArgumentParameterTypeParamMixed_f<Boolean,Integer>(callableArgumentParameterTypeParamMixed_bar<Boolean,Integer>);
        callableArgumentParameterTypeParamMixed_f<X,Integer>(callableArgumentParameterTypeParamMixed_bar<X,Integer>);
        callableArgumentParameterTypeParamMixed_f<X,Y>(callableArgumentParameterTypeParamMixed_bar<X,Y>);
        callableArgumentParameterTypeParamMixed_f<X&Y,Y>(callableArgumentParameterTypeParamMixed_bar<X&Y,Y>);
        callableArgumentParameterTypeParamMixed_f<X|Y,Y>(callableArgumentParameterTypeParamMixed_bar<X|Y,Y>);
    }
}