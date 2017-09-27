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
import java.io { File, InputStreamReader, BufferedReader }
import java.net { InetSocketAddress, URI }
import java.nio.channels { ServerSocketChannel }
import java.nio.file { 
    FileSystems,
    Files, 
    Paths, 
    StandardCopyOption
}
import java.util { HashMap }

@noanno
void methods() {
    File f1 = File("file1");
    File f2 = File("file2");
    print(f1.canRead());
    Integer cmp = f1.compareTo(f2);
    f1.listFiles();
}

@noanno
void overloadedMethodsAndSubClasses() {
    JavaWithOverloadedMembersSubClass inst = JavaWithOverloadedMembersSubClass();
    inst.method();
    inst.method(1);
    inst.method(1, 2);
    inst.topMethod();
}

@noanno
void overloadedConstructors() {
    JavaWithOverloadedMembersSubClass inst = JavaWithOverloadedMembersSubClass();
    JavaWithOverloadedMembersSubClass inst2 = JavaWithOverloadedMembersSubClass(2);
}

@noanno
class OverloadedMembersAndSubClasses() extends JavaWithOverloadedMembersSubClass() {
    void test(){
        method();
        method(1);
        method(1, 2);
        topMethod();
    }
}

@noanno
class OverloadedMembersAndSubClasses2() extends JavaWithOverloadedMembersSubClass(2) {
}

@noanno
void variadicMethods() {
    T box<T>(T t){return t;}
    TypesJava java = TypesJava();

    java.variadicBoolean(true, box(false), true);
    java.variadicBoolean(*[]);
    java.variadicBoolean(*[true, box(false), true]);
    java.variadicBoolean(true, box(false), *[true, box(false), true]);

    java.variadicCeylonBoolean(true, box(false), true);
    java.variadicCeylonBoolean(*[]);
    java.variadicCeylonBoolean(*[true, box(false), true]);

    java.variadicChar('a', box('b'), 'c');
    java.variadicChar(*[]);
    java.variadicChar(*['a', box('b'), 'c']);

    java.variadicByte(1.byte, box(2.byte), 3.byte);
    java.variadicByte(*[]);
    java.variadicByte(*[1.byte, box(2.byte), 3.byte]);
    java.variadicByte(1.byte, *[1.byte, box(2.byte), 3.byte]);
    java.variadicByte(1.byte, box(2.byte), *[1.byte, box(2.byte), 3.byte]);

    java.variadicShort(*[]);
    java.variadicShort(*[1, box(2), 3]);

    java.variadicInt(*[]);
    java.variadicInt(*[1, box(2), 3]);

    java.variadicLong(1, box(2), 3);
    java.variadicLong(1);
    java.variadicLong(box(1));
    java.variadicLong();
    java.variadicLong(*[]);
    java.variadicLong(*[1, box(2), 3]);
    Integer[] empty = [];
    Integer[] full = [1, 2];
    java.variadicLong(*empty);
    java.variadicLong(*full);

    java.variadicFloat(*[]);
    java.variadicFloat(1.0, box(2.0), 3.0);
    java.variadicFloat(*[1.0, box(2.0), 3.0]);
    java.variadicFloat(1.0, *[1.0, box(2.0), 3.0]);
    java.variadicFloat(1.0, box(2.0), *[1.0, box(2.0), 3.0]);

    java.variadicDouble(*[]);
    java.variadicDouble(*[1.0, box(2.0), 3.0]);

    java.variadicJavaString("a", box("b"), "c");
    java.variadicJavaString("a");
    java.variadicJavaString(box("a"));
    java.variadicJavaString();
    String? string = "a";
    java.variadicJavaString(string);
    java.variadicJavaString(*[]);
    java.variadicJavaString(*["a", box("b"), "c"]);
    java.variadicJavaString("a", box("b"), *["a", box("b"), "c"]);
    {String*} vals = {"a"};
    java.variadicJavaString(*vals);

    java.variadicCeylonString("a", box("b"), "c");
    java.variadicCeylonString(*[]);
    java.variadicCeylonString(*["a", box("b"), "c"]);
    String[] emptyString = [];
    String[] fullString = ["a", "b"];
    java.variadicCeylonString(*emptyString);
    java.variadicCeylonString(*fullString);

    java.variadicObject("a", box("b"), 1, box(2));
    java.variadicObject(*[]);
    java.variadicObject(*["a", box("b"), 1, box(2)]);

    java.variadicObject(JString("a"), JInteger(1));
    java.variadicObject(*[JString("a"), JInteger(1)]);
    
    java.variadicT<Integer>(1, box(2), 3);
    java.variadicT<Integer>(*[]);
    java.variadicT<Integer>(*[1, box(2), 3]);
    Integer[] sequence = [1, box(2), 3];
    java.variadicT<Integer>(*sequence);
    java.variadicT<Integer>(1, box(2), *sequence);

    java.variadicT<String>("a", box("b"), "c");
    java.variadicT<String>(*vals);
    TypesJava2<String> java2 = TypesJava2<String>();
    java2.variadicT(*vals);
    
    java.variadicObject(for (i in 1..2) i);
    java.variadicInt(for (i in 1..2) i);
}

