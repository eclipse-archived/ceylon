shared abstract class Case(String caseName) 
        satisfies Matcher<Object> {
    
    shared actual Boolean matches(Object that) {
        return this===that;
    }
    
    shared actual default String string = caseName;
    
}