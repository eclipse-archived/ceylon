import ceylon.language.meta { modules }
import foo.user { ... }

shared void run(){
    print("Calling User");
    // make sure we can call it
    User().user();
    // make sure we can see its overridden dependencies 
    assert(`module foo.user`.dependencies.size == 1);
    // make sure we can find extra modules we did not load but want available
    assert(modules.find("modules.extra", "1") exists);
}