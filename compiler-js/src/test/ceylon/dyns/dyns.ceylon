//Test the dynamic annotation
shared void test() {
}

void nogo() {
    dynamic {
        value d = require("util");
        d.name = "Gavin";
        Integer res = d.add(1, 3);
        value first = d.children[0];
        if (exists first) {
            Integer o = first;
        }
        d.children.set(1, 4);
        String name = d.name;
        value x = d + d + 10;
        d.count++;
        d.count--;
        Boolean b = d.adult && d.male;
        value range = d.start..d.end;
        Boolean lt = d.age<10 || d.age>5 || d.age<=100 || d.age>=500;
        if (d.something == 5 || d.other != 10) {}
        d.name = null;
        value n = new { x=3; y="hello"; };
        value n2 = new (3, "hello");
        function f(value z) => z else x;
        f(1);
        f{z="z";};
    }
}
