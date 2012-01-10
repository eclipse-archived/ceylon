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
void arraysBoxed() {
    TypesJava java = TypesJava();
    Boolean[] b1 = java.array_boolean();
    Boolean[] b2 = java.array_Boolean();
    Integer[] n1 = java.array_int();
    Integer[] n2 = java.array_Integer();
    Integer[] n3 = java.array_long();
    Integer[] n4 = java.array_Long();
    Float[] f1 = java.array_float();
    Float[] f2 = java.array_Float();
    Float[] f3 = java.array_double();
    Float[] f4 = java.array_Double();
    Character[] c1 = java.array_char();
    Character[] c2 = java.array_Character();
    String[] s = java.array_String();
    Object[] o = java.array_Object();
}

@nomodel
void arraysUnboxed() {
    TypesJava java = TypesJava();
    Array<Boolean> b1 = java.array_boolean();
    Array<Boolean> b2 = java.array_Boolean();
    Array<Integer> n1 = java.array_int();
    Array<Integer> n2 = java.array_Integer();
    Array<Integer> n3 = java.array_long();
    Array<Integer> n4 = java.array_Long();
    Array<Float> f1 = java.array_float();
    Array<Float> f2 = java.array_Float();
    Array<Float> f3 = java.array_double();
    Array<Float> f4 = java.array_Double();
    Array<Character> c1 = java.array_char();
    Array<Character> c2 = java.array_Character();
    Array<String> s = java.array_String();
    Array<Object> o = java.array_Object();
}
