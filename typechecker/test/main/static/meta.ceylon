void meta() {
    $type:"Attribute<Null,String,Nothing>"
    value hn = `ClassWithStaticMembers.name`;
    $type:"Method<Null,Anything,[String]>"
    value hh = `ClassWithStaticMembers.hello`;
    $type:"MemberClass<Null,ClassWithStaticMembers.Inner,[]>"
    value hic = `ClassWithStaticMembers.Inner`;
    $type:"MemberInterface<Null,ClassWithStaticMembers.Inter>"
    value hif = `ClassWithStaticMembers.Inter`;
}