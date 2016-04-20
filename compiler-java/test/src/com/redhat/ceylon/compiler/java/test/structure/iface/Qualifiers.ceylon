/*
 Because shared non-formal members become static, within those we need to 
 prefix other members with $this
 */
@noanno
interface Qualifiers {
    Integer x => 1;
    shared default Integer y => 2;
    shared formal Integer z;
    shared Integer sum => x+y+z;
    Integer sum2 => x+y+z;
     
    shared Integer i() => 1;
    shared Integer j() => i();
    shared Integer k(Integer n=j()) => n;
}
@noanno
interface QualifiersSub satisfies Qualifiers {
    shared Integer n(Integer m =k()) => i() + j() + m;
}