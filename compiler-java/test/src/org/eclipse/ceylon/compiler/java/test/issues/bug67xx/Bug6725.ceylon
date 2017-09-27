import java.util { Arrays }

@noanno
void bug6725(){
    Arrays.asList("hello", "world", "goodbye", 1, 100)
            .stream()
            .filter((s) => switch (s) 
                            case (is String) 
                                s.longerThan(2) 
                            case (is Integer) 
                                s>10)
            .forEach((s) => print(s));
    Arrays.asList(*interleave(0..30, 'a'..'z'))
            .forEach((x) {
        switch (x)
        case (is Integer) {
            print("Integer: ``x``");
        }
        case (is Character) {
            print("Character: ``x`` ``x.uppercased`` ``x.integer``");
        }
    });
    value int = Arrays.asList(1, 2, "").get(0);
    switch (int)
    case (is String) {}
    case (is Integer) {
        print(int);
    }
    else {}
    
    if (is String int) {}
    else if (is Integer int) {}
    else {
        print(int);
        Integer i = int;
    }
}
