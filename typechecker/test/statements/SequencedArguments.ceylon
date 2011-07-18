class Assignment() {
    variable String name:="gavin";
    this.name:="stef";
    name:="tako";
    Assignment().name:="emmanuel";
    value count { return 0; }
    assign count {}
    count:=1;
}

class SequencedArguments() {
    
    void print(String s, String... strings) {}
    void printAll(String[] strings) {}
    void printSum(Natural... n) {}
    
    String[] names = { "stef", "tako" };
    
    print(" ", "hello", "world");
    print(" ", names...);
    printSum(1,2,3);
    printSum({1,2,3}...);
    @error print(" ", names);
    @error print(" ", "hello"...);
    @error print(" ", "hello", "world"...);
    @error print(" "...);
    @error printSum(1...);
    printAll(names);
    @error printAll(names...);
}