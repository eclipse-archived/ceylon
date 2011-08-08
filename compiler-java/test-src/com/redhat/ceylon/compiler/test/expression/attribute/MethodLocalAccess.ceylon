@nomodel
class MethodLocalAccess(){
    Natural m1(){
        Natural n1 = 1;
        return n1;
    }
    Natural m2(){
        Natural n2 {
            return 1;        
        }
        return n2;
    }
}