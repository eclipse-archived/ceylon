import ceylon.language.meta.model {
    InvocationException
}

void bug607a(Integer* x){}
void bug607b(Integer+ x){}

@test
shared void bug607() {
    `function bug607a`.invoke([]);
    `function bug607a`.invoke([], 1);
    `function bug607a`.invoke([], 1, 2);
    try{
        `function bug607b`.invoke([]);
        assert(false);
    }catch(InvocationException x){
        assert(x.message == "Not enough arguments to function. Expected 1 but got only 0");
    }
    `function bug607b`.invoke([], 1);
    `function bug607b`.invoke([], 1, 2);
}
