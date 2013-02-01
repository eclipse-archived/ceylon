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
shared Integer globalMethodInteger(Integer i){ return i; }
@nomodel
shared Float globalMethodFloat(Float i){ return i; }
@nomodel
shared String globalMethodString(String i){ return i; }
@nomodel
shared Boolean globalMethodBoolean(Boolean i){ return i; }
@nomodel
shared Character globalMethodCharacter(Character i){ return i; }

@nomodel
shared Integer? globalMethodOptionalInteger(Integer? i){ return i; }
@nomodel
shared Float? globalMethodOptionalFloat(Float? i){ return i; }
@nomodel
shared String? globalMethodOptionalString(String? i){ return i; }
@nomodel
shared Boolean? globalMethodOptionalBoolean(Boolean? i){ return i; }
@nomodel
shared Character? globalMethodOptionalCharacter(Character? i){ return i; }

@nomodel
shared Integer globalAttrInteger = +1;
@nomodel
shared Float globalAttrFloat = 1.0;
@nomodel
shared String globalAttrString = "a";
@nomodel
shared Boolean globalAttrBoolean = true;
@nomodel
shared Character globalAttrCharacter = 'a';

@nomodel
shared Integer? globalAttrOptionalInteger = null;
@nomodel
shared Float? globalAttrOptionalFloat = null;
@nomodel
shared String? globalAttrOptionalString = null;
@nomodel
shared Boolean? globalAttrOptionalBoolean = null;
@nomodel
shared Character? globalAttrOptionalCharacter = null;

@nomodel
class BasicTypes(Integer paramInteger,
                 Float paramFloat,
                 String paramString,
                 Boolean paramBoolean,
                 Character paramCharacter,
                 Integer? paramOptionalInteger,
                 Float? paramOptionalFloat,
                 String? paramOptionalString,
                 Boolean? paramOptionalBoolean,
                 Character? paramOptionalCharacter) {
  Integer privateNonCapturedAttrInteger;
  Float privateNonCapturedAttrFloat;
  String privateNonCapturedAttrString;
  Boolean privateNonCapturedAttrBoolean;
  Character privateNonCapturedAttrCharacter;

  Integer? privateNonCapturedAttrOptionalInteger;
  Float? privateNonCapturedAttrOptionalFloat;
  String? privateNonCapturedAttrOptionalString;
  Boolean? privateNonCapturedAttrOptionalBoolean;
  Character? privateNonCapturedAttrOptionalCharacter;

  Integer privateAttrInteger = 1;
  Float privateAttrFloat = 1.0;
  String privateAttrString = "a";
  Boolean privateAttrBoolean = true;
  Character privateAttrCharacter = 'a';

  Integer? privateAttrOptionalInteger = null;
  Float? privateAttrOptionalFloat = null;
  String? privateAttrOptionalString = null;
  Boolean? privateAttrOptionalBoolean = null;
  Character? privateAttrOptionalCharacter = null;

  shared Integer sharedAttrInteger = 1;
  shared Float sharedAttrFloat = 1.0;
  shared String sharedAttrString = "a";
  shared Boolean sharedAttrBoolean = true;
  shared Character sharedAttrCharacter = 'a';

  shared Integer? sharedAttrOptionalInteger = null;
  shared Float? sharedAttrOptionalFloat = null;
  shared String? sharedAttrOptionalString = null;
  shared Boolean? sharedAttrOptionalBoolean = null;
  shared Character? sharedAttrOptionalCharacter = null;

  void m() {
    Integer localAttrInteger = privateAttrInteger;
    Float localAttrFloat = privateAttrFloat;
    String localAttrString = privateAttrString;
    Boolean localAttrBoolean = privateAttrBoolean;
    Character localAttrCharacter = privateAttrCharacter;
    
    Integer? localAttrOptionalInteger = privateAttrOptionalInteger;
    Float? localAttrOptionalFloat = privateAttrOptionalFloat;
    String? localAttrOptionalString = privateAttrOptionalString;
    Boolean? localAttrOptionalBoolean = privateAttrOptionalBoolean;
    Character? localAttrOptionalCharacter = privateAttrOptionalCharacter;
  }

  Integer methodInteger(Integer i){ return i; }
  Float methodFloat(Float i){ return i; }
  String methodString(String i){ return i; }
  Boolean methodBoolean(Boolean i){ return i; }
  Character methodCharacter(Character i){ return i; }

  Integer? methodOptionalInteger(Integer? i){ return i; }
  Float? methodOptionalFloat(Float? i){ return i; }
  String? methodOptionalString(String? i){ return i; }
  Boolean? methodOptionalBoolean(Boolean? i){ return i; }
  Character? methodOptionalCharacter(Character? i){ return i; }
}
@nomodel
abstract class TypeParameterClass<ClassParam>(ClassParam classParam){
 shared formal ClassParam attr;
 shared formal ClassParam classParamMethod(ClassParam param);
}

@nomodel
class IntegerParameterClass(Integer classParam) extends TypeParameterClass<Integer>(classParam) {
 shared actual Integer attr = classParam;
 shared actual Integer classParamMethod(Integer param){ return param; }
}