import java.lang{JIterable=Iterable}
import java.util{JIterator=Iterator}

@noanno
class SatisfyInterfaceWithDefaultMethod() satisfies JIterable<String> {
    shared actual JIterator<String> iterator() => nothing;
    
    void use() {
        forEach(nothing);
    }
}