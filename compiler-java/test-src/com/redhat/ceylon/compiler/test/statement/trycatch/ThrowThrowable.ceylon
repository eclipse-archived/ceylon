@nomodel
class ThrowThrowable() {
    void m() {
        try {
            
        } catch (Exception e) {
            if (exists cause = e.cause) { 
                throw cause;
            }
        }
    }
}
