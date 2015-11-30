import ceylon.language.serialization{...}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter,
    ClassDeclaration
}
import ceylon.language.meta.model {
    ClassModel,
    Type
}

"A serializable class with a single attribute for test purposes."
serializable class Container<Element>(shared Element element) {
    shared serializable class Member() {
        shared Container<Element> container => outer;
    }
}
@test
shared void testSerializationOfObject() {
    value sc = serialization();
    value container = Container("hello, world");
    variable value refs = sc.references(container);
    assert(is Identifiable x = refs.instance,
        container === x);
    assert(1 == refs.size);
    assert(exists reference= refs.first,
        is Object o = reference.item,
        container.element == o);
    
    value member = container.Member();
    refs = sc.references(member);
    assert(is Identifiable z = refs.instance, 
        member === z);
    assert(1 == refs.size);
    assert(exists outerReference = refs.first,
        //is Container<String> a=
        is Identifiable c=outerReference.item,
        container === c);
}

@test
shared void serializationWithUninitializedLate() {
    value sc = serialization();
    value cycle = Cycle();
    value refs = sc.references(cycle).references;
    assert(exists r = refs.first);
    assert(exists x=r.referred(cycle),
        x == uninitializedLateValue);
}

@test
shared void testSerializationOfArray() {
    value sc = serialization();
    value array  = Array{"hello"};
    variable value refs = sc.references(array);
    assert(is Identifiable x = refs.instance,
        array  === x);
    assert(2 == refs.size);
    assert(exists reference= refs.first,
        is Member sizeMember = reference.key,
        sizeMember.attribute == `value Array.size`,
        is Object o = reference.item,
        1 == o);
    assert(exists elementReference = refs.sequence()[1],
        is Element element = elementReference.key,
        element.index == 0,
        is Object o2 = elementReference.item,
        "hello" == o2);
}

@test
shared void testSerializationOfTuple() {
    value sc = serialization();
    value tuple = [1, "hello"];
    variable value refs = sc.references(tuple);
    assert(is [Integer, String] x = refs.instance,
        tuple == x);
    assert(2 == refs.size);
    assert(exists reference= refs.first,
        is Member firstMember = reference.key,
        firstMember.attribute == `value Tuple.first`,
        is Object o = reference.item,
        1 == o);
    assert(exists restReference = refs.sequence()[1],
        is Member restMember = restReference.key,
        restMember.attribute == `value Tuple.rest`,
        is Object o2 = restReference.item,
        ["hello"] == o2);
    
    refs = sc.references(o2);
    assert(2 == refs.size);
    assert(exists reference2= refs.first,
        is Member firstMember2 = reference2.key,
        firstMember2.attribute == `value Tuple.first`,
        is Object o3 = reference2.item,
        "hello" == o3);
    assert(exists restReference2 = refs.sequence()[1],
        is Member restMember2 = restReference2.key,
        restMember2.attribute == `value Tuple.rest`,
        is Object o4 = restReference2.item,
        [] == o4);
}
@test
shared void testSerializationOfTupleTail() {
    value sc = serialization();
    value tuple = ["hello", *(2..1_000_000)];
    variable value refs = sc.references(tuple);
    assert(is [String, Integer*] x = refs.instance,
        tuple == x);
    assert(2 == refs.size);
    assert(exists reference= refs.first,
        is Member firstMember = reference.key,
        firstMember.attribute == `value Tuple.first`,
        is Object o = reference.item,
        "hello" == o);
    assert(exists restReference = refs.sequence()[1],
        is Member restMember = restReference.key,
        restMember.attribute == `value Tuple.rest`,
        is Object o2 = restReference.item,
        (2..1_000_000) == o2);
}

@test
shared void serializationOfCallable() {
    value sc = serialization();
    Anything(String) inst = print;
    try {
        variable value refs = sc.references(inst);
    } catch (SerializationException e) {
        
    }
}

