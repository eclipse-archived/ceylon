class MajikNullaryCtor(shared Integer i,
    shared Character c,
    shared Boolean b,
    shared Byte o,
    shared Float f) {
    // generated
}
abstract class MajikNullaryCtorAbstract(Integer i) {
    // generated
}
class MajikNullaryCtorNullary() {
    // not generated in this case: there's already a nullary ctor
}
class MajikNullaryCtorNullarySuper(Integer i) extends MajikNullaryCtorNullary() {
    // generated: there's a super constructor to delegate to
}

class MajikNullaryCtorDefaulted(Integer i = 0) {
    // not generated in this case: there's already a nullary ctor
}

class MajikNullaryCtorDefaultedSub(Integer j) extends MajikNullaryCtorDefaulted(0){
    // generated: there's a super constructor to delegate to
}

class MajikNullaryCtorSequenced(shared Integer* va) {
    // not generated in this case: there's already a nullary ctor
}
class MajikNullaryCtorSequencedNonempty(shared Integer+ va) {
    // generated
}

class MajikNullaryCtorGeneric<T>(shared default Integer i) {
    // generated, but it's not nullary (it's for subclasses)
}

class MajikNullaryCtorGenericSub(shared actual Integer i) extends MajikNullaryCtorGeneric<String>(i){
    // generated
}

class MajikNullaryCtorMixin(shared Integer i) satisfies Category<String> {
    shared actual Boolean contains(String element) => true;
    // generated, but companions must not be null!
}
class MajikNullaryCtorMixinSub(Integer i) extends MajikNullaryCtorMixin(i) satisfies Category<String> {
}

class MajikNullaryCtorValueCtor {
    shared new val {}
    // not generated in this case
}

class MajikNullaryCtorValueCtorAndDefault {
    shared new() {}
    shared new val {}
    // generated
}

class MajikNullaryCtorValueCtorAndCallable {
    shared new callable() {}
    shared new val {}
    // generated
}

abstract class MajikNullaryCtorValueCtorOf(Integer i) of majikNullaryCtorValueCtorOf{
    // not generated in this case
    
}
object majikNullaryCtorValueCtorOf extends MajikNullaryCtorValueCtorOf(1) {
    
}

serializable class MajikNullaryCtorSer(Integer i) {
     
}
//serializable class MajikNullaryCtorSerGeneric<T>(Integer i) {
    
//}
//serializable class MajikNullaryCtorSerMixin(Integer i) satisfies Category<String> {
    
//}

/*
class MajikNullaryCtorInterop1(Integer i) extends JavaWithNullary {
    
}

class MajikNullaryCtorInterop2(Integer i) extends JavaWithoutNullary {
    
}*/