import ceylon.language.model { modules }

void bug238(){
    value m = modules.find("metamodel", "123");
    assert(exists m);
    
    assert(m.members.size == 1);
}