@noanno
void tupleSpreading(){
    TypesJava java = TypesJava();
    value tuple = [true, 1.byte, 2, 3, 4, 5.0, 6.0, 'a', "foo", java];
    java.takeAll(true, 1.byte, 2, 3, 4, 5.0, 6.0, 'a', "foo", java);
    // full spread
    java.takeAll(*tuple);
    // partial spread
    java.takeAll(true, *tuple.rest);
    value tuple2 = [true, 1.byte, 'a', 'b', 'c'];
    // full spread including variadic
    java.takeAllVariadic(*tuple2);
    // partial spread including variadic
    value tuple3 = ['c', 'd'];
    java.takeAllVariadic(true, 1.byte, 'a', 'b', *tuple3);
}

@noanno
void comprehensions(){
    TypesJava java = TypesJava();
    java.variadicFloat(*{for (i in 1..2) 2.0});
    java.variadicFloat(1.0, *{for (i in 1..2) 2.0});
    java.variadicFloat(*[for (i in 1..2) 2.0]);
    java.variadicFloat(1.0, *[for (i in 1..2) 2.0]);
}

@noanno
void moreOverloads(){
    FileSystems.newFileSystem(URI.create(""), HashMap<JString,Object>());
    BufferedReader(InputStreamReader(null));
    ServerSocketChannel.open().bind(InetSocketAddress("",1));
    Files.copy(Paths.get(URI("", "", "")), Paths.get(URI("")), StandardCopyOption.\iATOMIC_MOVE, 
            *{StandardCopyOption.\iREPLACE_EXISTING, StandardCopyOption.\iCOPY_ATTRIBUTES});

}

@noanno
void overloadsWithPrimitives(){
    TypesJava java = TypesJava();
    value overload = JavaWithOverloadedMembers<Object>();
    
    overload.overloadedPrimitive(1, 2);
    overload.overloadedPrimitive(java.return_short(), 2);
    overload.overloadedPrimitive(java.return_int(), 2);
    overload.overloadedPrimitive(java.return_long(), 2);

    overload.overloadedPrimitiveVariadic(1, 2);
    // call long...
    overload.overloadedPrimitiveVariadic(java.return_short(), 2);
    // call short...
    overload.overloadedPrimitiveVariadic(java.return_short());
    overload.overloadedPrimitiveVariadic(java.return_int(), 2);
    overload.overloadedPrimitiveVariadic(java.return_int());
    overload.overloadedPrimitiveVariadic(java.return_long(), 2);
    overload.overloadedPrimitiveVariadic(java.return_long());

    Integer a = overload.overloadedPrimitive2(1);
    Boolean b = overload.overloadedPrimitive2(true);
}
