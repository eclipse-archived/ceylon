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
    JString = String,
    BooleanArray,
    ByteArray,
    ShortArray,
    IntArray,
    LongArray,
    FloatArray,
    DoubleArray,
    CharArray,
    ObjectArray
}
import com.redhat.ceylon.compiler.java.test.interop { TypesJava }
import java.io { File }

@noanno
void testFiles() {
    File f = File(".");
    ObjectArray<File> items = f.listFiles();
    File? f2 = items.get(0);
}

// make sure we can write that
BooleanArray booleanArray({Boolean*} values){
    BooleanArray ret = BooleanArray(values.size);
    variable Integer idx = 0;
    for(val in values){
        ret.set(idx++, val);
    }
    return ret;
}

@noanno
void test_booleans() {
    TypesJava java = TypesJava();
    BooleanArray items = java.return_booleans();
    Boolean? b = items.get(0);
    //Boolean? b3 = items[0];
    if (exists b) {
        items.set(1, b);
    }
    Iterable<Character> it = "foo";
    //for (Boolean b2 in items) { print(b2); }
    java.take_booleans(items);
    java.take_booleans(booleanArray([true, true, false]));
    java.take_booleans(booleanArray{});
    BooleanArray{size=2;};
    BooleanArray(2);
    Anything(Integer) ctorRef = BooleanArray;
    Integer i = items.size;
    Array<Boolean> arr1 = items.booleanArray;
    Array<JBoolean> arr2 = items.array;

    items.copyTo(items);
    items.copyTo(items, 0, 0, items.size);
    items.copyTo{destination = items;};
    items.copyTo{destination = items; sourcePosition = 0; destinationPosition = 0;};
    
    // multi-dimensional array
    ObjectArray<BooleanArray> matrix = ObjectArray<BooleanArray>(10);
    for(x in 0:matrix.size){
        matrix.set(x, BooleanArray(2));
    }
    Array<BooleanArray?> boolArray = matrix.array;
    
    // reified stuff
    Object o = items;
    if(is BooleanArray o){
    }
    if(is ObjectArray<BooleanArray> o){
    }
    if(is ObjectArray<ObjectArray<Boolean>> o){
    }
    value cloned = items.clone();
}

@noanno
void test_JBooleans() {
    TypesJava java = TypesJava();
    ObjectArray<JBoolean> items = java.return_Booleans();
    JBoolean? b = items.get(0);
    if (exists b) {
        items.set(1, b);
    }
    //for (JBoolean b2 in items) { print(b2); }
    java.take_Booleans(items);
}

@noanno
void test_bytes() {
    TypesJava java = TypesJava();
    ByteArray items = java.return_bytes();
    Integer? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (Integer n2 in items) { print(n2); }
    java.take_bytes(items);
    
    ByteArray(2);
    Anything(Integer) ctorRef = ByteArray;
    
    value cloned = items.clone();
}

@noanno
void test_JBytes() {
    TypesJava java = TypesJava();
    ObjectArray<JByte> items = java.return_Bytes();
    JByte? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (JByte n2 in items) { print(n2); }
    java.take_Bytes(items);
}

@noanno
void test_shorts() {
    TypesJava java = TypesJava();
    ShortArray items = java.return_shorts();
    Integer? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (Integer n2 in items) { print(n2); }
    java.take_shorts(items);
    
    ShortArray(2);
    Anything(Integer) ctorRef = ShortArray;
    
    value cloned = items.clone();
}

@noanno
void test_JShorts() {
    TypesJava java = TypesJava();
    ObjectArray<JShort> items = java.return_Shorts();
    JShort? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (JShort n2 in items) { print(n2); }
    java.take_Shorts(items);
}

@noanno
void test_ints() {
    TypesJava java = TypesJava();
    IntArray items = java.return_ints();
    Integer? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (Integer n2 in items) { print(n2); }
    java.take_ints(items);
    
    IntArray(2);
    Anything(Integer) ctorRef = IntArray;
    
    value cloned = items.clone();
}

