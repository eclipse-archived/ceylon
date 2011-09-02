@nomodel
class BasicTypes() {
  void m() {
    Natural localAttrNatural;
    Integer localAttrInteger;
    Float localAttrFloat;
    String localAttrString;
    Boolean localAttrBoolean;
    Character localAttrCharacter;
    
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