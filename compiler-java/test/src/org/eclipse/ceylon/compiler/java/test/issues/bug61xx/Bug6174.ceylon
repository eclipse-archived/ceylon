@noanno
class Bug6174() {
    shared class Param(model, size = model.size) {
        shared String model;
        shared variable Integer size;
    }
    
    void method(Param obj) {
        print(obj.model);
    }
}