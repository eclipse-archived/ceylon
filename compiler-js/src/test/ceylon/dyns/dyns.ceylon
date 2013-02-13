//Test the dynamic annotation
shared void test() {
}

void nogo() {
    dynamic {
        value d = require("util");
        d.name = "Gavin";
        Integer res = d.add(1, 3);
        Object? first = d.children[0];
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
    }
}
