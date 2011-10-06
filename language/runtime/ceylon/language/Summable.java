package ceylon.language;

public interface Summable<Other extends Summable<Other>> {
    public Other plus(Other number);
}
