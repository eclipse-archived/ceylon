interface ParameterHiding {

    class Hiding(String name) {
        shared String name = name;
    }
    
    class HidingWithTypeConstraint<T>(String name)
            given T(String name) {
        shared String name = name;
        shared T t = T(name);
    }
    
    class AdvancedHiding(Float x) {
        shared String x = x.string;
        @type["Float"] value f = x;
    }
    
    void advancedHiding() {
        @type["String"] value s = AdvancedHiding(1.0).x;
    }
    
}
