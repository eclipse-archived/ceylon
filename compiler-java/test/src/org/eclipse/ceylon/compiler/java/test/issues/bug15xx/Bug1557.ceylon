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
void run(C c) {
    print(cmp(["a", 2], ["b", 4])); // <= this one is fine and reports correctly
    print(cmp([1, 2], [2, 4])); // <= IDE says backend error
    cmp1([1, 2]);
    cmp2([c, c]);
}

@noanno
interface A{}
@noanno
interface B{}
@noanno
interface C satisfies A & B{}

@noanno
Comparison cmp<Value1,Value2>([Value1, Value2] a, [Value1, Value2] b) 
    given Value1 satisfies Comparable<Value1>
    given Value2 satisfies Comparable<Value2>
{
    if(a[0] < b[0]) { return smaller; }
    else if(a[0] > b[0]) { return larger; }
    else if(a[1] < b[1]) { return smaller; }
    else if(a[1] > b[1]) { return larger; }
    else { return equal; }
}

@noanno
void cmp1<Value1,Value2>([Value1, Value2] a) {
}

@noanno
void cmp2([A, B] a) {
}
