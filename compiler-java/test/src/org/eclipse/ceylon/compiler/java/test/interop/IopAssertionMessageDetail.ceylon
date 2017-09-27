import java.util{JList=List, Arrays}

void assertionMessageDetail() {
    // Test the assertion message works with non-refified types
    value j = Arrays.asList("a");
    try {
        assert(is JList<String> j);
    } catch (AssertionError e) {
        assert(e.message.contains("right-hand expression has class java.util::Arrays.ArrayList"));
    }
    try {
        assert(["a"] == j);
    } catch (AssertionError e) {
        assert(e.message.contains("right-hand expression has class java.util::Arrays.ArrayList"));
    }
}