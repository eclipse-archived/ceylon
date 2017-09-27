class Person(shared String name) {
    shared class Address(String house, String street, String city){
        shared String format() => "``house``\n``street``\n``city``";
    }
    shared void say(String greeting) {
        print("``greeting``, ``name``");
    }
}
void funrefs() {
    value person = Person("Gavin");
    String(Person) nameFun = Person.name;
    value name = Person.name(person);
    Anything(String)(Person) sayfunfun = Person.say;
    Anything(String) sayfun = Person.say(person);
    value say = Person.say(person)("hello");
    //value hash = person.say.hash;
    String(Singleton<String>) firstFun = Singleton<String>.first;
    value firstFun2 = Singleton<String>.first;
    value first = Singleton<String>.first(Singleton(""));
    String?(Integer)(Singleton<String>) get = Singleton<String>.get;
    Person.Address(String,String,String)(Person) addFunFun = Person.Address;
    Person.Address(String,String,String) addFun = Person.Address(person);
    String()(Person.Address) formatfun = Person.Address.format;
    String() format = Person.Address.format(person.Address("house","street", "city"));
    value formatfun2 = Person.Address.format;
    print(format());
}