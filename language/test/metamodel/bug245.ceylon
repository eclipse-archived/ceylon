class Bug245Person(String name) {
    string=>name;
}

void bug245() {
    value createPerson = `Bug245Person`;
    value person = createPerson("Gavin");
    print(person);
}