@noanno
void test_JIntegers() {
    TypesJava java = TypesJava();
    ObjectArray<JInteger> items = java.return_Integers();
    JInteger? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (JInteger n2 in items) { print(n2); }
    java.take_Integers(items);
}

@noanno
void test_longs() {
    TypesJava java = TypesJava();
    LongArray items = java.return_longs();
    Integer? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (Integer n2 in items) { print(n2); }
    java.take_longs(items);
    
    LongArray(2);
    Anything(Integer) ctorRef = LongArray;
    
    value cloned = items.clone();
}

@noanno
void test_JLongs() {
    TypesJava java = TypesJava();
    ObjectArray<JLong> items = java.return_Longs();
    JLong? n = items.get(0);
    if (exists n) {
        items.set(1, n);
    }
    //for (JLong n2 in items) { print(n2); }
    java.take_Longs(items);
}

@noanno
void test_floats() {
    TypesJava java = TypesJava();
    FloatArray items = java.return_floats();
    Float? f = items.get(0);
    if (exists f) {
        items.set(1, f);
    }
    //for (Float f2 in items) { print(f2); }
    java.take_floats(items);
    
    FloatArray(2);
    Anything(Integer) ctorRef = FloatArray;
    
    value cloned = items.clone();
}

@noanno
void test_JFloats() {
    TypesJava java = TypesJava();
    ObjectArray<JFloat> items = java.return_Floats();
    JFloat? f = items.get(0);
    if (exists f) {
        items.set(1, f);
    }
    //for (JFloat f2 in items) { print(f2); }
    java.take_Floats(items);
}

@noanno
void test_doubles() {
    TypesJava java = TypesJava();
    DoubleArray items = java.return_doubles();
    Float? f = items.get(0);
    if (exists f) {
        items.set(1, f);
    }
    //for (Float f2 in items) { print(f2); }
    java.take_doubles(items);
    
    DoubleArray(2);
    Anything(Integer) ctorRef = DoubleArray;
    
    value cloned = items.clone();
}

@noanno
void test_JDoubles() {
    TypesJava java = TypesJava();
    ObjectArray<JDouble> items = java.return_Doubles();
    JDouble? f = items.get(0);
    if (exists f) {
        items.set(1, f);
    }
    //for (JDouble f2 in items) { print(f2); }
    java.take_Doubles(items);
}

@noanno
void test_chars() {
    TypesJava java = TypesJava();
    CharArray items = java.return_chars();
    Character? c = items.get(0);
    if (exists c) {
        items.set(1, c);
    }
    //for (Character c2 in items) { print(c2); }
    java.take_chars(items);
    
    IntArray ints = java.return_ints();
    Array<JInteger> chars1 = ints.array;
    Array<Character> chars2 = ints.codePointArray;
    
    value cloned = items.clone();
}

@noanno
void test_JCharacters() {
    TypesJava java = TypesJava();
    ObjectArray<JCharacter> items = java.return_Characters();
    JCharacter? c = items.get(0);
    if (exists c) {
        items.set(1, c);
    }
    //for (JCharacter c2 in items) { print(c2); }
    java.take_Characters(items);
}

@noanno
void test_Strings() {
    TypesJava java = TypesJava();
    ObjectArray<JString> items = java.return_Strings();
    JString? s = items.get(0);
    if (exists s) {
        items.set(1, s);
    }
    //for (String s2 in items) { print(s2); }
    java.take_Strings(items);
    
    ObjectArray<String> ceylonStrings = ObjectArray<String>(0);
    Array<String?> ceylonStringArray = ceylonStrings.array;
    Array<JString?> javaStringArray = items.array;
    
    value cloned = items.clone();
}

@noanno
void test_Objects() {
    TypesJava java = TypesJava();
    ObjectArray<Object> items = java.return_Objects();
    Object? o = items.get(0);
    Object o2 = items.get(0);
    if (exists o) {
        items.set(1, o);
        items.set(1, null);
    }
    //for (Object o2 in items) { print(o2); }
    java.take_Objects(items);
    
    value cloned = items.clone();
}

