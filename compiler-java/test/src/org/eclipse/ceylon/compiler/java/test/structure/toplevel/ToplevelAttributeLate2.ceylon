shared late variable ToplevelAttributeLate2 toplevelAttributeLate2Obj;

shared class ToplevelAttributeLate2() {
    
    shared String someStringProperty = "";
    
}

shared void toplevelAttributeLate2() {  
    toplevelAttributeLate2Obj = ToplevelAttributeLate2();
    assert(toplevelAttributeLate2Obj.someStringProperty == ""); 
}