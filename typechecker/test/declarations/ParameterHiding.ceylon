interface ParameterHiding {

    class Hiding(String name) {
        shared String name = name;
    }
    
    class HidingWithTypeConstraint<T>(String name)
            given T(String name) {
        shared String name = name;
        shared T t = T(name);
    }
    
    class AdvancedHiding(Float x, Float y) {
        @error shared String x = x.string;
        @error shared variable Float y := y;
        @type["Float"] value f = x;
    }
    
    void advancedHiding() {
        @type["String"] value s = AdvancedHiding(1.0, 2.0).x;
        @type["Float"] value f = AdvancedHiding(1.0, 2.0).y;
    }
    
}
