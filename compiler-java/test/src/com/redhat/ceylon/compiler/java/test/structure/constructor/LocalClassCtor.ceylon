@noanno
void localClassCtor(Integer i) {
    abstract class LocalClassCtor {
        Integer sum;
        shared new (Integer j) {
            sum = i+j;
        }
        shared new other(Integer j) {
            sum = i+j;
        }
    }
    class LocalClassCtorSub extends LocalClassCtor {
        shared new (Integer j) extends LocalClassCtor(j) {
            
        }
        shared new other(Integer j) extends super.other(j){
            
        }
    }
    LocalClassCtorSub(0);
    LocalClassCtorSub(0);
    LocalClassCtorSub.other(0);
}