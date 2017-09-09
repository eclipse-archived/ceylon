interface StaticInterfaceMembers {
    static Integer int = 0;
    static Integer get() => int;
    static Integer val = get();
}

class StaticClassMembers {
    static Integer int = 0;
    static Integer get() => int;
    static Integer val = get();
    new () {}
}