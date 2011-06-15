package ceylon.language;

public abstract class Boolean extends Case {

    public Boolean(String caseName) {
        super(caseName);
    }

    public static Boolean instance(boolean b) {
        return b ? _true.value : _false.value;
    }

}
