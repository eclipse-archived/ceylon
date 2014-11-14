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

    print(switch (bool) case (true) "bar" case (false) "foo");
    
    @type:"String|Float" value a1 = 
            if (exists arg) then arg.string else 0.0;
    @type:"String" value s1 
            = switch (arg) case (is Null) "null" case (is Integer) arg.string;
    
    @type:"String|Float" value a2 = 
            if (exists a=arg) then a.string else 0.0;
    @type:"String" value s2 
            = switch (a=arg) case (is Null) "null" case (is Integer) a.string;
    
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
