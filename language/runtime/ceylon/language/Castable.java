package ceylon.language;

public interface Castable<Types> {
    <CastValue extends Types> CastValue as();
}
