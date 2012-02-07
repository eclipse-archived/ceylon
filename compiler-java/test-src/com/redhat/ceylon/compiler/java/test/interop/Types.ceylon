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
T box<T>(T t){
    return t;
}

@nomodel
@error
void booleanTypes() {
    @error
    TypesJava java = TypesJava();
    Boolean b1 = java.return_boolean();
    Boolean b2 = java.return_Boolean();
    Boolean? b3 = java.return_Boolean();
    Object b4 = java.return_Boolean();
    Object? b5 = java.return_Boolean();
    @error
    java.booleanParams(true, true, true);
    @error
    java.booleanParams(box(true), box(true), box(true));
    @error
    java.booleanParams(java.return_Boolean(), java.return_Boolean(), java.return_Boolean());
    @error
    java.booleanParams(java.return_boolean(), java.return_boolean(), java.return_boolean());
}
@nomodel
@error
void byteTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_byte();
    Integer? n2 = java.return_byte();
    Integer n3 = java.return_Byte();
    Integer? n4 = java.return_Byte();
    @error
    java.byteParams(1, 1);
    @error
    java.byteParams(box(1), box(1));
    @error
    java.byteParams(java.return_byte(), java.return_byte());
    @error
    java.byteParams(java.return_Byte(), java.return_Byte());
}
@nomodel
@error
void shortTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_short();
    Integer? n2 = java.return_short();
    Integer n3 = java.return_Short();
    Integer? n4 = java.return_Short();
    @error
    java.shortParams(1, 1);
    @error
    java.shortParams(box(1), box(1));
    @error
    java.shortParams(java.return_short(), java.return_short());
    @error
    java.shortParams(java.return_Short(), java.return_Short());
}
@nomodel
@error
void integerTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_int();
    Integer? n2 = java.return_int();
    Integer n3 = java.return_Integer();
    Integer? n4 = java.return_Integer();
    @error
    java.intParams(1, 1);
    @error
    java.intParams(box(1), box(1));
    @error
    java.intParams(java.return_int(), java.return_int());
    @error
    java.intParams(java.return_Integer(), java.return_Integer());
}
@nomodel
@error
void longTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_long();
    Integer? n2 = java.return_long();
    Integer n3 = java.return_Long();
    Integer? n4 = java.return_Long();
    @error
    java.longParams(1, 1, 1);
    @error
    java.longParams(box(1), box(1), box(1));
    @error
    java.longParams(java.return_long(), java.return_long(), java.return_long());
    @error
    java.longParams(java.return_Long(), java.return_Long(), java.return_Long());
}
@nomodel
@error
void floatTypes() {
    @error
    TypesJava java = TypesJava();
    Float f1 = java.return_float();
    Float? f2 = java.return_float();
    Float f3 = java.return_Float();
    Float? f4 = java.return_Float();
    @error
    java.floatParams(1.0, 1.0);
    @error
    java.floatParams(box(1.0), box(1.0));
    @error
    java.floatParams(java.return_float(), java.return_float());
    @error
    java.floatParams(java.return_Float(), java.return_Float());
}
@nomodel
@error
void doubleTypes() {
    @error
    TypesJava java = TypesJava();
    Float f1 = java.return_double();
    Float? f2 = java.return_double();
    Float f3 = java.return_Double();
    Float? f4 = java.return_Double();
    @error
    java.doubleParams(1.0, 1.0, 1.0);
    @error
    java.doubleParams(box(1.0), box(1.0), box(1.0));
    @error
    java.doubleParams(java.return_float(), java.return_float(), java.return_double());
    @error
    java.doubleParams(java.return_Double(), java.return_Double(), java.return_Double());
}
@nomodel
@error
void characterTypes() {
    @error
    TypesJava java = TypesJava();
    Character c1 = java.return_char();
    Character? c2 = java.return_char();
    Character c3 = java.return_Character();
    Character? c4 = java.return_Character();
    @error
    java.charParams(`a`, `a`, `a`);
    @error
    java.charParams(box(`a`), box(`a`), box(`a`));
    @error
    java.charParams(java.return_char(), java.return_char(), java.return_char());
    @error
    java.charParams(java.return_Character(), java.return_Character(), java.return_Character());
}
@nomodel
@error
void stringTypes() {
    @error
    TypesJava java = TypesJava();
    String s1 = java.return_String();
    String? s2 = java.return_String();
    @error
    java.stringParams("a", "a");
    @error
    java.stringParams(box("a"), box("a"));
    @error
    java.stringParams(java.return_String(), java.return_String());
}
@nomodel
@error
void objectTypes() {
    @error
    TypesJava java = TypesJava();
    IdentifiableObject o1 = java.return_Object();
    IdentifiableObject? o2 = java.return_Object();
    @error
    java.objectParams(o1);
    @error
    java.objectParams(java.return_Object());
}
