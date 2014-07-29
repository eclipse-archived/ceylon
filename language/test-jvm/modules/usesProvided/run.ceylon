import foo.user { ... }

shared void run(){
    print("Calling User");
    // make sure we can call it
    User().user();
    // make sure we can see its overridden dependencies 
    assert(`module foo.user`.dependencies.size == 1);
}