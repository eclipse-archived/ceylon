shared interface GreetingService {
    shared formal String language;
    shared formal String greet(String name);
    shared actual String string => language;
}
service(`interface GreetingService`)
shared class Hello() satisfies GreetingService {
    shared actual String language => "en";
    shared actual String greet(String name) => "Hello ``name``";
}
service(`interface GreetingService`)
shared class Bonjour() satisfies GreetingService {
    shared actual String language => "fr";
    shared actual String greet(String name) => "Bonjour ``name``";
}

shared interface GenericService<Item> {
    shared formal Item item;
}
service(`interface GenericService`)
shared class StringService() satisfies GenericService<String> {
    shared actual String item => "foo";
}
service(`interface GenericService`)
shared class IntegerService() satisfies GenericService<Integer> {
    shared actual Integer item => 1;
}


@test
shared void servicesTest() {
    value providers = `module`.findServiceProviders(`GreetingService`);
    assert(providers.size == 2);
    assert(exists en = providers.find((p) => p.language == "en"));
    assert(exists fr = providers.find((p) => p.language == "fr"));
    assert("Hello Tom" == en.greet("Tom"));
    assert("Bonjour Stef" == fr.greet("Stef"));
    
    value sproviders = `module`.findServiceProviders(`GenericService<String>`);
    assert(sproviders.size == 1,
        exists s=sproviders.first);
    assert("foo" == s.item);
    
    value iproviders = `module`.findServiceProviders(`GenericService<Integer>`);
    assert(iproviders.size == 1,
        exists i=iproviders.first);
    assert(1 == i.item);
}