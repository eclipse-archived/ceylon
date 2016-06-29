@noanno
class Bug6174() {
    shared class Param(model, size = model.size) {
        shared variable String model;
        shared variable Integer size;
    }
    
    void method(Param obj) {
        print(obj.model);
    }
}