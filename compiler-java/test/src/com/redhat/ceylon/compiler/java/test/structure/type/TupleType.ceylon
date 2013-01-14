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
[String, Integer, Float] triple(String s, Integer i, Float f) {
    return [s, i, f];
}

@nomodel
Float add([Float,Float] floats=[1.0, 2.0]) {
    return floats.first+floats.rest.first;
}

@nomodel
void tupleTypeTest() {
    value tup = triple("hello", 0, 0.0);
    String a = tup.first;
    Integer b = tup.rest.first;
    Float c = tup.rest.rest.first;

    [String, String] hibye = ["hello", "goodbye"];
    [String, String] fun() {
        return hibye;
    }
    Sequence<String> strings = hibye;
    [String, String*] hibye1 = hibye;
    [String*] hibye2 = hibye;
    [String, Integer, Object*] trip = triple("", 0, 0.0);
    
    [] emptyTuple = [];
    [String] oneTuple = ["a"];
    [String, Integer] twoTuple = ["a", 2];
    [String, Integer, String] threeTuple = ["a", 2, "c"];
    [String*] oneTupleEllipsis = [*strings];
    [Integer, String*] twoTupleEllipsis = [1, *strings];
    [String, Integer, String*] threeTupleEllipsis = ["a", 1, *strings];
    [String*] comprehensionTuple = [for (s in strings) s];
}
