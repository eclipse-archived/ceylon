import ceylon.language.meta { modules }

shared void run(){
    print("Hello World");
    // make sure we can find extra modules we did not load but want available
    assert(modules.find("modules.extra", "1") exists);
}