@noanno
class ChainingInit<T>(Integer i) {
    
}
@noanno
class ChainingCtor<T> extends ChainingInit<T> {
    shared new (Integer i) extends ChainingInit<T>(i) {
        
    }
    shared new nonDefault(Integer i) extends ChainingInit<T>(i) {
        
    }
}
@noanno
class ChainingCtorCtor2<T> extends ChainingCtor<T> {
    shared new (Integer i) extends ChainingCtor<T>(i) {
    }
    new nonDefault(Integer i) extends ChainingCtor<T>.nonDefault(i) {
    }
    
}
@noanno
class ChainingCtorInit1<T>(Integer i) extends ChainingCtor<T>(i) {

}
@noanno
class ChainingCtorInit2<T>(Integer i) extends ChainingCtor<T>(i) {
    
}
@noanno
class ChainingCtorInit3<T>(Integer i) extends ChainingCtor<T>.nonDefault(i) {
    
}
@noanno
class ChainingCtorCtor<T> extends ChainingCtor<T> {
    shared new (Integer i) extends ChainingCtor<T>(i) {
    }
    new nonDefault(Integer i) extends super.nonDefault(i) {
    }
    
}
