@noanno
class StaticAttribute {
    
    static String nonSharedStat=nothing;
    shared static String sharedStat=nothing;
    static String nonSharedStatGetter=>nothing;
    shared static String sharedStatGetter=>nothing;
    static String nonSharedStatGetter2 {
        return nothing;
    }
    shared static String sharedStatGetter2 {
        return nothing;
    }
    
    static variable String nonSharedVariableStat=nothing;
    shared variable static String sharedVariableStat=nothing;
    static String nonSharedVariableStatGetter=>nothing;
    assign nonSharedVariableStatGetter {}
    shared static String sharedVariableStatGetter=>nothing;
    assign sharedVariableStatGetter {}
    static String nonSharedVariableStatGetter2 {
        return nothing;
    }
    assign nonSharedVariableStatGetter2 {}
    shared static String sharedVariableStatGetter2 {
        return nothing;
    }
    assign sharedVariableStatGetter2 {}
    
    shared new() {
    }
}
void staticAttributeGeneric() {
    variable value s = StaticAttribute.sharedStat;
    s = StaticAttribute.sharedStatGetter;
    s = StaticAttribute.sharedStatGetter2;
    
    s = StaticAttribute.sharedVariableStat;
    s = StaticAttribute.sharedVariableStatGetter;
    s = StaticAttribute.sharedVariableStatGetter2;
    
    StaticAttribute.sharedVariableStat = s;
    StaticAttribute.sharedVariableStatGetter = s;
    StaticAttribute.sharedVariableStatGetter2 = s;
}
