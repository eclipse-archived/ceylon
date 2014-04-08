import check { check }

void testEnumerations() {
    value seq=[1,2,3]; //should be [1,2,3]
    value lcomp={for (c in "hola") c};//should be {Character*}
    value ecomp=[for (c in "hola") c];//should be [Character*]
    value s2 = {0, *seq};//{Integer*}
    value s3 = {'A', *lcomp};//remains {Character*}
    value t1=[1,2,3];//same as seq
    value t2=[0, *seq];//same as s2
    check(className(seq).startsWith("ceylon.language::Tuple"), "{1,2,3} is not a Tuple but a ``className(seq)``");
    check(!className(lcomp).startsWith("ceylon.language::Tuple"), "lazy comprehension is a Tuple ``className(lcomp)``");
    check(className(ecomp).startsWith("ceylon.language::ArraySequence"), "eager comprehension is not a Tuple but a ``className(ecomp)``");
    check(!className(s2).startsWith("ceylon.language::Tuple"), "{0,*seq} is a Tuple ``className(s2)``");
    check(!className(s3).startsWith("ceylon.language::Tuple"), "{x,*iter} is a Tuple ``className(s3)``");
    check(className(t1).startsWith("ceylon.language::Tuple"), "[1,2,3] is not a Tuple but a ``className(t1)``");
    check(className(t2).startsWith("ceylon.language::Tuple"), "[0,*seq] is not a Tuple but a ``className(t2)``");
    check(seq==t1, "{1,2,3} != [1,2,3]");
    check(className(t2)!=className(s2), "{0,*seq} != [0,*seq] ``className(t2)`` vs`` className(s2)``");
    check(seq.size==3, "seq.size!=3");
    check(lcomp.sequence.size==4, "lcomp.size!=4");
    check(ecomp.size==4, "ecomp.size!=4");
    check(s2.size==4, "s2.size!=4");
    check(s3.sequence.size==5, "s3.size!=5");
    check(t1.size==3, "t1.size!=3");
    check(t2.size==4, "t2.size!=4");
    //check(t3.size==5, "t3.size!=5");
    check(!className({*lcomp}).startsWith("ceylon.language::Tuple"), "{*comp} is not Tuple but ``className({*lcomp})``");
    check(className({*ecomp}).startsWith("ceylon.language::LazyIterable"), "{*ecomp} is not LazyIterable but ``className({*ecomp})``");
    check(className({*seq}).startsWith("ceylon.language::LazyIterable"), "{*seq} is not LazyIterable but ``className({*seq})``");
}
