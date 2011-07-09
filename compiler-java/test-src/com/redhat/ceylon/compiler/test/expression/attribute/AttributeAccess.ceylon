@nomodel
class AttributeAccess(){
    Boolean b1 = true;
    shared Boolean b2 = true;
    Boolean b3 {
        return true;
    }
    shared Boolean b4 {
        return true;
    }
   
    void m(){
        Boolean bb1 = b1;
        Boolean bb2 = b2;
        Boolean bb3 = b3;
        Boolean bb4 = b4;
    }
}