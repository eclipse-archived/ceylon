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
}
@noanno
interface A{}
@noanno
interface B{}
@noanno
interface Left satisfies Top<A>{}
@noanno
interface Right satisfies Top<B>{
    // why is this not legal?
    //shared actual default <A&B>? s => null;
}

@noanno
class Bottom() satisfies Left&Right/*&Top<B>*/{
    shared actual <A&B>? s => super.s;
}

void bug1240_1(){
    Top<A> ba = Bottom();
    A? a = ba.s;
    Top<B> bb = Bottom();
    B? b = bb.s;
    print("ok");
}