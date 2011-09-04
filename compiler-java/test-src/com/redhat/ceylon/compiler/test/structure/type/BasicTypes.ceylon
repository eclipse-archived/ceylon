@nomodel
shared Integer globalMethodInteger(Integer i){ return i; }
@nomodel
shared Natural globalMethodNatural(Natural i){ return i; }
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
shared Natural? globalMethodOptionalNatural(Natural? i){ return i; }
@nomodel
shared Float? globalMethodOptionalFloat(Float? i){ return i; }
@nomodel
shared String? globalMethodOptionalString(String? i){ return i; }
@nomodel
shared Boolean? globalMethodOptionalBoolean(Boolean? i){ return i; }
@nomodel
shared Character? globalMethodOptionalCharacter(Character? i){ return i; }

@nomodel
shared Natural globalAttrNatural = 1;
@nomodel
shared Integer globalAttrInteger = +1;
@nomodel
shared Float globalAttrFloat = 1.0;
@nomodel
shared String globalAttrString = "a";
@nomodel
shared Boolean globalAttrBoolean = true;
@nomodel
shared Character globalAttrCharacter = `a`;

@nomodel
shared Natural? globalAttrOptionalNatural = null;
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
class BasicTypes(Natural paramNatural,
                 Integer paramInteger,
                 Float paramFloat,
                 String paramString,
                 Boolean paramBoolean,
                 Character paramCharacter,
                 Natural? paramOptionalNatural,
                 Integer? paramOptionalInteger,
                 Float? paramOptionalFloat,
                 String? paramOptionalString,
                 Boolean? paramOptionalBoolean,
                 Character? paramOptionalCharacter) {
  Natural privateNonCapturedAttrNatural;
  Integer privateNonCapturedAttrInteger;
  Float privateNonCapturedAttrFloat;
  String privateNonCapturedAttrString;
  Boolean privateNonCapturedAttrBoolean;
  Character privateNonCapturedAttrCharacter;

  Natural? privateNonCapturedAttrOptionalNatural;
  Integer? privateNonCapturedAttrOptionalInteger;
  Float? privateNonCapturedAttrOptionalFloat;
  String? privateNonCapturedAttrOptionalString;
  Boolean? privateNonCapturedAttrOptionalBoolean;
  Character? privateNonCapturedAttrOptionalCharacter;

  Natural privateAttrNatural = 1;
  Integer privateAttrInteger = +1;
  Float privateAttrFloat = 1.0;
  String privateAttrString = "a";
  Boolean privateAttrBoolean = true;
  Character privateAttrCharacter = `a`;

  Natural? privateAttrOptionalNatural = null;
  Integer? privateAttrOptionalInteger = null;
  Float? privateAttrOptionalFloat = null;
  String? privateAttrOptionalString = null;
  Boolean? privateAttrOptionalBoolean = null;
  Character? privateAttrOptionalCharacter = null;

  shared Natural sharedAttrNatural = 1;
  shared Integer sharedAttrInteger = +1;
  shared Float sharedAttrFloat = 1.0;
  shared String sharedAttrString = "a";
  shared Boolean sharedAttrBoolean = true;
  shared Character sharedAttrCharacter = `a`;

  shared Natural? sharedAttrOptionalNatural = null;
  shared Integer? sharedAttrOptionalInteger = null;
  shared Float? sharedAttrOptionalFloat = null;
  shared String? sharedAttrOptionalString = null;
  shared Boolean? sharedAttrOptionalBoolean = null;
  shared Character? sharedAttrOptionalCharacter = null;

  void m() {
    Natural localAttrNatural = privateAttrNatural;
    Integer localAttrInteger = privateAttrInteger;
    Float localAttrFloat = privateAttrFloat;
    String localAttrString = privateAttrString;
    Boolean localAttrBoolean = privateAttrBoolean;
    Character localAttrCharacter = privateAttrCharacter;
    
    Natural? localAttrOptionalNatural = privateAttrOptionalNatural;
    Integer? localAttrOptionalInteger = privateAttrOptionalInteger;
    Float? localAttrOptionalFloat = privateAttrOptionalFloat;
    String? localAttrOptionalString = privateAttrOptionalString;
    Boolean? localAttrOptionalBoolean = privateAttrOptionalBoolean;
    Character? localAttrOptionalCharacter = privateAttrOptionalCharacter;
  }

  Integer methodInteger(Integer i){ return i; }
  Natural methodNatural(Natural i){ return i; }
  Float methodFloat(Float i){ return i; }
  String methodString(String i){ return i; }
  Boolean methodBoolean(Boolean i){ return i; }
  Character methodCharacter(Character i){ return i; }

  Integer? methodOptionalInteger(Integer? i){ return i; }
  Natural? methodOptionalNatural(Natural? i){ return i; }
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