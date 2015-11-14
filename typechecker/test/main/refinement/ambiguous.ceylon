interface AB {
    shared formal Anything anything;
}

class CD() satisfies AB {
    shared actual Null anything => null;
}

class EF() satisfies AB {
    shared actual String anything => "";
}

class GH() satisfies AB {
    shared actual Object anything => "";
}

void abcdefghUnion(CD|EF cdef, EF|GH efgh, AB|GH abgh) {
    @type:"CD|EF" value v1 = cdef;
    @type:"EF|GH" value v2 = efgh;
    @type:"AB" value v3 = abgh;
    @type:"Anything" value ns = cdef.anything;
    @type:"Anything" value o = efgh.anything;
    @type:"Anything" value no = abgh.anything;
}

void abcdefghIntersection(@type:"CD&EF" CD&EF cdef, 
        @type:"EF&GH" EF&GH efgh, 
        @type:"AB&GH" AB&GH abgh) {
    @type:"Nothing" value v1 = cdef;
    @type:"Nothing" value v2 = efgh;
    @type:"GH" value v3 = abgh;
    @error ns = cdef.anything;
    @error value o = efgh.anything;
    @type:"Object" value no = abgh.anything;
}

interface IJ satisfies AB {
    shared actual Null anything => null;
}

interface KL satisfies AB {
    shared actual String anything => "";
}

interface MN satisfies AB {
    shared actual Object anything => "";
}

void ijklmnUnion(IJ|KL cdef, KL|MN efgh, AB|IJ abgh) {
    @type:"IJ|KL" value v1 = cdef;
    @type:"KL|MN" value v2 = efgh;
    @type:"AB" value v3 = abgh;
    @type:"Anything" value ns = cdef.anything;
    @type:"Anything" value o = efgh.anything;
    @type:"Anything" value no = abgh.anything;
}

void ijklmnIntersection(IJ&KL cdef, KL&MN efgh, AB&MN abgh) {
    @type:"IJ&KL" value v1 = cdef;
    @type:"KL&MN" value v2 = efgh;
    @type:"MN" value v3 = abgh;
    @type:"Anything" value ns = cdef.anything;
    @type:"Anything" value o = efgh.anything;
    @type:"Object" value no = abgh.anything;
}


interface IBase {
    shared default String someString => "IBase";
    shared default String someStringF() => "IBaseF";
}

interface ISub1 satisfies IBase {
    shared actual default String someString => "I1";
    shared actual default String someStringF() => "I1F";
}

interface ISub2 satisfies IBase {
    shared actual default String someString => "I2";
    shared actual default String someStringF() => "I2F";
}

class Parent() satisfies IBase {
    shared actual default String someString = "parentClass";
    shared actual default String someStringF() => "parentClassF";
}

class Bad() extends Parent() satisfies ISub1 & ISub2 {
    @error shared actual default String someString => super.someString;
    @error shared actual default String someStringF() => super.someStringF();
}

class Good() extends Parent() satisfies ISub1 & ISub2 {
    shared actual default String someString => (super of Parent).someString;
    shared actual default String someStringF() => (super of Parent).someStringF();
}

shared
void runIt() {
    print(Bad().someString + Bad().someStringF());
    print(Good().someString + Good().someStringF());
}
