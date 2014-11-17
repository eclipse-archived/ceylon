void inlineExpressions(Integer? arg, Boolean bool, Integer|Float num) {
    
    @type:"Integer|Float" value someStuff 
            = if (exists arg) 
                then arg else 0.0;
    
    @type:"String" value moreStuff 
            = switch (bool) 
                case (true) "bar" 
                case (false) "foo";
    
    @error value moreStuffBroken
            = switch (bool) 
                case (true) "bar";
    
    @type:"Integer|Float" value evenMoreStuff 
            = switch (num) 
                case (is Integer) -num 
                case (is Float) num/2.0;
    
    @error value evenMoreStuffBroken
            = switch (num) 
                case (is Integer) -num;
    
    @type:"Basic&Category<String>" value xxx 
            = object extends Basic() 
            satisfies Category<String> {
        contains(String that) => !that.empty;
    };
    
    print(if (exists arg) then arg else 0.0);
    print(switch (bool) case (true) "bar" case (false) "foo");
    print(object satisfies Category<String> {
        print("creating object");
        contains(String that) => !that.empty;
    });
    
    print { val = if (exists arg) then arg else 0.0; };
    print { if (exists arg) then arg else 0.0; };
    print { val = switch (bool) case (true) "bar" case (false) "foo"; };
    print { switch (bool) case (true) "bar" case (false) "foo"; };
    print { 
        object extends Object() satisfies Category<String> {
            contains(String that) => !that.empty;
            equals(Object that) => false;
            hash => 0;
        };
    };
    print { 
        val = object extends Object() satisfies Category<String> {
            contains(String that) => !that.empty;
            equals(Object that) => false;
            hash => 0;
        };
    };
    @type:"Tuple<Basic,Basic,Tuple<Basic,Basic,Empty>>" value objs1 = [object{}, object{}];
    @type:"Array<Basic>" value objs2 = Array {object{}};
}
