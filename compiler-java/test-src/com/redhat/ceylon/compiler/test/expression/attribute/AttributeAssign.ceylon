@nomodel
class AttributeAssign(){
    variable Boolean b1 := true;
    variable shared Boolean b2 := true;
    Boolean b3 {
        return true;
    }
    assign b3 {
    }
    shared Boolean b4 {
        return true;
    }
    assign b4 {
    }
   
    void m(){
        b1 := false;
        b2 := false;
        b3 := false;
        b4 := false;
    }
}