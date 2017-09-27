class Bug2176C() {
    shared class Inner {
        shared new instance {
            
        }
    }
}

void bug2176C() {
    Bug2176C.Inner(Bug2176C) ref = Bug2176C.Inner.instance;
    Bug2176C.Inner inst = ref(Bug2176C());
}