void inlineExpressions(Integer? arg, Boolean bool) {
    
    @type:"Integer|Float" value someStuff 
            = if (exists arg) 
                then arg else 0.0;
    
    @type:"String" value moreStuff 
            = switch (bool) 
                case (true) "bar" 
                case (false) "foo";
    
    @type:"Basic&Category<String>" value xxx 
            = object extends Basic() 
            satisfies Category<String> {
        contains(String that) => !that.empty;
    };
    
    print(if (exists arg) then arg else 0.0);
    print(switch (bool) case (true) "bar" case (false) "foo");
    print(object satisfies Category<String> {
        contains(String that) => !that.empty;
    });
    
    print { val = if (exists arg) then arg else 0.0; };
    print { if (exists arg) then arg else 0.0; };
    print { val = switch (bool) case (true) "bar" case (false) "foo"; };
    print { switch (bool) case (true) "bar" case (false) "foo"; };
    print { 
        object satisfies Category<String> {
            contains(String that) => !that.empty;
        };
    };
    print { 
        val = object satisfies Category<String> {
            contains(String that) => !that.empty;
        };
    };
}
