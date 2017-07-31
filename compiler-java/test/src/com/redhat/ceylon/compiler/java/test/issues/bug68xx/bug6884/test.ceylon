import ceylon.language.meta.declaration { Module }

shared void run(){
    Module mod = `module`;
    assert (exists resource = mod.resourceByPath("test.txt"));
    print(resource);
}
