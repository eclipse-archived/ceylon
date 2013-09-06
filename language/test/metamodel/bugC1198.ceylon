import ceylon.language.model { modules }

shared class BugC1198Person(shared String name) {
    string => name;
}

void bugC1198() {
    value temp = `BugC1198Person.name`;
}
