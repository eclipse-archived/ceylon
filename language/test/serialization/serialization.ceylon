import ceylon.language.serialization{...}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}
import ceylon.language.meta.model {
    Type
}

"A serializable class with a single attribute for test purposes."
serializable class Container<Element>(shared Element element) {
    shared serializable class Member() {
        shared Container<Element> container => outer;
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
// TODO tuple deserialization
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
shared void noInfo() {
    value dc = deserialization<Integer>();
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "unknown id: 1.");
    }
}

@test
shared void noClass() {
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
shared void insufficientStateObject() {
    value dc = deserialization<Integer>();
    dc.instance(1, `Cycle`);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "lacking sufficient state for instance with id 1: [serialization::Cycle.ref]");
    }
}

@test
shared void insufficientStateArray() {
    variable value dc = deserialization<Integer>();
    dc.instance(1, `Array<String>`);
    try {
        dc.reconstruct<Anything>(1);
        assert(false);
    } catch(DeserializationException e) {
        assert(e.message == "lacking sufficient state for instance with id 1: ceylon.language::Array.size");
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
shared void badAttributeType() {
    value dc = deserialization<Integer>();
    dc.instance(2, `Container<String>`);
    dc.attribute(2, `value Container.element`, 1);
    dc.instanceValue(1, 'c');
    try {
        value x =dc.reconstruct<Container<String>>(2);
        print(x.element);
        // XXX pb here is jvm see container has having a field of type
        // <upper bound of Element=Object>, so doesn't complain when we set the field value.
        // it would if the field was not a tp, but there' basically no check that
        // the value being set on a field is of the correct ceylon type as the 
        // ceylon Value 
        assert(false);
    } catch(DeserializationException e) {
        print(e.message);
        assert(e.message == "instance not assignable to attribute serialization::Container.element of id 2: Character is not assignable to String");
    }
}

@test
shared void badElementType() {
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
    shared String collides; 
}

@test
shared void attributeNamingCollision() {
    variable value dc = deserialization<Integer>();
    dc.instance(1, `CollisionSub`);
    dc.attribute(1, `class CollisionSuper`.getDeclaredMemberDeclaration<ValueDeclaration>("collides") else nothing, 2);
    dc.instanceValue(2, "super");
    dc.attribute(1, `value CollisionSub.collides`, 3);
    dc.instanceValue(3, "sub");
    
    variable CollisionSub reconstructed = dc.reconstruct<CollisionSub>(1);
    assert(reconstructed.string == "super");
    assert(reconstructed.collides == "sub");
}



shared void run() {
    testDeserializationOfObject();
    testDeserializationOfObject2();
    testDeserializationOfMemberObject();
    testDeserializationOfArrays();
}
shared void test() { run(); }