serializable class SerializableMethodSpecification(Anything(String) f) {
    shared Anything m(String s) => f(s);
}
@test
shared void serializationOfIndirectCallable() {
    value sc = serialization();
    Anything(Anything) p = print;
    value inst = SerializableMethodSpecification(p);
    variable value refs = sc.references(inst);
    assert(exists inst2 = refs.instance,
        inst2 == inst);
    assert(exists ref = refs.sequence().first);
    assert(is Member f = ref.key,
    f.attribute.string == "value serialization::SerializableMethodSpecification.f");
    assert(is Anything(Anything) pRef = ref.item);
    // can't test p ===pRef because Callable.equals returns false 
}

@test
shared void testDeserializationOfTuple() {
    variable value dc = deserialization<Integer>();
    dc.instance(0, `[String, Integer]`);
    dc.attribute(0, `value Tuple.first`, 1);
    dc.instanceValue(1, "hello");
    dc.attribute(0, `value Tuple.rest`, 2);
    dc.instance(2, `[Integer]`);
    dc.attribute(2, `value Tuple.first`, 3);
    dc.instanceValue(3, 42);
    dc.attribute(2, `value Tuple.rest`, 4);
    dc.instanceValue(4, []);
    value reconstructed = dc.reconstruct<[String,Integer]>(0);
    assert(["hello", 42] == reconstructed);
}

@test
shared void testDeserializationOfTupleTail() {
    variable value dc = deserialization<Integer>();
    dc.instance(0, `[String, Integer+]`);
    dc.attribute(0, `value Tuple.first`, 1);
    dc.instanceValue(1, "hello");
    dc.attribute(0, `value Tuple.rest`, 2);
    dc.instanceValue(2, 0..10);
    value reconstructed = dc.reconstruct<[String,Integer+]>(0);
    assert(["hello", *(0..10)] == reconstructed);
}

@test
shared void testDeserializationOfTupleBrokenTypes() {
    variable value dc = deserialization<Integer>();
    dc.instance(0, `[String, Integer]`);
    dc.attribute(0, `value Tuple.first`, 1);
    dc.instanceValue(1, "hello");
    dc.attribute(0, `value Tuple.rest`, 2);
    dc.instance(2, `[Integer]`);
    dc.attribute(2, `value Tuple.first`, 3);
    dc.instanceValue(3, "world");
    dc.attribute(2, `value Tuple.rest`, 4);
    dc.instanceValue(4, []);
    try {
        dc.reconstruct<Anything>(0);
        assert(false);
    } catch (DeserializationException e) {
        assert(e.message == "instance not assignable to value ceylon.language::Tuple.first of id 2: String is not assignable to Integer");
    }
}

@test
"deserialize a Container that references a string. 
 test every possible ordering of invocation of [[DeserializationContext]] methods 
 "
shared void testDeserializationOfObject() {
    variable value dc = deserialization<Integer>();
    dc.attribute(2, `value Container.element`, 1);
    dc.instanceValue(1, "hello, world");
    dc.instance(2, `Container<String>`);
    variable Container<String> reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
    
    dc = deserialization<Integer>();
    dc.attribute(2, `value Container.element`, 1);
    dc.instance(2, `Container<String>`);
    dc.instanceValue(1, "hello, world");
    reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, "hello, world");
    dc.attribute(2, `value Container.element`, 1);
    
    dc.instance(2, `Container<String>`);
    reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, "hello, world");
    dc.instance(2, `Container<String>`);
    dc.attribute(2, `value Container.element`, 1);
    reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
    
    dc = deserialization<Integer>();
    dc.instance(2, `Container<String>`);
    dc.attribute(2, `value Container.element`, 1);
    dc.instanceValue(1, "hello, world");
    reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
    
    dc = deserialization<Integer>();
    dc.instance(2, `Container<String>`);
    dc.instanceValue(1, "hello, world");
    dc.attribute(2, `value Container.element`, 1);
    reconstructed = dc.reconstruct<Container<String>>(2);
    assert(reconstructed.element == "hello, world");
}

