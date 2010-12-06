
doc "foobar"
doc "baz"
public class Hello() {
    
    void run(Process p) {
        Sequence<String>? annos = getType().annotations(String);
        if (exists annos) {
            mutable Iterator<String> iter = annos.iterator();
            while (exists String anno = iter.head()) {
                p.writeLine(anno);
                iter := iter.tail();
            }
        }
    }
}
