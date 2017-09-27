import java.util.stream { Stream, Collectors }

@noanno
void bug6706(){
    value list
            = Stream.\iof("hello", "world", "goodbye", null)
            .filter((s) => if (exists s) then s.longerThan(2) else false)
            .filter((s) => s.longerThan(2))
            .collect(Collectors.toList<String>());
}