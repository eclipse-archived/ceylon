package ceylon.language;

public interface FixedSlots<Other extends FixedSlots<Other>>
        extends Slots<Other> {

    public Other getComplement();

}