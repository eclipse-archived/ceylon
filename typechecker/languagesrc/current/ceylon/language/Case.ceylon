shared abstract class Case(String caseName)
        satisfies Matcher<Case> { //TODO: should this be Matcher<IdentifiableObject>?
    
    shared actual Boolean matches(Case that) {
        return this==that;
    }
    
    shared actual Boolean equals(Object that) {
        return super.equals(that);
    }
    
    shared actual default String string = caseName;
    
}