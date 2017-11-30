import ceylon.language.meta.declaration {
    ...
}
import ceylon.language.meta {
    type
}

shared [Integer, String] bug782Test = [1,"a"];

@test
shared void bug782(){
    value decl = `value bug782Test`;

    assert(is OpenClassType tupleType1 = decl.openType,
           tupleType1.declaration == `class Tuple`);
    assert(is OpenUnion tt1Element = tupleType1.typeArgumentList[0],
           tt1Element.caseTypes.find((OpenType elem) 
               => if(is OpenClassType elem) then elem.declaration == `class Integer` else false) exists,
           tt1Element.caseTypes.find((OpenType elem) 
               => if(is OpenClassType elem) then elem.declaration == `class String` else false) exists);
    assert(is OpenClassType tt1First = tupleType1.typeArgumentList[1],
           tt1First.declaration == `class Integer`);

    assert(is OpenClassType tupleType2 = tupleType1.typeArgumentList[2],
           tupleType2.declaration == `class Tuple`);
    assert(is OpenClassType tt2Element = tupleType2.typeArgumentList[1],
        tt2Element.declaration == `class String`);
    assert(is OpenClassType tt2First = tupleType2.typeArgumentList[1],
        tt2First.declaration == `class String`);

    assert(is OpenInterfaceType tupleType3 = tupleType2.typeArgumentList[2],
        tupleType3.declaration == `interface Empty`);
}