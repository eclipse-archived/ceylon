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
import java.lang { JString = String, JInteger = Integer }
import java.io{File}

@nomodel
@error
void methods() {
    @error
    File f1 = File("file1");
    @error
    File f2 = File("file2");
    @error
    print(f1.canRead());
    Integer cmp = f1.compareTo(f2);
    @error
    f1.listFiles();
}

@error
@nomodel
void overloadedMethodsAndSubClasses() {
    @error
    JavaWithOverloadedMembersSubClass inst = JavaWithOverloadedMembersSubClass();
    @error
    inst.method();
    @error
    inst.method(1);
    @error
    inst.method(1, 2);
    @error
    inst.topMethod();
}

@error
@nomodel
void overloadedConstructors() {
    @error
    JavaWithOverloadedMembersSubClass inst = JavaWithOverloadedMembersSubClass();
    @error
    JavaWithOverloadedMembersSubClass inst2 = JavaWithOverloadedMembersSubClass(2);
}

@error
@nomodel
class OverloadedMembersAndSubClasses() extends JavaWithOverloadedMembersSubClass() {
    void test(){
        @error
        method();
        @error
        method(1);
        @error
        method(1, 2);
        @error
        topMethod();
    }
}

@error
@nomodel
class OverloadedMembersAndSubClasses2() extends JavaWithOverloadedMembersSubClass(2) {
}

@nomodel
@error
void variadicMethods() {
    T box<T>(T t){return t;}
    @error
    TypesJava java = TypesJava();

    @error
    java.variadicBoolean(true, box(false), true);
    @error
    java.variadicBoolean(*{});
    @error
    java.variadicBoolean(*[true, box(false), true]);

    @error
    java.variadicCeylonBoolean(true, box(false), true);
    @error
    java.variadicCeylonBoolean(*{});
    @error
    java.variadicCeylonBoolean(*[true, box(false), true]);

    @error
    java.variadicChar(`a`, box(`b`), `c`);
    @error
    java.variadicChar(*{});
    @error
    java.variadicChar(*[`a`, box(`b`), `c`]);

    @error
    java.variadicByte(1, box(2), 3);
    @error
    java.variadicByte(*{});
    @error
    java.variadicByte(*[1, box(2), 3]);

    @error
    java.variadicShort(*{});
    @error
    java.variadicShort(*[1, box(2), 3]);

    @error
    java.variadicInt(*{});
    @error
    java.variadicInt(*[1, box(2), 3]);

    @error
    java.variadicLong(1, box(2), 3);
    @error
    java.variadicLong(1);
    @error
    java.variadicLong(box(1));
    @error
    java.variadicLong();
    @error
    java.variadicLong(*{});
    @error
    java.variadicLong(*[1, box(2), 3]);
    Integer[] empty = {};
    Integer[] full = [1, 2];
    @error
    java.variadicLong(*empty);
    @error
    java.variadicLong(*full);

    @error
    java.variadicFloat(*{});
    @error
    java.variadicFloat(*[1.0, box(2.0), 3.0]);

    @error
    java.variadicDouble(*{});
    @error
    java.variadicDouble(*[1.0, box(2.0), 3.0]);

    @error
    java.variadicJavaString("a", box("b"), "c");
    @error
    java.variadicJavaString("a");
    @error
    java.variadicJavaString(box("a"));
    @error
    java.variadicJavaString();
    String? string = "a";
    @error
    java.variadicJavaString(string);
    @error
    java.variadicJavaString(*{});
    @error
    java.variadicJavaString(*["a", box("b"), "c"]);

    @error
    java.variadicCeylonString("a", box("b"), "c");
    @error
    java.variadicCeylonString(*{});
    @error
    java.variadicCeylonString(*["a", box("b"), "c"]);
    String[] emptyString = {};
    String[] fullString = ["a", "b"];
    @error
    java.variadicCeylonString(*emptyString);
    @error
    java.variadicCeylonString(*fullString);

    @error
    java.variadicObject("a", box("b"), 1, box(2));
    @error
    java.variadicObject(*{});
    @error
    java.variadicObject(*["a", box("b"), 1, box(2)]);

    @error
    java.variadicObject(JString("a"), JInteger(1));
    @error
    java.variadicObject(*[JString("a"), JInteger(1)]);
    
    @error
    java.variadicT<Integer>(1, box(2), 3);
    @error
    java.variadicT<Integer>(*{});
    @error
    java.variadicT<Integer>(*[1, box(2), 3]);
    Integer[] sequence = [1, box(2), 3];
    @error
    java.variadicT<Integer>(*sequence);

    @error
    java.variadicT<String>("a", box("b"), "c");
}