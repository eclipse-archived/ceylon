package ceylon.language;

public class $true {
    private final static Boolean value = new Boolean(String.instance("true")){
			@Override
			public boolean booleanValue() {
				return true;
			}
		}; 

    public static Boolean getTrue(){
        return value;
    }
}
