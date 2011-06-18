package ceylon.language;

public interface Ordinal<Other extends Ordinal<Other>>
 extends Equality {
    public Other getSuccessor();
    public Other getPredecessor();
}
