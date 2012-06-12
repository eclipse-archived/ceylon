@nomodel
abstract class TypeFamilyGeneric<N>() 
    given N satisfies Node {

    shared formal class Node() of N {
    }

}

@nomodel
interface Inv<T> {}

@nomodel
void typeFamilyGeneric<X>() given X satisfies TypeFamilyGeneric<X>.Node{
    Inv<TypeFamilyGeneric<X>.Node> l1 { throw; }
    Inv<X> l2 { throw; }
    Inv<TypeFamilyGeneric<X>.Node> l3 = l2;
    Inv<X> l4 = l1;
}