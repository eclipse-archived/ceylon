void inlineExpressions(Integer? arg, Boolean bool, Integer|Float num) {
    
    @type:"Integer|Float" value someStuff 
            = if (exists arg) 
                then arg else 0.0;
    
    @type:"Float" value otherStuff 
            = if (exists arg) 
                then 1.0 else 0.0;
    
    @type:"String" value moreStuff 
            = switch (bool) 
                case (true) "bar" 
                case (false) "foo";
    
    @type:"Boolean|Null" value someMoreStuff 
            = switch (bool) 
                case (true) true 
                case (false) null;

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
