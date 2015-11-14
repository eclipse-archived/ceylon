import ceylon.language.meta { modules }

shared class BugC1198Person(shared String name) {
    string => name;
}

@test
shared void bugC1198() {
    value temp = `BugC1198Person.name`;
}
