package ceylon.language;

public interface Slots<Other extends Slots<Other>> {     
    public Other or(Other bits);
    public Other and(Other bits);
    public Other xor(Other bits);
    public Other complementIn(Other bits);
}
