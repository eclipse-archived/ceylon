import ceylon.language.meta.model { Attribute }

@test
shared void bug534() {
    class Command(){}
    
    class CommandValue(){
        shared variable Boolean bool = false; 
        shared variable Command command = Command(); 
    }
    
    class Opt(){
        shared String bind<OptValue>(
            Attribute<OptValue, Boolean?, Boolean> attr
        ) => "";
    }
    
    for(String s in {Opt().bind(`CommandValue.bool`)}) {}   
}
