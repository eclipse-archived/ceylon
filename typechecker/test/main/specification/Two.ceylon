class Two() extends One() {
    void fun() {
        @error integer = 2;
    }
}

class Three() extends One() {
    @error integer = 3;
}