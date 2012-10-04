@nomodel
abstract class ClassDefaultMemberClassWithMemberSubclass() {
    shared default class Default(Integer i) {
    }
    shared formal class Formal(Integer i) {
    }
    shared class Shared(Integer i) {
    }
    class NonShared(Integer i) {
    }
    class DefaultSub(Integer i) extends Default(i){
    }
    class FormalSub(Integer i) extends Formal(i){
    }
    class SharedSub(Integer i) extends Shared(i){
    }
    class NonSharedSub(Integer i) extends NonShared(i){
    }
    void m() {
        class LocalDefaultSub(Integer i) extends Default(i) {
        }
        class LocalFormalSub(Integer i) extends Formal(i) {
        }
        class LocalSharedSub(Integer i) extends Shared(i) {
        }
        class LocalNonSharedSub(Integer i) extends NonShared(i) {
        }
    } 
}