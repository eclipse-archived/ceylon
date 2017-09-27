class StaticAttributeGeneric<T> {
    
    static T nonSharedStat=nothing;
    shared static T sharedStat=nothing;
    static T nonSharedStatGetter=>nothing;
    shared static T sharedStatGetter=>nothing;
    static T nonSharedStatGetter2 {
        return nothing;
    }
    shared static T sharedStatGetter2 {
        return nothing;
    }
    
    //static variable String nonSharedVariableStat=nothing;
    //shared variable static String sharedVariableStat=nothing;
    static T nonSharedVariableStatGetter=>nothing;
    assign nonSharedVariableStatGetter {}
    shared static T sharedVariableStatGetter=>nothing;
    assign sharedVariableStatGetter {}
    static T nonSharedVariableStatGetter2 {
        return nothing;
    }
    assign nonSharedVariableStatGetter2 {}
    shared static T sharedVariableStatGetter2 {
        return nothing;
    }
    assign sharedVariableStatGetter2 {}
    
    
    
    shared new() {
    }
}
void staticAttributeGeneric() {
    variable value s = StaticAttributeGeneric<String>.sharedStat;
    s = StaticAttributeGeneric<String>.sharedStatGetter;
    s = StaticAttributeGeneric<String>.sharedStatGetter2;
    
    //s = StaticAttributeGeneric<String>.sharedVariableStat;
    s = StaticAttributeGeneric<String>.sharedVariableStatGetter;
    s = StaticAttributeGeneric<String>.sharedVariableStatGetter2;
    
    //StaticAttributeGeneric<String>.sharedVariableStat = s;
    StaticAttributeGeneric<String>.sharedVariableStatGetter = s;
    StaticAttributeGeneric<String>.sharedVariableStatGetter2 = s;
}
