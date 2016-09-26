@noanno
class StaticObject {
    
    static shared object o {
        
    }
    
    shared new () {}
}
@noanno
void staticObject() {
    value ob = StaticObject.o;
}