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
    JInteger = Integer,
    JLong = Long,
    JFloat = Float,
    JDouble = Double,
    JCharacter = Character
}
 
@nomodel
@error
void returnTypes() {
    @error
    TypesJava java = TypesJava();
    Boolean b1 = java.return_boolean();
    Boolean b2 = java.return_Boolean().booleanValue();
    Integer n1 = fromJavaInt(java.return_int());
    Integer n2 = fromJavaInt(java.return_Integer());
    Integer n3 = java.return_long();
    Integer n4 = java.return_Long().longValue();
    Float f1 = fromJavaFloat(java.return_float());
    Float f2 = fromJavaFloat(java.return_Float());
    Float f3 = java.return_double();
    Float f4 = java.return_Double().doubleValue();
    Character c1 = fromJavaChar(java.return_char());
    Character c2 = fromJavaChar(java.return_Character());
    String s = java.return_String();
    Object o = java.return_Object();
}

@nomodel
T box<T>(T t){
    return t;
}

@nomodel
@error
void parameterTypes() {
    @error
    TypesJava java = TypesJava();
    @error
    java.booleanParams(true, JBoolean(true), true);
    @error
    java.intParams(toJavaInt(1), toJavaInt(1));
    @error
    java.longParams(1, JLong(1), 1);
    @error
    java.floatParams(toJavaFloat(1.0), toJavaFloat(1.0));
    @error
    java.doubleParams(1.0, JDouble(1.0), 1.0);
    @error
    java.charParams(toJavaChar(`a`), toJavaChar(`a`), `a`);
    @error
    java.charParams(toJavaChar(box(`a`)), toJavaChar(box(`a`)), box(`a`));
    @error
    java.stringParams("a", "a");
    @error
    java.stringParams(box("a"), box("a"));
    @error
    java.objectParams(java);
}

@nomodel
@error
void juggle() {
    @error
    TypesJava java = TypesJava();
    @error
    JInteger n1 = java.return_int();
    @error
    java.intParams(n1, n1);
    @error
    JInteger n2 = box(n1);
}