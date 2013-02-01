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
import java.lang {
    JBoolean = Boolean,
    JByte = Byte,
    JShort = Short,
    JInteger = Integer,
    JLong = Long,
    JFloat = Float,
    JDouble = Double,
    JCharacter = Character,
    JString = String
}
import java.util { List, ArrayList }

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
    Boolean b2 = java.return_Boolean().booleanValue();
    Object b4 = java.return_Boolean();
    Object? b5 = java.return_Boolean();
    @error
    java.booleanParams(true, JBoolean(true), true);
    @error
    java.booleanParams(box(true), JBoolean(box(true)), box(true));
    @error
    java.booleanParams(java.return_Boolean().booleanValue(), java.return_Boolean(), java.return_Boolean().booleanValue());
    @error
    java.booleanParams(java.return_boolean(), JBoolean(java.return_boolean()), java.return_boolean());
}
@nomodel
@error
void byteTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_byte();
    Integer? n2 = java.return_byte();
    Integer n3 = java.return_Byte().longValue();
    // FIXME:
    //Integer? n4 = java.return_Byte()?.longValue();
    @error
    java.byteParams(1, JByte(1));
    @error
    java.byteParams(box(1), JByte(box(1)));
    @error
    java.byteParams(java.return_byte(), JByte(java.return_byte()));
    @error
    java.byteParams(java.return_Byte().byteValue(), java.return_Byte());
}
@nomodel
@error
void shortTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_short();
    Integer? n2 = java.return_short();
    Integer n3 = java.return_Short().longValue();
    // FIXME:
    //Integer? n4 = java.return_Short()?.longValue();
    @error
    java.shortParams(1, JShort(1));
    @error
    java.shortParams(box(1), JShort(box(1)));
    @error
    java.shortParams(java.return_short(), JShort(java.return_short()));
    @error
    java.shortParams(java.return_Short().shortValue(), java.return_Short());
}
@nomodel
@error
void integerTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_int();
    Integer? n2 = java.return_int();
    Integer n3 = java.return_Integer().longValue();
    // FIXME
    //Integer? n4 = java.return_Integer()?.longValue();
    @error
    java.intParams(1, JInteger(1));
    @error
    java.intParams(box(1), JInteger(box(1)));
    @error
    java.intParams(java.return_int(), JInteger(java.return_int()));
    @error
    java.intParams(java.return_Integer().intValue(), java.return_Integer());
}
@nomodel
@error
void longTypes() {
    @error
    TypesJava java = TypesJava();
    Integer n1 = java.return_long();
    Integer? n2 = java.return_long();
    Integer n3 = java.return_Long().longValue();
    // FIXME:
    //Integer? n4 = java.return_Long()?.longValue();
    @error
    java.longParams(1, JLong(1), 1);
    @error
    java.longParams(box(1), JLong(box(1)), box(1));
    @error
    java.longParams(java.return_long(), JLong(java.return_long()), java.return_long());
    @error
    java.longParams(java.return_Long().longValue(), java.return_Long(), java.return_Long().longValue());
}
@nomodel
@error
void floatTypes() {
    @error
    TypesJava java = TypesJava();
    Float f1 = java.return_float();
    Float? f2 = java.return_float();
    Float f3 = java.return_Float().doubleValue();
    // FIXME
    //Float? f4 = java.return_Float()?.doubleValue();
    @error
    java.floatParams(1.0, JFloat(1.0));
    @error
    java.floatParams(box(1.0), JFloat(box(1.0)));
    @error
    java.floatParams(java.return_float(), JFloat(java.return_float()));
    @error
    java.floatParams(java.return_Float().floatValue(), java.return_Float());
}
@nomodel
@error
void doubleTypes() {
    @error
    TypesJava java = TypesJava();
    Float f1 = java.return_double();
    Float? f2 = java.return_double();
    Float f3 = java.return_Double().doubleValue();
    // FIXME
    //Float? f4 = java.return_Double()?.doubleValue();
    @error
    java.doubleParams(1.0, JDouble(1.0), 1.0);
    @error
    java.doubleParams(box(1.0), JDouble(box(1.0)), box(1.0));
    @error
    java.doubleParams(java.return_double(), JDouble(java.return_double()), java.return_double());
    @error
    java.doubleParams(java.return_Double().doubleValue(), java.return_Double(), java.return_Double().doubleValue());
}
@nomodel
@error
void characterTypes() {
    @error
    TypesJava java = TypesJava();
    Character c1 = java.return_char();
    Character? c2 = java.return_char();
    Character c3 = java.return_Character().charValue();
    // FIXME
    //Character? c4 = java.return_Character()?.charValue();
    @error
    java.charParams('a', JCharacter('a'), 'a');
    @error
    java.charParams(box('a'), JCharacter(box('a')), box('a'));
    @error
    java.charParams(java.return_char(), JCharacter(java.return_char()), java.return_char());
    @error
    java.charParams(java.return_Character().charValue(), java.return_Character(), java.return_Character().charValue());
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
    Object o1 = java.return_Object();
    Object? o2 = java.return_Object();
    @error
    java.objectParams(o1);
    @error
    java.objectParams(java.return_Object());
}

