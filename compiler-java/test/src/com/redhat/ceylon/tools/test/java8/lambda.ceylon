import java.lang{Iterable}
import java.util{Iterator}

suppressWarnings("expressionTypeNothing", "unusedDeclaration")
void f() {
    object it satisfies Iterable<String> {
        shared actual Iterator<String> iterator() => nothing;
    }
    value split = it.spliterator();
     
}