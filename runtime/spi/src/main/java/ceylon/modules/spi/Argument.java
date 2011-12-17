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
    SOURCE("src", ArgumentType.CEYLON, 1), 
    HELP("help", ArgumentType.CEYLON, 0, "h", "-help"), 
    VERSION("version", ArgumentType.CEYLON, 0, "v", "-version");

    private String value;
    private int requiredValues = 0;
    private ArgumentType argumentType;
    private String[] aliases;

    Argument(String value, ArgumentType type, int requiredValues, String... aliases) {
        this.value = value;
        this.argumentType = type;
        this.requiredValues = requiredValues;
        this.aliases = aliases;
    }

    public int getRequiredValues(){
        return requiredValues;
    }

    public static Argument forArgumentName(String name, ArgumentType type){
        for(Argument argument : Argument.values()){
            if(argument.argumentType != type)
                continue;
            if(argument.value.equals(name))
                return argument;
            for(String alias : argument.aliases)
                if(alias.equals(name))
                    return argument;
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
