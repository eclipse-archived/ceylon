import ceylon.language.metamodel { modules }

shared class Bug1198Person(shared String name) {
    string => name;
}

void bug1198() {
    value temp = `Bug1198Person.name`;
}
