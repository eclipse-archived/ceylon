package ceylon.modules.spi;

/**
 * Represents a command-line argument
 * 
 * @author Stephane Epardaud stef@epardaud.fr
 */
public enum Argument {

    EXECUTABLE("executable", ArgumentType.IMPL, 1),
    CACHE_CONTENT("cache_content", ArgumentType.IMPL, 0),
    IMPLEMENTATION("impl", ArgumentType.IMPL, 2),
    RUN("run", ArgumentType.CEYLON, 1),
    REPOSITORY("rep", ArgumentType.CEYLON, 1),
    SOURCE("src", ArgumentType.CEYLON, 1);

    private String value;
    private int requiredValues = 0;
    private ArgumentType argumentType;

    Argument(String value, ArgumentType type, int requiredValues) {
        this.value = value;
        this.argumentType = type;
        this.requiredValues = requiredValues;
    }

    public int getRequiredValues(){
        return requiredValues;
    }

    public static Argument forArgumentName(String name, ArgumentType type){
        for(Argument argument : Argument.values())
            if(argument.value.equals(name) && argument.argumentType == type)
                return argument;
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
