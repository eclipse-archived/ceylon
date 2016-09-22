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
/*@noanno
class StaticAttributeSub extends StaticAttribute {
    
    shared static Integer sharedStat=1;
    
    shared new () {}
    
}*/
// TODO use site
