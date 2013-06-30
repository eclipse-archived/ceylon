class Person(shared String name) 
{
    shared void say(String saying) {
        print(name + saying);
    }
    shared class Address(String x, String y, String z) {}
    
    Person.Address(String,String,String)(Person) addy = Person.Address;
}

void funrefs() {
    value person = Person("Gavin");
    String(Person) nameFun = Person.name;
    @type:"String" value name = Person.name(person);
    Anything(String)(Person) sayfunfun = Person.say;
    Anything(String) sayfun = Person.say(person);
    @type:"Anything" value say = Person.say(person)("hello");
    //value hash = person.say.hash;
    String(Singleton<String>) firstFun = Singleton<String>.first;
    @type:"String" value first = Singleton<String>.first(Singleton(""));
    String?(Integer)(Singleton<String>) get = Singleton<String>.get;
    Person.Address(String,String,String)(Person) addFunFun = Person.Address;
    Person.Address(String,String,String) addFun = Person.Address(person);
}
