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

void abcdefgh(CD|EF cdef, EF|GH efgh, AB|GH abgh) {
    @type:"Anything" value ns = cdef.anything;
    @type:"Anything" value o = efgh.anything;
    @type:"Anything" value no = abgh.anything;
}