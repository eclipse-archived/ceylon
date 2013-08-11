class Assignment() {
    variable String name="gavin";
    this.name="stef";
    name="tako";
    Assignment().name="emmanuel";
    value count { return 0; }
    assign count {}
    count=1;
    function hello() => "hello";
    @error hello="goodbye";
    value org => "JBoss";
    @error org="Red Hat";
}

class SequencedArguments() {
    void print(String s, String* strings) {}
    void printAll(String[] strings) {}
    void printSum(Integer* n) {}
    
    String[] names = [ "stef", "tako" ];
    
    print(" ", "hello", "world");
    print(" ", *names);
    print(" ", "hello", "world", *names);
    printSum(1,2,3);
    printSum(*[1,2,3]);
    @error print(" ", names);
    @error print(" ", "hello", names, "world");
    @error print(" ", *"hello");
    @error print(" ", "hello", *"world");
    @error print(" ", "hello", names, "world", *names);
    @error print(*" ");
    @error printSum(*1);
    printAll(names);
    @error printAll(*names);
}

class SequencedArgumentsAgain() {
    String[] strings = ["world", "moon"];
    void printAll(Integer i, String* strings) {}
    printAll(1, "hello", "goodbye", *strings);
    @error printAll();
    printAll(2);
    printAll(1, "hello", "goodbye", *strings);
    @error printAll(1, 0, "goodbye", *strings);
}

class NonEmptySequencedArguments() {
    void printAll(String+ strings) {}
    void broken(String s="", @error String+ ss) {}
    printAll("hello");
    printAll("hello", "world");
    @error printAll();
    String[] strings1 = [];
    [String+] strings2 = ["hello"];
    @error printAll(*strings1);
    printAll(*strings2);
    printAll("", *strings1);    
    @error printAll(for (s in strings1) s);
    printAll(for (s in strings2) s);
}