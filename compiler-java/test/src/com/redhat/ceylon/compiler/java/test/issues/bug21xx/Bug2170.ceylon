@noanno
shared void bug2170() {
    print(switch (int = 3)
        case (1) "one ``int``"
        case (2) "two"
        else "``int``");
}