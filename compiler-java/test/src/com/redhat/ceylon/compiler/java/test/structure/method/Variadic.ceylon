@noanno
class Variadic() {
    void possiblyEmpty(String* args) {
    }
    
    void notEmpty(String+ args) {
    }
    
    void possiblyEmptyInBody(args) {
        String* args;
    }
    
    void notEmptyInBody(args) {
        String+ args;
    }
}