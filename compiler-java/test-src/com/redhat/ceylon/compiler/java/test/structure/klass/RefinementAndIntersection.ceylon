@nomodel
interface RefinementAndIntersection_G {}
@nomodel 
interface RefinementAndIntersection_H {}
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
    default shared actual RefinementAndIntersection_H&RefinementAndIntersection_G get() { return bottom; } //error!
}