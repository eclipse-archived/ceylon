class Bug245Person(String name) {
    string=>name;
}

@test
shared void bug245() {
    value createPerson = `Bug245Person`;
    value person = createPerson("Gavin");
    print(person);
}
