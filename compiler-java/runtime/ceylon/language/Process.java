package ceylon.language;

public class Process extends Object {
    public void writeLine(ceylon.language.String s) {
        java.lang.System.out.println(s.toJavaString());
    }
    public void write(ceylon.language.String s) {
        java.lang.System.out.print(s.toJavaString());
    }
}
