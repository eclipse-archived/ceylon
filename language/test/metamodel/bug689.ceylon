import ceylon.language.meta.model {...}

shared class Bug689() {
    shared String constantProperty = "";
    shared String computedProperty => "";
    
    shared variable String variableProperty = "";
    shared String assignableComputedProperty => "";
    assign assignableComputedProperty {}
}

@test
shared void bug689() {
    assert(!`Bug689.constantProperty`.declaration.setter exists);
    assert(!`Bug689.computedProperty`.declaration.setter exists);
    
    assert(!`Bug689.variableProperty`.declaration.setter exists);
    assert(`Bug689.assignableComputedProperty`.declaration.setter exists);    
}
