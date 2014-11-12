@noanno
void localClassCtor(Integer i) {
    abstract class LocalClassCtor {
        Integer sum;
        shared new LocalClassCtor(Integer j) {
            sum = i+j;
        }
        shared new Other(Integer j) {
            sum = i+j;
        }
    }
    class LocalClassCtorSub extends LocalClassCtor {
        shared new LocalClassCtorSub(Integer j) extends LocalClassCtor(j) {
            
        }
        shared new Other(Integer j) extends super.Other(j){
            
        }
    }
    LocalClassCtorSub(0);
    LocalClassCtorSub.LocalClassCtorSub(0);
    LocalClassCtorSub.Other(0);
}