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
import java.lang { ObjectArray, JCharacter = Character }

@noanno
abstract class VariadicImplementations1() extends TypesJava2<String>() {
    shared actual void variadicT(String?* vars){}
}

@noanno
abstract class VariadicImplementations2() extends TypesJava() {
    shared actual Boolean variadicBooleanRet(Boolean* vars) => vars.first else true;

    shared actual void variadicBoolean(Boolean* vars){
        value vars2 = vars;
        variadicBoolean(*vars);
        variadicBoolean();
        variadicBoolean(true);
        variadicBoolean(true, false);
    }
    shared actual void variadicCeylonBoolean(Boolean?* vars){
        value vars2 = vars;
        variadicCeylonBoolean(*vars);
        variadicCeylonBoolean();
        variadicCeylonBoolean(true);
        variadicCeylonBoolean(true, false);
    }

    shared actual void variadicChar(Character* vars){
        value vars2 = vars;
        variadicChar(*vars);
        variadicChar();
        super.variadicChar('a');
        variadicChar('a', 'b');
    }
    
    shared actual void variadicByte(Byte* vars){
        value vars2 = vars;
        variadicByte(*vars);
        variadicByte();
        variadicByte(0.byte);
        variadicByte(0.byte, 1.byte);
    }
    shared actual void variadicShort(Integer* vars){
        value vars2 = vars;
        variadicShort(*vars);
        variadicShort();
        variadicShort(1);
        variadicShort(1, 2);
    }
    shared actual void variadicInt(Integer* vars){
        value vars2 = vars;
        variadicInt(*vars);
        variadicInt();
        variadicInt(1);
        variadicInt(1, 2);
    }
    shared actual void variadicLong(Integer* vars){
        value vars2 = vars;
        variadicLong(*vars);
        variadicLong();
        variadicLong(1);
        variadicLong(1, 2);
    }
    shared actual void variadicCeylonInteger(Integer?* vars){
        value vars2 = vars;
        variadicCeylonInteger(*vars);
        variadicCeylonInteger();
        variadicCeylonInteger(1);
        variadicCeylonInteger(1, 2);
    }
    
    shared actual void variadicFloat(Float* vars){
        value vars2 = vars;
        variadicFloat(*vars);
        variadicFloat();
        variadicFloat(1.0);
        variadicFloat(1.0, 2.0);
    }
    shared actual void variadicDouble(Float* vars){
        value vars2 = vars;
        variadicDouble(*vars);
        variadicDouble();
        variadicDouble(1.0);
        variadicDouble(1.0, 2.0);
    }
    shared actual void variadicCeylonFloat(Float?* vars){
        value vars2 = vars;
        variadicCeylonFloat(*vars);
        variadicCeylonFloat();
        variadicCeylonFloat(1.0);
        variadicCeylonFloat(1.0, 2.0);
    }
        
    shared actual void variadicJavaString(String?* vars){
        value vars2 = vars;
        variadicJavaString(*vars);
        variadicJavaString();
        variadicJavaString("a");
        variadicJavaString("a", "b");
    }
    shared actual void variadicCeylonString(String?* vars){
        value vars2 = vars;
        variadicCeylonString(*vars);
        variadicCeylonString();
        variadicCeylonString("a");
        variadicCeylonString("a", "b");
    }

    shared actual void variadicObject(Object?* vars){
        value vars2 = vars;
        variadicObject(*vars);
        variadicObject();
        variadicObject("a");
        variadicObject("a", "b");
    }
//    //shared actual void variadicT<T> (T?* vars)
//    //    given T satisfies Object {}// ERROR: uses missing reified param
//
    shared actual  void variadicObjectArray(ObjectArray<Object>?* vars){
        value vars2 = vars;
        variadicObjectArray(*vars);
        variadicObjectArray();
        variadicObjectArray(ObjectArray<Object>(1));
        variadicObjectArray(ObjectArray<Object>(1), ObjectArray<Object>(1));
    }
}