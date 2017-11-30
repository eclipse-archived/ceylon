abstract class GenericClass1<TT>() {
    shared formal class MemberClass(TT t){}
}

class Implementation1<T>() extends GenericClass1<T>() {
    shared actual class MemberClass(T t) extends super.MemberClass(t){}
}

interface GenericInterface2<TT> {
    shared formal class MemberClass(TT t){}
}

class Implementation2<T>() satisfies GenericInterface2<T> {
    shared actual class MemberClass(T t) extends super.MemberClass(t){}
}
