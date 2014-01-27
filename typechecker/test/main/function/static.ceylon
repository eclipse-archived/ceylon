class Person(shared String name) 
{
    shared void say(String saying) {
        print(name + saying);
    }
    shared class Address(String x, String y, String z) {
        shared String format() => x+y+z;
    }
    
    Person.Address(String,String,String)(Person) addy = Person.Address;
}

void funrefs<T>(T t) given T satisfies Category {
    value person = Person("Gavin");
    String(Person) nameFun = Person.name;
    @type:"String" value name = Person.name(person);
    Anything(String)(Person) sayfunfun = Person.say;
    Anything(String) sayfun = Person.say(person);
    @type:"Anything" value say = Person.say(person)("hello");
    Person.say(person)("hello");
    @error Person.say("hello");
    @error Person.say.equals("");
    @error value hash = person.say.hash;
    @type:"Null|Character" List<Character>.get("hello")(1);
    @error List.get("hello")(1);
    @type:"Boolean" Category<Character>.contains("hello")('l');
    @error @type:"Boolean" T.contains(t)('l');
    String(Singleton<String>) firstFun = Singleton<String>.first;
    @type:"String" value first = Singleton<String>.first(Singleton(""));
    String?(Integer)(Singleton<String>) get = Singleton<String>.get;
    Person.Address(String,String,String)(Person) addFunFun = Person.Address;
    Person.Address(String,String,String) addFun = Person.Address(person);
    String()(Person.Address) formatfun = Person.Address.format;
    String() format = Person.Address.format(person.Address("","", ""));
    @error String()(Person.Address) broke = person.Address.format;
}

interface AB<T> { 
    shared interface BA {  
        shared Integer iii=>10;
    }
    shared class CA() {
         shared Integer jjj=>10; 
    }
    shared interface G {
        shared void m<T>() {}
    }
}
void testAB() {
    value val0 = AB<String>.BA.iii;
    value val1 = AB<String>.CA.jjj;  
    @error value val2 = AB<String>.BA; 
    @error value val3 = AB.BA.iii;
    value val4 = AB<String>.CA;
    @error value val5 = AB.CA.jjj;
    value val6 = AB<String>.G.m<Float>;
    @error value val7 = AB<String>.G.m;
    @error value val9 = Singleton;
    @error value val10 = coalesce;
    value val11 = Singleton<String>;
    value val12 = coalesce<Integer>;
}
