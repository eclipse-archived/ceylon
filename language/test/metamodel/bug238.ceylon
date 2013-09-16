import ceylon.language.meta { modules }

void bug238(){
    value m = modules.find("metamodel", "0.1");
    assert(exists m);
    
    assert(m.members.size == 1);
}
