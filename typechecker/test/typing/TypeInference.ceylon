interface TypeInference {
    
    class Inference() {
    
        @type["String"] function inferredMethod() {
            return "Hello";
        }
        
        @type["String"] value inferredGetter {
            return "Hello";
        }
        
        @type["String"] value inferredAttribute = "Hello";
        
        /*@error value brokenInferredMethod() {
            return "Hello";
        }*/
        
        @error function brokenInferredGetter {
            return "Hello";
        }
        
        @error function brokenInferredAttribute = "Hello";
        
    }
    
    class ForwardTypeInference(){
        function test(){
            return 0;    
        }
        void m(){
            @type["Natural"] value n = test();
        }
        @type["Natural"] function f() {
            return test();
        }
    }
    
    class BackwardTypeInference(){
        void m(){
            @type["Void"] @error value n = test();
        }
        @type["Void"] function f() {
            @error return test();
        }
        function test(){
            return 0;    
        }
    }
    
    class BrokenInference() {
        
        @error value x;
        
        @error value y {}
        
        @error function f() {}
        
        @error function g();
        
        @error class C();
        
        @error interface I;
        
    }

    class UnknownInference() {
        
        @error @type["Void"] value x = burp;
        
        @type["Void"] value y {
            @error return burp;
        }
        
        @type["Void"] function f() {
            @error return burp;
        }
        
        @error @type["Void"] function g() = burp;
        
        @error @type["Sequence<Void>"] value seq = { burp };
        
        @type["Sequence<Void>"] function createSeq() {
            @error @type["Sequence<Void>"] return { hi };
        }
        
        Sequence<T> singleton<T>(T element) {
            return {element};
        }
        
        @type["Sequence<Void>"] @error value sing = singleton(hi);
        
        value hi = "hi";
        
    }
    
    class MultipleReturnTypeInference() {
        
        @type["String|Natural"] function inferredMethod() {
            if (true) {
                return "Hello";
            }
            else {
                return 0;
            }
        }
        
        @type["String|Integer"] value inferredGetter {
            if (true) {
                return "Hello";
            }
            else {
                return +1;
            }
        }
        
        @type["String|Natural|Integer"] function method() {
            if (1==0) {
                return inferredMethod();
            }
            else {
                return inferredGetter;
            }
        }
        
        @type["Bottom"] function f() {
            throw;
        }
        
        @type["Bottom"] value v {
            throw;
        }
        
    }
    
    class NamedArgTypeInference() {
        function join(String x, String y, String f(String z)) {
            return f(x+" "+y);
        }
        @type["String"] value hw = join {
            value x { return "Hello"; }
            value y { return "Hello"; }
            function f(String z) { return z.uppercase; }
        };
        @type["String"] value broken = join {
            value x { return "Hello"; }
            @error value y { return 0; }
            @error function f(Natural z) { return z; }
        };
    }

}