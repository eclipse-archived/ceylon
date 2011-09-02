@nomodel
class BasicTypes() {
  Natural privateNonCapturedAttrNatural;
  Integer privateNonCapturedAttrInteger;
  Float privateNonCapturedAttrFloat;
  String privateNonCapturedAttrString;
  Boolean privateNonCapturedAttrBoolean;
  Character privateNonCapturedAttrCharacter;

  Natural privateAttrNatural = 1;
  Integer privateAttrInteger = +1;
  Float privateAttrFloat = 1.0;
  String privateAttrString = "a";
  Boolean privateAttrBoolean = true;
  Character privateAttrCharacter = `a`;

  shared Natural sharedAttrNatural = 1;
  shared Integer sharedAttrInteger = +1;
  shared Float sharedAttrFloat = 1.0;
  shared String sharedAttrString = "a";
  shared Boolean sharedAttrBoolean = true;
  shared Character sharedAttrCharacter = `a`;

  void m() {
    Natural localAttrNatural = privateAttrNatural;
    Integer localAttrInteger = privateAttrInteger;
    Float localAttrFloat = privateAttrFloat;
    String localAttrString = privateAttrString;
    Boolean localAttrBoolean = privateAttrBoolean;
    Character localAttrCharacter = privateAttrCharacter;
    
    Natural? localAttrOptionalNatural;
    Integer? localAttrOptionalInteger;
    Float? localAttrOptionalFloat;
    String? localAttrOptionalString;
    Boolean? localAttrOptionalBoolean;
    Character? localAttrOptionalCharacter;
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