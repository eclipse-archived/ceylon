import ceylon.language.meta.declaration { Module }

shared void run(){
    Module mod = `module`;
    assert (exists resource1 = mod.resourceByPath("bar.txt"));
    assert(resource1.textContent() == "HERE");
    assert (exists resource2 = mod.resourceByPath("/com/foo/bar.txt"));
}