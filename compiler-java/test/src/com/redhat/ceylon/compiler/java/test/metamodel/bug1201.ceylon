import ceylon.language.metamodel { modules }

shared class Bug1201Person(name) {
    variable shared String name;
    string=>name;
}

void bug1201() {
    value p = `Bug1201Person`;
    p("Me");
    value person = Bug1201Person("Gavin");
    value n = `Bug1201Person.name`;
    n(person).set("Stef");
    print(n(person).get());
}
