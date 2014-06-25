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

@noanno
T box<T>(T t){
    return t;
}

@noanno
void booleanTypes() {
    TypesJava java = TypesJava();
    Boolean b1 = java.return_boolean();
    Boolean b2 = java.return_Boolean().booleanValue();
    Object b4 = java.return_Boolean();
    Object? b5 = java.return_Boolean();
    java.booleanParams(true, JBoolean(true), true);
    java.booleanParams(box(true), JBoolean(box(true)), box(true));
    java.booleanParams(java.return_Boolean().booleanValue(), java.return_Boolean(), java.return_Boolean().booleanValue());
    java.booleanParams(java.return_boolean(), JBoolean(java.return_boolean()), java.return_boolean());
}
@noanno
void byteTypes() {
    TypesJava java = TypesJava();
    Integer n1 = java.return_byte();
    Integer? n2 = java.return_byte();
    Integer n3 = java.return_Byte().longValue();
    // FIXME:
    //Integer? n4 = java.return_Byte()?.longValue();
    java.byteParams(1, JByte(1));
    java.byteParams(box(1), JByte(box(1)));
    java.byteParams(java.return_byte(), JByte(java.return_byte()));
    java.byteParams(java.return_Byte().byteValue(), java.return_Byte());
}
@noanno
void shortTypes() {
    TypesJava java = TypesJava();
    Integer n1 = java.return_short();
    Integer? n2 = java.return_short();
    Integer n3 = java.return_Short().longValue();
    // FIXME:
    //Integer? n4 = java.return_Short()?.longValue();
    java.shortParams(1, JShort(1));
    java.shortParams(box(1), JShort(box(1)));
    java.shortParams(java.return_short(), JShort(java.return_short()));
    java.shortParams(java.return_Short().shortValue(), java.return_Short());
}
@noanno
void integerTypes() {
    TypesJava java = TypesJava();
    Integer n1 = java.return_int();
    Integer? n2 = java.return_int();
    Integer n3 = java.return_Integer().longValue();
    // FIXME
    //Integer? n4 = java.return_Integer()?.longValue();
    java.intParams(1, JInteger(1));
    java.intParams(box(1), JInteger(box(1)));
    java.intParams(java.return_int(), JInteger(java.return_int()));
    java.intParams(java.return_Integer().intValue(), java.return_Integer());
}
@noanno
void longTypes() {
    TypesJava java = TypesJava();
    Integer n1 = java.return_long();
    Integer? n2 = java.return_long();
    Integer n3 = java.return_Long().longValue();
    // FIXME:
    //Integer? n4 = java.return_Long()?.longValue();
    java.longParams(1, JLong(1), 1);
    java.longParams(box(1), JLong(box(1)), box(1));
    java.longParams(java.return_long(), JLong(java.return_long()), java.return_long());
    java.longParams(java.return_Long().longValue(), java.return_Long(), java.return_Long().longValue());
}
@noanno
void floatTypes() {
    TypesJava java = TypesJava();
    Float f1 = java.return_float();
    Float? f2 = java.return_float();
    Float f3 = java.return_Float().doubleValue();
    // FIXME
    //Float? f4 = java.return_Float()?.doubleValue();
    java.floatParams(1.0, JFloat(1.0));
    java.floatParams(box(1.0), JFloat(box(1.0)));
    java.floatParams(java.return_float(), JFloat(java.return_float()));
    java.floatParams(java.return_Float().floatValue(), java.return_Float());
}
@noanno
void doubleTypes() {
    TypesJava java = TypesJava();
    Float f1 = java.return_double();
    Float? f2 = java.return_double();
    Float f3 = java.return_Double().doubleValue();
    // FIXME
    //Float? f4 = java.return_Double()?.doubleValue();
    java.doubleParams(1.0, JDouble(1.0), 1.0);
    java.doubleParams(box(1.0), JDouble(box(1.0)), box(1.0));
    java.doubleParams(java.return_double(), JDouble(java.return_double()), java.return_double());
    java.doubleParams(java.return_Double().doubleValue(), java.return_Double(), java.return_Double().doubleValue());
}
@noanno
void characterTypes() {
    TypesJava java = TypesJava();
    Character c1 = java.return_char();
    Character? c2 = java.return_char();
    Character c3 = java.return_Character().charValue();
    // FIXME
    //Character? c4 = java.return_Character()?.charValue();
    java.charParams('a', JCharacter('a'), 'a');
    java.charParams(box('a'), JCharacter(box('a')), box('a'));
    java.charParams(java.return_char(), JCharacter(java.return_char()), java.return_char());
    java.charParams(java.return_Character().charValue(), java.return_Character(), java.return_Character().charValue());
}
@noanno
void stringTypes() {
    TypesJava java = TypesJava();
    String s1 = java.return_String();
    String? s2 = java.return_String();
    java.stringParams("a", "a");
    java.stringParams(box("a"), box("a"));
    java.stringParams(java.return_String(), java.return_String());
}
@noanno
void objectTypes() {
    TypesJava java = TypesJava();
    Object o1 = java.return_Object();
    Object? o2 = java.return_Object();
    java.objectParams(o1);
    java.objectParams(java.return_Object());
}

@noanno
void operationsOnBytes() {
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

@noanno
void operationsOnBytes2() {
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

@noanno
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

@noanno
void operationsOnMixedTypes() {
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

@noanno
void typeParameters(){
    TypesJava java = TypesJava();

    List<String> stringList = java.stringList();
    stringList.add("foo");
    List<JString> jstringList = java.jstringList();
    jstringList.add(JString("foo"));
    List<Object> objectList = java.objectList();
    objectList.add("foo");
}

@noanno
void optionalTypes(){
    TypesJava java = TypesJava();

    String? string = "foo";
    java.stringParams(string, string);
}