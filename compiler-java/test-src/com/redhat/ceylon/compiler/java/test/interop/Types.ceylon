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
@error
void m() {
    @error
    TypesJava java = TypesJava();
    Boolean b1 = java.return_boolean();
    Boolean b2 = java.return_Boolean().booleanValue();
    Integer n1 = java.return_int();
    Integer n2 = java.return_Integer().longValue();
    Integer n3 = java.return_long();
    Integer n4 = java.return_Long().longValue();
    Float f1 = java.return_float();
    Float f2 = java.return_Float().doubleValue();
    Float f3 = java.return_double();
    Float f4 = java.return_Double().doubleValue();
    Character c1 = java.return_char();
    Character c2 = java.return_Character().charValue();
    String s = java.return_String();
    Object o = java.return_Object();
}
