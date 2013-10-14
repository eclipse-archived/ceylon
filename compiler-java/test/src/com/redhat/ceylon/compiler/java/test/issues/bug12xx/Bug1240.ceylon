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
interface Top<out T> {
    shared default T? s => null;
    shared default T? m() => null;
}
@noanno
interface A{}
@noanno
interface B{}
@noanno
interface Left satisfies Top<A>{}
@noanno
interface Middle<out T> satisfies Top<T> {
    shared actual default T? s => super.s;
    shared actual default T? m() => super.m();
}
@noanno
interface Right satisfies Middle<A>{}

@noanno
class Bottom() satisfies Left&Middle<B>&Right{}
@noanno
class Bottom2() satisfies Left&Middle<B>&Right{
    shared actual <A&B>? s => super.s;
    shared actual <A&B>? m() => super.m();
}

void bug1240(){
    bug1240Check(Bottom());
    bug1240Check(Bottom2());
}

void bug1240Check(Left&Middle<B>&Right b){
    Top<A> ba = b;
    A? a1 = ba.s;
    Top<B> bb = b;
    B? b1 = bb.s;
    Middle<A> ma = b;
    A? a2 = ma.s;
    Middle<B> mb = b;
    B? b2 = mb.s;
    Left l = b;
    A? a3 = l.s;
    Middle<B> r = b;
    B? b3 = r.s;
    print("ok");
}