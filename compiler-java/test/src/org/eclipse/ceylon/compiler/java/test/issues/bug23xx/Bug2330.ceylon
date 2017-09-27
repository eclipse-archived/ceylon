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
void bug2330<Item, X>(Item item, X x)
        given Item satisfies Comparable<Item>
        given X satisfies Comparable<X> {
    
    value y = item;
    assert (is Integer y);
    value lt1 = y < 5; // Comparable<Item | Integer>
    value gt1 = 5 > y; // Comparable<Item | Integer>
    
    
    if (is Integer item) {
        value lt = item < 5;
        value gt = 5 > item;
        value eq = item < item;
        value cmp = item <=> 5;
        value wthn = item <= item <= 5;
    } else if (is Float item) {
        value lt = item < 5.0;
        value gt = 5.0 > item;
        value eq = item < item;
        value cmp = item <=> 5.0;
        value wthn = item <= item <= 5.0;
    } else if (is Character item) {
        value lt = item < 'a';
        value gt = 'a' > item;
        value eq = item < item;
        value cmp = item <=> 'a';
        value wthn = item <= item <= 'a';
    } else if (is String item) {
        value lt = item < "a";
        value gt = "a" > item;
        value eq = item < item;
        value cmp = item <=> "a";
        value wthn = item <= item <= "a";
    } else if (is X item) {
        value lt = item < x;
        value gt = x > item;// HERE
        value eq = item < item;
        value cmp = item <=> x;
        value wthn = item <= item <= x;
    }
    
    if (is Comparable<Integer> item) {
        value lt = item < 5;// item has type Item&Comparable<Integer>
        value gt = 5 > (item of Integer);
        value eq = item < item;
        value cmp = item <=> 5;
        value wthn = item <= item <= 5;
    } else if (is Comparable<Float> item) {
        value lt = item < 5.0;// item has type Item&Comparable<Float>
        value gt = 5.9 > (item of Float);
        value eq = item < item;
        value cmp = item <=> 5.0;
        value wthn = item <= item <= 5.0;
    } else if (is Comparable<Character> item) {
        value lt = item < 'a';
        value gt = 'a' > (item of Character);
        value eq = item < item;
        value cmp = item <=> '5';
        value wthn = item <= item <= '5';
    } else if (is Comparable<String> item) {
        value lt = item < "a";
        value gt = "a" > (item of String);
        value eq = item < item;
        value cmp = item <=> "5";
        value wthn = item <= item <= "5";
    } else if (is Comparable<X> item) {
        // item has type Item&Comparable<X>
        // x has type X
        value lt = item < x;
        value gt = x > (item of X);// HERE
        value eq = item < item;
        value cmp = item <=> x;
        value wthn = item <= item <= x;
    }
    
    // TODO also test within operator
}