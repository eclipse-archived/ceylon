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
}

@test
shared void testSerializeBasics() {
    value sc = serialization();
    variable value id = 0;
    sc.reference(id++, 1);
    sc.reference(id++, 1.0);
    sc.reference(id++, "");
    sc.reference(id++, '\{#00}');
    sc.reference(id++, null);
    sc.reference(id++, true);
    sc.reference(id++, false);
    sc.reference(id++, Container<Integer>(1));
}

@test
"it's not OK to register different instances against the same id"
shared void testSerializationRegisterTwice() {
    value sc = serialization();
    variable value id = 0;
    sc.reference(id, 1);
    try {
        sc.reference(id, 1);
        throw Exception("Expected an error");
    } catch (AssertionError e) {
        //print(e.message);
        //assert(e.message == "A different instance has already been registered with id 0: \"1\", \"1\"");
    }
}

@test
"it is OK to register the same instance twice with different ids"
shared void testSerializationRegisterTwice3() {
    value sc = serialization();
    variable value id = 0;
    value instance = 1;
    sc.reference(0, instance);
    sc.reference(1, instance);
}

@test
"deserialize a Container that references an integer"
shared void testBasicDeserializationOfObject() {
    value dc = deserialization();
    value intRef = dc.reference(0, `Container<Integer>`);
    assert(is DeserializableReference<Container<Integer>> intRef);
    value rIntRef = intRef.deserialize{
        object deconstructed satisfies Deconstructed {
            shared actual Iterator<[ValueDeclaration, Anything]> iterator() => {[`value Container.element`, 1]}.iterator();
            shared actual Reference<Instance>? getOuterInstance<Instance>() => null;
            
            shared actual Type getTypeArgument(TypeParameter typeParameter) {
                assert(exists element = `Container<Integer>`.declaration.getTypeParameterDeclaration("Element"));
                assert (typeParameter == element);
                assert(is TypeParameter->Type<Anything> result =`Container<Integer>`.typeArguments.first);
                return result.item;
            }
            
            shared actual Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute) {
                assert (attribute == `value Container.element`);
                assert(is Instance|Reference<Instance> result = 1);
                return result;
            }
            
            shared actual Instance|Reference<Instance> getElement<Instance>(Integer index) => nothing;
        }
    };
    assert(1 == rIntRef.instance().element);
    
}

@test
"deserialize an array that references an integer"
shared void testBasicDeserializationOfArray() {
    value dc = deserialization();
    value intRef = dc.reference(0, `Array<Integer>`);
    assert(is DeserializableReference<Array<Integer>> intRef);
    value rIntRef = intRef.deserialize{
        object deconstructed satisfies Deconstructed {
            shared actual Iterator<[ValueDeclaration, Anything]> iterator() => {[`value Array.size`, 1]}.iterator();
            shared actual Reference<Instance>? getOuterInstance<Instance>() => null;
            
            shared actual Type getTypeArgument(TypeParameter typeParameter) {
                assert(exists element = `Array<Integer>`.declaration.getTypeParameterDeclaration("Element"));
                assert (typeParameter == element);
                assert(is TypeParameter->Type<Anything> result =`Array<Integer>`.typeArguments.first);
                return result.item;
            }
            
            shared actual Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute) {
                assert (attribute == `value Array.size`);
                assert(is Instance|Reference<Instance> result = 1);
                return result;
            }
            
            shared actual Instance|Reference<Instance> getElement<Instance>(Integer index) {
                assert(index == 0);
                assert(is Instance|Reference<Instance> result = 1);
                return result;
            }
        }
    };
    value array = rIntRef.instance();
    assert(array.size == 1);
    assert(exists first = array[0],
        1 == first);
    
}

@test
"its not OK to register the same id with different classes"
shared void testDerserializationRegisterTwice() {
    // TODO this is not symmetric with the serialization case (where registration is strictly once only)
    value dc = deserialization();
    value intRef = dc.reference(0, `Container<Integer>`);
    try {
        dc.reference(0, `Container<String>`);
        throw Exception("Expecting an error");
    } catch (AssertionError e) {
        assert("reference already made to instance with a different class" == e.message);
    }
}

@test
shared void testDerserializationDeserializeTwice() {
    value dc = deserialization();
    value intRef = dc.reference(0, `Array<Integer>`);
    assert(is DeserializableReference<Array<Integer>> intRef);
    object deconstructed satisfies Deconstructed {
        shared actual Iterator<[ValueDeclaration, Anything]> iterator() => {[`value Array.size`, 1]}.iterator();
        shared actual Reference<Instance>? getOuterInstance<Instance>() => null;
        
        shared actual Type getTypeArgument(TypeParameter typeParameter) {
            assert(exists element = `Array<Integer>`.declaration.getTypeParameterDeclaration("Element"));
            assert (typeParameter == element);
            assert(exists result =`Array<Integer>`.typeArguments.first);
            return result.item;
        }
        
        shared actual Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute) {
            assert (attribute == `value Array.size`);
            assert(is Instance|Reference<Instance> result = 1);
            return result;
        }
        
        shared actual Instance|Reference<Instance> getElement<Instance>(Integer index) {
            assert(index == 0);
            assert(is Instance|Reference<Instance> result = 1);
            return result;
        }
    }
    value rIntRef = intRef.deserialize(deconstructed);
    try {
        intRef.deserialize(deconstructed);
        throw Exception("expected an error");
    } catch (AssertionError e) {
        print(e.message);
    }
}

@test
"it's OK to call instance twice, but you get back exactly the same instance"
shared void testDerserializationInstanceTwice() {
    value dc = deserialization();
    value intRef = dc.reference(0, `Container<Integer>`);
    assert(is DeserializableReference<Container<Integer>> intRef);
    value rIntRef = intRef.deserialize{
        object deconstructed satisfies Deconstructed {
            shared actual Iterator<[ValueDeclaration, Anything]> iterator() => {[`value Container.element`, 1]}.iterator();
            shared actual Reference<Instance>? getOuterInstance<Instance>() => null;
            
            shared actual Type getTypeArgument(TypeParameter typeParameter) {
                assert(exists element = `Container<Integer>`.declaration.getTypeParameterDeclaration("Element"));
                assert (typeParameter == element);
                assert(exists result =`Container<Integer>`.typeArguments.first);
                return result.item;
            }
            
            shared actual Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute) {
                assert (attribute == `value Container.element`);
                assert(is Instance|Reference<Instance> result = 1);
                return result;
            }
            
            shared actual Instance|Reference<Instance> getElement<Instance>(Integer index) => nothing;
        }
    };
    assert(rIntRef.instance() === rIntRef.instance());
    
}

shared void run() {
    testSerializeBasics();
    testSerializationRegisterTwice();
    testSerializationRegisterTwice3();
    testBasicDeserializationOfObject();
    testBasicDeserializationOfArray();
    testDerserializationRegisterTwice();
    testDerserializationDeserializeTwice();
    testDerserializationInstanceTwice();
}
shared void test() { run(); }

