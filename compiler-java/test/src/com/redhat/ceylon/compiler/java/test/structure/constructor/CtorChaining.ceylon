@noanno
class ChainingInit<T>(Integer i) {
    
}
@noanno
class ChainingCtor<T> extends ChainingInit<T> {
    shared new (Integer i) extends ChainingInit<T>(i) {
        
    }
    shared new NonDefault(Integer i) extends ChainingInit<T>(i) {
        
    }
}
@noanno
class ChainingCtorCtor2<T> extends ChainingCtor<T> {
    shared new (Integer i) extends ChainingCtor<T>(i) {
    }
    new NonDefault(Integer i) extends ChainingCtor<T>.NonDefault(i) {
    }
    
}
@noanno
class ChainingCtorInit1<T>(Integer i) extends ChainingCtor<T>(i) {

}
@noanno
class ChainingCtorInit2<T>(Integer i) extends ChainingCtor<T>(i) {
    
}
@noanno
class ChainingCtorInit3<T>(Integer i) extends ChainingCtor<T>.NonDefault(i) {
    
}
@noanno
class ChainingCtorCtor<T> extends ChainingCtor<T> {
    shared new (Integer i) extends ChainingCtor<T>(i) {
    }
    new NonDefault(Integer i) extends super.NonDefault(i) {
    }
    
}
