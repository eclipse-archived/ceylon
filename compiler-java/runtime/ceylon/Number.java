package ceylon;

public interface Number {
    @Extension
    public ceylon.Natural natural();

    @Extension
    public ceylon.Integer integer();
}
