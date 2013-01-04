import check { check }

void testEnumerations() {
    value seq=[1,2,3]; //should be [1,2,3]
    value lcomp={for (c in "hola") c};//should be {Character...}
    value ecomp=[for (c in "hola") c];//should be [Character...]
    value s2 = {0, seq...};//[Integer,Integer...]
    value s3 = {`A`, lcomp...};//remains {Character...}
    value t1=[1,2,3];//same as seq
    value t2=[0, seq...];//same as s2
    //value t3=[`A`,lcomp...];//should be [Character,Character...]
    check(className(seq)=="ceylon.language::Tuple", "{1,2,3} is not Tuple");
    check(className(lcomp)!="ceylon.language::Tuple", "lazy comprehension is a Tuple");
    check(className(ecomp)=="ceylon.language::Tuple", "eager comprehension is not a Tuple");
    check(className(s2)=="ceylon.language::Tuple", "{0,seq...} is not a Tuple");
    check(className(s3)!="ceylon.language::Tuple", "{x,iter...} is a Tuple");
    check(className(t1)=="ceylon.language::Tuple", "[1,2,3] is not a Tuple");
    check(className(t2)=="ceylon.language::Tuple", "[0,seq...] is not a Tuple");
    //check(className(t3)=="ceylon.language::Tuple", "[x,iter...] is not a Tuple");
    check(seq==t1, "{1,2,3} != [1,2,3]");
    check(className(t2)==className(s2), "{0,seq...} != [0,seq...]");
    check(seq.size==3, "seq.size!=3");
    check(lcomp.sequence.size==4, "lcomp.size!=4");
    check(ecomp.size==4, "ecomp.size!=4");
    check(s2.size==4, "s2.size!=4");
    check(s3.sequence.size==5, "s3.size!=5");
    check(t1.size==3, "t1.size!=3");
    check(t2.size==4, "t2.size!=4");
    //check(t3.size==5, "t3.size!=5");
    check(className({lcomp...})!="ceylon.language::Tuple", "{comp...} is Tuple");
    check(className({ecomp...})=="ceylon.language::Tuple", "{ecomp...} is not Tuple");
    check(className({seq...})=="ceylon.language::Tuple", "{seq...} is not Tuple");
}
