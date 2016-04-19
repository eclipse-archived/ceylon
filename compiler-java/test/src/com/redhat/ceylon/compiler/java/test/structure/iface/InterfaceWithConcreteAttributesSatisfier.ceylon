class InterfaceWithConcreteAttributesSatisfier() satisfies InterfaceWithConcreteAttributes<Integer> {
    shared actual Integer formalAttribute {
        return 1;
    }
    shared actual Integer formalAttribute2 {
        return 2;
    }
    assign formalAttribute2 {
        
    }
}