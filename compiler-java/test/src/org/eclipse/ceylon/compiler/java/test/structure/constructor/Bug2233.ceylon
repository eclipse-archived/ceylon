@noanno
class ParentConstr2233() {}

@noanno
class Test2233<F> extends ParentConstr2233 {
    //value constructorSB = StringBuilder();
    //constructorSB.append("1");
    shared F f;
    //constructorSB.append(",2");
    shared new bar(F f) extends ParentConstr2233() {
        this.f = f;
        //check(`F`==`Integer`, "#557.3");
    }
    //constructorSB.append(",3");
    
    shared new (F f) extends bar(f){
    }
    //constructorSB.append(",4");
}