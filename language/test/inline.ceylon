@test
shared void testInlineExpressions() {
    Integer|String str = "xxx";
    Integer|String int = 1;
    //if expressions
    check((if (is String str) then str.size else str)==3, "inline if 1");
    check((if (is Integer str) then str else str.size)==3, "inline if 2");
    check((if (is String int) then int.size else int)==1, "inline if 3");
    check((if (is Integer int) then int else int.size)==1, "inline if 4");
    //ceylon-js#531
    check((if (is Integer int) then int else if (true) then int.size else int.size)==1, "inline if 5 (if/else if/else)");
    Object o531="X";
    check((if (is Integer o531) then o531 else
           if (is String o531) then o531.size else
           if (is Anything[] o531) then o531.size else
           if (true) then 2 else 3)==1, "inline if 6");
    check((if (is Integer o531) then o531 else
           if (is String o531) then o531.size else
           if (is Anything[] o531) then o531.size else
           if (false) then 2 else 3)==1, "inline if 7");
    check((if (is Integer o531) then o531 else
           if (is Anything[] o531) then o531.size else
           if (is String o531) then o531.size else
           if (false) then 2 else 3)==1, "inline if 8");
    check((if (is Integer o531) then o531 else
           if (is Anything[] o531) then o531.size else
           if (is Character o531) then o531.integer else
           if (true) then 2 else 3)==2, "inline if 9");
    check((if (is Integer o531) then o531 else
           if (is Anything[] o531) then o531.size else
           if (is Character o531) then o531.integer else
           if (false) then 2 else 3)==3, "inline if 10");
    for (i in 1..4) {
        check(if (i%2==0) then true else i%2==1, "If expressions 1");
    }
    for (i in ["Hey", null]) {
        check(if (is String i) then i.uppercased=="HEY" else true, "If expressions 2");
        check(if (exists i) then i.reversed=="yeH" else true, "If expressions 3");
    }
    Object obj="Hey";
    //TODO wasn't the "else" optional?
    check(!(if (is Integer obj) then obj.successor else null) exists, "If expressions 4");
    check(if (is String obj, exists c=obj[2], c=='y') then true else false, "If expression with multiple conditions");
    check(if (obj=="HEY") then false else if (obj==1) then false else true, "If-else-if expression 2");
    check(if (is Integer obj) then false else if (is String obj, exists c=obj[2], c=='y') then true else false, "if-else-if expression 2");
    check(if (obj=="nay") then false else if (is String obj, exists c=obj[2], c=='y') then true else false, "if-else-if expression 3");

    //Switch expressions
    check((switch (str) case (is String) str.size else str)==3, "inline switch 1");
    check((switch (str) case (is Integer) str else str.size)==3, "inline switch 2");
    check((switch (int) case (is String) int.size else int)==1, "inline switch 3");
    check((switch (int) case (is Integer) int else int.size)==1, "inline switch 4");
    Boolean testSwitchExpression(Anything x) =>
        switch(x)
        case (is Integer) x==1
        case (is Null) false
        else x is String;
    check(testSwitchExpression(1), "Switch expression 1");
    check(testSwitchExpression("X"), "Switch expression 2");
    check(!testSwitchExpression(null), "Switch expression 3");

    //Let expressions
    value x=2;
    value y=3;
    check(let (dist = x^2+y^2) [x+dist,y+dist] == [15,16], "Let expr 1");
    check((let (e="K"->1, k=e.key, v=e.item) [v,k]) == [1,"K"], "Let expr 1");
    check((let (e="K"->1, k=e.key, v=k.lowercased) k->v) == "K"->"k", "Let expr 2");

    //Object expressions
    Iterator<T> objectExpression1<T>(T element)
        => object satisfies Iterator<T> {
               variable T|Finished e=element;
               shared actual T|Finished next() {
                   value c=e;
                   e=finished;
                   return c;
               }
    };

    check(objectExpression1("Hey").next()=="Hey", "Object expressions 1");
    check((object {
        hash=449;
    }).hash==449, "Object expressions 2");
    check("``(object {
        string="HEY";
    })``"=="HEY", "Object expressions 3");
}
