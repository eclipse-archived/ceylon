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
class MRAVEC_Invariant<Element>()  {}
@noanno
class MRAVEC_Covariant<out Element>() {}
@noanno
class MRAVEC_Contravariant<in Element>() {}

@noanno
class MRAVEC_Top<Element>() {
    shared default Element f() => nothing;
    shared default MRAVEC_Invariant<Element> f2() => nothing;
    shared default MRAVEC_Covariant<Element> f3() => nothing;
    shared default MRAVEC_Contravariant<Element> f4() => nothing;
}
@noanno
class MRAVEC_BottomNull() extends MRAVEC_Top<Null>(){
    shared actual Null f() => nothing;
    shared actual MRAVEC_Invariant<Null> f2() => nothing;
    shared actual MRAVEC_Covariant<Null> f3() => nothing;
    shared actual MRAVEC_Contravariant<Null> f4() => nothing;
}
@noanno
class MRAVEC_BottomNothing() extends MRAVEC_Top<Nothing>(){
    shared actual Nothing f() => nothing;
    shared actual MRAVEC_Invariant<Nothing> f2() => nothing;
    shared actual MRAVEC_Covariant<Nothing> f3() => nothing;
    shared actual MRAVEC_Contravariant<Nothing> f4() => nothing;
}
@noanno
class MRAVEC_BottomAnything() extends MRAVEC_Top<Anything>(){
    shared actual Anything f() => nothing;
    shared actual MRAVEC_Invariant<Anything> f2() => nothing;
    shared actual MRAVEC_Covariant<Anything> f3() => nothing;
    shared actual MRAVEC_Contravariant<Anything> f4() => nothing;
}
@noanno
class MRAVEC_BottomErased() extends MRAVEC_Top<MRAVEC_BottomErased|MRAVEC_BottomNothing>(){
    shared actual MRAVEC_BottomErased|MRAVEC_BottomNothing f() => nothing;
    shared actual MRAVEC_Invariant<MRAVEC_BottomErased|MRAVEC_BottomNothing> f2() => nothing;
    shared actual MRAVEC_Covariant<MRAVEC_BottomErased|MRAVEC_BottomNothing> f3() => nothing;
    shared actual MRAVEC_Contravariant<MRAVEC_BottomErased|MRAVEC_BottomNothing> f4() => nothing;
}