@nomodel
@error
void operationsOnBytes() {
    @error
    TypesJava java = TypesJava();
    java.byte_attr = java.byte_attr + java.byte_attr;
    java.byte_attr = java.byte_attr * java.byte_attr;
    java.byte_attr = java.byte_attr / java.byte_attr;
    java.byte_attr = java.byte_attr - java.byte_attr;
    java.byte_attr = java.byte_attr ^ java.byte_attr;
    
    java.byte_attr = java.byte_attr++;
    java.byte_attr = ++java.byte_attr;

    java.byte_attr = java.byte_attr += java.byte_attr;
    java.byte_attr = java.byte_attr -= java.byte_attr;
    java.byte_attr = java.byte_attr *= java.byte_attr;
    java.byte_attr = java.byte_attr /= java.byte_attr;
    
    variable Boolean sync;
    sync = java.byte_attr == java.byte_attr;
    sync = java.byte_attr != java.byte_attr;
    sync = java.byte_attr < java.byte_attr;
}

@nomodel
@error
void operationsOnBytes2() {
    @error
    TypesJava java = TypesJava();
    java.byte = java.byte + java.byte;
    java.byte = java.byte * java.byte;
    java.byte = java.byte / java.byte;
    java.byte = java.byte - java.byte;
    java.byte = java.byte ^ java.byte;
    
    java.byte = java.byte++;
    java.byte = ++java.byte;

    java.byte = java.byte += java.byte;
    java.byte = java.byte -= java.byte;
    java.byte = java.byte *= java.byte;
    java.byte = java.byte /= java.byte;
    
    variable Boolean sync;
    sync = java.byte == java.byte;
    sync = java.byte != java.byte;
    sync = java.byte < java.byte;
}

@nomodel
class OperationsOnBytes() extends TypesJava(){
    void m(){
        byte_attr = byte_attr + byte_attr;
        byte_attr = byte_attr * byte_attr;
        byte_attr = byte_attr / byte_attr;
        byte_attr = byte_attr - byte_attr;
        byte_attr = byte_attr ^ byte_attr;
        
        byte_attr = byte_attr++;
        byte_attr = ++byte_attr;
        
        byte_attr = byte_attr += byte_attr;
        byte_attr = byte_attr -= byte_attr;
        byte_attr = byte_attr *= byte_attr;
        byte_attr = byte_attr /= byte_attr;
        
        variable Boolean sync;
        sync = byte_attr == byte_attr;
        sync = byte_attr != byte_attr;
        sync = byte_attr < byte_attr;
    }
}

@nomodel
@error
void operationsOnMixedTypes() {
    @error
    TypesJava java = TypesJava();
    java.byte_attr = java.byte_attr + java.int_attr;
    java.int_attr = java.byte_attr + java.int_attr;
    java.byte_attr = java.int_attr + java.byte_attr;
    java.int_attr = java.int_attr + java.byte_attr;
    
    java.byte_attr = java.byte_attr ^ java.int_attr;
    java.byte_attr = java.int_attr ^ java.byte_attr;
    
    java.byte_attr = java.int_attr++;
    java.byte_attr = ++java.int_attr;

    java.byte_attr = java.byte_attr += java.int_attr;
    java.byte_attr = java.int_attr += java.byte_attr;
    
    variable Boolean sync;
    sync = java.byte_attr == java.int_attr;
    sync = java.byte_attr != java.int_attr;
    sync = java.byte_attr < java.int_attr;
}

@nomodel
@error
void typeParameters(){
    @error
    TypesJava java = TypesJava();

    List<String> stringList = java.stringList();
    stringList.add("foo");
    List<JString> jstringList = java.jstringList();
    jstringList.add(JString("foo"));
    List<Object> objectList = java.objectList();
    objectList.add("foo");
}

@nomodel
@error
void optionalTypes(){
    @error
    TypesJava java = TypesJava();

    String? string = "foo";
    java.stringParams(string, string);
}