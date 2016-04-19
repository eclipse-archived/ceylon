interface InterfaceWithConcreteAttributes<T> {
    Integer nonSharedAttribute {
        return 1;
    }
    Integer nonSharedAttribute2 => 1;
    
    shared Integer sharedAttribute { 
        return 1;
    }
    shared Integer sharedAttribute2 => 1;
    shared Integer defaultAttribute => 1;
    assign defaultAttribute {
        //this.formalAttribute = defaultAttribute;
    }
    
    shared formal Integer formalAttribute;
    shared formal variable Integer formalAttribute2;
    
}