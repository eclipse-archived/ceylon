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
shared interface Bug1879_N<out P, N> 
        given N satisfies Bug1879_N<P, N>{
}
@noanno
shared interface Bug1879_P<N>
        given N satisfies Bug1879_N <Bug1879_P<N>, N> {
    shared formal void f0(String * args);
    shared formal variable {String*} v; 
    shared formal class MemberClass({String*} g){}
}
@noanno
shared class Bug1879_X() satisfies Bug1879_N<Bug1879_Y, Bug1879_X> {
} 
@noanno
shared class Bug1879_Y() satisfies Bug1879_P<Bug1879_X> {
    shared actual default void f0(String * p) {}
    shared actual default {String*} v => {};
    assign v{}
    shared actual default class MemberClass({String*} g) extends super.MemberClass(g){}
}
@noanno
shared class Bug1879_Z() extends Bug1879_Y() {
    shared actual default void f0(String * p) {}
    shared actual default {String*} v => {};
    assign v{}
    shared actual default class MemberClass({String*} g) extends super.MemberClass(g){}
}
@noanno
shared interface Bug1879_Y2 satisfies Bug1879_P<Bug1879_X> {
    shared actual default void f0(String * p) {}
    shared actual default {String*} v => {};
    assign v{}
    shared actual default class MemberClass({String*} g) extends super.MemberClass(g){}
}