class StringIterationStatic() {
    void simple(String s) {
        for (char in s) {
            print(char);
        }
    }
    
    void step(String s) {
        for (char in s.by(4)) {
            print(char);
        }
    }
}