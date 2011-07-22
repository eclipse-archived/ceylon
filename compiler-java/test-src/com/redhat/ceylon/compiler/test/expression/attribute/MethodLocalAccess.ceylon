@nomodel
class MethodLocalAccess(){
    Natural m(){
        Natural n1 = 1;
        Natural n2 {
            return 1;        
        }
        return n1 + n2;
    }
}