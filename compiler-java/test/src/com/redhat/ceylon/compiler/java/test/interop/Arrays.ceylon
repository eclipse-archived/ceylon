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
    JCharacter = Character
}
import com.redhat.ceylon.compiler.java.test.interop { TypesJava }
import java.io { File }

@nomodel
void testFiles() {
    File f = File(".");
    Array<File> items = f.listFiles();
    File? f2 = items.item(0);
}

@nomodel
void test_booleans() {
    TypesJava java = TypesJava();
    Array<Boolean> items = java.return_booleans();
    Boolean? b = items.item(0);
    Boolean? b3 = items[0];
    if (exists b) {
        items.setItem(1, b);
    }
    for (Boolean b2 in items) { print(b2); }
    java.take_booleans(items);
    java.take_booleans(array(true, true, false));
    java.take_booleans(array({true, true, false}...));
    java.take_booleans(array<Boolean>());
}

@nomodel
void test_JBooleans() {
    TypesJava java = TypesJava();
    Array<JBoolean> items = java.return_Booleans();
    JBoolean? b = items.item(0);
    JBoolean? b3 = items[0];
    if (exists b) {
        items.setItem(1, b);
    }
    for (JBoolean b2 in items) { print(b2); }
    java.take_Booleans(items);
}

@nomodel
void test_bytes() {
    TypesJava java = TypesJava();
    Array<Integer> items = java.return_bytes();
    Integer? n = items.item(0);
    Integer? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (Integer n2 in items) { print(n2); }
    java.take_bytes(items);
}

@nomodel
void test_JBytes() {
    TypesJava java = TypesJava();
    Array<JByte> items = java.return_Bytes();
    JByte? n = items.item(0);
    JByte? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (JByte n2 in items) { print(n2); }
    java.take_Bytes(items);
}

@nomodel
void test_shorts() {
    TypesJava java = TypesJava();
    Array<Integer> items = java.return_shorts();
    Integer? n = items.item(0);
    Integer? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (Integer n2 in items) { print(n2); }
    java.take_shorts(items);
}

@nomodel
void test_JShorts() {
    TypesJava java = TypesJava();
    Array<JShort> items = java.return_Shorts();
    JShort? n = items.item(0);
    JShort? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (JShort n2 in items) { print(n2); }
    java.take_Shorts(items);
}

@nomodel
void test_ints() {
    TypesJava java = TypesJava();
    Array<Integer> items = java.return_ints();
    Integer? n = items.item(0);
    Integer? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (Integer n2 in items) { print(n2); }
    java.take_ints(items);
}

@nomodel
void test_JIntegers() {
    TypesJava java = TypesJava();
    Array<JInteger> items = java.return_Integers();
    JInteger? n = items.item(0);
    JInteger? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (JInteger n2 in items) { print(n2); }
    java.take_Integers(items);
}

@nomodel
void test_longs() {
    TypesJava java = TypesJava();
    Array<Integer> items = java.return_longs();
    Integer? n = items.item(0);
    Integer? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (Integer n2 in items) { print(n2); }
    java.take_longs(items);
    java.take_longs(array(1, 2, 3));
    java.take_longs(array({1, 2, 3}...));
    java.take_longs(array<Integer>());
}

@nomodel
void test_JLongs() {
    TypesJava java = TypesJava();
    Array<JLong> items = java.return_Longs();
    JLong? n = items.item(0);
    JLong? n3 = items[0];
    if (exists n) {
        items.setItem(1, n);
    }
    for (JLong n2 in items) { print(n2); }
    java.take_Longs(items);
}

@nomodel
void test_floats() {
    TypesJava java = TypesJava();
    Array<Float> items = java.return_floats();
    Float? f = items.item(0);
    Float? f3 = items[0];
    if (exists f) {
        items.setItem(1, f);
    }
    for (Float f2 in items) { print(f2); }
    java.take_floats(items);
}

@nomodel
void test_JFloats() {
    TypesJava java = TypesJava();
    Array<JFloat> items = java.return_Floats();
    JFloat? f = items.item(0);
    JFloat? f3 = items[0];
    if (exists f) {
        items.setItem(1, f);
    }
    for (JFloat f2 in items) { print(f2); }
    java.take_Floats(items);
}

@nomodel
void test_doubles() {
    TypesJava java = TypesJava();
    Array<Float> items = java.return_doubles();
    Float? f = items.item(0);
    Float? f3 = items[0];
    if (exists f) {
        items.setItem(1, f);
    }
    for (Float f2 in items) { print(f2); }
    java.take_doubles(items);
    java.take_doubles(array(1.0, 2.0, 3.0));
    java.take_doubles(array({1.0, 2.0, 3.0}...));
    java.take_doubles(array<Float>());
}

@nomodel
void test_JDoubles() {
    TypesJava java = TypesJava();
    Array<JDouble> items = java.return_Doubles();
    JDouble? f = items.item(0);
    JDouble? f3 = items[0];
    if (exists f) {
        items.setItem(1, f);
    }
    for (JDouble f2 in items) { print(f2); }
    java.take_Doubles(items);
}

@nomodel
void test_chars() {
    TypesJava java = TypesJava();
    Array<Character> items = java.return_chars();
    Character? c = items.item(0);
    Character? c3 = items[0];
    if (exists c) {
        items.setItem(1, c);
    }
    for (Character c2 in items) { print(c2); }
    java.take_chars(items);
    java.take_chars(array(`a`, `b`, `c`));
    java.take_chars(array({`a`, `b`, `c`}...));
    java.take_chars(array<Character>());
}

@nomodel
void test_JCharacters() {
    TypesJava java = TypesJava();
    Array<JCharacter> items = java.return_Characters();
    JCharacter? c = items.item(0);
    JCharacter? c3 = items[0];
    if (exists c) {
        items.setItem(1, c);
    }
    for (JCharacter c2 in items) { print(c2); }
    java.take_Characters(items);
}

@nomodel
void test_Strings() {
    TypesJava java = TypesJava();
    Array<String> items = java.return_Strings();
    String? s = items.item(0);
    String? s3 = items[0];
    if (exists s) {
        items.setItem(1, s);
    }
    for (String s2 in items) { print(s2); }
    java.take_Strings(items);
    java.take_Strings(array("aap", "noot", "mies"));
    java.take_Strings(array({"aap", "noot", "mies"}...));
    java.take_Strings(array<String>());
}

@nomodel
void test_Objects() {
    TypesJava java = TypesJava();
    Array<Object> items = java.return_Objects();
    Object? o = items.item(0);
    if (exists o) {
        items.setItem(1, o);
    }
    for (Object o2 in items) { print(o2); }
    java.take_Objects(items);
    // FIXME Shouldn't this work?
    //java.take_Objects(array<Object>("aap", "noot", "mies"));
    //java.take_Objects(arrayOfSome<Object>({"aap", "noot", "mies"}));
    //java.take_Objects(arrayOfNone<Object>());
}

