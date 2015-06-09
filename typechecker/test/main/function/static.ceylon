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

alias IntegerOrFloat => Integer|Float;

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
    @type:"Null|Character" List.get("hello")(1);
    @type:"Boolean" Category<Character>.contains("hello")('l');
    @type:"Boolean" T.contains(t)('l');
    String(Singleton<String>) firstFun = Singleton<String>.first;
    @type:"String" value first = Singleton<String>.first(Singleton(""));
    String?(Integer)(Singleton<String>) get = Singleton<String>.get;
    Person.Address(String,String,String)(Person) addFunFun = Person.Address;
    Person.Address(String,String,String) addFun = Person.Address(person);
    String()(Person.Address) formatfun = Person.Address.format;
    String() format = Person.Address.format(person.Address("","", ""));
    @error String()(Person.Address) broke = person.Address.format;
    Comparison(Nothing)(IntegerOrFloat) compare = IntegerOrFloat.compare;
    Boolean(Object)(T) contains = T.contains;
    @error value fold1 = Iterable.fold;
    String(String(String, String))(String)({String*}) fold2 = Iterable<String>.fold<String>;
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
    @type:"<out Element> => Callable<Singleton<Element>,Tuple<Element,Element,Empty>>" 
    value val9 = Singleton;
    @type:"<Value> given Value satisfies Summable<Value> => Callable<Value,Tuple<Iterable<Value,Nothing>,Iterable<Value,Nothing>,Empty>>" 
    value val10 = sum;
    value val11 = Singleton<String>;
    value val12 = sum<Integer>;
    value val13 = every;
}

void testCallableMembers() {
    value ok1 = String.equals;
    value alsoOk1 = (String).equals;
    //@error value bad1 = String equals;
    value ok2 = Identifiable.equals;
    @error value alsoOk2 = (Identifiable).equals;
    //@error value bad2 = Identifiable equals;
}

void testStaticRefTypeInference() {
    @type:"XXXX<Integer>" value xx = XXXX(1);
    @type:"XXXX<Integer>" value x = XXXX.yyyy(1);
    
    @type:"ZZZZ<String>.XXXX<Integer>" value z = ZZZZ("").XXXX.yyyy(1,"");
    
    @type:"WWWW<Integer>.XXXX<Float>" value uv = WWWW.XXXX<Float>(WWWW<Integer>())(1, 0.0);
    
    @type:"Null|Character" value ggg = List.get("hello")(1);
}

class ZZZZ<U>(U u) {
    shared class XXXX<T> {
        shared new yyyy(T t, U u) {}
    }
}

class XXXX<T> {
    shared new (T t) {}
    shared new yyyy(T t) {}
}


class WWWW<U>() {
    shared class XXXX<V>(U u, V v) {}
}

void testMissingTypeArgs() {
    @type:"Callable<Array<String>,Empty>" Array.clone(Array{""});
    @type:"Callable<AB<Integer>.CA,Empty>" AB.CA(object satisfies AB<Integer>{});
    @error:"missing type arguments" AB.CA();
    @error:"missing type arguments" Array.clone();
    @error:"missing type arguments" value blah = Array.clone;
    @error:"wrong number of type arguments" Array<>.clone();
    @error:"wrong number of type arguments" value blahblah = Array<>.clone;
    value genericRef = Array;
    <T>=>Array<T>({T*}) genericRef2 = Array;
    value nonGenericRef = Array<String>.clone;
    Array<String>()(Array<String>) nonGenericRef2 = Array<String>.clone;
    value char = Integer.character;
    Character(Integer) char2 = Integer.character;
    @error:"missing argument to required parameter" value nochar = Integer.character();
    @error:"does not accept type arguments" value notchar = Integer<>.character;
    @error:"does not accept type arguments" value nottachar = Integer.character<>;
    value and = Integer.and;
    Integer(Integer)(Integer) and2 = Integer.and;
    @error:"missing argument to required parameter" value noand = Integer.and();
    @error:"does not accept type arguments" value notand = Integer<>.and;
    @error:"does not accept type arguments" value nottaand = Integer.and<>;
}


class OuterClass() {
    shared class InnerClass {
        shared String name => "Gavin";
        shared void reset() {}
        shared new instance {}
        shared new create() {}
    }
}

void testOuterInner() {
    @type:"OuterClass.InnerClass(OuterClass)"
    value i = OuterClass.InnerClass.instance;
    @type:"OuterClass.InnerClass()(OuterClass)" 
    value c = OuterClass.InnerClass.create;
    @type:"String(OuterClass.InnerClass)" 
    value n = OuterClass.InnerClass.name;
    @type:"Anything()(OuterClass.InnerClass)" 
    value r = OuterClass.InnerClass.reset;
    @type:"Attribute<OuterClass,OuterClass.InnerClass,Nothing>"
    value meta1 = `OuterClass.InnerClass.instance`;
    @type:"MemberClassConstructor<OuterClass,OuterClass.InnerClass,Empty>"
    value meta2 = `OuterClass.InnerClass.create`;
    @type:"ValueDeclaration"
    value meta3 = `value OuterClass.InnerClass.instance`;
    @type:"ConstructorDeclaration"
    value meta4 = `new OuterClass.InnerClass.create`;
}
