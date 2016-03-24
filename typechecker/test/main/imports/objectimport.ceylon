import imports {
    toplevel { 
        inner
    }
}

shared object toplevel {
    shared object inner {}
}

void run() {
    \Itoplevel.\Iinner inner1 = toplevel.inner;
    \Itoplevel.\Iinner inner2 = inner; 
    \Iinner inner3 = toplevel.inner;
    \Iinner inner4 = inner; 
}

