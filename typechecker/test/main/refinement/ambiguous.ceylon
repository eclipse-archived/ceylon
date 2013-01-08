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