@test
"deserialize a Container that references an container that references a string"
shared void testDeserializationOfObject2() {
    value dc = deserialization<Integer>();
    dc.attribute(2, `value Container.element`, 1);
    dc.instanceValue(1, "hello, world");
    dc.instance(2, `Container<String>`);
    dc.attribute(3, `value Container.element`, 2);
    dc.instance(3, `Container<Container<String>>`);
    Container<Container<String>> reconstructed = dc.reconstruct<Container<Container<String>>>(3);
    assert(reconstructed.element.element == "hello, world");
}

@test
"deserialize a Container>Member that references an container that references a string"
shared void testDeserializationOfMemberObject() {
    value dc = deserialization<Integer>();
    dc.instanceValue(1, "hello, world");
    dc.instance(2, `Container<String>`);
    dc.attribute(2, `value Container.element`, 1);
    dc.instance(3, `Container<String>.Member`);
    dc.memberInstance(2, 3);
    Container<String>.Member reconstructed = dc.reconstruct<Container<String>.Member>(3);
    assert(reconstructed.container.element == "hello, world");
}

@test
"deserialize an array that references a string"
shared void testDeserializationOfArrays() {
    variable value dc = deserialization<Integer>();
    dc.instanceValue(1, "hello, world");
    dc.instance(2, `Array<String>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    Array<String> reconstructedString = dc.reconstruct<Array<String>>(2);
    assert(reconstructedString.size == 1);
    assert(exists s=reconstructedString[0], s == "hello, world");
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, 'h');
    dc.instance(2, `Array<Character>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    Array<Character> reconstructedCharacter = dc.reconstruct<Array<Character>>(2);
    assert(reconstructedCharacter.size == 1);
    assert(exists c=reconstructedCharacter[0], c == 'h');
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, 42);
    dc.instance(2, `Array<Integer>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    Array<Integer> reconstructedInteger = dc.reconstruct<Array<Integer>>(2);
    assert(reconstructedInteger.size == 1);
    assert(exists i=reconstructedInteger[0], i == 42);
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, true);
    dc.instance(2, `Array<Boolean>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    Array<Boolean> reconstructedBoolean = dc.reconstruct<Array<Boolean>>(2);
    assert(reconstructedBoolean.size == 1);
    assert(exists b=reconstructedBoolean[0], b == true);
    
    dc = deserialization<Integer>();
    dc.instanceValue(1, 42.byte);
    dc.instance(2, `Array<Byte>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    Array<Byte> reconstructedByte = dc.reconstruct<Array<Byte>>(2);
    assert(reconstructedByte.size == 1);
    assert(exists by=reconstructedByte[0], by == 42.byte);
}
// TODO attribute ordering
// TODO member class ordering
// TODO error cases (missing id, wrong type for attribute, wrong type for reconstruct)
// TODO serialization tests

serializable class Cycle() {
    shared late Identifiable ref;
}

@test
"check we can deserialize a cyclic object"
shared void testDeserializationOfObjectCycle() {
    value dc = deserialization<Integer>();
    dc.instance(1, `Cycle`);
    dc.attribute(1, `value Cycle.ref`, 1);
    Cycle reconstructed = dc.reconstruct<Cycle>(1);
    assert(reconstructed.ref === reconstructed);
}

@test
"check we can deserialize a cyclic array"
shared void testDeserializationOfArrayCycle() {
    value dc = deserialization<Integer>();
    dc.instance(1, `Array<Identifiable>`);
    dc.attribute(1, `value Array.size`, 2);
    dc.instanceValue(2, 1);
    dc.element(1, 0, 1);
    Array<Identifiable> reconstructed = dc.reconstruct<Array<Identifiable>>(1);
    assert(exists r=reconstructed[0], r === reconstructed);
}

@test
shared void deserializationWithNoInfo() {
    value dc = deserialization<Integer>();
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "unknown id: 1.");
    }
}

@test
shared void deserializationWithNoClass() {
    value dc = deserialization<Integer>();
    dc.attribute(1, `value Cycle.ref`, 1);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "no class specified for instance with id 1");
    }
}

@test
shared void deserializationWithUninitializedLateAttribute() {
    value dc = deserialization<Integer>();
    dc.instance(1, `Cycle`);
    Cycle c = dc.reconstruct<Cycle>(1);
    // It should be partially initialized, so I should be able to set its ref
    c.ref = c;
}

@test
shared void deserializationWithInsufficientStateObject() {
    value dc = deserialization<Integer>();
    dc.instance(1, `Container<String>`);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "lacking sufficient state for instance with id 1: value serialization::Container.element");
    }
}

@test
shared void deserializationWithInsufficientStateArray() {
    variable value dc = deserialization<Integer>();
    dc.instance(1, `Array<String>`);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "lacking sufficient state for instance with id 1: value ceylon.language::Array.size");
    }
    
    dc = deserialization<Integer>();
    dc.instance(1, `Array<String>`);
    dc.instanceValue(2, 1);
    dc.attribute(1, `value Array.size`, 2);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "lacking sufficient state for instance with id 1: index 0");
    }
}

@test
shared void deserializationWithBadAttributeType() {
    value dc = deserialization<Integer>();
    dc.instance(2, `Container<String>`);
    dc.attribute(2, `value Container.element`, 1);
    dc.instanceValue(1, 'c');
    try {
        value x =dc.reconstruct<Container<String>>(2);
        // pb here was jvm wascontainer has having a field of type
        // <upper bound of Element=Object>, so didn't complain when we set the field value.
        // it would if the field was not a tp, but there' basically no check that
        // the value being set on a field is of the correct ceylon type as the 
        // ceylon Value 
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "instance not assignable to value serialization::Container.element of id 2: Character is not assignable to String");
    }
}

@test
shared void deserializationWithBadElementType() {
    variable value dc = deserialization<Integer>();
    dc.instanceValue(1, 'c');
    dc.instance(2, `Array<String>`);
    dc.attribute(2, `value Array.size`, 3);
    dc.instanceValue(3, 1);
    dc.element(2, 0, 1);
    try {
        Array<String> reconstructedString = dc.reconstruct<Array<String>>(2);
    } catch(DeserializationException e) {
        assert(e.message == "instance not assignable to index 0 of id 2: Character is not assignable to String");
    }
    
}

serializable class CollisionSuper(collides) {
    String collides;
    shared actual String string => collides;
}
serializable class CollisionSub(String sup, collides) extends CollisionSuper(sup) {
    shared Integer collides; 
}

@test
shared void deserializationWithAttributeNamingCollision() {
    variable value dc = deserialization<Integer>();
    dc.instance(1, `CollisionSub`);
    dc.attribute(1, `class CollisionSuper`.getDeclaredMemberDeclaration<ValueDeclaration>("collides") else nothing, 2);
    dc.instanceValue(2, "super");
    dc.attribute(1, `value CollisionSub.collides`, 3);
    dc.instanceValue(3, 42);
    
    variable CollisionSub reconstructed = dc.reconstruct<CollisionSub>(1);
    assert(reconstructed.string == "super");
    assert(reconstructed.collides == 42);
}

serializable class SerializableSuper() {}
class NonserializableSub() extends SerializableSuper(){}
@test
shared void serNonSerializable() {
    value sc = serialization();
    try {
        sc.references(NonserializableSub());
        "we expect it to throw"
        assert(false);
    } catch (SerializationException e) {
        assert("instance of non-serializable class: serialization::NonserializableSub" == e.message);
    }
}
@test
shared void deserNonSerializable() {
    value dc = deserialization<Integer>();
    try {
        dc.instance(1, `NonserializableSub`);
        "we expect it to throw"
        assert(false);
    } catch (DeserializationException e) {
        assert("not serializable: serialization::NonserializableSub" == e.message);
    }
}

serializable class Person(name, employer) {
    shared String name;
    shared Company employer;
}
serializable class Company(name) {
    shared String name;
    shared late Person owner;
}

Integer nullary() => 42;

@test
shared void testFactoryNullary() {
    value dc = deserialization<Integer>();
    dc.factory(1, nullary);
    dc.arguments(1, []);
    value got = dc.reconstruct<Object>(1);
    print(got);
    assert(42 == got);
}


@test
shared void testFactoryParse() {
    value dc = deserialization<Integer>();
    dc.instanceValue(0, "42");
    dc.factory(1, parseInteger);
    dc.arguments(1, [0]);
    value got = dc.reconstruct<Object>(1);
    print(got);
    assert(42 == got);
}

Element[] seq<Element>(Array<Element> array) {
    return array.sequence();
}

@test
shared void testFactorySequence() {
    value dc = deserialization<Integer>();
    dc.instanceValue(0, 42);
    dc.instanceValue(1, "hello");
    dc.instance(3, `Array<String|Integer>`);
    dc.attribute(3, `value Array.size`, 4);
    dc.instanceValue(4, 2);
    dc.element(3, 0, 0);
    dc.element(3, 1, 1);
    dc.factory(5, seq<String|Integer>);
    dc.arguments(5, [3]);
    value got = dc.reconstruct<Object>(5);
    print(got);
    assert([42, "hello"] == got);
    assert(is ArraySequence<String|Integer> got);
}

@test
shared void testFactorySequencesWithCycles() {
    /*
       cycle1.ref = cycle2;
       cycle2.ref = cycle1;
       
       cycle3.ref = cycle4;
       cycle4.ref = cycle3;
       
       array5 = [cycle1, cycle2];
       seq7 = seq(array5);
       array8 = [seq7, cycle3];
       seq9 = seq(array8);
     */
    
    //for (ids in [1, 2, 3, 4, 5, 6, 7, 8, 9].permutations) {
    [Integer+] ids = [1, 2, 3, 4, 5, 6, 7, 8, 9];if (true) {
        value dc = deserialization<Integer>();
        value cycle1 = ids[0];
        assert(exists cycle2 = ids[1]);
        assert(exists cycle3 = ids[2]);
        assert(exists cycle4 = ids[3]);
        
        assert(exists array5 = ids[4]);
        assert(exists arraySize6 = ids[5]);
        assert(exists seq7 = ids[6]);
        assert(exists array8 = ids[7]);
        assert(exists seq9 = ids[8]);
        //print(ids);
        dc.instance(cycle1, `Cycle`);
        dc.instance(cycle2, `Cycle`);
        dc.instance(cycle3, `Cycle`);
        dc.instance(cycle4, `Cycle`);
        dc.attribute(cycle1, `value Cycle.ref`, cycle2);
        dc.attribute(cycle2, `value Cycle.ref`, cycle1);
        dc.attribute(cycle3, `value Cycle.ref`, cycle4);
        dc.attribute(cycle4, `value Cycle.ref`, cycle3);
        
        dc.instance(array5, `Array<Cycle>`);
        dc.attribute(array5, `value Array.size`, arraySize6);
        dc.instanceValue(arraySize6, 2);
        dc.element(array5, 0, cycle1);
        dc.element(array5, 1, cycle2);
        
        dc.factory(seq7, `seq<Cycle>`);
        dc.arguments(seq7, [array5]);
        
        dc.instance(array8, `Array<Cycle|Sequential<Cycle>>`);
        dc.attribute(array8, `value Array.size`, arraySize6);
        dc.element(array8, 0, seq7);
        dc.element(array8, 1, cycle3);
        
        dc.factory(seq9, `seq<Cycle|Sequential<Cycle>>`);
        dc.arguments(seq9, [array8]);
        
        value gotSeq9 = dc.reconstruct<Object>(seq9);
        print(gotSeq9);
        assert(is Sequential<Cycle|Sequential<Cycle>> gotSeq9);
        assert(is Sequential<Cycle> gotSeq7 = gotSeq9[0]);
        assert(is Cycle gotCycle3 = gotSeq9[1]);
        assert(gotSeq9.size == 2);
        assert(is Cycle gotCycle1 =  gotSeq7[0]);
        assert(is Cycle gotCycle2 =  gotSeq7[1]);
        assert(gotSeq7.size == 2);
        value gotCycle4 = gotCycle3.ref;
        assert(!gotCycle1 === gotCycle2);
        assert(!gotCycle1 === gotCycle3);
        assert(!gotCycle1 === gotCycle4);
        assert(!gotCycle2 === gotCycle3);
        assert(!gotCycle2 === gotCycle4);
        assert(!gotCycle3 === gotCycle4);
    }
    
}

@test
shared void testDetectCyclicInvocation() {
    /*
       array5 = [seq9];
       seq7 = seq(array5);
       array8 = [seq7];
       seq9 = seq(array8);
     */
    
    for (ids in [5, 6, 7, 8, 9].permutations) {
    //[Integer+] ids = [5, 6, 7, 8, 9];if (true) {
        value dc = deserialization<Integer>();
        
        value array5 = ids[0];
        assert(exists arraySize6 = ids[1]);
        assert(exists seq7 = ids[2]);
        assert(exists array8 = ids[3]);
        assert(exists seq9 = ids[4]);
        print(ids);
        
        dc.instance(array5, `Array<Sequential<Cycle>>`);
        dc.attribute(array5, `value Array.size`, arraySize6);
        dc.instanceValue(arraySize6, 1);
        dc.element(array5, 0, seq9);
        
        dc.factory(seq7, `seq<Sequential<Object>>`);
        dc.arguments(seq7, [array5]);
        
        dc.instance(array8, `Array<Sequential<Cycle>>`);
        dc.attribute(array8, `value Array.size`, arraySize6);
        dc.element(array8, 0, seq7);
        
        dc.factory(seq9, `seq<Sequential<Object>>`);
        dc.arguments(seq9, [array8]);
        
        try {
            dc.reconstruct<Object>(seq9);
        } catch (DeserializationException e) {
            assert("cyclic factory invocation" == e.message);
        }
        
    }

}

// TODO test we can pick out different subgraphs, both with an without factories


@test
shared void docExample() {
    value wonkaInc = Company("Wonka Inc.");
    value willy = Person("Willy Wonka", wonkaInc);
    value umpaLumpa = Person("Umpa lumpa", wonkaInc);
    wonkaInc.owner = willy;
    
    value dc = deserialization<String>();
    
    dc.attribute("ww", `value Person.name`, "wwn");
    dc.attribute("ww", `value Person.employer`, "wi");
    dc.attribute("ul", `value Person.name`, "uln");
    dc.attribute("ul", `value Person.employer`, "wi");
    dc.attribute("wi", `value Company.name`, "win");
    dc.attribute("wi", `value Company.owner`, "ww");
    
    dc.instanceValue("win", "Wonka Inc.");
    dc.instanceValue("wwn", "Willy Wonka");
    dc.instanceValue("uln", "Umpa lumpa");
    
    dc.instance("wi", `Company`);
    dc.instance("ww", `Person`);
    dc.instance("ul", `Person`);
    
    value wonkaInc2 = dc.reconstruct<Company>("wi");
    value willy2 = dc.reconstruct<Person>("ww");
    value umpaLumpa2 = dc.reconstruct<Person>("ul");
    
    assert(wonkaInc2.owner === willy2);
    assert(willy2.employer === wonkaInc2);
    assert(umpaLumpa2.employer === wonkaInc2);
}

@test
shared void deserializationWhitelist() {
    void do(variable DeserializationContext<Integer> dc) {
        dc.attribute(2, `value Container.element`, 1);
        dc.instanceValue(1, "hello, world");
        dc.instance(2, `Container<String>`);
        variable Container<String> reconstructed = dc.reconstruct<Container<String>>(2);
        assert(reconstructed.element == "hello, world");
    }
    
    variable value dc = deserialization<Integer>(function (ClassModel<> clazz) 
        => let (m = clazz.declaration.containingModule)
                m == `module` || m == `module ceylon.language`);
    do(dc);
    
    dc = deserialization<Integer>(function (ClassModel<> clazz) 
        => let (m = clazz.declaration.containingModule) m == `module ceylon.language`);
    try {
        do(dc);
        throw;
    } catch (DeserializationException e) {
        assert(e.message == "class not whitelisted: serialization::Container<ceylon.language::String> for instance with id 2");
    }
    
}

shared void run() {
    testDeserializationOfObject();
    testDeserializationOfObject2();
    testDeserializationOfMemberObject();
    testDeserializationOfArrays();
}
shared void test() { run(); }

