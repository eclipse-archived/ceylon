@noanno
class StaticAttribute {
    
    static String nonSharedStat="I'm static";
    shared static String sharedStat="I'm static";
    static String nonSharedStatGetter=>"I'm static";
    shared static String sharedStatGetter=>"I'm static";
    static String nonSharedStatGetter2 {
        return "I'm static";
    }
    shared static String sharedStatGetter2 {
        return "I'm static";
    }
    
    static variable String nonSharedVariableStat="I'm static";
    shared variable static String sharedVariableStat="I'm static";
    static String nonSharedVariableStatGetter=>"I'm static";
    assign nonSharedVariableStatGetter {}
    shared static String sharedVariableStatGetter=>"I'm static";
    assign sharedVariableStatGetter {}
    static String nonSharedVariableStatGetter2 {
        return "I'm static";
    }
    assign nonSharedVariableStatGetter2 {}
    shared static String sharedVariableStatGetter2 {
        return "I'm static";
    }
    assign sharedVariableStatGetter2 {}
    
    shared new() {
    }
}
void staticAttribute() {
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
