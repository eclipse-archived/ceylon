import java.lang{JIterable=Iterable}
import java.util{JIterator=Iterator}
import java.util.\ifunction{Consumer}

@noanno
class RefineDefaultInterfaceMethod() satisfies JIterable<String> {
    shared actual JIterator<String> iterator() => nothing;
    
    shared actual void forEach(Consumer<in String> consumer) {
        super.forEach(consumer);
        this.forEach(consumer);
    }
}