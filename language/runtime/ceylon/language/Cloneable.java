package ceylon.language;

public interface Cloneable<Clone extends Cloneable<Clone>> {
    public Clone getClone();
}
