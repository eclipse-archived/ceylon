import java.lang{JIterable=Iterable}
import java.util{JIterator=Iterator}

shared class A() satisfies JIterable<String>{
    shared JIterable<String> it => nothing;
    shared actual JIterator<String> iterator() => nothing;
}