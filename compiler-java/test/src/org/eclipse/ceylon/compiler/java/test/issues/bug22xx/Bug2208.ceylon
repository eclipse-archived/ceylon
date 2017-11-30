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
shared abstract class Sup() {}

@noanno
shared class Sub1() extends Sup() {}

@noanno
shared class Sub2() extends Sup() {}

@noanno
shared interface I<S>
        given S satisfies Sup {
    shared formal void toT(S s = nothing, S s2 = nothing);
    shared default void toT2(S s = nothing, S s2 = nothing){}
    
    shared formal class Inner(S s = nothing, S s2 = nothing){}
    shared class Inner2(S s = nothing, S s2 = nothing){}
}

@noanno
shared class C() satisfies I<Sub1|Sub2> {
    shared actual void toT(Sub1|Sub2 s, Sub1|Sub2 s2){}

    shared actual class Inner(Sub1|Sub2 s, Sub1|Sub2 s2) extends super.Inner(s, s2){}
}

@noanno
shared interface Range {
    shared formal String|Integer step;
}

@noanno
shared abstract class DateRange( step = nothing ) satisfies Range {

    shared actual String step;
}

@noanno
shared interface Completion<out Element, out T>
        given T satisfies Element[] {
    
    shared default void completed(
      Anything(Throwable) onRejected = nothing){}
}

@noanno
shared abstract class Promise<out Value>()
        satisfies Completion<Value,[Value]> {

    shared actual void completed(
      Anything onRejected(Throwable reason)){}
}

@noanno
interface FormalMethods<A, B> {
    shared default void tpMethodBUnary(B arg=nothing){}
}
@noanno
class MissingActualMethods() 
        satisfies FormalMethods<Integer,String>{}

@noanno
class IfaceInst() satisfies InterfaceConstructors<String> {
}

@noanno
shared interface InterfaceConstructors<T> {
    shared class Member {
        shared new (T? t=null) {
            
        }
    }
}

