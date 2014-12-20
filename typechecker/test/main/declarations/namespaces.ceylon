String name = "z";

void namespaces1() {
    if (1==1) {
        String name = "x";
    }
    for (c in "hello") {
        String name = "x";
    }
    String name = "y";
}

void namespaces2() {
    String name = "y";
    if (1==1) {
        @error String name = "x";
    }
}

void namespaces3() {
    String name = "y";
    void fun(){
        String name = "x";
    }
    class Class() {
        String name = "x";
    }
}