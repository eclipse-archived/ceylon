class Assignment() {
    variable String name="gavin";
    this.name="stef";
    name="tako";
    Assignment().name="emmanuel";
    value count { return 0; }
    assign count {}
    count=1;
    function hello() { return "hello"; }
    @error hello="goodbye";
    value org { return "JBoss"; }
    @error org="Red Hat";
}

class SequencedArguments() {
    
    void print(String s, String... strings) {}
    void printAll(String[] strings) {}
    void printSum(Integer... n) {}
    
    String[] names = { "stef", "tako" };
    
    print(" ", "hello", "world");
    print(" ", names...);
    print(" ", "hello", "world", names...);
    printSum(1,2,3);
    printSum({1,2,3}...);
    @error print(" ", names);
    @error print(" ", "hello", names, "world");
    @error print(" ", "hello"...);
    @error print(" ", "hello", "world"...);
    @error print(" ", "hello", names, "world", names...);
    @error print(" "...);
    @error printSum(1...);
    printAll(names);
    @error printAll(names...);
}