package ceylon.language;


public class Iterable$impl {
    public static <Element> boolean getEmpty(Iterable<Element> $this){
        return $this.getIterator() == null;
    }
}
