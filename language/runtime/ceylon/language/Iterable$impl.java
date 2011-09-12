package ceylon.language;


public class Iterable$impl {
    public static <Element> boolean getEmpty(Iterable<Element> $this){
        return $this.getFirst() == null;
    }
    public static <Element> Element getFirst(Iterable<Element> $this){
        return $this.getIterator().getHead();
    }

}
