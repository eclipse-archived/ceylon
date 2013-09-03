shared String toplevelString = "a";
shared Integer toplevelInteger = 1;
shared Float toplevelFloat = 1.2;
shared Character toplevelCharacter = 'a';
shared Boolean toplevelBoolean = true;
shared Object toplevelObject = 2;

shared variable String toplevelString2 = "a";
shared variable Integer toplevelInteger2 = 1;
shared variable Float toplevelFloat2 = 1.2;
shared variable Character toplevelCharacter2 = 'a';
shared variable Boolean toplevelBoolean2 = true;
shared variable Object toplevelObject2 = 2;

String privateToplevelAttribute = "a";
String privateToplevelFunction(){
    return "b";
}

shared object topLevelObjectDeclaration {
}

class PrivateClass(){
    String privateString = "a";
    String privateMethod(){
        // capture privateString
        privateString.iterator();
        return "b";
    }
    class Inner(){
        string = "c";
    }
    string = "d";
}

shared class NoParams(){
    shared variable String str2 = "a";
    shared variable Integer integer2 = 1;
    shared variable Float float2 = 1.2;
    shared variable Character character2 = 'a';
    shared variable Boolean boolean2 = true;
    shared variable Object obj2 = 2;

    shared String str = "a";
    shared Integer integer = 1;
    shared Float float = 1.2;
    shared Character character = 'a';
    shared Boolean boolean = true;
    shared NoParams obj => this;

    shared NoParams noParams() => this;

    shared NoParams fixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
        assert(s == "a");
        assert(i == 1);
        assert(f == 1.2);
        assert(c == 'a');
        assert(b == true);
        assert(is NoParams o);
        
        return this;
    }
    
    shared NoParams typeParams<T>(T s, Integer i)
        given T satisfies Object {
        
        assert(s == "a");
        assert(i == 1);
        
        // check that our reified T got passed correctly
        assert(is TypeParams<String> t = TypeParams<T>(s, i));
        
        return this;
    }
    
    shared String getString() => "a";
    shared Integer getInteger() => 1;
    shared Float getFloat() => 1.2;
    shared Character getCharacter() => 'a';
    shared Boolean getBoolean() => true;
}

shared class FixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
    assert(s == "a");
    assert(i == 1);
    assert(f == 1.2);
    assert(c == 'a');
    assert(b == true);
    assert(is NoParams o);
}

shared class DefaultedParams(Integer lastGiven = 0, String s = "a", Boolean b = true){
    if(lastGiven == 0){
        assert(s == "a");
        assert(b == true);
    }else if(lastGiven == 1){
        assert(s == "b");
        assert(b == true);
    }else if(lastGiven == 2){
        assert(s == "b");
        assert(b == false);
    }
}

shared class DefaultedParams2(Boolean set, Integer a = 1, Integer b = 2, Integer c = 3, Integer d = 4){
    if(set){
        assert(a == -1);
        assert(b == -2);
        assert(c == -3);
        assert(d == -4);
    }else{
        assert(a == 1);
        assert(b == 2);
        assert(c == 3);
        assert(d == 4);
    }
}

shared class TypeParams<T>(T s, Integer i)
    given T satisfies Object {
    
    assert(s == "a");
    assert(i == 1);
}

shared void fixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o, NoParams oTyped){
    assert(s == "a");
    assert(i == 1);
    assert(f == 1.2);
    assert(c == 'a');
    assert(b == true);
    assert(is NoParams o);
}

shared void defaultedParams(Integer lastGiven = 0, String s = "a", Boolean b = true){
    if(lastGiven == 0){
        assert(s == "a");
        assert(b == true);
    }else if(lastGiven == 1){
        assert(s == "b");
        assert(b == true);
    }else if(lastGiven == 2){
        assert(s == "b");
        assert(b == false);
    }
}

shared void defaultedParams2(Boolean set, Integer a = 1, Integer b = 2, Integer c = 3, Integer d = 4){
    if(set){
        assert(a == -1);
        assert(b == -2);
        assert(c == -3);
        assert(d == -4);
    }else{
        assert(a == 1);
        assert(b == 2);
        assert(c == 3);
        assert(d == 4);
    }
}

shared void variadicParams(Integer count = 0, String* strings){
    assert(count == strings.size);
    for(s in strings){
        assert(s == "a");
    }
}

shared T typeParams<T>(T s, Integer i)
    given T satisfies Object {
    
    assert(s == "a");
    assert(i == 1);
    
    // check that our reified T got passed correctly
    assert(is TypeParams<String> t = TypeParams<T>(s, i));
    
    return s;
}

shared String getString() => "a";
shared Integer getInteger() => 1;
shared Float getFloat() => 1.2;
shared Character getCharacter() => 'a';
shared Boolean getBoolean() => true;
shared Object getObject() => 2;

shared NoParams getAndTakeNoParams(NoParams o) => o;

shared String toplevelWithMultipleParameterLists(Integer i)(String s) => s + i.string;

shared class ContainerClass(){
    shared class InnerClass(){}
    shared class DefaultedParams(Integer expected, Integer toCheck = 0){
        assert(expected == toCheck);
    }
}

shared class ParameterisedContainerClass<Outer>(){
    shared class InnerClass<Inner>(){}
}

shared interface ContainerInterface{
    shared class InnerClass(){}
}

shared class ContainerInterfaceImpl() satisfies ContainerInterface {}

shared alias TypeAliasToClass => NoParams;

shared alias TypeAliasToClassTP<J>
    given J satisfies Object
    => TypeParams<J>;

shared alias TypeAliasToUnion => Integer | String;