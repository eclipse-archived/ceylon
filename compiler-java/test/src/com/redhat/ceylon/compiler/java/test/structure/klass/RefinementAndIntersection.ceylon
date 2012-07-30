@nomodel
interface RefinementAndIntersection_G {
    shared void g(){}
}
@nomodel 
interface RefinementAndIntersection_H {
    shared void h(){}
}
@nomodel
interface RefinementAndIntersection_Co<out T> {
    formal shared T get();
}
@nomodel
class RefinementAndIntersection_SuperCoGood() satisfies RefinementAndIntersection_Co<RefinementAndIntersection_G> {
    default shared actual RefinementAndIntersection_G get() { return bottom; }
}
@nomodel
class RefinementAndIntersection_SubCoGood() extends RefinementAndIntersection_SuperCoGood() satisfies RefinementAndIntersection_Co<RefinementAndIntersection_H> {
    default shared actual RefinementAndIntersection_H&RefinementAndIntersection_G get() { return bottom; }
}

@nomodel
void refinementAndIntersection_method(){
    RefinementAndIntersection_SubCoGood sub = RefinementAndIntersection_SubCoGood();
    // make sure we can call methods of H
    sub.get().g();
    sub.get().h();
}